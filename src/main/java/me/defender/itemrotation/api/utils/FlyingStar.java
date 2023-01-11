package me.defender.itemrotation.api.utils;

import me.defender.itemrotation.api.utils.gsound.GSound;
import me.defender.itemrotation.items.SugarkenRotationItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

public class FlyingStar {
    // The speed at which the star will fly
    ConfigUtils config = new ConfigUtils();
    private final double speed = config.getDouble("Items." + new SugarkenRotationItem().defaultName() + ".star.speed");
    // The maximum distance that the star can travel
    private final int range = config.getInt("Items." + new SugarkenRotationItem().defaultName() + ".star.max-range");
    // The amount of damage that the star will deal when it collides with a living entity
    private final double damage = config.getDouble("Items." + new SugarkenRotationItem().defaultName() + ".star.damage");;
    // The ArmorStand entity representing the star
    private ArmorStand star;
    // The player who caused the star to be summoned
    private final Player player;
    // The direction in which the star is moving
    private final Vector direction;
    // The distance that the star has traveled so far
    private double distanceTravelled;
    // Is the arrow alive
    public boolean isAlive = true;

    public FlyingStar(Player player) {
        this.player = player;
        // Calculate the direction in which the star should fly based on the player's eye direction
        this.direction = player.getEyeLocation().getDirection().normalize();
        Location spawnLocation = player.getEyeLocation().add(this.direction.multiply(1.5));
        // Create a new ArmorStand entity to represent the star
        this.star = (ArmorStand) player.getWorld().spawnEntity(spawnLocation, EntityType.ARMOR_STAND);
        // Set the star's properties
        this.star.setVisible(false);
        this.star.setGravity(false);
        this.star.setBasePlate(false);
        this.star.setArms(true);
        try {
            this.star.setItemInHand(new ItemStack(Material.valueOf(config.getString("Items." + new SugarkenRotationItem().defaultName() + ".star.item").toUpperCase())));
        } catch (IllegalArgumentException e) {
            player.sendMessage(ChatColor.RED + "An error occurred while spawning star! contact an admin");
            e.printStackTrace();
            Bukkit.getLogger().warning("Looks like Material is Null! make the star item is set correct according to your version!");
        }
       this.star.setRightArmPose(new EulerAngle(Math.PI / 2, 0, 0));
    }

    public void move() {
        if(!isAlive) return;
        // Update the distance traveled by the star
        this.distanceTravelled += this.speed;
        // Get the current location of the star
        Location starLocation = this.star.getLocation();
        // Calculate the new location of the star based on its current location, direction, and speed
        Location newLocation = starLocation.add(this.direction.multiply(this.speed));
        // Teleport the star to the new location
        this.star.teleport(newLocation);
        // Check if the star has collided with any living entities
        for (Entity entity : this.star.getNearbyEntities(0.5, 0.5, 0.5)) {
            if (entity instanceof Player) {
                if(!isAlive) return;
                // Deal damage to the entity and remove the star
                ((Player) entity).damage(this.damage, this.player);
                this.star.remove();
                isAlive = false;
                player.playSound(player.getLocation(), GSound.ENTITY_ARROW_HIT_PLAYER.parseSound(), 1.0f, 1.0f);
                return;
            }
        }
        // Remove the star if it has traveled the maximum distance
        if (this.distanceTravelled > this.range) {
            isAlive = false;
            this.star.remove();
        }
    }
}