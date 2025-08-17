package com.yourname.armorslot;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import org.bukkit.Bukkit;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ArmorSlotCommand implements CommandExecutor {
    
    private final ArmorSlotManager armorSlotManager;
    private final Map<UUID, Long> pendingResets = new HashMap<>();
    private static final long CONFIRMATION_TIMEOUT = 30000; // 30 seconds
    
    public ArmorSlotCommand(ArmorSlotManager armorSlotManager) {
        this.armorSlotManager = armorSlotManager;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("armorslot.admin")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
            return true;
        }
        
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /armorslots reset [confirm]");
            return true;
        }
        
        if (args[0].equalsIgnoreCase("reset")) {
            if (args.length == 1) {
                // First reset command - ask for confirmation
                if (sender instanceof Player) {
                    UUID senderUuid = ((Player) sender).getUniqueId();
                    pendingResets.put(senderUuid, System.currentTimeMillis());
                }
                sender.sendMessage(ChatColor.YELLOW + "Are you sure you want to reset everyone back to 4 armor slots?");
                sender.sendMessage(ChatColor.YELLOW + "Type " + ChatColor.WHITE + "/armorslots reset confirm" + ChatColor.YELLOW + " to confirm.");
                sender.sendMessage(ChatColor.GRAY + "This confirmation expires in 30 seconds.");
                return true;
            } else if (args.length == 2 && args[1].equalsIgnoreCase("confirm")) {
                // Check if there's a pending reset
                boolean canReset = false;
                if (sender instanceof Player) {
                    UUID senderUuid = ((Player) sender).getUniqueId();
                    Long resetTime = pendingResets.get(senderUuid);
                    if (resetTime != null && (System.currentTimeMillis() - resetTime) <= CONFIRMATION_TIMEOUT) {
                        canReset = true;
                        pendingResets.remove(senderUuid);
                    }
                } else {
                    // Console can always reset without confirmation timeout
                    canReset = true;
                }
                
                if (!canReset) {
                    sender.sendMessage(ChatColor.RED + "No pending reset found or confirmation expired. Use /armorslots reset first.");
                    return true;
                }
                
                // Perform the reset
                armorSlotManager.resetAllPlayers();
                
                // Remove disallowed armor from all online players
                for (Player player : Bukkit.getOnlinePlayers()) {
                    armorSlotManager.removeDisallowedArmor(player);
                }
                
                sender.sendMessage(ChatColor.GREEN + "All players' armor slots have been reset to default!");
                Bukkit.broadcastMessage(ChatColor.YELLOW + "All armor slots have been reset by an administrator!");
                return true;
            } else {
                sender.sendMessage(ChatColor.RED + "Usage: /armorslots reset [confirm]");
                return true;
            }
        }
        
        sender.sendMessage(ChatColor.RED + "Usage: /armorslots reset [confirm]");
        return true;
    }
}