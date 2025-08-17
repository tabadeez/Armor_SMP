# How to Build and Download Your Minecraft Armor Slot Plugin

## Step 1: Install Java Development Kit (JDK)

### Windows:
1. Go to https://adoptium.net/
2. Download "Temurin 21 (LTS)" for Windows
3. Run the installer and follow the setup wizard
4. Make sure to check "Set JAVA_HOME variable" during installation

### Mac:
1. Go to https://adoptium.net/
2. Download "Temurin 21 (LTS)" for macOS
3. Run the .pkg installer
4. Follow the installation prompts

### Linux:
```bash
sudo apt update
sudo apt install openjdk-21-jdk
```

## Step 2: Install Apache Maven

### Windows:
1. Go to https://maven.apache.org/download.cgi
2. Download "Binary zip archive" (apache-maven-3.9.x-bin.zip)
3. Extract it to `C:\Program Files\Apache\maven`
4. Add Maven to your PATH:
   - Press Windows + R, type `sysdm.cpl`
   - Click "Environment Variables"
   - Under "System Variables", find "Path" and click "Edit"
   - Click "New" and add: `C:\Program Files\Apache\maven\bin`
   - Click OK on all windows

### Mac:
```bash
# Install Homebrew if you don't have it
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"

# Install Maven
brew install maven
```

### Linux:
```bash
sudo apt update
sudo apt install maven
```

## Step 3: Verify Installation

Open a terminal/command prompt and run:
```bash
java -version
mvn -version
```

You should see version information for both commands.

## Step 4: Download the Project Files

1. Create a new folder on your desktop called `armor-slot-plugin`
2. Download all the project files from this conversation
3. Make sure your folder structure looks like this:

```
armor-slot-plugin/
├── pom.xml
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── yourname/
│       │           └── armorslot/
│       │               ├── ArmorSlotPlugin.java
│       │               ├── ArmorSlotManager.java
│       │               ├── PlayerDeathListener.java
│       │               ├── ArmorEquipListener.java
│       │               ├── PlayerJoinListener.java
│       │               ├── ArmorSlotItem.java
│       │               ├── ArmorSlotItemListener.java
│       │               ├── RecipeManager.java
│       │               ├── ArmorSlotCommand.java
│       │               ├── WithdrawCommand.java
│       │               ├── RevivalStar.java
│       │               ├── RevivalGUI.java
│       │               ├── RevivalGUIListener.java
│       │               ├── RevivalRecipeManager.java
│       │               └── RevivalCraftListener.java
│       └── resources/
│           ├── plugin.yml
│           └── data/
│               └── minecraft/
│                   └── recipe/
│                       ├── armor_slot.json
│                       └── revival.json
```

## Step 5: Build the Plugin

1. Open terminal/command prompt
2. Navigate to your project folder:
   ```bash
   cd Desktop/armor-slot-plugin
   ```
3. Build the plugin:
   ```bash
   mvn clean package
   ```

## Step 6: Get Your Plugin JAR

After the build completes successfully:
1. Look in the `target/` folder
2. You'll find `armor-slot-plugin-1.0.0.jar`
3. This is your plugin file!

## Step 7: Install on Your Minecraft Server

1. Stop your Minecraft server
2. Copy `armor-slot-plugin-1.0.0.jar` to your server's `plugins/` folder
3. Start your server
4. The plugin will automatically create its data files

## Troubleshooting

### "Command not found" errors:
- Make sure Java and Maven are properly installed
- Restart your terminal after installation
- Check that PATH variables are set correctly

### Build errors:
- Make sure all files are in the correct folders
- Check that you have Java 21 installed
- Verify your internet connection (Maven downloads dependencies)

### Plugin not loading:
- Check your server is running Paper/Spigot 1.21.1
- Look at server console for error messages
- Make sure the JAR file isn't corrupted

## Commands Available After Installation:
- `/armorslots reset` - Shows reset confirmation
- `/armorslots reset confirm` - Resets all players to 4 armor slots
- `/withdraw <amount>` - Withdraw armor slot items

## Need Help?
If you run into any issues, let me know exactly what error message you're seeing and at which step you're stuck!