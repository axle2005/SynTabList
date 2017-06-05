package io.github.axle2005.syntablist.common;

import java.util.UUID;

public class StaffData extends PlayerData {

	private static final long serialVersionUID = 4413687814272747933L;
	private Rank rank;
	private Boolean isHidden;
	public enum Rank {
		  HIDDEN,OWNER,OPERATOR,SENIORADMIN,SENIORDEVELOPER,ADMIN,DEVELOPER,SENIORMOD,MOD,CHATMOD,HELPER;
		}
	
	public StaffData(String playerName, UUID playerUUID, Action packet, Rank rank, Boolean isHidden) {
		super(playerName, playerUUID, packet);
		this.rank=rank;
		this.isHidden = isHidden;
	}
	public Rank getRank()
	{
		return rank;
	}
	public Boolean isHidden()
	{
		return isHidden;
	}

	
}
