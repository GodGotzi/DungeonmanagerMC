// This class was created by Wireless


package at.gotzi.dungeonmanager.manager.file;

import at.gotzi.dungeonmanager.Main;
import at.gotzi.dungeonmanager.data.PlayerData;
import at.gotzi.dungeonmanager.manager.InventoryManager;
import at.gotzi.dungeonmanager.objects.advacements.CustomAdvancement;
import at.gotzi.dungeonmanager.objects.enchantments.Glow;
import at.gotzi.dungeonmanager.objects.enums.Error;
import at.gotzi.dungeonmanager.objects.groups.Group;
import at.gotzi.dungeonmanager.objects.playerutils.Title;
import at.gotzi.dungeonmanager.objects.skills.SkillObj;
import at.gotzi.dungeonmanager.objects.skills.SkillType;
import at.gotzi.dungeonmanager.objects.skills.Skills;
import at.gotzi.dungeonmanager.objects.groups.GroupObj;
import at.gotzi.dungeonmanager.objects.skills.Skill;
import at.gotzi.dungeonmanager.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GroupManager extends FileManager {

    private static HashMap<Group, GroupObj> groups = new HashMap<>();
    public static List<Group> freeGroups = new ArrayList<>();
    private static String guiNameSwitch = null;
    private static String guiNameMain = null;
    private static String guiNameSkills = null;
    public static int fadeIn = 0;
    public static int fadeOut = 0;
    public static int stay = 0;
    public static String npc;

    private static YamlConfiguration groupConfig;
    
    public GroupManager() {
        super("classes", YamlConfiguration.loadConfiguration(new File("plugins//DungeonManager//classes.yml")));
        groupConfig = YamlConfiguration.loadConfiguration(new File("plugins//DungeonManager//classes.yml"));
    }

    @Override
    public void initialize() {
        //basic
        List<Group> frGroups = new ArrayList<>();
        for (String str : groupConfig.getStringList("freeClasses")) {
            frGroups.add(Group.getGroup(str));
        }
        freeGroups.addAll(frGroups);
        guiNameSwitch = groupConfig.getString("classGui.switch.title");
        guiNameMain = groupConfig.getString("classGui.main.title");
        guiNameSkills = groupConfig.getString("classGui.skillInfo.title");
        npc = groupConfig.getString("classGui.npc");

        fadeIn = groupConfig.getInt("msgSettings.fadeIn");
        fadeOut = groupConfig.getInt("msgSettings.fadeOut");
        stay = groupConfig.getInt("msgSettings.stay");

        for (Group group : Group.values()) {
            List<Skill> openSkills = new ArrayList<>();
            for (String skill : getStringList(group, "openSkills")) {
                Skills sk = Skills.getSkill(skill);
                if (sk != null) {
                    if(sk.isBool()) {
                        int strength = getInt(group, sk.toString());
                        if(strength == 0) Utils.callError(groupConfig.getName() + "." + group + "." + skill, Error.MaybeWrong);
                        openSkills.add(new Skill(sk, strength));
                    } else {
                        openSkills.add(new Skill(sk));
                    }
                } else
                    Utils.callError(groupConfig.getName() + skill, Error.FileSyntax);
            }

            List<Skill> onlyClassSkills = new ArrayList<>();
            for (String skill : getStringList(group, "onlyClassSkills")) {
                Skills sk = Skills.getSkill(skill);
                if (sk != null) {
                    if(sk.isBool()) {
                        int strength = getInt(group, "class" + sk);
                        if(strength == 0) Utils.callError(groupConfig.getName() + "." + group + "." + skill, Error.MaybeWrong);
                        onlyClassSkills.add(new Skill(sk, strength));
                    } else {
                        onlyClassSkills.add(new Skill(sk));
                    }
                } else
                    Utils.callError(groupConfig.getName() + skill, Error.FileSyntax);
            }
            String itemName = Utils.color(getString(group, "item.itemName"));
            List<String> lore = new ArrayList<>();
            for (String l : getStringList(group, "item.lore")) {
                lore.add(Utils.color(l));
            }
            int modelData = getInt(group, "item.modelDataId");

            ItemStack itemStack = new ItemStack(Material.PAPER);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setCustomModelData(modelData);
            itemStack.setItemMeta(itemMeta);
            int slot = getInt(group, "item.slot");

            int cost = getInt(group, "cost");
            String arrangement = getStringNoColor(group, "arrangement");
            //congrats Title
            String msg = getString(group, "congratsTitle.msg");
            String subMsg = getString(group, "congratsTitle.subMsg");
            Title title = new Title(msg, subMsg);

            //advancement
            String advMsg = getString(group, "advancement.msg");
            String desc = getString(group, "advancement.description");
            boolean chat = getBoolean(group, "advancement.chat");
            String background = getStringNoColor(group, "advancement.background");
            float x = (float) getDouble(group, "advancement.x");
            float y = (float) getDouble(group, "advancement.y");

            GroupObj groupObj = new GroupObj(group, openSkills, onlyClassSkills, getNeededGroups(group), getNextGroups(group), itemName, lore, modelData, cost, arrangement, x, y, slot, new CustomAdvancement(itemStack, advMsg, desc, chat, background), title);
            groups.put(group, groupObj);
        }
    }

    public static GroupObj getGroupObj(Group group) {
        return groups.get(group);
    }

    public static boolean openGUIMain(Player player) {
        List<Group> playerGroups = PlayerData.getGroupRAM(player.getUniqueId());
        GroupObj gs = null;
        if(!playerGroups.isEmpty()) {
            gs = groups.get(playerGroups.get(playerGroups.size() - 1));
        }

        GroupObj finalGs = gs;
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            Inventory inventory = Bukkit.createInventory(null, 9 * 5, guiNameMain);
            List<Integer> items = new ArrayList<>();
            for (String item : groupConfig.getConfigurationSection("classGui.main.items.").getKeys(false)) {
                String itemName = Utils.color(groupConfig.getString("classGui.main.items." + item + ".itemName"));
                Material material = Material.getMaterial(groupConfig.getString("classGui.main.items." + item + ".material"));
                int slot = groupConfig.getInt("classGui.main.items." + item + ".slot");
                items.add(slot);
                int modelDataId = groupConfig.getInt("classGui.main.items." + "skillsItem" + ".modelDataId");
                List<String> lore = new ArrayList<>();
                List<SkillObj> skills = new ArrayList<>();

                if (finalGs != null) {
                    for (Skill skill : finalGs.getOnlyClass()) {
                        SkillObj skillObj = SkillManager.skillItems.get(skill.getSkills());
                        skills.add(skillObj.setDisplayName(skillObj.getDisplayName().replace("%AMOUNT%",
                                String.valueOf(skill.getStrength()))));
                    }
                }

                if (item.equals("skillsItem")) {
                    for (Group group : playerGroups) {
                        GroupObj groupObj = groups.get(group);
                        for (Skill skill : groupObj.getOpen()) {
                            SkillObj skillObj = SkillManager.skillItems.get(skill.getSkills());
                            skills.add(skillObj.setDisplayName(skillObj.getDisplayName().replace("%AMOUNT%",
                                    String.valueOf(skill.getStrength()))));
                        }
                    }
                    for (String l : groupConfig.getStringList("classGui.main.items." + "skillsItem" + ".lore")) {
                        l = Utils.color(l);
                        if (l.contains("%SKILLS...%")) {
                            List<Skills> skills1 = new ArrayList<>();
                            for (SkillObj skillObj : skills) {
                                if (skillObj == null) return;
                                if (skillObj.getSkills().isBool() || !skillObj.getSkills().isBool() && !skills1.contains(skillObj.getSkills())) {
                                    lore.add(skillObj.getDisplayName());
                                    skills1.add(skillObj.getSkills());
                                }
                            }
                        } else {
                            lore.add(l);
                        }
                    }
                }

                ItemStack skillItem = new ItemStack(material);
                ItemMeta itemMeta = skillItem.getItemMeta();
                itemMeta.setLore(lore);
                itemMeta.setCustomModelData(modelDataId);
                itemMeta.setDisplayName(itemName);
                skillItem.setItemMeta(itemMeta);

                inventory.setItem(slot, skillItem);
            }

            if(Main.fillup) {
                for (int i = 0; i < (9*5); i++) {
                    if(!items.contains(i)) {
                        inventory.setItem(i, Main.fillUpItemStack);
                    }
                }
            }

            Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                InventoryManager.registerInv(inventory.hashCode(), "GROUPMAIN");
                player.openInventory(inventory);
            });
        });
        return true;
    }

    public static void openGuiSkills(Player player, int page) {
        List<Group> playerGroups = PlayerData.getGroupRAM(player.getUniqueId());
        GroupObj gs = null;
        if (!playerGroups.isEmpty()) {
            gs = groups.get(playerGroups.get(playerGroups.size() - 1));
        }

        GroupObj finalGs = gs;
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            Inventory inventory = Bukkit.createInventory(null, 9*5, guiNameSkills);
            List<Integer> items = new ArrayList<>();
            List<Skill> sks = PlayerData.getSkills(player.getUniqueId());
            List<Skills> invSk = new ArrayList<>();

            int count = (page*23)-23;
            for (int i = 10; i <= 34; i++) {
                if (i % 9 == 0 || (i + 1) % 9 == 0) {
                } else {
                    if (count < sks.size()) {
                        boolean bool = true;
                        while(bool) {
                            if (count < sks.size()) {
                                Skill skill = sks.get(count);
                                if (skill.getSkills().isBool() || !skill.getSkills().isBool() && !invSk.contains(skill.getSkills())) {
                                    SkillObj skillObj = SkillManager.skillItems.get(skill.getSkills());
                                    ItemStack itemStack = skillObj.getItemStack();
                                    ItemMeta itemMeta = itemStack.getItemMeta();
                                    itemMeta.setDisplayName(itemMeta.getDisplayName().replace("%AMOUNT%", String.valueOf(skill.getStrength())));
                                    itemStack.setItemMeta(itemMeta);
                                    inventory.setItem(i, itemStack);
                                    sks.remove(skill);
                                    items.add(i);
                                    invSk.add(skill.getSkills());
                                    bool = false;
                                } else {
                                    items.add(i);
                                    sks.remove(skill);
                                }
                            } else {
                                bool = false;
                            }
                        }
                    } else {
                        items.add(i);
                    }
                }
            }

            for (String item : groupConfig.getConfigurationSection("classGui.skillInfo.items").getKeys(false)) {
                if (invSk.size() == 21 || invSk.size() != 21 && !item.equals("nextPage")) {
                    String itemName = Utils.color(groupConfig.getString("classGui.skillInfo.items." + item + ".itemName"));
                    Material material = Material.getMaterial(groupConfig.getString("classGui.skillInfo.items." + item + ".material"));
                    int slot = groupConfig.getInt("classGui.skillInfo.items." + item + ".slot");
                    int modelDataId = groupConfig.getInt("classGui.skillInfo.items." + item + ".modelDataId");
                    List<String> lore = new ArrayList<>();
                    for (String l : groupConfig.getStringList("classGui.skillInfo.items." + item + ".lore")) {
                        lore.add(Utils.color(l));
                    }

                    ItemStack skillItem = new ItemStack(material);
                    ItemMeta itemMeta = skillItem.getItemMeta();
                    itemMeta.setLore(lore);
                    itemMeta.setCustomModelData(modelDataId);
                    itemMeta.setDisplayName(itemName);
                    skillItem.setItemMeta(itemMeta);

                    inventory.setItem(slot, skillItem);
                    items.add(slot);
                }
            }

            if(Main.fillup) {
                for (int i = 0; i < (9*5); i++) {
                    if(!items.contains(i)) {
                        inventory.setItem(i, Main.fillUpItemStack);
                    }
                }
            }

            Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                InventoryManager.registerInv(inventory.hashCode(), "GROUPSKILLS");
                player.openInventory(inventory);
            });
        });
    }

    public static void openGuiGroupInfo(Player player, GroupObj groupObj) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            Inventory inventory = Bukkit.createInventory(null, 9*5, guiNameSkills);
            List<Skill> sks = new ArrayList<>();
            List<Integer> items = new ArrayList<>();
            sks.addAll(groupObj.getOnlyClass());
            sks.addAll(groupObj.getOpen());

            int count = 0;
            for (int i = 10; i <= 34; i++) {
                if (i == 10 || i == 11 || i == 12 || i == 19 || i == 20 || i == 21 || i == 28 || i == 29 || i == 30) {
                    if (count < sks.size()) {
                        Skill skill = sks.get(count);
                        SkillObj skillObj = SkillManager.skillItems.get(skill.getSkills());
                        ItemStack itemStack = skillObj.getItemStack();
                        ItemMeta itemMeta = itemStack.getItemMeta();
                        itemMeta.setDisplayName(itemMeta.getDisplayName().replace("%AMOUNT%", String.valueOf(skill.getStrength())));
                        itemStack.setItemMeta(itemMeta);

                        inventory.setItem(i, itemStack);
                        items.add(i);
                        count++;
                    } else {
                        items.add(i);
                    }
                }
            }

            for (String item : groupConfig.getConfigurationSection("classGui.buyInfo.items").getKeys(false)) {
                String itemName = Utils.color(groupConfig.getString("classGui.buyInfo.items." + item + ".itemName"));
                Material material = Material.getMaterial(groupConfig.getString("classGui.buyInfo.items." + item + ".material"));
                int slot = groupConfig.getInt("classGui.buyInfo.items." + item + ".slot");
                int modelDataId = groupConfig.getInt("classGui.buyInfo.items." + item + ".modelDataId");
                if (item.equals("buyItem")) {
                    modelDataId = groupObj.getModelData();
                }
                boolean glow = groupConfig.getBoolean("classGui.buyInfo.items." + item + ".glow");
                List<String> lore = new ArrayList<>();
                for (String l : groupConfig.getStringList("classGui.buyInfo.items." + item + ".lore")) {
                    lore.add(Utils.color(l));
                }

                ItemStack skillItem = new ItemStack(material);
                ItemMeta itemMeta = skillItem.getItemMeta();
                itemMeta.setLore(lore);
                itemMeta.setCustomModelData(modelDataId);
                itemMeta.setDisplayName(itemName);
                if(glow)
                    itemMeta.addEnchant(Glow.generateEnchant(), 1, true);
                for (String enchant : groupConfig.getStringList("classGui.buyInfo.items." + item + ".enchantments")) {
                    Enchantment enchantment = Enchantment.getByName(enchant);
                    if(enchantment == null) {
                        lore.add("§7" + enchant);
                    } else
                        itemMeta.addEnchant(enchantment, 1, false);
                }
                skillItem.setItemMeta(itemMeta);

                inventory.setItem(slot, skillItem);
                items.add(slot);
            }

            if(Main.fillup) {
                for (int i = 0; i < (9*5); i++) {
                    if(!items.contains(i)) {
                        inventory.setItem(i, Main.fillUpItemStack);
                    }
                }
            }

            Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                player.openInventory(inventory);
                InventoryManager.registerInv(inventory.hashCode(), "GROUPBUY");
            });
        });
    }

    private static boolean hasClassUnlocked(List<Group> playerGroups, GroupObj groupObj) {
        return playerGroups.containsAll(groupObj.getNeededGroups());
    }

    public static boolean openGUISwitch(Player player) {
        List<Group> playerGroups = PlayerData.getGroupRAM(player.getUniqueId());
        List<Integer> items = new ArrayList<>();
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            Inventory inventory = Bukkit.createInventory(null, 9 * 6, guiNameSwitch);
            for (GroupObj groupObj : groups.values()) {
                ItemStack itemStack = new ItemStack(Material.PAPER);
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName(groupObj.getItemName());
                List<String> l = new ArrayList<>();
                if (freeGroups.contains(groupObj.getGroup()) || hasClassUnlocked(playerGroups, groupObj) || playerGroups.contains(groupObj.getGroup())) {
                    itemMeta.setCustomModelData(groupObj.getModelData());
                    for (String s : groupObj.getLore()) {
                        if (!s.contains("%LOCK%"))
                            l.add(s.replace("%LOCK%", "Unlocked").replace("%COST%", String.valueOf(groupObj.getCost())));
                    }
                } else {
                    itemMeta.setCustomModelData(groupObj.getModelData() + 100);
                    for (String s : groupObj.getLore()) {
                        if (!s.contains("%COST%"))
                            l.add(s.replace("%LOCK%", "Unlock"));
                    }
                }
                if (playerGroups.contains(groupObj.getGroup()))
                    itemMeta.addEnchant(Glow.generateEnchant(), 1, true);
                if (!playerGroups.isEmpty()) {
                    if (groupObj.getGroup() == playerGroups.get(playerGroups.size()-1)) {
                        l.add("§7Selected");
                    }
                }

                itemMeta.setLore(l);
                itemStack.setItemMeta(itemMeta);
                inventory.setItem(groupObj.getSlot(), itemStack);
                items.add(groupObj.getSlot());
            }

            for (String item : groupConfig.getConfigurationSection("classGui.switch.items").getKeys(false)) {
                String itemName = Utils.color(groupConfig.getString("classGui.switch.items." + item + ".itemName"));
                Material material = Material.getMaterial(groupConfig.getString("classGui.switch.items." + item + ".material"));
                int slot = groupConfig.getInt("classGui.switch.items." + item + ".slot");
                int modelDataId = groupConfig.getInt("classGui.switch.items." + item + ".modelDataId");
                boolean glow = groupConfig.getBoolean("classGui.switch.items." + item + ".glow");
                List<String> lore = new ArrayList<>();
                for (String l : groupConfig.getStringList("classGui.switch.items." + item + ".lore")) {
                    lore.add(Utils.color(l));
                }

                ItemStack skillItem = new ItemStack(material);
                ItemMeta itemMeta = skillItem.getItemMeta();
                itemMeta.setLore(lore);
                itemMeta.setCustomModelData(modelDataId);
                itemMeta.setDisplayName(itemName);
                if (glow)
                    itemMeta.addEnchant(Glow.generateEnchant(), 1, true);
                for (String enchant : groupConfig.getStringList("classGui.switch.items." + item + ".enchantments")) {
                    Enchantment enchantment = Enchantment.getByName(enchant);
                    if (enchantment == null) {
                        lore.add("§7" + enchant);
                    } else
                        itemMeta.addEnchant(enchantment, 1, false);
                }
                skillItem.setItemMeta(itemMeta);

                inventory.setItem(slot, skillItem);
                items.add(slot);
            }

            if (Main.fillup) {
                for (int i = 0; i < (9 * 6); i++) {
                    if (!items.contains(i)) {
                        inventory.setItem(i, Main.fillUpItemStack);
                    }
                }
            }

            Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                InventoryManager.registerInv(inventory.hashCode(), "GROUPSWITCH");
                player.openInventory(inventory);
            });
        });
        return false;
    }

    public static String getAction(String inv, String item) {
        for (String i : groupConfig.getConfigurationSection("classGui." + getSection(inv) + ".items").getKeys(false)) {
            String itemName = Utils.color(groupConfig.getString("classGui." + getSection(inv) + ".items." + i + ".itemName"));
            if(itemName.equals(item)) return i;
        }
        return null;
    }
    
    public static String getSection(String inv) {
        switch (inv) {
            case "GROUPMAIN":
                return "main";
            case "GROUPSWITCH":
                return "switch";
            case "GROUPSKILLS":
                return "skillInfo";
            case "GROUPBUY":
                return "buyInfo";
        }
        return null;
    }
    
    private static List<Group> getNeededGroups(Group group) {
        List<Group> gs = new ArrayList<>();
        for(String str : groupConfig.getStringList("classes." + group.toString() + "." + "neededClasses")) {
            gs.add(Group.getGroup(str));
        }
        return gs;
    }

    private static List<Group> getNextGroups(Group group) {
        List<Group> gs = new ArrayList<>();
        for(String str : groupConfig.getStringList("classes." + group.toString() + "." + "nextClasses")) {
            gs.add(Group.getGroup(str));
        }
        return gs;
    }

    public static String getSkillGroups(Skills skill) {
        List<GroupObj> gps = new ArrayList<>();
        for (GroupObj groupObj : groups.values()) {
            List<Skill> skills = new ArrayList<>();
            skills.addAll(groupObj.getOpen());
            skills.addAll(groupObj.getOnlyClass());
            for (Skill sk : skills) {
                if (sk.getSkills() == skill)
                    gps.add(groupObj);
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < gps.size(); i++) {
            GroupObj groupObj = gps.get(i);
            if (i == gps.size()-1) {
                stringBuilder.append(ChatColor.stripColor(groupObj.getItemName()));
            } else {
                stringBuilder.append(ChatColor.stripColor(groupObj.getItemName()) + " or ");
            }
        }

        return stringBuilder.toString();
    }
    
}