package com.devonsreach.sleepez.commands;

import com.devonsreach.sleepez.SleepEZ;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ToggleTimeLapse implements CommandExecutor {

    private final SleepEZ plugin;

    public ToggleTimeLapse(SleepEZ pl) {
        plugin = pl;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length > 0) {
            return false;
        }

        if (plugin.config.isTimeLapse()) {
            plugin.config.setTimeLapse(false);
            if (commandSender instanceof Player) {
                Player player = (Player) commandSender;
                player.sendMessage(ChatColor.GREEN + "SleepEZ will no longer do a Time-Lapse effect.");
            } else {
                plugin.getLogger().info("SleepEZ will no longer perform a Time-Lapse effect.");
            }
        } else {
            plugin.config.setTimeLapse(true);
            if (commandSender instanceof Player) {
                Player player = (Player) commandSender;
                player.sendMessage(ChatColor.GREEN + "SleepEZ will now perform a Time-Lapse effect.");
            } else {
                plugin.getLogger().info("SleepEZ will now perform a Time-Lapse effect.");
            }
        }
        return true;
    }
}
