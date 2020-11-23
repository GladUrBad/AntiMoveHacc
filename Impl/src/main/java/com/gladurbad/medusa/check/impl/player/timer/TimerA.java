package com.gladurbad.medusa.check.impl.player.timer;

import com.gladurbad.medusa.check.Check;
import com.gladurbad.medusa.check.CheckInfo;
import com.gladurbad.medusa.data.PlayerData;
import com.gladurbad.medusa.packet.Packet;
import com.gladurbad.medusa.util.MathUtil;
import com.gladurbad.medusa.util.type.EvictingList;


@CheckInfo(name = "Timer (A)", description = "Checks for game speed which is too fast.")
public class TimerA extends Check {


    private final EvictingList<Long> samples = new EvictingList<>(50);
    private long lastFlyingTime;

    public TimerA(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if (packet.isFlying()) {
            final long now = now();
            final long delta = now - lastFlyingTime;

            if (delta > 1) {
                samples.add(delta);
            }

            if (samples.isFull()) {
                final double average = samples.stream().mapToDouble(value -> value).average().orElse(1);
                final double speed = 50 / average;

                if (speed >= 1.01) {
                    if (increaseBuffer() > 25) {
                        fail(String.format("speed=%.4f, delta=%o, buffer=%.2f", speed, delta, getBuffer()));
                    }
                } else {
                    decreaseBuffer();
                }
            }

            lastFlyingTime = now;
        } else if (packet.isTeleport()) {
            samples.add(110L);
        }
    }
}
