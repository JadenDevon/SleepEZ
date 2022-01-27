package com.devonsreach.sleepez.commands;

import com.devonsreach.sleepez.SleepEZ;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetTimeLapse implements CommandExecutor {

    private final SleepEZ plugin;

    public SetTimeLapse(SleepEZ pl) {
        plugin = pl;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length != 1) {
            return false;
        }

        boolean timeLapse;
        if (args[0].toLowerCase().equals("true")) {
            timeLapse = true;
        } else if (args[0].toLowerCase().equals("false")) {
            timeLapse = false;
        } else {
            return false;
        }

        plugin.getConfig().set("Time-Lapse", timeLapse);
        plugin.saveConfig();
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            player.sendMessage(ChatColor.GREEN + "Time-Lapse has been set to " + timeLapse);
        } else {
            plugin.getLogger().info("Time-Lapse has be set to " + timeLapse);
        }
        return true;
    }
}
