package com.gladurbad.medusa.check.impl.movement.speed;

import com.gladurbad.medusa.check.Check;
import com.gladurbad.api.check.CheckInfo;
import com.gladurbad.medusa.network.Packet;
import com.gladurbad.medusa.playerdata.PlayerData;
import com.gladurbad.medusa.util.CollisionUtil;
import com.gladurbad.medusa.util.PlayerUtil;
import org.bukkit.entity.Player;

import static io.github.retrooper.packetevents.enums.ServerVersion.v_1_13;

@CheckInfo(name = "Speed", type = "A")
public class SpeedA extends Check {

    public SpeedA(PlayerData data) {
        super(data);
    }

    private boolean lastOnGround;

    @Override
    public void handle(Packet packet) {
        if (packet.isFlying()) {
            final Player player = data.getPlayer();

            final double deltaXZ = data.getDeltaXZ();
            final double lastDeltaXZ = data.getLastDeltaXZ();

            final double prediction = lastDeltaXZ * 0.91F + (data.isSprinting() ? 0.0263 : 0.02);
            final double difference = deltaXZ - prediction;

            if (difference > 1E-12 && !CollisionUtil.isOnGround(player) && !lastOnGround && !data.getPlayer().isFlying() && !data.isRiptiding() && !data.isGliding()) {
                if (increaseBuffer() > 1) {
                    fail();
                }
            } else {
                decreaseBuffer();
                setLastLegitLocation(player.getLocation());
            }
            lastOnGround = CollisionUtil.isOnGround(player);
        }
    }
}
