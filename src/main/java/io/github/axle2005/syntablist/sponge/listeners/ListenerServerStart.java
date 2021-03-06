package io.github.axle2005.syntablist.sponge.listeners;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;

import io.github.axle2005.syntablist.common.PlayerData;
import io.github.axle2005.syntablist.common.PlayerData.Action;
import io.github.axle2005.syntablist.common.ServerData;
import io.github.axle2005.syntablist.common.Utils;
import net.kaikk.mc.synx.SynX;
import net.kaikk.mc.synx.packets.ChannelListener;
import net.kaikk.mc.synx.packets.Packet;

public class ListenerServerStart implements ChannelListener {

    PlayerData p;
    
    public ListenerServerStart() {

    }

    @Override
    public void onPacketReceived(Packet packet) {

	// final String sendingServer = packet.getFrom().getName();
	final Object data = packet.getObject();
	ServerData serverData = (ServerData) data;

	switch (serverData.getState()) {
	case STOP: {

	    for (Player player : Sponge.getServer().getOnlinePlayers()) {
		
	    }
	    break;
	}
	case CRASH: {
	    // onlinePlayers("crash");
	    break;
	}
	case START: {
	    for (Player player : Sponge.getServer().getOnlinePlayers()) {
		//Use send so that it only sends to the requesting node. 
		if(player.hasPermission("syntablist.staff")){
		    SynX.instance().send(Utils.getChannel(),Utils.getStaffData(player, Action.JOIN) , System.currentTimeMillis() + 60000, packet.getFrom());
		}
		else
		{	
		    SynX.instance().send(Utils.getChannel(),Utils.getPlayerData(player, Action.JOIN) , System.currentTimeMillis() + 60000, packet.getFrom());
		}
	    }
	    break;
	}
	}
    }

}
