package io.github.axle2005.syntablist.sponge.commands;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

import io.github.axle2005.syntablist.sponge.SynTabList;

public class CommandRegister {

	SynTabList plugin; 
	public CommandRegister(SynTabList plugin)
	{
		CommandSpec run = CommandSpec.builder().permission("syntablist.hide").description(Text.of("Hides staff member from tablist"))
				.arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("true/false"))))
				.executor(new CommandHide()).build();

		CommandSpec syntablist = CommandSpec.builder().description(Text.of("SynTabList Commands")).child(run, "hide")
				.build();

		Sponge.getCommandManager().register(plugin, syntablist, "syntablist", "stl");
	}
	
}
