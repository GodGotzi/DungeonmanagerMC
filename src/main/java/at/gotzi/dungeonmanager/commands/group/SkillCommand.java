package at.gotzi.dungeonmanager.commands.group;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SkillCommand extends GroupCommand implements TabCompleter {

    public static List<UUID> disabled = new ArrayList<>();

    @Override
    public boolean execute() {
        if (sender instanceof Player player) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("enable")) {
                    if (!disabled.contains(player.getUniqueId())) {
                        player.sendMessage("§6Skills already §8enabled!");
                        return false;
                    }
                    disabled.remove(player.getUniqueId());
                    player.sendMessage("§6Skills §8enabled!");
                } else if (args[0].equalsIgnoreCase("disable")) {
                    if (disabled.contains(player.getUniqueId())) {
                        player.sendMessage("§6Skills already §8disabled!");
                        return false;
                    }
                    disabled.add(player.getUniqueId());
                    player.setFlying(false);
                    player.setAllowFlight(false);
                    player.sendMessage("§6Skills §8disabled!");
                }
            }
        }
        return false;
    }

    public static boolean isDisabled(Player player, boolean silent) {
        if (disabled.contains(player.getUniqueId())) {
            if (!silent) player.sendMessage("§cSkills are disabled! /skills enable");
            return true;
        }
        return false;
    }

    public static List<UUID> getDisabled() {
        return disabled;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        List<String> commands = new ArrayList<>();
        if(sender instanceof Player player) {
            if (args.length == 1) {
                commands.add("enable");
                commands.add("disable");
            }
        }

        if(commands.isEmpty()) {
            return null;
        } else return commands;
    }
}
