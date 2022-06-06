package at.gotzi.dungeonmanager.commands.AdminCommands;

import at.gotzi.dungeonmanager.Main;
import at.gotzi.dungeonmanager.commands.PortalCommand;
import at.gotzi.dungeonmanager.data.PlayerData;
import at.gotzi.dungeonmanager.manager.file.*;
import at.gotzi.dungeonmanager.manager.world.ClaimManager;
import at.gotzi.dungeonmanager.manager.world.ResourceWorldsManager;
import at.gotzi.dungeonmanager.objects.BossBarObj;
import at.gotzi.dungeonmanager.objects.groups.Group;
import at.gotzi.dungeonmanager.utils.Locations;
import at.gotzi.dungeonmanager.utils.Messages;
import at.gotzi.dungeonmanager.utils.Utils;
import com.gamingmesh.jobs.dao.JobsManager;
import com.google.common.xml.XmlEscapers;
import com.magmaguy.elitemobs.economy.EconomyHandler;
import net.milkbowl.vault.economy.Economy;
import net.minecraft.world.item.ItemMapEmpty;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SystemCommand implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("server.manage")) {
            sender.sendMessage(Messages.noPermission);
            return false;
        }
        if (args.length >= 1) {

            Player player = (Player)sender;
            ItemMeta itemMeta = player.getInventory().getItemInMainHand().getItemMeta();
            itemMeta.addEnchant(Enchantment.getByKey(NamespacedKey.minecraft(args[0].toLowerCase())), 1, true);
            player.getInventory().getItemInMainHand().setItemMeta(itemMeta);
            Utils.DebugMessage(Enchantment.getByKey(NamespacedKey.minecraft(args[0].toLowerCase())).getName(), 1);
            if (args[0].equalsIgnoreCase("reloadFile")) {
                if (args.length == 2) {
                    switch (args[1]) {
                        case "messages.yml":
                            new Messages();
                            break;
                        case "classes.yml":
                            new GroupManager().initialize();
                            break;
                        case "skills.yml":
                            new SkillManager().initialize();
                            break;
                        case "locations.yml":
                            new Locations();
                            break;
                        case "portals.yml":
                            PortalCommand.reloadConfig();
                            break;
                        case "shop.yml":
                            new ShopManager().initialize();
                            break;
                        case "resourceWorlds.yml":
                            new ResourceWorldsManager().initialize();
                            break;
                        case "jobs.yml":
                            new JobManager().initialize();
                            break;
                        case "basic.yml":
                            Main.getInstance().newBasicConfig();
                            break;
                        case "alchemist.yml":
                            new AlchemistManager().initialize();
                            break;
                        case "config.yml":
                            Main.getInstance().reloadConfig();
                            break;
                        case "advancements.yml":
                            AdvancementsManager.reload();
                            break;
                        case "ores.yml":
                            new OresManager().initialize();
                            break;
                        case "pets.yml":
                            PetManager.load();
                            break;
                        case "chunk.yml":
                            new ChunkManager().initialize();
                            break;
                        case "scrapper.yml":
                            new ScrapperManager().initialize();
                            break;
                        default:
                            sender.sendMessage("&4Invalid File!");
                            break;
                    }
                    sender.sendMessage("§1File has been reloaded");
                }
            } else if (args[0].equalsIgnoreCase("news")) {
                if (args.length > 3) {
                    if (args[2].equalsIgnoreCase("setTitle")) {
                        if (Character.isDigit(args[1].toCharArray()[0])) {
                            double num = Double.parseDouble(String.valueOf(args[1].toCharArray()[0]));
                            if (Utils.isInt(num)) {
                                if (BasicConfigManager.bossBars.size() >= num && num > 0) {
                                    BossBarObj bossBar = BasicConfigManager.bossBars.get((int) num - 1);
                                    BasicConfigManager.updateCurrent(bossBar);
                                    StringBuilder stringBuilder = new StringBuilder();
                                    for (int i = 3; i < args.length; i++) {
                                        stringBuilder.append(Utils.color(args[i]) + " ");
                                    }
                                    bossBar.setTitle(stringBuilder.substring(0, stringBuilder.toString().length() - 1));
                                    BasicConfigManager.bossBars.set((int) num - 1, bossBar);
                                    sender.sendMessage("§1News has been §7updated!");
                                    BasicConfigManager.setBasicConfig("news.mode." + (int) num + ".title", Utils.deColor(stringBuilder.substring(0, stringBuilder.toString().length() - 1)));
                                    Main.getInstance().newBasicConfig();
                                } else if (BasicConfigManager.bossBars.size() == num - 1) {
                                    StringBuilder stringBuilder = new StringBuilder();
                                    for (int i = 3; i < args.length; i++) {
                                        stringBuilder.append(Utils.color(args[i]) + " ");
                                    }
                                    BasicConfigManager.bossBars.add(new BossBarObj(stringBuilder.substring(0, stringBuilder.toString().length() - 1), BarColor.WHITE));
                                    sender.sendMessage("§1News has been §7created!");
                                    BasicConfigManager.setBasicConfig("news.mode." + (int) num + ".title", Utils.deColor(stringBuilder.substring(0, stringBuilder.toString().length() - 1)));
                                    BasicConfigManager.setBasicConfig("news.mode." + (int) num + ".barColor", BarColor.WHITE.toString());
                                    Main.getInstance().newBasicConfig();
                                } else
                                    sender.sendMessage("§4Out of Range!");
                            } else
                                sender.sendMessage(Messages.falseSyntaxCmd);
                        } else
                            sender.sendMessage(Messages.falseSyntaxCmd);
                    } else if (args[2].equalsIgnoreCase("setColor")) {
                        if (Character.isDigit(args[1].toCharArray()[0])) {
                            double num = Double.parseDouble(String.valueOf(args[1].toCharArray()[0]));
                            if (Utils.isInt(num)) {
                                if (BasicConfigManager.bossBars.size() >= num && num > 0) {
                                    BossBarObj bossBar = BasicConfigManager.bossBars.get((int) num - 1);
                                    BasicConfigManager.updateCurrent(bossBar);
                                    BarColor barColor = BarColor.valueOf(args[3]);
                                    if (barColor == null) {
                                        sender.sendMessage("§4Invalid Color!");
                                        return false;
                                    }
                                    bossBar.setBarColor(barColor);
                                    BasicConfigManager.bossBars.set((int) num - 1, bossBar);
                                    sender.sendMessage("§1News has been §7updated!");
                                    BasicConfigManager.setBasicConfig("news.mode." + (int) num + ".barColor", barColor.toString());
                                    BasicConfigManager.updateCurrent(bossBar);
                                    Main.getInstance().newBasicConfig();
                                } else if (BasicConfigManager.bossBars.size() == num - 1) {
                                    BarColor barColor = BarColor.valueOf(args[3]);
                                    if (barColor == null) {
                                        sender.sendMessage("§4Invalid Color!");
                                        return false;
                                    }
                                    BasicConfigManager.bossBars.add(new BossBarObj("", barColor));
                                    sender.sendMessage("§1News has been §7created!");
                                    BasicConfigManager.setBasicConfig("news.mode." + (int) num + ".title", "&r");
                                    BasicConfigManager.setBasicConfig("news.mode." + (int) num + ".barColor", barColor.toString());
                                    Main.getInstance().newBasicConfig();
                                } else
                                    sender.sendMessage("§4Out of Range!");
                            } else
                                sender.sendMessage(Messages.falseSyntaxCmd);
                        } else
                            sender.sendMessage(Messages.falseSyntaxCmd);
                    } else
                        sender.sendMessage(Messages.falseSyntaxCmd);
                } else if (args[2].equalsIgnoreCase("set")) {
                    if (Character.isDigit(args[1].toCharArray()[0])) {
                        double num = Double.parseDouble(String.valueOf(args[1].toCharArray()[0]));
                        if (Utils.isInt(num)) {
                            if (BasicConfigManager.bossBars.size() >= num && num > 0) {
                                BasicConfigManager.setNews(BasicConfigManager.bossBars.get((int) num - 1));
                            } else
                                sender.sendMessage("§4Out of Range!");
                        } else
                            sender.sendMessage(Messages.falseSyntaxCmd);
                    } else
                        sender.sendMessage(Messages.falseSyntaxCmd);;
                } else
                    sender.sendMessage(Messages.falseSyntaxCmd);
            } else if (args[0].equalsIgnoreCase("newsremove")) {
                if (args.length == 2) {
                    if (Character.isDigit(args[1].toCharArray()[0])) {
                        double num = Double.parseDouble(String.valueOf(args[1].toCharArray()[0]));
                        if (Utils.isInt(num)) {
                            if (BasicConfigManager.bossBars.size() >= num && num > 0) {
                                BasicConfigManager.bossBars.remove((int) num - 1);
                                sender.sendMessage("§1News has been §7removed!");
                                BasicConfigManager.removeNews((int)num);
                                Main.getInstance().newBasicConfig();
                            } else
                                sender.sendMessage("§4Out of Range!");
                        } else
                            sender.sendMessage(Messages.falseSyntaxCmd);
                    }  else
                        sender.sendMessage(Messages.falseSyntaxCmd);
                } else
                    sender.sendMessage(Messages.falseSyntaxCmd);
            } else if (args[0].equalsIgnoreCase("player")) {
                if (args.length > 2) {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target == null) {
                        sender.sendMessage(Messages.playerNotExist);
                        return false;
                    }

                    if (args[2].equalsIgnoreCase("removeClass")) {
                        if (args.length == 4) {
                            Group group = Group.getGroup(args[3]);
                            if (group == null) {
                                sender.sendMessage("§4Invalid Class!");
                                return false;
                            }
                            PlayerData.removeGroup(target.getUniqueId(), group);
                            AdvancementsManager.revokeAdvancement(target, group);
                            sender.sendMessage("§1Class removed from " + target.getName());
                        } else
                            sender.sendMessage(Messages.falseSyntaxCmd);

                    } else if (args[2].equalsIgnoreCase("addClass")) {
                        if (args.length == 4) {
                            Group group = Group.getGroup(args[3]);
                            if (group == null) {
                                sender.sendMessage("§4Invalid Class!");
                                return false;
                            }
                            PlayerData.addGroup(target.getUniqueId(), group);
                            AdvancementsManager.grantAdvancement(target, group);
                            sender.sendMessage("§1Class added to " + target.getName());
                        } else
                            sender.sendMessage(Messages.falseSyntaxCmd);
                    } else if (args[2].equalsIgnoreCase("addAllClasses")) {
                        PlayerData.addAllGroups(target.getUniqueId());
                        for (Group group : PlayerData.getGroupRAM(target.getUniqueId())) {
                            AdvancementsManager.grantAdvancement(target, group);
                        }
                        sender.sendMessage("§1Classes all added for " + target.getName());
                    } else if (args[2].equalsIgnoreCase("removeAllClasses")) {
                        PlayerData.removeAllGroups(target.getUniqueId());
                        for (Group group : PlayerData.getGroupRAM(target.getUniqueId())) {
                            AdvancementsManager.revokeAdvancement(target, group);
                        }
                        sender.sendMessage("§1Classes removed from " + target.getName());
                    } else if (args[2].equalsIgnoreCase("eco")) {
                        if (args.length == 5) {
                            if (args[3].equalsIgnoreCase("setBalance")) {
                                if (Utils.isNumeric(args[4])) {
                                    double num = Double.parseDouble(String.valueOf(args[4]));
                                    Economy eco = Main.getInstance().eco;
                                    eco.withdrawPlayer(target, eco.getBalance(target));
                                    eco.depositPlayer(target, num);
                                    sender.sendMessage(Messages.operationComplete);
                                } else
                                    sender.sendMessage("§4Invalid amount!");
                            } else if (args[3].equalsIgnoreCase("add")) {
                                if (Utils.isNumeric(args[4])) {
                                    double num = Double.parseDouble(String.valueOf(args[4]));
                                    Economy eco = Main.getInstance().eco;
                                    eco.depositPlayer(target, num);
                                    sender.sendMessage(Messages.operationComplete);
                                } else
                                    sender.sendMessage("§4Invalid amount!");
                            } else if (args[3].equalsIgnoreCase("remove")) {
                                if (Utils.isNumeric(args[4])) {
                                    double num = Double.parseDouble(String.valueOf(args[4]));
                                    Economy eco = Main.getInstance().eco;
                                    eco.withdrawPlayer(target, num);
                                    sender.sendMessage(Messages.operationComplete);
                                } else
                                    sender.sendMessage("§4Invalid amount!");
                            }
                        } else
                            sender.sendMessage(Messages.falseSyntaxCmd);

                    } else if (args[2].equalsIgnoreCase("exp")) {
                        if (args.length == 5) {
                            if (args[3].equalsIgnoreCase("setBalance")) {
                                if (Utils.isNumeric(args[4])) {
                                    double num = Double.parseDouble(String.valueOf(args[4]));
                                    EconomyHandler.setCurrency(target.getUniqueId(), num);
                                    sender.sendMessage(Messages.operationComplete);
                                } else
                                    sender.sendMessage("§4Invalid amount!");
                            } else if (args[3].equalsIgnoreCase("add")) {
                                if (Utils.isNumeric(args[4])) {
                                    double num = Double.parseDouble(String.valueOf(args[4]));
                                    EconomyHandler.addCurrency(target.getUniqueId(), num);
                                    sender.sendMessage(Messages.operationComplete);
                                } else
                                    sender.sendMessage("§4Invalid amount!");
                            } else if (args[3].equalsIgnoreCase("remove")) {
                                if (Utils.isNumeric(args[4])) {
                                    double num = Double.parseDouble(String.valueOf(args[4]));
                                    EconomyHandler.subtractCurrency(target.getUniqueId(), num);
                                    sender.sendMessage(Messages.operationComplete);
                                } else
                                    sender.sendMessage("§4Invalid amount!");
                            }
                        } else
                            sender.sendMessage(Messages.falseSyntaxCmd);
                    } else if (args[2].equalsIgnoreCase("playtime")) {
                        if (args.length == 5) {
                            if (args[3].equalsIgnoreCase("setminutes")) {
                                if (Utils.isNumeric(args[4])) {
                                    int[] playtime = PlayerData.getPlayTimeRAM(target.getUniqueId());
                                    double num = Double.parseDouble(String.valueOf(args[4]));
                                    PlayerData.setPlaytime(target.getUniqueId(), new int[]{(int)num, playtime[1]});
                                    sender.sendMessage(Messages.operationComplete);
                                } else
                                    sender.sendMessage("§4Invalid amount!");
                            } else if (args[3].equalsIgnoreCase("sethours")) {
                                if (Utils.isNumeric(args[4])) {
                                    int[] playtime = PlayerData.getPlayTimeRAM(target.getUniqueId());
                                    double num = Double.parseDouble(String.valueOf(args[4]));
                                    PlayerData.setPlaytime(target.getUniqueId(), new int[]{playtime[0], (int)num});
                                    sender.sendMessage(Messages.operationComplete);
                                } else
                                    sender.sendMessage("§4Invalid amount!");
                            }
                        } else
                            sender.sendMessage(Messages.falseSyntaxCmd);
                    }
                } else
                    sender.sendMessage(Messages.falseSyntaxCmd);
            } else if (args[0].equalsIgnoreCase("reloadTextures")) {
                Main.getInstance().reloadConfig();
                for (Player all : Bukkit.getOnlinePlayers()) {
                    all.setTexturePack(Main.textureURl);
                }
            }
        }
        return false;
    }

}

/*
if (Character.isDigit(args[1].toCharArray()[0])) {
                        double i = Double.parseDouble(String.valueOf(args[1].toCharArray()[0]));
                        if (Utils.isInt(i)) {
                            if (BasicConfigManager.bossBars.size() > i) {

                            }
                        } else
                            sender.sendMessage(Messages.falseSyntaxCmd);
                    } else
                        sender.sendMessage(Messages.falseSyntaxCmd);
 */
