package com.devonsreach.sleepez.configs;

import com.devonsreach.sleepez.SleepEZ;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class SleepEZConfiguration {

    private final SleepEZ plugin;
    private final Logger logger;

    private final File configFile;
    private Configuration config;
    private Configuration defaults;

    public SleepEZConfiguration(SleepEZ pl, File file) {
        plugin = pl;
        logger = pl.getLogger();
        configFile = file;
    }

    public void setup() {
        InputStream resourceStream = plugin.getResource(configFile.getName());
        InputStreamReader streamReader = new InputStreamReader(resourceStream);
        defaults = YamlConfiguration.loadConfiguration(streamReader);
        if (!configFile.exists()) {
            plugin.saveResource(configFile.getName(), false);
        } else {
            config = YamlConfiguration.loadConfiguration(configFile);
            reload();
        }
    }

    void reload() {
        if (needsUpdate()) {
            updateConfig();
        }
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    private boolean needsUpdate() {
        if (!(config.getString("Version") == null) && !config.getString("Version").equals(defaults.getString("Version"))) {
            return true;
        }
        for (String key : defaults.getKeys(true)) {
            if (!config.contains(key)) {
                return true;
            }
        }
        return false;
    }

    private void updateConfig() {
        if (configFile.exists()) {
            plugin.saveResource(configFile.getName(), true);
        }
        for (String key : config.getKeys(true)) {
            if (key.equalsIgnoreCase("version")) {
                save(key, config.get(key), plugin.getDescription().getVersion());
                continue;
            }
            if (config.contains(key)) {
                save(key, defaults.getString(key), config.getString(key));
            }
        }
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    private void save(final String key, final Object oldValue, final Object newValue) {
        List<String> lines = new ArrayList<>();
        String line;
        try {
            FileReader fileReader = new FileReader(configFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while ((line = bufferedReader.readLine()) != null) {
                if (line.startsWith(key + ":") && line.contains(oldValue.toString())) {
                    line = line.replace(oldValue.toString(), newValue.toString());
                }
                lines.add(line);
            }
            fileReader.close();
            bufferedReader.close();

            FileWriter fileWriter = new FileWriter(configFile);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            for (String s : lines) {
                bufferedWriter.write(s + "\n");
            }
            bufferedWriter.flush();
            bufferedWriter.close();
            fileWriter.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void saveDefault(String key) {
        save(key, config.getString(key), defaults.getString(key));
    }

    public String getString(final String key) {
        return config.getString(key);
    }

    public int getInt(final String path) {
        return config.getInt(path);
    }

    public double getDouble(final String key) {
        return config.getDouble(key);
    }

    public boolean getBoolean(final String key) {
        return config.getBoolean(key);
    }

    public void setString(final String key, final String oldValue, final String newValue) {
        save(key, oldValue, newValue);
    }

    public void setInt(final String key, final int oldValue, final int newValue) {
        save(key, oldValue, newValue);
    }

    public void setDouble(final String key, final double oldValue, final double newValue) {
        save(key, oldValue, newValue);
    }

    public void setBoolean(final String key, final boolean oldValue, final boolean newValue) {
        save(key, oldValue, newValue);
    }
}
