// This class was created by Wireless


package at.gotzi.dungeonmanager.data;

import at.gotzi.dungeonmanager.Main;
import at.gotzi.dungeonmanager.manager.file.GroupManager;
import at.gotzi.dungeonmanager.manager.file.PetManager;
import at.gotzi.dungeonmanager.objects.Loc;
import at.gotzi.dungeonmanager.objects.groups.Group;
import at.gotzi.dungeonmanager.objects.groups.others.Home;
import at.gotzi.dungeonmanager.objects.pets.Pet;
import at.gotzi.dungeonmanager.objects.skills.Skill;
import at.gotzi.dungeonmanager.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class PlayerData {

    private UUID uuid;

    private static Main main = Main.getInstance();

    private static final File playerDataFile = new File("plugins//PlayerData//playerdata.yml");
    private static final YamlConfiguration playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);

    public static HashMap<String, UUID> names = new HashMap<>();
    public static HashMap<UUID, Loc> claimHomes = new HashMap<>();
    public static HashMap<UUID, List<String>> groups = new HashMap<>();
    public static HashMap<UUID, int[]> playtime = new HashMap<>();
    public static HashMap<UUID, List<Long>> claimsByUUID = new HashMap<>();
    public static HashMap<UUID, List<String>> banned = new HashMap<>();
    public static HashMap<UUID, List<String>> trusted = new HashMap<>();
    public static HashMap<UUID, List<Home>> homes = new HashMap<>();
    public static HashMap<UUID, String> worlds = new HashMap<>();
    public static HashMap<UUID, Pets> pets = new HashMap<>();

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
    }

    public static void startPlayTime() {
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(main, new Runnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    int minutes = playtime.get(player.getUniqueId())[0];
                    int hours = playtime.get(player.getUniqueId())[1];
                    if (minutes == 60) {
                        hours++;
                        minutes = 0;
                    }
                    minutes++;
                    playtime.put(player.getUniqueId(), new int[]{minutes, hours});
                }
            }
        }, 1200, 1200);
    }

    public static void loadData() {
        String uuid = null;
        File file = new File("world" + "//playerdata");
        if(file.isDirectory()) {
            if(file.listFiles() == null) return;
            for (File file1 : Objects.requireNonNull(file.listFiles())) {
                uuid = file1.getName().replace(".dat", "");
                if(!uuid.contains("_old")) {
                    List<Long> cs = playerDataConfig.getLongList("players." + uuid + ".claims");
                    List<String> bannedList = playerDataConfig.getStringList("players." + uuid + ".banned");
                    List<String> trustedList = playerDataConfig.getStringList("players." + uuid + ".trusted");
                    String loc = playerDataConfig.getString("players."+ uuid + ".claimHome");
                    if (loc != null) {
                        double x = Double.parseDouble(loc.split(";")[0]);
                        double y = Double.parseDouble(loc.split(";")[1]);
                        double z = Double.parseDouble(loc.split(";")[2]);
                        float yaw = Float.parseFloat(loc.split(";")[3]);
                        float pitch = Float.parseFloat(loc.split(";")[4]);
                        claimHomes.put(UUID.fromString(uuid), new Loc(x, y, z, yaw, pitch));
                    }
                    String name = playerDataConfig.getString("players." + uuid + ".name");

                    names.put(name, UUID.fromString(uuid));
                    banned.put(UUID.fromString(uuid), bannedList);
                    trusted.put(UUID.fromString(uuid), trustedList);
                    claimsByUUID.put(UUID.fromString(uuid), cs);
                    Utils.info("Loaded Playerdata for " + uuid);
                }
            }
        }
    }

    public void savePlayerData(String world) {
        new BukkitRunnable() {
            @Override
            public void run() {
                playerDataConfig.set("players." + uuid + ".playtime.minutes", playtime.get(uuid)[0]);
                playerDataConfig.set("players." + uuid + ".playtime.hours", playtime.get(uuid)[1]);
                playerDataConfig.set("players." + uuid + ".groups", groups.get(uuid));
                playerDataConfig.set("players." + uuid + ".claims", claimsByUUID.get(uuid));
                playerDataConfig.set("players." + uuid + ".banned", trusted.get(uuid));
                playerDataConfig.set("players." + uuid + ".banned", banned.get(uuid));
                if (claimHomes.get(uuid) != null) {
                    Loc loc = claimHomes.get(uuid);
                    playerDataConfig.set("players." + uuid + ".claimHome", loc.getX() + ";" + loc.getY() + ";" + loc.getZ() + ";" + loc.getYaw() + ";" + loc.getPitch());
                } else {
                    playerDataConfig.set("players." + uuid + ".claimHome", null);
                }

                List<String> homeStrings = new ArrayList<>();
                for (Home home : homes.get(uuid)) {
                    homeStrings.add(home.getId() + ";" + home.getWorld() + ";" + home.getX() + ";" + home.getY() + ";" + home.getZ() + ";" + home.getYaw() + ";" + home.getPitch());
                }
                playerDataConfig.set("players." + uuid + ".homes", homeStrings);
                playerDataConfig.set("players." + uuid + ".world", world);

                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < pets.get(uuid).getExp().size(); i++) {
                    int exp = pets.get(uuid).getExp().get(i);
                    if (i != 0)
                        stringBuilder.append(";").append(exp);
                    else
                        stringBuilder.append(exp);
                }
                playerDataConfig.set("players." + uuid + ".pets", stringBuilder.toString());

                try {
                    playerDataConfig.save(playerDataFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                clearHashMaps();
            }
        }.runTaskAsynchronously(Main.getInstance());
    }

    public void savePlayerDataStop(String world) {
        playerDataConfig.set("players." + uuid + ".playtime.minutes", playtime.get(uuid)[0]);
        playerDataConfig.set("players." + uuid + ".playtime.hours", playtime.get(uuid)[1]);
        playerDataConfig.set("players." + uuid + ".groups", groups.get(uuid));
        playerDataConfig.set("players." + uuid + ".claims", claimsByUUID.get(uuid));
        playerDataConfig.set("players." + uuid + ".banned", trusted.get(uuid));
        playerDataConfig.set("players." + uuid + ".banned", banned.get(uuid));
        if (claimHomes.get(uuid) != null) {
            Loc loc = claimHomes.get(uuid);
            playerDataConfig.set("players." + uuid + ".claimHome", loc.getX() + ";" + loc.getY() + ";" + loc.getZ() + ";" + loc.getYaw() + ";" + loc.getPitch());
        } else {
            playerDataConfig.set("players." + uuid + ".claimHome", null);
        }

        List<String> homeStrings = new ArrayList<>();
        for (Home home : homes.get(uuid)) {
            homeStrings.add(home.getId() + ";" + home.getWorld() + ";" + home.getX() + ";" + home.getY() + ";" + home.getZ() + ";" + home.getYaw() + ";" + home.getPitch());
        }
        playerDataConfig.set("players." + uuid + ".homes", homeStrings);
        playerDataConfig.set("players." + uuid + ".world", world);

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < pets.get(uuid).getExp().size(); i++) {
            int exp = pets.get(uuid).getExp().get(i);
            if (i != 0)
                stringBuilder.append(";").append(exp);
            else
                stringBuilder.append(exp);
        }
        playerDataConfig.set("players." + uuid + ".pets", stringBuilder.toString());

        try {
            playerDataConfig.save(playerDataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        clearHashMaps();
    }

    public void getPetsConfig() {
        HashMap<Integer, Integer> ps = new HashMap<>();
        if (playerDataConfig.getString("players." + uuid + ".pets") != null) {
            int counter = 0;
            for (String lvl : playerDataConfig.getString("players." + uuid + ".pets").split(";")) {
                ps.put(counter, Integer.parseInt(lvl));
                counter++;
            }
        } else {
            for (int i = 0; i < PetManager.lvls.size(); i++) {
                ps.put(i, 0);
            }
        }
        pets.put(uuid, new Pets(ps));
    }

    public void clearHashMaps() {
        playtime.remove(uuid);
        groups.remove(uuid);
        homes.remove(uuid);
        pets.remove(uuid);
    }

    public static void selectGroup(UUID uuid, Group group) {
        removeGroup(uuid, group);
        addGroup(uuid, group);
    }

    public void getPlayTimeConfig() {
        int minutes = playerDataConfig.getInt("players." + uuid + ".playtime.minutes");
        int hours = playerDataConfig.getInt("players." + uuid + ".playtime.hours");
        String name = getName(uuid);
        if (!Objects.equals(name, Bukkit.getPlayer(uuid).getName())) {

            names.put(Bukkit.getPlayer(uuid).getName(), uuid);
            playerDataConfig.set("players." + uuid + ".name", Bukkit.getPlayer(uuid).getName());
            try {
                playerDataConfig.save(playerDataFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String world = playerDataConfig.getString("players." + uuid + ".world");
        if (world != null)
            worlds.put(uuid, world);
        playtime.put(uuid, new int[]{minutes, hours});
        Utils.info("Playtime loaded for " + uuid);
    }

    public static void setWorld(UUID uuid, String str) {
        worlds.put(uuid, str);
    }

    public static String getWorld(UUID uuid) {
        return worlds.get(uuid);
    }

    public void getGroupConfig() {
        List<String> groupList = playerDataConfig.getStringList("players." + uuid + ".groups");
        groups.put(uuid, groupList);
        Utils.info("Classes loaded for " + uuid);
    }

    public void getHomesConfig() {
        List<Home> hs = new ArrayList<>();

        if (playerDataConfig.getStringList("players." + uuid + ".homes") == null) {
            homes.put(uuid, hs);
            return;
        }

        for (String h : playerDataConfig.getStringList("players." + uuid + ".homes")) {
            String name = h.split(";")[0];
            String world = h.split(";")[1];
            if (Bukkit.getWorld(world) != null) {
                double x = Double.parseDouble(h.split(";")[2]);
                double y = Double.parseDouble(h.split(";")[3]);
                double z = Double.parseDouble(h.split(";")[4]);
                float yaw = Float.parseFloat(h.split(";")[5]);
                float pitch = Float.parseFloat(h.split(";")[6]);

                Home home = new Home(name, world, x ,y, z, yaw, pitch);
                hs.add(home);
            }
        }
        homes.put(uuid, hs);
    }

    public static List<Group> getGroupRAM(UUID uuid) {
        List<Group> gs = new ArrayList<>();
        for (String str : groups.get(uuid)) {
            gs.add(Group.getGroup(str));
        }
        return gs;
    }

    public static int[] getPlayTimeRAM(UUID uuid) {
        return playtime.get(uuid);
    }

    private static HashMap<UUID, BossBar> bossBarHashMap = new HashMap<>();

    public static void setPetsExp(UUID uuid, int index, int exp) {
        Pets ps = getPets(uuid);
        Player player = Bukkit.getPlayer(uuid);
        HashMap<Integer, Integer> integers = ps.getExp();
        int lvl = Utils.calPetLvl(exp+1);
        if (Utils.calPetLvl(integers.get(index)) != Utils.calPetLvl(exp)) {
            if (Utils.calPetLvl(exp+1) > (PetManager.lvls.size()-1)) {
                player.sendTitle(
                        PetManager.levelUp.getTitle()
                                .replace("%PET%", PetManager.entityTypes.get(index).toString())
                                .replace("%LVL%", "MAX"),
                        PetManager.levelUp.getSubTitle()
                                .replace("%PET%", PetManager.entityTypes.get(index).toString())
                                .replace("%LVL%", "MAX"),
                        PetManager.levelUp.getFadeIn(),
                        PetManager.levelUp.getFadeOut(),
                        PetManager.levelUp.getStay());
            } else {
                player.sendTitle(
                        PetManager.levelUp.getTitle()
                                .replace("%PET%", PetManager.entityTypes.get(index).toString())
                                .replace("%LVL%", String.valueOf(Utils.calPetLvl(exp + 1))),
                        PetManager.levelUp.getSubTitle()
                                .replace("%PET%", PetManager.entityTypes.get(index).toString())
                                .replace("%LVL%", String.valueOf(Utils.calPetLvl(exp + 1))),
                        PetManager.levelUp.getFadeIn(),
                        PetManager.levelUp.getFadeOut(),
                        PetManager.levelUp.getStay());
            }
        } else {
            if (!bossBarHashMap.containsKey(player.getUniqueId())) {
                BossBar bossBar;
                if (Utils.calPetLvl(exp) > (PetManager.lvls.size()-1)) {
                    bossBar = Bukkit.createBossBar(PetManager.levelExp.getTitle()
                                    .replace("%PET%", PetManager.entityTypes.get(index).toString())
                                    .replace("%LVL%", "MAX"),
                            PetManager.levelExp.getBarColor(),
                            PetManager.barStyle);
                    bossBar.setProgress(1);
                } else {
                    bossBar = Bukkit.createBossBar(PetManager.levelExp.getTitle()
                                    .replace("%PET%", PetManager.entityTypes.get(index).toString())
                                    .replace("%LVL%", String.valueOf(Utils.calPetLvl(exp + 1))),
                            PetManager.levelExp.getBarColor(),
                            PetManager.barStyle);
                    if (lvl != 1)
                        bossBar.setProgress(((double)exp-Utils.calPetExp(lvl-1))/((double)Utils.calPetExp(lvl)-Utils.calPetExp(lvl-1)));
                    else
                        bossBar.setProgress(((double)exp/(double)20));
                }
                bossBar.addPlayer(player);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (player != null) {
                            Pets ps = getPets(player.getUniqueId());
                            if (ps.getExp().get(index) == exp) {
                                bossBarHashMap.remove(player.getUniqueId());
                                bossBar.removeAll();
                            }
                        } else {
                            bossBar.removeAll();
                        }
                    }
                }.runTaskLater(Main.getInstance(), 100);
            } else {
                BossBar bossBar = bossBarHashMap.get(player.getUniqueId());
                if (Utils.calPetLvl(exp) > (PetManager.lvls.size()-1)) {
                    bossBar.setTitle(PetManager.levelExp.getTitle()
                            .replace("%PET%", PetManager.entityTypes.get(index).toString())
                            .replace("%LVL%", "MAX"));
                    bossBar.setProgress(1);
                } else {
                    bossBar.setTitle(PetManager.levelExp.getTitle()
                            .replace("%PET%", PetManager.entityTypes.get(index).toString())
                            .replace("%LVL%", String.valueOf(Utils.calPetLvl(exp))));
                    if (lvl != 1)
                        bossBar.setProgress(((double)exp-Utils.calPetExp(lvl-1))/((double)Utils.calPetExp(lvl)-Utils.calPetExp(lvl-1)));
                    else
                        bossBar.setProgress(((double)exp/(double)20));
                }
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (player != null) {
                            Pets ps = getPets(player.getUniqueId());
                            if (ps.getExp().get(index) == exp) {
                                bossBarHashMap.remove(player.getUniqueId());
                                bossBar.removeAll();
                            }
                        } else {
                            bossBar.removeAll();
                        }
                    }
                }.runTaskLater(Main.getInstance(), 100);
            }
        }
        integers.put(index, exp);
        pets.put(uuid, new Pets(integers));
    }

    public static int getPetsExp(UUID uuid, int index) {
        return getPets(uuid).getExp().get(index);
    }

    public static List<Long> getClaimsRAM(UUID uuid) {
        return claimsByUUID.get(uuid);
    }

    public static List<String> getBannedRAM(UUID uuid) {
        return banned.get(uuid);
    }
    public static List<String> getTrustedRAM(UUID uuid) {
        return trusted.get(uuid);
    }
    public static Loc getClaimHomeRAM(UUID uuid) {
        return claimHomes.get(uuid);
    }

    public static boolean hasClaimHome(UUID uuid) {
        return claimHomes.containsKey(uuid);
    }

    public static void setClaimHome(UUID uuid, Loc loc) {
        claimHomes.remove(uuid);
        claimHomes.put(uuid, loc);
    }

    public static void removeClaimHome(UUID uuid) {
        claimHomes.remove(uuid);
    }

    public static List<Skill> getSkills(UUID uuid) {
        List<Skill> skills = new ArrayList<>();
        List<Group> groups = getGroupRAM(uuid);
        if (!groups.isEmpty()) {
            Group gr = groups.get(groups.size()-1);
            skills.addAll(GroupManager.getGroupObj(gr).getOnlyClass());
            for (Group group : groups) {
                skills.addAll(GroupManager.getGroupObj(group).getOpen());
            }
        }
        return skills;
    }

    public static Pets getPets(UUID uuid) {
        return pets.get(uuid);
    }

    public static void addClaim(UUID uuid, long chunkID) {
        List<Long> cs = null;
        if(claimsByUUID.get(uuid) == null) {
            cs = new ArrayList<>();
        } else {
            cs = claimsByUUID.get(uuid);
        }
        cs.add(chunkID);
        claimsByUUID.put(uuid, cs);
    }

    public static void removeClaim(UUID uuid, long chunkID) {
        List<Long> cs = null;
        if(claimsByUUID.get(uuid) == null) {
            cs = new ArrayList<>();
        } else {
            cs = claimsByUUID.get(uuid);
        }
        cs.remove(chunkID);
    }


    public static void addTrusted(UUID owner, UUID uuid) {
        List<String> trustedList = trusted.get(owner);
        trustedList.add(uuid.toString());
        trusted.put(owner, trustedList);
    }

    public static void removeTrust(UUID owner, UUID uuid) {
        List<String> trustedList = trusted.get(owner);
        trustedList.remove(uuid.toString());
        trusted.put(owner, trustedList);
    }

    public static void addBanned(UUID owner, UUID uuid) {
        List<String> bannedList = banned.get(owner);
        bannedList.add(uuid.toString());
        banned.put(owner, bannedList);
    }

    public static boolean addHome(String id, UUID uuid, Location location) {
        List<Home> hs = homes.get(uuid);
        for (Home h : hs) {
            if (h.getId().equals(id)) {
                return false;
            }
        }
        Home home = new Home(id, location.getWorld().getName(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        hs.add(home);
        homes.put(uuid, hs);
        return true;
    }

    public static boolean removeHome(String id, UUID uuid) {
        List<Home> hs = homes.get(uuid);

        for (Home h : hs) {
            if (h.getId().equals(id)) {
                hs.remove(h);
                homes.put(uuid, hs);
                return true;
            }
        }
        return false;
    }

    public static List<Home> getHomes(UUID uuid) {
        return homes.get(uuid);
    }

    public static Home getHome(String id, UUID uuid) {
        for (Home h : homes.get(uuid)) {
            if (h.getId().equals(id)) {
                return h;
            }
        }
        return null;
    }

    public static Home getHome(int id, UUID uuid) {
        if (homes.get(uuid).isEmpty())
            return null;
        else
            return homes.get(uuid).get(id);
    }

    public static void removeBanned(UUID owner, UUID uuid) {
        List<String> bannedList = banned.get(owner);
        bannedList.remove(uuid.toString());
        banned.put(owner, bannedList);
    }

    public static void addGroup(UUID uuid, Group group) {
        List<String> groupList = groups.get(uuid);
        groupList.add(group.toString());
        groups.put(uuid, groupList);
    }

    public static void addAllGroups(UUID uuid) {
        List<String> groupList = groups.get(uuid);
        for (Group g : Group.values()) {
            groupList.add(g.toString());
        }
        groups.put(uuid, groupList);
    }

    public static void removeGroup(UUID uuid, Group group) {
        List<String> groupList = groups.get(uuid);
        groupList.remove(group.toString());
        groups.put(uuid, groupList);
    }

    public static void removeAllGroups(UUID uuid) {
        List<String> groupList = groups.get(uuid);
        groupList.clear();
        groups.put(uuid, groupList);
    }

    public static Pet getPetType(UUID uuid) {
        String petString = playerDataConfig.getString("players." + uuid + ".pet.type");
        if(petString == null) return null;
        return Pet.getPet(petString);
    }

    public static void setPlaytime(UUID uuid, int[] time) {
        playtime.put(uuid, time);
    }

    public static UUID getUUID(String name) {
        return names.get(name);
    }

    public static String getName(UUID uuid) {
        for (String s : names.keySet()) {
            if (names.get(s).toString().equals(uuid.toString()))
                return s;
        }
        return null;
    }

    public static class Pets {
        private final HashMap<Integer, Integer> exp;

        public Pets(HashMap<Integer, Integer> exp) {
            this.exp = exp;
        }

        public HashMap<Integer, Integer> getExp() {
            return exp;
        }
    }

}