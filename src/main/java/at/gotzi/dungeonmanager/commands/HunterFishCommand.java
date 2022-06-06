package at.gotzi.dungeonmanager.commands;

import at.gotzi.dungeonmanager.Main;
import at.gotzi.dungeonmanager.data.PlayerData;
import at.gotzi.dungeonmanager.manager.file.HunterFishManager;
import at.gotzi.dungeonmanager.objects.HunterFish;
import at.gotzi.dungeonmanager.utils.Messages;
import net.milkbowl.vault.economy.EconomyResponse;
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
import java.util.Objects;
import java.util.UUID;

public record HunterFishCommand(Main main) implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (!Objects.equals(player.getUniqueId().toString(), HunterFishManager.getTarget().toString())) {
                player.sendMessage(Messages.notHunted);
                return false;
            }

            if (args.length == 1)  {
                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    player.sendMessage(Messages.playerNotExist);
                    return false;
                }
                if (!target.getWorld().getName().equals("openworld")) {
                    player.sendMessage(Messages.playerNotInWorld);
                    return false;
                }
                if (target == player) {
                    player.sendMessage(Messages.playerNotExist);
                    return false;
                }
                EconomyResponse r = main.eco.withdrawPlayer(player, HunterFishManager.cost);
                if (r.transactionSuccess()) {
                    player.sendMessage(Messages.huntRemove);
                    HunterFishManager.nextTarget(target, false);
                } else {
                    player.sendMessage(Messages.ecoNotEnough);
                }
            } else
                player.sendMessage(Messages.falseSyntaxCmd);
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> commands = new ArrayList<>();
        if (args.length == 1) {
            commands.add("<Price " + HunterFishManager.cost + "$>");
            for (Player player : Bukkit.getOnlinePlayers()) {
                commands.add(player.getName());
            }
        }

        if(commands.isEmpty()) {
            return null;
        } else return commands;
    }
}
