package io.github.axle2005.syntablist.sponge.listeners;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;

import io.github.axle2005.syntablist.sponge.SynTabList;
import io.github.axle2005.syntablist.sponge.TabListUtil;
import io.github.axle2005.syntablist.common.Utils;

public class ListenerPlayerConnect {

	private static String CHANNEL;

	SynTabList plugin;

	public ListenerPlayerConnect(SynTabList plugin) {
		this.plugin = plugin;
		CHANNEL = plugin.getChannel();
	}

	@Listener(beforeModifications = true)
	public void handle(ClientConnectionEvent.Join event) throws Exception {
		
		Player player = event.getTargetEntity();

		Utils.eventJoin(CHANNEL, player);

		
		
		player.getTabList().setHeaderAndFooter(TabListUtil.getHeader(), TabListUtil.getFooter());
		player.setScoreboard(plugin.getScoreboard());

	}

}
