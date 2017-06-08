package io.github.axle2005.syntablist.bukkit.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import io.github.axle2005.syntablist.common.Utils;
import io.github.axle2005.syntablist.common.PlayerData.Action;
import net.kaikk.mc.synx.SynX;

public class ListenerPlayerConnect implements Listener {

    private static final String CHANNEL = Utils.getChannel();
	public ListenerPlayerConnect() {
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		//Utils.eventJoin(CHANNEL, player);
		if(player.hasPermission("syntablist.staff")){
		    SynX.instance().broadcast(CHANNEL,Utils.getStaffData(player, Action.JOIN) , System.currentTimeMillis() + 60000);
		}
		else
		{	
		    SynX.instance().broadcast(Utils.getChannel(),Utils.getPlayerData(player, Action.JOIN) , System.currentTimeMillis() + 60000);
		}
		
		
		
	}
}
