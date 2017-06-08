package io.github.axle2005.syntablist.bukkit.listeners;

import io.github.axle2005.syntablist.bukkit.SynTabList;
import io.github.axle2005.syntablist.bukkit.listeners.ListenerPlayerConnect;
import io.github.axle2005.syntablist.bukkit.listeners.ListenerPlayerDisconnect;

public class ListenerRegister {
	SynTabList plugin;
	ListenerPlayerConnect connect;
	ListenerPlayerDisconnect disconnect;

	public ListenerRegister() {
		connect = new ListenerPlayerConnect();
		disconnect = new ListenerPlayerDisconnect();

	}
}
