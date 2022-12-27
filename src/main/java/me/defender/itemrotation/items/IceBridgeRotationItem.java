package me.defender.itemrotation.items;


import com.hakan.core.HCore;
import me.defender.itemrotation.api.RotationItem;
import me.defender.itemrotation.API;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class IceBridgeRotationItem extends RotationItem {

    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.ICE);
    }

    @Override
    public String defaultName() {
        return "Ice Bridge";
    }

    @Override
    public List<String> defaultLore() {
        return Arrays.asList("&7Cost: &6%price% Gold", "", "&7Spawns a bridge of ice blocks,", "&7in front of you", "", "%BuyStatus%");
    }

    @Override
    public int getPrice() {
        // Set the price of the ice bridge item
        return 5;
    }

    @Override
    public Material getCurrency() {
        // Set the currency used to purchase the ice bridge item
        return Material.GOLD_INGOT;
    }

    @Override
    public boolean execute(Player player, Block block2) {
        List<Block> blocks = new ArrayList<>();
        // Get the player's current position and direction
        Location pos = player.getLocation();
        Vector direction = pos.getDirection();

        // Calculate the position of the first block in the ice path
        // based on the player's position and direction
        double x = pos.getX() + direction.getX();
        double y = pos.getY() + direction.getY();
        double z = pos.getZ() + direction.getZ();
        World world = pos.getWorld();

        // Set the blocks in a straight line in front of the player
        for (int i = 0; i < 30; i++) {
            Block block = world.getBlockAt((int) x, (int) y, (int) z);
            if(!block.getType().isSolid()) {
                block.setType(Material.ICE);
                blocks.add(block);
                block = world.getBlockAt((int) x + 1, (int) y, (int) z);
                block.setType(Material.ICE);
                blocks.add(block);
                block = world.getBlockAt((int) x + 2, (int) y, (int) z);
                block.setType(Material.ICE);
                blocks.add(block);
                block = world.getBlockAt((int) x + 3, (int) y, (int) z);
                block.setType(Material.ICE);
                blocks.add(block);
            }
            x += direction.getX();
            y += direction.getY();
            z += direction.getZ();
        }

        HCore.syncScheduler().after(20, TimeUnit.SECONDS).run(() -> {
           for(Block block : blocks){
               block.setType(Material.AIR);
           }
        });

         HCore.syncScheduler().every(1, TimeUnit.SECONDS).run((runnable) -> {
            // Check if the player is standing on an ice block
            Block block = world.getBlockAt(player.getLocation().getBlockX(), player.getLocation().getBlockY() - 1, player.getLocation().getBlockZ());
            if (block.getType() == Material.ICE && blocks.contains(block)) {
                // Remove the ice block
                HCore.syncScheduler().after(1).run(() -> {
                    block.setType(Material.AIR);
                    blocks.remove(block);
                });
                for(Block block1 : API.getNearbyBlocks(block.getLocation(), 2)){
                    if(blocks.contains(block1)){
                        HCore.syncScheduler().after(1).run(() -> {
                            block.setType(Material.AIR);
                            blocks.remove(block);
                        });
                    }
                }
            }

            // If there are no more ice blocks, stop the task
            if (blocks.isEmpty()) {
               runnable.cancel();
            }
        });


        return true;
    }


}

