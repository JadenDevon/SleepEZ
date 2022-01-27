package com.devonsreach.sleepez.commands;

import com.devonsreach.sleepez.SleepEZ;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetAllowUnsafeSleep implements CommandExecutor {

    private final SleepEZ plugin;

    public SetAllowUnsafeSleep(SleepEZ pl) {
        plugin = pl;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length != 1) {
            return false;
        }
        boolean allowUnsafeSleep;
        if (args[0].toLowerCase().equals("true")) {
            allowUnsafeSleep = true;
        } else if (args[0].toLowerCase().equals("false")) {
            allowUnsafeSleep = false;
        } else {
            return false;
        }
        plugin.getConfig().set("Allow Unsafe Sleep", allowUnsafeSleep);
        plugin.saveConfig();
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            player.sendMessage(ChatColor.GREEN + "Allow Unsafe Sleep has been set to " + allowUnsafeSleep);
        } else {
            plugin.getLogger().info("Allow Unsafe Sleep has been set to " + allowUnsafeSleep);
        }
        return true;
    }
}
