package com.yourname.armorslot;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Bukkit;

public class RevivalCraftListener implements Listener {
    
    private final ArmorSlotManager armorSlotManager;
    
    public RevivalCraftListener(ArmorSlotManager armorSlotManager) {
        this.armorSlotManager = armorSlotManager;
    }
    
    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        
        Player player = (Player) event.getWhoClicked();
        
        // Check if this is our revival recipe
        if (event.getRecipe().getKey().getKey().equals("revival")) {
            // Recipe now creates Revival Star item - no special handling needed
            player.sendMessage(ChatColor.GREEN + "You have crafted a Revival Star!");
            player.sendMessage(ChatColor.YELLOW + "Right-click it to revive an eliminated player.");
        }
    }
    
    private void getLogger() {
        // Helper method to access plugin logger
        Bukkit.getLogger();
    }
}