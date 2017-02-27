package io.github.axle2005.syntablist.common;

import java.io.Serializable;
import java.util.UUID;

public class PlayerData implements Serializable {
	private static final long serialVersionUID = 7143543049235984071L;
	
	private final String playerName;
	private final UUID playerUUID;
	private final String type;
	
	public PlayerData(String playerName, UUID playerUUID, String type) {
		this.playerName = playerName;
		this.playerUUID = playerUUID;
		this.type = type;
	}

	public String getPlayerName() {
		return playerName;
	}

	public UUID getPlayerUUID() {
		return playerUUID;
	}
	public String getType()
	{
		return type;
	}
}
