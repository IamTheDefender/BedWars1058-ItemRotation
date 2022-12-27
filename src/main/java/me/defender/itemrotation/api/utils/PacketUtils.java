package me.defender.itemrotation.api.utils;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public class PacketUtils {
    public static void destroyFireball(Fireball fireball, Player player) {
        // Get the ProtocolManager
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

        // Create a PacketContainer for the PacketPlayOutEntityDestroy packet
        PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.ENTITY_DESTROY);

        // Set the entity IDs to destroy
        packet.getIntegerArrays().write(0, new int[] {fireball.getEntityId()});

        // Send the packet to the player
        try {
            protocolManager.sendServerPacket(player, packet);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
