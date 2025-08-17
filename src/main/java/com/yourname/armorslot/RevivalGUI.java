package com.yourname.armorslot;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.ChatColor;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class RevivalGUI {
    
    private final ArmorSlotManager armorSlotManager;
    
    public RevivalGUI(ArmorSlotManager armorSlotManager) {
        this.armorSlotManager = armorSlotManager;
    }
    
    public void openRevivalMenu(Player player) {
        List<UUID> eliminatedPlayers = armorSlotManager.getEliminatedPlayers();
        
        if (eliminatedPlayers.isEmpty()) {
            player.sendMessage(ChatColor.RED + "There are no eliminated players to revive!");
            return;
        }
        
        int size = Math.min(54, ((eliminatedPlayers.size() + 8) / 9) * 9); // Round up to nearest 9
        Inventory gui = Bukkit.createInventory(null, size, ChatColor.DARK_RED + "Select Player to Revive");
        
        for (int i = 0; i < eliminatedPlayers.size() && i < 54; i++) {
            UUID eliminatedUuid = eliminatedPlayers.get(i);
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(eliminatedUuid);
            
            ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
            
            if (skullMeta != null) {
                skullMeta.setOwningPlayer(offlinePlayer);
                skullMeta.setDisplayName(ChatColor.RED + offlinePlayer.getName());
                skullMeta.setLore(Arrays.asList(
                    ChatColor.GRAY + "Click to revive this player",
                    ChatColor.GRAY + "They will start with 3 armor slots",
                    ChatColor.YELLOW + "UUID: " + eliminatedUuid.toString()
                ));
                skull.setItemMeta(skullMeta);
            }
            
            gui.setItem(i, skull);
        }
        
        player.openInventory(gui);
    }
}