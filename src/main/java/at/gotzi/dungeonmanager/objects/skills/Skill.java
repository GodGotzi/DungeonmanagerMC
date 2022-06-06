package at.gotzi.dungeonmanager.objects.skills;

public class Skill {

    private final Skills skills;
    private int strength;

    public Skill(Skills skills, int strength) {
        this.skills = skills;
        this.strength = strength;
    }

    public Skill(Skills skills) {
        this.skills = skills;
    }

    public int getStrength() {
        return strength;
    }

    public Skills getSkills() {
        return skills;
    }
}
