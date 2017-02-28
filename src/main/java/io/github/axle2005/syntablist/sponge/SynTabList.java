package io.github.axle2005.syntablist.sponge;

import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.entity.living.player.tab.TabList;
import org.spongepowered.api.entity.living.player.tab.TabListEntry;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.profile.GameProfileCache;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.plugin.Dependency;

import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.axle2005.syntablist.common.PlayerData;
import io.github.axle2005.syntablist.common.Utils;
import io.github.axle2005.syntablist.sponge.listeners.ListenerRegister;
import net.kaikk.mc.synx.SynX;
import net.kaikk.mc.synx.packets.ChannelListener;
import net.kaikk.mc.synx.packets.Packet;

@Plugin(id = "syntablist", name = "SynTabList", dependencies = @Dependency(id = "synx"))
public class SynTabList implements ChannelListener {

	@Inject
	private Logger log;

	private ListenerRegister events;
	private Server server;
	private GameProfileCache gpmcache;
	Map<UUID, PlayerData> playersData = new HashMap<>();

	@Listener
	public void initialization(GameInitializationEvent event) {
		// new CommandRegister(this);
		events = new ListenerRegister(this);
		server = Sponge.getServer();
		gpmcache = Sponge.getServer().getGameProfileManager().getCache();
		
	}

	@Listener
	public void onEnable(GameStartedServerEvent event) {

		SynX.instance().register(this, Utils.getChannel(), this);
		events.registerEvent("Connect");
		events.registerEvent("Disconnect");

	}

	@Override
	public void onPacketReceived(Packet packet) {
		// This is the server that sent this packet
		final String sendingServer = packet.getFrom().getName();

		// Let's get the object from the packet
		final PlayerData playerData = packet.getObject(PlayerData.class);

		if (playerData.getType().equals("Join")) {
			playersData.put(playerData.getPlayerUUID(), playerData);
		} else if (playerData.getType().equals("Quit")) {
			if (playersData.containsKey(playerData.getPlayerUUID())) {
				playersData.remove(playerData.getPlayerUUID());
			}
			log.info(playerData.getPlayerName() + " " + playerData.getPlayerUUID());
		}
		for (Player player : server.getOnlinePlayers()) {

			TabList tablist = player.getTabList();

			tablist.setHeader(Text.of(TextColors.BLUE, "=======================", Text.NEW_LINE, TextColors.BLUE,
					"=========", TextColors.WHITE, "DeVco", TextColors.BLUE, "========="));
			tablist.setFooter(Text.of(TextColors.BLUE, "======================="));

			
			//Checks if the player has been removed from playersData (Logged out) and removes from tablist
			if (!playersData.containsKey(playerData.getPlayerUUID())) {
				Optional<TabListEntry> optional = tablist.getEntry(playerData.getPlayerUUID());
				if (optional.isPresent()) {
					tablist.removeEntry(playerData.getPlayerUUID());
				}
				
			}
			
			
			for (PlayerData pData : playersData.values()) {

				Optional<TabListEntry> optional = tablist.getEntry(pData.getPlayerUUID());
				if (optional.isPresent()) {
					TabListEntry entry = optional.get()
							.setDisplayName(Text.of(TextColors.WHITE, pData.getPlayerName()));
				} else {
					tablist.addEntry(addTabList(player.getTabList(), pData.getPlayerUUID(), pData.getPlayerName(),
							sendingServer));
				}
			}

		}

	}

	private void buildTabList(TabList tablist) {
		tablist.setHeader(Text.of(TextColors.GOLD, "=========DeVco========="));
		tablist.setFooter(Text.of(TextColors.RED, "======================="));

	}

	private TabListEntry addTabList(TabList tablist, UUID uuid, String playername, String server) {
		Optional<GameProfile> gp = gpmcache.getById(uuid);
		if (!gp.isPresent()) {
			GameProfile fakeProfile = GameProfile.of(uuid, playername);

			gpmcache.add(fakeProfile);
			gp = gpmcache.getById(uuid);
		}

		TabListEntry entry = TabListEntry.builder().list(tablist).profile(gp.get()).gameMode(GameModes.SURVIVAL)
				.displayName(Text.of(TextColors.WHITE, playername)).build();
		return entry;

	}

	private void removeTabList(TabList tablist, UUID uuid) {
		Optional<TabListEntry> optional = tablist.getEntry(uuid);
		if (optional.isPresent()) {
			TabListEntry entry = optional.get();
		}
	}

}
