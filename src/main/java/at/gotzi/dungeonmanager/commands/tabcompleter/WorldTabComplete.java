// This class was created by Wireless


package at.gotzi.dungeonmanager.commands.tabcompleter;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class WorldTabComplete implements TabCompleter {


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> commands = new ArrayList<>();
        if(args.length == 1) {
            commands.add("create");
            commands.add("import");
            commands.add("tp");
        } else if(args.length == 2) {
            if(args[0].equalsIgnoreCase("tp")) {
                List<World> worlds = Bukkit.getWorlds();
                for (World world : worlds) {
                    commands.add(world.getName());
                }
            }
        } else if(args.length == 3) {
            if(args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("import")) {
                commands.add("normal");
                commands.add("nether");
                commands.add("the_end");
            }
        } else if(args.length == 4) {
            if(args[0].equalsIgnoreCase("create")) {
                commands.add("flat");
                commands.add("large_biomes");
                commands.add("amplified");
                commands.add("TerraformGenerator");
            }
        }
        if(commands.isEmpty()) {
            return null;
        } else return commands;
    }
}