package io.github.axle2005.syntablist.sponge.listeners;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;

import io.github.axle2005.syntablist.sponge.SynTabList;
import io.github.axle2005.syntablist.common.PlayerData;
import io.github.axle2005.syntablist.common.PlayerData.Action;
import io.github.axle2005.syntablist.common.Utils;
import net.kaikk.mc.synx.SynX;

public class ListenerPlayerConnect {

	private static String CHANNEL;
	
	SynTabList plugin;
	public ListenerPlayerConnect(SynTabList plugin) {
		this.plugin = plugin;
		CHANNEL = Utils.getChannel();
	}
	
	@Listener(beforeModifications = true)
	public void handle(ClientConnectionEvent.Join event) throws Exception {
		Player player = event.getTargetEntity();
		
		PlayerData playerData = new PlayerData(player.getName(), player.getUniqueId(), Action.JOIN);
		SynX.instance().broadcast(CHANNEL, playerData);
		
	}

}
