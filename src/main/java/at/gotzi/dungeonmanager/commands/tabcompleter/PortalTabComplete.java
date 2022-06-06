// This class was created by Wireless


package at.gotzi.dungeonmanager.commands.tabcompleter;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PortalTabComplete implements TabCompleter {


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> commands = new ArrayList<>();
        if(args.length == 2) {
            commands.add("setpos1");
            commands.add("setpos2");
            commands.add("setdestination");
        }
        if(commands.isEmpty()) {
            return null;
        } else return commands;
    }
}