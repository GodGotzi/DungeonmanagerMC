package at.gotzi.dungeonmanager.listener.group;

import at.gotzi.dungeonmanager.utils.Utils;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ExpBottleEvent;
import org.bukkit.inventory.meta.ItemMeta;

public class ExpThrowListener implements Listener {



    @EventHandler
    public void onThrow(ExpBottleEvent event) {
        ItemMeta expBottleMeta = event.getEntity().getItem().getItemMeta();

        if (expBottleMeta.getEnchants().containsKey(Enchantment.LOOT_BONUS_MOBS)) {
            int lvl = expBottleMeta.getEnchantLevel(Enchantment.LOOT_BONUS_MOBS);

            event.setExperience(Utils.getTotalExperience(lvl));
        }
    }

}
