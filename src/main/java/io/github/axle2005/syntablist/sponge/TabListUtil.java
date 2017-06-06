package io.github.axle2005.syntablist.sponge;

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

public class TabListUtil {

    final static GameProfileManager gpm = Sponge.getServer().getGameProfileManager();

    public static void removeTabList(TabList tablist, UUID uuid) {
	Optional<TabListEntry> tabListEntry = tablist.getEntry(uuid);
	if (tabListEntry.isPresent()) {
	    tablist.removeEntry(uuid);
	}

    }

    public static TabListEntry addTabList(TabList tablist, UUID uuid, Text playername, String server) {

	CompletableFuture<GameProfile> futureGameProfile = gpm.get(uuid);
	try {
	    GameProfile gp1 = futureGameProfile.get();
	    TabListEntry entry = TabListEntry.builder().list(tablist).profile(gp1).gameMode(GameModes.SURVIVAL)
		    .displayName(playername).build();
	    return entry;
	} catch (InterruptedException | ExecutionException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return null;

    }

}
