package com.devonsreach.sleepez.configs;

import com.devonsreach.sleepez.SleepEZ;

import java.io.File;
import java.util.logging.Logger;

public class MainConfig extends SleepEZConfiguration{

    private final SleepEZ plugin;
    private final Logger logger;

    private final static String FILE_NAME = "config.yml";
    private final static String KEY_USE_PERCENT_OR_NUMBER = "Use Percent or Number";
    private final static String KEY_PERCENT_OF_PLAYERS = "Percent of Players";
    private final static String KEY_NUMBER_OF_SLEEPERS = "Number of Sleepers";
    private final static String KEY_TIME_LAPSE = "Time-Lapse";
    private final static String KEY_TIME_LAPSE_SPEED = "Time-Lapse Speed";
    private final static String KEY_ALLOW_EXIT_BED_DURING_TIME_LAPSE = "Allow Exit Bed During Time-Lapse";
    private final static String KEY_ALLOW_ENTER_BED_DURING_TIME_LAPSE = "Allow Enter Bed During Time-Lapse";
    private final static String KEY_END_STORM = "End Storm";
    private final static String KEY_ALLOW_UNSAFE_SLEEP = "Allow Unsafe Sleep";

    private String usePercentOrNumber;
    private int percentOfPlayers;
    private int numberOfSleepers;
    private boolean timeLapse;
    private int timeLapseSpeed;
    private boolean allowExitBedDuringTimeLapse;
    private boolean allowEnterBedDuringTimeLapse;
    private boolean endStorm;
    private boolean allowUnsafeSleep;

    public MainConfig(SleepEZ pl) {
        super(pl, new File(pl.getDataFolder(), FILE_NAME));
        plugin = pl;
        logger = pl.getLogger();
    }

    public void setup() {
        super.setup();
    }

    public void loadConfigVariables() {
        usePercentOrNumber = getString(KEY_USE_PERCENT_OR_NUMBER);
        percentOfPlayers = getInt(KEY_PERCENT_OF_PLAYERS);
        numberOfSleepers = getInt(KEY_NUMBER_OF_SLEEPERS);
        timeLapse = getBoolean(KEY_TIME_LAPSE);
        timeLapseSpeed = getInt(KEY_TIME_LAPSE_SPEED);
        allowExitBedDuringTimeLapse = getBoolean(KEY_ALLOW_EXIT_BED_DURING_TIME_LAPSE);
        allowEnterBedDuringTimeLapse = getBoolean(KEY_ALLOW_ENTER_BED_DURING_TIME_LAPSE);
        endStorm = getBoolean(KEY_END_STORM);
        allowUnsafeSleep = getBoolean(KEY_ALLOW_UNSAFE_SLEEP);
    }

    public void reload() {
        super.reload();
        loadConfigVariables();
    }

    public String getUsePercentOrNumber() {
        return usePercentOrNumber;
    }

    public void setUsePercentOrNumber(String newValue) {
        if (checkUsePercentOrNumber(newValue)) {
            setString(KEY_USE_PERCENT_OR_NUMBER, this.usePercentOrNumber, newValue);
            usePercentOrNumber = newValue;
        }
    }

    private boolean checkUsePercentOrNumber(String newValue) {
        if (!newValue.equalsIgnoreCase("PERCENT") && !newValue.equalsIgnoreCase("NUMBER")) {
            return false;
        } else {
            return true;
        }
    }

    public int getPercentOfPlayers() {
        return percentOfPlayers;
    }

    public void setPercentOfPlayers(int newValue) {
        if (checkPercentOfPlayers(newValue)) {
            setInt(KEY_PERCENT_OF_PLAYERS, percentOfPlayers, newValue);
            percentOfPlayers = newValue;
        }
    }

    private boolean checkPercentOfPlayers(int newValue) {
        if (newValue >= 0 && newValue <= 100) {
            return true;
        } else {
            return false;
        }
    }

    public int getNumberOfSleepers() {
        return numberOfSleepers;
    }

    public void setNumberOfSleepers(int newValue) {
        if (checkNumberOfSleepers(newValue)) {
            setInt(KEY_NUMBER_OF_SLEEPERS, numberOfSleepers, newValue);
            numberOfSleepers = newValue;
        }
    }

    private boolean checkNumberOfSleepers(int newValue) {
        if (newValue >= 0) {
            return true;
        }
        return false;
    }

    public boolean isTimeLapse() {
        return timeLapse;
    }

    public void setTimeLapse(boolean newValue) {
        setBoolean(KEY_TIME_LAPSE, timeLapse, newValue);
        timeLapse = newValue;
    }

    public int getTimeLapseSpeed() {
        return timeLapseSpeed;
    }

    public void setTimeLapseSpeed(int newValue) {
        if (checkTimeLapseSpeed(newValue)) {
            setInt(KEY_TIME_LAPSE_SPEED, timeLapseSpeed, newValue);
            timeLapseSpeed = newValue;
        }
    }

    private boolean checkTimeLapseSpeed(int newValue) {
        if (newValue >= 1 && newValue <= 10) {
            return true;
        }
        return false;
    }

    public boolean isAllowExitBedDuringTimeLapse() {
        return allowExitBedDuringTimeLapse;
    }

    public void setAllowExitBedDuringTimeLapse(boolean newValue) {
        setBoolean(KEY_ALLOW_EXIT_BED_DURING_TIME_LAPSE, allowExitBedDuringTimeLapse, newValue);
        allowExitBedDuringTimeLapse = newValue;
    }

    public boolean isAllowEnterBedDuringTimeLapse() {
        return allowEnterBedDuringTimeLapse;
    }

    public void setAllowEnterBedDuringTimeLapse(boolean newValue) {
        setBoolean(KEY_ALLOW_ENTER_BED_DURING_TIME_LAPSE, allowEnterBedDuringTimeLapse, newValue);
        allowEnterBedDuringTimeLapse = newValue;
    }

    public boolean isEndStorm() {
        return endStorm;
    }

    public void setEndStorm(boolean newValue) {
        setBoolean(KEY_END_STORM, endStorm, newValue);
        endStorm = newValue;
    }

    public boolean isAllowUnsafeSleep() {
        return allowUnsafeSleep;
    }

    public void setAllowUnsafeSleep(boolean newValue) {
        setBoolean(KEY_ALLOW_UNSAFE_SLEEP, allowUnsafeSleep, newValue);
        allowUnsafeSleep = newValue;
    }
}
