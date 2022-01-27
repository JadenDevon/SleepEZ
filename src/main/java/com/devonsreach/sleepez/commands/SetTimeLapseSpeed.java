package com.devonsreach.sleepez.commands;

import com.devonsreach.sleepez.SleepEZ;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetTimeLapseSpeed implements CommandExecutor {

    private final SleepEZ plugin;

    public SetTimeLapseSpeed(SleepEZ pl) {
        plugin = pl;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        int timeLapseSpeed;

        if (args.length == 0){
            return false;
        }

        try {
            timeLapseSpeed = Integer.parseInt(args[0]);
        }
        catch (NumberFormatException ex){
            return false;
        }

        if (timeLapseSpeed >= 1 && timeLapseSpeed <= 10) {
            plugin.getConfig().set("Time-Lapse Speed", timeLapseSpeed);
            plugin.saveConfig();
            if (commandSender instanceof Player) {
                Player player = (Player) commandSender;
                player.sendMessage(ChatColor.GREEN + "Time-Lapse Speed has been set to " + timeLapseSpeed);
            } else {
                plugin.getLogger().info("Time-Lapse Speed has been set to " + timeLapseSpeed);
            }
            return true;
        } else {
            return false;
        }
    }
}
