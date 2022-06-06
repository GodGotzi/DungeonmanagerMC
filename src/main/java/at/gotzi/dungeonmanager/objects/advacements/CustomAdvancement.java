package at.gotzi.dungeonmanager.objects.advacements;

import org.bukkit.inventory.ItemStack;

public class CustomAdvancement {

    private final ItemStack pic;
    private final String msg;
    private final String description;
    private final boolean chat;
    private final String backgroundPic;

    public CustomAdvancement(ItemStack pic, String msg, String description, boolean chat, String backgroundPic) {
        this.pic = pic;
        this.msg = msg;
        this.description = description;
        this.chat = chat;
        this.backgroundPic = backgroundPic;
    }

    public ItemStack getPic() {
        return pic;
    }

    public String getMsg() {
        return msg;
    }

    public String getDescription() {
        return description;
    }

    public boolean isChat() {
        return chat;
    }

    public String getBackgroundPic() {
        return backgroundPic;
    }

}
