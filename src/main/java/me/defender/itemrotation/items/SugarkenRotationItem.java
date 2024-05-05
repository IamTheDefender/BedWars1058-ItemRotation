package me.defender.itemrotation.items;

import com.hakan.core.HCore;
import me.defender.itemrotation.api.RotationItem;
import me.defender.itemrotation.api.utils.FlyingStar;
import me.defender.itemrotation.API;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class SugarkenRotationItem extends RotationItem {
    @Override
    public ItemStack getItem() {
        return HCore.itemBuilder(Material.NETHER_STAR).build();
    }

    @Override
    public String defaultName() {
        return "Sugarken";
    }

    @Override
    public List<String> defaultLore() {
        return Arrays.asList("&7Cost:&f %price% Iron ", "", "&7A ninja's best friend! Right", "&7Click to throw.", "", "%BuyStatus%");
    }

    @Override
    public int getPrice() {
        return 20;
    }

    @Override
    public Material getCurrency() {
        return Material.IRON_INGOT;
    }

    @Override
    public boolean execute(Player player, Block block) {
        FlyingStar sword = new FlyingStar(player);
        // Schedule a task to move the sword every tick
        HCore.syncScheduler().every(1).run((runnable -> {
            if(sword.isAlive){
                sword.move();
            }else{
                runnable.cancel();
            }
        }));
        return true;
    }

    @Override
    public boolean isBlockRequired() {
        return false;
    }


}
