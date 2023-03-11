package com.github.pocketkid2.randomcraft.randomcraft;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class RandomCraftCommand implements CommandExecutor {

	private RandomCraftPlugin plugin;

	public RandomCraftCommand(RandomCraftPlugin p) {
		plugin = p;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!sender.hasPermission("randomcraft.command"))
			return false;
		plugin.createMapping();
		sender.sendMessage("Random item mappings have been reset!");
		return true;
	}

}
