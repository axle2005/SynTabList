package io.github.axle2005.syntablist.bukkit.listeners;

import org.spongepowered.api.Sponge;

import io.github.axle2005.syntablist.bukkit.SynTabList;
import io.github.axle2005.syntablist.bukkit.listeners.ListenerPlayerConnect;
import io.github.axle2005.syntablist.bukkit.listeners.ListenerPlayerDisconnect;

public class ListenerRegister {
	SynTabList plugin;
	ListenerPlayerConnect connect;
	ListenerPlayerDisconnect disconnect;

	public ListenerRegister(SynTabList plugin) {
		this.plugin = plugin;
		connect = new ListenerPlayerConnect(plugin);
		disconnect = new ListenerPlayerDisconnect(plugin);

	}

	public void registerEvent(String event) {

		if (event.equals("Connect")) {

			Sponge.getEventManager().registerListeners(plugin, connect);
		}
		if (event.equals("Disconnect")) {

			Sponge.getEventManager().registerListeners(plugin, disconnect);
		}

	}

	public void unregisterEvent(String event) {

		if (event.equals("Connect")) {

			Sponge.getEventManager().unregisterListeners(connect);
		}
		if (event.equals("Disconnect")) {

			Sponge.getEventManager().unregisterListeners(disconnect);
		}

	}
}
