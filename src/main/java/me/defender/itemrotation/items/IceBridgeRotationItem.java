package me.defender.itemrotation.items;

import me.defender.itemrotation.ItemRotation;
import me.defender.itemrotation.api.RotationItem;
import me.defender.itemrotation.api.utils.xseries.XSound;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        double y = pos.getY();
        double z = pos.getZ() + direction.getZ();
        World world = pos.getWorld();

        // Set the blocks in a straight line in front of the player
        for (int i = 0; i < 30; i++) {
            double finalX = x;
            double finalZ = z;
            Bukkit.getScheduler().runTaskLater(ItemRotation.getInstance(), () -> {
                for (int j = -1; j <= 1; j++) {
                    Vector perp = new Vector(-direction.getZ(), 0, direction.getX()).normalize();
                    double offsetX = j * perp.getX();
                    double offsetZ = j * perp.getZ();
                    Location location = new Location(world, Math.round(finalX + offsetX), y-1, Math.round(finalZ + offsetZ));
                    Block block = world.getBlockAt(location);
                    if (!block.getType().isSolid()) {
                        XSound.BLOCK_GLASS_BREAK.play(location, 1.0f, 0.5f);
                        world.playEffect(location.clone().add(0, 0.5, 0), Effect.HAPPY_VILLAGER, 0, 1);
                        block.setType(Material.ICE);
                        blocks.add(block);
                    }
                    Bukkit.getScheduler().runTaskLater(ItemRotation.getInstance(), () -> {
                        if (blocks.contains(block)) {
                            World w = block.getWorld();
                            XSound.ENTITY_CHICKEN_EGG.play(block.getLocation(), 1.0f, 0.5f);
                            w.playEffect(block.getLocation().clone().add(0, 0.5, 0), Effect.CLOUD, 0, 1);
                            block.setType(Material.AIR);
                            blocks.remove(block);
                        }
                    }, 6 * 15L);
                }
            }, i * 2L);
            x += direction.getX();
            z += direction.getZ();
        }

        /**
         HCore.syncScheduler().every(1L).run((runnable) -> {
            // Check if the player is standing on an ice block
            Block block = world.getBlockAt(player.getLocation().getBlockX(), player.getLocation().getBlockY() - 1, player.getLocation().getBlockZ());
            if (block.getType() == Material.ICE && blocks.contains(block)) {
                // Remove the ice block
                    block.setType(Material.AIR);
                    blocks.remove(block);
                for(Block block1 : API.getNearbyBlocks(block.getLocation(), new ConfigUtils().getInt("Items." + defaultName() + ".block-break.radius"))){
                    if(blocks.contains(block1)){
                            block.setType(Material.AIR);
                            blocks.remove(block);
                    }
                }
            }

            // If there are no more ice blocks, stop the task
            if (blocks.isEmpty()) {
               runnable.cancel();
            }
        });
         **/


        return true;
    }

    @Override
    public boolean isBlockRequired() {
        return false;
    }


}

