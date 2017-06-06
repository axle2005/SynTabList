package io.github.axle2005.syntablist.common;

import java.io.Serializable;
import java.util.UUID;

public class PlayerData implements Serializable {
	private static final long serialVersionUID = 7143543049235984071L;
	
	private final String playerName;
	private final UUID playerUUID;
	private Action packet;
	
	public PlayerData(String playerName, UUID playerUUID, Action packet) {
		this.playerName = playerName;
		this.playerUUID = playerUUID;
		this.packet = packet;
	}
	public enum Action {
		  JOIN, QUIT;
		}
	public String getPlayerName() {
		return playerName;
	}

	public UUID getPlayerUUID() {
		return playerUUID;
	}
	public Action getAction()
	{
		return packet;
	}
	
	
}
