package io.github.axle2005.syntablist.sponge.listeners;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;

import io.github.axle2005.syntablist.sponge.SynTabList;
import io.github.axle2005.syntablist.common.PlayerData;
import io.github.axle2005.syntablist.common.Utils;
import io.github.axle2005.syntablist.common.PlayerData.Action;
import net.kaikk.mc.synx.SynX;

public class ListenerPlayerDisconnect {

	SynTabList plugin;
	private static String CHANNEL;
	

	public ListenerPlayerDisconnect(SynTabList plugin) {
		this.plugin = plugin;
		CHANNEL = Utils.getChannel();
	}
	
	@Listener(beforeModifications = true)
	public void handle(ClientConnectionEvent.Disconnect event) throws Exception {
		Player player = event.getTargetEntity();
		
		

		// we want to send the player's data to the other servers so they can show a message to everyone
		PlayerData playerData = new PlayerData(player.getName(), player.getUniqueId(),Action.QUIT);
		
		// broadcast data to the JPlayer channel - all servers will receive a packet with this data!
		SynX.instance().broadcast(CHANNEL, playerData,System.currentTimeMillis()+60000);

	}

}
