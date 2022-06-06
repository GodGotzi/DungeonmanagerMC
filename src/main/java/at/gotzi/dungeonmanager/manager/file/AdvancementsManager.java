package at.gotzi.dungeonmanager.manager.file;

import at.gotzi.dungeonmanager.Main;
import at.gotzi.dungeonmanager.objects.advacements.CustomAdvancement;
import at.gotzi.dungeonmanager.objects.groups.Group;
import at.gotzi.dungeonmanager.objects.groups.GroupObj;
import at.gotzi.dungeonmanager.utils.Utils;
import eu.endercentral.crazy_advancements.Advancement;
import eu.endercentral.crazy_advancements.AdvancementDisplay;
import eu.endercentral.crazy_advancements.AdvancementVisibility;
import eu.endercentral.crazy_advancements.NameKey;
import eu.endercentral.crazy_advancements.manager.AdvancementManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdvancementsManager {

    private static AdvancementManager manager;
    private static List<Advancement> advancements = new ArrayList<>();
    private static HashMap<Group, Advancement> groupAdvancement = new HashMap<>();
    private int counter = 0;
    
    private static YamlConfiguration advConfig;

    public void initialize() {
        manager = new AdvancementManager();
        advConfig = YamlConfiguration.loadConfiguration(new File("plugins//DungeonManager//advancements.yml"));

        AdvancementDisplay rootDisplay = new AdvancementDisplay(Material.AMETHYST_SHARD, Utils.color(advConfig.getString("rootAdvancement.advancement.title")), Utils.color(advConfig.getString("rootAdvancement.advancement.description")), AdvancementDisplay.AdvancementFrame.TASK, false, false, AdvancementVisibility.ALWAYS);
        rootDisplay.setBackgroundTexture("textures/block/obsidian.png");
        Advancement root = new Advancement(null, new NameKey("class", "root"), rootDisplay);
        advancements.add(root);

        for (Group group : GroupManager.freeGroups) {
            GroupObj groupObj = GroupManager.getGroupObj(group);
            CustomAdvancement customAdvancement = groupObj.getCustomAdvancement();
            AdvancementDisplay display = new AdvancementDisplay(customAdvancement.getPic(),
                    customAdvancement.getMsg(),
                    customAdvancement.getDescription(),
                    AdvancementDisplay.AdvancementFrame.GOAL,
                    true, false,
                    AdvancementVisibility.ALWAYS);
            display.setCoordinates(groupObj.getX(), groupObj.getY());
            Advancement child = new Advancement(root, new NameKey("class", "classes" + groupObj.getGroup().toString() + this.counter), display);
            groupAdvancement.remove(group);
            groupAdvancement.put(group, child);

            advancements.add(child);
            this.counter++;
            this.loopAdvancements(groupObj, child);
        }

        Advancement[] advs = new Advancement[advancements.size()];
        advs = advancements.toArray(advs);
        manager.addAdvancement(advs);
        Utils.info("Advancements loaded!");
    }

    public static void reload() {
        for (Player all : Bukkit.getOnlinePlayers()) {
            manager.addPlayer(all);
            manager.grantAdvancement(all, advancements.get(0));
            Advancement[] advs = new Advancement[advancements.size()];
            advs = advancements.toArray(advs);
            manager.loadProgress(all, advs);
        }
    }

    public void loopAdvancements(GroupObj g, Advancement parent) {
        for (Group group : g.getNextGroups()) {
            GroupObj groupObj = GroupManager.getGroupObj(group);
            CustomAdvancement customAdvancement = groupObj.getCustomAdvancement();
            AdvancementDisplay display = new AdvancementDisplay(customAdvancement.getPic(),
                    customAdvancement.getMsg(),
                    customAdvancement.getDescription(),
                    AdvancementDisplay.AdvancementFrame.GOAL,
                    true, false,
                    AdvancementVisibility.ALWAYS);
            display.setCoordinates(groupObj.getX(), groupObj.getY());
            Advancement child = new Advancement(parent, new NameKey("class", "classes" + groupObj.getGroup().toString() + counter), display);
            groupAdvancement.remove(group);
            groupAdvancement.put(group, child);
            advancements.add(child);
            counter++;
            this.loopAdvancements(groupObj, child);
        }
    }

    public static void load(Player player) {
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
            manager.addPlayer(player);
            manager.grantAdvancement(player.getUniqueId(), advancements.get(0));
            Advancement[] advs = new Advancement[advancements.size()];
            advs = advancements.toArray(advs);
            manager.loadProgress(player, advs);
        }, 20);
    }

    public static void addPlayer(Player player) {
        manager.addPlayer(player);
    }

    public static void grantAdvancement(Player player, Group group) {
        manager.grantAdvancement(player.getUniqueId(), groupAdvancement.get(group));
    }

    public static void revokeAdvancement(Player player, Group group) {
        manager.revokeAdvancement(player.getUniqueId(), groupAdvancement.get(group));
    }


    public static void saveProgress(Player player) {
        manager.saveProgress(player.getUniqueId(), "class");
    }
}
