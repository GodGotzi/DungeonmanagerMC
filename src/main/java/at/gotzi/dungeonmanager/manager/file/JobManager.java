package at.gotzi.dungeonmanager.manager.file;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class JobManager extends FileManager {

    private static YamlConfiguration jobsConfig;

    public static String jobNpc;

    public JobManager() {
        super(YamlConfiguration.loadConfiguration(new File("plugins//DungeonManager//jobs.yml")));
        jobsConfig = YamlConfiguration.loadConfiguration(new File("plugins//DungeonManager//jobs.yml"));
    }

    @Override
    public void initialize() {
        jobNpc = getString("npc");
    }

}
