package io.github.axle2005.syntablist.sponge.listeners;

import org.spongepowered.api.Sponge;

import io.github.axle2005.syntablist.sponge.SynTabList;


	public class ListenerRegister {

		SynTabList plugin;
		ListenerPlayerConnect connect;
		ListenerPlayerDisconnect disconnect;

		public ListenerRegister(SynTabList plugin) {
			this.plugin = plugin;
			connect = new ListenerPlayerConnect();
			disconnect = new ListenerPlayerDisconnect();

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

