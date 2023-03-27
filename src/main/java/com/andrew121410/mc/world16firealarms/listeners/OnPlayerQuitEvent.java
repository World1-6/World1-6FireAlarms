package com.andrew121410.mc.world16firealarms.listeners;

import com.andrew121410.mc.world16firealarms.World16FireAlarms;
import com.andrew121410.mc.world16firealarms.sign.ScreenFocus;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Map;
import java.util.UUID;

public class OnPlayerQuitEvent implements Listener {

    private final Map<UUID, ScreenFocus> screenFocusMap;

    private final World16FireAlarms plugin;

    public OnPlayerQuitEvent(World16FireAlarms plugin) {
        this.plugin = plugin;
        this.screenFocusMap = this.plugin.getSetListMap().getScreenFocusMap();
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        ScreenFocus screenFocus = this.screenFocusMap.get(player.getUniqueId());
        if (screenFocus != null) {
            screenFocus.revert();
            this.screenFocusMap.remove(player.getUniqueId());
        }
    }
}
