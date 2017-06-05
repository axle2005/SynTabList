package io.github.axle2005.syntablist.sponge;

import org.spongepowered.api.scoreboard.Team;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import static io.github.axle2005.syntablist.sponge.SynTabList.scoreboard;


public class RankTeams {
    public static Team DarkGreen = Team.builder().name("Staff_A").prefix(Text.of(TextColors.DARK_GREEN)).color(TextColors.DARK_GREEN).build();
    public static Team DarkBlue = Team.builder().name("Staff_B").prefix(Text.of(TextColors.DARK_BLUE)).color(TextColors.DARK_BLUE).build();
    public static Team Aqua = Team.builder().name("Staff_C").prefix(Text.of(TextColors.AQUA)).color(TextColors.AQUA).build();
    public static Team LightPurple = Team.builder().name("Staff_D").prefix(Text.of(TextColors.LIGHT_PURPLE)).color(TextColors.LIGHT_PURPLE).build();

    public static Team White = Team.builder().name("Z").prefix(Text.of(TextColors.WHITE)).color(TextColors.WHITE).build();
    
    
    public static Team Black = Team.builder().name("Black").prefix(Text.of(TextColors.BLACK)).color(TextColors.BLACK).build();
    public static Team DarkAqua = Team.builder().name("DarkAqua").prefix(Text.of(TextColors.DARK_AQUA)).color(TextColors.DARK_AQUA).build();
    public static Team DarkRed = Team.builder().name("DarkRed").prefix(Text.of(TextColors.DARK_RED)).color(TextColors.DARK_RED).build();
    public static Team DarkPurple = Team.builder().name("DarkPurple").prefix(Text.of(TextColors.DARK_PURPLE)).color(TextColors.DARK_PURPLE).build();
    public static Team Gold = Team.builder().name("Orange").prefix(Text.of(TextColors.GOLD)).color(TextColors.GOLD).build();
    public static Team Gray = Team.builder().name("Gray").prefix(Text.of(TextColors.GRAY)).color(TextColors.GRAY).build();
    public static Team DarkGray = Team.builder().name("DarkGray").prefix(Text.of(TextColors.DARK_GRAY)).color(TextColors.DARK_GRAY).build();
    public static Team Blue = Team.builder().name("Blue").prefix(Text.of(TextColors.BLUE)).color(TextColors.BLUE).build();
    public static Team Green = Team.builder().name("Green").prefix(Text.of(TextColors.GREEN)).color(TextColors.GREEN).build();
    public static Team Red = Team.builder().name("Red").prefix(Text.of(TextColors.RED)).color(TextColors.RED).build();
    public static Team Yellow = Team.builder().name("Yellow").prefix(Text.of(TextColors.YELLOW)).color(TextColors.YELLOW).build();
    

    public static void registerTeams(){
        /*scoreboard.registerTeam(Black);
        
        
        scoreboard.registerTeam(DarkAqua);
        scoreboard.registerTeam(DarkRed);
        scoreboard.registerTeam(DarkPurple);
        scoreboard.registerTeam(Gold);
        scoreboard.registerTeam(Gray);
        scoreboard.registerTeam(DarkGray);
        scoreboard.registerTeam(Blue);
        scoreboard.registerTeam(Green);
        
        scoreboard.registerTeam(Red);
        
        scoreboard.registerTeam(Yellow);*/
        scoreboard.registerTeam(White);
        
        scoreboard.registerTeam(LightPurple);
        scoreboard.registerTeam(Aqua);
        scoreboard.registerTeam(DarkBlue);
        scoreboard.registerTeam(DarkGreen);
    }
}
