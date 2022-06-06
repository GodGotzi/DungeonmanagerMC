// This class was created by Wireless


package at.gotzi.dungeonmanager.commands;

import at.gotzi.dungeonmanager.Main;
import at.gotzi.dungeonmanager.utils.Messages;
import at.gotzi.dungeonmanager.utils.Utils;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class PortalCommand implements CommandExecutor {

    private Main main;

    private static final File file = new File("plugins//DungeonManager//portals.yml");
    private static final YamlConfiguration pconfig = YamlConfiguration.loadConfiguration(file);



    public PortalCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        String noPermission = Messages.noPermission;
        String falseSyntaxCmd = Messages.falseSyntaxCmd;
        String onlyPlayer = Messages.onlyPlayer;
        String setCmd = Messages.setCmd;
        String noBlockTargeting = Messages.noBlockTargeting;


        if(sender instanceof Player player) {
            if (!sender.hasPermission("server.manage")) {
                sender.sendMessage(Messages.noPermission);
                return false;
            }
            if(args.length > 1) {
                if(args[1].equalsIgnoreCase("setpos1")) {
                    Block block = player.getTargetBlock(5);
                    if (block == null) {
                        player.sendMessage(noBlockTargeting);
                        return false;
                    }
                    Location location = block.getLocation();
                    String loc = location.getX() + ";" + location.getY() + ";" + location.getZ();
                    pconfig.set("portals." + args[0] + ".world", location.getWorld().getName());
                    pconfig.set("portals." + args[0] + ".pos1", loc);
                    try {
                        pconfig.save(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    player.sendMessage(main.msgPrefix(setCmd.replace("%SET%", args[0] + " pos1")));
                } else if(args[1].equalsIgnoreCase("setpos2")) {
                    Block block = player.getTargetBlock(5);
                    if (block == null) {
                        player.sendMessage(noBlockTargeting);
                        return false;
                    }
                    Location location = block.getLocation();
                    String loc = location.getX() + ";" + location.getY() + ";" + location.getZ();
                    pconfig.set("portals." + args[0] + ".world", location.getWorld().getName());
                    pconfig.set("portals." + args[0] + ".pos2", loc);
                    try {
                        pconfig.save(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    player.sendMessage(main.msgPrefix(setCmd.replace("%SET%", args[0] + " pos2")));
                } else if(args[1].equalsIgnoreCase("setdestination")) {
                    Location location = player.getLocation();
                    String loc = location.getWorld().getName() + ";" + location.getX() + ";" + location.getY() + ";" + location.getZ() + ";" + location.getYaw() + ";" + location.getPitch();
                    pconfig.set("portals." + args[0] + ".world", location.getWorld().getName());
                    pconfig.set("portals." + args[0] + ".destination", loc);
                    try {
                        pconfig.save(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    player.sendMessage(main.msgPrefix(setCmd.replace("%SET%", args[0] + " destination")));
                } else
                    player.sendMessage(main.msgPrefix("setpos1, setpos2, setdestination"));
            } else
                player.sendMessage(main.msgPrefix(falseSyntaxCmd.replace("%CMD%", "/portal <name> <command>")));
        }
        return false;
    }

    public static YamlConfiguration getPconfig() {
        return pconfig;
    }

    public static void reloadConfig() {
        try {
            pconfig.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
}
