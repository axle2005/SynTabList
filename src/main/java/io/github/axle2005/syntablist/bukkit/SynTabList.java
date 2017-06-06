package io.github.axle2005.syntablist.bukkit;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;
import io.github.axle2005.syntablist.bukkit.listeners.ListenerPlayerConnect;
import io.github.axle2005.syntablist.bukkit.listeners.ListenerPlayerDisconnect;
import io.github.axle2005.syntablist.common.PlayerData;
import io.github.axle2005.syntablist.common.PlayerData.Action;
import net.kaikk.mc.synx.SynX;

public class SynTabList extends JavaPlugin {
	 //implements ChannelListener
	static Logger log = Logger.getLogger("Minecraft");
	static Permission permission = null;
	final String channel = "TabList";

	// Config config = new Config(this, "config.yml");
	@Override
	public void onEnable() {
		
		getServer().getPluginManager().registerEvents(new ListenerPlayerConnect(this), this);
		getServer().getPluginManager().registerEvents(new ListenerPlayerDisconnect(this), this);
		//SynX.instance().register(this, CHANNEL, this);
	}
	
	
	//Bukkit does not send PlayerQuit events on shutdown. 
	//This will send a quit packet for each player online. 
	@Override
	public void onDisable(){
	    for(Player player : Bukkit.getServer().getOnlinePlayers()){
		// we want to send the player's data to the other servers so they can show a message to everyone
		PlayerData playerData = new PlayerData(player.getName(), player.getUniqueId(),Action.QUIT);
		
		// broadcast data to the JPlayer channel - all servers will receive a packet with this data!
		SynX.instance().broadcast(channel, playerData,System.currentTimeMillis()+60000);
	    }
	}

	public static Logger getLog() {
		return log;
	}


}
