package at.gotzi.dungeonmanager.manager.file;

import at.gotzi.dungeonmanager.Main;
import at.gotzi.dungeonmanager.data.PlayerData;
import at.gotzi.dungeonmanager.objects.BossBarObj;
import at.gotzi.dungeonmanager.objects.groups.Group;
import at.gotzi.dungeonmanager.objects.groups.GroupObj;
import at.gotzi.dungeonmanager.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BasicConfigManager extends FileManager {

    private static YamlConfiguration basicConfig;

    private static DecimalFormat df = new DecimalFormat("0.00");
    private static List<String> scores = new ArrayList<>();
    private static String title;

    private static List<Integer> tasks = new ArrayList<>();
    public static BarStyle barStyle;

    public static List<UUID> noActionBar = new ArrayList<>();

    private int count;
    public static List<BossBarObj> bossBars = new ArrayList<>();
    private static BossBar currentBossBar = null;

    private static String header;
    private static String footer;

    private static String openWorld;
    private static String world;
    private static String resourceWorlds;

    private int period;
    private int sched;

    public BasicConfigManager() {
        super(YamlConfiguration.loadConfiguration(new File("plugins//DungeonManager//basic.yml")));
        basicConfig = YamlConfiguration.loadConfiguration(new File("plugins//DungeonManager//basic.yml"));
    }

    @Override
    public void initialize() {
        for (int i : tasks) {
            Bukkit.getScheduler().cancelTask(i);
        }

        List<String> s = new ArrayList<>();
        for (String str : getStringList("scoreboard.text")) {
            s.add(Utils.color(str));
        }
        title = getString("scoreboard.title");
        scores = s;

        this.period = getInt("news.settings.period");
        barStyle = BarStyle.valueOf(getString("news.settings.barStyle"));
        bossBars.clear();
        for (String b : getConfigSection("news.mode").getKeys(false)) {
            String title = getString("news.mode." + b + ".title");
            BarColor barColor = BarColor.valueOf(getString("news.mode." + b + ".barColor"));
            bossBars.add(new BossBarObj(title, barColor));
        }
        if (bossBars.isEmpty()) return;
        if (currentBossBar == null)
            currentBossBar = Bukkit.createBossBar(bossBars.get(0).getTitle(), bossBars.get(0).getBarColor(), barStyle);

        StringBuilder h = new StringBuilder();
        for (int i = 0; i < getStringList("tablist.header").size(); i++) {
            String s1 = Utils.color(getStringList("tablist.header").get(i));
            if (i == (getStringList("tablist.header").size()-1))  {
                h.append(s1);
            } else {
                h.append(s1).append("\n");
            }
        }
        header = h.toString();
        StringBuilder f = new StringBuilder();
        for (int i = 0; i < getStringList("tablist.footer").size(); i++) {
            String s1 = Utils.color(getStringList("tablist.footer").get(i));
            if (i == (getStringList("tablist.footer").size()-1))  {
                f.append(s1);
            } else {
                f.append(s1).append("\n");
            }
        }
        footer = f.toString();

        openWorld = getString("tablist.openworld");
        world = getString("tablist.world");
        resourceWorlds = getString("tablist.resourceworlds");

        this.startScheduler();
    }

    public void startScheduler() {
        sched = new BukkitRunnable() {
            @Override
            public void run() {
                boolean bool = false;
                if (!bossBars.isEmpty()) {
                    int index = count / period;
                    if (bossBars.size() > index) {
                        if (count % period == 0) {
                            if (index == 0) {
                                currentBossBar.setTitle(bossBars.get(0).getTitle());
                                currentBossBar.setColor(bossBars.get(0).getBarColor());
                            } else {
                                currentBossBar.setTitle(bossBars.get(index).getTitle());
                                currentBossBar.setColor(bossBars.get(index).getBarColor());
                            }
                            bool = true;
                        }
                    } else {
                        count = 0;
                        currentBossBar.setTitle(bossBars.get(0).getTitle());
                        currentBossBar.setColor(bossBars.get(0).getBarColor());
                        bool = true;
                    }
                }
                for (Player player : Bukkit.getOnlinePlayers()) {
                    setRightScoreboard(player);
                    setTabList(player);
                    if (!noActionBar.contains(player.getUniqueId()))
                        player.sendActionBar( "§3" + player.getAttribute(Attribute.GENERIC_ARMOR).getValue() + " Armor");
                    if (bool) {
                        currentBossBar.addPlayer(player);
                    }
                }
                count++;
            }
        }.runTaskTimer(Main.getInstance(), 20, 20).getTaskId();
    }

    public void cancelTask() {
        Bukkit.getScheduler().cancelTask(sched);
    }

    public static void loadNews(Player player) {
        if (currentBossBar == null) return;
        currentBossBar.addPlayer(player);
    }

    public static void unloadNews(Player player) {
        if (currentBossBar == null) return;
        currentBossBar.removePlayer(player);
    }

    public static void setTabList(Player player) {
        String w = "";
        int ping = player.getPing();
        int onlineP = Bukkit.getOnlinePlayers().size();
        int max = Bukkit.getMaxPlayers();

        if (player.getWorld().getName().equals("world")) {
            w = world;
        } else if (player.getWorld().getName().equals("openworld")) {
            w = openWorld;
        } else if (player.getWorld().getName().contains("resource")) {
            w = resourceWorlds;
        } else if (player.getWorld().getName().contains("dungeons//dungeoncache")) {
            String wo = player.getWorld().getName();
            wo = wo.split("//")[2];
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < wo.split("_").length; i++) {
                String p = wo.split("_")[i];
                if (i != (wo.split("_").length-1) && i != (wo.split("_").length-2)) {
                    sb.append(p).append("_");
                } else if  (i == (wo.split("_").length-2)) {
                    sb.append(p);
                }
            }
            w = Main.getInstance().getConfig().getString("dungeons." + sb + ".displayName");
        }
        String he = header.replace("%WORLD%", w)
                .replace("%PLAYER%", player.getName())
                .replace("%PING%", String.valueOf(ping))
                .replace("%ONP%", String.valueOf(onlineP))
                .replace("%MAXP%", String.valueOf(max));
        String fo = footer.replace("%WORLD%", w)
                .replace("%PLAYER%", player.getName())
                .replace("%PING%", String.valueOf(ping))
                .replace("%ONP%", String.valueOf(onlineP))
                .replace("%MAXP%", String.valueOf(max));
        ((CraftPlayer) player).setPlayerListHeaderFooter(he, fo);
    }

    public static void setRightScoreboard(Player player) {
        Scoreboard board = null;
        Objective objective = null;
        board = Bukkit.getScoreboardManager().getNewScoreboard();
        objective = board.registerNewObjective("DungeonMain", "abc");
        int minutes = PlayerData.getPlayTimeRAM(player.getUniqueId())[0];
        int hours = PlayerData.getPlayTimeRAM(player.getUniqueId())[1];
        List<Group> playerGroups = PlayerData.getGroupRAM(player.getUniqueId());
        GroupObj gs = null;
        String groupName = null;
        if(!playerGroups.isEmpty()) {
            gs = GroupManager.getGroupObj(playerGroups.get(playerGroups.size() - 1));
            groupName = ChatColor.stripColor(gs.getItemName());
        } else  {
            groupName = "None";
        }

        String balance = df.format(Main.getInstance().eco.getBalance(player));

        Team h = board.registerNewTeam("hours");
        Team m = board.registerNewTeam("minutes");
        Team cls = board.registerNewTeam("class");
        Team exp = board.registerNewTeam("exp");
        Team money = board.registerNewTeam("money");

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(title);

        double expNum = 0;

        Statement statement = null;
        try {
            statement = com.magmaguy.elitemobs.playerdata.PlayerData.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM PlayerData WHERE PlayerUUID = '" + player.getUniqueId().toString() + "';");
            String reply = resultSet.getString("Currency");
            resultSet.close();
            statement.close();
            expNum = Double.parseDouble(reply);
        } catch (Exception e) {
            expNum = 0.00;
        }

        int size = scores.size()-1;
        for (int i = 0; i < scores.size(); i++) {
            String score = scores.get(i);
            objective.getScore(score.replace("%MINS%", String.valueOf(minutes))
                    .replace("%HOURS%", String.valueOf(hours))
                    .replace("%CLASS%", String.valueOf(groupName))
                    .replace("%EXP%", String.valueOf(expNum))
                    .replace("%MONEY%", String.valueOf(balance))).setScore(size-i);
        }

        /*
        h.addEntry("§1§r");
        m.addEntry("§0§r");
        cls.addEntry("§2§r");
        exp.addEntry("§3§r");
        money.addEntry("§4§r");
        h.setPrefix(String.valueOf(hours)); //String.valueOf(hours)
        m.setPrefix(String.valueOf(minutes));
        cls.setPrefix(String.valueOf(groupName));
        Statement statement = null;
        try {
            statement = com.magmaguy.elitemobs.playerdata.PlayerData.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM PlayerData WHERE PlayerUUID = '" + player.getUniqueId().toString() + "';");
            String reply = resultSet.getString("Currency");
            resultSet.close();
            statement.close();
            exp.setPrefix(String.valueOf(Double.parseDouble(reply)));
        } catch (Exception e) {
            exp.setPrefix(String.valueOf(0.00));
        }
        money.setPrefix(String.valueOf(balance));

         */

        player.setScoreboard(board);
    }

    public void updateScoreboard(Player player) {
        Scoreboard board = player.getScoreboard();
        int minutes = PlayerData.getPlayTimeRAM(player.getUniqueId())[0];
        int hours = PlayerData.getPlayTimeRAM(player.getUniqueId())[1];
        List<Group> playerGroups = PlayerData.getGroupRAM(player.getUniqueId());
        GroupObj gs = null;
        String groupName = null;
        if(!playerGroups.isEmpty()) {
            gs = GroupManager.getGroupObj(playerGroups.get(playerGroups.size() - 1));
            groupName = ChatColor.stripColor(gs.getItemName());
        } else  {
            groupName = "None";
        }
        String balance = df.format(Main.getInstance().eco.getBalance(player));

        Team h = board.getTeam("hours");
        Team m = board.getTeam("minutes");
        Team cls = board.getTeam("class");
        Team exp = board.getTeam("exp");
        Team money = board.getTeam("money");
        h.setPrefix(String.valueOf(hours));
        m.setPrefix(String.valueOf(minutes));
        cls.setPrefix(String.valueOf(groupName));
        Statement statement = null;
        try {
            statement = com.magmaguy.elitemobs.playerdata.PlayerData.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM PlayerData WHERE PlayerUUID = '" + player.getUniqueId().toString() + "';");
            String reply = resultSet.getString("Currency");
            resultSet.close();
            statement.close();
            exp.setPrefix(String.valueOf(Double.parseDouble(reply)));
        } catch (Exception e) {
            exp.setPrefix(String.valueOf(0.00));
        }

        money.setPrefix(String.valueOf(balance));

        player.setScoreboard(board);
    }

    public static void setBasicConfig(String path, Object obj) {
        basicConfig.set(path, obj);
        try {
            basicConfig.save(new File("plugins//DungeonManager//basic.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void removeNews(int index) {
        for (String m : basicConfig.getConfigurationSection("news.mode").getKeys(false)) {
            basicConfig.set("news.mode." + m + ".title", null);
            basicConfig.set("news.mode." + m + ".barColor", null);
            basicConfig.set("news.mode." + m, null);
        }

        for (BossBarObj bossBarObj : bossBars) {
            int i = bossBars.indexOf(bossBarObj)+1;
            basicConfig.set("news.mode." + i + ".title", bossBarObj.getTitle());
            basicConfig.set("news.mode." + i + ".barColor", bossBarObj.getBarColor().toString());
        }
        try {
            basicConfig.save(new File("plugins//DungeonManager//basic.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updateCurrent(BossBarObj bossBarObj) {
        if (bossBarObj.getTitle().equals(currentBossBar.getTitle()) && bossBarObj.getBarColor() == currentBossBar.getColor()) {
            currentBossBar.setTitle(bossBarObj.getTitle());
            currentBossBar.setColor(bossBarObj.getBarColor());
        }

    }

    public static void setNews(BossBarObj bossBarObj) {
        currentBossBar.setTitle(bossBarObj.getTitle());
        currentBossBar.setColor(bossBarObj.getBarColor());
    }

}
