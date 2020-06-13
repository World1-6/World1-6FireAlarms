package com.andrew121410.mc.world16firealarms.simple;

import com.andrew121410.mc.world16firealarms.*;
import com.andrew121410.mc.world16firealarms.interfaces.IFireAlarm;
import com.andrew121410.mc.world16firealarms.interfaces.IStrobe;
import com.andrew121410.mc.world16firealarms.sign.FireAlarmScreen;
import com.andrew121410.mc.world16firealarms.sign.FireAlarmSignOS;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@SerializableAs("IFireAlarm")
public class SimpleFireAlarm implements IFireAlarm, ConfigurationSerializable {

    private Main plugin;

    private String name;

    private Map<String, IStrobe> strobesMap;
    private Map<String, Location> signsMap;

    private FireAlarmStatus fireAlarmStatus;
    private boolean isAlarmCurrently = false;

    private FireAlarmSettings fireAlarmSettings;

    public SimpleFireAlarm(Main plugin, String name, FireAlarmSettings fireAlarmSettings) {
        this(plugin, name, fireAlarmSettings, new HashMap<>(), new HashMap<>());
    }

    public SimpleFireAlarm(Main plugin, String name, FireAlarmSettings fireAlarmSettings, Map<String, IStrobe> strobesMap, Map<String, Location> signsMap) {
        this.plugin = plugin;
        this.name = name;
        this.fireAlarmSettings = fireAlarmSettings;
        this.strobesMap = strobesMap;
        this.signsMap = signsMap;
        this.fireAlarmStatus = FireAlarmStatus.READY;
    }

    public void registerStrobe(IStrobe iStrobe) {
        this.strobesMap.putIfAbsent(iStrobe.getName(), iStrobe);
        this.strobesMap.get(iStrobe.getName()).setFireAlarmSound(fireAlarmSettings.getFireAlarmSound());
    }

    public void registerNac() {
    }

    public void registerSign(String string, Location location) {
        this.signsMap.put(string.toLowerCase(), location);
    }

    public void reset() {
        this.fireAlarmStatus = FireAlarmStatus.READY;

        //Signs
        Iterator<Map.Entry<String, Location>> iterator = this.signsMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Location> entry = iterator.next();
            FireAlarmScreen fireAlarmScreen = this.plugin.getSetListMap().getFireAlarmScreenMap().get(entry.getValue());
            FireAlarmSignOS fireAlarmSignOS = null;
            if (fireAlarmScreen != null)
                fireAlarmSignOS = (FireAlarmSignOS) fireAlarmScreen.getSignScreenManager().getSignScreen();

            if (fireAlarmSignOS != null) {
                fireAlarmSignOS.mainPage(fireAlarmScreen.getSignScreenManager());
            } else {
                iterator.remove();
                this.plugin.getFireAlarmManager().deleteFireAlarmSignScreen(name, entry.getKey().toLowerCase(), entry.getValue());
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
        Iterator<Map.Entry<String, Location>> iterator = this.signsMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Location> entry = iterator.next();
            FireAlarmScreen fireAlarmScreen = this.plugin.getSetListMap().getFireAlarmScreenMap().get(entry.getValue());
            FireAlarmSignOS fireAlarmSignOS = null;
            if (fireAlarmScreen != null)
                fireAlarmSignOS = (FireAlarmSignOS) fireAlarmScreen.getSignScreenManager().getSignScreen();

            if (fireAlarmSignOS != null) {
                fireAlarmSignOS.sendPopup(fireAlarmScreen.getSignScreenManager(), fireAlarmReason);
            } else {
                iterator.remove();
                this.plugin.getFireAlarmManager().deleteFireAlarmSignScreen(name, entry.getKey().toLowerCase(), entry.getValue());
            }
            //Signs DONE...
        }
    }

    private void resetStrobes() {
        for (Map.Entry<String, IStrobe> entry : this.strobesMap.entrySet()) {
            String k = entry.getKey();
            IStrobe v = entry.getValue();
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
                            for (Map.Entry<String, IStrobe> entry : strobesMap.entrySet()) {
                                String k = entry.getKey();
                                IStrobe v = entry.getValue();
                                v.on();
                            }
                            marchTime++;
                        } else if (marchTime >= 1) {
                            for (Map.Entry<String, IStrobe> entry : strobesMap.entrySet()) {
                                String k = entry.getKey();
                                IStrobe v = entry.getValue();
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
                            for (Map.Entry<String, IStrobe> entry : strobesMap.entrySet()) {
                                String k = entry.getKey();
                                IStrobe v = entry.getValue();
                                v.on();
                            }
                            code3++;
                        } else if (code3 == 1 || code3 == 3 || code3 == 5) {
                            for (Map.Entry<String, IStrobe> entry : strobesMap.entrySet()) {
                                String k = entry.getKey();
                                IStrobe v = entry.getValue();
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

    @Override
    public Map<String, IStrobe> getStrobesMap() {
        return strobesMap;
    }

    @Override
    public Map<String, Location> getSigns() {
        return signsMap;
    }

    public void setStrobesMap(Map<String, IStrobe> strobesMap) {
        this.strobesMap = strobesMap;
    }

    public Map<String, Location> getSignsMap() {
        return signsMap;
    }

    public void setSignsMap(Map<String, Location> signsMap) {
        this.signsMap = signsMap;
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

    @Override
    public FireAlarmSettings getFireAlarmSettings() {
        return fireAlarmSettings;
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
        map.put("FireAlarmSettings", this.fireAlarmSettings);
        map.put("StrobesMap", this.strobesMap);
        map.put("SignsMap", this.signsMap);
        return map;
    }

    public static SimpleFireAlarm deserialize(Map<String, Object> map) {
        return new SimpleFireAlarm(Main.getInstance(), (String) map.get("Name"), (FireAlarmSettings) map.get("FireAlarmSettings"), (Map<String, IStrobe>) map.get("StrobesMap"), (Map<String, Location>) map.get("SignsMap"));
    }
}