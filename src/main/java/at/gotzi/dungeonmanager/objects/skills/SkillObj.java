package at.gotzi.dungeonmanager.objects.skills;

import org.bukkit.inventory.ItemStack;

public class SkillObj {

    private String displayName;
    private final Skills skills;
    private final ItemStack itemStack;

    public SkillObj(String displayName, Skills skills, ItemStack itemStack) {
        this.displayName = displayName;
        this.skills = skills;
        this.itemStack = itemStack;
    }

    public Skills getSkills() {
        return skills;
    }

    public String getDisplayName() {
        return displayName;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public SkillObj setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

}
