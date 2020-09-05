package com.andrew121410.mc.world16firealarms.interfaces;

import com.andrew121410.mc.world16firealarms.FireAlarmReason;
import com.andrew121410.mc.world16firealarms.FireAlarmSettings;
import com.andrew121410.mc.world16firealarms.sign.FireAlarmScreen;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.Map;

public interface IFireAlarm extends ConfigurationSerializable {

    String getName();

    void registerNac();

    void registerStrobe(IStrobe iStrobe);

    void registerSign(String string, FireAlarmScreen fireAlarmScreen);

    void reset();

    void trouble();

    void alarm(FireAlarmReason fireAlarmReason);

    Map<String, IStrobe> getStrobesMap();

    Map<String, FireAlarmScreen> getSignsMap();

    FireAlarmSettings getFireAlarmSettings();

    Location getMainChunk();
}
