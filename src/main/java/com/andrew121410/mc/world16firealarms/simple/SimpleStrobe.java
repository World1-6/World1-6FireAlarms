package com.andrew121410.mc.world16firealarms.simple;

import com.andrew121410.mc.world16firealarms.FireAlarmSound;
import com.andrew121410.mc.world16firealarms.interfaces.IStrobe;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Lightable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.HashMap;
import java.util.Map;

@SerializableAs("IStrobe")
public class SimpleStrobe implements IStrobe {

    private final String name;
    private final Location location;
    private FireAlarmSound fireAlarmSound;

    private boolean isLight;

    public SimpleStrobe(Location block, String name) {
        this.location = block;
        this.name = name;

        this.fireAlarmSound = new FireAlarmSound();

        BlockData data = this.location.getBlock().getBlockData();
        if (data instanceof Lightable) isLight = true;
    }

    public SimpleStrobe(Block block, String name) {
        this(block.getLocation(), name);
    }

    public static SimpleStrobe deserialize(Map<String, Object> map) {
        return new SimpleStrobe((Location) map.get("Location"), (String) map.get("Name"));
    }

    public void on() {
        if (isLight) {
            sound();
            BlockData data = this.location.getBlock().getBlockData();
            if (data instanceof Lightable) {
                ((Lightable) data).setLit(true);
                this.location.getBlock().setBlockData(data);
                isLight = true;
            } else isLight = false;
            return;
        }
        this.location.getBlock().setType(Material.REDSTONE_BLOCK);
    }

    public void off() {
        if (isLight) {
            this.location.getBlock().setType(Material.REDSTONE_LAMP);
            return;
        }
        this.location.getBlock().setType(Material.SOUL_SAND);
    }

    public void sound() {
        if (this.location.getWorld() != null)
            this.location.getWorld().playSound(location, fireAlarmSound.getSound(), fireAlarmSound.getVolume(), fireAlarmSound.getPitch());
    }

    public Location getLocation() {
        return this.location;
    }

    public String getName() {
        return this.name;
    }

    public FireAlarmSound getFireAlarmSound() {
        return fireAlarmSound;
    }

    public void setFireAlarmSound(FireAlarmSound fireAlarmSound) {
        this.fireAlarmSound = fireAlarmSound;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("Location", this.location);
        map.put("Name", this.name);
        return map;
    }
}