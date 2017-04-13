package io.github.axle2005.syntablist.bukkit.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import io.github.axle2005.syntablist.bukkit.SynTabList;
import io.github.axle2005.syntablist.common.PlayerData;
import io.github.axle2005.syntablist.common.StaffData;
import io.github.axle2005.syntablist.common.PlayerData.Action;
import io.github.axle2005.syntablist.common.StaffData.Rank;
import io.github.axle2005.syntablist.common.Utils;
import net.kaikk.mc.synx.SynX;

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

		if (player.hasPermission("syntablist.senioradmin")) {
			StaffData staffData = new StaffData(player.getName(), player.getUniqueId(), Action.JOIN,Rank.SENIORADMIN, false);
			SynX.instance().broadcast(CHANNEL, staffData,System.currentTimeMillis()+60000);
		}
		else
		{
			// we want to send the player's data to the other servers so they can show a message to everyone
			PlayerData playerData = new PlayerData(player.getName(), player.getUniqueId(),Action.JOIN);
			
			// broadcast data to the JPlayer channel - all servers will receive a packet with this data!
			SynX.instance().broadcast(CHANNEL, playerData,System.currentTimeMillis()+60000);
		}
		
		
		
		
	}
}
