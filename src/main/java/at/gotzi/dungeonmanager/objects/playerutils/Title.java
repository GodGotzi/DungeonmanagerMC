package at.gotzi.dungeonmanager.objects.playerutils;


public class Title {

    private final String title;
    private final String subTitle;


    public Title(String title, String subTitle) {
        this.title = title;
        this.subTitle = subTitle;
    }

    public String getTitle() {
        return title;
    }

    public String getSubTitle() {
        return subTitle;
    }

}
