package me.defender.itemrotation.listeners;

import com.hakan.core.HCore;
import me.defender.itemrotation.api.RotationItem;
import me.defender.itemrotation.API;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class onRightClick implements Listener {

    @EventHandler
    public void onRightClick(PlayerInteractEvent event){
        ItemStack item = event.getItem();
        RotationItem rotationItem = API.items.get(API.getMain().getConfig().getInt("item-index"));

        if(item == null || item.getType() == Material.AIR) return;
        if(item.getItemMeta() == null || item.getItemMeta().getDisplayName() == null) return;
        // if the items are same
        if(item.getType().equals(rotationItem.getItem().getType()) && item.getItemMeta().getDisplayName().contains(rotationItem.defaultName())){
            Player player = event.getPlayer();

            // If "execute" method returns true, it means that it executed without errors and should proceed
            if(API.items.get(API.getMain().getConfig().getInt("item-index")).execute(player, event.getClickedBlock())){
                event.setCancelled(true);
                if(player.getItemInHand().getAmount() == 1){
                    player.setItemInHand(HCore.itemBuilder(Material.AIR).build());
                    return;
                }
                player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
            }

        }
    }
}
