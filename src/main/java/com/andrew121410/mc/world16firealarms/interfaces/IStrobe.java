package com.andrew121410.mc.world16firealarms.interfaces;

import com.andrew121410.mc.world16firealarms.FireAlarmSound;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

public interface IStrobe extends ConfigurationSerializable {

    void on();

    void off();

    void sound();

    String getName();

    Location getLocation();

    void setFireAlarmSound(FireAlarmSound fireAlarmSound);
}