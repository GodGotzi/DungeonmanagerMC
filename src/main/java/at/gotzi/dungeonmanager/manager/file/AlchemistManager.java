package at.gotzi.dungeonmanager.manager.file;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class AlchemistManager extends FileManager {

    private static YamlConfiguration aliConfig;

    public static String aliNpc;

    public AlchemistManager() {
        super(YamlConfiguration.loadConfiguration(new File("plugins//DungeonManager//alchemist.yml")));
        aliConfig = YamlConfiguration.loadConfiguration(new File("plugins//DungeonManager//alchemist.yml"));
    }

    @Override
    public void initialize() {
        aliNpc = this.getString("npc");
    }
}
