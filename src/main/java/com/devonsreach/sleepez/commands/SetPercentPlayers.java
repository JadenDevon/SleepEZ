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
        double percent;

        if (args.length != 1){
            return false;
        }

        try {
            percent = Double.parseDouble(args[0]);
        }
        catch (NumberFormatException ex){
            return false;
        }

        if (percent >= 0 && percent <= 100) {
            plugin.getConfig().set("Percentage of Players", percent);
            plugin.saveConfig();
            if (commandSender instanceof Player) {
                Player player = (Player) commandSender;
                player.sendMessage(ChatColor.GREEN + "Percentage of players required to sleep is now " + percent);
            } else {
                plugin.getLogger().info("Percentage of players required to sleep is now " + percent);
            }
            return true;
        } else {
            return false;
        }
    }
}
