package me.defender.itemrotation.listeners;

import com.andrei1058.bedwars.api.language.Language;
import com.hakan.core.HCore;
import com.hakan.core.ui.inventory.InventoryGui;
import me.defender.itemrotation.menu.ItemRotationMenu;
import me.defender.itemrotation.API;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class onShopOpen implements Listener {

    @EventHandler
    public void onPlayerShopOpen(InventoryOpenEvent event){
        Inventory inv = event.getInventory();
        String title = event.getView().getTitle();
        Player player = (Player) event.getPlayer();

        HCore.syncScheduler().after(1L).run(() -> {
           if(inv == null) return;
           if(!API.isShop(player, title)) return;

           String itemName = Language.getMsg(player, "item-rotation.item-name");
           List<String> itemLore = Language.getList(player, "item-rotation.item-lore");
           ItemStack item = HCore.itemBuilder(Material.TORCH).name(true, itemName).lores(true, itemLore).build();
           HCore.registerEvent(InventoryClickEvent.class).consume((e) -> {
              if(e.getCurrentItem().getItemMeta() != null && e.getCurrentItem().getItemMeta().getDisplayName() != null){
                  if(e.getCurrentItem().getItemMeta().getDisplayName().equals(itemName)){
                      InventoryGui menu = new ItemRotationMenu(player);
                      menu.open(player);
                  }
              }
           });
           inv.setItem(8, item);
        });
    }
}
