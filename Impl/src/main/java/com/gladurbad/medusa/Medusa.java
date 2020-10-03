package com.gladurbad.medusa;

import com.gladurbad.medusa.command.MedusaCommands;
import com.gladurbad.medusa.config.Config;
import com.gladurbad.medusa.manager.AlertManager;
import com.gladurbad.medusa.network.PacketProcessor;
import com.gladurbad.medusa.listener.RegistrationListener;
import com.gladurbad.medusa.manager.CheckManager;
import com.gladurbad.medusa.manager.PlayerDataManager;
import com.gladurbad.medusa.playerdata.PlayerData;
import com.gladurbad.medusa.util.ChatUtil;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.PacketEvent;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class Medusa extends JavaPlugin {

    @Getter
    private static Medusa instance;

    @Override
    public void onLoad() {
        PacketEvents.load();
    }

    @Override
    public void onEnable() {
        instance = this;

        // Get the Config (and save it if it doesn't exist) 
        this.saveDefaultConfig();
        Config.updateConfig();

        // Register Checks. 
        CheckManager.registerChecks();

        // Start Command Stuff. 
        MedusaCommands medusaCommands = new MedusaCommands(this);
        getCommand("medusa").setExecutor(medusaCommands);

        // Register listeners.
        PacketEvents.getSettings().setIdentifier("medusa_identifier");
        PacketEvents.getSettings().setUninjectAsync(true);
        PacketEvents.getSettings().setInjectAsync(true);
        PacketEvents.init(this);

        PacketEvents.getAPI().getEventManager().registerListener(new PacketProcessor());

        Bukkit.getServer().getPluginManager().registerEvents(new RegistrationListener(), this);

        // Register online players into the system.
        for (final Player player : Bukkit.getOnlinePlayers()) {
            UUID playerUUID = player.getUniqueId();
            if (!PlayerDataManager.getInstance().containsPlayer(playerUUID)) {
                final PlayerData playerData = new PlayerData(playerUUID, player);

                if (Config.TESTMODE) {
                    playerData.setAlerts(true);
                }

                PlayerDataManager.getInstance().getPlayerData().put(playerUUID, playerData);
            }
        }

        // Set the plugin to enabled and log to console. 
        Bukkit.getLogger().info("Medusa by GladUrBad has been enabled.");
        instance.setEnabled(true);

        // Setup the Alert Manager. 
        AlertManager.setup();
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("Disabling Medusa by GladUrBad");
        PacketEvents.stop();
        instance = null;
    }
}
