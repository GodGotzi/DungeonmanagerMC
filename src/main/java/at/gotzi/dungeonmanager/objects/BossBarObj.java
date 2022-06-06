package at.gotzi.dungeonmanager.objects;

import org.bukkit.boss.BarColor;

public class BossBarObj {

    private String title;
    private BarColor barColor;


    public BossBarObj(String title, BarColor barColor) {
        this.title = title;
        this.barColor = barColor;
    }

    public BarColor getBarColor() {
        return barColor;
    }

    public String getTitle() {
        return title;
    }

    public void setBarColor(BarColor barColor) {
        this.barColor = barColor;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
