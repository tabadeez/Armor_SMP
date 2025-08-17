package com.yourname.armorslot;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.ChatColor;
import java.util.*;

public class ArmorSlotManager {
    
    private final ArmorSlotPlugin plugin;
    private final Map<UUID, Set<ArmorSlot>> playerArmorSlots;
    private final Map<UUID, String> pendingRevivalNotifications;
    
    public enum ArmorSlot {
        HELMET(39),
        CHESTPLATE(38),
        LEGGINGS(37),
        BOOTS(36);
        
        private final int slot;
        
        ArmorSlot(int slot) {
            this.slot = slot;
        }
        
        public int getSlot() {
            return slot;
        }
    }
    
    public ArmorSlotManager(ArmorSlotPlugin plugin) {
        this.plugin = plugin;
        this.playerArmorSlots = new HashMap<>();
        this.pendingRevivalNotifications = new HashMap<>();
        loadAllData();
    }
    
    public void initializePlayer(Player player) {
        UUID uuid = player.getUniqueId();
        if (!playerArmorSlots.containsKey(uuid)) {
            // All players start with all 4 armor slots
            Set<ArmorSlot> slots = EnumSet.allOf(ArmorSlot.class);
            playerArmorSlots.put(uuid, slots);
            savePlayerData(uuid);
        }
    }
    
    public Set<ArmorSlot> getPlayerArmorSlots(UUID uuid) {
        return playerArmorSlots.getOrDefault(uuid, EnumSet.allOf(ArmorSlot.class));
    }
    
    public boolean hasArmorSlot(UUID uuid, ArmorSlot slot) {
        return getPlayerArmorSlots(uuid).contains(slot);
    }
    
    public void removeArmorSlot(UUID uuid) {
        Set<ArmorSlot> slots = getPlayerArmorSlots(uuid);
        
        // Remove slots in order: HELMET -> CHESTPLATE -> LEGGINGS -> BOOTS
        if (slots.contains(ArmorSlot.HELMET)) {
            slots.remove(ArmorSlot.HELMET);
        } else if (slots.contains(ArmorSlot.CHESTPLATE)) {
            slots.remove(ArmorSlot.CHESTPLATE);
        } else if (slots.contains(ArmorSlot.LEGGINGS)) {
            slots.remove(ArmorSlot.LEGGINGS);
        } else if (slots.contains(ArmorSlot.BOOTS)) {
            slots.remove(ArmorSlot.BOOTS);
        }
        
        playerArmorSlots.put(uuid, slots);
        savePlayerData(uuid);
    }
    
    public void addArmorSlot(UUID uuid) {
        Set<ArmorSlot> slots = getPlayerArmorSlots(uuid);
        
        // Add slots in reverse order: BOOTS -> LEGGINGS -> CHESTPLATE -> HELMET
        if (!slots.contains(ArmorSlot.BOOTS)) {
            slots.add(ArmorSlot.BOOTS);
        } else if (!slots.contains(ArmorSlot.LEGGINGS)) {
            slots.add(ArmorSlot.LEGGINGS);
        } else if (!slots.contains(ArmorSlot.CHESTPLATE)) {
            slots.add(ArmorSlot.CHESTPLATE);
        } else if (!slots.contains(ArmorSlot.HELMET)) {
            slots.add(ArmorSlot.HELMET);
        }
        
        playerArmorSlots.put(uuid, slots);
        savePlayerData(uuid);
    }
    
    public boolean hasAllArmorSlots(UUID uuid) {
        Set<ArmorSlot> slots = getPlayerArmorSlots(uuid);
        return slots.size() == 4;
    }
    
    public boolean hasNoArmorSlots(UUID uuid) {
        Set<ArmorSlot> slots = getPlayerArmorSlots(uuid);
        return slots.isEmpty();
    }
    
    public List<UUID> getEliminatedPlayers() {
        List<UUID> eliminated = new ArrayList<>();
        for (Map.Entry<UUID, Set<ArmorSlot>> entry : playerArmorSlots.entrySet()) {
            if (entry.getValue().isEmpty()) {
                eliminated.add(entry.getKey());
            }
        }
        return eliminated;
    }
    
    public void revivePlayer(UUID uuid) {
        // Give player 3 armor slots (missing helmet)
        Set<ArmorSlot> slots = EnumSet.of(ArmorSlot.CHESTPLATE, ArmorSlot.LEGGINGS, ArmorSlot.BOOTS);
        playerArmorSlots.put(uuid, slots);
        savePlayerData(uuid);
    }
    
    public void setPendingRevivalNotification(UUID uuid, String reviverName) {
        pendingRevivalNotifications.put(uuid, reviverName);
    }
    
    public String getPendingRevivalNotification(UUID uuid) {
        return pendingRevivalNotifications.remove(uuid);
    }
    
    public boolean hasOnlyBootsLeft(UUID uuid) {
        Set<ArmorSlot> slots = getPlayerArmorSlots(uuid);
        return slots.size() == 1 && slots.contains(ArmorSlot.BOOTS);
    }
    
    public int getArmorSlotCount(UUID uuid) {
        return getPlayerArmorSlots(uuid).size();
    }
    
    public void removeArmorSlots(UUID uuid, int amount) {
        Set<ArmorSlot> slots = getPlayerArmorSlots(uuid);
        
        // Remove slots in order: HELMET -> CHESTPLATE -> LEGGINGS -> BOOTS
        for (int i = 0; i < amount && !slots.isEmpty(); i++) {
            if (slots.contains(ArmorSlot.HELMET)) {
                slots.remove(ArmorSlot.HELMET);
            } else if (slots.contains(ArmorSlot.CHESTPLATE)) {
                slots.remove(ArmorSlot.CHESTPLATE);
            } else if (slots.contains(ArmorSlot.LEGGINGS)) {
                slots.remove(ArmorSlot.LEGGINGS);
            } else if (slots.contains(ArmorSlot.BOOTS)) {
                slots.remove(ArmorSlot.BOOTS);
            }
        }
        
        playerArmorSlots.put(uuid, slots);
        savePlayerData(uuid);
    }
    
    public void removeDisallowedArmor(Player player) {
        PlayerInventory inv = player.getInventory();
        UUID uuid = player.getUniqueId();
        
        for (ArmorSlot armorSlot : ArmorSlot.values()) {
            if (!hasArmorSlot(uuid, armorSlot)) {
                ItemStack item = inv.getItem(armorSlot.getSlot());
                if (item != null && !item.getType().isAir()) {
                    inv.setItem(armorSlot.getSlot(), null);
                    // Drop the item at player's location
                    player.getWorld().dropItemNaturally(player.getLocation(), item);
                }
            }
        }
    }
    
    public void resetAllPlayers() {
        playerArmorSlots.clear();
        plugin.getDataConfig().set("players", null);
        plugin.saveDataConfig();
    }
    
    public void resetPlayer(UUID uuid) {
        Set<ArmorSlot> slots = EnumSet.allOf(ArmorSlot.class);
        playerArmorSlots.put(uuid, slots);
        savePlayerData(uuid);
    }
    
    private void savePlayerData(UUID uuid) {
        Set<ArmorSlot> slots = getPlayerArmorSlots(uuid);
        List<String> slotNames = new ArrayList<>();
        for (ArmorSlot slot : slots) {
            slotNames.add(slot.name());
        }
        plugin.getDataConfig().set("players." + uuid.toString(), slotNames);
        plugin.saveDataConfig();
    }
    
    public void saveAllData() {
        for (UUID uuid : playerArmorSlots.keySet()) {
            savePlayerData(uuid);
        }
    }
    
    private void loadAllData() {
        if (plugin.getDataConfig().getConfigurationSection("players") == null) {
            return;
        }
        
        for (String uuidString : plugin.getDataConfig().getConfigurationSection("players").getKeys(false)) {
            try {
                UUID uuid = UUID.fromString(uuidString);
                List<String> slotNames = plugin.getDataConfig().getStringList("players." + uuidString);
                Set<ArmorSlot> slots = EnumSet.noneOf(ArmorSlot.class);
                
                for (String slotName : slotNames) {
                    try {
                        slots.add(ArmorSlot.valueOf(slotName));
                    } catch (IllegalArgumentException e) {
                        plugin.getLogger().warning("Invalid armor slot name: " + slotName);
                    }
                }
                
                playerArmorSlots.put(uuid, slots);
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Invalid UUID in data file: " + uuidString);
            }
        }
    }
}