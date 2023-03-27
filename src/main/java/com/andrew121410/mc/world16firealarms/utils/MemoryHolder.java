package com.andrew121410.mc.world16firealarms.utils;

import com.andrew121410.mc.world16firealarms.sign.FireAlarmScreen;
import com.andrew121410.mc.world16firealarms.sign.ScreenFocus;
import com.andrew121410.mc.world16firealarms.simple.SimpleFireAlarm;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MemoryHolder {

    private final Map<String, SimpleFireAlarm> fireAlarmMap;
    private final Map<Location, FireAlarmScreen> fireAlarmScreenMap;
    private final Map<Location, String> chunkToFireAlarmName;

    // Clear when player quits.
    private final Map<UUID, ScreenFocus> screenFocusMap; //1

    public MemoryHolder() {
        this.fireAlarmMap = new HashMap<>();
        this.fireAlarmScreenMap = new HashMap<>();
        this.chunkToFireAlarmName = new HashMap<>();
        this.screenFocusMap = new HashMap<>();
    }

    public Map<String, SimpleFireAlarm> getFireAlarmMap() {
        return fireAlarmMap;
    }

    public Map<Location, FireAlarmScreen> getFireAlarmScreenMap() {
        return fireAlarmScreenMap;
    }

    public Map<Location, String> getChunkToFireAlarmName() {
        return chunkToFireAlarmName;
    }

    public Map<UUID, ScreenFocus> getScreenFocusMap() {
        return screenFocusMap;
    }
}
