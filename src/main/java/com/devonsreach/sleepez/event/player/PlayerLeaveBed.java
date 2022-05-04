package com.devonsreach.sleepez.event.player;

import com.devonsreach.sleepez.SleepEZ;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedLeaveEvent;

public class PlayerLeaveBed implements Listener {

    private final SleepEZ plugin;

    public PlayerLeaveBed(SleepEZ pl) {
        plugin = pl;
    }

    @EventHandler
    public void onPlayerLeaveBed(PlayerBedLeaveEvent event) {
        Player sleepingPlayer = event.getPlayer();

        if (!plugin.config.isAllowExitBedDuringTimeLapse()) {
            if (plugin.timeLapseTriggered) {
                event.setCancelled(true);
                return;
            }
        }
        //Check if it is still night when the player leaves their bed
        if (sleepingPlayer.getWorld().getTime() > 12541 && sleepingPlayer.getWorld().getTime() < 23458) {
            String playerExitBedMessage = plugin.messageConfig.getPlayerExitBedMessage();
            if (playerExitBedMessage.contains("[PLAYER]")) {
                playerExitBedMessage = playerExitBedMessage.replaceAll("[PLAYER]", sleepingPlayer.getDisplayName());
            }
            for (Player player : sleepingPlayer.getWorld().getPlayers()) {
                if (playerExitBedMessage.length() > 0) {
                    player.sendMessage(playerExitBedMessage);
                }
            }
        }
        plugin.sleeperList.remove(sleepingPlayer);
    }
}