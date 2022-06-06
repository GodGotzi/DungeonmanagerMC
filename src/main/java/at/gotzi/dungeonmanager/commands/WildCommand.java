package at.gotzi.dungeonmanager.commands;

import at.gotzi.dungeonmanager.manager.player.TeleportManager;
import at.gotzi.dungeonmanager.utils.Messages;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class WildCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player player) {
            if (player.getWorld().getName().contains("resource") || player.getWorld().getName().contains("openworld")) {
                int result1 = new Random().nextInt(1000-900)+900+(int)player.getLocation().getX();
                int result2 = new Random().nextInt(1000-900)+900+(int)player.getLocation().getZ();
                int negative = new Random().nextInt(2);
                if (negative == 1)
                    result1*=-1;
                else if (negative == 2)
                    result2*=-1;

                int y = 50;
                Material[] material;
                do {
                    y++;
                    material = new Material[]{player.getWorld().getBlockAt(result1, y, result2).getType(), player.getWorld().getBlockAt(result1, y+1, result2).getType()};
                } while (material[0] != Material.AIR || material[1] != Material.AIR);
                Location location = new Location(player.getWorld(), result1, y, result2, player.getLocation().getYaw(), player.getLocation().getPitch());
                location.getChunk().load();
                TeleportManager.registerTeleport(player, location);
            } else
                sender.sendMessage(Messages.cantUseWildCmd);
        } else
            sender.sendMessage(Messages.onlyPlayer);

        return false;
    }

}
