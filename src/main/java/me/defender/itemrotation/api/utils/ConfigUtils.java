package me.defender.itemrotation.api.utils;

import me.defender.itemrotation.API;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class ConfigUtils {

    private final FileConfiguration config = API.getMain().getConfig();

    /**
     * Saves a value to the configuration file.
     *
     * @param key The key to save the value under.
     * @param value The value to save.
     */
    public void saveValue(String key, Object value) {
        config.set(key, value);
        API.getMain().saveConfig();
    }

    /**
     * Saves a value to the configuration file if the key does not already exist.
     *
     * @param key The key to save the value under.
     * @param value The value to save.
     */
    public void saveValueIfNotExists(String key, Object value) {
        if (!config.contains(key)) {
            config.set(key, value);
            API.getMain().saveConfig();
        }
    }

    /**
     * Gets an object from the configuration file.
     *
     * @param key The key of the value to retrieve.
     * @return The value associated with the key, or null if the key does not exist.
     */
    public Object getObject(String key) {
        return config.get(key);
    }

    /**
     * Gets a string from the configuration file.
     *
     * @param key The key of the string to retrieve.
     * @return The string associated with the key, or null if the key does not exist or is not a string.
     */
    public String getString(String key) {
        return config.getString(key);
    }

    /**
     * Gets an integer from the configuration file.
     *
     * @param key The key of the integer to retrieve.
     * @return The integer associated with the key, or 0 if the key does not exist or is not an integer.
     */
    public int getInt(String key) {
        return config.getInt(key);
    }

    /**
     * Gets a boolean from the configuration file.
     *
     * @param key The key of the boolean to retrieve.
     * @return The boolean associated with the key, or false if the key does not exist or is not a boolean.
     */
    public boolean getBoolean(String key) {
        return config.getBoolean(key);
    }

    /**
     * Gets a list from the configuration file.
     *
     * @param key The key of the list to retrieve.
     * @return The list associated with the key, or an empty list if the key does not exist or is not a list.
     */
    public List<String> getStringList(String key) {
        return config.getStringList(key);
    }

    /**
     * Gets a double from the configuration file.
     *
     * @param key The key of the double to retrieve.
     * @return The double associated with the key, or 0 if the key does not exist or is not a double.
     */
    public double getDouble(String key) {
        return config.getDouble(key);
    }
}
