package me.defender.itemrotation.api.utils;

import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class FlyingHorse extends BukkitRunnable {

    private final Player player;
    private final Horse horse;
    private final double speed;
    private final double verticalSpeed;
    private final double horizontalSpeed;

    public FlyingHorse(Player player, Horse horse, double speed, double verticalSpeed, double horizontalSpeed) {
        this.player = player;
        this.horse = horse;
        this.speed = speed;
        this.verticalSpeed = verticalSpeed;
        this.horizontalSpeed = horizontalSpeed;
    }

    @Override
    public void run() {
        if (player.getVehicle() == null || !player.getVehicle().equals(horse)) {
            // Stop the horse if the player is not riding it
            horse.setVelocity(new Vector(0, 0, 0));
            cancel();
            horse.remove();
            return;
        }

        Vector direction = player.getLocation().getDirection();
        Vector velocity = direction.clone().multiply(horizontalSpeed);
        velocity.setY(verticalSpeed);

        horse.setVelocity(velocity);
        horse.setFallDistance(0);

        double pitch = player.getLocation().getPitch();
        if (pitch > 90) pitch = 90;
        else if (pitch < -90) pitch = -90;
        horse.getLocation().setPitch((float) -pitch);

        double yaw = player.getLocation().getYaw();
        horse.getLocation().setYaw((float) yaw);

        double speedModifier = Math.min(speed, velocity.length()) / velocity.length();
        horse.setJumpStrength((float) (0.4 * speedModifier));

        // Update horse position to make it look smooth
        horse.teleport(horse.getLocation().add(velocity.clone().multiply(1.0 / 20.0)));

        // Update rider position to keep them centered on the horse
        player.teleport(horse.getLocation().add(0, horse.getHeight() + 0.75, 0));
    }
}
