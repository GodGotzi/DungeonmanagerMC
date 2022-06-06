// This class was created by Wireless


package at.gotzi.dungeonmanager.commands.tabcompleter;

import at.gotzi.dungeonmanager.objects.enums.Error;
import at.gotzi.dungeonmanager.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainTabComplete implements TabCompleter {


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> commands = new ArrayList<>();
        if(args.length == 1) {
            commands.add("lobbysetup");
            commands.add("dungeonsetup");
        } else if(args.length == 2) {
            if(args[0].equalsIgnoreCase("lobbysetup")) {
                commands.add("sethologramspawn");
                commands.add("setdungeonportalpos1");
                commands.add("setdungeonportalpos2");
            } else if(args[0].equalsIgnoreCase("dungeonsetup")) {
                File file = new File("dungeons");
                if (file.isDirectory()) {
                    if (!Arrays.asList(file.listFiles()).stream().toList().isEmpty()) {
                        for (File f : Arrays.asList(file.listFiles()).stream().toList()) {
                            if (!f.getName().equals("dungeoncache")) {
                                commands.add(f.getName());
                            }
                        }
                    } else
                        Utils.callError(" Structure Error call me",  Error.Unexpected);
                } else
                    Utils.callError(" Structure Error call me",  Error.Unexpected);
            }
        } else if(args.length == 3) {
            if(args[0].equalsIgnoreCase("dungeonsetup")) {
                commands.add("setMob");
                commands.add("setChest");
                commands.add("setBoss");
                commands.add("setSpawn");
                commands.add("import");
                commands.add("edit");
            }
        } else if(args.length == 4) {
            if(args[0].equalsIgnoreCase("dungeonsetup")) {
                if(args[2].equalsIgnoreCase("setMob") || args[2].equalsIgnoreCase("setBoss")) {
                    File file = new File("plugins//EliteMobs//custombosses");
                    if(file.isDirectory()) {
                        File[] files = file.listFiles();
                        assert files != null;
                        for (File f : files) {
                            commands.add(f.getName());
                        }
                    }
                } else if (args[2].equalsIgnoreCase("import")) {
                    commands.add("normal");
                    commands.add("nether");
                    commands.add("end");
                }
            }

        } else if(args.length == 5) {
            if(args[0].equalsIgnoreCase("dungeonsetup")) {
                if(args[2].equalsIgnoreCase("setMob")) {
                    if(!args[3].contains("tier")) {
                        for (int i = 0; i < 250; i++) {
                            commands.add(String.valueOf(i));
                        }
                    }
                }
            }
        }

        if(commands.isEmpty()) {
            return null;
        } else return commands;
    }
}
