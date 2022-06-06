package at.gotzi.dungeonmanager.manager;

import at.gotzi.dungeonmanager.Main;
import at.gotzi.dungeonmanager.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class BossManager {

    private static HashMap<UUID, UUID> bosses = new HashMap<>();


    public static void removeBoss(List<UUID> uuids) {
        for (UUID uuid : uuids) {
            bosses.remove(uuid);
        }
    }

    public static void removeBoss(UUID uuid) {
        bosses.remove(uuid);
    }

    public static void addDuringRun(UUID u, UUID uuid) {
        if (!bosses.containsKey(u) && Bukkit.getEntity(uuid) != null)
            bosses.put(u, uuid);
    }

    public static UUID getBoss(UUID uuid) {
        return bosses.get(uuid);
    }

    public static boolean isBoss(UUID u, UUID uuid) {
        return bosses.get(u).toString().equals(uuid.toString());
    }

    public static boolean isDungeonBoss(UUID uuid) {
        return bosses.containsValue(uuid);
    }

    public static Player getPlayer(UUID uuid) {
        for (UUID uuid1 : bosses.keySet()) {
            if (bosses.get(uuid1).toString().equals(uuid.toString()))
                return Bukkit.getPlayer(uuid1);
        }
        return null;
    }

    public static void playTitle(String world, String playerName) {
        new BukkitRunnable() {
            @Override
            public void run() {
                String dungeon = world.split("//")[2];
                dungeon = dungeon.replace("_" + dungeon.split("_")[dungeon.split("_").length-1], "");
                String title = Utils.color(Main.getInstance().getConfig().getString( "dungeons." +  dungeon + "." + "bossKill.title").replace("%PLAYER%", playerName));
                String subTitle = Utils.color(Main.getInstance().getConfig().getString( "dungeons." +  dungeon + "." + "bossKill.subtitle").replace("%PLAYER%", playerName));
                int fadeIn = Main.getInstance().getConfig().getInt("dungeons." + dungeon + "." + "bossKill.fadeIn");
                int fadeOut = Main.getInstance().getConfig().getInt( "dungeons." + dungeon + "." + "bossKill.fadeOut");
                int stay = Main.getInstance().getConfig().getInt("dungeons." + dungeon + "." + "bossKill.stay");

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        World w = Bukkit.getWorld(world);
                        for (Player player : w.getPlayers()) {
                            player.sendTitle(title, subTitle, fadeIn, fadeOut, stay);
                        }
                    }
                }.runTask(Main.getInstance());
            }
        }.runTaskAsynchronously(Main.getInstance());
    }

    public static void addBoss(List<UUID> uuids, UUID uuid) {
        for (UUID u: uuids) {
            bosses.put(u, uuid);
        }
    }

    public static void addBoss(UUID u, UUID uuid) {
        bosses.put(u, uuid);
    }

    public static boolean hasBoss(UUID uuid) {
        return bosses.containsKey(uuid);
    }

}
