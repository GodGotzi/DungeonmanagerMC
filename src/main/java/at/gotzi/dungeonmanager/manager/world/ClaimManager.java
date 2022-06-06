// This class was created by Wireless


package at.gotzi.dungeonmanager.manager.world;

import at.gotzi.dungeonmanager.data.PlayerData;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class ClaimManager {

    private final Player player;

    public static int spawnRange = 0;

    public ClaimManager(Player player) {
        this.player = player;
    }

    private static final File file = new File("plugins//DungeonManager//claims.yml");
    private static final YamlConfiguration claimConfig = YamlConfiguration.loadConfiguration(file);

    public static void initialize() {
        spawnRange = claimConfig.getInt("spawnRange");
    }

    public UUID getClaimOwner() {
        for(UUID uuid : PlayerData.claimsByUUID.keySet()) {
            if(PlayerData.claimsByUUID.get(uuid).contains(player.getChunk().getChunkKey())) {
                return uuid;
            }
        }
        return null;
    }

    public UUID getClaimOwnerByChunk(long chunkID) {
        for(UUID uuid : PlayerData.claimsByUUID.keySet()) {
            if(PlayerData.claimsByUUID.get(uuid).contains(chunkID)) {
                return uuid;
            }
        }
        return null;
    }

    public boolean kick(Player player) {
        List<Long> claims = PlayerData.getClaimsRAM(this.player.getUniqueId());
        return claims.contains(player.getChunk().getChunkKey());
    }

    public static int getPeriod() {
        return claimConfig.getInt("period");
    }

    public static int getMaxPlots() {
        return claimConfig.getInt("maxClaims");
    }

    public void claim(long chunkID) {
        PlayerData.addClaim(player.getUniqueId(), chunkID);
    }

    public void unClaim(long chunkID) {
        PlayerData.removeClaim(player.getUniqueId(), chunkID);
    }
}
