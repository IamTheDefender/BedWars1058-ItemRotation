package me.defender.itemrotation.listeners;

import com.andrei1058.bedwars.api.language.Language;
import com.hakan.core.HCore;
import com.hakan.core.ui.inventory.InventoryGui;
import me.defender.itemrotation.api.utils.ConfigUtils;
import me.defender.itemrotation.menu.ItemRotationMenu;
import me.defender.itemrotation.API;
import org.bukkit.Bukkit;
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
    public void onPlayerShopOpen(InventoryOpenEvent event) {
        Inventory inv = event.getInventory();
        String title = event.getView().getTitle();
        Player player = (Player) event.getPlayer();
        // Check if the inventory is a shop inventory
        if (inv == null || !API.isShop(player, title)) {
            return;
        }
        API.debug("Player " + player.getName() + " opened the shop");


        // Check if the plugin should be enabled for this game
        ConfigUtils config = new ConfigUtils();
        boolean enableInSpecificGroups = config.getBoolean("Settings.EnableInSpecificGroups.enabled");
        if (enableInSpecificGroups) {
            API.debug("Specific Groups is enabled");
            List<String> specificGroups = config.getStringList("Settings.EnableInSpecificGroups.groups");
            if (!specificGroups.contains(API.getBedwarsAPI().getArenaUtil().getArenaByPlayer(player).getGroup())) {
                API.debug("The arena is not in the group list!");
                return;
            }
        }
        API.debug("Adding the item.");

        String itemName = Language.getMsg(player, "item-rotation.item-name");
        List<String> itemLore = Language.getList(player, "item-rotation.item-lore");
        ItemStack item = HCore.itemBuilder(Material.TORCH)
                .name(true, itemName)
                .lores(true, itemLore)
                .build();
        inv.setItem(8, item);

        // Register the InventoryClickEvent listener
        HCore.registerEvent(InventoryClickEvent.class).consume((e) -> {
            try {
                if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR && e.getCurrentItem().getItemMeta() != null && e.getCurrentItem().getItemMeta().getDisplayName() != null) {
                    if (e.getCurrentItem().getItemMeta().getDisplayName().equals(itemName)) {
                        InventoryGui menu = new ItemRotationMenu(player);
                        menu.open((Player) e.getWhoClicked());
                    }
                }
            }catch (Exception exception){
                exception.printStackTrace();
                Bukkit.getLogger().severe("Error Message: " + exception.getMessage());
            }
        });
    }
}
