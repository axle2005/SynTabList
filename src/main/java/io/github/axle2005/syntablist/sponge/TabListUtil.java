package io.github.axle2005.syntablist.sponge;

import java.util.Optional;
import java.util.UUID;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.entity.living.player.tab.TabList;
import org.spongepowered.api.entity.living.player.tab.TabListEntry;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.profile.GameProfileManager;
import org.spongepowered.api.text.Text;

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
	return TabListEntry.builder().list(tablist).profile(GameProfile.of(uuid, "")).gameMode(GameModes.SURVIVAL)
		.displayName(playername).build();
    }

    public static TabListEntry addFakeEntry(TabList tablist, int fakeUUID) {

	return TabListEntry.builder().list(tablist).latency(0)
		.profile(GameProfile.of(UUID.fromString("00000000-0000-0000-0000-000000000001"), ""))
		.gameMode(GameModes.SURVIVAL).displayName(Text.of("")).build();
    }

    public static void setHeader(Text t) {
	tabHeader = t;
    }

    public static void setFooter(Text t) {
	tabFooter = t;
    }

    public static Text getHeader() {
	return tabHeader;
    }

    public static Text getFooter() {
	return tabFooter;
    }

}
