package at.gotzi.dungeonmanager.objects.shop;

import org.bukkit.Material;

public class SellBuyItem {


    private final Material material;
    private final double price;

    public SellBuyItem(Material material, double price) {
        this.material = material;
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public Material getMaterial() {
        return material;
    }
}
