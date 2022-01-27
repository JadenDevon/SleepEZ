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

        //Check if it is still night when the player leaves their bed
        if (sleepingPlayer.getWorld().getTime() > 12541 && sleepingPlayer.getWorld().getTime() < 23458) {
            for (Player player : sleepingPlayer.getWorld().getPlayers()) {
                player.sendMessage(sleepingPlayer.getDisplayName() + " is no longer trying to sleep.");
            }
        }
    }
}