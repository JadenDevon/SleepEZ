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
        int numPlayers;

        if (args.length != 1){
            return false;
        }

        try {
            numPlayers = Integer.parseInt(args[0]);
        }
        catch (NumberFormatException ex){
            return false;
        }

        if (numPlayers >= 0) {
            plugin.getConfig().set("Number of Sleepers", numPlayers);
            plugin.saveConfig();
            if (commandSender instanceof Player) {
                Player player = (Player) commandSender;
                player.sendMessage(ChatColor.GREEN + "Number of players required to sleep is now " + numPlayers);
            } else {
                plugin.getLogger().info("Number of players required to sleep is now " + numPlayers);
            }
            return true;
        } else {
            return false;
        }
    }
}
