package io.github.axle2005.syntablist.common;

import java.io.File;
import java.io.IOException;

import org.spongepowered.api.entity.living.player.Player;

import io.github.axle2005.syntablist.common.PlayerData.Action;
import io.github.axle2005.syntablist.common.StaffData.Rank;
import net.kaikk.mc.synx.SynX;

public class Utils {

	private static String channel = "TabList";

	public static String getChannel() {
		return channel;
	}

	public static void eventJoin(String channel, Player player) {
		
		if (player.hasPermission("syntablist.senioradmin")) {
			StaffData staffData = new StaffData(player.getName(), player.getUniqueId(), Action.JOIN, Rank.SENIORADMIN,
					false);
			SynX.instance().broadcast(channel, staffData, System.currentTimeMillis() + 60000);
		} else if (player.hasPermission("syntablist.admin")) {
			StaffData staffData = new StaffData(player.getName(), player.getUniqueId(), Action.JOIN, Rank.ADMIN,
					false);
			SynX.instance().broadcast(channel, staffData, System.currentTimeMillis() + 60000);
		} else if (player.hasPermission("syntablist.mod")) {
			StaffData staffData = new StaffData(player.getName(), player.getUniqueId(), Action.JOIN, Rank.MOD,
					false);
			SynX.instance().broadcast(channel, staffData, System.currentTimeMillis() + 60000);
		} else if (player.hasPermission("syntablist.helper")) {
			StaffData staffData = new StaffData(player.getName(), player.getUniqueId(), Action.JOIN, Rank.HELPER,
					false);
			SynX.instance().broadcast(channel, staffData, System.currentTimeMillis() + 60000);
		} else {
			PlayerData playerData = new PlayerData(player.getName(), player.getUniqueId(), Action.JOIN);
			SynX.instance().broadcast(channel, playerData, System.currentTimeMillis() + 60000);
		}
		if (player.hasPermission("syntablist.hide.perm")) {
			StaffData staffData = new StaffData(player.getName(), player.getUniqueId(), Action.QUIT, Rank.HIDDEN, true);
			SynX.instance().broadcast(channel, staffData, System.currentTimeMillis() + 60000);
		}
	}

	public static void eventQuit(String channel, Player player) {
		// we want to send the player's data to the other servers so they can
		// show a message to everyone
		PlayerData playerData = new PlayerData(player.getName(), player.getUniqueId(), Action.QUIT);

		// broadcast data to the JPlayer channel - all servers will receive a
		// packet with this data!
		SynX.instance().broadcast(channel, playerData, System.currentTimeMillis() + 60000);
	}

	public static void eventJoin(String channel, org.bukkit.entity.Player player) {
		if (player.hasPermission("syntablist.hide.perm")) {
			StaffData staffData = new StaffData(player.getName(), player.getUniqueId(), Action.QUIT, Rank.HIDDEN, true);
			SynX.instance().broadcast(channel, staffData, System.currentTimeMillis() + 60000);
		} else if (player.hasPermission("syntablist.senioradmin")) {
			StaffData staffData = new StaffData(player.getName(), player.getUniqueId(), Action.JOIN, Rank.SENIORADMIN,
					false);
			SynX.instance().broadcast(channel, staffData, System.currentTimeMillis() + 60000);
		} else {
			PlayerData playerData = new PlayerData(player.getName(), player.getUniqueId(), Action.JOIN);
			SynX.instance().broadcast(channel, playerData, System.currentTimeMillis() + 60000);
		}
	}

	public static Boolean checkDummyFile(String folder) {
		String path = folder.replaceAll("/n/", File.separator + "");
		File dummy = new File(path + File.separator + "dummy");
		if (dummy.exists()) {
			dummy.delete();
			return true;
		} else {
			return false;
		}
	}

	public static void createDummyFile(String folder) {

		String path = folder.replaceAll("/n/", File.separator + "");
		// String path = Sponge.getGame().getGameDirectory() + File.separator +
		// folder;

		File location = new File(path);
		if (location.isDirectory()) {
			File dummy = new File(path + File.separator + "dummy");

			if (!dummy.exists()) {
				try {
					dummy.createNewFile();

				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

	}

}
