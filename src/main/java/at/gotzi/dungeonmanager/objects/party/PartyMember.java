// This class was created by Wireless


package at.gotzi.dungeonmanager.objects.party;

import org.bukkit.entity.Player;

import java.util.List;

public class PartyMember {

    private Player player;
    private PartyRole partyRole;
    private List<Player> players;

    public PartyMember(Player player, PartyRole partyRole) {
        this.player = player;
        this.partyRole = partyRole;
    }

    public void setPartyRole(PartyRole partyRole) {
        this.partyRole = partyRole;
    }
    public PartyRole getPartyRole() {
        return partyRole;
    }

    public Player getPlayer() {
        return player;
    }
}
