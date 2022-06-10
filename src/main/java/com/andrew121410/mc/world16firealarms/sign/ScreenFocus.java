package com.andrew121410.mc.world16firealarms.sign;

import com.andrew121410.mc.world16firealarms.World16FireAlarms;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;

public class ScreenFocus {

    private final World16FireAlarms plugin;

    private final Player player;

    private final ItemStack[] oldInv;
    private final Collection<PotionEffect> potionEffects;

    public ScreenFocus(World16FireAlarms plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        this.oldInv = player.getInventory().getContents();
        this.potionEffects = this.player.getActivePotionEffects();

        for (PotionEffect effect : this.player.getActivePotionEffects())
            this.player.removePotionEffect(effect.getType());

        this.player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 1, true, false));
        this.player.getInventory().clear();
        giveTools();
    }

    public void giveTools() {

        ItemStack SCROLLDOWN = new ItemStack(Material.GOLDEN_SWORD);
        ItemMeta SCROLLDOWNItemMeta = SCROLLDOWN.getItemMeta();
        SCROLLDOWNItemMeta.setDisplayName("SCROLL DOWN");
        SCROLLDOWN.setItemMeta(SCROLLDOWNItemMeta);

        ItemStack SCROLLUP = new ItemStack(Material.GOLDEN_SWORD);
        ItemMeta SCROLlUPITEMMETA = SCROLLUP.getItemMeta();
        SCROLlUPITEMMETA.setDisplayName("SCROLL UP");
        SCROLLUP.setItemMeta(SCROLlUPITEMMETA);

        ItemStack Exit = new ItemStack(Material.BARRIER);
        ItemMeta ExitItemmeta = Exit.getItemMeta();
        ExitItemmeta.setDisplayName("EXIT");
        Exit.setItemMeta(ExitItemmeta);

        this.player.getInventory().setItem(0, SCROLLDOWN);
        this.player.getInventory().setItem(2, SCROLLUP);
        this.player.getInventory().setItem(8, Exit);
    }

    public void revert() {
        this.player.getInventory().clear();

        for (PotionEffect effect : this.player.getActivePotionEffects())
            this.player.removePotionEffect(effect.getType());

        this.player.addPotionEffects(this.potionEffects);
        this.player.getInventory().setContents(this.oldInv);
    }
}