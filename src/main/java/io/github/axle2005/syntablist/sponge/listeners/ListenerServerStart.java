package io.github.axle2005.syntablist.sponge.listeners;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;

import io.github.axle2005.syntablist.common.ServerData;
import io.github.axle2005.syntablist.common.Utils;
import io.github.axle2005.syntablist.sponge.SynTabList;
import net.kaikk.mc.synx.packets.ChannelListener;
import net.kaikk.mc.synx.packets.Packet;

public class ListenerServerStart implements ChannelListener {

    SynTabList instance;

    public ListenerServerStart(SynTabList instance) {
	this.instance = instance;
    }

    @Override
    public void onPacketReceived(Packet packet) {

	//final String sendingServer = packet.getFrom().getName();
	final Object data = packet.getObject();
	if (data instanceof ServerData) {

	    ServerData serverData = (ServerData) data;

	    switch (serverData.getState()) {
	    case STOP: {

		for(Player player : Sponge.getServer().getOnlinePlayers()){
		    Utils.eventQuit(instance.getChannel(), player);
		}
		break;
	    }
	    case CRASH: {
		// onlinePlayers("crash");
		break;
	    }
	    case START: {
		for(Player player : Sponge.getServer().getOnlinePlayers()){
		    Utils.eventJoin(instance.getChannel(), player);
		}
		break;
	    }
	    }
	}

    }

}
