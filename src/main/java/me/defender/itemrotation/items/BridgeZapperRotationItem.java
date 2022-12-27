package me.defender.itemrotation.items;

import com.andrei1058.bedwars.api.language.Language;
import me.defender.itemrotation.api.RotationItem;
import me.defender.itemrotation.API;
import me.defender.itemrotation.api.utils.xseries.XMaterial;
import me.defender.itemrotation.api.utils.xseries.XTag;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class BridgeZapperRotationItem extends RotationItem {
    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.SHEARS);
    }

    @Override
    public String defaultName() {
        return "Bridge Zapper";
    }

    @Override
    public List<String> defaultLore() {
        return Arrays.asList("&7Cost: &2%price% Emerald", "", "&7Clicking on wool will instantly zap up", "&7to 8 adjacent wool blocks!", "", "%BuyStatus%");
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
        if(XTag.WOOL.isTagged(XMaterial.matchXMaterial(block.getType()))){
            player.sendMessage(Language.getMsg(player, "item-rotation.Bridge-Zapper.deny"));
            return false;
        }
        for(Block block1 : API.getNearbyBlocks(block.getLocation(), 5)){
            if(XTag.WOOL.isTagged(XMaterial.matchXMaterial(block.getType()))){
                block1.setType(Material.AIR);
            }
        }

        return true;
    }
}
