package at.gotzi.dungeonmanager.manager.file;

import at.gotzi.dungeonmanager.Main;
import at.gotzi.dungeonmanager.manager.InventoryManager;
import at.gotzi.dungeonmanager.objects.enchantments.Glow;
import at.gotzi.dungeonmanager.objects.inventory.BackItem;
import at.gotzi.dungeonmanager.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ShopManager extends FileManager {

    private static List<String> lore;
    private static List<String> buyLore;

    private static float percent;

    private static String shopMain;

    public static String shopNpc;

    private static String shopBuyArrangement;
    
    private static BackItem backItem;

    private static YamlConfiguration shopConfig;


    public ShopManager() {
        super(YamlConfiguration.loadConfiguration(new File("plugins//DungeonManager//shop.yml")));
        shopConfig = YamlConfiguration.loadConfiguration(new File("plugins//DungeonManager//shop.yml"));
    }

    @Override
    public void initialize() {
        percent = (float) shopConfig.getDouble("invs.percent");

        shopMain = this.getString("invs.main.title");

        shopNpc = this.getString("invs.npc");
        List<String> lo = new ArrayList<>();
        for (String l : this.getStringList("itemlore")) {
            lo.add(Utils.color(l));
        }
        lore = lo;

        List<String> lor = new ArrayList<>();
        for (String l : this.getStringList("buyInfoItemLore")) {
            lor.add(Utils.color(l));
        }
        buyLore = lor;
        shopBuyArrangement = this.getString("invs.buyInfo.arrangement");
        
        //backItem
        String itemName = Utils.color(this.getString("invs.backItem" + ".itemName"));
        Material material = Material.getMaterial(this.getString("invs.backItem" + ".material"));
        int modelDataId = this.getInt("invs.backItem" + ".modelDataId");
        boolean glow = this.getBoolean("invs.backItem" + ".glow");
        int slot = this.getInt("invs.backItem" + ".slot");
        List<String> lore = new ArrayList<>();
        for (String l : this.getStringList("invs.backItem" + ".lore")) {
            lore.add(Utils.color(l));
        }
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setLore(lore);
        itemMeta.setCustomModelData(modelDataId);
        itemMeta.setDisplayName(itemName);
        if(glow)
            itemMeta.addEnchant(Glow.generateEnchant(), 1, true);
        for (String enchant : this.getStringList("invs.backItem" + ".enchantments")) {
            Enchantment enchantment = Enchantment.getByName(enchant);
            if(enchantment == null) {
                lore.add("§7" + enchant);
            } else
                itemMeta.addEnchant(enchantment, 1, false);
        }
        itemStack.setItemMeta(itemMeta);
        
        backItem = new BackItem(itemStack, slot);
    }

    public static void openGuiMain(Player player) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            Inventory inventory = Bukkit.createInventory(null, 9*5, shopMain);
            List<Integer> items = new ArrayList<>();
            List<ItemStack> itemStacks = new ArrayList<>();
            for (String item : shopConfig.getConfigurationSection("items.parent").getKeys(false)) {
                String itemName = Utils.color(shopConfig.getString("items.parent." + item + ".itemName"));
                Material material = Material.getMaterial(shopConfig.getString("items.parent." + item + ".material"));
                int modelDataId = shopConfig.getInt("items.parent." + item + ".modelDataId");
                boolean glow = shopConfig.getBoolean("items.parent." + item + ".glow");
                List<String> lore = new ArrayList<>();
                for (String l : shopConfig.getStringList("items.parent." + item + ".lore")) {
                    lore.add(Utils.color(l));
                }
                ItemStack itemStack = new ItemStack(material);
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setLore(lore);
                itemMeta.setCustomModelData(modelDataId);
                itemMeta.setDisplayName(itemName);
                if(glow)
                    itemMeta.addEnchant(Glow.generateEnchant(), 1, true);
                for (String enchant : shopConfig.getStringList("items.parent." + item + ".enchantments")) {
                    Enchantment enchantment = Enchantment.getByName(enchant);
                    if(enchantment == null) {
                        lore.add("§7" + enchant);
                    } else
                        itemMeta.addEnchant(enchantment, 1, false);
                }
                itemStack.setItemMeta(itemMeta);
                itemStacks.add(itemStack);
            }

            int count = 0;
            for (int i = 10; i <= 34; i++) {
                if (i % 9 == 0 || (i + 1) % 9 == 0) {
                } else {
                    if (count < itemStacks.size()) {
                        ItemStack itemStack = itemStacks.get(count);
                        inventory.setItem(i, itemStack);
                        items.add(i);
                        count++;
                    } else {
                        items.add(i);
                    }
                }
            }

            for (String item : shopConfig.getConfigurationSection("invs.main.items").getKeys(false)) {
                String itemName = Utils.color(shopConfig.getString("invs.main.items." + item + ".itemName"));
                Material material = Material.getMaterial(shopConfig.getString("invs.main.items." + item + ".material"));
                int slot = shopConfig.getInt("invs.main.items." + item + ".slot");
                int modelDataId = shopConfig.getInt("invs.main.items." + item + ".modelDataId");
                boolean glow = shopConfig.getBoolean("invs.main.items." + item + ".glow");
                List<String> lore = new ArrayList<>();
                for (String l : shopConfig.getStringList("invs.main.items." + item + ".lore")) {
                    lore.add(Utils.color(l));
                }
                ItemStack skillItem = new ItemStack(material);
                ItemMeta itemMeta = skillItem.getItemMeta();
                itemMeta.setLore(lore);
                itemMeta.setCustomModelData(modelDataId);
                itemMeta.setDisplayName(itemName);
                if(glow)
                    itemMeta.addEnchant(Glow.generateEnchant(), 1, true);
                for (String enchant : shopConfig.getStringList("invs.main.items." + item + ".enchantments")) {
                    Enchantment enchantment = Enchantment.getByName(enchant);
                    if(enchantment == null) {
                        lore.add("§7" + enchant);
                    } else
                        itemMeta.addEnchant(enchantment, 1, false);
                }
                skillItem.setItemMeta(itemMeta);

                inventory.setItem(slot, skillItem);
                items.add(slot);
            }

            if(Main.fillup) {
                for (int i = 0; i < (9*5); i++) {
                    if(!items.contains(i)) {
                        inventory.setItem(i, Main.fillUpItemStack);
                    }
                }
            }

            Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                player.openInventory(inventory);
                InventoryManager.registerInv(inventory.hashCode(), "SHOPMAIN");
            });
        });
    }

    public static void openGui(String path, Player player) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            Inventory inventory = Bukkit.createInventory(null, 9*5, shopMain);
            List<Integer> items = new ArrayList<>();
            List<ItemStack> itemStacks = new ArrayList<>();
            for (String item : shopConfig.getConfigurationSection("items.parent." + path).getKeys(false)) {
                String materialString = shopConfig.getString("items.parent." + path + "." + item + ".material");
                if (materialString != null && !item.equals("parent")) {
                    Material material = Material.getMaterial(materialString);
                    ItemStack itemStack = new ItemStack(material);
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    List<String> lor = new ArrayList<>();
                    for (String l : lore) {
                        lor.add(l.replace("%COST%", String.valueOf(shopConfig.getDouble("items.parent." + path + "." + item + ".price"))));
                    }
                    itemMeta.setLore(lor);
                    itemMeta.setCustomModelData(5);
                    itemStack.setItemMeta(itemMeta);
                    itemStacks.add(itemStack);
                }
            }

            if (shopConfig.getConfigurationSection("items.parent." + path + ".parent") != null) {
                for (String item : shopConfig.getConfigurationSection("items.parent." + path + ".parent").getKeys(false)) {
                    String itemName = Utils.color(shopConfig.getString("items.parent." + path + ".parent." + item + ".itemName"));
                    if (itemName != null) {
                        Material material = Material.getMaterial(shopConfig.getString("items.parent." + path + ".parent." + item + ".material"));
                        int modelDataId = shopConfig.getInt("items.parent." + path + ".parent." + item + ".modelDataId");
                        boolean glow = shopConfig.getBoolean("items.parent." + path + ".parent." + item + ".glow");
                        List<String> lore = new ArrayList<>();
                        for (String l : shopConfig.getStringList("items.parent." + path + ".parent." + item + ".lore")) {
                            lore.add(Utils.color(l));
                        }
                        ItemStack itemStack = new ItemStack(material);
                        ItemMeta itemMeta = itemStack.getItemMeta();
                        itemMeta.setLore(lore);
                        itemMeta.setCustomModelData(modelDataId);
                        itemMeta.setDisplayName(itemName);
                        if (glow)
                            itemMeta.addEnchant(Glow.generateEnchant(), 1, true);
                        for (String enchant : shopConfig.getStringList("items.parent." + path + ".parent." + item + ".enchantments")) {
                            Enchantment enchantment = Enchantment.getByName(enchant);
                            if (enchantment == null) {
                                lore.add("§7" + enchant);
                            } else
                                itemMeta.addEnchant(enchantment, 1, false);
                        }
                        itemStack.setItemMeta(itemMeta);
                        itemStacks.add(itemStack);
                    }
                }
            }

            int count = 0;
            for (int i = 10; i <= 34; i++) {
                if (i % 9 == 0 || (i + 1) % 9 == 0) {
                } else {
                    if (count < itemStacks.size()) {
                        ItemStack itemStack = itemStacks.get(count);
                        inventory.setItem(i, itemStack);
                        items.add(i);
                        count++;
                    } else {
                        items.add(i);
                    }
                }
            }

            inventory.setItem(backItem.getSlot(), backItem.getItemStack());
            items.add(backItem.getSlot());

            if(Main.fillup) {
                for (int i = 0; i < (9*5); i++) {
                    if(!items.contains(i)) {
                        inventory.setItem(i, Main.fillUpItemStack);
                    }
                }
            }

            Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                player.openInventory(inventory);
                InventoryManager.registerInv(inventory.hashCode(), "SHOP " + path);
            });
        });
    }

    public static void openGuiShopBuy(Player player, String path, String oldInv) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            Inventory inventory = Bukkit.createInventory(null, 9*5, "Buy");
            Material material = Material.getMaterial(shopConfig.getString("items.parent." + path + ".material"));
            double price = shopConfig.getDouble("items.parent." + path + ".price");
            ItemStack itemStack = new ItemStack(material);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setCustomModelData(5);
            itemStack.setItemMeta(itemMeta);
            List<Integer> items = new ArrayList<>();
            List<ItemStack> itemStacks = new ArrayList<>();
            //arrangement 1 8 16 32 64
            String[] ar = shopBuyArrangement.split(";");

            for (String a : ar) {
                items.add(Integer.parseInt(a));
            }

            //1
            itemStack.setAmount(1);
            itemMeta.setLore(generateLorePrice(price));
            itemStack.setItemMeta(itemMeta);
            inventory.setItem(Integer.parseInt(ar[0]), itemStack);

            //8
            itemStack.setAmount(8);
            itemMeta.setLore(generateLorePrice(price * 8));
            itemStack.setItemMeta(itemMeta);
            inventory.setItem(Integer.parseInt(ar[1]), itemStack);

            //16
            itemStack.setAmount(16);
            itemMeta.setLore(generateLorePrice(price * 16));
            itemStack.setItemMeta(itemMeta);
            inventory.setItem(Integer.parseInt(ar[2]), itemStack);

            //32
            itemStack.setAmount(32);
            itemMeta.setLore(generateLorePrice(price * 32));
            itemStack.setItemMeta(itemMeta);
            inventory.setItem(Integer.parseInt(ar[3]), itemStack);

            //64
            itemStack.setAmount(64);
            itemMeta.setLore(generateLorePrice(price * 64));
            itemStack.setItemMeta(itemMeta);
            inventory.setItem(Integer.parseInt(ar[4]), itemStack);

            inventory.setItem(backItem.getSlot(), backItem.getItemStack());
            items.add(backItem.getSlot());

            if(Main.fillup) {
                for (int i = 0; i < (9*5); i++) {
                    if(!items.contains(i)) {
                        inventory.setItem(i, Main.fillUpItemStack);
                    }
                }
            }

            Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                player.openInventory(inventory);
                InventoryManager.registerInv(inventory.hashCode(), "SHOPBUY " + oldInv + " " + price + ";" + price * 8 + ";" + price * 16 + ";" + price * 32+ ";" + price * 64);
            });
        });
    }

    public static void openGuiShopSell(Player player, String path, String oldInv) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            Inventory inventory = Bukkit.createInventory(null, 9*5, "Sell");
            Material material = Material.getMaterial(shopConfig.getString("items.parent." + path + ".material"));
            double p = shopConfig.getDouble("items.parent." + path + ".price");
            ItemStack itemStack = new ItemStack(material);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemStack.setItemMeta(itemMeta);
            itemMeta.setCustomModelData(5);
            List<Integer> items = new ArrayList<>();
            //arrangement 1 8 16 32 64
            String[] ar = shopBuyArrangement.split(";");
            double price = (p - Utils.percent(p, percent));

            for (String a : ar) {
                items.add(Integer.parseInt(a));
            }

            //1
            itemStack.setAmount(1);
            itemMeta.setLore(generateLorePrice((int)price));
            itemStack.setItemMeta(itemMeta);
            inventory.setItem(Integer.parseInt(ar[0]), itemStack);

            //8
            itemStack.setAmount(8);
            itemMeta.setLore(generateLorePrice((int)(price*8)));
            itemStack.setItemMeta(itemMeta);
            inventory.setItem(Integer.parseInt(ar[1]), itemStack);

            //16
            itemStack.setAmount(16);
            itemMeta.setLore(generateLorePrice((int)(price*16)));
            itemStack.setItemMeta(itemMeta);
            inventory.setItem(Integer.parseInt(ar[2]), itemStack);

            //32
            itemStack.setAmount(32);
            itemMeta.setLore(generateLorePrice((int)(price*32)));
            itemStack.setItemMeta(itemMeta);
            inventory.setItem(Integer.parseInt(ar[3]), itemStack);

            //64
            itemStack.setAmount(64);
            itemMeta.setLore(generateLorePrice((int)(price*64)));
            itemStack.setItemMeta(itemMeta);
            inventory.setItem(Integer.parseInt(ar[4]), itemStack);

            inventory.setItem(backItem.getSlot(), backItem.getItemStack());
            items.add(backItem.getSlot());

            if(Main.fillup) {
                for (int i = 0; i < (9*5); i++) {
                    if(!items.contains(i)) {
                        inventory.setItem(i, Main.fillUpItemStack);
                    }
                }
            }

            Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                player.openInventory(inventory);
                InventoryManager.registerInv(inventory.hashCode(), "SHOPSELL " + oldInv + " " + (int)price + ";" + (int)(price*8) + ";" + (int)(price*16) + ";" + (int)(price*32) + ";" + (int)(price*64));
            });
        });
    }

    public static List<String> generateLorePrice(double price) {
        List<String> lo = new ArrayList<>();
        for (String l :  buyLore) {
            lo.add(l.replace("%COST%", String.valueOf(price)));
        }
        return lo;
    }

    public static boolean isBackItem(int slot) {
        return backItem.getSlot() == slot;
    }

    public static int getPrice(String path) {
        return shopConfig.getInt(path);
    }

    public static String getAction(String path, String item) {
        for (String i : shopConfig.getConfigurationSection(path).getKeys(false)) {
            if(i.equals(item)) return i;
            String itemName = Utils.color(shopConfig.getString(path + "." + i + ".itemName"));
            if(itemName.equals(item)) return i;
        }
        return null;
    }

    public static String getAction(String path, Material material) {
        for (String i : shopConfig.getConfigurationSection(path).getKeys(false)) {
            Material mat = Material.getMaterial(shopConfig.getString(path + "." + i + ".material"));
            if(material == mat) return i;
        }
        return null;
    }

}
