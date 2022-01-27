package com.devonsreach.sleepez.misc;

import com.devonsreach.sleepez.SleepEZ;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class SleeperList {

    private ArrayList<Player> list = new ArrayList<>();

    public SleeperList(SleepEZ plugin) {
        // Check in case of plugin reload by server while players are trying to sleep
        if (list.isEmpty()) {
            for (Player player : plugin.getServer().getOnlinePlayers()) {
                if (player.isSleeping()) {
                    if (!list.contains(player)) {
                        add(player);
                    }
                }
            }
        }
    }

    public int getLength() {
        return list.size();
    }

    public void add(Player player) {
        list.add(player);
    }

    public void remove(Player player) {
        list.remove(player);
    }
}
