package io.github.axle2005.syntablist.bukkit.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import io.github.axle2005.syntablist.bukkit.SynTabList;
import io.github.axle2005.syntablist.common.PlayerData;
import io.github.axle2005.syntablist.common.PlayerData.Action;
import io.github.axle2005.syntablist.common.Utils;
import net.kaikk.mc.synx.SynX;

public class ListenerPlayerDisconnect implements Listener {

	private static String CHANNEL;
	
	SynTabList plugin;
	public ListenerPlayerDisconnect(SynTabList plugin) {
		this.plugin = plugin;
		CHANNEL = Utils.getChannel();
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerQuitEvent event) {
		Player player = event.getPlayer();

		// we want to send the player's data to the other servers so they can show a message to everyone
		PlayerData playerData = new PlayerData(player.getName(), player.getUniqueId(),Action.QUIT);
		
		// broadcast data to the JPlayer channel - all servers will receive a packet with this data!
		SynX.instance().broadcast(CHANNEL, playerData,System.currentTimeMillis()+60000);
		
	}
	
}
