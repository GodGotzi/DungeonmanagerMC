package at.gotzi.dungeonmanager.manager.file;

import at.gotzi.dungeonmanager.utils.Utils;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class ChunkManager extends FileManager {

    private static final YamlConfiguration chunkConfig = YamlConfiguration.loadConfiguration(new File("plugins//DungeonManager//chunk.yml"));

    private static int redStone;
    private static int armorStand;
    private static int itemFrame;
    private static int spawner;

    public ChunkManager() {
        super(chunkConfig);
    }

    @Override
    public void initialize() {
        redStone = chunkConfig.getInt("redStone");
        armorStand = chunkConfig.getInt("armorStand");
        itemFrame = chunkConfig.getInt("itemFrame");
        spawner = chunkConfig.getInt("spawner");
    }

    public static boolean isBlocked(Material material) {
        return material == Material.REDSTONE || material == Material.ITEM_FRAME || material == Material.ARMOR_STAND || material == Material.SPAWNER || material == Material.GLOW_ITEM_FRAME;
    }

    public static boolean hasLeft(@NotNull final Material material, @NotNull final Chunk chunk) {
        if (material == Material.REDSTONE) {
            return getMaterialAmount(chunk, Material.REDSTONE_WIRE) < redStone;
        } else if (material == Material.ITEM_FRAME || material == Material.GLOW_ITEM_FRAME) {
            int amount = 0;
            amount += getEntitiesAmount(chunk, EntityType.GLOW_ITEM_FRAME);
            amount += getEntitiesAmount(chunk, EntityType.ITEM_FRAME);
            return amount < itemFrame;
        } else if (material == Material.ARMOR_STAND) {
            return getEntitiesAmount(chunk, EntityType.ARMOR_STAND) < armorStand;
        } else if (material == Material.SPAWNER) {
            return (getMaterialAmount(chunk, Material.SPAWNER)-1) < spawner;
        }
        return true;
    }

    public static int getEntitiesAmount(final Chunk chunk, final EntityType entityType) {
        int amount = 0;
        for (Entity e : chunk.getEntities() ) {
            if (e.getType() == entityType)
                amount++;
        }
        return amount;
    }

    public static int getMaterialAmount(final Chunk chunk, final Material material) {
        int amount = 0;
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 256; y++) {
                for (int z = 0; z < 16; z++) {
                    if (chunk.getBlock(x, y, z).getType() == material)
                        amount++;
                }
            }
        }
        return amount;
    }
}
