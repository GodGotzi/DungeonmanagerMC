package at.gotzi.dungeonmanager.manager.player;

import at.gotzi.dungeonmanager.commands.AdminCommands.BuildCommand;
import at.gotzi.dungeonmanager.data.PlayerData;
import at.gotzi.dungeonmanager.manager.world.ClaimManager;
import at.gotzi.dungeonmanager.manager.world.ResourceWorldsManager;
import at.gotzi.dungeonmanager.utils.Locations;
import at.gotzi.dungeonmanager.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.UUID;

public class InteractManager {

    private final UUID uuid;
    private final Location location;

    public InteractManager(UUID uuid, Location location) {
        this.uuid = uuid;
        this.location = location;
    }

    public boolean isAllowedStructure() {
        Player player = Bukkit.getPlayer(this.uuid);
        if (BuildCommand.hasBuildEnabled(this.uuid)) return true;
        if(this.location.getWorld().getName().equals(Locations.openWorldSpawn.getWorld().getName())) {
            if (inSpawnRange(player.getChunk()) || player.getTargetBlock(5) != null) {
                if (player.getTargetBlock(5) != null) {
                    if (inSpawnRange(player.getTargetBlock(5).getChunk())) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
            ClaimManager claimManager = new ClaimManager(player);
            UUID owner = claimManager.getClaimOwnerByChunk(player.getChunk().getChunkKey());
            if (owner == null) return true;
            if (!Objects.equals(owner.toString(), player.getUniqueId().toString())) {
                if (!PlayerData.getTrustedRAM(owner).contains(player.getUniqueId().toString())) {
                    return false;
                }
            }
        }

        if (this.location.getWorld().getName().equals(Locations.lobbySpawn.getWorld().getName()))
            return false;

        if (this.location.getWorld().getName().contains("resource")) {
            if (this.location.getWorld().getName().contains("overworld")) {
                Location location = Locations.overworldSpawn;
                location = location.set(location.getX(), player.getLocation().getY(), location.getZ());
                if (location.distance(player.getLocation()) <= ResourceWorldsManager.overWorldRange) {
                    return false;
                }
            } else if (this.location.getWorld().getName().contains("nether")) {
                Location location = Locations.netherSpawn;
                location = location.set(location.getX(), player.getLocation().getY(), location.getZ());
                if (location.distance(player.getLocation()) <= ResourceWorldsManager.netherRange) {
                    return false;
                }
            } else if (this.location.getWorld().getName().contains("end")) {
                Location location = Locations.endSpawn;
                location = location.set(location.getX(), player.getLocation().getY(), location.getZ());
                if (location.distance(player.getLocation()) <= ResourceWorldsManager.endRange) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isAllowedInteractBlock() {
        Player player = Bukkit.getPlayer(this.uuid);
        if (BuildCommand.hasBuildEnabled(this.uuid)) return true;
        if(this.location.getWorld().getName().equals(Locations.openWorldSpawn.getWorld().getName())) {
            ClaimManager claimManager = new ClaimManager(player);
            UUID owner = claimManager.getClaimOwnerByChunk(player.getTargetBlock(5).getChunk().getChunkKey());
            if (inSpawnRange(player.getChunk()) || player.getTargetBlock(5) != null) {
                if (player.getTargetBlock(5) != null) {
                    if (inSpawnRange(player.getTargetBlock(5).getChunk())) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
            if (owner == null) {
                return true;
            }
            if (!Objects.equals(owner.toString(), player.getUniqueId().toString())) {
                if (!PlayerData.getTrustedRAM(owner).contains(player.getUniqueId().toString())) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean inSpawnRange(Chunk chunk) {
        Chunk spawn = Locations.openWorldSpawn.getChunk();
        int x = chunk.getX();
        int z = chunk.getZ();
        return Math.abs(spawn.getX() - x) <= (ClaimManager.spawnRange / 16) && Math.abs(spawn.getZ() - z) <= (ClaimManager.spawnRange / 16);
    }

    public Location getLocation() {
        return location;
    }

    public UUID getUuid() {
        return uuid;
    }
}
