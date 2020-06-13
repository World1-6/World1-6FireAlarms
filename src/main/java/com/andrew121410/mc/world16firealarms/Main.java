package com.andrew121410.mc.world16firealarms;

import com.andrew121410.mc.world16firealarms.commands.FireAlarmCMD;
import com.andrew121410.mc.world16firealarms.events.OnBlockBreakEvent;
import com.andrew121410.mc.world16firealarms.events.OnInventoryClickEvent;
import com.andrew121410.mc.world16firealarms.events.OnPlayerInteractEvent;
import com.andrew121410.mc.world16firealarms.events.OnPlayerQuitEvent;
import com.andrew121410.mc.world16firealarms.managers.FireAlarmManager;
import com.andrew121410.mc.world16firealarms.sign.FireAlarmScreen;
import com.andrew121410.mc.world16firealarms.simple.SimpleFireAlarm;
import com.andrew121410.mc.world16firealarms.simple.SimpleStrobe;
import com.andrew121410.mc.world16firealarms.utils.SetListMap;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    static {
        ConfigurationSerialization.registerClass(FireAlarmSound.class, "FireAlarmSound");
        ConfigurationSerialization.registerClass(FireAlarmSettings.class, "FireAlarmSettings");
        ConfigurationSerialization.registerClass(SimpleStrobe.class, "IStrobe");
        ConfigurationSerialization.registerClass(SimpleFireAlarm.class, "IFireAlarm");
        ConfigurationSerialization.registerClass(FireAlarmScreen.class, "FireAlarmScreen");
    }

    private static Main instance;

    private SetListMap setListMap;

    private FireAlarmManager fireAlarmManager;

    @Override
    public void onEnable() {
        instance = this;
        this.setListMap = new SetListMap();

        this.fireAlarmManager = new FireAlarmManager(this);
        this.fireAlarmManager.loadFireAlarms();

        regEvents();
        regCommands();
    }

    @Override
    public void onDisable() {
        this.fireAlarmManager.saveFireAlarms();
    }

    public void regCommands() {
        new FireAlarmCMD(this);
    }

    public void regEvents() {
        new OnBlockBreakEvent(this);
        new OnPlayerInteractEvent(this);
        new OnInventoryClickEvent(this);
        new OnPlayerQuitEvent(this);
    }

    public static Main getInstance() {
        return instance;
    }

    public SetListMap getSetListMap() {
        return setListMap;
    }

    public FireAlarmManager getFireAlarmManager() {
        return fireAlarmManager;
    }
}
