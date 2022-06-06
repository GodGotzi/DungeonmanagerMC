// This class was created by Wireless


package at.gotzi.dungeonmanager.listener.playerlistener;

import at.gotzi.dungeonmanager.Main;
import at.gotzi.dungeonmanager.commands.PortalCommand;
import at.gotzi.dungeonmanager.events.MoveEvent;
import at.gotzi.dungeonmanager.manager.dungeon.ChestManager;
import at.gotzi.dungeonmanager.manager.dungeon.DungeonSetterTimer;
import at.gotzi.dungeonmanager.manager.file.PetManager;
import at.gotzi.dungeonmanager.manager.player.TeleportManager;
import at.gotzi.dungeonmanager.manager.world.DungeonWorldManager;
import at.gotzi.dungeonmanager.manager.world.PortalManager;
import at.gotzi.dungeonmanager.utils.Locations;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.potion.PotionEffect;

import java.util.*;

public class MoveListener implements Listener {

    private Main main;
    private DungeonWorldManager dungeonWorldManager;
    private ChestManager chestManager;
    private PortalManager portalManager = new PortalManager();
    private DungeonSetterTimer dungeonSetterTimer;
    private List<Player> waitList = new ArrayList<>();
    private static int dungeonUsed = 0;

    private static final YamlConfiguration pconfig = PortalCommand.getPconfig();

    public MoveListener(Main main, DungeonWorldManager dungeonWorldManager, DungeonSetterTimer dungeonSetterTimer) {
        this.dungeonSetterTimer = dungeonSetterTimer;
        this.main = main;
        this.dungeonWorldManager = dungeonWorldManager;
        this.chestManager = new ChestManager(main);
    }

    @EventHandler(
            priority = EventPriority.HIGHEST
    )
    public void onMove(MoveEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();
        Player player = event.getPlayer();
        if (event.getTo().getX() != event.getFrom().getX() || event.getTo().getZ() != event.getFrom().getZ())
            TeleportManager.locUUID.remove(player.getUniqueId());
        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            Set<String> portals = pconfig.getConfigurationSection("portals").getKeys(false);
            for (String portal : portals) {
                String world = pconfig.getString("portals." + portal + ".world");
                assert world != null;
                if(from.getWorld().getName().equals(world)) {
                    String pos1 = pconfig.getString("portals." + portal + ".pos1");
                    if (pos1 == null) return;
                    double x1 = Double.parseDouble(pos1.split(";")[0]);
                    double y1 = Double.parseDouble(pos1.split(";")[1]);
                    double z1 = Double.parseDouble(pos1.split(";")[2]);
                    String pos2 = pconfig.getString("portals." + portal + ".pos2");
                    if (pos2 == null) return;
                    double x2 = Double.parseDouble(pos2.split(";")[0]);
                    double y2 = Double.parseDouble(pos2.split(";")[1]);
                    double z2 = Double.parseDouble(pos2.split(";")[2]);
                    if (to.getX() <= x1 && to.getX() >= x2 || to.getX() >= x1 && to.getX() <= x2) {
                        if (to.getY() <= y1 && to.getY() >= y2 || to.getY() >= y1 && to.getY() <= y2) {
                            if (to.getZ() <= z1 && to.getZ() >= z2 || to.getZ() >= z1 && to.getZ() <= z2) {
                                Location location = portalManager.getPortalDest(portal);
                                Bukkit.getScheduler().runTask(main, () -> {
                                    player.teleport(location);
                                    if (location.getWorld() == event.getFrom().getWorld()) {
                                        if (PetManager.hasPet(player)) {
                                            PetManager.tpPetSameWorld(player);
                                        }
                                    }
                                });
                                return;
                            }
                        }
                    }
                }
            }
            if(from.getWorld().getName().equals(Locations.lobbySpawn.getWorld().getName())) {
                String pos1 = main.getConfig().getString("portal.pos1");
                double x1 = Double.parseDouble(pos1.split(";")[0]);
                double y1 = Double.parseDouble(pos1.split(";")[1]);
                double z1 = Double.parseDouble(pos1.split(";")[2]);
                String pos2 = main.getConfig().getString("portal.pos2");
                double x2 = Double.parseDouble(pos2.split(";")[0]);
                double y2 = Double.parseDouble(pos2.split(";")[1]);
                double z2 = Double.parseDouble(pos2.split(";")[2]);
                if (to.getX() <= x1 && to.getX() >= x2 || to.getX() >= x1 && to.getX() <= x2) {
                    if (to.getY() <= y1 && to.getY() >= y2 || to.getY() >= y1 && to.getY() <= y2) {
                        if (to.getZ() <= z1 && to.getZ() >= z2 || to.getZ() >= z1 && to.getZ() <= z2) {
                            worldManage(player);
                            return;
                        }
                    }
                }
            }
        });

        if (event.getPlayer().getWorld().getName().equals("world")) {
            if (event.getTo().getY() <= Main.getInstance().getConfig().getInt("voidReset")) {
                player.teleport(Locations.lobbySpawn);
            }
        }

        /*if(PetManager.pets.containsKey(player.getUniqueId())) {
            Entity entity = PetManager.getPet(player.getUniqueId());
            if(entity == null) {
                PetManager.removePet(player);
                return;
            }
            if(player.getLocation().distance(entity.getLocation()) > 8) {
                entity.teleport(player.getLocation().subtract(2, 0, 2));
            }
        }*/
    }

    public void worldManage(Player player) {
        Bukkit.getScheduler().runTaskAsynchronously(main, new Runnable() {
            @Override
            public void run() {
                if(waitList.contains(player)) {
                    return;
                }
                String currentDungeon = DungeonSetterTimer.getCurrentDungeon();
                String loc = main.getConfig().getString("dungeons." + currentDungeon + ".spawn");
                double x = Double.parseDouble(loc.split(";")[0]);
                double y = Double.parseDouble(loc.split(";")[1]);
                double z = Double.parseDouble(loc.split(";")[2]);
                float yaw = Float.parseFloat(loc.split(";")[3]);
                float pitch = Float.parseFloat(loc.split(";")[4]);
                waitList.add(player);
                Bukkit.getScheduler().runTask(main, () -> {
                    World world = dungeonWorldManager.loadWorld(currentDungeon + "_" + String.valueOf(DungeonWorldManager.dungeonCount));
                    dungeonWorldManager.spawnMobs(currentDungeon, world, player.getUniqueId());
                    chestManager.setChests(currentDungeon, player, world);
                    player.teleport(new Location(world, x, y, z, yaw, pitch));
                    player.setGameMode(GameMode.ADVENTURE);
                    for (PotionEffect potionEffect : player.getActivePotionEffects()) {
                        player.removePotionEffect(potionEffect.getType());
                    }
                });

                Bukkit.getScheduler().runTaskLaterAsynchronously(main, () -> {
                    waitList.remove(player);
                    dungeonWorldManager.copyWorld();
                }, 20);
                dungeonUsed++;
            }
        });
    }

    @EventHandler
    public void onPortal(PlayerPortalEvent event) {
        event.setCancelled(true);
    }

    public static int getDungeonUsed() {
        return dungeonUsed;
    }

}