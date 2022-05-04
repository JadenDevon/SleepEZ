/*
Main handler for server events involving players trying to enter their bed
 */
package com.devonsreach.sleepez.event.player;

import com.devonsreach.sleepez.SleepEZ;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Logger;

public class PlayerEnterBed implements Listener {

    private final SleepEZ plugin;

    private final Logger logger;

    private Player sleepingPlayer;

    private String playerEnterBedMessage;
    private String moreSleepersNeededMessage;
    private String percentTriggeredMessage;
    private String numberTriggeredMessage;
    private int numPlayers;

    public PlayerEnterBed(SleepEZ pl) {
        plugin = pl;
        logger = plugin.getLogger();
    }

    //Event handler for player entering beds
    @EventHandler
    public void onPlayerEnterBed(PlayerBedEnterEvent event) {
        sleepingPlayer = event.getPlayer();

        numPlayers = checkNumPlayers();
        importMessages();
        setMessages();

        //Prevent entering bed during time-lapse if not allowed in config
        if (!plugin.config.isAllowEnterBedDuringTimeLapse()) {
            if (plugin.timeLapseTriggered) {
                event.setUseBed(Event.Result.DENY);
                event.setCancelled(true);
                //sleepingPlayer.sendMessage("You may not enter your bed after the time-lapse has started.");
            }
        }

        //Allows entering bed even when unsafe when specified in config
        if (plugin.config.isAllowUnsafeSleep()) {
            if (event.getBedEnterResult().equals(PlayerBedEnterEvent.BedEnterResult.NOT_SAFE)) {
                event.setUseBed(Event.Result.ALLOW);
            }
        }
        if (event.isCancelled()) {
            return;
        }

        //Skips all number checks if player has sleepmaster permission
        if (sleepingPlayer.hasPermission("sleepez.sleepmaster")) {
            setDay(event);
            return;
        }

        plugin.sleeperList.add(sleepingPlayer);

        if (plugin.config.getUsePercentOrNumber().equalsIgnoreCase("NUMBER")) {
            numSleep(event);
        } else {
            percentSleep(event);
        }
    }

    //Triggered if PERCENT is designated in the config
    private void percentSleep(PlayerBedEnterEvent event) {
        int numSleepers = plugin.sleeperList.size();

        double sleepPercent = (double) numSleepers/ (double) numPlayers;
        double percentMultiplier = ((double) plugin.config.getPercentOfPlayers() / 100);

        int numNeededSleepers = (int) Math.ceil(percentMultiplier * numPlayers) - numSleepers;

        //Triggers setDay() once designated percent of players sleeping is met
        if (sleepPercent >= percentMultiplier) {
            for (Player player : sleepingPlayer.getWorld().getPlayers()) {
                if (playerEnterBedMessage != null) {
                    if (playerEnterBedMessage.length() > 0) {
                        player.sendMessage("[SleepEZ] " + playerEnterBedMessage);
                    }
                }
                if (percentTriggeredMessage != null) {
                    if (percentTriggeredMessage.length() > 0) {
                        player.sendMessage("[SleepEZ] " + percentTriggeredMessage);
                    }
                }
            }
            logger.info("At least " + plugin.config.getPercentOfPlayers() + "% of players are sleeping. Skipping night!");
            setDay(event);
        } else {
            if (moreSleepersNeededMessage.contains("[NUMBER]")) {
                moreSleepersNeededMessage = moreSleepersNeededMessage.replace("[NUMBER]", String.valueOf(numNeededSleepers - numSleepers));
            }
            for (Player player : sleepingPlayer.getWorld().getPlayers()) {
                if (playerEnterBedMessage != null) {
                    if (playerEnterBedMessage.length() > 0) {
                        player.sendMessage("[SleepEZ] " + playerEnterBedMessage);
                    }
                }
                if (moreSleepersNeededMessage != null) {
                    if (moreSleepersNeededMessage.length() > 0) {
                        player.sendMessage("[SleepEZ] " + moreSleepersNeededMessage);
                    }
                }
            }
        }
    }

    //Triggered if NUMBER is designated in the config
    private void numSleep(PlayerBedEnterEvent event) {
        Player sleepingPlayer = event.getPlayer();

        int numSleepers = plugin.sleeperList.size();

        if (numSleepers >= plugin.config.getNumberOfSleepers()) {
            for (Player player : sleepingPlayer.getWorld().getPlayers()) {
                if (playerEnterBedMessage.length() > 0) {
                    player.sendMessage("[SleepEZ] " + playerEnterBedMessage);
                }
                if (numberTriggeredMessage.length() > 0) {
                    player.sendMessage("[SleepEZ] " + numberTriggeredMessage);
                }
            }
            logger.info("Skipping night");
            setDay(event);
        } else if (numSleepers <= sleepingPlayer.getWorld().getPlayers().size()) {
            logger.info("Skipping night");
            setDay(event);
        } else {
            if (moreSleepersNeededMessage != null) {
                if (moreSleepersNeededMessage.contains("[NUMBER]")) {
                    moreSleepersNeededMessage = moreSleepersNeededMessage.replace("[NUMBER]", String.valueOf(plugin.config.getNumberOfSleepers() - numSleepers));
                }
            }
            for (Player player : sleepingPlayer.getWorld().getPlayers()) {
                if (playerEnterBedMessage != null) {
                    if (playerEnterBedMessage.length() > 0) {
                        player.sendMessage("[SleepEZ] " + playerEnterBedMessage);
                    }
                }
                if (moreSleepersNeededMessage != null) {
                    if (moreSleepersNeededMessage.length() > 0) {
                        player.sendMessage("[SleepEZ] " + moreSleepersNeededMessage);
                    }
                }
            }
        }
    }

    //Sets time to day once required number of players are sleeping
    private void setDay(PlayerBedEnterEvent event) {
        if (plugin.config.isEndStorm()) {
            endStorm(event);
        }
        if (!plugin.timeLapseTriggered && plugin.config.isTimeLapse()) {
            timeLapse(event);
        } else {
            if (event.getPlayer().getWorld().getTime() > 12541 && event.getPlayer().getWorld().getTime() < 23458){
                event.getPlayer().getWorld().setTime(0);
            }
        }
    }

    //Ends storm if End Storm is set to true in config
    private void endStorm(PlayerBedEnterEvent event) {
        event.getPlayer().getWorld().setStorm(false);
        event.getPlayer().getWorld().setThundering(false);
    }

    //Initiates time-lapse if set to true in config
    private void timeLapse(PlayerBedEnterEvent event) {
        long currentTime = event.getPlayer().getWorld().getTime();
        if(currentTime > 12541 && currentTime < 23458) {
            //Sets variable to prevent secondary time-lapses.
            plugin.timeLapseTriggered = true;
            new BukkitRunnable() {
                @Override
                public void run() {
                    int timeLapseSpeed = plugin.config.getTimeLapseSpeed() * 20;
                    long currentTime = event.getPlayer().getWorld().getTime();
                    if (currentTime > 12541 && currentTime < (23999 - timeLapseSpeed)) {
                        long t = currentTime + (long) timeLapseSpeed;
                        event.getPlayer().getWorld().setTime(t);
                    } else {
                        //Removes block for time-lapses once time-lapse has completed
                        plugin.timeLapseTriggered = false;
                        this.cancel();
                    }
                }
            }.runTaskTimer(plugin, 0, 1);
        }
    }

    //
    private void importMessages() {
        moreSleepersNeededMessage = plugin.messageConfig.getMoreSleepersNeededMessage();
        playerEnterBedMessage = plugin.messageConfig.getPlayerEnterBedMessage();
        percentTriggeredMessage = plugin.messageConfig.getPercentTriggeredMessage();
        numberTriggeredMessage = plugin.messageConfig.getNumberTriggeredMessage();
    }

    //Replaces variables in messages if variables are used
    private void setMessages() {
        if (playerEnterBedMessage.contains("[PLAYER]")) {
            playerEnterBedMessage = playerEnterBedMessage.replace("[PLAYER]", sleepingPlayer.getDisplayName());
        }
        if (percentTriggeredMessage.contains("[PERCENT]")) {
            percentTriggeredMessage = percentTriggeredMessage.replace("[PERCENT]", String.valueOf(plugin.config.getPercentOfPlayers()));
        }
        if (numberTriggeredMessage.contains("[NUMBER]")) {
            if (plugin.sleeperList.size() >= numPlayers) {
                numberTriggeredMessage = numberTriggeredMessage.replace("[NUMBER]", String.valueOf(plugin.sleeperList.size()));
            } else {
                numberTriggeredMessage = numberTriggeredMessage.replace("[NUMBER]", String.valueOf(plugin.config.getNumberOfSleepers()));
            }
        }
    }

    private int checkNumPlayers() {
        int numPlayers = 0;
        for (Player player : sleepingPlayer.getWorld().getPlayers()) {
            if (!player.hasPermission("sleepez.ignoreplayer")) {
                numPlayers++;
            }
        }
        if (numPlayers < 1) {
            numPlayers = 1;
        }
        return numPlayers;
    }
}