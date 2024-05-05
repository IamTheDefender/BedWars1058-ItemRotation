package me.defender.itemrotation.items;

import com.hakan.core.HCore;
import me.defender.itemrotation.API;
import me.defender.itemrotation.api.RotationItem;
import me.defender.itemrotation.api.utils.xseries.XMaterial;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FestivityMineRotationItem extends RotationItem {
    @Override
    public ItemStack getItem() {
        return XMaterial.GRAY_CARPET.parseItem();
    }

    @Override
    public String defaultName() {
        return "Festivity Mine";
    }

    @Override
    public List<String> defaultLore() {
        return Arrays.asList("&7Cost: &6%price% Gold", "", "&7Spawns a mine that will blast,", "&7if enemy player walks on it", "", "%BuyStatus%");
    }

    @Override
    public int getPrice() {
        return 10;
    }

    @Override
    public Material getCurrency() {
        return Material.GOLD_INGOT;
    }

    @Override
    public boolean execute(Player player, Block block) {
        spawnGrayCarpetAndBlowUpOnEnemy(player, block.getRelative(BlockFace.UP));
        return true;
    }

    public void spawnGrayCarpetAndBlowUpOnEnemy(Player player, Block block) {

        // Set the block to a gray carpet
        block.setType(XMaterial.GRAY_CARPET.parseMaterial());

        // Create a new task to check for players walking on the carpet
        HCore.syncScheduler().every(1).run((runnable) -> {
            // Get all players standing on the carpet
            List<Player> playersOnCarpet = block.getWorld().getPlayers().stream()
                    .filter(p -> p.getLocation().getBlock().equals(block))
                    .collect(Collectors.toList());

            // If there are any players on the carpet, blow them up
            if (!playersOnCarpet.isEmpty()) {
                boolean blast = false;
                for (Player p : playersOnCarpet) {
                    // Check if the player is on a different team & is not respawning & is not a spectator
                    if (!API.getBedwarsAPI().getArenaUtil().getArenaByPlayer(player).getTeam(player).getMembers().contains(p) &&
                    !API.getBedwarsAPI().getArenaUtil().getArenaByPlayer(player).isReSpawning(player) &&
                    !API.getBedwarsAPI().getArenaUtil().getArenaByPlayer(player).isSpectator(player)) {
                        blast = true;
                    }
                }
                if (blast) {
                    for (int i = 0; i < 10; i++) {
                        TNTPrimed tnt = (TNTPrimed) block.getWorld().spawnEntity(block.getLocation(), EntityType.PRIMED_TNT);
                        tnt.setFuseTicks(0); // Make the TNT explode immediately
                        block.setType(Material.AIR);
                    }
                    runnable.cancel();
                }
            }
        });
    }

    @Override
    public boolean isBlockRequired() {
        return true;
    }
}
