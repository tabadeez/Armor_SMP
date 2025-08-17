package com.yourname.armorslot;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;
import java.util.Arrays;

public class RevivalStar {
    
    public static final String REVIVAL_STAR_KEY = "revival_star_item";
    
    public static ItemStack createRevivalStar() {
        ItemStack item = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "Revival Star");
            meta.setLore(Arrays.asList(
                ChatColor.GRAY + "Right-click to revive an eliminated player",
                ChatColor.GRAY + "Opens a menu to select who to revive"
            ));
            
            // Add persistent data to identify this as a revival star
            NamespacedKey key = new NamespacedKey("armorslotplugin", REVIVAL_STAR_KEY);
            meta.getPersistentDataContainer().set(key, PersistentDataType.BOOLEAN, true);
            
            item.setItemMeta(meta);
        }
        
        return item;
    }
    
    public static boolean isRevivalStar(ItemStack item) {
        if (item == null || item.getType() != Material.NETHER_STAR) {
            return false;
        }
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return false;
        }
        
        NamespacedKey key = new NamespacedKey("armorslotplugin", REVIVAL_STAR_KEY);
        return meta.getPersistentDataContainer().has(key, PersistentDataType.BOOLEAN);
    }
}