package io.github.axle2005.syntablist.bukkit;

import java.util.logging.Logger;

import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;
import io.github.axle2005.syntablist.bukkit.listeners.ListenerPlayerConnect;
import io.github.axle2005.syntablist.bukkit.listeners.ListenerPlayerDisconnect;

public class SynTabList extends JavaPlugin {
	 //implements ChannelListener
	static Logger log = Logger.getLogger("Minecraft");
	static Permission permission = null;

	// Config config = new Config(this, "config.yml");
	@Override
	public void onEnable() {
		
		getServer().getPluginManager().registerEvents(new ListenerPlayerConnect(this), this);
		getServer().getPluginManager().registerEvents(new ListenerPlayerDisconnect(this), this);
		//SynX.instance().register(this, CHANNEL, this);
	}

	public static Logger getLog() {
		return log;
	}


}
