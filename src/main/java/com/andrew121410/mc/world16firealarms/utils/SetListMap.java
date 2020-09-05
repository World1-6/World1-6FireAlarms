package com.andrew121410.mc.world16firealarms.utils;

import com.andrew121410.mc.world16firealarms.interfaces.IFireAlarm;
import com.andrew121410.mc.world16firealarms.sign.FireAlarmScreen;
import com.andrew121410.mc.world16firealarms.sign.ScreenFocus;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SetListMap {

    //0 is when the server shuts down.
    //1 is when the player quits.

    private Map<String, IFireAlarm> fireAlarmMap; //0
    private Map<Location, FireAlarmScreen> fireAlarmScreenMap; //0
    private Map<Location, String> chunkToFireAlarmName; //0
    private Map<UUID, ScreenFocus> screenFocusMap; //1

    public SetListMap() {
        this.fireAlarmMap = new HashMap<>();
        this.fireAlarmScreenMap = new HashMap<>();
        this.chunkToFireAlarmName = new HashMap<>();
        this.screenFocusMap = new HashMap<>();
    }

    public Map<String, IFireAlarm> getFireAlarmMap() {
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
