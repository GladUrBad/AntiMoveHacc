package com.gladurbad.medusa.manager;

import com.gladurbad.medusa.check.Check;
import com.gladurbad.medusa.check.impl.combat.aim.*;
import com.gladurbad.medusa.check.impl.combat.autoclicker.AutoClickerB;
import com.gladurbad.medusa.check.impl.combat.autoclicker.AutoClickerA;
import com.gladurbad.medusa.check.impl.combat.killaura.*;
import com.gladurbad.medusa.check.impl.combat.reach.ReachB;
import com.gladurbad.medusa.check.impl.movement.motion.*;
import com.gladurbad.medusa.check.impl.combat.reach.ReachA;
import com.gladurbad.medusa.check.impl.combat.velocity.VelocityA;
import com.gladurbad.medusa.check.impl.movement.jesus.JesusC;
import com.gladurbad.medusa.check.impl.movement.speed.SpeedC;
import com.gladurbad.medusa.check.impl.player.badpackets.*;
import com.gladurbad.medusa.check.impl.movement.fastclimb.FastClimbA;
import com.gladurbad.medusa.check.impl.movement.flight.FlightA;
import com.gladurbad.medusa.check.impl.movement.flight.FlightB;
import com.gladurbad.medusa.check.impl.movement.jesus.JesusA;
import com.gladurbad.medusa.check.impl.movement.jesus.JesusB;
import com.gladurbad.medusa.check.impl.movement.nofall.NofallA;
import com.gladurbad.medusa.check.impl.movement.speed.SpeedA;
import com.gladurbad.medusa.check.impl.movement.speed.SpeedB;
import com.gladurbad.medusa.check.impl.player.inventory.InventoryA;
import com.gladurbad.medusa.check.impl.player.inventory.InventoryB;
import com.gladurbad.medusa.check.impl.player.pingspoof.PingSpoofA;
import com.gladurbad.medusa.check.impl.player.scaffold.ScaffoldA;
import com.gladurbad.medusa.check.impl.player.scaffold.ScaffoldB;
import com.gladurbad.medusa.check.impl.player.scaffold.ScaffoldC;
import com.gladurbad.medusa.check.impl.player.timer.TimerA;
import com.gladurbad.medusa.check.impl.player.timer.TimerB;
import com.gladurbad.medusa.config.Config;
import com.gladurbad.medusa.playerdata.PlayerData;
import org.bukkit.Bukkit;

import com.gladurbad.medusa.network.PacketProcessor;
import io.github.retrooper.packetevents.PacketEvents;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;


public class CheckManager {

    public static final Class[] CHECKS = new Class[] {
            AimA.class,
            AimB.class,
            AimC.class,
            AimD.class,
            AimE.class,
            AutoClickerA.class,
            AutoClickerB.class,
            KillauraA.class,
            KillauraB.class,
            KillauraC.class,
            KillauraD.class,
            KillauraE.class,
            KillauraF.class,
            KillauraG.class,
            KillauraH.class,
            ReachA.class,
            ReachB.class,
            VelocityA.class,
            FastClimbA.class,
            FlightA.class,
            FlightB.class,
            JesusA.class,
            JesusB.class,
            JesusC.class,
            MotionA.class,
            MotionB.class,
            MotionC.class,
            MotionD.class,
            MotionE.class,
            MotionF.class,
            NofallA.class,
            SpeedA.class,
            SpeedB.class,
            SpeedC.class,
            BadPacketsA.class,
            BadPacketsB.class,
            BadPacketsC.class,
            BadPacketsD.class,
            BadPacketsE.class,
            InventoryA.class,
            InventoryB.class,
            PingSpoofA.class,
            ScaffoldA.class,
            ScaffoldB.class,
            ScaffoldC.class,
            TimerA.class,
            TimerB.class
    };

    private static final List<Constructor<?>> CONSTRUCTORS = new ArrayList<>();

    public static void registerChecks() {
        PacketEvents.getAPI().getEventManager().registerListener(new PacketProcessor());
        for (Class clazz : CHECKS) {
            if (Config.ENABLED_CHECKS.contains(clazz.getSimpleName())) {
                try {
                    CONSTRUCTORS.add(clazz.getConstructor(PlayerData.class));
                    Bukkit.getLogger().info(clazz.getSimpleName() + " is enabled!");
                } catch (NoSuchMethodException exception) {
                    exception.printStackTrace();
                }
            } else {
                Bukkit.getLogger().info(clazz.getSimpleName() + " is disabled!");
            }
        }
    }

    public static List<Check> loadChecks(PlayerData data) {
        final List<Check> checkList = new ArrayList<>();
        for (Constructor<?> constructor : CONSTRUCTORS) {
            try {
                checkList.add((Check) constructor.newInstance(data));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return checkList;
    }
}