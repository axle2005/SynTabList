package io.github.axle2005.syntablist.common;

import java.util.UUID;

public class StaffData extends PlayerData {

	private static final long serialVersionUID = 4413687814272747933L;
	private Rank rank;
	public enum Rank {
		  SENIORADMIN,SENIORDEVELOPER,ADMIN,DEVELOPER,SENIORMOD,MOD,CHATMOD,HELPER;
		}
	
	public StaffData(String playerName, UUID playerUUID, Action packet, Rank rank) {
		super(playerName, playerUUID, packet);
		this.rank=rank;
	}
	public Rank getRank()
	{
		return rank;
	}

	
}
