package com.andrew121410.mc.world16firealarms.managers;

import com.andrew121410.mc.world16firealarms.Main;
import com.andrew121410.mc.world16firealarms.interfaces.IFireAlarm;
import com.andrew121410.mc.world16firealarms.sign.FireAlarmScreen;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.config.CustomYmlManager;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Map;

public class FireAlarmManager {

    private Main plugin;

    private CustomYmlManager fireAlarmsYml;

    private Map<String, IFireAlarm> fireAlarmMap;
    private Map<Location, FireAlarmScreen> fireAlarmScreenMap;

    public FireAlarmManager(Main plugin) {
        this.plugin = plugin;

        //firealarms.yml
        this.fireAlarmsYml = new CustomYmlManager(this.plugin, false);
        this.fireAlarmsYml.setup("firealarms.yml");
        this.fireAlarmsYml.saveConfig();
        this.fireAlarmsYml.reloadConfig();
        //...

        this.fireAlarmMap = this.plugin.getSetListMap().getFireAlarmMap();
        this.fireAlarmScreenMap = this.plugin.getSetListMap().getFireAlarmScreenMap();
    }

    public void loadFireAlarms() {
        ConfigurationSection cs = this.fireAlarmsYml.getConfig().getConfigurationSection("FireAlarms");
        ConfigurationSection cs1 = this.fireAlarmsYml.getConfig().getConfigurationSection("FireAlarmScreens");

        //Only runs when firealarms.yml is first being created.
        if (cs == null || cs1 == null) {
            if (cs == null) {
                this.fireAlarmsYml.getConfig().createSection("FireAlarms");
                this.fireAlarmsYml.saveConfig();
                this.plugin.getServer().getConsoleSender().sendMessage(Translate.chat("&c[FireAlarmManager]&r&6 FireAlarm section has been created."));
            }
            if (cs1 == null) {
                this.fireAlarmsYml.getConfig().createSection("FireAlarmScreens");
                this.fireAlarmsYml.saveConfig();
            }
            return;
        }

        //For each fire alarm.
        for (String fireAlarm : cs.getKeys(false)) {
            ConfigurationSection fireAlarmConfig = cs.getConfigurationSection(fireAlarm);
            IFireAlarm iFireAlarm = (IFireAlarm) fireAlarmConfig.get("IFireAlarm");
            fireAlarmMap.putIfAbsent(fireAlarm, iFireAlarm);
        }

        //For each fire alarm screen
        for (String fireAlarmScreenName : cs1.getKeys(false)) {
            ConfigurationSection cs2 = cs1.getConfigurationSection(fireAlarmScreenName);
            FireAlarmScreen fireAlarmScreen = (FireAlarmScreen) cs2.get("FireAlarmScreen");
            this.fireAlarmScreenMap.putIfAbsent(fireAlarmScreen.getLocation(), fireAlarmScreen);
        }
    }

    public void saveFireAlarms() {
        //For each fire alarm
        for (Map.Entry<String, IFireAlarm> entry : fireAlarmMap.entrySet()) {
            String k = entry.getKey();
            IFireAlarm v = entry.getValue();

            String fireAlarmLocation = "FireAlarms" + "." + k.toLowerCase();

            ConfigurationSection fireAlarmConfig = this.fireAlarmsYml.getConfig().getConfigurationSection(fireAlarmLocation);
            if (fireAlarmConfig == null) {
                fireAlarmConfig = this.fireAlarmsYml.getConfig().createSection(fireAlarmLocation);
                this.fireAlarmsYml.saveConfig();
            }

            fireAlarmConfig.set("IFireAlarm", v);
            this.fireAlarmsYml.saveConfig();
        }

        //For each sign screen
        for (Map.Entry<Location, FireAlarmScreen> entry : fireAlarmScreenMap.entrySet()) {
            Location k = entry.getKey();
            FireAlarmScreen v = entry.getValue();

            String fireAlarmScreen = "FireAlarmScreens" + "." + v.getName().toLowerCase();

            ConfigurationSection fireAlarmScreenConfig = this.fireAlarmsYml.getConfig().getConfigurationSection(fireAlarmScreen);
            if (fireAlarmScreenConfig == null) {
                fireAlarmScreenConfig = this.fireAlarmsYml.getConfig().createSection(fireAlarmScreen);
                this.fireAlarmsYml.saveConfig();
            }

            fireAlarmScreenConfig.set("FireAlarmScreen", v);
            this.fireAlarmsYml.saveConfig();
        }
    }

    public void deleteFireAlarm(String name) {
        if (this.fireAlarmMap.get(name) != null) {
            fireAlarmMap.remove(name);

            ConfigurationSection firealarmConfig = this.fireAlarmsYml.getConfig().getConfigurationSection("FireAlarms");
            if (firealarmConfig != null)
                firealarmConfig.set(name.toLowerCase(), null);
            this.fireAlarmsYml.saveConfig();
        }
    }

    public void deleteFireAlarmStrobe(String fireAlarm, String strobeName) {
        if (this.fireAlarmMap.get(fireAlarm) != null) {
            this.fireAlarmMap.get(fireAlarm).getStrobesMap().remove(strobeName);
            ConfigurationSection fireAlarmConfig = this.fireAlarmsYml.getConfig().getConfigurationSection("FireAlarms." + fireAlarm.toLowerCase());
            if (fireAlarmConfig != null) {
                ConfigurationSection fireAlarmStrobes = fireAlarmConfig.getConfigurationSection("Strobes");
                if (fireAlarmStrobes != null)
                    fireAlarmStrobes.set(strobeName.toLowerCase(), null);
            }
            this.fireAlarmsYml.saveConfig();
        }
    }

    public void deleteFireAlarmSignScreen(Location location) {
        if (this.fireAlarmScreenMap.get(location) != null) {
            String signName = this.fireAlarmScreenMap.get(location).getName();
            fireAlarmScreenMap.remove(location);

            ConfigurationSection firealarmScreensConfig = this.fireAlarmsYml.getConfig().getConfigurationSection("FireAlarmScreens");
            if (firealarmScreensConfig != null)
                firealarmScreensConfig.set(signName.toLowerCase(), null);
            this.fireAlarmsYml.saveConfig();
        }
    }

    public void deleteFireAlarmSignScreen(String fireAlarmName, String signName, Location location) {
        if (this.fireAlarmMap.get(fireAlarmName.toLowerCase()) != null) {
            fireAlarmScreenMap.remove(location);
            fireAlarmMap.get(fireAlarmName).getSigns().remove(signName);

            ConfigurationSection cs = this.fireAlarmsYml.getConfig().getConfigurationSection("FireAlarms." + fireAlarmName.toLowerCase());
            if (cs != null) {
                ConfigurationSection cs1 = cs.getConfigurationSection("Signs");
                if (cs1 != null)
                    cs1.set(signName.toLowerCase(), null);
            }

            ConfigurationSection fireAlarmScreensConfig = this.fireAlarmsYml.getConfig().getConfigurationSection("FireAlarmScreens");
            if (fireAlarmScreensConfig != null)
                fireAlarmScreensConfig.set(signName.toLowerCase(), null);
            this.fireAlarmsYml.saveConfig();
        }
    }
}