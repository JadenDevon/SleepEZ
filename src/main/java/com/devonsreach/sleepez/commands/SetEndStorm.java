package com.devonsreach.sleepez.commands;

import com.devonsreach.sleepez.SleepEZ;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetEndStorm implements CommandExecutor {

    private final SleepEZ plugin;

    public SetEndStorm(SleepEZ pl) {
        plugin = pl;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length != 1) {
            return false;
        }

        boolean endStorm;
        if (args[0].toLowerCase().equals("true")) {
            endStorm = true;
        } else if (args[0].toLowerCase().equals("false")) {
            endStorm = false;
        } else {
            return false;
        }

        plugin.getConfig().set("End Storm", endStorm);
        plugin.saveConfig();

        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            player.sendMessage(ChatColor.GREEN + "End Storm has been set to " + endStorm);
        } else {
            plugin.getLogger().info("End Storm has be set to " + endStorm);
        }
        return true;
    }
}
