package at.gotzi.dungeonmanager.objects.groups;

import at.gotzi.dungeonmanager.objects.advacements.CustomAdvancement;
import at.gotzi.dungeonmanager.objects.playerutils.Title;
import at.gotzi.dungeonmanager.objects.skills.Skill;

import java.util.List;

public class GroupObj {

    private final Group group;
    private final List<Skill> open;
    private final List<Skill> onlyClass;
    private final List<Group> neededGroups;
    private final List<Group> nextGroups;

    private final String itemName;
    private final List<String> lore;
    private final int modelData;
    private final int cost;
    private final String arrangement;

    private final float x;
    private final float y;

    private final int slot;

    private final CustomAdvancement customAdvancement;

    private final Title title;

    public GroupObj(Group group, List<Skill> open, List<Skill> onlyClass, List<Group> neededGroups, List<Group> nextGroups, String itemName, List<String> lore, int modelData, int cost, String arrangement, float x, float y, int slot, CustomAdvancement customAdvancement, Title title) {
        this.group = group;
        this.open = open;
        this.onlyClass = onlyClass;
        this.neededGroups = neededGroups;
        this.nextGroups = nextGroups;
        this.itemName = itemName;
        this.lore = lore;
        this.modelData = modelData;
        this.cost = cost;
        this.arrangement = arrangement;
        this.x = x;
        this.y = y;
        this.slot = slot;
        this.customAdvancement = customAdvancement;
        this.title = title;
    }

    public List<Group> getNextGroups() {
        return nextGroups;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public CustomAdvancement getCustomAdvancement() {
        return customAdvancement;
    }

    public List<Skill> getOnlyClass() {
        return onlyClass;
    }

    public int getCost() {
        return cost;
    }

    public Group getGroup() {
        return group;
    }

    public List<Skill> getOpen() {
        return open;
    }

    public List<Group> getNeededGroups() {
        return neededGroups;
    }

    public String getItemName() {
        return itemName;
    }

    public List<String> getLore() {
        return lore;
    }

    public int getModelData() {
        return modelData;
    }

    public Title getTitle() {
        return title;
    }

    public String getArrangement() {
        return arrangement;
    }

    public int getSlot() {
        return slot;
    }
}
