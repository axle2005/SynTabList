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

		onlinePlayers("stop");
		break;
	    }
	    case CRASH: {
		// onlinePlayers("crash");
		break;
	    }
	    case START: {
		onlinePlayers("start");
		break;
	    }
	    }
	}

    }

    private void onlinePlayers(String action) {

	for (Player player : Sponge.getServer().getOnlinePlayers()) {
	    if (action.equals("start")) {
		Utils.eventJoin(instance.getStateChannel(), player);
	    } else {
		Utils.eventQuit(instance.getStateChannel(), player);
	    }
	}
    }

}
