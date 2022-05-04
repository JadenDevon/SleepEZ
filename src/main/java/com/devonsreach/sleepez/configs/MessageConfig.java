package com.devonsreach.sleepez.configs;

import com.devonsreach.sleepez.SleepEZ;

import java.io.File;

public class MessageConfig extends SleepEZConfiguration{

    private final static String FILE_NAME = "messages.yml";

    private String playerEnterBedMessage;
    private String moreSleepersNeededMessage;
    private String percentTriggeredMessage;
    private String numberTriggeredMessage;
    private String playerExitBedMessage;

    public MessageConfig(SleepEZ pl) {
        super(pl, new File(pl.getDataFolder(), FILE_NAME));
    }

    public void loadMessages() {
        playerEnterBedMessage = getString("Player Enter Bed");
        moreSleepersNeededMessage = getString("More Sleepers Needed");
        percentTriggeredMessage = getString("Sleep Percent Triggered");
        numberTriggeredMessage = getString("Sleep Number Triggered");
        playerExitBedMessage = getString("Player Exit Bed");
    }

    public void setup() {
        super.setup();
        loadMessages();
    }

    public void reload() {
        super.reload();

    }

    public String getPlayerEnterBedMessage() {
        return playerEnterBedMessage;
    }

    public void setPlayerEnterBedMessage(String newValue) {
        setString("Player Enter Bed", playerEnterBedMessage, newValue);
    }

    public String getMoreSleepersNeededMessage() {
        return moreSleepersNeededMessage;
    }

    public void setMoreSleepersNeededMessage(String newValue) {
        setString("More Sleepers Needed", moreSleepersNeededMessage, newValue);
    }

    public String getPercentTriggeredMessage() {
        return percentTriggeredMessage;
    }

    public void setPercentTriggeredMessage(String newValue) {
        setString("Sleep Percent Triggered", percentTriggeredMessage, newValue);
    }

    public String getNumberTriggeredMessage() {
        return numberTriggeredMessage;
    }

    public void setNumberTriggeredMessage(String newValue) {
        setString("Sleep Number Triggered", numberTriggeredMessage, newValue);
    }

    public String getPlayerExitBedMessage() {
        return playerExitBedMessage;
    }

    public void setPlayerExitBedMessage(String newValue) {
        setString("Player Exit Bed", playerExitBedMessage, newValue);
    }
}
