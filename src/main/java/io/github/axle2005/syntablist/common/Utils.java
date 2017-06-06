package io.github.axle2005.syntablist.common;

import java.io.File;
import java.io.IOException;

import org.spongepowered.api.entity.living.player.Player;

import io.github.axle2005.syntablist.common.PlayerData.Action;
import io.github.axle2005.syntablist.common.StaffData.Rank;
import net.kaikk.mc.synx.SynX;

public class Utils {

    private static final String channel = "TabList";
    private static final String channelState = "State";
    
    public static String getChannel() {
	return channel;
    }
    public static String getStateChannel(){
	return channelState;
    }
    
    public static void eventQuit(String channel, Player player) {
	PlayerData playerData = new PlayerData(player.getName(), player.getUniqueId(), Action.QUIT);
	SynX.instance().broadcast(channel, playerData, System.currentTimeMillis() + 60000);
    }

    //Sponge implementation
    public static void eventJoin(String channel, Player player) {
	Boolean isHidden = false;
	if (player.hasPermission("syntablist.hide.perm"))
	    isHidden = true;

	if (player.hasPermission("syntablist.senioradmin")) {
	    StaffData staffData = new StaffData(player.getName(), player.getUniqueId(), Action.JOIN, Rank.SENIORADMIN,
		    isHidden);
	    SynX.instance().broadcast(channel, staffData, System.currentTimeMillis() + 60000);
	} else if (player.hasPermission("syntablist.admin")) {
	    StaffData staffData = new StaffData(player.getName(), player.getUniqueId(), Action.JOIN, Rank.ADMIN,
		    isHidden);
	    SynX.instance().broadcast(channel, staffData, System.currentTimeMillis() + 60000);
	} else if (player.hasPermission("syntablist.mod")) {
	    StaffData staffData = new StaffData(player.getName(), player.getUniqueId(), Action.JOIN, Rank.MOD,
		    isHidden);
	    SynX.instance().broadcast(channel, staffData, System.currentTimeMillis() + 60000);
	} else if (player.hasPermission("syntablist.helper")) {
	    StaffData staffData = new StaffData(player.getName(), player.getUniqueId(), Action.JOIN, Rank.HELPER,
		    isHidden);
	    SynX.instance().broadcast(channel, staffData, System.currentTimeMillis() + 60000);
	} else {
	    PlayerData playerData = new PlayerData(player.getName(), player.getUniqueId(), Action.JOIN);
	    SynX.instance().broadcast(channel, playerData, System.currentTimeMillis() + 60000);
	}

    }
    
    //Bukkit Implementation 
    public static void eventJoin(String channel, org.bukkit.entity.Player player) {
	if (player.hasPermission("syntablist.senioradmin")) {
	    StaffData staffData = new StaffData(player.getName(), player.getUniqueId(), Action.JOIN, Rank.SENIORADMIN,
		    false);
	    SynX.instance().broadcast(channel, staffData, System.currentTimeMillis() + 60000);
	} else if (player.hasPermission("syntablist.admin")) {
	    StaffData staffData = new StaffData(player.getName(), player.getUniqueId(), Action.JOIN, Rank.ADMIN, false);
	    SynX.instance().broadcast(channel, staffData, System.currentTimeMillis() + 60000);
	} else if (player.hasPermission("syntablist.mod")) {
	    StaffData staffData = new StaffData(player.getName(), player.getUniqueId(), Action.JOIN, Rank.MOD, false);
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
