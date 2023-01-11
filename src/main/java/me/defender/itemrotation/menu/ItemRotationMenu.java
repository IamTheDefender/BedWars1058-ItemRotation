package me.defender.itemrotation.menu;

import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.shop.ShopCache;
import com.andrei1058.bedwars.shop.ShopManager;
import com.andrei1058.bedwars.shop.main.ShopCategory;
import com.andrei1058.bedwars.shop.quickbuy.PlayerQuickBuyCache;
import com.hakan.core.HCore;
import com.hakan.core.ui.inventory.InventoryGui;
import me.defender.itemrotation.api.RotationItem;
import me.defender.itemrotation.API;
import me.defender.itemrotation.api.utils.gsound.GSound;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemRotationMenu extends InventoryGui {
    public ItemRotationMenu(Player player) {
        super("ItemRotationMenu." + player.getDisplayName(), Language.getMsg(player, "item-rotation.menu-title"), 6, InventoryType.CHEST);
    }

    @Override
    public void onOpen(@Nonnull Player player) {
        int slotForItem = 31;
        // Other category's without quick buy category
        for (ShopCategory sc : ShopManager.getShop().getCategoryList())
            super.setItem(sc.getSlot(), sc.getItemStack(player), (e) -> {
                sc.open(player, ShopManager.getShop(), ShopCache.getShopCache(player.getUniqueId()));
            });

        // Separator item
        int[] ints = { 9, 10, 11, 12, 13, 14, 15, 16 };
        for (int slot : ints) {
            ItemStack itemStack = (ShopManager.getShop()).separatorStandard;
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Language.getMsg(player, "shop-items-messages.separator-item-name")));
            List<String> list = new ArrayList<>();
            for (String lores : Language.getPlayerLanguage(player).getList("shop-items-messages.separator-item-lore"))
                list.add(ChatColor.translateAlternateColorCodes('&', lores));
            itemMeta.setLore(list);
            itemStack.setItemMeta(itemMeta);
            super.setItem(slot, itemStack);
        }

        // Setting item for Rotating Items category
        String itemName = Language.getMsg(player, "item-rotation.item-name");
        List<String> itemLore = Language.getList(player, "item-rotation.item-lore");
        ItemStack item2 = HCore.itemBuilder(Material.TORCH).name(true, itemName).lores(true, itemLore).build();
        super.setItem(8, item2);

        // Glasses that separate category's from items to buy
        ItemStack item = (ShopManager.getShop()).separatorSelected;
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Language.getMsg(player, "shop-items-messages.separator-item-name")));
        List<String> lore = new ArrayList<>();
        for (String lores : Language.getPlayerLanguage(player).getList("shop-items-messages.separator-item-lore"))
            lore.add(ChatColor.translateAlternateColorCodes('&', lores));
        meta.setLore(lore);
        item.setItemMeta(meta);
        super.setItem(17, item);

        // Quick Buy Button
        super.setItem(ShopManager.getShop().getQuickBuyButton().getSlot(), ShopManager.getShop().getQuickBuyButton().getItemStack(player), (e) -> {
            ShopManager.getShop().open((Player) e.getWhoClicked(), PlayerQuickBuyCache.getQuickBuyCache(e.getWhoClicked().getUniqueId()), true);
        });

        // Set current rotation item
        // !                      !
        // !                      !
        // !                       !
        RotationItem item1 = API.items.get(API.getMain().getConfig().getInt("item-index"));
        String name = item1.defaultName();
        List<String> lores = item1.defaultLore();
        int price = item1.getPrice();
        Material currency = item1.getCurrency();

        Collections.replaceAll(lores, "%BuyStatus%", API.getStatus(player, price, currency));
        Collections.replaceAll(lores, "%price%", String.valueOf(price));
        List<String> itemLores = new ArrayList<>();
        for(String loress : lores){
            if(loress.contains("%price%")){
                itemLores.add(loress.replace("%price%", String.valueOf(price)));
            }else{
                itemLores.add(loress);
            }

        }

        // The current rotation item that is selected by admin or user.
        ItemStack itemStack = HCore.itemBuilder(item1.getItem()).name(true, ChatColor.RED + name).lores(true, itemLores).build();
        super.setItem(slotForItem, itemStack, (e) -> {
            if (API.getBedwarsAPI().getShopUtil().calculateMoney((Player)e.getWhoClicked(), currency) >= price) {
                API.getBedwarsAPI().getShopUtil().takeMoney( (Player) e.getWhoClicked(), currency, price);
                // add item to player's inventory ._.
                e.getWhoClicked().getInventory().addItem(HCore.itemBuilder(item1.getItem()).name(true, ChatColor.RED + name).build());
               // send message to the player .-.
                ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), GSound.ENTITY_VILLAGER_YES.parseSound(), 1.0f, 1.0f);
                e.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&',
                        Language.getMsg((Player)e.getWhoClicked(), "shop-new-purchase")
                                .replace("{prefix}", Language.getMsg((Player)e.getWhoClicked(), "prefix"))
                                .replace("{item}", "&a" + name)));
            }else{
                // send not enough resources message .-.
                ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), GSound.ENTITY_VILLAGER_NO.parseSound(), 1.0f, 1.0f);
                e.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&',
                        Language.getMsg((Player)e.getWhoClicked(), "shop-insuff-money")
                                .replace("{prefix}", Language.getMsg((Player)e.getWhoClicked(), "prefix"))
                                .replace("{amount}", String.valueOf(price - API.getBedwarsAPI().getShopUtil().calculateMoney((Player)e.getWhoClicked(), currency)))
                                .replace("{currency}", API.convertCurrencyToString(currency))));
            }
        });

        // Why rotation paper information
        // !                           !
        // !                           !
        // !                           !
        // !                           !
        String infoPaperName = Language.getMsg(player, "item-rotation.info-paper.name");
        List<String> infoPaperLore = Language.getList(player, "item-rotation.info-paper.lore");
        ItemStack itemStack2 = HCore.itemBuilder(Material.PAPER).name(true, infoPaperName).lores(infoPaperLore).build();
        super.setItem(49, itemStack2, (e) -> e.setCancelled(true));



    }
}
