package com.yourname.armorslot;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;

public class ArmorSlotPlugin extends JavaPlugin {
    
    private ArmorSlotManager armorSlotManager;
    private File dataFile;
    private FileConfiguration dataConfig;
    
    @Override
    public void onEnable() {
        // Create data folder if it doesn't exist
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }
        
        // Initialize data file
        dataFile = new File(getDataFolder(), "playerdata.yml");
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                getLogger().severe("Could not create playerdata.yml file!");
                e.printStackTrace();
            }
        }
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
        
        // Initialize managers
        armorSlotManager = new ArmorSlotManager(this);
        
        // Register events
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(armorSlotManager), this);
        getServer().getPluginManager().registerEvents(new ArmorEquipListener(armorSlotManager), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(armorSlotManager), this);
        getServer().getPluginManager().registerEvents(new ArmorSlotItemListener(armorSlotManager), this);
        getServer().getPluginManager().registerEvents(new RevivalCraftListener(armorSlotManager), this);
        getServer().getPluginManager().registerEvents(new RevivalGUIListener(armorSlotManager), this);
        
        // Register commands
        getCommand("armorslots").setExecutor(new ArmorSlotCommand(armorSlotManager));
        getCommand("withdraw").setExecutor(new WithdrawCommand(armorSlotManager));
        
        // Register recipes
        RecipeManager.registerRecipes(this);
        RevivalRecipeManager.registerRevivalRecipe(this);
        
        getLogger().info("ArmorSlotPlugin has been enabled!");
    }
    
    @Override
    public void onDisable() {
        if (armorSlotManager != null) {
            armorSlotManager.saveAllData();
        }
        getLogger().info("ArmorSlotPlugin has been disabled!");
    }
    
    public FileConfiguration getDataConfig() {
        return dataConfig;
    }
    
    public void saveDataConfig() {
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            getLogger().severe("Could not save playerdata.yml!");
            e.printStackTrace();
        }
    }
    
    public void reloadDataConfig() {
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
    }
}