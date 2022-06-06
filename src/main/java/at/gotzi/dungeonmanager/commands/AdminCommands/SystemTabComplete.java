package at.gotzi.dungeonmanager.commands.AdminCommands;

import at.gotzi.dungeonmanager.data.PlayerData;
import at.gotzi.dungeonmanager.manager.file.BasicConfigManager;
import at.gotzi.dungeonmanager.objects.groups.Group;
import at.gotzi.dungeonmanager.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SystemTabComplete implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!sender.hasPermission("server.manage")) {
            return null;
        }
        List<String> commands = new ArrayList<>();
        if(sender instanceof Player player) {
            if (args.length == 1) {
                commands.add("reloadFile");
                commands.add("news");
                commands.add("newsremove");
                commands.add("player");
                commands.add("reloadTextures");
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("reloadFile")) {
                    commands.add("messages.yml");
                    commands.add("classes.yml");
                    commands.add("skills.yml");
                    commands.add("locations.yml");
                    commands.add("portals.yml");
                    commands.add("shop.yml");
                    commands.add("resourceWorlds.yml");
                    commands.add("jobs.yml");
                    commands.add("basic.yml");
                    commands.add("alchemist.yml");
                    commands.add("config.yml");
                    commands.add("advancements.yml");
                    commands.add("ores.yml");
                    commands.add("pets.yml");
                    commands.add("chunk.yml");
                    commands.add("scrapper.yml");
                } else if (args[0].equalsIgnoreCase("news")) {
                    for (int i = 1; i <= BasicConfigManager.bossBars.size(); i++) {
                        commands.add(String.valueOf(i));
                    }
                } else if (args[0].equalsIgnoreCase("newsremove")) {
                    for (int i = 1; i <= BasicConfigManager.bossBars.size(); i++) {
                        commands.add(String.valueOf(i));
                    }
                } else if (args[0].equalsIgnoreCase("player")) {
                    for (Player player1 : Bukkit.getOnlinePlayers()) {
                        commands.add(player1.getName());
                    }
                }
            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("news")) {
                    commands.add("setColor");
                    commands.add("setTitle");
                    commands.add("set");
                } else if (args[0].equalsIgnoreCase("player")) {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target == null) return null;
                    commands.add("addClass");
                    commands.add("removeClass");
                    commands.add("removeAllClasses");
                    commands.add("addAllClasses");
                    commands.add("eco");
                    commands.add("exp");
                    commands.add("playtime");
                }
            } else if (args.length == 4) {
                if (args[0].equalsIgnoreCase("player")) {
                    if (args[2].equalsIgnoreCase("addClass")) {
                        Player target = Bukkit.getPlayer(args[1]);
                        if (target == null) return null;
                        for (Group group : Group.values()) {
                            if (!PlayerData.getGroupRAM(target.getUniqueId()).contains(group)) {
                                commands.add(group.toString());
                            }
                        }
                    } else if (args[2].equalsIgnoreCase("removeClass")) {
                        Player target = Bukkit.getPlayer(args[1]);
                        if (target == null) return null;
                        for (Group group : PlayerData.getGroupRAM(target.getUniqueId())) {
                            commands.add(group.toString());
                        }
                    } else if (args[2].equalsIgnoreCase("eco") || args[2].equalsIgnoreCase("exp")) {
                        Player target = Bukkit.getPlayer(args[1]);
                        if (target == null) return null;
                        commands.add("add");
                        commands.add("remove");
                        commands.add("setBalance");
                    } else if (args[2].equalsIgnoreCase("playtime")) {
                        Player target = Bukkit.getPlayer(args[1]);
                        if (target == null) return null;
                        commands.add("sethours");
                        commands.add("setminutes");
                    }
                }
            } else if (args.length == 5) {

            }
        }

        if(commands.isEmpty()) {
            return null;
        } else return commands;
    }
}
