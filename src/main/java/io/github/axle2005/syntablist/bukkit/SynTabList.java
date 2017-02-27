package io.github.axle2005.syntablist.bukkit;

import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import io.github.axle2005.syntablist.common.PlayerData;
import io.github.axle2005.syntablist.common.Utils;
import net.kaikk.mc.synx.SynX;
import net.kaikk.mc.synx.packets.ChannelListener;
import net.kaikk.mc.synx.packets.Packet;

public class SynTabList extends JavaPlugin implements ChannelListener {

	private static String CHANNEL;
	private Server server;
	static Logger log = Logger.getLogger("Minecraft");
	static Permission permission = null;

	// Config config = new Config(this, "config.yml");
	@Override
	public void onEnable() {
		PluginDescriptionFile pdf = getDescription();
		this.log.info(pdf.getName() + "> " + pdf.getName() + " v" + pdf.getVersion() + " enabled!!");
		server = Bukkit.getServer();
		CHANNEL = Utils.getChannel();
		SynX.instance().register(this, CHANNEL, this);
	}

	public static Logger getLog() {
		return log;
	}

	@Override
	public void onPacketReceived(Packet packet) {
		// This is the server that sent this packet
		final String sendingServer = packet.getFrom().getName();

		/*
		 * onPacketReceived method is called asynchronously. This means that we
		 * can't access most of Bukkit stuff without synchronizing with the
		 * server thread. This can be easily achieved by using the
		 * BukkitRunnable class in the following way.
		 */
		final PlayerData playerData = packet.getObject(PlayerData.class);
		if (playerData.getType().equals("Quit")) {
			for (Player player : server.getOnlinePlayers()) {
				// removeTabList(player.getTabList(),
				// playerData.getPlayerUUID());

			}

			Collection<? extends Player> p = Bukkit.getOnlinePlayers();
			for (Player player : p) {

			}
			new BukkitRunnable() {
				@Override
				public void run() {
					for (Player player : Bukkit.getOnlinePlayers()) {

						// player.sendMessage(ChatColor.GOLD +
						// joinedPlayerData.getPlayerName() + " has joined " +
						// sendingServer);
					}
				}
			}.runTask(this);
		}

	}

	public String getvaultversion() {
		if (getServer().getPluginManager().getPlugin("Vault") != null) {
			return getServer().getPluginManager().getPlugin("Vault").getDescription().getVersion();
		}
		return "";
	}

	public boolean checkvault() {
		return checkvault(0);
	}

	public boolean checkvault(int log) {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			if (log == 1) {
				getLogger().info("Vault not found. Support Disabled!");
			}
			return false;
		}
		return true;
	}

	public boolean setupPermissions() {
		if (!checkvault()) {
			return false;
		}
		RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager()
				.getRegistration(Permission.class);
		if (permissionProvider != null) {
			permission = (Permission) permissionProvider.getProvider();
			getLogger().info("Permission Plugin found! " + permission.getName() + " hooked!");
		}
		return permission != null;
	}

	public static boolean hasPermission(Player player, List<String> list) {
		for (String permission : list) {
			String perm = permission.substring(5);
			if (player.hasPermission(perm)) {
				return true;
			}
		}
		return false;
	}
}
