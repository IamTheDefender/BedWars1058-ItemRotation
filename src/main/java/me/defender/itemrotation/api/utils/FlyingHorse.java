package me.defender.itemrotation.api.utils;

import com.andrei1058.bedwars.api.language.Language;
import com.hakan.core.HCore;
import com.hakan.core.utils.ColorUtil;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class FlyingHorse extends BukkitRunnable {

    private final Player player;
    private final Horse horse;
    private final double speed;
    private final double verticalSpeed;
    private final double horizontalSpeed;
    int time;
    AtomicInteger sideTime;

    public FlyingHorse(Player player, Horse horse, double speed, double verticalSpeed, double horizontalSpeed) {
        this.player = player;
        this.horse = horse;
        this.speed = speed;
        this.verticalSpeed = verticalSpeed;
        this.horizontalSpeed = horizontalSpeed;
        time = 0;

        sideTime = new AtomicInteger();
        HCore.syncScheduler().every(1, TimeUnit.SECONDS).freezeIf(r -> sideTime.get() > 10).run(sideTime::getAndIncrement);
    }

    @Override
    public void run() {
        if (player.getVehicle() == null || !player.getVehicle().equals(horse)) {
            if(horse.isDead()){
                horse.setVelocity(new Vector(0, 0, 0));
                cancel();
                horse.remove();
                player.setFlying(false);
            }else{
                horse.setPassenger(player);
            }

            return;
        }
        time++;
        Vector direction = player.getLocation().getDirection();
        Vector velocity = direction.clone().multiply(horizontalSpeed);
        double pitch = player.getLocation().getPitch();
        if (pitch < -45) {
            // If player is looking down, decrease vertical speed
            velocity.setY(velocity.getY() - 1);
        }
        HCore.sendActionBar(player, Language.getMsg(player, ColorUtil.colored("actionbar-itemrotation")).replace("%s%", 10 - sideTime.get() + ""));
        horse.setVelocity(velocity);
        horse.setFallDistance(0);

        pitch = Math.min(90, Math.max(-90, -pitch));
        horse.getLocation().setPitch((float) pitch);

        double yaw = player.getLocation().getYaw();
        horse.getLocation().setYaw((float) yaw);

        double speedModifier = Math.min(speed, velocity.length()) / velocity.length();
        horse.setJumpStrength((float) (0.4 * speedModifier));
        // Update horse position to make it look smoother
        horse.teleport(horse.getLocation().add(velocity.clone().multiply(1.0 / 20.0)).setDirection(horse.getLocation().clone().toVector().subtract(player.getLocation().toVector())));
    }
}
