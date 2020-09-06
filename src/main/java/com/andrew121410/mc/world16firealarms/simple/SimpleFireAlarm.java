package com.andrew121410.mc.world16firealarms.simple;

import com.andrew121410.mc.world16firealarms.*;
import com.andrew121410.mc.world16firealarms.sign.FireAlarmScreen;
import com.andrew121410.mc.world16firealarms.sign.FireAlarmSignOS;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@SerializableAs("SimpleFireAlarm")
public class SimpleFireAlarm implements ConfigurationSerializable {

    private World16FireAlarms plugin;

    private String name;
    private Location mainChunk;

    private Map<String, SimpleStrobe> strobesMap;
    private Map<String, FireAlarmScreen> signsMap;

    private FireAlarmStatus fireAlarmStatus;
    private boolean isAlarmCurrently = false;

    private FireAlarmSettings fireAlarmSettings;

    public SimpleFireAlarm(World16FireAlarms plugin, String name, Location mainChunk) {
        this(plugin, name, mainChunk, new FireAlarmSettings(), new HashMap<>(), new HashMap<>());
    }

    public SimpleFireAlarm(World16FireAlarms plugin, String name, Location mainChunk, FireAlarmSettings fireAlarmSettings, Map<String, SimpleStrobe> strobesMap, Map<String, FireAlarmScreen> signsMap) {
        this.plugin = plugin;
        this.name = name;
        this.mainChunk = mainChunk;
        this.fireAlarmSettings = fireAlarmSettings;
        this.strobesMap = strobesMap;
        this.signsMap = signsMap;
        this.fireAlarmStatus = FireAlarmStatus.READY;
    }

    public void registerStrobe(SimpleStrobe iStrobe) {
        this.strobesMap.putIfAbsent(iStrobe.getName(), iStrobe);
        this.strobesMap.get(iStrobe.getName()).setFireAlarmSound(fireAlarmSettings.getFireAlarmSound());
    }

    public void registerNac() {
    }

    public void registerSign(String string, FireAlarmScreen fireAlarmScreen) {
        this.signsMap.put(string.toLowerCase(), fireAlarmScreen);
    }

    public void reset() {
        this.fireAlarmStatus = FireAlarmStatus.READY;

        //Signs
        Iterator<Map.Entry<String, FireAlarmScreen>> iterator = this.signsMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, FireAlarmScreen> entry = iterator.next();
            FireAlarmScreen fireAlarmScreen = this.signsMap.get(entry.getKey());
            FireAlarmSignOS fireAlarmSignOS = null;
            if (fireAlarmScreen != null)
                fireAlarmSignOS = (FireAlarmSignOS) fireAlarmScreen.getSignScreenManager().getSignScreen();

            if (fireAlarmSignOS != null) {
                fireAlarmSignOS.mainPage(fireAlarmScreen.getSignScreenManager());
            } else {
                iterator.remove();
                this.plugin.getFireAlarmManager().deleteFireAlarmSign(this.name, entry.getKey());
            }
        }
        //SIGNS DONE...
    }

    public void trouble() {
        //TODO add Trouble
    }

    public void alarm(FireAlarmReason fireAlarmReason) {
        this.fireAlarmStatus = FireAlarmStatus.ALARM;

        if (fireAlarmSettings.getCommandTrigger() != null)
            this.plugin.getServer().dispatchCommand(this.plugin.getServer().getConsoleSender(), this.fireAlarmSettings.getCommandTrigger());

        if (fireAlarmSettings.getFireAlarmTempo() == FireAlarmTempo.CODE_3) setupCode3();
        else if (fireAlarmSettings.getFireAlarmTempo() == FireAlarmTempo.MARCH_TIME) setupMarchTime();

        //Signs
        Iterator<Map.Entry<String, FireAlarmScreen>> iterator = this.signsMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, FireAlarmScreen> entry = iterator.next();
            FireAlarmScreen fireAlarmScreen = this.signsMap.get(entry.getKey());
            FireAlarmSignOS fireAlarmSignOS = null;
            if (fireAlarmScreen != null)
                fireAlarmSignOS = (FireAlarmSignOS) fireAlarmScreen.getSignScreenManager().getSignScreen();

            if (fireAlarmSignOS != null) {
                fireAlarmSignOS.sendPopup(fireAlarmScreen.getSignScreenManager(), fireAlarmReason);
            } else {
                iterator.remove();
                this.plugin.getFireAlarmManager().deleteFireAlarmSign(this.name, entry.getKey());
            }
            //Signs DONE...
        }
    }

    private void resetStrobes() {
        for (Map.Entry<String, SimpleStrobe> entry : this.strobesMap.entrySet()) {
            String k = entry.getKey();
            SimpleStrobe v = entry.getValue();
            v.off();
        }
    }

    private int marchTime = 0;

    private void setupMarchTime() {
        if (!isAlarmCurrently) {
            isAlarmCurrently = true;

            new BukkitRunnable() {
                @Override
                public void run() {
                    if (fireAlarmStatus == FireAlarmStatus.ALARM) {
                        if (marchTime == 0) {
                            for (Map.Entry<String, SimpleStrobe> entry : strobesMap.entrySet()) {
                                String k = entry.getKey();
                                SimpleStrobe v = entry.getValue();
                                v.on();
                            }
                            marchTime++;
                        } else if (marchTime >= 1) {
                            for (Map.Entry<String, SimpleStrobe> entry : strobesMap.entrySet()) {
                                String k = entry.getKey();
                                SimpleStrobe v = entry.getValue();
                                v.off();
                            }
                            marchTime = 0;
                        }
                    } else {
                        isAlarmCurrently = false;
                        marchTime = 0;
                        resetStrobes();
                        this.cancel();
                    }
                }
            }.runTaskTimer(plugin, 10L, 10L);
        }
    }

    private int code3 = 0;

    private void setupCode3() {
        if (!this.isAlarmCurrently) {
            this.isAlarmCurrently = true;

            new BukkitRunnable() {
                @Override
                public void run() {
                    if (fireAlarmStatus == FireAlarmStatus.ALARM) {
                        if (code3 == 0 || code3 == 2 || code3 == 4) {
                            for (Map.Entry<String, SimpleStrobe> entry : strobesMap.entrySet()) {
                                String k = entry.getKey();
                                SimpleStrobe v = entry.getValue();
                                v.on();
                            }
                            code3++;
                        } else if (code3 == 1 || code3 == 3 || code3 == 5) {
                            for (Map.Entry<String, SimpleStrobe> entry : strobesMap.entrySet()) {
                                String k = entry.getKey();
                                SimpleStrobe v = entry.getValue();
                                v.off();
                            }
                            code3++;
                        } else if (code3 >= 6) {
                            if (code3 == 8) {
                                code3 = 0;
                                return;
                            }
                            code3++;
                        }
                    } else {
                        isAlarmCurrently = false;
                        code3 = 0;
                        resetStrobes();
                        this.cancel();
                    }
                }
            }.runTaskTimer(plugin, 10L, 10L);
        }
    }

    //GETTERS AND SETTERS
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, SimpleStrobe> getStrobesMap() {
        return strobesMap;
    }

    public void setStrobesMap(Map<String, SimpleStrobe> strobesMap) {
        this.strobesMap = strobesMap;
    }

    public Map<String, FireAlarmScreen> getSignsMap() {
        return signsMap;
    }

    public FireAlarmStatus getFireAlarmStatus() {
        return fireAlarmStatus;
    }

    public void setFireAlarmStatus(FireAlarmStatus fireAlarmStatus) {
        this.fireAlarmStatus = fireAlarmStatus;
    }

    public boolean isAlarmCurrently() {
        return isAlarmCurrently;
    }

    public void setAlarmCurrently(boolean alarmCurrently) {
        isAlarmCurrently = alarmCurrently;
    }

    public FireAlarmSettings getFireAlarmSettings() {
        return fireAlarmSettings;
    }

    public Location getMainChunk() {
        return mainChunk;
    }

    public void setFireAlarmSettings(FireAlarmSettings fireAlarmSettings) {
        this.fireAlarmSettings = fireAlarmSettings;
    }

    public int getMarchTime() {
        return marchTime;
    }

    public void setMarchTime(int marchTime) {
        this.marchTime = marchTime;
    }

    public int getCode3() {
        return code3;
    }

    public void setCode3(int code3) {
        this.code3 = code3;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("Name", this.name);
        map.put("MainChunk", this.mainChunk);
        map.put("FireAlarmSettings", this.fireAlarmSettings);
        map.put("StrobesMap", this.strobesMap);
        map.put("SignsMap", this.signsMap);
        return map;
    }

    public static SimpleFireAlarm deserialize(Map<String, Object> map) {
        return new SimpleFireAlarm(World16FireAlarms.getInstance(), (String) map.get("Name"), (Location) map.get("MainChunk"), (FireAlarmSettings) map.get("FireAlarmSettings"), (Map<String, SimpleStrobe>) map.get("StrobesMap"), (Map<String, FireAlarmScreen>) map.get("SignsMap"));
    }
}