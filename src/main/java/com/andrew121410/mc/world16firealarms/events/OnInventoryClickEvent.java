package com.andrew121410.mc.world16firealarms.events;

import com.andrew121410.mc.world16firealarms.World16FireAlarms;
import com.andrew121410.mc.world16firealarms.sign.ScreenFocus;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Map;
import java.util.UUID;

public class OnInventoryClickEvent implements Listener {

    private Map<UUID, ScreenFocus> screenFocusMap;

    private World16FireAlarms plugin;

    public OnInventoryClickEvent(World16FireAlarms plugin) {
        this.plugin = plugin;
        this.screenFocusMap = this.plugin.getSetListMap().getScreenFocusMap();
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void OnInvClickEvent(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (this.screenFocusMap.containsKey(player.getUniqueId())) event.setCancelled(true);
    }
}
