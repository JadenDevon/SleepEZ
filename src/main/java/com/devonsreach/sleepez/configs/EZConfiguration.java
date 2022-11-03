package com.devonsreach.sleepez.configs;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

public class EZConfiguration {

    private final JavaPlugin plugin;
    private final Logger logger;
    private final File serverConfigFile;
    private Configuration serverConfig;
    private Configuration defaultConfig;
    private final ArrayList<String> serverConfigLines;
    private final HashMap<String, Object> serverConfigKeyMap;
    private final HashMap<String, Object> defaultConfigKeyMap;

    public EZConfiguration(JavaPlugin pl, File file) {
        plugin = pl;
        logger = pl.getLogger();
        serverConfigFile = file;
        serverConfigLines = new ArrayList<>();
        serverConfigKeyMap = new HashMap<>();
        defaultConfigKeyMap = new HashMap<>();
        setup();
    }

    public void setup() {
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
            serverConfig = YamlConfiguration.loadConfiguration(serverConfigFile);
        } else {
            serverConfig = YamlConfiguration.loadConfiguration(serverConfigFile);
            checkServerConfig();
        }
    }

    private void initializeKeyMap(HashMap<String, Object> map, Configuration c) {
        for (String key : c.getKeys(true)){
            map.put(key, c.get(key));
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
    }

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
        serverConfig = YamlConfiguration.loadConfiguration(serverConfigFile);
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

    public void saveDefault(final String key) {
        setConfigValue(key, defaultConfig.getString(key));
    }

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

    public void setString(final String key, final String newValue) {
        setConfigValue(key, newValue);
    }

    public void setInt(final String key, final int newValue) {
        setConfigValue(key, newValue);
    }

    public void setDouble(final String key, final double newValue) {
        setConfigValue(key, newValue);
    }

    public void setBoolean(final String key, final boolean newValue) {
        setConfigValue(key, newValue);
    }

    public String getString(final String key) {
        return serverConfig.getString(key);
    }

    public int getInt(final String path) {
        return serverConfig.getInt(path);
    }

    public double getDouble(final String key) {
        return serverConfig.getDouble(key);
    }

    public boolean getBoolean(final String key) {
        return serverConfig.getBoolean(key);
    }

    public Object getConfigValue(final String key) {
        return serverConfigKeyMap.get(key);
    }

    /*
    private void save(final String key, final Object oldValue, final Object newValue) {
        serverConfigLines = new ArrayList<>();
        String line;
        try {
            FileReader fileReader = new FileReader(serverConfigFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while ((line = bufferedReader.readLine()) != null) {
                if (line.startsWith(key + ":") && line.contains(oldValue.toString())) {
                    line = line.replace(oldValue.toString(), newValue.toString());
                }
                serverConfigLines.add(line);
            }
            fileReader.close();
            bufferedReader.close();

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
    */
}
