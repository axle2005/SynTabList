package io.github.axle2005.syntablist.sponge;

import static io.github.axle2005.syntablist.sponge.RankTeams.Aqua;
import static io.github.axle2005.syntablist.sponge.RankTeams.DarkBlue;
import static io.github.axle2005.syntablist.sponge.RankTeams.DarkGreen;
import static io.github.axle2005.syntablist.sponge.RankTeams.LightPurple;
import static io.github.axle2005.syntablist.sponge.RankTeams.White;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.entity.living.player.tab.TabList;
import org.spongepowered.api.entity.living.player.tab.TabListEntry;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.profile.GameProfileManager;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import io.github.axle2005.syntablist.common.PlayerData;
import io.github.axle2005.syntablist.common.StaffData;

public class TabListUtil {

    static Text tabHeader;
    static Text tabFooter;
    final static GameProfileManager gpm = Sponge.getServer().getGameProfileManager();

    public static void removeTabList(TabList tablist, UUID uuid) {
	Optional<TabListEntry> tabListEntry = tablist.getEntry(uuid);
	if (tabListEntry.isPresent()) {
	    tablist.removeEntry(uuid);
	}

    }

    public static TabListEntry addTabList(TabList tablist, UUID uuid, Text playername) {

	CompletableFuture<GameProfile> futureGameProfile = gpm.get(uuid);
	try {
	    GameProfile gp1 = futureGameProfile.get();
	    TabListEntry entry = TabListEntry.builder().list(tablist).profile(gp1).gameMode(GameModes.SURVIVAL)
		    .displayName(playername).build();
	    tablist.addEntry(entry);
	    
	    return entry;
	} catch (InterruptedException | ExecutionException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return null;

    }
    
    public static TabListEntry.Builder addTabList(UUID uuid, Text playername) {

	CompletableFuture<GameProfile> futureGameProfile = gpm.get(uuid);
	try {
	    GameProfile gp1 = futureGameProfile.get();
	    TabListEntry.Builder entry = TabListEntry.builder().profile(gp1).gameMode(GameModes.SURVIVAL)
		    .displayName(playername);
	    return entry;
	} catch (InterruptedException | ExecutionException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return null;

    }
    
    public static TabListEntry addFakeEntry(TabList tablist){
	return null;
    }
    public static void setHeader(Text t){
	tabHeader = t;
    }
    public static void setFooter(Text t){
	tabFooter = t;
    }
    public static Text getHeader(){
	return tabHeader;
    }
    public static Text getFooter(){
	return tabFooter;
    }



}
