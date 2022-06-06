package at.gotzi.dungeonmanager.manager.file;

import at.gotzi.dungeonmanager.Main;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

public class OresManager extends FileManager {

    private static final YamlConfiguration oresConfig = YamlConfiguration.loadConfiguration(new File("plugins//DungeonManager//ores.yml"));

    private static int period;

    private static HashMap<Material, Integer> ores = new HashMap<>();

    private static HashMap<UUID, Ores> oresHashMap = new HashMap<>();

    public OresManager() {
        super(oresConfig);
    }

    @Override
    public void initialize() {
        period = this.getInt("period");

        for (String s : oresConfig.getConfigurationSection("ores").getKeys(false)) {
            ores.put(Material.getMaterial(s), this.getInt("ores." + s));
        }
    }

    public static boolean isMaterial(Material material) {
        return ores.containsKey(material);
    }

    public static void createNew(UUID uuid) {
        oresHashMap.put(uuid, new Ores());
        startTimer(uuid);
    }

    public static boolean hasLeft(UUID uuid, Material material) {
        return !(oresHashMap.get(uuid).getCount(material) >= ores.get(material));
    }

    public static void addBreak(UUID uuid, Material material) {
        oresHashMap.put(uuid, oresHashMap.get(uuid).addBreak(material));
    }

    public static boolean isEmpty(UUID uuid) {
        return !oresHashMap.containsKey(uuid);
    }

    public static void startTimer(UUID uuid) {
        new BukkitRunnable() {
            @Override
            public void run() {
                oresHashMap.remove(uuid);
            }
        }.runTaskLaterAsynchronously(Main.getInstance(), period);
    }

    private static class Ores {

        private HashMap<Material, Integer> oreCount = new HashMap<>();

        public Ores() {
            for (Material material : ores.keySet()) {
                oreCount.put(material, 0);
            }
        }

        private Ores addBreak(Material material) {
            oreCount.put(material, oreCount.get(material)+1);
            return this;
        }

        private int getCount(Material material) {
            return oreCount.get(material);
        }
    }


}
