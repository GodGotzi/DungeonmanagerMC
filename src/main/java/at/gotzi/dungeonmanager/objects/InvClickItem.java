package at.gotzi.dungeonmanager.objects;

public class InvClickItem {

    private final String configSection;
    private final String displayName;


    public InvClickItem(String configSection, String displayName) {
        this.configSection = configSection;
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getConfigSection() {
        return configSection;
    }
}
