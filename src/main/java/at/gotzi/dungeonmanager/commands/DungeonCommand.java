package at.gotzi.dungeonmanager.commands;

import at.gotzi.dungeonmanager.data.PlayerData;
import at.gotzi.dungeonmanager.manager.player.TeleportManager;
import at.gotzi.dungeonmanager.utils.Locations;
import at.gotzi.dungeonmanager.utils.Messages;
import at.gotzi.dungeonmanager.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DungeonCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("leave")) {
                    if (Utils.isDungeonWorld(player.getWorld().getName())) {
                        TeleportManager.registerTeleport(player, Locations.lobbySpawn);
                    } else
                        sender.sendMessage(Messages.noDungeonWorld);
                } else
                    sender.sendMessage(Messages.falseSyntaxCmd);
            } else
                sender.sendMessage(Messages.falseSyntaxCmd);
        } else
            sender.sendMessage(Messages.onlyPlayer);
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        List<String> commands = new ArrayList<>();
        if(commandSender instanceof Player player) {
            if (strings.length == 1) {
                commands.add("leave");
            }
        }

        if(commands.isEmpty()) {
            return null;
        } else return commands;
    }
}
