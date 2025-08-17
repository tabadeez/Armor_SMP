package com.yourname.armorslot;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.ChatColor;

public class ArmorSlotItemListener implements Listener {
    
    private final ArmorSlotManager armorSlotManager;
    
    public ArmorSlotItemListener(ArmorSlotManager armorSlotManager) {
        this.armorSlotManager = armorSlotManager;
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        
        if (!ArmorSlotItem.isArmorSlotItem(item)) {
            return;
        }
        
        event.setCancelled(true);
        
        // Check if player has all armor slots
        if (armorSlotManager.hasAllArmorSlots(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "You already have all armor slots!");
            return;
        }
        
        // Give player an armor slot
        armorSlotManager.addArmorSlot(player.getUniqueId());
        
        // Remove one item from the stack
        if (item.getAmount() > 1) {
            item.setAmount(item.getAmount() - 1);
        } else {
            player.getInventory().setItemInMainHand(null);
        }
        
        player.sendMessage(ChatColor.GREEN + "You have restored an armor slot!");
    }
}