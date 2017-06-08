package io.github.axle2005.syntablist.sponge.listeners;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import io.github.axle2005.syntablist.sponge.SynTabList;
import io.github.axle2005.syntablist.sponge.TabListUtil;
import net.kaikk.mc.synx.SynX;
import io.github.axle2005.syntablist.common.Utils;
import io.github.axle2005.syntablist.common.PlayerData.Action;

public class ListenerPlayerConnect {

	private static final String CHANNEL = Utils.getChannel();

	private SynTabList plugin;
	public ListenerPlayerConnect() {
	}

	@Listener
	public void handle(ClientConnectionEvent.Join event) throws Exception {
		
		Player player = event.getTargetEntity();

		//Utils.eventJoin(CHANNEL, player);
		if(player.hasPermission("syntablist.staff")){
		    SynX.instance().broadcast(CHANNEL,Utils.getStaffData(player, Action.JOIN) , System.currentTimeMillis() + 60000);
		}
		else
		{	
		    SynX.instance().broadcast(CHANNEL,Utils.getPlayerData(player, Action.JOIN) , System.currentTimeMillis() + 60000);
		}
		
		player.getTabList().setHeaderAndFooter(TabListUtil.getHeader(), TabListUtil.getFooter());
		player.setScoreboard(plugin.getScoreboard());
		

	}

}
