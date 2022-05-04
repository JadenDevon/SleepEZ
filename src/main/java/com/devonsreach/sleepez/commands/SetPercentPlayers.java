package com.devonsreach.sleepez.commands;

import com.devonsreach.sleepez.SleepEZ;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetPercentPlayers implements CommandExecutor {

    private final SleepEZ plugin;

    public SetPercentPlayers(SleepEZ pl) {
        plugin = pl;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        int percent;

        if (args.length != 1){
            return false;
        }

        try {
            percent = Integer.parseInt(args[0]);
        }
        catch (NumberFormatException ex){
            return false;
        }

        if (percent == plugin.config.getPercentOfPlayers()) {
            if (commandSender instanceof Player) {
                Player player = (Player) commandSender;
                player.sendMessage(ChatColor.GREEN + "Percent of player required to sleep is already set to " + percent);
            } else {
                plugin.getLogger().info("Percent of players required to sleep is already set to " + percent);
            }
            return true;
        }

        if (percent >= 0 && percent <= 100) {
            plugin.config.setPercentOfPlayers(percent);
            if (commandSender instanceof Player) {
                Player player = (Player) commandSender;
                player.sendMessage(ChatColor.GREEN + "Percent of players required to sleep is now " + percent);
            } else {
                plugin.getLogger().info("Percent of players required to sleep is now " + percent);
            }
            return true;
        } else {
            return false;
        }
    }
}
