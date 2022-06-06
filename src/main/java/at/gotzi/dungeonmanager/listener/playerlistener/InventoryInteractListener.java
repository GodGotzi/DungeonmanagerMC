package at.gotzi.dungeonmanager.listener.playerlistener;

import at.gotzi.dungeonmanager.Main;
import at.gotzi.dungeonmanager.data.PlayerData;
import at.gotzi.dungeonmanager.manager.InventoryManager;
import at.gotzi.dungeonmanager.manager.file.AdvancementsManager;
import at.gotzi.dungeonmanager.manager.file.GroupManager;
import at.gotzi.dungeonmanager.manager.file.ShopManager;
import at.gotzi.dungeonmanager.manager.file.PetManager;
import at.gotzi.dungeonmanager.objects.enchantments.Glow;
import at.gotzi.dungeonmanager.objects.groups.Group;
import at.gotzi.dungeonmanager.objects.groups.GroupObj;
import at.gotzi.dungeonmanager.objects.playerutils.Title;
import at.gotzi.dungeonmanager.utils.Messages;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class InventoryInteractListener implements Listener {

    private Main main;

    private static HashMap<UUID, String> oldInvGroup = new HashMap<>();

    public InventoryInteractListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onInteract(InventoryClickEvent event) {
        ItemStack itemStack = event.getCurrentItem();
        if(itemStack == null) return;
        Inventory inventory = event.getInventory();
        if(event.getWhoClicked() instanceof Player player) {
            if (LoginListener.isTutorial(player.getUniqueId())) {
                event.setCancelled(true);
                return;
            }
            double hashCode = event.getInventory().hashCode();
            if (InventoryManager.isRegistered(hashCode)) {
                event.setCancelled(true);
                if (itemStack.getType() == Main.fillUpItemStack.getType()) return;
                String inv = InventoryManager.getInv(hashCode);
                if (Objects.equals(inv, "GROUPMAIN")) {
                    String action = GroupManager.getAction("GROUPMAIN", itemStack.getItemMeta().getDisplayName());
                    if (action == null) return;
                    switch (action) {
                        case "switchItem" -> {
                            oldInvGroup.put(player.getUniqueId(), "GROUPMAIN");
                            GroupManager.openGUISwitch(player);
                        }
                        case "skillsItem" -> {
                            oldInvGroup.put(player.getUniqueId(), "GROUPMAIN");
                            GroupManager.openGuiSkills(player, 1);
                        }
                        case "closeItem" -> player.closeInventory();
                    }
                } else if (Objects.equals(inv, "GROUPSKILLS")) {
                    String action = GroupManager.getAction("GROUPSKILLS", itemStack.getItemMeta().getDisplayName());
                    if (action == null) return;
                    if ("backItem".equals(action)) {
                        if (oldInvGroup.get(player.getUniqueId()) != null) {
                            this.openOldInv(player, oldInvGroup.get(player.getUniqueId()));
                            oldInvGroup.put(player.getUniqueId(), "GROUPMAIN");
                        } else {
                            player.closeInventory();
                            oldInvGroup.remove(player.getUniqueId());
                        }
                    } else if ("nextPage".equals(action)) {
                        oldInvGroup.put(player.getUniqueId(), "GROUPSKILLS");
                        GroupManager.openGuiSkills(player, 2);
                    }
                } else if (Objects.equals(inv, "GROUPBUY")) {
                    String action = GroupManager.getAction("GROUPBUY", itemStack.getItemMeta().getDisplayName());
                    if ("backItem".equals(action)) {
                        if (oldInvGroup.get(player.getUniqueId()) != null) {
                            this.openOldInv(player, oldInvGroup.get(player.getUniqueId()));
                            oldInvGroup.put(player.getUniqueId(), "GROUPMAIN");
                        } else {
                            player.closeInventory();
                        }
                    }

                    Group group = Group.getGroup(itemStack.getItemMeta().getCustomModelData());
                    GroupObj groupObj = GroupManager.getGroupObj(group);
                    if (groupObj == null) {
                        return;
                    }

                    player.closeInventory();
                    EconomyResponse r = main.eco.withdrawPlayer(player, groupObj.getCost());
                    if (r.transactionSuccess()) {
                        Title title = groupObj.getTitle();
                        player.sendTitle(title.getTitle().replace("%GROUP%", itemStack.getItemMeta().getDisplayName()),
                                title.getSubTitle().replace("%GROUP%", itemStack.getItemMeta().getDisplayName()),
                                GroupManager.fadeIn, GroupManager.stay, GroupManager.fadeOut);
                        AdvancementsManager.grantAdvancement(player, group);
                        PlayerData.addGroup(player.getUniqueId(), group);
                        player.sendMessage(Messages.ecoBuy.replace("%AMOUNT%", groupObj.getCost() + "$"));
                    } else {
                        player.sendMessage(Messages.ecoNotEnough);
                    }
                } else if (Objects.equals(inv, "GROUPSWITCH")) {
                    String action = GroupManager.getAction("GROUPSKILLS", itemStack.getItemMeta().getDisplayName());
                    if ("backItem".equals(action)) {
                        if (oldInvGroup.get(player.getUniqueId()) != null) {
                            this.openOldInv(player, oldInvGroup.get(player.getUniqueId()));
                        } else {
                            player.closeInventory();
                            oldInvGroup.remove(player.getUniqueId());
                        }
                    }
                    Group group = Group.getGroup(itemStack.getItemMeta().getCustomModelData());

                    if (group != null && !itemStack.getItemMeta().hasEnchant(Glow.generateEnchant())) {
                        GroupObj groupObj = GroupManager.getGroupObj(group);
                        oldInvGroup.put(player.getUniqueId(), "GROUPSWITCH");
                        GroupManager.openGuiGroupInfo(player, groupObj);
                    } else if (PlayerData.getGroupRAM(player.getUniqueId()).contains(group)) {
                        PlayerData.selectGroup(player.getUniqueId(), group);
                        GroupManager.openGUISwitch(player);
                    }

                } else if (Objects.equals(inv, "SHOPMAIN")) {
                    String action = ShopManager.getAction("invs.main.items", itemStack.getItemMeta().getDisplayName());
                    if ("closeItem".equals(action)) {
                        player.closeInventory();
                        return;
                    }

                    action = ShopManager.getAction("items.parent", itemStack.getItemMeta().getDisplayName());
                    ShopManager.openGui(action + ".child", player);

                } else if (inv.contains("SHOP") && !inv.contains("BUY") && !inv.contains("SELL")) {

                    String[] split = inv.split(" ");
                    String split1 = split[1] +".parent";
                    if (ShopManager.isBackItem(event.getSlot())) {
                        String[] splitPath = split1.split("\\.");
                        if (splitPath.length == 3) {
                            ShopManager.openGuiMain(player);
                            return;
                        }

                        List<String> splitPathList = new ArrayList<>(Arrays.stream(splitPath).toList());
                        StringBuilder stringBuilder = new StringBuilder();
                        splitPathList.remove(splitPathList.size() - 1);
                        splitPathList.remove(splitPathList.size() - 1);
                        splitPathList.remove(splitPathList.size() - 1);
                        splitPathList.remove(splitPathList.size() - 1);

                        for (String str : splitPathList) {
                            if (str.equals(splitPathList.get(0))) {
                                stringBuilder.append(str);
                            } else {
                                stringBuilder.append("." + str);
                            }
                        }

                        ShopManager.openGui(stringBuilder.toString(), player);
                        return;
                    }

                    if (itemStack.getItemMeta().getCustomModelData() == 5) {
                        if (event.getAction() == InventoryAction.PICKUP_ALL) {
                            String action = ShopManager.getAction("items.parent." + split[1], itemStack.getType());
                            ShopManager.openGuiShopBuy(player, split[1] + "." + action, split[1]);
                            return;
                        } else if(event.getAction() == InventoryAction.PICKUP_HALF) {
                            String action = ShopManager.getAction("items.parent." + split[1], itemStack.getType());
                            ShopManager.openGuiShopSell(player, split[1] + "." + action, split[1]);
                            return;
                        }
                    }

                    String action = ShopManager.getAction("items.parent." + split1, itemStack.getItemMeta().getDisplayName());
                    ShopManager.openGui(split1 + "." + action + ".child", player);
                } else if (inv.contains("BUY")) {
                    String[] split = inv.split(" ");
                    if (ShopManager.isBackItem(event.getSlot())) {
                        ShopManager.openGui(split[1], player);
                        return;
                    }
                    if (itemStack.getItemMeta().getCustomModelData() != 5) return;
                    String[] prices = split[2].split(";");
                    double price = 0;
                    if (itemStack.getAmount() == 1) {
                        price = Double.parseDouble(prices[0]);
                    } else if (itemStack.getAmount() == 8) {
                        price = Double.parseDouble(prices[1]);
                    } else if (itemStack.getAmount() == 16) {
                        price = Double.parseDouble(prices[2]);
                    } else if (itemStack.getAmount() == 32) {
                        price = Double.parseDouble(prices[3]);
                    } else if (itemStack.getAmount() == 64) {
                        price = Double.parseDouble(prices[4]);
                    }

                    if (this.isFull(player.getInventory())) {
                        player.sendMessage(Messages.invFull);
                        return;
                    }
                    EconomyResponse r = main.eco.withdrawPlayer(player, price);
                    if (r.transactionSuccess()) {
                        player.sendMessage(Messages.ecoBuy.replace("%AMOUNT%", price + "$"));
                        ItemMeta itemMeta = itemStack.getItemMeta();
                        itemMeta.setLore(new ArrayList<>());
                        itemStack.setItemMeta(itemMeta);
                        player.getInventory().addItem(itemStack);
                    } else {
                        player.sendMessage(Messages.ecoNotEnough);
                    }
                } else if (inv.contains("SELL")) {
                    String[] split = inv.split(" ");
                    if (ShopManager.isBackItem(event.getSlot())) {
                        ShopManager.openGui(split[1], player);
                        return;
                    }
                    if (itemStack.getItemMeta().getCustomModelData() != 5) return;
                    String[] prices = split[2].split(";");
                    double price = 0;
                    if (itemStack.getAmount() == 1) {
                        price = Double.parseDouble(prices[0]);
                    } else if (itemStack.getAmount() == 8) {
                        price = Double.parseDouble(prices[1]);
                    } else if (itemStack.getAmount() == 16) {
                        price = Double.parseDouble(prices[2]);
                    } else if (itemStack.getAmount() == 32) {
                        price = Double.parseDouble(prices[3]);
                    } else if (itemStack.getAmount() == 64) {
                        price = Double.parseDouble(prices[4]);
                    }

                    if (!this.isItemInInv(player.getInventory(), itemStack)) {
                        player.sendMessage(Messages.noItem);
                        return;
                    }
                    EconomyResponse r = main.eco.depositPlayer(player, price);
                    if (r.transactionSuccess()) {
                        player.sendMessage(Messages.ecoGet.replace("%AMOUNT%", price + "$"));
                        this.removeItemsInv(player.getInventory(), itemStack);
                    }
                } else if (Objects.equals(inv, "PETMENU")) {
                    if (itemStack.getItemMeta().getCustomModelData() == 5) {
                        if (itemStack.getType() != Material.IRON_BLOCK && itemStack.getType() != Material.DRAGON_HEAD) {
                            EntityType entityType = EntityType.valueOf(itemStack.getType().toString().split("_")[0]);
                            PetManager petManager = new PetManager(player.getUniqueId(), "§3" + player.getName() + "'s Pet", player.getLocation());
                            petManager.spawn(player, entityType);
                        } else if (itemStack.getType() == Material.IRON_BLOCK) {
                            PetManager petManager = new PetManager(player.getUniqueId(), "§3" + player.getName() + "'s Pet", player.getLocation());
                            petManager.spawn(player, EntityType.IRON_GOLEM);
                        } else {
                            PetManager petManager = new PetManager(player.getUniqueId(), "§3" + player.getName() + "'s Pet", player.getLocation());
                            petManager.spawn(player, EntityType.ENDER_DRAGON);
                        }
                        player.sendMessage(Messages.petSummoned);
                    } else if (itemStack.getItemMeta().getCustomModelData() == 6) {
                        if (PetManager.hasPet(player)) {
                            PetManager.removePet(player);
                            player.sendMessage(Messages.petRemove);
                        }
                    }
                    player.closeInventory();
                }
            }
        }
    }

    public boolean isFull(Inventory inv) {
        int count = 0;
        for (ItemStack itemStack : inv.getContents()) {
            count++;
            if (count > 36) return true;
            System.out.println(itemStack);
            if (itemStack == null) {
                return false;
            }
        }
        return true;
    }

    public boolean isItemInInv(PlayerInventory playerInventory, ItemStack itemStack) {
        int amount = 0;
        for (ItemStack iS : playerInventory.getContents()) {
            if (iS != null) {
                if (iS.getType() == itemStack.getType()) {
                    amount += iS.getAmount();
                }
            }
        }
        return amount >= itemStack.getAmount();
    }

    public void removeItemsInv(PlayerInventory playerInventory, ItemStack itemStack) {
        int needAmount = itemStack.getAmount();

        for (ItemStack iS : playerInventory.getContents()) {
            if (needAmount == 0) return;
            if (iS != null) {
                if (iS.getType() == itemStack.getType()) {
                    if (iS.getAmount() <= needAmount) {
                        needAmount -= iS.getAmount();
                        playerInventory.removeItem(iS);
                    } else {
                        int need = iS.getAmount() - needAmount;
                        needAmount = 0;
                        iS.setAmount(need);
                    }
                }
            }
        }
    }

    //int price = ShopManager.getPrice("buyItems.parent." + split[1] + "." + action + ".price");
    public void openOldInv(Player player, String inv) {
        switch(inv) {
            case "GROUPMAIN" -> {
                GroupManager.openGUIMain(player);
            }
            case "GROUPSWITCH" -> {
                GroupManager.openGUISwitch(player);
            }
            case "GROUPSKILLS" -> {
                GroupManager.openGuiSkills(player, 1);
            }
        }
    }
}