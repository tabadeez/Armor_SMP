package com.yourname.armorslot;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import java.util.UUID;

public class RevivalGUIListener implements Listener {
    
    private final ArmorSlotManager armorSlotManager;
    private final RevivalGUI revivalGUI;
    
    public RevivalGUIListener(ArmorSlotManager armorSlotManager) {
        this.armorSlotManager = armorSlotManager;
        this.revivalGUI = new RevivalGUI(armorSlotManager);
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        
        if (!RevivalStar.isRevivalStar(item)) {
            return;
        }
        
        event.setCancelled(true);
        revivalGUI.openRevivalMenu(player);
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(ChatColor.DARK_RED + "Select Player to Revive")) {
            return;
        }
        
        event.setCancelled(true);
        
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        
        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();
        
        if (clickedItem == null || clickedItem.getType() != org.bukkit.Material.PLAYER_HEAD) {
            return;
        }
        
        SkullMeta skullMeta = (SkullMeta) clickedItem.getItemMeta();
        if (skullMeta == null || skullMeta.getOwningPlayer() == null) {
            return;
        }
        
        OfflinePlayer targetPlayer = skullMeta.getOwningPlayer();
        UUID targetUuid = targetPlayer.getUniqueId();
        
        // Check if player still has revival star
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        ItemStack offHand = player.getInventory().getItemInOffHand();
        
        if (RevivalStar.isRevivalStar(mainHand)) {
            if (mainHand.getAmount() > 1) {
                mainHand.setAmount(mainHand.getAmount() - 1);
            } else {
                player.getInventory().setItemInMainHand(null);
            }
        } else if (RevivalStar.isRevivalStar(offHand)) {
            if (offHand.getAmount() > 1) {
                offHand.setAmount(offHand.getAmount() - 1);
            } else {
                player.getInventory().setItemInOffHand(null);
            }
        } else {
            player.sendMessage(ChatColor.RED + "You need a Revival Star to revive players!");
            player.closeInventory();
            return;
        }
        
        // Revive the player (give them 3 armor slots - missing helmet)
        armorSlotManager.revivePlayer(targetUuid);
        
        player.closeInventory();
        player.sendMessage(ChatColor.GREEN + "You have revived " + targetPlayer.getName() + "!");
        
        // Check if target player is online to notify them
        Player onlineTarget = Bukkit.getPlayer(targetUuid);
        if (onlineTarget != null) {
            onlineTarget.sendMessage(ChatColor.GREEN + "You have been revived by " + player.getName() + "!");
            onlineTarget.sendMessage(ChatColor.YELLOW + "You start with 3 armor slots (missing helmet).");
            // Broadcast revival message
            Bukkit.broadcastMessage(ChatColor.GOLD + targetPlayer.getName() + ChatColor.YELLOW + " has been revived!");
        } else {
            // Store revival notification for when they log in
            armorSlotManager.setPendingRevivalNotification(targetUuid, player.getName());
        }
    }
}