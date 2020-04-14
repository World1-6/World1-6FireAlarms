package com.andrew121410.mc.world16firealarms;

import org.bukkit.Sound;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.HashMap;
import java.util.Map;

@SerializableAs("FireAlarmSound")
public class FireAlarmSound implements ConfigurationSerializable {

    private Sound sound = Sound.BLOCK_NOTE_BLOCK_PLING;
    private float volume = 1F;
    private float pitch = 1F;

    public FireAlarmSound() {
    }

    public FireAlarmSound(Sound sound, float volume, float pitch) {
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

    public Sound getSound() {
        return sound;
    }

    public void setSound(Sound sound) {
        this.sound = sound;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("Sound", this.sound.toString());
        map.put("Volume", this.volume);
        map.put("Pitch", this.pitch);
        return map;
    }

    public static FireAlarmSound deserialize(Map<String, Object> map) {
        double fakeVolume = (double) map.get("Volume");
        double fakePitch = (double) map.get("Pitch");
        return new FireAlarmSound(Sound.valueOf((String) map.get("Sound")), (float) fakeVolume, (float) fakePitch);
    }
}