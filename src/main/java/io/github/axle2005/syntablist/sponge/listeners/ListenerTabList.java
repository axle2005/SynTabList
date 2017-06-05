package io.github.axle2005.syntablist.sponge.listeners;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.entity.living.player.tab.TabList;
import org.spongepowered.api.entity.living.player.tab.TabListEntry;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.profile.GameProfileManager;
import org.spongepowered.api.scheduler.Scheduler;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import io.github.axle2005.syntablist.common.PlayerData;
import io.github.axle2005.syntablist.common.StaffData;
import io.github.axle2005.syntablist.sponge.SynTabList;
import net.kaikk.mc.synx.packets.ChannelListener;
import net.kaikk.mc.synx.packets.Packet;

public class ListenerTabList implements ChannelListener {

	Scheduler scheduler = Sponge.getScheduler();
	Task.Builder taskBuilder = scheduler.createTaskBuilder();
	Task task = null;
	
	private Server server;
	private GameProfileManager gpm;
	private Optional<TabListEntry> tabListEntry;


	Text tabHeader;
	Text tabFooter;
	Map<UUID, PlayerData> playersData = new ConcurrentHashMap<>();
	Map<UUID, StaffData> staffsData = new ConcurrentHashMap<>();
	Map<UUID, Text> rankData = new ConcurrentHashMap<>();
	
	
	SynTabList instance;
	public ListenerTabList(SynTabList instance){
		this.instance = instance;
		tabHeader = instance.getHeader();
		tabFooter = instance.getFooter();
		server = Sponge.getServer();
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
		}).async().submit(this);

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

}
