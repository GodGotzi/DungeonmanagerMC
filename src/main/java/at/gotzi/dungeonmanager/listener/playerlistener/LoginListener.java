// This class was created by Wireless


package at.gotzi.dungeonmanager.listener.playerlistener;

import at.gotzi.dungeonmanager.Main;
import at.gotzi.dungeonmanager.data.PlayerData;
import at.gotzi.dungeonmanager.manager.file.AdvancementsManager;
import at.gotzi.dungeonmanager.manager.file.BasicConfigManager;
import at.gotzi.dungeonmanager.utils.Locations;
import at.gotzi.dungeonmanager.utils.Messages;
import at.gotzi.dungeonmanager.utils.Utils;
import com.magmaguy.elitemobs.config.npcs.NPCsConfigFields;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class LoginListener implements Listener {

    private final Main main;

    private static List<UUID> players = new ArrayList<>();

    public LoginListener(Main main) {
        this.main = main;
    }

    @EventHandler(
            priority = EventPriority.HIGHEST
    )
    public void onLogin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.setResourcePack(Main.textureURl);
        player.setGameMode(GameMode.SURVIVAL);

        PlayerData playerData = new PlayerData(player.getUniqueId());
                playerData.getPlayTimeConfig();
                playerData.getGroupConfig();
                playerData.getHomesConfig();
                playerData.getPetsConfig();

        BasicConfigManager.loadNews(player);
        BasicConfigManager.setRightScoreboard(player);
        AdvancementsManager.load(player);

        if(PlayerData.getGroupRAM(player.getUniqueId()).isEmpty()) {
            //this.firstJoin(player);
        }
        if (!player.hasPlayedBefore() || PlayerData.getWorld(player.getUniqueId()) == null || Bukkit.getWorld(PlayerData.getWorld(player.getUniqueId())) == null) {
            player.teleport(Locations.lobbySpawn);
        }
        if (!player.hasPlayedBefore()) {
            AdvancementsManager.addPlayer(player);
        }

        event.setJoinMessage(Messages.joinMsg.replace("%PLAYER%", player.getName()));
    }

    public static NPCsConfigFields npCsConfigFields;

    public void firstJoin(Player player) {
        PlayerInventory inventory = player.getInventory();
        inventory.clear();

        players.add(player.getUniqueId());
        Bukkit.getScheduler().runTaskAsynchronously(main, new Runnable() {
            @Override
            public void run() {
                BasicConfigManager basicConfigManager = new BasicConfigManager();
                
                if(!basicConfigManager.getBoolean("firstjoin.custominventory")) {
                    return;
                }

                for (String item : basicConfigManager.getConfigSection("firstjoin.custominv.items").getKeys(false)) {
                    Material material = Material.getMaterial(basicConfigManager.getString("firstjoin.custominv.items." + item + ".material"));
                    ItemStack itemStack = new ItemStack(material);
                    String name = Utils.color(basicConfigManager.getString("firstjoin.custominv.items." + item + ".displayname"));
                    if(material != Material.COMPASS) {
                        ItemMeta itemMeta = itemStack.getItemMeta();
                        itemMeta.setLore(basicConfigManager.getStringList("firstjoin.custominv.items." + item + ".lore"));
                        if(name != null)
                            itemMeta.setDisplayName(name);
                        itemStack.setItemMeta(itemMeta);
                    } else {
                        CompassMeta itemMeta = (CompassMeta) itemStack.getItemMeta();
                        itemMeta.setLore(basicConfigManager.getStringList("firstjoin.custominv.items." + item + ".lore"));
                        String loc = basicConfigManager.getString("firstjoin.custominv.items." + item + ".location");
                        if(loc != null) {
                            Location location = new Location(Locations.lobbySpawn.getWorld(),
                                    Double.parseDouble(loc.split(";")[0]),
                                    Double.parseDouble(loc.split(";")[1]),
                                    Double.parseDouble(loc.split(";")[2]), 0, 0);
                            itemMeta.setLodestoneTracked(false);
                            itemMeta.setLodestone(location);
                        }
                        if(name != null)
                            itemMeta.setDisplayName(name);
                        itemStack.setItemMeta(itemMeta);
                    }
                    String slot = basicConfigManager.getString("firstjoin.custominv.items." + item + "." + "slot");
                    if(basicConfigManager.getBoolean("firstjoin.custominv.items." + item + "." + "offhand") && slot == null) {
                        inventory.setItemInOffHand(itemStack);
                    } else if(slot.contains("-")) {
                        int x = Integer.parseInt(slot.split("-")[0]);
                        int y = Integer.parseInt(slot.split("-")[1]);
                        Bukkit.getScheduler().runTask(main, new Runnable() {
                            @Override
                            public void run() {
                                for (int i = x; i <= y; i++) {
                                    inventory.setItem(i, itemStack);
                                }
                            }
                        });
                    } else {
                        Bukkit.getScheduler().runTask(main, new Runnable() {
                            @Override
                            public void run() {
                                inventory.setItem(Integer.parseInt(slot), itemStack);
                            }
                        });
                    }
                }
            }
        });
    }

    public static boolean isTutorial(UUID uuid) {
        return players.contains(uuid);
    }

}