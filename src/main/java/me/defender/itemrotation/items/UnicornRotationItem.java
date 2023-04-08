package me.defender.itemrotation.items;

import com.hakan.core.HCore;
import me.defender.itemrotation.API;
import me.defender.itemrotation.ItemRotation;
import me.defender.itemrotation.api.RotationItem;
import me.defender.itemrotation.api.utils.FlyingHorse;
import me.defender.itemrotation.api.utils.xseries.XMaterial;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class UnicornRotationItem extends RotationItem {
    @Override
    public ItemStack getItem() {
        return XMaterial.HORSE_SPAWN_EGG.parseItem();
    }

    @Override
    public String defaultName() {
        return "Unicorn";
    }

    @Override
    public List<String> defaultLore() {
        return Arrays.asList("&7Cost:&2 %price% Emerald", "","&7Spawn a unicorn that will", "&7fly for 10 seconds", "", "%BuyStatus%");
    }

    @Override
    public int getPrice() {
        return 1;
    }

    @Override
    public Material getCurrency() {
        return Material.EMERALD;
    }

    @Override
    public boolean execute(Player player, Block block) {
        Horse horse = (Horse) player.getWorld().spawnEntity(player.getLocation(), EntityType.HORSE);
        FlyingHorse flyingHorse = new FlyingHorse(player, horse, 0.6, 0.2, 1.0);
        flyingHorse.runTaskTimer(API.getMain(), 0L, 1L);
        HCore.syncScheduler().after(10, TimeUnit.SECONDS).run(horse::remove);
        return true;
    }
}
