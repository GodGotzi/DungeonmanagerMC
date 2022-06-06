package at.gotzi.dungeonmanager.commands.basic;

import at.gotzi.dungeonmanager.Main;
import at.gotzi.dungeonmanager.manager.player.TeleportManager;
import at.gotzi.dungeonmanager.utils.Locations;
import at.gotzi.dungeonmanager.utils.Messages;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public class SpawnCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            String worldName = player.getWorld().getName();
            if (worldName.contains("resource")) {
                if (worldName.contains("overworld")) {
                    Location location = Locations.overworldSpawn;
                    location.setY(179);
                    TeleportManager.registerTeleport(player, location);
                } else if (worldName.contains("nether")) {
                    Location location = Locations.netherSpawn;
                    TeleportManager.registerTeleport(player, location);
                } else if (worldName.contains("end")) {
                    Location location = Locations.endSpawn;
                    TeleportManager.registerTeleport(player, location);
                }
            } else if (worldName.equals("openworld")) {
                Location location = Locations.openWorldSpawn;
                TeleportManager.registerTeleport(player, location);
            } else if (worldName.equals("world")) {
                Location location = Locations.lobbySpawn;
                TeleportManager.registerTeleport(player, location);
            }
        }
        return false;
    }
}