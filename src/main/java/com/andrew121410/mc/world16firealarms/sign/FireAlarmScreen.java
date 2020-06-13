package com.andrew121410.mc.world16firealarms.sign;

import com.andrew121410.mc.world16firealarms.Main;
import com.andrew121410.mc.world16utils.sign.screen.SignScreenManager;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.HashMap;
import java.util.Map;

@SerializableAs("FireAlarmScreen")
public class FireAlarmScreen implements ConfigurationSerializable {

    private String name;
    private String fireAlarmName;
    private Location location;

    private SignScreenManager signScreenManager;

    private Main plugin;

    public FireAlarmScreen(Main plugin, String name, String fireAlarmName, Location location) {
        this.plugin = plugin;

        this.name = name;
        this.fireAlarmName = fireAlarmName;
        this.location = location;

        this.signScreenManager = new SignScreenManager(this.plugin, location, name, 6L, new FireAlarmSignOS(this.plugin, name, fireAlarmName));
    }

    public String getName() {
        return name;
    }

    public String getFireAlarmName() {
        return fireAlarmName;
    }

    public Location getLocation() {
        return location;
    }

    public SignScreenManager getSignScreenManager() {
        return signScreenManager;
    }

    public Main getPlugin() {
        return plugin;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("Name", name);
        map.put("FireAlarmName", fireAlarmName);
        map.put("Location", location);
        return map;
    }

    public static FireAlarmScreen deserialize(Map<String, Object> map) {
        return new FireAlarmScreen(Main.getInstance(), (String) map.get("Name"), (String) map.get("FireAlarmName"), (Location) map.get("Location"));
    }
}
