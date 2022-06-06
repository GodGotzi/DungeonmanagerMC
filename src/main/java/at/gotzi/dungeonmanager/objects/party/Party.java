// This class was created by Wireless


package at.gotzi.dungeonmanager.objects.party;

import at.gotzi.dungeonmanager.objects.party.PartyMember;

import java.util.List;

public class Party {

    private final List<PartyMember> members;

    public Party(List<PartyMember> members) {
        this.members = members;
    }

    public List<PartyMember> getMembers() {
        return members;
    }

    public void addMember(PartyMember partyMember) {
        members.add(partyMember);
    }

    public void removeMember(PartyMember partyMember) {
        members.remove(partyMember);
    }
}
