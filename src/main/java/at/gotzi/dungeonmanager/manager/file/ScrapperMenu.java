package at.gotzi.dungeonmanager.manager.file;

import com.magmaguy.elitemobs.ChatColorConverter;
import com.magmaguy.elitemobs.api.EliteMobsItemDetector;
import com.magmaguy.elitemobs.config.ConfigValues;
import com.magmaguy.elitemobs.config.menus.premade.ScrapperMenuConfig;
import com.magmaguy.elitemobs.config.menus.premade.SellMenuConfig;
import com.magmaguy.elitemobs.items.ItemTierFinder;
import com.magmaguy.elitemobs.items.customenchantments.SoulbindEnchantment;
import com.magmaguy.elitemobs.items.itemconstructor.ItemConstructor;
import com.magmaguy.elitemobs.menus.EliteMenu;
import com.magmaguy.elitemobs.utils.ItemStackGenerator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static com.magmaguy.elitemobs.menus.EliteMenu.createEliteMenu;

public class ScrapperMenu {
    private static final List<Integer> validSlots;
    public static HashMap<Player, Inventory> inventories;

    public void constructScrapMenu(Player player) {
        Inventory scrapInventory = Bukkit.createInventory(player, 54, ScrapperMenuConfig.shopName);

        for(int i = 0; i < 54; ++i) {
            if (i == ScrapperMenuConfig.infoSlot) {
                scrapInventory.setItem(i, ScrapperMenuConfig.infoButton);
            } else if (i == ScrapperMenuConfig.cancelSlot) {
                scrapInventory.setItem(i, ScrapperMenuConfig.cancelButton);
            } else if (i != ScrapperMenuConfig.confirmSlot) {
                if (!validSlots.contains(i)) {
                    scrapInventory.setItem(i, ItemStackGenerator.generateItemStack(Material.GLASS_PANE));
                }
            } else {
                ItemStack clonedConfirmButton = ScrapperMenuConfig.confirmButton.clone();
                List<String> lore = new ArrayList();
                Iterator var6 = ScrapperMenuConfig.confirmButton.getItemMeta().getLore().iterator();

                while(var6.hasNext()) {
                    String string = (String)var6.next();
                    lore.add(string);
                }

                ScrapperMenuConfig.confirmButton.getItemMeta().setLore(lore);
                ItemMeta clonedMeta = clonedConfirmButton.getItemMeta();
                clonedMeta.setLore(lore);
                clonedConfirmButton.setItemMeta(clonedMeta);
                scrapInventory.setItem(i, clonedConfirmButton);
            }
        }

        player.openInventory(scrapInventory);
        createEliteMenu(player, scrapInventory, inventories);
    }

    static {
        validSlots = ScrapperMenuConfig.storeSlots;
        inventories = new HashMap();
    }

    public static class ScrapperMenuEvents implements Listener {
        public ScrapperMenuEvents() {
        }

        @EventHandler(
                ignoreCancelled = true
        )
        public void onInventoryInteract(InventoryClickEvent event) {
            if (EliteMenu.isEliteMenu(event, ScrapperMenu.inventories)) {
                event.setCancelled(true);
                Player player = (Player)event.getWhoClicked();
                ItemStack currentItem = event.getCurrentItem();
                Inventory shopInventory = event.getView().getTopInventory();
                Inventory playerInventory = event.getView().getBottomInventory();
                Iterator var6;
                if (EliteMenu.isBottomMenu(event)) {
                    if (!EliteMobsItemDetector.isEliteMobsItem(event.getCurrentItem())) {
                        event.getWhoClicked().sendMessage(ChatColorConverter.convert(ConfigValues.translationConfig.getString("Shop sale instructions")));
                        return;
                    }

                    if (!SoulbindEnchantment.isValidSoulbindUser(currentItem.getItemMeta(), player)) {
                        player.sendMessage(ChatColorConverter.convert(ConfigValues.translationConfig.getString("Shop sale player items warning")));
                        return;
                    }

                    var6 = ScrapperMenuConfig.storeSlots.iterator();

                    while(var6.hasNext()) {
                        int slot = (Integer)var6.next();
                        if (shopInventory.getItem(slot) == null) {
                            shopInventory.setItem(slot, currentItem);
                            playerInventory.clear(event.getSlot());
                            break;
                        }
                    }
                } else if (EliteMenu.isTopMenu(event)) {
                    if (currentItem.equals(SellMenuConfig.infoButton)) {
                        return;
                    }

                    if (event.getSlot() == SellMenuConfig.confirmSlot) {
                        var6 = ScrapperMenu.validSlots.iterator();

                        while(true) {
                            ItemStack itemStack;
                            do {
                                if (!var6.hasNext()) {
                                    return;
                                }

                                Integer validSlot = (Integer)var6.next();
                                itemStack = shopInventory.getItem(validSlot);
                            } while(itemStack == null);

                            int tier = ItemTierFinder.findBattleTier(itemStack);

                            for(int i = 0; i < itemStack.getAmount(); ++i) {
                                if (ThreadLocalRandom.current().nextDouble() > ((double)ScrapperManager.percent/(double)100)) {
                                    player.sendMessage(ChatColorConverter.convert("Scrap failed!"));
                                } else {
                                    player.getInventory().addItem(ItemConstructor.constructScrapItem(tier, player, false));
                                    player.sendMessage(ChatColorConverter.convert("Scrap succeeded!"));
                                }
                            }

                            itemStack.setAmount(0);
                        }
                    }

                    if (event.getSlot() == SellMenuConfig.cancelSlot) {
                        player.closeInventory();
                        return;
                    }

                    if (!ScrapperMenu.validSlots.contains(event.getSlot())) {
                        return;
                    }

                    playerInventory.addItem(event.getCurrentItem());
                    shopInventory.clear(event.getSlot());
                }

            }
        }

        @EventHandler
        public void onInventoryClose(InventoryCloseEvent event) {
            if (EliteMenu.onInventoryClose(event, ScrapperMenu.inventories)) {
                EliteMenu.cancel(event.getView().getTopInventory(), event.getView().getBottomInventory(), ScrapperMenu.validSlots);
            }
        }
    }
}
