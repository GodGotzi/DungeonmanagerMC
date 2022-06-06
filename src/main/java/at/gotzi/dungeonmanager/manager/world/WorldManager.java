// This class was created by Wireless


package at.gotzi.dungeonmanager.manager.world;

import at.gotzi.dungeonmanager.Main;
import io.netty.buffer.UnpooledUnsafeDirectByteBuf;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.configuration.file.YamlConfiguration;
import org.terraform.coregen.bukkit.TerraformGenerator;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class WorldManager {

    private Main main;

    public WorldManager(Main main) {
        this.main = main;
    }

    private File file = new File("plugins//DungeonManager//worlds.yml");
    private YamlConfiguration worldconfig = YamlConfiguration.loadConfiguration(file);

    public void loadWorlds() {
        Set<String> worlds = worldconfig.getConfigurationSection("worlds").getKeys(false);

        for (String world : worlds) {
            File file = new File(world);
            if(file.exists()) {
                String environment = worldconfig.getString("worlds." + world + ".environment");
                String worldTypeString = worldconfig.getString("worlds." + world + ".worldType");
                String generator = worldconfig.getString("worlds." + world + ".generator");
                if (environment == null) {
                    main.print("§3Environment in WorldConfig " + world + " does not exist!");
                    return;
                }

                World.Environment env = null;
                if (environment.equalsIgnoreCase("NORMAL")) {
                    env = World.Environment.NORMAL;
                } else if (environment.equalsIgnoreCase("NETHER")) {
                    env = World.Environment.NETHER;
                } else if (environment.equalsIgnoreCase("THE_END")) {
                    env = World.Environment.THE_END;
                } else {
                    main.print("§3 Environment in WorldConfig " + world + " does not exist!");
                    return;
                }

                WorldType worldType = null;

                if (env == World.Environment.NORMAL) {
                    if (worldTypeString != null) {
                        if (worldTypeString.equalsIgnoreCase("flat")) {
                            worldType = WorldType.FLAT;
                        } else if (worldTypeString.equalsIgnoreCase("large_biomes")) {
                            worldType = WorldType.LARGE_BIOMES;
                        } else if (worldTypeString.equalsIgnoreCase("amplified")) {
                            worldType = WorldType.AMPLIFIED;
                        } else {
                            main.print("§cOnly available with environment normal!");
                            return;
                        }
                    }
                }

                if (generator != null) {
                    if (generator.equalsIgnoreCase("TerraformGenerator")) {
                        if (!environment.equalsIgnoreCase("normal")) {
                            main.print("§cUse TerraformGenerator only with Environment \"normal\"!");
                            return;
                        }
                        TerraformGenerator terraformGenerator = new TerraformGenerator();
                        new WorldCreator(world).environment(World.Environment.NORMAL).generator("TerraformGenerator").createWorld();
                    }
                }

                if (worldType == null) {
                    new WorldCreator(world).environment(env).createWorld();
                } else {
                    new WorldCreator(world).environment(env).type(worldType).createWorld();
                }
            } else
                main.printErr("Remove " + world + " from Config couldn't find the world folder!");
        }
    }
}
