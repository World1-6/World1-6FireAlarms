package com.andrew121410.mc.world16firealarms;

import com.andrew121410.mc.world16firealarms.commands.FireAlarmCMD;
import com.andrew121410.mc.world16firealarms.events.OnBlockBreakEvent;
import com.andrew121410.mc.world16firealarms.events.OnInventoryClickEvent;
import com.andrew121410.mc.world16firealarms.events.OnPlayerInteractEvent;
import com.andrew121410.mc.world16firealarms.events.OnPlayerQuitEvent;
import com.andrew121410.mc.world16firealarms.managers.FireAlarmChunkSmartManager;
import com.andrew121410.mc.world16firealarms.managers.FireAlarmManager;
import com.andrew121410.mc.world16firealarms.sign.FireAlarmScreen;
import com.andrew121410.mc.world16firealarms.simple.SimpleFireAlarm;
import com.andrew121410.mc.world16firealarms.simple.SimpleStrobe;
import com.andrew121410.mc.world16firealarms.utils.OtherPlugins;
import com.andrew121410.mc.world16firealarms.utils.SetListMap;
import com.andrew121410.mc.world16utils.updater.UpdateManager;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

public final class World16FireAlarms extends JavaPlugin {

    private static World16FireAlarms instance;

    static {
        ConfigurationSerialization.registerClass(FireAlarmSound.class, "FireAlarmSound");
        ConfigurationSerialization.registerClass(FireAlarmSettings.class, "FireAlarmSettings");
        ConfigurationSerialization.registerClass(SimpleStrobe.class, "SimpleStrobe");
        ConfigurationSerialization.registerClass(SimpleFireAlarm.class, "SimpleFireAlarm");
        ConfigurationSerialization.registerClass(FireAlarmScreen.class, "FireAlarmScreen");
    }

    private SetListMap setListMap;
    private OtherPlugins otherPlugins;

    private FireAlarmManager fireAlarmManager;

    //Config
    private boolean chunkSmartManagement = false;

    public static World16FireAlarms getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        this.setListMap = new SetListMap();
        this.otherPlugins = new OtherPlugins(this);

        regDefaultConfig();

        //Config
        this.chunkSmartManagement = this.getConfig().getBoolean("ChunkSmartManagement");

        this.fireAlarmManager = new FireAlarmManager(this);
        this.fireAlarmManager.loadAllFireAlarms();

        if (chunkSmartManagement) {
            this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new FireAlarmChunkSmartManager(this), 200L, 200L);
        }

        regEvents();
        regCommands();

        UpdateManager.registerUpdater(this, new com.andrew121410.mc.world16firealarms.Updater(this));
    }

    @Override
    public void onDisable() {
        this.fireAlarmManager.saveAllFireAlarms();
    }

    private void regCommands() {
        new FireAlarmCMD(this);
    }

    private void regEvents() {
        new OnBlockBreakEvent(this);
        new OnPlayerInteractEvent(this);
        new OnInventoryClickEvent(this);
        new OnPlayerQuitEvent(this);
    }

    private void regDefaultConfig() {
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
        this.reloadConfig();
    }

    public SetListMap getSetListMap() {
        return setListMap;
    }

    public FireAlarmManager getFireAlarmManager() {
        return fireAlarmManager;
    }

    public OtherPlugins getOtherPlugins() {
        return otherPlugins;
    }

    public boolean isChunkSmartManagement() {
        return chunkSmartManagement;
    }
}
