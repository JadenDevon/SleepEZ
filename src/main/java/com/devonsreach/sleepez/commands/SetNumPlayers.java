package com.devonsreach.sleepez.commands;

import com.devonsreach.sleepez.SleepEZ;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetNumPlayers implements CommandExecutor {

    private final SleepEZ plugin;

    public SetNumPlayers(SleepEZ pl) {
        plugin = pl;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        int numSleepers;

        if (args.length != 1){
            return false;
        }

        try {
            numSleepers = Integer.parseInt(args[0]);
        }
        catch (NumberFormatException ex){
            return false;
        }

        if (numSleepers == plugin.config.getNumberOfSleepers()) {
            if (commandSender instanceof Player) {
                Player player = (Player) commandSender;
                player.sendMessage(ChatColor.GREEN + "Number of players required to sleep is already set to " + numSleepers);
            } else {
                plugin.getLogger().info("Number of players required to sleep is already set to " + numSleepers);
            }
            return true;
        }

        if (numSleepers >= 0) {
            plugin.config.setNumberOfSleepers(numSleepers);
            if (commandSender instanceof Player) {
                Player player = (Player) commandSender;
                player.sendMessage(ChatColor.GREEN + "Number of players required to sleep is now " + numSleepers);
            } else {
                plugin.getLogger().info("Number of players required to sleep is now " + numSleepers);
            }
            return true;
        } else {
            return false;
        }
    }
}
