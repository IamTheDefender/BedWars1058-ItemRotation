package me.defender.itemrotation.items;

import com.andrei1058.bedwars.api.language.Language;
import me.defender.itemrotation.api.RotationItem;
import me.defender.itemrotation.API;
import me.defender.itemrotation.api.utils.ConfigUtils;
import me.defender.itemrotation.api.utils.xseries.XMaterial;
import me.defender.itemrotation.api.utils.xseries.XTag;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collection;
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
        return Arrays.asList("&7Cost: &f%price% Iron", "", "&7Clicking on wool will instantly zap up", "&7to 8 adjacent wool blocks!", "", "%BuyStatus%");
    }

    @Override
    public int getPrice() {
        return 32;
    }

    @Override
    public Material getCurrency() {
        return Material.IRON_INGOT;
    }

    @Override
    public boolean execute(Player player, Block block) {
        if (player == null || block == null) {
            // Player or block is null, return false
            return false;
        }
       if(!API.getBedwarsAPI().getArenaUtil().getArenaByPlayer(player).isBlockPlaced(block)){
           player.sendMessage(Language.getMsg(player, "item-rotation.Bridge-Zapper.deny"));
           return false;
       }
        if (!isWool(block)) {
            player.sendMessage(Language.getMsg(player, "item-rotation.Bridge-Zapper.deny"));
            return false;
        }

        int radius = new ConfigUtils().getInt("Items." + defaultName() + ".radius");
        if (radius <= 0) {
            // Radius is not a positive integer, return false
            return false;
        }

        Location blockLocation = block.getLocation();
        if (blockLocation == null) {
            // Block location is null, return false
            return false;
        }

        Collection<Block> nearbyBlocks = API.getNearbyBlocks(blockLocation, radius);

        for (Block nearbyBlock : nearbyBlocks) {
            if (isWool(nearbyBlock)) {
                nearbyBlock.setType(Material.AIR);
            }
        }

        return true;
    }

    /**
     * Returns true if the given block is wool, false otherwise.
     *
     * @param block the block to check
     * @return true if the block is wool, false otherwise
     */
    public boolean isWool(Block block) {
        return XTag.WOOL.isTagged(XMaterial.matchXMaterial(block.getType()));
        }
    }

