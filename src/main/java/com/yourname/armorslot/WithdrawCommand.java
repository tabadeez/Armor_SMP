package com.yourname.armorslot;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.ChatColor;
import org.bukkit.Bukkit;

public class WithdrawCommand implements CommandExecutor {
    
    private final ArmorSlotManager armorSlotManager;
    
    public WithdrawCommand(ArmorSlotManager armorSlotManager) {
        this.armorSlotManager = armorSlotManager;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
            return true;
        }
        
        Player player = (Player) sender;
        
        // Check if player has no armor slots
        if (armorSlotManager.hasNoArmorSlots(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "You cannot withdraw armor slots when you have none remaining!");
            return true;
        }
        
        if (args.length != 1) {
            player.sendMessage(ChatColor.RED + "Usage: /withdraw <amount>");
            return true;
        }
        
        int amount;
        try {
            amount = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + "Please enter a valid number.");
            return true;
        }
        
        if (amount <= 0) {
            player.sendMessage(ChatColor.RED + "Amount must be greater than 0.");
            return true;
        }
        
        int currentSlots = armorSlotManager.getArmorSlotCount(player.getUniqueId());
        
        if (amount > currentSlots) {
            player.sendMessage(ChatColor.RED + "You don't have enough armor slots! You have " + currentSlots + " slots.");
            return true;
        }
        
        // Remove armor slots from player
        armorSlotManager.removeArmorSlots(player.getUniqueId(), amount);
        
        // Give armor slot items
        ItemStack armorSlotItem = ArmorSlotItem.createArmorSlotItem();
        armorSlotItem.setAmount(amount);
        
        // Try to add to inventory, drop if full
        if (player.getInventory().firstEmpty() != -1) {
            player.getInventory().addItem(armorSlotItem);
        } else {
            player.getWorld().dropItemNaturally(player.getLocation(), armorSlotItem);
            player.sendMessage(ChatColor.YELLOW + "Your inventory was full, so the items were dropped at your feet.");
        }
        
        // Remove any disallowed armor after withdrawing slots
        Bukkit.getScheduler().runTask(Bukkit.getPluginManager().getPlugin("ArmorSlotPlugin"), () -> {
            armorSlotManager.removeDisallowedArmor(player);
        });
        
        player.sendMessage(ChatColor.GREEN + "You have withdrawn " + amount + " armor slot" + (amount == 1 ? "" : "s") + "!");
        player.sendMessage(ChatColor.GRAY + "You now have " + (currentSlots - amount) + " armor slot" + ((currentSlots - amount) == 1 ? "" : "s") + " remaining.");
        
        return true;
    }
}