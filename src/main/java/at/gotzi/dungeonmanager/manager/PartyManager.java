// This class was created by Wireless


package at.gotzi.dungeonmanager.manager;

import at.gotzi.dungeonmanager.Main;
import at.gotzi.dungeonmanager.objects.party.Party;
import at.gotzi.dungeonmanager.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PartyManager {

    private static List<Party> parties = new ArrayList<>();

    public static HashMap<UUID, Party> playerPartyHashMap = new HashMap<>();
    public static HashMap<UUID, Party> invites = new HashMap<>();
    public static HashMap<UUID, Location> jumps = new HashMap<>();

    public static Party getParty(Player player) {
        return playerPartyHashMap.get(player.getUniqueId());
    }

    public static void setLinkPlayerToParty(Player player, Party party) {
        playerPartyHashMap.put(player.getUniqueId(), party);
    }

    public static void registerParty(Party party) {
        parties.add(party);
    }

    public static void removeParty(Party party) {
        parties.remove(party);
    }

    public static void removeLink(Player player) {
        playerPartyHashMap.remove(player.getUniqueId());
    }

    public static void addInvite(Player player, Party party) {
        invites.put(player.getUniqueId(), party);
    }

    public static Party getInvite(Player player) {
        return invites.get(player.getUniqueId());
    }

    public static void removeInvite(Player player) {
        invites.remove(player.getUniqueId());
    }

    public static void addJump(Player player, Location location) {
        jumps.put(player.getUniqueId(), location);
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                if(jumps.get(player.getUniqueId()) != null) {
                    jumps.remove(player.getUniqueId());
                }
            }
        }, 600);
    }

    public static Location getJump(Player player) {
        return jumps.get(player.getUniqueId());
    }
}
