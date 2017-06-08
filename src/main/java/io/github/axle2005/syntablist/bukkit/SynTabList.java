package io.github.axle2005.syntablist.bukkit;

import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;
import io.github.axle2005.syntablist.bukkit.listeners.ListenerPlayerConnect;
import io.github.axle2005.syntablist.bukkit.listeners.ListenerPlayerDisconnect;
import io.github.axle2005.syntablist.bukkit.listeners.ListenerServerStart;
import io.github.axle2005.syntablist.common.ServerData;
import io.github.axle2005.syntablist.common.ServerData.State;
import io.github.axle2005.syntablist.common.Utils;
import net.kaikk.mc.synx.SynX;

public class SynTabList extends JavaPlugin {
	 //implements ChannelListener
	static Permission permission = null;
	final String channel = "TabList";

	// Config config = new Config(this, "config.yml");
	@Override
	public void onEnable() {
		
		getServer().getPluginManager().registerEvents(new ListenerPlayerConnect(this), this);
		getServer().getPluginManager().registerEvents(new ListenerPlayerDisconnect(this), this);
		SynX.instance().register(this, Utils.getStateChannel(), new ListenerServerStart());
		SynX.instance().broadcast(Utils.getStateChannel(), new ServerData(State.START), System.currentTimeMillis() + 60000);
	}
	
	
	//Bukkit does not send PlayerQuit events on shutdown. 
	//This will send a quit packet for each player online. 
	@Override
	public void onDisable(){
	    SynX.instance().broadcast(Utils.getStateChannel(), new ServerData(State.STOP), System.currentTimeMillis() + 60000);
	}

}
