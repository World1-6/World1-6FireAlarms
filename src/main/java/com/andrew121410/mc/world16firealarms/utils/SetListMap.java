package com.andrew121410.mc.world16firealarms.utils;

import com.andrew121410.mc.world16firealarms.interfaces.IFireAlarm;
import com.andrew121410.mc.world16firealarms.sign.FireAlarmScreen;
import com.andrew121410.mc.world16firealarms.sign.ScreenFocus;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SetListMap {

    private Map<String, IFireAlarm> fireAlarmMap;
    private Map<Location, FireAlarmScreen> fireAlarmScreenMap;
    private Map<UUID, ScreenFocus> screenFocusMap;

    public SetListMap() {
        this.fireAlarmMap = new HashMap<>();
        this.fireAlarmScreenMap = new HashMap<>();
        this.screenFocusMap = new HashMap<>();
    }

    public Map<String, IFireAlarm> getFireAlarmMap() {
        return fireAlarmMap;
    }

    public Map<Location, FireAlarmScreen> getFireAlarmScreenMap() {
        return fireAlarmScreenMap;
    }

    public Map<UUID, ScreenFocus> getScreenFocusMap() {
        return screenFocusMap;
    }
}
