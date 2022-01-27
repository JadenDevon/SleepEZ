package com.devonsreach.sleepez.event.player;

import com.devonsreach.sleepez.SleepEZ;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.TimeSkipEvent;

public class CancelSleepTimeSkip implements Listener {

    private final SleepEZ plugin;

    public CancelSleepTimeSkip(SleepEZ pl) { plugin = pl; }

    @EventHandler
    public void onTimeSkipEvent(TimeSkipEvent event) {
        if (event.getSkipReason().equals(TimeSkipEvent.SkipReason.NIGHT_SKIP)){
            if (plugin.getConfig().getBoolean("Time-Lapse")){
                event.setCancelled(true);
            }
        }
    }
}
