package at.gotzi.dungeonmanager.manager.file;

import at.gotzi.dungeonmanager.Main;
import at.gotzi.dungeonmanager.objects.HunterFish;
import at.gotzi.dungeonmanager.objects.Loc;
import at.gotzi.dungeonmanager.utils.Messages;
import at.gotzi.dungeonmanager.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collection;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

public class HunterFishManager extends FileManager {

    private static YamlConfiguration sfConfig;

    private static UUID fish;
    private static UUID target = UUID.randomUUID();

    public static int schedulerId = 0;
    public static String customName;
    public static int cost;
    public static int wait;
    public static int waitOffline;
    public static double speed;

    public HunterFishManager() {
        super(YamlConfiguration.loadConfiguration(new File("plugins//DungeonManager//hunterFish.yml")));
        sfConfig = YamlConfiguration.loadConfiguration(new File("plugins//DungeonManager//hunterFish.yml"));
    }

    @Override
    public void initialize() {
        HunterFishManager.customName = Utils.color(sfConfig.getString("customName"));
        HunterFishManager.cost = sfConfig.getInt("cost");
        HunterFishManager.wait = sfConfig.getInt("wait");
        HunterFishManager.waitOffline = sfConfig.getInt("waitOffline");
        HunterFishManager.speed = sfConfig.getDouble("speed");
        startFirst();
    }

    public static void startFirst() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!Bukkit.getOnlinePlayers().isEmpty()) {
                    if (!Bukkit.getWorld("openworld").getPlayers().isEmpty()) {
                        nextTarget();
                        this.cancel();
                    }
                }
            }
        }.runTaskTimer(Main.getInstance(), 20, 20);
    }

    public static void startNextTarget() {
        schedulerId = Bukkit.getScheduler().runTaskLater(Main.getInstance(),
                () -> Bukkit.getScheduler().runTaskLater(
                        Main.getInstance(),
                        HunterFishManager::startFirst,
                        wait*20L),
                waitOffline*20L).getTaskId();
    }

    public static void nextTarget(@NotNull Player player , boolean silent) {
        target = player.getUniqueId();
        Loc loc = generateLoc(player.getWorld(), (int)player.getLocation().getX(), (int)player.getLocation().getZ());
        Location location = new Location(Bukkit.getWorld(loc.getWorld()),
                loc.getX(),
                loc.getY(),
                loc.getZ());
        if (Bukkit.getEntity(fish) != null)
            Bukkit.getEntity(fish).remove();
        HunterFish hunterFish = new HunterFish(player, ((CraftWorld)player.getWorld()).getHandle(), loc);
        ((CraftWorld)player.getWorld()).getHandle().addEntity(hunterFish);
        fish = hunterFish.getUniqueID();
        hunterFish.teleportAndSync(location.getX(), location.getY(), location.getZ());
        if (!silent)
            player.sendMessage(Messages.huntAdd);
    }

    public static void removeFish(boolean silent) {
        if (Bukkit.getEntity(fish) == null) return;
        Bukkit.getEntity(fish).remove();
        if (!silent) {
            if (target != null) {
                if (Bukkit.getPlayer(target) != null) {
                    Bukkit.getPlayer(target).sendMessage(Messages.huntRemove);
                }
            }
        }
        fish = UUID.randomUUID();
        target = UUID.randomUUID();
    }

    public static void nextTarget() {
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        int rnd;
        int i = 0;
        do {
            if (i >= players.size()) {
                startFirst();
            }
            rnd = new Random().nextInt(players.size());
            i++;
        } while (Objects.equals(target.toString(), players.stream().toList().get(rnd).getUniqueId().toString()) ||
                !players.stream().toList().get(rnd).getWorld().getName().contains("openworld"));
        Player player = players.stream().toList().get(rnd);
        target = player.getUniqueId();
        Loc loc = generateLoc(player.getWorld(), (int)player.getLocation().getX(), (int)player.getLocation().getZ());
        Location location = new Location(Bukkit.getWorld(loc.getWorld()),
                loc.getX(),
                loc.getY(),
                loc.getZ());
        if (fish != null)
            if (Bukkit.getEntity(fish) != null)
                Bukkit.getEntity(fish).remove();
        HunterFish hunterFish = new HunterFish(player, ((CraftWorld)player.getWorld()).getHandle(), loc);
        ((CraftWorld)player.getWorld()).getHandle().addEntity(hunterFish);
        fish = hunterFish.getUniqueID();
        hunterFish.teleportAndSync(location.getX(), location.getY(), location.getZ());
        player.sendMessage(Messages.huntAdd);
    }

    public static Loc generateLoc(World world, int x, int z) {
        int rx = new Random().nextInt(6)+10;
        int rz = new Random().nextInt(6)+10;
        if (new Random().nextBoolean())
            rx *= -1;
        if (new Random().nextBoolean())
            rz *= -1;
        int ry = findYBlock(world, x, z);
        return new Loc(world.getName(), x+rx, ry, z+rz, 0, 0);
    }

    public static int findYBlock(World world, int x, int z) {
        int y = 50;
        Material[] material;
        do {
            y++;
            material = new Material[]{world.getBlockAt(x, y, z).getType(), world.getBlockAt(x, y+1, z).getType()};
        } while (material[0] != Material.AIR || material[1] != Material.AIR);
        return y;
    }

    public static void cancelTask() {
        if (schedulerId != 0)
            Bukkit.getScheduler().cancelTask(schedulerId);
        schedulerId = 0;
    }

    public static UUID getTarget() {
        return target;
    }

    public static UUID getFish() {
        return fish;
    }

    public static void setTarget(UUID target) {
        HunterFishManager.target = target;
    }
}
