package me.defender.itemrotation;

import com.andrei1058.bedwars.api.language.Language;
import com.hakan.core.HCore;
import me.defender.itemrotation.api.utils.StartupUtils;
import me.defender.itemrotation.command.MainCommand;
import me.defender.itemrotation.listeners.onRightClick;
import me.defender.itemrotation.listeners.onShopOpen;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class ItemRotation extends JavaPlugin{

    @Override
    public void onEnable() {
        if (!Bukkit.getPluginManager().isPluginEnabled("BedWars1058")) {
            getLogger().severe("BedWars1058 was not found or is not enabled. Disabling ItemRotation plugin...");
            Bukkit.getPluginManager().disablePlugin(this);
        }
        getLogger().info("Loading ItemRotation plugin...");
        try {
            // Register event listeners and initialize variables
            HCore.initialize(this);
            HCore.registerListeners(new onShopOpen());
            HCore.registerListeners(new onRightClick());
            API.items = new ArrayList<>();

            // Save default config and language file values
            getConfig().options().copyDefaults(true);
            saveDefaultConfig();
            StartupUtils.saveNameAndDescription();

            // Register rotation items and update selected item
            StartupUtils.registerItems();
            API.updateSelectedItem(API.items, getConfig());

            // Register commands and add config values
            HCore.registerCommands(new MainCommand());
            StartupUtils.addValuesToConfig();
            Language.saveIfNotExists("actionbar-itemrotation", "&aYou have %s% seconds left!");

            getLogger().info("ItemRotation plugin loaded successfully.");
        } catch (Exception e) {
            getLogger().severe("An error occurred while loading the ItemRotation plugin: " + e.getMessage());
            e.printStackTrace();
        }

    }

    @Override
    public void onDisable() {
        getLogger().info("Shutting down ItemRotation plugin...");
        // Unregister event listeners
        HandlerList.unregisterAll(this);

        // Clear lists and variables
        API.items.clear();
        API.items = null;

        getLogger().info("ItemRotation plugin shut down successfully.");
    }

}
