package io.github.axle2005.syntablist.sponge;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.entity.living.player.tab.TabList;
import org.spongepowered.api.entity.living.player.tab.TabListEntry;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameLoadCompleteEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.profile.GameProfileManager;
import org.spongepowered.api.scheduler.Scheduler;
import org.spongepowered.api.scheduler.Task;
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
import io.github.axle2005.syntablist.common.PlayerData.Action;
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

    Scheduler scheduler = Sponge.getScheduler();
    Task.Builder taskBuilder = scheduler.createTaskBuilder();
    Task task = null;

    private ListenerRegister events;
    private ListenerServerStart start;
    private Server server;
    private GameProfileManager gpm;
    private Optional<TabListEntry> tabListEntry;
    private String channel;
    private String channelState = "State";
    private String nodeName;
    private Boolean globalStaff;
    private Boolean globalPlayer;

    Text tabHeader;
    Text tabFooter;
    Map<UUID, PlayerData> playersData = new ConcurrentHashMap<>();
    Map<UUID, StaffData> staffsData = new ConcurrentHashMap<>();
    Map<UUID, Text> rankData = new ConcurrentHashMap<>();

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

	tabHeader = Text.of(TextSerializers.formattingCode('&').deserialize(config.getNodeString("TabList,Header")));
	tabFooter = Text.of(TextSerializers.formattingCode('&').deserialize(config.getNodeString("TabList,Footer")));

	channel = "TabList";

	nodeName = SynX.instance().getNode().getName();

	new CommandRegister(this);
	events = new ListenerRegister(this);
	start = new ListenerServerStart(this);

	gpm = Sponge.getServer().getGameProfileManager();

	if (!Utils.checkDummyFile("plugins/configs/syntablist")) {
	    ServerData serverData = new ServerData(nodeName, State.CRASH);
	    SynX.instance().broadcast(channelState, serverData, System.currentTimeMillis() + 60000);
	}

    }

    @Listener
    public void onLoad(GameLoadCompleteEvent event) {

	SynX.instance().register(this, channel, this);
	SynX.instance().register(this, channelState, start);

	events.registerEvent("Connect");
	events.registerEvent("Disconnect");

	ServerData serverData = new ServerData(nodeName, State.START);
	SynX.instance().broadcast(channelState, serverData, System.currentTimeMillis() + 60000);

    }

    // Creates a Dummy file that gets checked on startup. If this file isn't
    // generated,
    // the plugin knows the server crashed.
    @Listener
    public void onStop(GameStoppingServerEvent event) {
	Utils.createDummyFile("plugins/configs/syntablist");
    }

    @Override
    public void onPacketReceived(Packet packet) {
	// This is the server that sent this packet
	final String sendingServer = packet.getFrom().getName();

	// Let's get the object from the packet
	// final PlayerData playerData = packet.getObject(PlayerData.class);

	final Object data = packet.getObject();

	PlayerData playerData = (PlayerData) data;

	switch (playerData.getAction()) {
	case JOIN: {
	    if ((playerData instanceof StaffData && globalStaff) || globalPlayer || !globalPlayer && getOnline(playerData)) {
		playersData.put(playerData.getPlayerUUID(), playerData);
	    }

	    break;
	}
	case QUIT: {
	    playersData.remove(playerData.getPlayerUUID());
	    break;
	}
	}

	task = taskBuilder.execute(() -> {
	    PlayerData pOnline;
	    for (Player player : server.getOnlinePlayers()) {
		
		/*if(!playersData.containsKey(player.getUniqueId()))
		{
		    pOnline = new PlayerData(player.getName(), player.getUniqueId(), Action.JOIN);
		    playersData.put(pOnline.getPlayerUUID(), pOnline);
		}*/
		
		TabList tablist = player.getTabList();
		tablist.setHeaderAndFooter(tabHeader, tabFooter);

		// Checks if the player has been removed from playersData
		// (Logged out) and removes from tablist
		if (!playersData.containsKey(playerData.getPlayerUUID())) {
		    removeTabList(tablist, playerData.getPlayerUUID());
		}
		
		
		
		
		for (PlayerData pData : playersData.values()) {

		    tabListEntry = tablist.getEntry(pData.getPlayerUUID());
		    TabListEntry entry;
		    if (tabListEntry.isPresent()) {
			entry = tabListEntry.get();
			handleData(pData, entry);
		    } else {

			entry = addTabList(player.getTabList(), pData.getPlayerUUID(),
				Text.of(TextColors.WHITE, pData.getPlayerName()), sendingServer);

			/*
			 * if (pData instanceof StaffData) {
			 * 
			 * DarkGreen.addMember(Text.of(pData.getPlayerName()));
			 * entry.setDisplayName(Text.of(TextColors.DARK_GREEN,
			 * pData.getPlayerName())); } else {
			 * White.addMember(Text.of(pData.getPlayerName()));
			 * entry.setDisplayName(Text.of(TextColors.WHITE,
			 * pData.getPlayerName())); }
			 */

			tablist.addEntry(entry);

		    }

		    handleData(pData, entry);
		}

		player.setScoreboard(scoreboard);
	    }
	}).async().submit(this);

    }

    private TabListEntry addTabList(TabList tablist, UUID uuid, Text playername, String server) {
	CompletableFuture<GameProfile> futureGameProfile = gpm.get(uuid);
	try {
	    GameProfile gp1 = futureGameProfile.get();
	    TabListEntry entry = TabListEntry.builder().list(tablist).profile(gp1).gameMode(GameModes.SURVIVAL)
		    .displayName(playername).build();
	    return entry;
	} catch (InterruptedException | ExecutionException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	/*
	 * if (!gp.isPresent()) { GameProfile fakeProfile = GameProfile.of(uuid,
	 * playername.toString());
	 * 
	 * gpm.getCache().add(fakeProfile); gp = gpm.getCache().getById(uuid); }
	 */
	return null;

    }

    private void removeTabList(TabList tablist, UUID uuid) {
	tabListEntry = tablist.getEntry(uuid);
	if (tabListEntry.isPresent()) {
	    tablist.removeEntry(uuid);
	}

    }

    private void handleData(PlayerData p, TabListEntry entry) {
	if (p instanceof StaffData) {
	    StaffData s = (StaffData) p;
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
	} else {
	    White.addMember(Text.of(p.getPlayerName()));
	    entry.setDisplayName(Text.of(TextColors.WHITE, p.getPlayerName()));
	}

    }

    private Boolean getOnline(PlayerData player) {
	Optional<Player> optP = Sponge.getServer().getPlayer(player.getPlayerUUID());
	if (optP.isPresent()) {
	    return optP.get().isOnline();
	}
	return false;

    }

    public Text getHeader() {
	return tabHeader;
    }

    public Text getFooter() {
	return tabFooter;
    }

    public Logger getLogger() {
	return log;
    }

    public String getStateChannel() {
	return channelState;
    }

    public String getChannel() {
	return channel;
    }
}
