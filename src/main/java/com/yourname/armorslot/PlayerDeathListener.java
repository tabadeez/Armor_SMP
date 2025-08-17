package com.yourname.armorslot;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.ChatColor;
import org.bukkit.Bukkit;

public class PlayerDeathListener implements Listener {
    
    private final ArmorSlotManager armorSlotManager;
    
    public PlayerDeathListener(ArmorSlotManager armorSlotManager) {
        this.armorSlotManager = armorSlotManager;
    }
    
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();
        
        // Only process if killed by another player
        if (killer == null || killer.equals(victim)) {
            return;
        }
        
        // Check if victim has only boots left (final warning)
        if (armorSlotManager.hasOnlyBootsLeft(victim.getUniqueId())) {
            victim.sendMessage(ChatColor.RED + "You have one more chance and then you're gone for good!");
        }
        
        // Remove armor slot from victim
        armorSlotManager.removeArmorSlot(victim.getUniqueId());
        
        // Check if victim should be banned
        if (armorSlotManager.hasNoArmorSlots(victim.getUniqueId())) {
            Bukkit.getScheduler().runTask(Bukkit.getPluginManager().getPlugin("ArmorSlotPlugin"), () -> {
                victim.banPlayer("You Ran Out of Armor slots");
            });
            return;
        }
        
        // Handle killer gaining armor slot
        if (armorSlotManager.hasAllArmorSlots(killer.getUniqueId()) && !armorSlotManager.hasNoArmorSlots(victim.getUniqueId())) {
            // Drop armor slot item if killer already has all slots
            ArmorSlotItem.dropArmorSlotItem(killer.getLocation());
        } else if (!armorSlotManager.hasNoArmorSlots(victim.getUniqueId())) {
            // Give killer an armor slot
            armorSlotManager.addArmorSlot(killer.getUniqueId());
        }
        
        // Remove any disallowed armor from victim
        Bukkit.getScheduler().runTask(Bukkit.getPluginManager().getPlugin("ArmorSlotPlugin"), () -> {
            armorSlotManager.removeDisallowedArmor(victim);
        });
    }
}