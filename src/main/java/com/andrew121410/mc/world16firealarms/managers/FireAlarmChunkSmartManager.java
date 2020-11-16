package com.andrew121410.mc.world16firealarms.managers;

import com.andrew121410.mc.world16firealarms.World16FireAlarms;
import com.andrew121410.mc.world16firealarms.simple.SimpleFireAlarm;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.Iterator;
import java.util.Map;

public class FireAlarmChunkSmartManager implements Runnable {

    private Map<Location, String> chunkToFireAlarmName;
    private Map<String, SimpleFireAlarm> iFireAlarmMap;

    private World16FireAlarms plugin;

    public FireAlarmChunkSmartManager(World16FireAlarms plugin) {
        this.plugin = plugin;
        this.chunkToFireAlarmName = this.plugin.getSetListMap().getChunkToFireAlarmName();
        this.iFireAlarmMap = this.plugin.getSetListMap().getFireAlarmMap();
    }

    @Override
    public void run() {
        Iterator<Map.Entry<Location, String>> iterator = this.chunkToFireAlarmName.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Location, String> entry = iterator.next();
            Location location = entry.getKey();
            String fireAlarmName = entry.getValue();
            boolean isChunkLoaded = location.getWorld().isChunkLoaded(location.getBlockX(), location.getBlockZ());
            if (isChunkLoaded && !this.iFireAlarmMap.containsKey(fireAlarmName)) {
                this.plugin.getFireAlarmManager().loadUpFireAlarm(fireAlarmName);
                Bukkit.getServer().broadcastMessage("Loaded: " + fireAlarmName);
            } else if (!isChunkLoaded && this.iFireAlarmMap.containsKey(fireAlarmName)) {
                SimpleFireAlarm simpleFireAlarm = this.iFireAlarmMap.get(fireAlarmName);
                this.plugin.getFireAlarmManager().saveAndUnloadFireAlarm(simpleFireAlarm);
                Bukkit.getServer().broadcastMessage("Unloaded: " + fireAlarmName);
            }
        }
    }
}
