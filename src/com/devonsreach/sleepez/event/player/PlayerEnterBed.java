package com.devonsreach.sleepez.event.player;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import com.devonsreach.sleepez.SleepEZ;

public class PlayerEnterBed  implements Listener {

    private final SleepEZ plugin;

    public PlayerEnterBed(SleepEZ pl) {
        plugin = pl;
    }

    @EventHandler
    public void onPlayerEnterBed(PlayerBedEnterEvent event) {
        Player sleepingPlayer = event.getPlayer();

        Logger logger = Bukkit.getLogger();

        double numPlayers = 0;
        double numSleepers = 0;

        if(!event.isCancelled()) {
            numSleepers++;
            for(Player player : event.getPlayer().getWorld().getPlayers()) {
                if (!player.hasPermission("sleepez.bypass")) {
                    numPlayers++;
                }
                if(player.isSleeping()) {
                    numSleepers++;
                }
            }
            //never divide by zero
            if(numPlayers < 1) {
                numPlayers = 1;
            }

            double sleepPercent = numSleepers / numPlayers;
            double neededPercent = plugin.getConfig().getDouble("Percentage");

            //valid config check for "Percentage" value
            if(neededPercent > 1 || neededPercent < 0) {
                neededPercent = 0.5;
            }

            int numNeededSleepers = (int) Math.ceil(neededPercent * numPlayers);

            logger.info("[SleepEZ] " + (int) numSleepers + "/" + (int) numPlayers + " are sleeping.");

            if(sleepPercent >= neededPercent) {
                logger.info("[SleepEZ] At least " + (int) (neededPercent *100) + "% of players are sleeping. Skipping night!");

                for(Player player : sleepingPlayer.getWorld().getPlayers()) {
                    player.sendMessage("[SleepEZ] " + sleepingPlayer.getDisplayName() + " is now sleeping.");
                    player.sendMessage("[SleepEZ] At least " + (int) (neededPercent *100) + "% of players are sleeping. Skipping night!");
                }

                setDay(event);

            } else {
                logger.info("[SleepEZ] " + numNeededSleepers + " players still need to sleep.");

                for(Player player : sleepingPlayer.getWorld().getPlayers()) {
                    player.sendMessage("[SleepEZ] " + sleepingPlayer.getDisplayName() + " is now sleeping.");
                    player.sendMessage("[SleepEZ] " + (int) numSleepers + " player(s) are sleeping. At least " + numNeededSleepers + " player(s) need to sleep.");
                }
            }
        }
    }

    private void setDay(PlayerBedEnterEvent event) {
        event.getPlayer().getWorld().setTime(0);
        event.getPlayer().getWorld().setStorm(false);
        event.getPlayer().getWorld().setThundering(false);
    }

}