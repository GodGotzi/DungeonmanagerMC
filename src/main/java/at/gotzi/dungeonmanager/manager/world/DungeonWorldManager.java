// This class was created by Wireless


package at.gotzi.dungeonmanager.manager.world;

import at.gotzi.dungeonmanager.Main;
import at.gotzi.dungeonmanager.manager.BossManager;
import at.gotzi.dungeonmanager.manager.dungeon.DungeonFileManager;
import at.gotzi.dungeonmanager.manager.dungeon.DungeonSetterTimer;
import at.gotzi.dungeonmanager.objects.enums.Error;
import at.gotzi.dungeonmanager.utils.Utils;
import com.magmaguy.elitemobs.config.custombosses.CustomBossesConfig;
import com.magmaguy.elitemobs.config.custombosses.CustomBossesConfigFields;
import com.magmaguy.elitemobs.mobconstructor.custombosses.CustomBossEntity;
import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.util.*;

public class DungeonWorldManager {

    private Main main;
    public static int dungeonCount = 0;
    private static List<String> worldList = new ArrayList<>();
    private DungeonSetterTimer dungeonSetterTimer;
    private DungeonFileManager dungeonFileManager;

    public DungeonWorldManager(Main main) {
        this.main = main;
        this.dungeonSetterTimer = new DungeonSetterTimer(main, this);
        this.dungeonFileManager = new DungeonFileManager(main);
    }

    public World loadWorld(String path) {
        String currentDungeon = DungeonSetterTimer.getCurrentDungeon();
        World.Environment env = null;
        String environment = main.getConfig().getString("dungeons." + currentDungeon + ".environment");
        assert environment != null;
        if(environment.equalsIgnoreCase("NORMAL")) {
            env = World.Environment.NORMAL;
        } else if(environment.equalsIgnoreCase("NETHER")){
            env = World.Environment.NETHER;
        } else if(environment.equalsIgnoreCase("THE_END")) {
            env = World.Environment.THE_END;
        } else {
            System.err.println("Error at environment " + currentDungeon + " environment is not valid!");
        }
        World.Environment finalEnv = env;
        World world = new WorldCreator("dungeons//dungeoncache//" + path).environment(finalEnv).createWorld();
        assert world != null;
        setTicksNull(world);
        main.print("World: " + world.getName() + " loaded!");
        return world;
    }

    public void deleteWorldFoldersWhileRun(String currentDungeon) {
        File file = new File("dungeons/dungeoncache");
        if (file.isDirectory()) {
            if (!Arrays.stream(file.listFiles()).toList().isEmpty()) {
                for (File world : Arrays.stream(file.listFiles()).toList()) {
                    if (!world.getName().contains(currentDungeon) && Bukkit.getWorld("dungeons//dungeoncache//"+ world.getName()) == null) {
                        this.deleteWorldWhileRun(new File("dungeons/dungeoncache/" + world.getName()));
                        main.print("Cache cleared from " + world);
                    }
                }
            } else
                Utils.callError("Structure Error call me", Error.Unexpected);
        } else
            Utils.callError("Structure Error call me", Error.Unexpected);
    }

    public void copyWorld() {
        dungeonCount++;
        String currentDungeon = dungeonSetterTimer.getCurrentDungeon();
        File currentDunFile = new File("dungeons//" + currentDungeon);
        File newFolder = new File( "dungeons//dungeoncache//" + currentDungeon + "_" + dungeonCount);
        this.copyFolder(currentDunFile, newFolder);
        main.print("World: " + currentDungeon + "_" + dungeonCount + " copied!");
        worldList.add(currentDungeon + "_" + dungeonCount);
    }

    public void copyWorld(String nextDungeon) {
        dungeonCount++;
        File currentDunFile = new File("dungeons//" + nextDungeon);
        File newFolder = new File( "dungeons//dungeoncache//" + nextDungeon + "_" + dungeonCount);
        this.copyFolder(currentDunFile, newFolder);
        main.print("World: " + nextDungeon + "_" + dungeonCount + " copied!");
        worldList.add(nextDungeon + "_" + dungeonCount);
    }

    public void setTicksNull(World world) {
        world.setTicksPerAmbientSpawns(0);
        world.setTicksPerAnimalSpawns(0);
        world.setTicksPerMonsterSpawns(0);
        world.setTicksPerWaterAmbientSpawns(0);
        world.setTicksPerWaterSpawns(0);
        world.setGameRule(GameRule.RANDOM_TICK_SPEED, 0);
        world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
    }

    public void deleteWorld(World world) {
        Bukkit.getScheduler().runTaskAsynchronously(main, new Runnable() {
            @Override
            public void run() {
                File file = world.getWorldFolder();
                deleteWorld(file);
            }
        });
    }

    public void unloadWorld(World world) {
        if(world != null) {
            Bukkit.getServer().unloadWorld(world, true);
            main.print("World: " + world.getName().split("//")[2] + " unloaded!");
        }
    }

    public void deleteWorld(File path) {
        if(path.exists()) {
            File files[] = path.listFiles();
            for(int i=0; i < files.length; i++) {
                if(files[i].isDirectory()) {
                    deleteWorld(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        path.delete();
    }

    public void deleteWorldWhileRun(File path) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if(path.exists()) {
                    File files[] = path.listFiles();
                    for(int i=0; i < files.length; i++) {
                        if(files[i].isDirectory()) {
                            deleteWorld(files[i]);
                        } else {
                            files[i].delete();
                        }
                    }
                }
                path.delete();
            }
        }.runTaskAsynchronously(Main.getInstance());
    }

    public void copyFolder(File source, File target){
        try {
            ArrayList<String> ignore = new ArrayList<String>(Arrays.asList("uid.dat", "session.dat"));
            if(!ignore.contains(source.getName())) {
                if(source.isDirectory()) {
                    if(!target.exists())
                        target.mkdirs();
                    String files[] = source.list();
                    for (String file : files) {
                        File srcFile = new File(source, file);
                        File destFile = new File(target, file);
                        copyFolder(srcFile, destFile);
                    }
                } else {
                    InputStream in = new FileInputStream(source);
                    OutputStream out = new FileOutputStream(target);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = in.read(buffer)) > 0)
                        out.write(buffer, 0, length);
                    in.close();
                    out.close();
                }
            }
        } catch (IOException e) {

        }
    }

    public void spawnMobs(String currentDungeon, World world, UUID player) {
        List<EliteMobLoc> eliteMobLocs = new ArrayList<>();

        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            YamlConfiguration config = dungeonFileManager.readDungeonFiles(currentDungeon);
            Set<String> mobs = dungeonFileManager.getMobs(config);
            for (String mob : mobs) {
                String type = config.getString("mobs." + mob + ".type").split(";")[0];
                String locString = config.getString("mobs." + mob + ".spawnLocation");

                int x = Integer.parseInt(locString.split(";")[0]);
                int y = Integer.parseInt(locString.split(";")[1]);
                int z = Integer.parseInt(locString.split(";")[2]);
                float yaw = Float.parseFloat(locString.split(";")[3]);
                float pitch = Float.parseFloat(locString.split(";")[4]);

                Location location = new Location(world, x, y, z, yaw, pitch);
                CustomBossesConfigFields customBossesConfigFields = CustomBossesConfig.getCustomBoss(type);
                if (customBossesConfigFields == null) {
                    Bukkit.getLogger().info("§cError! " + currentDungeon + " Invalid CustomMob Type");
                } else {
                    if (!type.contains("tier")) {
                        String level = config.getString("mobs." + mob + ".type").split(";")[1];
                        eliteMobLocs.add(new EliteMobLoc(type, Integer.parseInt(level),
                                (int)location.getX(), (int)location.getY(),
                                (int)location.getZ(), (float) location.getYaw(),
                                (float)location.getPitch(), false));
                    } else {
                        eliteMobLocs.add(new EliteMobLoc(type, (int) location.getX(),
                                (int) location.getY(),
                                (int) location.getZ(), (float) location.getYaw(),
                                (float) location.getPitch(), false));
                    }
                }

            }
            Utils.info(" Dungeon Entities loaded!");
            String type = config.getString("boss" + ".type");
            if (type != null) {
                type = type.split(";")[0];
                String locString = config.getString("boss" + ".spawnLocation");

                int x = Integer.parseInt(locString.split(";")[0]);
                int y = Integer.parseInt(locString.split(";")[1]);
                int z = Integer.parseInt(locString.split(";")[2]);
                float yaw = Float.parseFloat(locString.split(";")[3]);
                float pitch = Float.parseFloat(locString.split(";")[4]);

                Location location = new Location(world, x, y, z, yaw, pitch);
                CustomBossesConfigFields customBossesConfigFields = CustomBossesConfig.getCustomBoss(type);
                if (customBossesConfigFields == null) {
                    Bukkit.getLogger().info("§cError! " + currentDungeon + " Invalid CustomMob Type");
                } else {
                    CustomBossEntity customBossEntity = new CustomBossEntity(customBossesConfigFields);
                    if (!type.contains("tier")) {
                        String level = config.getString("boss" + ".type").split(";")[1];
                        eliteMobLocs.add(new EliteMobLoc(type, Integer.parseInt(level),
                                (int)location.getX(), (int)location.getY(),
                                (int)location.getZ(), (float) location.getYaw(),
                                (float)location.getPitch(), true));
                    } else {
                        eliteMobLocs.add(new EliteMobLoc(type, (int) location.getX(),
                                (int) location.getY(),
                                (int) location.getZ(), (float) location.getYaw(),
                                (float) location.getPitch(), true));
                    }
                    Utils.info(" Dungeon Boss loaded!");
                }
            }

            new BukkitRunnable() {
                @Override
                public void run() {
                    if (Bukkit.getWorld(world.getName()) == null)
                        this.cancel();
                    for (Player all : world.getPlayers()) {
                        List<EliteMobLoc> remove = new ArrayList<>();
                        for (EliteMobLoc eliteMobLoc : eliteMobLocs) {
                            if (eliteMobLoc != null) {
                                Location location = new Location(all.getWorld(), eliteMobLoc.getX(), eliteMobLoc.getY(), eliteMobLoc.getZ(), eliteMobLoc.getYaw(), eliteMobLoc.getPitch());
                                if (all.getLocation().distance(location) < 50) {
                                    remove.add(eliteMobLoc);
                                    CustomBossesConfigFields customBossesConfigFields = CustomBossesConfig.getCustomBoss(eliteMobLoc.getEliteMob());
                                    CustomBossEntity customBossEntity = new CustomBossEntity(customBossesConfigFields);
                                    if (eliteMobLoc.getLevel() != 0)
                                        customBossEntity.setLevel(eliteMobLoc.getLevel());
                                    customBossEntity.setSpawnLocation(location);
                                    customBossEntity.spawn(true);
                                    if (eliteMobLoc.isBoss())
                                        BossManager.addBoss(player, customBossEntity.getLivingEntity().getUniqueId());
                                    PotionEffect potionEffect = new PotionEffect(PotionEffectType.SLOW, 13000000, 255);
                                    potionEffect.apply(customBossEntity.getLivingEntity());
                                }
                            }
                        }
                        eliteMobLocs.removeAll(remove);

                        if (BossManager.getBoss(player) != null) {
                            BossManager.addDuringRun(all.getUniqueId(), BossManager.getBoss(player));
                        }
                        List<Entity> entities1 = all.getNearbyEntities(15, 15, 15);
                        for (Entity entity : entities1) {
                            if (entity instanceof LivingEntity livingEntity && !(entity instanceof Player)) {
                                for (PotionEffect potionEffect : livingEntity.getActivePotionEffects()) {
                                    livingEntity.removePotionEffect(potionEffect.getType());
                                }
                            }
                        }
                    }
                }
            }.runTaskTimer(main, 20, 20);
        });
    }

    public void deleteWorldFolders() {
        File file = new File("dungeons/dungeoncache");
        if (file.isDirectory()) {
            if (!Arrays.stream(file.listFiles()).toList().isEmpty()) {
                for (File world : Arrays.stream(file.listFiles()).toList()) {
                    if (Bukkit.getWorld(world.getName()) != null)
                        Bukkit.unloadWorld("dungeons/dungeoncache/" + world, false);
                    this.deleteWorld(new File("dungeons/dungeoncache/" + world.getName()));
                    main.print("Cache cleared from " + world);
                }
            } else
                Utils.callError("Structure Error call me", Error.Unexpected);
        } else
            Utils.callError("Structure Error call me", Error.Unexpected);
    }

    public static int getDungeonCount() {
        return dungeonCount;
    }

    public static List<String> getWorldList() {
        return worldList;
    }

    private static class EliteMobLoc {
        private final String eliteMob;
        private int level = 0;
        private final int x;
        private final int y;
        private final int z;
        private final float yaw;
        private final float pitch;
        private final boolean boss;

        private EliteMobLoc(String eliteMob, int level, int x, int y, int z, float yaw, float pitch, boolean boss) {
            this.eliteMob = eliteMob;
            this.x = x;
            this.y = y;
            this.z = z;
            this.yaw = yaw;
            this.pitch = pitch;
            this.boss = boss;
            this.level = level;
        }

        private EliteMobLoc(String eliteMob, int x, int y, int z, float yaw, float pitch, boolean boss) {
            this.eliteMob = eliteMob;
            this.x = x;
            this.y = y;
            this.z = z;
            this.yaw = yaw;
            this.pitch = pitch;
            this.boss = boss;
        }

        public boolean isBoss() {
            return boss;
        }

        public String getEliteMob() {
            return eliteMob;
        }

        public int getLevel() {
            return level;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getZ() {
            return z;
        }

        public float getYaw() {
            return yaw;
        }

        public float getPitch() {
            return pitch;
        }
    }

}