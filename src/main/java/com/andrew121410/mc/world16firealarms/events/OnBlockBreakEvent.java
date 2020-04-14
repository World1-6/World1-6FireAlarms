package com.andrew121410.mc.world16firealarms.events;

import com.andrew121410.mc.world16firealarms.Main;
import com.andrew121410.mc.world16firealarms.sign.FireAlarmScreen;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

public class OnBlockBreakEvent implements Listener {

    private Main plugin;

    private Map<Location, FireAlarmScreen> fireAlarmScreenMap;

    public OnBlockBreakEvent(Main plugin) {
        this.plugin = plugin;

        this.fireAlarmScreenMap = this.plugin.getSetListMap().getFireAlarmScreenMap();

        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {

        if (fireAlarmScreenMap.get(event.getBlock().getLocation()) != null) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    plugin.getFireAlarmManager().deleteFireAlarmSignScreen(event.getBlock().getLocation());
                }
            }.runTaskLater(plugin, 20L);
        }
    }
}