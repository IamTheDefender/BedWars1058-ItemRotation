package me.defender.itemrotation.api.utils;

import com.andrei1058.bedwars.api.language.Language;
import me.defender.itemrotation.items.*;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.Collections;

public class StartupUtils {


    public static void saveNameAndDescription(){
        Language.saveIfNotExists("item-rotation.item-name", "&aRotating Items");
        Language.saveIfNotExists("item-rotation.menu-title", "Rotating Items");
        Language.saveIfNotExists("item-rotation.item-lore", Collections.singletonList("&eClick to view!"));
        Language.saveIfNotExists("item-rotation.info-paper.name", "&aWhat are Rotating Items?");
        Language.saveIfNotExists("item-rotation.info-paper.lore", Arrays.asList("&7Rotating Items are items that", "&7only available for a limited", "&7amount of time. They may", "&7disappear and be replaced with", "&7another temporary item at any", "&7time."));
        Language.saveIfNotExists("item-rotation.Bridge-Zapper.deny", "&cYou need to click on a wool block!");
    }

    public static void registerItems(){
        new SugarkenRotationItem().register();
        new SuperCookieRotationItem().register();
        new IceBridgeRotationItem().register();
        new BridgeZapperRotationItem().register();
        new FestivityMineRotationItem().register();
        new UnicornRotationItem().register();
    }
   

    public static void addValuesToConfig(){
        ConfigUtils config = new ConfigUtils();
        config.saveValueIfNotExists("Settings.EnableInSpecificGroups.enabled", true);
        config.saveValueIfNotExists("Settings.EnableInSpecificGroups.groups", Arrays.asList("Solo", "Doubles", "3v3v3v3"));

        config.saveValueIfNotExists("Items." + new BridgeZapperRotationItem().defaultName() + ".radius", 5);
        config.saveValueIfNotExists("Items." + new IceBridgeRotationItem().defaultName() + ".block-break.radius", 2);
        config.saveValueIfNotExists("Items." + new SugarkenRotationItem().defaultName() + ".star.speed", 1.0);
        config.saveValueIfNotExists("Items." + new SugarkenRotationItem().defaultName() + ".star.max-range", 100);
        config.saveValueIfNotExists("Items." + new SugarkenRotationItem().defaultName() + ".star.damage", 5.0);
        config.saveValueIfNotExists("Items." + new SugarkenRotationItem().defaultName() + ".star.item", Material.NETHER_STAR.toString());
    }

}
