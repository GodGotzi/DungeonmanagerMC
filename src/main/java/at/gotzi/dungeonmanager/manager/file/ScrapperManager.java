package at.gotzi.dungeonmanager.manager.file;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ScrapperManager extends FileManager {

    private static YamlConfiguration scrapperConfig;

    public static String scrapperNpc;
    public static int percent;

    public ScrapperManager() {
        super(YamlConfiguration.loadConfiguration(new File("plugins//DungeonManager//scrapper.yml")));
        scrapperConfig = YamlConfiguration.loadConfiguration(new File("plugins//DungeonManager//scrapper.yml"));
    }

    @Override
    public void initialize() {
        scrapperNpc = getString("npc");
        percent = getInt("percent");
    }
}
