package com.gladurbad.medusa;

import me.godead.lilliputian.Dependency;
import me.godead.lilliputian.Lilliputian;
import me.godead.lilliputian.Repository;

import com.gladurbad.medusa.command.MedusaCommands;
import com.gladurbad.medusa.config.Config;
import com.gladurbad.medusa.manager.AlertManager;
import com.gladurbad.medusa.listener.RegistrationListener;
import com.gladurbad.medusa.manager.CheckManager;
import com.gladurbad.medusa.manager.PlayerDataManager;
import com.gladurbad.medusa.playerdata.PlayerData;
import io.github.retrooper.packetevents.PacketEvents;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;



public class Medusa extends JavaPlugin {

    @Getter
    private static Medusa instance;

    @Override
    public void onEnable() {
        instance = this;

        this.saveDefaultConfig();
        Config.updateConfig();

        CheckManager.registerChecks();


        MedusaCommands medusaCommands = new MedusaCommands(this);
        getCommand("medusa").setExecutor(medusaCommands);

        //Register listeners.
        PacketEvents.getSettings().setIdentifier("medusa_identifier");
        PacketEvents.getSettings().setUninjectAsync(true);
        PacketEvents.getSettings().setInjectAsync(true);
        PacketEvents.init(this);

        Bukkit.getServer().getPluginManager().registerEvents(new RegistrationListener(), this);

        //Register online players into the system.
        Bukkit.getOnlinePlayers()
                .stream()
                .filter(player -> !PlayerDataManager.getInstance().containsPlayer(player))
                .forEach(player -> {
                    final PlayerData playerData = new PlayerData(player.getUniqueId(), player);
                    if (Config.TESTMODE) playerData.setAlerts(true);

                    PlayerDataManager.getInstance().getPlayerData().put(player.getUniqueId(), playerData);
                });

        Bukkit.getLogger().info("Medusa by GladUrBad has been enabled.");
        instance.setEnabled(true);

        AlertManager.setup();
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("Disabling Medusa by GladUrBad");
        PacketEvents.stop();
        instance = null;
    }


}
