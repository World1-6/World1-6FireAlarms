package com.andrew121410.mc.world16firealarms.listeners;

import com.andrew121410.mc.world16firealarms.World16FireAlarms;
import com.andrew121410.mc.world16firealarms.sign.FireAlarmScreen;
import com.andrew121410.mc.world16firealarms.sign.ScreenFocus;
import com.andrew121410.mc.world16utils.blocks.UniversalBlockUtils;
import com.andrew121410.mc.world16utils.sign.screen.SignScreenManager;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;
import java.util.UUID;

public class OnPlayerInteractEvent implements Listener {

    private final Map<Location, FireAlarmScreen> fireAlarmScreenMap;
    private final Map<UUID, ScreenFocus> screenFocusMap;

    private final World16FireAlarms plugin;

    public OnPlayerInteractEvent(World16FireAlarms plugin) {
        this.plugin = plugin;

        this.fireAlarmScreenMap = this.plugin.getSetListMap().getFireAlarmScreenMap();
        this.screenFocusMap = this.plugin.getSetListMap().getScreenFocusMap();

        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        Action action = event.getAction();

        if (block != null && action == Action.RIGHT_CLICK_BLOCK) {
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            ItemMeta itemMeta = itemStack.getItemMeta();
            FireAlarmScreen fireAlarmScreen = this.fireAlarmScreenMap.get(block.getLocation());
            ScreenFocus screenFocus = this.screenFocusMap.get(player.getUniqueId());
            boolean isSign = UniversalBlockUtils.isSign(block) != null;

            if (!isSign && screenFocus != null) {
                event.setCancelled(true);
                screenFocus.revert();
                this.screenFocusMap.remove(player.getUniqueId());
            }

            if (fireAlarmScreen == null) return;
            fireAlarmShit(event, player, itemMeta, fireAlarmScreen, screenFocus);
        }
    }

    private void fireAlarmShit(PlayerInteractEvent event, Player p, ItemMeta itemMeta, FireAlarmScreen fireAlarmScreen, ScreenFocus screenFocus) {
        SignScreenManager signScreenManager = fireAlarmScreen.getSignScreenManager();

        if (screenFocus == null) {
            fireAlarmScreen.getSignScreenManager().tick(p);
            this.screenFocusMap.putIfAbsent(p.getUniqueId(), new ScreenFocus(plugin, p));
            return;
        }

        if (itemMeta != null && itemMeta.hasDisplayName()) {
            if (itemMeta.getDisplayName().equalsIgnoreCase("Exit")) {
                event.setCancelled(true);
                signScreenManager.setStop(true);
                screenFocus.revert();
                this.screenFocusMap.remove(p.getUniqueId());
            } else if (itemMeta.getDisplayName().equalsIgnoreCase("SCROLL UP")) {
                signScreenManager.onScroll(p, true);
            } else if (itemMeta.getDisplayName().equalsIgnoreCase("SCROLL DOWN")) {
                signScreenManager.onScroll(p, false);
            }
        } else signScreenManager.onClick(p);
    }
}
