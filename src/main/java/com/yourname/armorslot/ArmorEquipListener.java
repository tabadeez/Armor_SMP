package com.yourname.armorslot;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.block.BlockDispenseArmorEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.Bukkit;

public class ArmorEquipListener implements Listener {
    
    private final ArmorSlotManager armorSlotManager;
    
    public ArmorEquipListener(ArmorSlotManager armorSlotManager) {
        this.armorSlotManager = armorSlotManager;
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        
        Player player = (Player) event.getWhoClicked();
        
        // Check if clicking in armor slots
        if (event.getSlotType() == InventoryType.SlotType.ARMOR) {
            ArmorSlotManager.ArmorSlot armorSlot = getArmorSlotFromSlot(event.getSlot());
            if (armorSlot != null && !armorSlotManager.hasArmorSlot(player.getUniqueId(), armorSlot)) {
                event.setCancelled(true);
                return;
            }
        }
        
        // Check if trying to equip armor by clicking on it
        ItemStack cursor = event.getCursor();
        ItemStack current = event.getCurrentItem();
        
        if (cursor != null && isArmor(cursor)) {
            ArmorSlotManager.ArmorSlot armorSlot = getArmorSlotFromItem(cursor);
            if (armorSlot != null && !armorSlotManager.hasArmorSlot(player.getUniqueId(), armorSlot)) {
                if (event.getSlotType() == InventoryType.SlotType.ARMOR) {
                    event.setCancelled(true);
                }
            }
        }
        
        if (current != null && isArmor(current)) {
            ArmorSlotManager.ArmorSlot armorSlot = getArmorSlotFromItem(current);
            if (armorSlot != null && !armorSlotManager.hasArmorSlot(player.getUniqueId(), armorSlot)) {
                if (event.getSlotType() == InventoryType.SlotType.ARMOR) {
                    event.setCancelled(true);
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        
        if (item != null && isArmor(item)) {
            ArmorSlotManager.ArmorSlot armorSlot = getArmorSlotFromItem(item);
            if (armorSlot != null && !armorSlotManager.hasArmorSlot(player.getUniqueId(), armorSlot)) {
                // Schedule a task to remove the armor if it was equipped
                Bukkit.getScheduler().runTask(Bukkit.getPluginManager().getPlugin("ArmorSlotPlugin"), () -> {
                    armorSlotManager.removeDisallowedArmor(player);
                });
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockDispenseArmor(BlockDispenseArmorEvent event) {
        if (!(event.getTargetEntity() instanceof Player)) {
            return;
        }
        
        Player player = (Player) event.getTargetEntity();
        ItemStack item = event.getItem();
        
        if (isArmor(item)) {
            ArmorSlotManager.ArmorSlot armorSlot = getArmorSlotFromItem(item);
            if (armorSlot != null && !armorSlotManager.hasArmorSlot(player.getUniqueId(), armorSlot)) {
                event.setCancelled(true);
            }
        }
    }
    
    private boolean isArmor(ItemStack item) {
        Material type = item.getType();
        return type.name().endsWith("_HELMET") || 
               type.name().endsWith("_CHESTPLATE") || 
               type.name().endsWith("_LEGGINGS") || 
               type.name().endsWith("_BOOTS");
    }
    
    private ArmorSlotManager.ArmorSlot getArmorSlotFromItem(ItemStack item) {
        Material type = item.getType();
        if (type.name().endsWith("_HELMET")) {
            return ArmorSlotManager.ArmorSlot.HELMET;
        } else if (type.name().endsWith("_CHESTPLATE")) {
            return ArmorSlotManager.ArmorSlot.CHESTPLATE;
        } else if (type.name().endsWith("_LEGGINGS")) {
            return ArmorSlotManager.ArmorSlot.LEGGINGS;
        } else if (type.name().endsWith("_BOOTS")) {
            return ArmorSlotManager.ArmorSlot.BOOTS;
        }
        return null;
    }
    
    private ArmorSlotManager.ArmorSlot getArmorSlotFromSlot(int slot) {
        switch (slot) {
            case 39: return ArmorSlotManager.ArmorSlot.HELMET;
            case 38: return ArmorSlotManager.ArmorSlot.CHESTPLATE;
            case 37: return ArmorSlotManager.ArmorSlot.LEGGINGS;
            case 36: return ArmorSlotManager.ArmorSlot.BOOTS;
            default: return null;
        }
    }
}