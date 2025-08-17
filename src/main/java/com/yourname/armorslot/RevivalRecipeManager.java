package com.yourname.armorslot;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;

public class RevivalRecipeManager {
    
    public static void registerRevivalRecipe(ArmorSlotPlugin plugin) {
        // Create a revival recipe that consumes armor slot items
        NamespacedKey key = new NamespacedKey(plugin, "revival");
        
        // This recipe doesn't produce an item, it's handled by the crafting listener
        ItemStack result = RevivalStar.createRevivalStar();
        ShapedRecipe recipe = new ShapedRecipe(key, result);
        
        recipe.shape("#N#", "NON", "#N#");
        
        // Use exact item match for armor slot items
        recipe.setIngredient('N', Material.NETHERITE_INGOT);
        recipe.setIngredient('O', Material.OMINOUS_TRIAL_KEY);
        recipe.setIngredient('#', new RecipeChoice.ExactChoice(ArmorSlotItem.createArmorSlotItem()));
        
        Bukkit.addRecipe(recipe);
        
        plugin.getLogger().info("Registered revival recipe!");
    }
}