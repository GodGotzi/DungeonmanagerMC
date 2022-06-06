// This class was created by Wireless


package at.gotzi.dungeonmanager.manager.dungeon;

import at.gotzi.dungeonmanager.Main;
import at.gotzi.dungeonmanager.listener.playerlistener.MoveListener;
import at.gotzi.dungeonmanager.manager.world.DungeonWorldManager;
import at.gotzi.dungeonmanager.utils.Utils;
import com.gmail.filoghost.holographicdisplays.commands.CommandValidator;
import com.gmail.filoghost.holographicdisplays.disk.HologramDatabase;
import com.gmail.filoghost.holographicdisplays.exception.CommandException;
import com.gmail.filoghost.holographicdisplays.object.NamedHologram;
import com.gmail.filoghost.holographicdisplays.object.NamedHologramManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class DungeonSetterTimer {

    private Main main;
    private static String currentDungeon = Utils.getDungeonList().get(0);
    private static int minutes = 0;
    private static List<String> dungeons = new ArrayList<>();
    private static final String id = "dungeonportalholo";
    private DungeonWorldManager dungeonWorldManager;
    private MoveListener portalListener;
    private int period;
    private static int asyncShed;
    public static boolean runAsyncTasks = true;
    public static boolean isStarting = true;
    private int amountCopy;


    private boolean prepare = false;

    public DungeonSetterTimer(Main main, DungeonWorldManager dungeonWorldManager) {
        this.main = main;
        this.dungeonWorldManager = dungeonWorldManager;
    }

    public void timerStart() {
        period = Utils.period();
        int size = Utils.getDungeonList().size();
        dungeons = Utils.getDungeonList();
        String holoGrammLoc = main.getConfig().getString("hologram.location");
        String world = "world";
        Location location = null;

        //GHoloMain.getInstance().getHoloManager().loadHolos();
        //GHoloMain.getInstance().getHoloManager().getSpawnManager().spawn();

        if(holoGrammLoc == null) {
            Bukkit.getLogger().info("No Hologram location!");
        } else {
            double x = Double.parseDouble(holoGrammLoc.split(";")[0]);
            double y = Double.parseDouble(holoGrammLoc.split(";")[1]);
            double z = Double.parseDouble(holoGrammLoc.split(";")[2]);
            float yaw = Float.parseFloat(holoGrammLoc.split(";")[3]);
            float pitch = Float.parseFloat(holoGrammLoc.split(";")[4]);
            assert world != null;
            location = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
        }
        this.setHologram(location);

        amountCopy = main.getConfig().getInt("amountWorldFoldersCopied");
        Bukkit.getScheduler().runTaskAsynchronously(main, new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < amountCopy; i++) {
                    if(runAsyncTasks) {
                        dungeonWorldManager.copyWorld();
                    } else {
                        main.print("Tasks closed!");
                        dungeonWorldManager.deleteWorldFolders();
                        return;
                    }
                }
                main.print("-------> Server started! <-------");
                isStarting = false;
            }
        }).getTaskId();

        Location finalLocation = location;
        asyncShed = Bukkit.getScheduler().scheduleAsyncRepeatingTask(main, () -> {
            minutes++;
            this.updateHologram(finalLocation);
            if(minutes == period) {
                if (dungeons.indexOf(currentDungeon)+1 == size) {
                    currentDungeon = dungeons.get(0);
                } else {
                    currentDungeon = dungeons.get(dungeons.indexOf(currentDungeon)+1);
                }
                dungeonWorldManager.deleteWorldFoldersWhileRun(currentDungeon);
                prepare = false;
                minutes = 0;
            } else if (((double)minutes/(double) period) >= 0.9) {
                if (!prepare)
                    this.prepareDungeon();
            }
        }, 0, 1200);
    }

    public void prepareDungeon() {
        this.prepare = true;
        String dungeon = null;
        if (dungeons.indexOf(currentDungeon)+1 == dungeons.size()) {
            dungeon = dungeons.get(0);
        } else {
            dungeon = dungeons.get(dungeons.indexOf(currentDungeon)+1);
        }
        for (int i = 0; i < this.amountCopy; i++) {
            dungeonWorldManager.copyWorld(dungeon);
        }
    }

    public void setHologram(Location location) {
        List<String> descColorCodes = new ArrayList<>();
        int hours;
        int min;

        hours = (period - minutes)/60;
        min = (int)(period - minutes) - (((int)(period - minutes)/60) * 60);
        for (String d : main.getConfig().getStringList("dungeons." + currentDungeon + ".desc")) {
            descColorCodes.add(Utils.color(d).replace("%TIME%", hours + " h " + (min+1) + " m " ));
        }

        NamedHologram hologram = new NamedHologram(location, "dungeonportalholo");
        for (String s : descColorCodes) {
            hologram.appendTextLine(s);
        }
        NamedHologramManager.addHologram(hologram);
        hologram.refreshAll();
        HologramDatabase.saveHologram(hologram);
    }

    public void updateHologram(Location location) {
        List<String> descColorCodes = new ArrayList<>();
        int hours;
        int min;

        hours = (period - minutes)/60;
        min = (int)(period - minutes) - (((int)(period - minutes)/60) * 60);
        for (String d : main.getConfig().getStringList("dungeons." + currentDungeon + ".desc")) {
            descColorCodes.add(Utils.color(d).replace("%TIME%", hours + " h " + (min+1) + " m " ));
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                NamedHologram hologram = null;
                try {
                    hologram = CommandValidator.getNamedHologram("dungeonportalholo");
                } catch (CommandException e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < descColorCodes.size(); i++) {
                    hologram.removeLine(0);
                }

                for (String s : descColorCodes) {
                    hologram.appendTextLine(s);
                }
                hologram.refreshAll();
                HologramDatabase.saveHologram(hologram);
            }
        }.runTask(Main.getInstance());
    }

    public static int getAsyncShed() {
        return asyncShed;
    }

    public static String getCurrentDungeon() {
        return currentDungeon;
    }
}