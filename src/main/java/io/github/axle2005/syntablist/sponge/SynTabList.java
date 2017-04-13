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
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.entity.living.player.tab.TabList;
import org.spongepowered.api.entity.living.player.tab.TabListEntry;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.profile.GameProfileManager;
import org.spongepowered.api.scheduler.Scheduler;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.scoreboard.Team;
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
import io.github.axle2005.syntablist.sponge.commands.CommandRegister;
import io.github.axle2005.syntablist.sponge.listeners.ListenerRegister;
import net.kaikk.mc.synx.SynX;
import net.kaikk.mc.synx.packets.ChannelListener;
import net.kaikk.mc.synx.packets.Packet;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

@Plugin(id = "syntablist", name = "SynTabList", dependencies = @Dependency(id = "synx"))
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
	private Server server;
	private GameProfileManager gpm;
	private Optional<TabListEntry> tabListEntry;
	private Team staff;
	private String channel;

	
	Text tabHeader;
	Text tabFooter;
	Map<UUID, PlayerData> playersData = new ConcurrentHashMap<>();
	Map<UUID, StaffData> staffsData = new ConcurrentHashMap<>();
	Map<UUID, Text> rankData = new ConcurrentHashMap<>();

	@Listener
	public void initialization(GameInitializationEvent event) {
		config = new Config(this, defaultConfig, configManager);
		
		tabHeader = Text.of(TextSerializers.formattingCode('&').deserialize(config.getNodeString("TabList,Header")));
		tabFooter = Text.of(TextSerializers.formattingCode('&').deserialize(config.getNodeString("TabList,Footer")));
		channel = config.getNodeString("SynX Channel");
		
		
		events = new ListenerRegister(this);
		new CommandRegister(this);
		
		server = Sponge.getServer();
		gpm = Sponge.getServer().getGameProfileManager();

	}

	@Listener
	public void onEnable(GameStartedServerEvent event) {

		SynX.instance().register(this, Utils.getChannel(), this);
		events.registerEvent("Connect");
		events.registerEvent("Disconnect");
		staff = Team.builder().name("Staff").build();
		
		
		
		

	}

	@Override
	public void onPacketReceived(Packet packet) {
		// This is the server that sent this packet
		final String sendingServer = packet.getFrom().getName();

		// Let's get the object from the packet
		// final PlayerData playerData = packet.getObject(PlayerData.class);

		final Object data = packet.getObject();
		/*if (data instanceof StaffData) {
			StaffData staffData = (StaffData) data;

			switch (staffData.getAction()) {
			case QUIT: {
				staffsData.remove(staffData.getPlayerUUID());
				rankData.remove(staffData.getPlayerUUID());

				break;
			}
			case JOIN: {
				staffsData.put(staffData.getPlayerUUID(), staffData);
				switch (staffData.getRank()) {
				case SENIORADMIN: {

					rankData.put(staffData.getPlayerUUID(), Text.of(TextColors.GREEN, staffData.getPlayerName() + " "));
					staff.addMember(Text.of(TextColors.GREEN, staffData.getPlayerName() + " "));
					break;
				}
				case SENIORDEVELOPER: {

					break;
				}
				}

				break;
			}
			}
			/*
			 * task = taskBuilder.execute(() -> { for (Player player :
			 * server.getOnlinePlayers()) {
			 * 
			 * TabList tablist = player.getTabList();
			 * 
			 * for (StaffData sData : staffsData.values()) {
			 * 
			 * tabHeader = Text.of(Text.NEW_LINE,
			 * rankData.get(staffData.getPlayerUUID()));
			 * log.info(""+Text.of(Text.NEW_LINE,
			 * rankData.get(staffData.getPlayerUUID())));
			 * 
			 * } tablist.setHeaderAndFooter(tabHeader, tabFooter); }
			 * }).submit(this);
			 
		}*/

		PlayerData playerData = (PlayerData) data;

		switch (playerData.getAction()) {
		case JOIN: {
			playersData.put(playerData.getPlayerUUID(), playerData);
			break;
		}
		case QUIT: {
			playersData.remove(playerData.getPlayerUUID());
			break;
		}
		}

		task = taskBuilder.execute(() -> {
			for (Player player : server.getOnlinePlayers()) {

				// Team staff = Team.builder().nameTagVisibility(Visibility.)
				TabList tablist = player.getTabList();

				tablist.setHeaderAndFooter(tabHeader, tabFooter);

				// Checks if the player has been removed from playersData
				// (Logged out) and removes from tablist
				if (!playersData.containsKey(playerData.getPlayerUUID())) {
					removeTabList(tablist, playerData.getPlayerUUID());
				}

				for (PlayerData pData : playersData.values()) {

					tabListEntry = tablist.getEntry(pData.getPlayerUUID());
					if (tabListEntry.isPresent()) {
						TabListEntry entry = tabListEntry.get();
						if (pData instanceof StaffData) {

							entry.setDisplayName(Text.of(TextColors.DARK_GREEN, pData.getPlayerName()));
						} else {
							entry.setDisplayName(Text.of(TextColors.WHITE, pData.getPlayerName()));
						}

					} else {
						TabListEntry entry = addTabList(player.getTabList(), pData.getPlayerUUID(),
								Text.of(TextColors.WHITE, pData.getPlayerName()), sendingServer);
						
						if (pData instanceof StaffData) {
							entry.setDisplayName(Text.of(TextColors.DARK_GREEN, pData.getPlayerName()));
						} 
						tablist.addEntry(entry);

					}
				}
			}
		}).submit(this);

	}

	private TabListEntry addTabList(TabList tablist, UUID uuid, Text playername, String server) {
		Optional<GameProfile> gp = gpm.getCache().getById(uuid);
		if (!gp.isPresent()) {
			GameProfile fakeProfile = GameProfile.of(uuid, playername.toString());

			gpm.getCache().add(fakeProfile);
			gp = gpm.getCache().getById(uuid);
		}

		TabListEntry entry = TabListEntry.builder().list(tablist).profile(gp.get()).gameMode(GameModes.SURVIVAL)
				.displayName(playername).build();
		return entry;

	}

	private void removeTabList(TabList tablist, UUID uuid) {
		tabListEntry = tablist.getEntry(uuid);
		if (tabListEntry.isPresent()) {
			tablist.removeEntry(uuid);
		}
	}
	public String getChannel()
	{
		return channel;
	}

}
