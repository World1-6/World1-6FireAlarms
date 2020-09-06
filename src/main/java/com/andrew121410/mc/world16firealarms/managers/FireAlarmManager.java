package com.andrew121410.mc.world16firealarms.managers;

import com.andrew121410.mc.world16firealarms.World16FireAlarms;
import com.andrew121410.mc.world16firealarms.sign.FireAlarmScreen;
import com.andrew121410.mc.world16firealarms.simple.SimpleFireAlarm;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.config.CustomYmlManager;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Map;

public class FireAlarmManager {

    private World16FireAlarms plugin;

    private CustomYmlManager fireAlarmsYml;

    private Map<String, SimpleFireAlarm> fireAlarmMap;
    private Map<Location, FireAlarmScreen> fireAlarmScreenMap;

    public FireAlarmManager(World16FireAlarms plugin) {
        this.plugin = plugin;
        this.fireAlarmMap = this.plugin.getSetListMap().getFireAlarmMap();
        this.fireAlarmScreenMap = this.plugin.getSetListMap().getFireAlarmScreenMap();

        //firealarms.yml
        this.fireAlarmsYml = new CustomYmlManager(this.plugin, false);
        this.fireAlarmsYml.setup("firealarms.yml");
        this.fireAlarmsYml.saveConfig();
        this.fireAlarmsYml.reloadConfig();
        //...
    }

    private SimpleFireAlarm load(String key) {
        ConfigurationSection cs = this.fireAlarmsYml.getConfig().getConfigurationSection("FireAlarms");
        return (SimpleFireAlarm) cs.get(key);
    }

    private void save(SimpleFireAlarm iFireAlarm) {
        ConfigurationSection cs = this.fireAlarmsYml.getConfig().getConfigurationSection("FireAlarms");
        cs.set(iFireAlarm.getName(), iFireAlarm);
        this.fireAlarmsYml.saveConfig();
    }

    public void loadAllFireAlarms() {
        ConfigurationSection cs = getFireAlarmsConfigurationSection();

        for (String key : cs.getKeys(false)) {
            SimpleFireAlarm simpleFireAlarm = load(key);
            if (this.plugin.isChunkSmartManagement()) {
                this.plugin.getSetListMap().getChunkToFireAlarmName().put(simpleFireAlarm.getMainChunk(), key);
            } else {
                this.fireAlarmMap.put(key, simpleFireAlarm);
                for (FireAlarmScreen value : simpleFireAlarm.getSignsMap().values()) {
                    this.fireAlarmScreenMap.put(value.getLocation(), value);
                }
            }
        }
    }

    public void saveAllFireAlarms() {
        ConfigurationSection cs = getFireAlarmsConfigurationSection();
        for (SimpleFireAlarm value : this.fireAlarmMap.values()) save(value);
    }

    public SimpleFireAlarm loadUpFireAlarm(String key) {
        SimpleFireAlarm simpleFireAlarm = load(key);
        if (simpleFireAlarm == null) throw new NullPointerException("NPE loadUpFireAlarm");
        this.fireAlarmMap.putIfAbsent(simpleFireAlarm.getName(), simpleFireAlarm);
        for (FireAlarmScreen value : simpleFireAlarm.getSignsMap().values()) {
            this.fireAlarmScreenMap.putIfAbsent(value.getLocation(), value);
        }
        return simpleFireAlarm;
    }

    public void saveAndUnloadFireAlarm(SimpleFireAlarm iFireAlarm) {
        save(iFireAlarm);
        for (FireAlarmScreen value : iFireAlarm.getSignsMap().values()) {
            this.fireAlarmScreenMap.remove(value.getLocation());
        }
        this.fireAlarmMap.remove(iFireAlarm.getName());
    }

    public void deleteFireAlarm(String fireAlarm) {
        ConfigurationSection cs = getFireAlarmsConfigurationSection();
        SimpleFireAlarm simpleFireAlarm = this.fireAlarmMap.get(fireAlarm);
        if (simpleFireAlarm == null) return;
        cs.set(simpleFireAlarm.getName(), null);
        this.fireAlarmsYml.saveConfig();
    }

    public void deleteFireAlarmSign(String fireAlarm, String signName) {
        SimpleFireAlarm simpleFireAlarm = this.fireAlarmMap.get(fireAlarm);
        FireAlarmScreen fireAlarmScreen = simpleFireAlarm.getSignsMap().get(signName);
        if (fireAlarmScreen == null) return;
        this.fireAlarmScreenMap.remove(fireAlarmScreen.getLocation());
        simpleFireAlarm.getSignsMap().remove(fireAlarmScreen.getName());
    }

    public void deleteFireAlarmSign(FireAlarmScreen fireAlarmScreen) {
        deleteFireAlarmSign(fireAlarmScreen.getFireAlarmName(), fireAlarmScreen.getName());
    }

    public ConfigurationSection getFireAlarmsConfigurationSection() {
        ConfigurationSection cs = this.fireAlarmsYml.getConfig().getConfigurationSection("FireAlarms");
        if (cs == null) {
            this.fireAlarmsYml.getConfig().createSection("FireAlarms");
            this.fireAlarmsYml.saveConfig();
            this.plugin.getServer().getConsoleSender().sendMessage(Translate.chat("&c[FireAlarmManager]&r&6 FireAlarm section has been created."));
        }
        return cs;
    }
}