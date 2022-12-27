package me.defender.itemrotation.api.utils;

import com.andrei1058.bedwars.api.language.Language;
import me.defender.itemrotation.items.BridgeZapperRotationItem;
import me.defender.itemrotation.items.IceBridgeRotationItem;
import me.defender.itemrotation.items.SugarkenRotationItem;
import me.defender.itemrotation.items.SuperCookieRotationItem;

import java.util.Arrays;
import java.util.Collections;

public class StartupUtils {


    public static void saveNameAndDescription(){
        Language.saveIfNotExists("item-rotation.item-name", "&aRotating Items");
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
    }

}
