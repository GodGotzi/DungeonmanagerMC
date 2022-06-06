package at.gotzi.dungeonmanager.commands.tabcompleter;

import at.gotzi.dungeonmanager.data.PlayerData;
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

public class ClaimTabComplete implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<String> commands = new ArrayList<>();
        if(commandSender instanceof Player player) {
            if (args.length == 1) {
                commands.add("claim");
                commands.add("unclaim");
                commands.add("trust");
                commands.add("untrust");
                commands.add("ban");
                commands.add("unban");
                commands.add("info");
                commands.add("kick");
                commands.add("home");
                commands.add("sethome");
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("trust") || args[0].equalsIgnoreCase("ban") || args[0].equalsIgnoreCase("kick")) {
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        commands.add(players.getName());
                    }
                } else if (args[0].equalsIgnoreCase("untrust")) {
                    for (String uuid : PlayerData.getTrustedRAM(player.getUniqueId())) {
                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
                        commands.add(offlinePlayer.getName());
                    }

                } else if(args[0].equalsIgnoreCase("unban")) {
                    for (String uuid : PlayerData.getBannedRAM(player.getUniqueId())) {
                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
                        commands.add(offlinePlayer.getName());
                    }
                } else if (args[0].equalsIgnoreCase("home")) {
                    for (Player all : Bukkit.getOnlinePlayers()) {
                        commands.add(all.getName());
                    }
                }
            }
        }

        if(commands.isEmpty()) {
            return null;
        } else return commands;
    }
}
