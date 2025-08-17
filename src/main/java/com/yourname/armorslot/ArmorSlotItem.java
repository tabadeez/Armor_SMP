package com.yourname.armorslot;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;
import java.util.Arrays;

public class ArmorSlotItem {
    
    public static final String ARMOR_SLOT_KEY = "armor_slot_item";
    
    public static ItemStack createArmorSlotItem() {
        ItemStack item = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "Armor Slot");
            meta.setLore(Arrays.asList(
                ChatColor.GRAY + "Right-click to restore a missing armor slot",
                ChatColor.GRAY + "Can only be used if you have missing slots"
            ));
            
            // Add persistent data to identify this as an armor slot item
            NamespacedKey key = new NamespacedKey("armorslotplugin", ARMOR_SLOT_KEY);
            meta.getPersistentDataContainer().set(key, PersistentDataType.BOOLEAN, true);
            
            item.setItemMeta(meta);
        }
        
        return item;
    }
    
    public static boolean isArmorSlotItem(ItemStack item) {
        if (item == null || item.getType() != Material.NETHER_STAR) {
            return false;
        }
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return false;
        }
        
        NamespacedKey key = new NamespacedKey("armorslotplugin", ARMOR_SLOT_KEY);
        return meta.getPersistentDataContainer().has(key, PersistentDataType.BOOLEAN);
    }
    
    public static void dropArmorSlotItem(Location location) {
        ItemStack armorSlotItem = createArmorSlotItem();
        location.getWorld().dropItemNaturally(location, armorSlotItem);
    }
}