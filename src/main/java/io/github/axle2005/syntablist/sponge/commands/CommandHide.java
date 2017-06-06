package io.github.axle2005.syntablist.sponge.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import io.github.axle2005.syntablist.common.PlayerData.Action;
import io.github.axle2005.syntablist.common.StaffData.Rank;
import io.github.axle2005.syntablist.common.StaffData;
import io.github.axle2005.syntablist.sponge.SynTabList;
import net.kaikk.mc.synx.SynX;

public class CommandHide implements CommandExecutor {

	private static String CHANNEL;

	public CommandHide(SynTabList plugin) {
		CHANNEL = plugin.getChannel();

	}

	@Override
	public CommandResult execute(CommandSource src, CommandContext arguments) throws CommandException {
		String args = arguments.<String>getOne("true/false").get();
		if (src instanceof Player) {
			Player player = (Player) src;

			if (args.equalsIgnoreCase("true")) {
				if (player.hasPermission("syntablist.senioradmin")) {
					StaffData staffData = new StaffData(player.getName(), player.getUniqueId(), Action.QUIT,
							Rank.SENIORADMIN, true);
					SynX.instance().broadcast(CHANNEL, staffData, System.currentTimeMillis() + 60000);
					player.sendMessage(Text.of(TextColors.AQUA, "You are now hidden"));
					return CommandResult.success();
				} else {
					player.sendMessage(Text.of(TextColors.RED, "/stl hide true/false"));
					return CommandResult.empty();
				}
			} else if (args.equalsIgnoreCase("false")) {
				if (player.hasPermission("syntablist.senioradmin")) {
					StaffData staffData = new StaffData(player.getName(), player.getUniqueId(), Action.JOIN,
							Rank.SENIORADMIN,false);
					SynX.instance().broadcast(CHANNEL, staffData, System.currentTimeMillis() + 60000);
					player.sendMessage(Text.of(TextColors.AQUA, "You are now visible"));
					return CommandResult.success();
				} else {
					player.sendMessage(Text.of(TextColors.RED, "/stl hide true/false"));
					return CommandResult.empty();
				}
			}

			else {
				player.sendMessage(Text.of(TextColors.RED, "/stl hide true/false"));
				return CommandResult.empty();
			}

		} else {
			
			return CommandResult.empty();
		}
	}
}
