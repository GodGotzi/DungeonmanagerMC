package at.gotzi.dungeonmanager.commands.AdminCommands;

import at.gotzi.dungeonmanager.utils.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BuildCommand implements CommandExecutor {

    private static List<UUID> uuids = new ArrayList<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (player.hasPermission("server.build")) {
                if (uuids.contains(player.getUniqueId())) {
                    uuids.remove(player.getUniqueId());
                    sender.sendMessage("§1Building disabled");
                } else {
                    uuids.add(player.getUniqueId());
                    sender.sendMessage("§1Building enabled");
                }
            } else
                sender.sendMessage(Messages.noPermission);
        } else
            sender.sendMessage(Messages.onlyPlayer);
        return false;
    }

    public static boolean hasBuildEnabled(UUID uuid) {
        return uuids.contains(uuid);
    }

}
