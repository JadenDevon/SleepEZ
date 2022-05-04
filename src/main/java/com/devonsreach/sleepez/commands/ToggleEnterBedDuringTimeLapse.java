package com.devonsreach.sleepez.commands;

import com.devonsreach.sleepez.SleepEZ;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ToggleEnterBedDuringTimeLapse implements CommandExecutor {

    private final SleepEZ plugin;

    public ToggleEnterBedDuringTimeLapse(SleepEZ pl) {
        plugin = pl;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length > 0) {
            return false;
        }

        if (plugin.config.isAllowEnterBedDuringTimeLapse()) {
            plugin.config.setAllowEnterBedDuringTimeLapse(false);
            if (commandSender instanceof Player) {
                Player player = (Player) commandSender;
                player.sendMessage(ChatColor.GREEN + "SleepEZ will no longer allow players to enter their beds during a time-lapse.");
            } else {
                plugin.getLogger().info("AllowEnterBedDuringTimeLapse has been set to FALSE.");
            }
        } else {
            plugin.config.setAllowEnterBedDuringTimeLapse(true);
            if (commandSender instanceof Player) {
                Player player = (Player) commandSender;
                player.sendMessage(ChatColor.GREEN + "SleepEZ will now allow players to enter their beds during a time-lapse.");
            } else {
                plugin.getLogger().info("AllowEnterBedDuringTimeLapse has been set to FALSE.");
            }
        }
        return true;
    }
}
