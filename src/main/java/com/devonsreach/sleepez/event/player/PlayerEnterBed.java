package com.devonsreach.sleepez.event.player;

import com.devonsreach.sleepez.SleepEZ;
import com.devonsreach.sleepez.misc.SleeperList;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Logger;

public class PlayerEnterBed implements Listener {

    private final SleepEZ plugin;

    public PlayerEnterBed(SleepEZ pl) {
        plugin = pl;
    }

    @EventHandler
    public void onPlayerEnterBed(PlayerBedEnterEvent event) {
        Player sleepingPlayer = event.getPlayer();

        if (plugin.getConfig().getBoolean("Allow Unsafe Sleep")) {
            if (event.getBedEnterResult().equals(PlayerBedEnterEvent.BedEnterResult.NOT_SAFE)) {
                event.setUseBed(Event.Result.ALLOW);
            }
        }

        if (event.isCancelled()){
            return;
        }

        if (sleepingPlayer.hasPermission("sleepez.sleepmaster")) {
            setDay(event);
            return;
        }

        plugin.sleeperList.add(sleepingPlayer);

        if (plugin.getConfig().getString("Use Percent or Number").toUpperCase().equals("PERCENT")) {
            percentSleep(event);
        } else if (plugin.getConfig().getString("Use Percent or Number").toUpperCase().equals("NUMBER")) {
            numSleep(event);
        } else {
            plugin.getConfig().set("Use Percent or Number", "PERCENT");
            plugin.saveConfig();
            percentSleep(event);
        }

    }

    private void percentSleep(PlayerBedEnterEvent event) {
        Player sleepingPlayer = event.getPlayer();

        Logger logger = plugin.getLogger();

        int numPlayers = 0;
        int numSleepers = plugin.sleeperList.getLength();

        for (Player player : event.getPlayer().getWorld().getPlayers()) {
            if (!player.hasPermission("sleepez.ignoreplayer")) {
                numPlayers++;
            }
        }
        //never divide by zero
        if (numPlayers < 1) {
            numPlayers = 1;
        }

        logger.info("[SleepEZ] " + numSleepers + "/" + numPlayers + " are sleeping.");

        double sleepPercent = (double) numSleepers/ (double) numPlayers;
        double configPercent = plugin.getConfig().getDouble("Percentage of Players");
        double neededPercent;

        //valid config check for "Percentage" value
        if (configPercent >= 0 && configPercent <= 1) {
            neededPercent = configPercent;
        } else if (configPercent > 1 && configPercent <= 100) {
            neededPercent = (configPercent / 100);
        } else {
            plugin.getConfig().set("Percentage of Players", 50);
            plugin.saveConfig();
            neededPercent = 0.5;
        }

        int numNeededSleepers = (int) Math.ceil(neededPercent * numPlayers);

        if (sleepPercent >= neededPercent) {
            for (Player player : sleepingPlayer.getWorld().getPlayers()) {
                player.sendMessage("[SleepEZ] " + sleepingPlayer.getDisplayName() + " is now sleeping.");
                player.sendMessage("[SleepEZ] At least " + (neededPercent * 100) + "% of players are sleeping. Skipping night!");
            }

            logger.info("At least " + (neededPercent * 100) + "% of players are sleeping. Skipping night!");
            setDay(event);

        } else {
            for (Player player : sleepingPlayer.getWorld().getPlayers()) {
                player.sendMessage("[SleepEZ] " + sleepingPlayer.getDisplayName() + " is now sleeping.");
                player.sendMessage("[SleepEZ] " + numSleepers + " player(s) are sleeping. At least " + numNeededSleepers + " player(s) need to sleep.");
            }
        }
    }

    private void numSleep(PlayerBedEnterEvent event) {
        Player sleepingPlayer = event.getPlayer();

        Logger logger = plugin.getLogger();

        int numPlayers = 0;
        int numSleepers = plugin.sleeperList.getLength();
        int neededSleepers = plugin.getConfig().getInt("Number of Sleepers");

        for (Player player : sleepingPlayer.getWorld().getPlayers()) {
            numPlayers++;
        }

        if (numSleepers == numPlayers) {
            for (Player player : sleepingPlayer.getWorld().getPlayers()) {
                player.sendMessage("[SleepEZ] All players are sleeping.");
            }
            setDay(event);
            return;
        }

        if (numSleepers >= neededSleepers) {
            for (Player player : sleepingPlayer.getWorld().getPlayers()) {
                player.sendMessage("[SleepEZ] " + numSleepers + "/" + neededSleepers + " needed players are sleeping. Skipping night.");
            }
            logger.info(numSleepers + "/" + neededSleepers + " needed players are sleeping. Skipping night");
            setDay(event);
        } else {
            for (Player player : sleepingPlayer.getWorld().getPlayers()) {
                player.sendMessage("[SleepEZ] " + sleepingPlayer.getDisplayName() + " is now sleeping.");
                player.sendMessage("[SleepEZ] " + (neededSleepers - numSleepers) + " player(s) still need to sleep.");
            }
        }
    }

    private void setDay(PlayerBedEnterEvent event) {
        if (plugin.getConfig().getBoolean("End Storm")) {
            endStorm(event);
        }
        if (plugin.getConfig().getBoolean("Time-Lapse")) {
            timeLapse(event);
        } else {
            if (event.getPlayer().getWorld().getTime() > 12541 && event.getPlayer().getWorld().getTime() < 23458){
                event.getPlayer().getWorld().setTime(0);
            }
        }
    }

    private void endStorm(PlayerBedEnterEvent event) {
        event.getPlayer().getWorld().setStorm(false);
        event.getPlayer().getWorld().setThundering(false);
    }

    private void timeLapse(PlayerBedEnterEvent event) {
        long currentTime = event.getPlayer().getWorld().getTime();
        int timeLapseSpeed = plugin.getConfig().getInt("Time-Lapse Speed");
        if (timeLapseSpeed < 1 || timeLapseSpeed > 10) {
            timeLapseSpeed = 5;
            plugin.getConfig().set("Time-Lapse Speed", timeLapseSpeed);
            plugin.saveConfig();
        }
        if(currentTime > 12541 && currentTime < 23458) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    double timeLapseSpeed = plugin.getConfig().getDouble("Time-Lapse Speed") * 40;
                    long currentTime = event.getPlayer().getWorld().getTime();
                    if (currentTime > 12541 && currentTime < (23999 - timeLapseSpeed)) {
                        long t = currentTime + (long) timeLapseSpeed;
                        event.getPlayer().getWorld().setTime(t);
                    } else {
                        this.cancel();
                    }
                }
            }.runTaskTimer(plugin, 0, 1);
        }
    }
}