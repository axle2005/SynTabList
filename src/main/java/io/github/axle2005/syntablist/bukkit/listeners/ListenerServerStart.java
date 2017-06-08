package io.github.axle2005.syntablist.bukkit.listeners;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

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

	   //Bukkit does not need to handle this yet, as Bukkit does not have a tablist. 
	    break;
	}
	case CRASH: {
	    // onlinePlayers("crash");
	    break;
	}
	case START: {
	    for (Player player : Bukkit.getServer().getOnlinePlayers()) {
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