package me.defender.itemrotation.items;

import com.andrei1058.bedwars.api.language.Language;
import com.hakan.core.HCore;
import com.hakan.core.utils.ColorUtil;
import me.defender.itemrotation.api.RotationItem;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;

public class SuperCookieRotationItem extends RotationItem {
    @Override
    public ItemStack getItem() {
        return HCore.itemBuilder(Material.COOKIE).build();
    }

    @Override
    public String defaultName() {
        return "Super Cookie";
    }

    @Override
    public List<String> defaultLore() {
        return Arrays.asList("&7Cost:&2 %price% Emerald", "","&7Gain Speed III and Jump IV for", "&715 seconds!", "", "%BuyStatus%");
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
        // save if message doesn't exists
        Language.saveIfNotExists("item-rotation." + defaultName() + ".Already-Active", "&cYou already have Jump or Speed potion effect!");
        // Get messages
        String already_active_msg = Language.getMsg(player, "item-rotation." + defaultName() + ".Already-Active");
        // Avoid giving effect if already active
        if(player.hasPotionEffect(PotionEffectType.JUMP) || player.hasPotionEffect(PotionEffectType.SPEED)) {
            player.sendMessage(ColorUtil.colored(already_active_msg));
            return false;
        }
        // Give effects
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 300, 3));
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 300, 4));
        return true;
    }
}
