package com.devonsreach.sleepez.commands;

import com.devonsreach.sleepez.SleepEZ;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ToggleNumberPercent implements CommandExecutor {

    private final SleepEZ plugin;

    public ToggleNumberPercent(SleepEZ pl) {
        plugin = pl;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length > 0) {
            return false;
        }

        if (plugin.config.getUsePercentOrNumber().equalsIgnoreCase("PERCENT")) {
            plugin.config.setUsePercentOrNumber("NUMBER");
            if (commandSender instanceof Player) {
                Player player = (Player) commandSender;
                player.sendMessage(ChatColor.GREEN + "SleepEZ will now use NUMBER of players sleeping.");
            } else {
                plugin.getLogger().info("SleepEZ will now use NUMBER of players sleeping.");
            }
            return true;
        } else if (plugin.config.getUsePercentOrNumber().equalsIgnoreCase("NUMBER")) {
            plugin.config.setUsePercentOrNumber("PERCENT");
            if (commandSender instanceof Player) {
                Player player = (Player) commandSender;
                player.sendMessage(ChatColor.GREEN + "SleepEZ will now use PERCENT of players sleeping.");
            } else {
                plugin.getLogger().info("SleepEZ will now use PERCENT of players sleeping");
            }
            return true;
        } else {
            return false;
        }
    }
}
