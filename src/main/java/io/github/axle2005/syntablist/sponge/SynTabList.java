package io.github.axle2005.syntablist.sponge;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.tab.TabListEntry;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameLoadCompleteEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;
import org.spongepowered.api.plugin.Dependency;

import com.google.inject.Inject;

import java.nio.file.Path;
import java.util.Map;
import io.github.axle2005.syntablist.common.PlayerData;
import io.github.axle2005.syntablist.common.StaffData;
import io.github.axle2005.syntablist.common.Utils;
import io.github.axle2005.syntablist.common.ServerData;
import io.github.axle2005.syntablist.common.ServerData.State;
import io.github.axle2005.syntablist.sponge.commands.CommandRegister;
import io.github.axle2005.syntablist.sponge.listeners.ListenerRegister;
import io.github.axle2005.syntablist.sponge.listeners.ListenerServerStart;
import net.kaikk.mc.synx.SynX;
import net.kaikk.mc.synx.packets.ChannelListener;
import net.kaikk.mc.synx.packets.Packet;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import static io.github.axle2005.syntablist.sponge.RankTeams.*;

@Plugin(id = "syntablist", name = "SynTabList", version = "0.2", dependencies = @Dependency(id = "synx"))
public class SynTabList implements ChannelListener {

    @Inject
    private Logger log;

    @Inject
    @ConfigDir(sharedRoot = false)
    private Path defaultConfig;

    @Inject
    @DefaultConfig(sharedRoot = false)
    private ConfigurationLoader<CommentedConfigurationNode> configManager;

    Config config;

    private ListenerRegister events;
    private ListenerServerStart start;
    private Server server;
    private Optional<TabListEntry> tabListEntry;
    private Boolean globalStaff;
    private Boolean globalPlayer;
    TabListEntry entr;

    Map<UUID, PlayerData> playersData = new ConcurrentHashMap<>();
    Map<UUID, StaffData> staffsData = new ConcurrentHashMap<>();

    public static Scoreboard scoreboard = Scoreboard.builder().build();

    @Listener
    public void onServerStart(GameStartedServerEvent e) {
	registerTeams();
    }

    @Listener
    public void initialization(GameInitializationEvent event) {
	server = Sponge.getServer();

	config = new Config(this, defaultConfig, configManager);

	globalStaff = config.getNodeBoolean("Broadcast Globally,Options,Staff");
	globalPlayer = config.getNodeBoolean("Broadcast Globally,Options,Players");

	// Header Expands after 18 players
	TabListUtil.setHeader(
		Text.of(TextSerializers.formattingCode('&').deserialize(config.getNodeString("TabList,Header"))));
	TabListUtil.setFooter(
		Text.of(TextSerializers.formattingCode('&').deserialize(config.getNodeString("TabList,Footer"))));


	new CommandRegister(this);
	events = new ListenerRegister(this);
	start = new ListenerServerStart();

	if (!Utils.checkDummyFile("plugins/configs/syntablist")) {
	    ServerData serverData = new ServerData(State.CRASH);
	    SynX.instance().broadcast(Utils.getStateChannel(), serverData, System.currentTimeMillis() + 60000);
	}

    }

    @Listener
    public void onLoad(GameLoadCompleteEvent event) {

	SynX.instance().register(this, Utils.getChannel(), this);
	SynX.instance().register(this, Utils.getStateChannel(), start);

	events.registerEvent("Connect");
	events.registerEvent("Disconnect");

	SynX.instance().broadcast(Utils.getStateChannel(), new ServerData(State.START), System.currentTimeMillis() + 60000);

    }

    // Creates a Dummy file that gets checked on startup. If this file isn't
    // generated,
    // the plugin knows the server crashed.
    @Listener
    public void onStop(GameStoppingServerEvent event) {
	Utils.createDummyFile("plugins/configs/syntablist");
	SynX.instance().broadcast(Utils.getStateChannel(), new ServerData(State.STOP), System.currentTimeMillis() + 60000);
    }

    @Override
    public void onPacketReceived(Packet packet) {
	// This is the server that sent this packet

	// Let's get the object from the packet
	// final PlayerData playerData = packet.getObject(PlayerData.class);

	final Object data = packet.getObject();

	PlayerData playerData = (PlayerData) data;

	switch (playerData.getAction()) {
	case JOIN: {

	    if (playerData instanceof StaffData && globalStaff) {
		staffsData.put(playerData.getPlayerUUID(), (StaffData) playerData);
	    }
	    if (globalPlayer || !globalPlayer && getOnline(playerData)) {

		if (!playersData.containsKey(playerData.getPlayerUUID())) {
		    playersData.put(playerData.getPlayerUUID(), playerData);

		    for (Player player : server.getOnlinePlayers()) {
			tabListEntry = player.getTabList().getEntry(playerData.getPlayerUUID());
			/*
			 * if (tabListEntry.isPresent()) { entr =
			 * tabListEntry.get(); } else {
			 */

			if (!tabListEntry.isPresent()) {
			    entr = TabListUtil.addTabList(player.getTabList(), playerData.getPlayerUUID(),
				    Text.of(playerData.getPlayerName()));

			    player.getTabList().addEntry(entr);

			} else {
			    entr = tabListEntry.get();
			}
			isStaff(playerData, entr);
			// validate(player);

			player.setScoreboard(scoreboard);

		    }

		}

	    }

	    break;

	}
	case QUIT: {
	    playersData.remove(playerData.getPlayerUUID());

	    // Checks if the player has been removed from playersData
	    // (Logged out) and removes from tablist
	    for (Player player : server.getOnlinePlayers()) {
		TabListUtil.removeTabList(player.getTabList(), playerData.getPlayerUUID());

	    }

	    break;
	}
	}

    }

    private Boolean getOnline(PlayerData player) {
	Optional<Player> optP = Sponge.getServer().getPlayer(player.getPlayerUUID());
	if (optP.isPresent()) {
	    return optP.get().isOnline();
	}
	return false;

    }

    private void validate(Player player) {
	if (player.getTabList().getEntries().size() < playersData.size()) {
	    for (PlayerData pData : playersData.values()) {
		tabListEntry = player.getTabList().getEntry(pData.getPlayerUUID());
		if (tabListEntry.isPresent()) {
		    isStaff(pData, tabListEntry.get());
		} else {

		    entr = TabListUtil.addTabList(player.getTabList(), pData.getPlayerUUID(),
			    Text.of(pData.getPlayerName()));
		    player.getTabList().addEntry(entr);
		    isStaff(pData, entr);
		}
	    }
	}
    }

    public Scoreboard getScoreboard() {
	return scoreboard;
    }

    public Logger getLogger() {
	return log;
    }

    public static void isStaff(PlayerData playerData, TabListEntry entr) {
	if (playerData instanceof StaffData) {
	    handleData((StaffData) playerData, entr);
	} else {
	    handleData(playerData, entr);
	}
    }

    public static void handleData(PlayerData p, TabListEntry entry) {
	White.addMember(Text.of(p.getPlayerName()));
	entry.setDisplayName(Text.of(TextColors.WHITE, p.getPlayerName()));
    }

    public static void handleData(StaffData s, TabListEntry entry) {
	switch (s.getRank()) {
	case SENIORADMIN: {
	    entry.setDisplayName(Text.of(TextColors.DARK_GREEN, s.getPlayerName()));
	    DarkGreen.addMember(Text.of(s.getPlayerName()));
	    break;
	}
	case ADMIN: {
	    entry.setDisplayName(Text.of(TextColors.DARK_RED, s.getPlayerName()));
	    DarkBlue.addMember(Text.of(s.getPlayerName()));
	    break;
	}
	case MOD: {
	    entry.setDisplayName(Text.of(TextColors.AQUA, s.getPlayerName()));
	    Aqua.addMember(Text.of(s.getPlayerName()));
	    break;
	}
	case HELPER: {
	    entry.setDisplayName(Text.of(TextColors.LIGHT_PURPLE, s.getPlayerName()));
	    LightPurple.addMember(Text.of(s.getPlayerName()));
	    break;
	}
	case CHATMOD:
	    break;
	case DEVELOPER:
	    break;
	case HIDDEN:
	    break;
	case OPERATOR:
	    break;
	case OWNER:
	    break;
	case SENIORDEVELOPER:
	    break;
	case SENIORMOD:
	    break;
	}
    }

}
