package at.gotzi.dungeonmanager.objects.enchantments;

import at.gotzi.dungeonmanager.Main;
import io.papermc.paper.enchantments.EnchantmentRarity;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.EntityCategory;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class Glow extends Enchantment {

    private static Main main = Main.getInstance();

    public Glow( NamespacedKey key) {
        super(key);
    }

    public static Glow generateEnchant() {
        NamespacedKey key = new NamespacedKey(main, main.getDescription().getName());
        return new Glow(key);
    }

    @Override
    public  String getName() {
        return null;
    }

    @Override
    public int getMaxLevel() {
        return 0;
    }

    @Override
    public int getStartLevel() {
        return 0;
    }

    @Override
    public  EnchantmentTarget getItemTarget() {
        return null;
    }

    @Override
    public boolean isTreasure() {
        return false;
    }

    @Override
    public boolean isCursed() {
        return false;
    }

    @Override
    public boolean conflictsWith( Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean canEnchantItem( ItemStack itemStack) {
        return false;
    }

    @Override
    public  Component displayName(int i) {
        return null;
    }

    @Override
    public boolean isTradeable() {
        return false;
    }

    @Override
    public boolean isDiscoverable() {
        return false;
    }

    @Override
    public  EnchantmentRarity getRarity() {
        return null;
    }

    @Override
    public float getDamageIncrease(int i,  EntityCategory entityCategory) {
        return 0;
    }

    @Override
    public  Set<EquipmentSlot> getActiveSlots() {
        return null;
    }

    @Override
    public  String translationKey() {
        return null;
    }
}
