package com.andrew121410.mc.world16firealarms.listeners;

import com.andrew121410.mc.world16firealarms.World16FireAlarms;
import com.andrew121410.mc.world16firealarms.sign.FireAlarmScreen;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

public class OnBlockBreakEvent implements Listener {

    private final World16FireAlarms plugin;

    private final Map<Location, FireAlarmScreen> fireAlarmScreenMap;

    public OnBlockBreakEvent(World16FireAlarms plugin) {
        this.plugin = plugin;

        this.fireAlarmScreenMap = this.plugin.getSetListMap().getFireAlarmScreenMap();

        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {
        if (fireAlarmScreenMap.containsKey(event.getBlock().getLocation())) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    plugin.getFireAlarmManager().deleteFireAlarmSign(fireAlarmScreenMap.get(event.getBlock().getLocation()));
                }
            }.runTaskLater(plugin, 20L);
        }
    }
}