package me.defender.itemrotation;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.hakan.core.HCore;
import me.defender.itemrotation.api.utils.StartupUtils;
import me.defender.itemrotation.command.MainCommand;
import me.defender.itemrotation.listeners.onRightClick;
import me.defender.itemrotation.listeners.onShopOpen;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class ItemRotation extends JavaPlugin {
    private ProtocolManager protocolManager;

    @Override
    public void onEnable() {
        if (!Bukkit.getPluginManager().isPluginEnabled("BedWars1058")) {
            getLogger().severe("BedWars1058 was not found or is not enabled. Disabling ItemRotation plugin...");
            Bukkit.getPluginManager().disablePlugin(this);
        }
        getLogger().info("Loading ItemRotation plugin...");
        try {
            // Register event listeners and initialize variables
            HCore.registerListeners(new onShopOpen());
            HCore.registerListeners(new onRightClick());
            API.items = new ArrayList<>();
            protocolManager = ProtocolLibrary.getProtocolManager();

            // Save default config and language file values
            saveDefaultConfig();
            StartupUtils.saveNameAndDescription();

            // Register rotation items and update selected item
            StartupUtils.registerItems();
            API.updateSelectedItem(API.items, getConfig());

            // Register commands
            HCore.registerCommands(new MainCommand());

            getLogger().info("ItemRotation plugin loaded successfully.");
        } catch (Exception e) {
            getLogger().severe("An error occurred while loading the ItemRotation plugin: " + e.getMessage());
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public ProtocolManager getProtocolManager() {
        return protocolManager;
    }
}
