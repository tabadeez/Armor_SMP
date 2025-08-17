package com.yourname.armorslot;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class PlayerJoinListener implements Listener {
    
    private final ArmorSlotManager armorSlotManager;
    
    public PlayerJoinListener(ArmorSlotManager armorSlotManager) {
        this.armorSlotManager = armorSlotManager;
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        // Initialize player if they're new
        armorSlotManager.initializePlayer(player);
        
        // Check for pending revival notifications
        String reviverName = armorSlotManager.getPendingRevivalNotification(player.getUniqueId());
        if (reviverName != null) {
            player.sendMessage(ChatColor.GREEN + "You have been revived by " + reviverName + "!");
            player.sendMessage(ChatColor.YELLOW + "You start with 3 armor slots (missing helmet).");
            // Broadcast revival message
            Bukkit.broadcastMessage(ChatColor.GOLD + player.getName() + ChatColor.YELLOW + " has been revived!");
        }
        
        // Remove any disallowed armor after a short delay
        Bukkit.getScheduler().runTaskLater(Bukkit.getPluginManager().getPlugin("ArmorSlotPlugin"), () -> {
            armorSlotManager.removeDisallowedArmor(player);
        }, 20L); // 1 second delay
    }
}