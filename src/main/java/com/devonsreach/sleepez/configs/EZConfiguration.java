package com.devonsreach.sleepez.configs;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 *  EZConfiguration is a standalone drop-in class for Spigot Minecraft plugins that handles all
 *  config values and requests including retrieving updated values from the config file and saving
 *  new values from commands to the config file.
 * @author JadenDevon
 */

public class EZConfiguration {

    private final JavaPlugin plugin;
    private final Logger logger;
    private final File serverConfigFile;
    private Configuration serverConfig;
    private Configuration defaultConfig;
    private final ArrayList<String> serverConfigLines;
    private final HashMap<String, Object> serverConfigKeyMap;
    private final HashMap<String, Object> defaultConfigKeyMap;

    /**
     * Constructor for EZConfiguration Object
     * @param plugin Object of type JavaPlugin matching your main Plugin instance
     * @param file The file that holds your config data
     */
    public EZConfiguration(JavaPlugin plugin, File file) {
        this.plugin = plugin;
        logger = plugin.getLogger();
        serverConfigFile = file;
        serverConfigLines = new ArrayList<>();
        serverConfigKeyMap = new HashMap<>();
        defaultConfigKeyMap = new HashMap<>();
        setup();
    }

    private void setup() {
        initializeDefaultConfiguration();
        initializeServerConfiguration();
        checkServerConfig();
        initializeKeyMap(serverConfigKeyMap, serverConfig);
        initializeKeyMap(defaultConfigKeyMap, defaultConfig);
        initializeServerConfigLinesArray();
        saveServerConfigFile();
    }

    private void initializeDefaultConfiguration() {
        InputStream resourceStream = plugin.getResource(serverConfigFile.getName());
        assert resourceStream != null;
        InputStreamReader streamReader = new InputStreamReader(resourceStream);
        defaultConfig = YamlConfiguration.loadConfiguration(streamReader);
    }

    private void initializeServerConfiguration() {
        if (!serverConfigFile.exists()) {
            plugin.saveResource(serverConfigFile.getName(), true);
        }
        serverConfig = YamlConfiguration.loadConfiguration(serverConfigFile);
    }

    private void initializeKeyMap(HashMap<String, Object> map, Configuration configuration) {
        map.clear();
        for (String key : configuration.getKeys(true)){
            map.put(key, configuration.get(key));
        }
        logger.info(map.toString());
    }

    private void initializeServerConfigLinesArray() {
        String line;
        serverConfigLines.clear();
        try {
            FileReader fileReader = new FileReader(serverConfigFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while ((line = bufferedReader.readLine()) != null) {
                serverConfigLines.add(line);
            }
            fileReader.close();
            bufferedReader.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void checkServerConfig() {
        if (needsUpdate()) {
            updateConfig();
        }
        serverConfig = YamlConfiguration.loadConfiguration(serverConfigFile);
    }

    /**
     *
     */
    public void reload() {
        checkServerConfig();
    }


    private boolean needsUpdate() {
        if (serverConfig.getString("Version") != null &&
                !serverConfig.getString("Version").equals(defaultConfig.getString("Version"))) {
            return true;
        }
        for (String key : defaultConfig.getKeys(true)) {
            if (!serverConfig.contains(key)) {
                return true;
            }
        }
        return false;
    }

    private void updateConfig() {
        if (serverConfigFile.exists()) {
            plugin.saveResource(serverConfigFile.getName(), true);
        }
        for (String key : serverConfig.getKeys(true)) {
            if (key.equalsIgnoreCase("version")) {
                setConfigValue(key,  plugin.getDescription().getVersion());
                continue;
            }
            if (serverConfig.contains(key)) {
                setConfigValue(key, serverConfig.getString(key));
            }
        }
    }

    private void saveServerConfigFile(){
        try {
            FileWriter fileWriter = new FileWriter(serverConfigFile);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            for (String s : serverConfigLines) {
                bufferedWriter.write(s + "\n");
            }
            bufferedWriter.flush();
            bufferedWriter.close();
            fileWriter.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Saves new config values of any generic type Object
     * @param key A String type indicating the config key to have its value changed
     * @param newValue An Object type denoting the replacement config value
     */
    public void setConfigValue(final String key, final Object newValue) {
        final Object currentValue = serverConfigKeyMap.get(key);
        if (serverConfigKeyMap.containsKey(key)) {
            for (String line : serverConfigLines) {
                if (line.startsWith(key)) {
                    serverConfigLines.set(serverConfigLines.indexOf(line),
                            line.replace(currentValue.toString(), newValue.toString()));
                    break;
                }
            }
            serverConfigKeyMap.replace(key, newValue);
        }
        logger.info(key + " has been changed to " + newValue);
        saveServerConfigFile();
    }

    /**
     * Replaces current config value with the default value
     * @param key A String type indicating the config key to have its value changed
     */
    public void setDefaultValue(final String key) {
        setConfigValue(key, defaultConfigKeyMap.get(key));
    }

    /**
     *
     * @param key A String type indicating the config key to have its value changed
     * @param newValue A String type denoting the replacement config value
     */
    public void setString(final String key, final String newValue) {
        setConfigValue(key, newValue);
    }

    /**
     *
     * @param key A String type indicating the config key to have its value changed
     * @param newValue An int type denoting the replacement config value
     */
    public void setInt(final String key, final int newValue) {
        setConfigValue(key, newValue);
    }

    /**
     *
     * @param key A String type indicating the config key to have its value changed
     * @param newValue A double type denoting the replacement config value
     */
    public void setDouble(final String key, final double newValue) {
        setConfigValue(key, newValue);
    }

    /**
     *
     * @param key A String type indicating the config key to have its value changed
     * @param newValue A boolean type denoting the replacement config value
     */
    public void setBoolean(final String key, final boolean newValue) {
        setConfigValue(key, newValue);
    }

    /**
     *
     * @param key A String type indicating the config key to be accessed
     * @return
     */
    public String getString(final String key) {
        return serverConfig.getString(key);
    }

    /**
     *
     * @param key A String type indicating the config key to be accessed
     * @return
     */
    public int getInt(final String key) {
        return serverConfig.getInt(key);
    }

    /**
     *
     * @param key A String type indicating the config key to be accessed
     * @return
     */
    public double getDouble(final String key) {
        return serverConfig.getDouble(key);
    }

    /**
     *
     * @param key A String type indicating the config key to be accessed
     * @return
     */
    public boolean getBoolean(final String key) {
        return serverConfig.getBoolean(key);
    }

    /**
     *
     * @param key A String type indicating the config key to be accessed
     * @return
     */
    public Object getConfigValue(final String key) {
        return serverConfigKeyMap.get(key);
    }
}
