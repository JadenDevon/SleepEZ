package com.devonsreach.sleepez.commands;

import com.devonsreach.sleepez.SleepEZ;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ToggleEndStorm implements CommandExecutor {

    private final SleepEZ plugin;

    public ToggleEndStorm(SleepEZ pl) {
        plugin = pl;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length > 0) {
            return false;
        }

        if (plugin.config.isEndStorm()) {
            plugin.config.setEndStorm(false);
            if (commandSender instanceof Player) {
                Player player = (Player) commandSender;
                player.sendMessage(ChatColor.GREEN + "SleepEZ will no longer allow players to sleep during a storm.");
            } else {
                plugin.getLogger().info("SleepEZ will no longer allow players to sleep during a storm.");
            }
        } else {
            plugin.config.setEndStorm(true);
            if (commandSender instanceof Player) {
                Player player = (Player) commandSender;
                player.sendMessage(ChatColor.GREEN + "SleepEZ will now allow players to sleep during a storm.");
            } else {
                plugin.getLogger().info("SleepEZ will now allow sleep players to during a storm.");
            }
        }
        return true;
    }
}