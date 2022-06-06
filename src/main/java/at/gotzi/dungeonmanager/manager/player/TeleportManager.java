package at.gotzi.dungeonmanager.manager.player;

import at.gotzi.dungeonmanager.Main;
import at.gotzi.dungeonmanager.manager.file.BasicConfigManager;
import at.gotzi.dungeonmanager.manager.file.HunterFishManager;
import at.gotzi.dungeonmanager.manager.file.PetManager;
import at.gotzi.dungeonmanager.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class TeleportManager {

    public static HashMap<UUID, Loc> locUUID = new HashMap<>();
    public static HashMap<UUID, Integer> counter = new HashMap<>();

    public static void registerTeleport(Player player, Location location) {
        locUUID.put(player.getUniqueId(), new Loc(location.getWorld().getName(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch()));
        counter.put(player.getUniqueId(), 0);

        UUID uuid = player.getUniqueId();

        new BukkitRunnable() {
            @Override
            public void run() {
                int count = counter.get(uuid);
                if (player == null) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            BasicConfigManager.noActionBar.remove(player.getUniqueId());
                        }
                    }.runTaskLaterAsynchronously(Main.getInstance(), 60);
                    return;
                }
                if (!locUUID.containsKey(uuid)) {
                    player.sendActionBar(Messages.teleportCancel);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            BasicConfigManager.noActionBar.remove(player.getUniqueId());
                        }
                    }.runTaskLaterAsynchronously(Main.getInstance(), 60);
                    this.cancel();
                } else {
                    if (count == 0) {
                        BasicConfigManager.noActionBar.add(player.getUniqueId());
                        player.sendActionBar(Messages.teleportCount.replace("%TIME%", "3"));
                    } else if (count == 1) {
                        player.sendActionBar(Messages.teleportCount.replace("%TIME%", "2"));
                    } else if (count == 2) {
                        player.sendActionBar(Messages.teleportCount.replace("%TIME%", "1"));
                    } else if (count == 3) {
                        player.sendActionBar(Messages.teleportMsg);
                        Loc loc = locUUID.get(uuid);
                        if (PetManager.hasPet(player)) {
                            PetManager.tpPet(player, new Location(Bukkit.getWorld(loc.getWorld()), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch()));
                        }
                        if (player.getWorld().getName().equals("openworld")) {
                            if (Objects.equals(player.getUniqueId().toString(), HunterFishManager.getTarget().toString())) {
                                HunterFishManager.removeFish(true);
                                HunterFishManager.nextTarget(player, true);
                            }
                        }
                        player.teleport(new Location(Bukkit.getWorld(loc.getWorld()), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch()));
                        counter.remove(uuid);
                        locUUID.remove(uuid);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                BasicConfigManager.noActionBar.remove(player.getUniqueId());
                            }
                        }.runTaskLaterAsynchronously(Main.getInstance(), 60);
                        this.cancel();
                    }
                    count++;
                    counter.put(uuid, count);
                }
            }
        }.runTaskTimer(Main.getInstance(), 0, 20);
    }

    private static class Loc {
        private final String world;
        private final double x;
        private final double y;
        private final double z;
        private final float yaw;
        private final float pitch;

        private Loc(String world, double x, double y, double z, float yaw, float pitch) {
            this.world = world;
            this.x = x;
            this.y = y;
            this.z = z;
            this.yaw = yaw;
            this.pitch = pitch;
        }

        public String getWorld() {
            return world;
        }

        public float getPitch() {
            return pitch;
        }

        public float getYaw() {
            return yaw;
        }

        public double getZ() {
            return z;
        }

        public double getY() {
            return y;
        }

        public double getX() {
            return x;
        }
    }
}
