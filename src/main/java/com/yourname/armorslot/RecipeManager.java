package com.yourname.armorslot;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ItemStack;

public class RecipeManager {
    
    public static void registerRecipes(ArmorSlotPlugin plugin) {
        // Create the armor slot item recipe
        ItemStack armorSlotItem = ArmorSlotItem.createArmorSlotItem();
        NamespacedKey key = new NamespacedKey(plugin, "armor_slot");
        
        ShapedRecipe recipe = new ShapedRecipe(key, armorSlotItem);
        recipe.shape(" N ", "NON", " N ");
        recipe.setIngredient('N', Material.NETHERITE_INGOT);
        recipe.setIngredient('O', Material.OMINOUS_TRIAL_KEY);
        
        Bukkit.addRecipe(recipe);
        
        plugin.getLogger().info("Registered armor slot recipe!");
    }
}