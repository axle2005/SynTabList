package io.github.axle2005.syntablist.bukkit.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import io.github.axle2005.syntablist.bukkit.SynTabList;
import io.github.axle2005.syntablist.common.Utils;

public class ListenerPlayerConnect implements Listener {

	
	private static String CHANNEL;
	
	SynTabList plugin;
	public ListenerPlayerConnect(SynTabList plugin) {
		this.plugin = plugin;
		CHANNEL = Utils.getChannel();
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		Utils.eventJoin(CHANNEL, player);
		
		
		
		
	}
}
