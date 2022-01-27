package com.devonsreach.sleepez.commands;

import com.devonsreach.sleepez.SleepEZ;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetUsePercentPlayers implements CommandExecutor {

    private final SleepEZ plugin;

    public SetUsePercentPlayers(SleepEZ pl) {
        plugin = pl;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length > 0) {
            return false;
        }

        plugin.getConfig().set("Use Percent or Number", "PERCENT");
        plugin.saveConfig();
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            player.sendMessage(ChatColor.GREEN + "SleepEZ will now use PERCENT of players sleeping.");
        } else {
            plugin.getLogger().info("SleepEZ will now use PERCENT of players sleeping");
        }
        return true;
    }
}
