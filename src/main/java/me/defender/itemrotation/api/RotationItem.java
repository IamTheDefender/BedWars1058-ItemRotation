package me.defender.itemrotation.api;

import com.andrei1058.bedwars.api.language.Language;
import me.defender.itemrotation.API;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Objects;

/**
 * Abstract base class for representing a rotation item.
 *
 * This class provides basic functionality for storing and manipulating the
 * properties of a rotation item, such as its name, lore, price, and currency.
 * Subclasses should override the {@link #execute(Player, Block)} method to implement
 * specific behavior for when the item is used.
 */
public abstract class RotationItem {
    /**
     * Returns the item stack for the rotation item.
     *
     * @return the item stack for the rotation item
     */
    public abstract ItemStack getItem();

    /**
     * Returns the default name for the rotation item.
     *
     * @return the default name for the rotation item
     */
    public abstract String defaultName();

    /**
     * Returns the default lore for the rotation item.
     *
     * @return the default lore for the rotation item
     */
    public abstract List<String> defaultLore();

    /**
     * Returns the price of the rotation item.
     *
     * @return the price of the rotation item
     */
    public abstract int getPrice();

    /**
     * Returns the currency used to purchase the rotation item.
     *
     * @return the currency used to purchase the rotation item
     */
    public abstract Material getCurrency();

    /**
     * Executes the action for the rotation item.
     *
     * This method should be implemented by subclasses to define the specific
     * behavior for when the item is used.
     *
     * @param player the player using the item
     * @param block the clicked block
     * @return true if the action was successful, false otherwise
     */
    public abstract boolean execute(Player player, Block block);

    /**
     * Registers the rotation item with the item rotation API.
     */
    public void register(){
        API.items.add(this);
        Language.saveIfNotExists("item-rotation.items." + defaultName() + ".price", getPrice());
        Language.saveIfNotExists("item-rotation.items." + defaultName() + ".name", "&a" + Objects.requireNonNull(defaultName()));
        Language.saveIfNotExists("item-rotation.items." + defaultName() + ".lore", Objects.requireNonNull(defaultLore()));
        Language.saveIfNotExists("item-rotation.items." + defaultName() + ".currency", Objects.requireNonNull(getCurrency().toString()));
    }

    public int getPriceConfig(Player player){
        return Language.getPlayerLanguage(player).getInt("item-rotation.items." + defaultName() + ".price");
    }

    public Material getCurrencyConfig(Player player){
        return Material.valueOf(Language.getMsg(player, "item-rotation.items." + defaultName() + ".currency"));
    }

    public List<String> getLore(Player player){
        return Language.getList(player, "item-rotation.items." + defaultName() + ".lore");
    }

    public String getDisplayName(Player player){
        return Language.getMsg(player, "item-rotation.items." + defaultName() + ".name");
    }
}
