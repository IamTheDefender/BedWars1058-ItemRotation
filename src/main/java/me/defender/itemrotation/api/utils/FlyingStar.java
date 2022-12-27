package me.defender.itemrotation.api.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

public class FlyingStar {
    // The speed at which the sword will fly
    private final double speed = 1.0;
    // The maximum distance that the sword can travel
    private final int range = 100;
    // The amount of damage that the sword will deal when it collides with a living entity
    private final double damage = 5.0;
    // The ArmorStand entity representing the sword
    private ArmorStand sword;
    // The player who caused the sword to be summoned
    private final Player player;
    // The direction in which the sword is moving
    private final Vector direction;
    // The distance that the sword has traveled so far
    private double distanceTravelled;

    public FlyingStar(Player player) {
        this.player = player;
        // Calculate the direction in which the sword should fly based on the player's eye direction
        this.direction = player.getEyeLocation().getDirection().normalize();
        Location spawnLocation = player.getEyeLocation().add(this.direction.multiply(1.5));
        // Create a new ArmorStand entity to represent the sword
        this.sword = (ArmorStand) player.getWorld().spawnEntity(spawnLocation, EntityType.ARMOR_STAND);
        // Set the sword's properties
        this.sword.setVisible(false);
        this.sword.setGravity(false);
        this.sword.setBasePlate(false);
        this.sword.setArms(true);
        this.sword.setItemInHand(new ItemStack(Material.NETHER_STAR));
        this.sword.setRightArmPose(new EulerAngle(Math.PI / 2, 0, 0));
    }

    public void move() {
        // Update the distance traveled by the sword
        this.distanceTravelled += this.speed;
        // Get the current location of the sword
        Location swordLocation = this.sword.getLocation();
        // Calculate the new location of the sword based on its current location, direction, and speed
        Location newLocation = swordLocation.add(this.direction.multiply(this.speed));
        // Teleport the sword to the new location
        this.sword.teleport(newLocation);
        // Check if the sword has collided with any living entities
        for (Entity entity : this.sword.getNearbyEntities(0.5, 0.5, 0.5)) {
            if (entity instanceof LivingEntity) {
                // Deal damage to the entity and remove the sword
                ((LivingEntity) entity).damage(this.damage, this.player);
                this.sword.remove();
                return;
            }
        }
        // Remove the sword if it has traveled the maximum distance
        if (this.distanceTravelled > this.range) {
            this.sword.remove();
        }
    }
}