package com.devonsreach.sleepez.commands;

import com.devonsreach.sleepez.SleepEZ;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadConfig implements CommandExecutor {

    private final SleepEZ plugin;

    public ReloadConfig(SleepEZ pl) {
        plugin = pl;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length > 0) {
            return false;
        }
        plugin.reloadConfig();
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            player.sendMessage(ChatColor.GREEN + "SleepEZ's config.yml has been reloaded.");
        } else {
            plugin.getLogger().info("config.yml has been reloaded.");
        }
        return true;
    }
}
