// This class was created by Wireless


package at.gotzi.dungeonmanager.utils;

import at.gotzi.dungeonmanager.Main;
import at.gotzi.dungeonmanager.objects.enums.Error;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.PacketPlayOutChat;
import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    private static Main main = Main.getInstance();

    public static List<String> getDungeonList() {
        List<String> dungeons = new ArrayList<>();
        dungeons.addAll(main.getConfig().getConfigurationSection("dungeons").getKeys(false));
        return dungeons;
    }

    public static String color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static String deColor(String msg) {
        return msg.replaceAll("§", "&");
    }

    public static int period() {
        return main.getConfig().getInt("period");
    }

    public static double percent(double value, float percent) {
        return (value*(percent/100.0f));
    }

    public static int calPetLvl(int exp) {
        return ((int)Math.sqrt(100+exp+1)-10+1);
    }

    public static int calPetExp(int lvl) {
        return (int) Math.round(Math.pow(lvl, 2) + 19 * lvl);
    }

    public static boolean isDungeonWorld(String name) {
        for (String s : getDungeonList()) {
            if (name.contains(s)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static boolean isInt(double i) {
        return i - (int) i == 0;
    }

    public static BlockFace getBlockFace(String blockFace) {
        switch(blockFace) {
            case "EAST":
                return BlockFace.EAST;
            case "WEST":
                return BlockFace.WEST;
            case "SOUTH":
                return BlockFace.SOUTH;
            case "NORTH":
                return BlockFace.NORTH;
            default:
                return null;
        }
    }

    public static String getUUID(String playerName) {
        try {
            String url = "https://api.mojang.com/users/profiles/minecraft/" + playerName;

            String UUIDJson = IOUtils.toString(new URL(url));

            JSONObject UUIDObject = (JSONObject) JSONValue.parseWithException(UUIDJson);

            String uuidString = UUIDObject.get("id").toString();

            String uuid = uuidString.substring(0, 7) + "-" + uuidString.substring(8, 11) + "-" + uuidString.substring(12, 15) + "-" + uuidString.substring(16, 20) + "-" + uuidString.substring(21);
            return uuid;
        } catch (Exception e) {
            return null;
        }
    }

    public static void DebugMessage(String msg, int nr) {
        Bukkit.getLogger().info("§4[Debug] " + msg + " §3" + String.valueOf(nr));
    }

    public static boolean isEven(int i) {
        if (i == 0) {
            return true;
        }
        return i % 2 == 0;
    }

    public static int getTotalExperience(int level) {
        int xp = 0;
        if (level >= 0 && level <= 15) {
            xp = (int) Math.round(Math.pow(level, 2) + 6 * level);
        } else if (level > 15 && level <= 30) {
            xp = (int) Math.round((2.5 * Math.pow(level, 2) - 40.5 * level + 360));
        } else if (level > 30) {
            xp = (int) Math.round(((4.5 * Math.pow(level, 2) - 162.5 * level + 2220)));
        }
        return xp;
    }

    public static void callError(String info, Error error){
        Bukkit.getLogger().warning(info);
        Bukkit.getLogger().warning("Error: " + error + " | if you don't know why that happened contact me!");
    }

    public static void info(String msg) {
        Bukkit.getLogger().info("§7[§6Information§7] §3" + msg);
    }
}