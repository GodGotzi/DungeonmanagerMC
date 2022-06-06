// This class was created by Wireless


package at.gotzi.dungeonmanager.commands;

import at.gotzi.dungeonmanager.Main;
import at.gotzi.dungeonmanager.utils.Messages;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class WorldCommand implements CommandExecutor {

        private Main main;

        public WorldCommand(Main main) {
                this.main = main;
        }

        private File worldsFile = new File("plugins//DungeonManager//worlds.yml");
        private YamlConfiguration worldsConfig = YamlConfiguration.loadConfiguration(worldsFile);


        @Override
        public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
                String noPermission = Messages.noPermission;
                if (!sender.hasPermission("server.manage")) {
                        sender.sendMessage(Messages.noPermission);
                        return false;
                }
                if(sender instanceof Player player) {
                        if (args.length != 0) {
                                if (args[0].equalsIgnoreCase("create")) {
                                        if (args.length > 2) {
                                                File file = new File(args[1]);
                                                if (file.exists()) {
                                                        sender.sendMessage(main.msgPrefix("§cWorldFolder exists! Use /world import <name> <environment>"));
                                                        return false;
                                                }
                                                World.Environment env = null;
                                                String environment = args[2];
                                                WorldType worldType = null;
                                                if (environment.equalsIgnoreCase("normal")) {
                                                        env = World.Environment.NORMAL;
                                                } else if (environment.equalsIgnoreCase("nether")) {
                                                        env = World.Environment.NETHER;
                                                } else if (environment.equalsIgnoreCase("the_end")) {
                                                        env = World.Environment.THE_END;
                                                } else {
                                                        sender.sendMessage(main.msgPrefix("Invalid Environment"));
                                                        return false;
                                                }
                                                if (args[2].equalsIgnoreCase("normal")) {
                                                        if (args.length >= 4) {
                                                                if (args[3].equalsIgnoreCase("TerraformGenerator")) {
                                                                        if (!environment.equalsIgnoreCase("normal")) {
                                                                                sender.sendMessage(main.msgPrefix("§cUse TerraformGenerator only with Environment \"normal\"!"));
                                                                                return false;
                                                                        }
                                                                        new WorldCreator(args[1]).environment(World.Environment.NORMAL).generator("TerraformGenerator").createWorld();
                                                                        worldsConfig.set("worlds." + args[1] + ".environment", env.toString());
                                                                        worldsConfig.set("worlds." + args[1] + ".generator", args[3]);
                                                                        try {
                                                                                worldsConfig.save(worldsFile);
                                                                        } catch (IOException e) {
                                                                                e.printStackTrace();
                                                                        }
                                                                        sender.sendMessage(main.msgPrefix("§3World: " + args[1] + " has been created!"));
                                                                        return false;
                                                                } else if (args[3].equalsIgnoreCase("flat")) {
                                                                        worldType = WorldType.FLAT;
                                                                } else if (args[3].equalsIgnoreCase("large_biomes")) {
                                                                        worldType = WorldType.LARGE_BIOMES;
                                                                } else if (args[3].equalsIgnoreCase("amplified")) {
                                                                        worldType = WorldType.AMPLIFIED;
                                                                }

                                                        }
                                                }

                                                if (worldType == null) {
                                                        new WorldCreator(args[1]).environment(env).createWorld();
                                                        worldsConfig.set("worlds." + args[1] + ".environment", env.toString());
                                                        try {
                                                                worldsConfig.save(worldsFile);
                                                        } catch (IOException e) {
                                                                e.printStackTrace();
                                                        }
                                                } else {
                                                        new WorldCreator(args[1]).environment(env).type(worldType).createWorld();
                                                        worldsConfig.set("worlds." + args[1] + ".environment", env.toString());
                                                        worldsConfig.set("worlds." + args[1] + ".worldType", worldType.getName());
                                                        try {
                                                                worldsConfig.save(worldsFile);
                                                        } catch (IOException e) {
                                                                e.printStackTrace();
                                                        }
                                                }
                                                sender.sendMessage(main.msgPrefix("§3World: " + args[1] + " has been created!"));
                                        } else
                                                sender.sendMessage(Messages.falseSyntaxCmd);
                                } else if (args[0].equalsIgnoreCase("import")) {
                                        if (args.length > 2) {
                                                World wor = Bukkit.getWorld(args[1]);
                                                if (wor != null) {
                                                        sender.sendMessage(main.msgPrefix("§cAlready exists!"));
                                                        return false;
                                                }
                                                File file = new File(args[1]);
                                                if (!file.exists()) {
                                                        sender.sendMessage(main.msgPrefix("§cCouldn't find world: " + args[1]));
                                                        return false;
                                                }
                                                World.Environment env = null;
                                                String environment = args[2];
                                                if (environment.equalsIgnoreCase("normal")) {
                                                        env = World.Environment.NORMAL;
                                                } else if (environment.equalsIgnoreCase("nether")) {
                                                        env = World.Environment.NETHER;
                                                } else if (environment.equalsIgnoreCase("the_end")) {
                                                        env = World.Environment.THE_END;
                                                } else {
                                                        sender.sendMessage("Invalid Environment");
                                                        return false;
                                                }

                                                worldsConfig.set("worlds." + args[1] + ".environment", env.toString());
                                                try {
                                                        worldsConfig.save(worldsFile);
                                                } catch (IOException e) {
                                                        e.printStackTrace();
                                                }
                                                new WorldCreator(args[1]).environment(env).createWorld();
                                                sender.sendMessage(main.msgPrefix("§3World: " + args[1] + " has been imported!"));
                                        } else
                                                sender.sendMessage(Messages.falseSyntaxCmd);
                                } else if (args[0].equalsIgnoreCase("tp")) {
                                        if (args.length == 2) {
                                                World world = Bukkit.getWorld(args[1]);
                                                if (world == null) {
                                                        sender.sendMessage(main.msgPrefix("§cCouldn't find world: " + args[1]));
                                                        return false;
                                                }
                                                Location location = world.getSpawnLocation();
                                                ((Player) sender).teleport(location);
                                                sender.sendMessage(main.msgPrefix("§3You've been teleported to world: " + args[1]));
                                        } else
                                                sender.sendMessage(Messages.falseSyntaxCmd);
                                } else
                                        player.sendMessage(Messages.falseSyntaxCmd);
                        } else
                                sender.sendMessage(Messages.falseSyntaxCmd);
                }
                return false;
        }
}
