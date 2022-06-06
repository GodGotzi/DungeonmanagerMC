// This class was created by Wireless


package at.gotzi.dungeonmanager.commands.tabcompleter;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PartyTabComplete implements TabCompleter {


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> commands = new ArrayList<>();
        if(args.length == 1) {
            commands.add("invite");
            commands.add("create");
            commands.add("delete");
            commands.add("accept");
            commands.add("kick");
            commands.add("leave");
            commands.add("list");
        } else if(args.length == 2) {
            if(args[0].equalsIgnoreCase("invite") || args[0].equalsIgnoreCase("kick")) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    commands.add(player.getName());
                }
            }
        }

        if(commands.isEmpty()) {
            return null;
        } else return commands;
    }
}
