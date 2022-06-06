package at.gotzi.dungeonmanager.manager.file;

import at.gotzi.dungeonmanager.Main;
import at.gotzi.dungeonmanager.data.PlayerData;
import at.gotzi.dungeonmanager.manager.InventoryManager;
import at.gotzi.dungeonmanager.objects.BossBarObj;
import at.gotzi.dungeonmanager.objects.enchantments.Glow;
import at.gotzi.dungeonmanager.objects.pets.animals.*;
import at.gotzi.dungeonmanager.objects.playerutils.Title;
import at.gotzi.dungeonmanager.utils.Messages;
import at.gotzi.dungeonmanager.utils.Utils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityLiving;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftMetaSpawnEgg;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class PetManager {

    private final UUID uuid;
    private final Location location;
    private final String name;

    private static HashMap<UUID, UUID> pets = new HashMap<>();
    public static List<EntityType> entityTypes = new ArrayList<>();

    public static List<String> lore = new ArrayList<>();

    public static List<Lvl> lvls = new ArrayList<>();
    public static LvlUp levelUp;
    public static BossBarObj levelExp;
    public static BarStyle barStyle;

    public static class Lvl {
        private final int damage;
        private final int attackSpeed;
        private final int level;

        public Lvl(int damage, int attackSpeed, int level) {
            this.damage = damage;
            this.attackSpeed = attackSpeed;
            this.level = level;
        }

        public int getLevel() {
            return level;
        }

        public int getAttackSpeed() {
            return attackSpeed;
        }

        public int getDamage() {
            return damage;
        }
    }

    public static class LvlUp {
        private final String title;
        private final String subTitle;
        private final int fadeIn;
        private final int fadeOut;
        private final int stay;


        public LvlUp(String title, String subTitle, int fadeIn, int fadeOut, int stay) {
            this.title = title;
            this.subTitle = subTitle;
            this.fadeIn = fadeIn;
            this.fadeOut = fadeOut;
            this.stay = stay;
        }

        public String getTitle() {
            return title;
        }

        public String getSubTitle() {
            return subTitle;
        }

        public int getFadeIn() {
            return fadeIn;
        }

        public int getFadeOut() {
            return fadeOut;
        }

        public int getStay() {
            return stay;
        }
    }


    private static final YamlConfiguration petConfig = YamlConfiguration.loadConfiguration(new File("plugins//DungeonManager//pets.yml"));

    public PetManager(UUID uuid, String name, Location location) {
        this.uuid = uuid;
        this.location = location;
        this.name = name;
    }

    public static void load() {
        try {
            petConfig.load(new File("plugins//DungeonManager//pets.yml"));
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        String title = Utils.color(petConfig.getString("levelUp." + "title"));
        String subTitle = Utils.color(petConfig.getString("levelUp." + "subTitle"));
        int fadeIn = petConfig.getInt("levelUp." + "fadeIn");
        int fadeOut = petConfig.getInt("levelUp." + "fadeOut");
        int stay = petConfig.getInt("levelUp." + "stay");
        levelUp = new LvlUp(title, subTitle, fadeIn, fadeOut, stay);
        levelExp = new BossBarObj(Utils.color(petConfig.getString("levelExpShow.text")), BarColor.valueOf(petConfig.getString("levelExpShow.barColor")));
        barStyle = BarStyle.valueOf(petConfig.getString("levelExpShow.barStyle"));

        List<String> l = new ArrayList<>();
        for (String lo : petConfig.getStringList("item.lore")) {
            l.add(Utils.color(lo));
        }
        lore = l;

        for (String i : petConfig.getConfigurationSection("level").getKeys(false)) {
            int damage = petConfig.getInt("level." + String.valueOf(i) + ".damage");
            int attackSpeed = petConfig.getInt("level." + String.valueOf(i) + ".attackSpeed");
            lvls.add(new Lvl(damage, attackSpeed, Integer.parseInt(i)));
        }
        entityTypes.clear();
        entityTypes.add(EntityType.CHICKEN);
        entityTypes.add(EntityType.COW);
        entityTypes.add(EntityType.FOX);
        entityTypes.add(EntityType.PIG);
        entityTypes.add(EntityType.RABBIT);
        entityTypes.add(EntityType.WOLF);
        entityTypes.add(EntityType.CAT);
        entityTypes.add(EntityType.IRON_GOLEM);

        Utils.info("Pets loaded!");
    }

    public static Player getOwner(UUID uuid) {
        for (UUID uuid1 : pets.keySet()) {
            if(Objects.equals(pets.get(uuid).toString(), uuid1.toString())) {
                return Bukkit.getPlayer(uuid1);
            }
        }
        return null;
    }

    public static void killAllPets() {
        for (UUID p : pets.values()) {
            Bukkit.getEntity(p).remove();
        }
    }

    public static org.bukkit.entity.Entity getPet(UUID uuid) {
        return Bukkit.getEntity(pets.get(uuid));
    }

    public void spawn(Player player, EntityType entityType) {
        if(pets.containsKey(player.getUniqueId())) {
            removePet(player);
        }

        UUID uuid = null;
        Entity entity = null;
        switch (entityType) {
            case CHICKEN:
                Chicken var1 = new Chicken(this.location, player, this.name);
                uuid = var1.getUniqueID();
                entity = var1;
                break;
            case COW:
                Cow var2 = new Cow(this.location, player, this.name);
                uuid = var2.getUniqueID();
                entity = var2;
                break;
            case FOX:
                Fox var3 = new Fox(this.location, (EntityLiving) ((CraftPlayer)player).getHandle(), this.name);
                uuid = var3.getUniqueID();
                entity = var3;
                break;
            case PIG:
                Pig var4 = new Pig(this.location, player, this.name);
                uuid = var4.getUniqueID();
                entity = var4;
                break;
            case RABBIT:
                Rabbit var5 = new Rabbit(this.location, player, this.name);
                uuid = var5.getUniqueID();
                entity = var5;
                break;  
            case WOLF:
                Wolf var6 = new Wolf(this.location, player, this.name);
                uuid = var6.getUniqueID();
                entity = var6;
                break;
            case CAT:
                Cat var7 = new Cat(this.location, player, this.name);
                uuid = var7.getUniqueID();
                entity = var7;
                break;
            case IRON_GOLEM:
                Golem var8 = new Golem(this.location, player, this.name);
                uuid = var8.getUniqueID();
                entity = var8;
                break;
            case GHAST:
                Ghast var9 = new Ghast(this.location, player, this.name);
                uuid = var9.getUniqueID();
                entity = var9;
                break;
            default:
                return;
        }

        pets.put(player.getUniqueId(), uuid);
        ((CraftWorld)player.getWorld()).getHandle().addEntity(entity);
        if (entityType == EntityType.GHAST)
            entity.enderTeleportAndLoad(this.location.getX(), this.location.getY()+10, this.location.getZ());
        else
            entity.enderTeleportAndLoad(this.location.getX(), this.location.getY(), this.location.getZ());
        player.sendMessage(Messages.petSummoned);
    }

    /**
     * If the player has a pet, remove it
     *
     * @param player The player who's pet you want to despawn.
     */
    public static void deSpawn(Player player) {
        if(!hasPet(player)) {
            return;
        }

        org.bukkit.entity.Entity entity = Bukkit.getEntity(pets.get(player.getUniqueId()));
        if (entity == null) {
            pets.remove(player.getUniqueId());
            return;
        }
        entity.remove();
        pets.remove(player.getUniqueId());
    }

    public static void openPetMenu(Player player) {
        new BukkitRunnable() {

            @Override
            public void run() {
                Inventory inventory = Bukkit.createInventory(null, 9 * 5, "Pet Menu");

                List<Integer> items = new ArrayList<>();
                new BukkitRunnable() {

                    @Override
                    public void run() {
                        int count = 0;
                        for(int i = 10; i <=34;i++) {
                            if (i % 9 == 0 || (i + 1) % 9 == 0) {
                            } else {
                                if (count < entityTypes.size()) {
                                    EntityType entityType = entityTypes.get(count);
                                    ItemStack itemStack = getPetItemStack(entityType, player);
                                    inventory.setItem(i, itemStack);
                                    items.add(i);
                                    count++;
                                } else {
                                    items.add(i);
                                }
                            }
                        }
                    }
                }.runTask(Main.getInstance());

                if(Main.fillup) {
                    for (int i = 0; i < (9 * 5); i++) {
                        if (!items.contains(i)) {
                            inventory.setItem(i, Main.fillUpItemStack);
                        }
                    }
                }

                Bukkit.getScheduler().runTask(Main.getInstance(), ()-> {
                    player.openInventory(inventory);
                    InventoryManager.registerInv(inventory.hashCode(), "PETMENU");
                });
            }
        }.runTaskAsynchronously(Main.getInstance());
    }

    private static ItemStack getPetItemStack(EntityType entityType, Player player) {
        int index = entityTypes.indexOf(entityType);
        int exp = PlayerData.getPetsExp(player.getUniqueId(), index);
        int lvl = Utils.calPetLvl(exp);
        String level;
        int damage;
        double attackSpeed;
        boolean bool = false;
        boolean bool2 = false;
        if (PetManager.hasPet(player))
            if (PetManager.getPet(player.getUniqueId()).getType() == entityType)
                bool = true;
        if ((Utils.calPetLvl(exp) > (PetManager.lvls.size()-1))) {
            lvl = lvls.size()-1;
            level = "MAX";
            damage = lvls.get(lvls.size()-1).getDamage();
            attackSpeed = (double)1/(double)(lvls.get(lvls.size()-1).getAttackSpeed()/(double)20);
            bool2 = true;
        } else {
            level = String.valueOf(lvl);
            damage = lvls.get(lvl-1).getDamage();
            attackSpeed = (double)1/(double)(lvls.get(lvl-1).getAttackSpeed()/(double)20);
        }
        int nextLevel;
        if (lvl != 1)
            nextLevel = (int)(((double)exp-Utils.calPetExp(lvl-1))/((double)Utils.calPetExp(lvl)-Utils.calPetExp(lvl-1))*(double)100);
        else
            nextLevel = (int)(((double)exp/(double)20)*(double)100);

        ItemStack itemStack;
        if (entityType != EntityType.ENDER_DRAGON && entityType != EntityType.IRON_GOLEM) {
            itemStack = new ItemStack(Material.getMaterial(entityType.toString() + "_SPAWN_EGG"));
        } else {
            itemStack = new ItemStack(Material.IRON_BLOCK);
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("§7" + entityType);
        List<String> l = new ArrayList<>();
        for (String lo : lore) {
            if (!bool2 || !lo.contains("%NLVL%")) {
                l.add(lo
                        .replace("%LVL%", level)
                        .replace("%DAMAGE%", String.valueOf(damage))
                        .replace("%ASPEED%", String.valueOf(attackSpeed))
                        .replace("%NLVL%", String.valueOf(nextLevel)));
            }
        }

        if (bool) {
            l.add("§7SELECTED");
            l.add("§7Click for despawn");
            itemMeta.addEnchant(Glow.generateEnchant(), 1, true);
            itemMeta.setCustomModelData(6);
        } else {
            itemMeta.setCustomModelData(5);
        }
        itemMeta.setLore(l);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static void removePet(Player player) {
        deSpawn(player);
        pets.remove(player.getUniqueId());
    }

    public static void removePet(UUID uuid) {
        for (UUID uuid1 : pets.keySet()) {
            if (pets.get(uuid1).toString().equals(uuid.toString())) {
                pets.remove(uuid1);
                if (Bukkit.getEntity(uuid) != null)
                    Bukkit.getEntity(uuid).remove();
            }
        }
    }

    public static boolean isPet(UUID uuid) {
        return pets.containsValue(uuid);
    }

    public static void tpPetSameWorld(Player player) {
        org.bukkit.entity.Entity entity = Bukkit.getEntity(pets.get(player.getUniqueId()));
        if (entity == null) {
            pets.remove(player.getUniqueId());
            return;
        }
        entity.teleport(player.getLocation());
    }
    public static void tpPetSameWorld(Player player, Location location) {
        org.bukkit.entity.Entity entity = Bukkit.getEntity(pets.get(player.getUniqueId()));
        if (entity == null) {
            pets.remove(player.getUniqueId());
            return;
        }
        entity.teleport(location);
    }

    public static void tpPet(Player player, Location location) {
        org.bukkit.entity.Entity entity = Bukkit.getEntity(pets.get(player.getUniqueId()));

        if (entity == null) {
            pets.remove(player.getUniqueId());
            return;
        }
        String name = entity.getCustomName();
        UUID uuid = null;
        Entity e = null;
        switch (entity.getType()) {
            case CHICKEN:
                Chicken var1 = new Chicken(location, player, name);
                uuid = var1.getUniqueID();
                e = var1;
                break;
            case COW:
                Cow var2 = new Cow(location, player, name);
                uuid = var2.getUniqueID();
                e = var2;
                break;
            case FOX:
                Fox var3 = new Fox(location, ((CraftPlayer)player).getHandle(), name);
                uuid = var3.getUniqueID();
                e = var3;
                break;
            case PIG:
                Pig var4 = new Pig(location, player, name);
                uuid = var4.getUniqueID();
                e = var4;
                break;
            case RABBIT:
                Rabbit var5 = new Rabbit(location, player, name);
                uuid = var5.getUniqueID();
                e = var5;
                break;
            case WOLF:
                Wolf var6 = new Wolf(location, player, name);
                uuid = var6.getUniqueID();
                e = var6;
                break;
            case CAT:
                Cat var7 = new Cat(location, player, name);
                uuid = var7.getUniqueID();
                e = var7;
                break;
            case IRON_GOLEM:
                Golem var8 = new Golem(location, player, name);
                uuid = var8.getUniqueID();
                e = var8;
                break;
            case GHAST:
                Ghast var9 = new Ghast(location, player, name);
                uuid = var9.getUniqueID();
                e = var9;
                break;
            default:
                return;
        }

        pets.remove(player.getUniqueId());
        pets.put(player.getUniqueId(), uuid);
        ((CraftWorld)player.getWorld()).getHandle().addEntity(e);
        if (entity.getType() == EntityType.GHAST)
            e.enderTeleportAndLoad(location.getX(), location.getY()+10, location.getZ());
        else
            e.enderTeleportAndLoad(location.getX(), location.getY(), location.getZ());
        entity.remove();
    }

    public static void tpPet(Player player) {
        org.bukkit.entity.Entity entity = Bukkit.getEntity(pets.get(player.getUniqueId()));
        if (entity == null) {
            pets.remove(player.getUniqueId());
            return;
        }

        String name = entity.getCustomName();
        Location location = player.getLocation();

        UUID uuid = null;
        Entity e = null;
        switch (entity.getType()) {
            case CHICKEN:
                Chicken var1 = new Chicken(location, player, name);
                uuid = var1.getUniqueID();
                e = var1;
                break;
            case COW:
                Cow var2 = new Cow(location, player, name);
                uuid = var2.getUniqueID();
                e = var2;
                break;
            case FOX:
                Fox var3 = new Fox(location, ((CraftPlayer)player).getHandle(), name);
                uuid = var3.getUniqueID();
                e = var3;
                break;
            case PIG:
                Pig var4 = new Pig(location, player, name);
                uuid = var4.getUniqueID();
                e = var4;
                break;
            case RABBIT:
                Rabbit var5 = new Rabbit(location, player, name);
                uuid = var5.getUniqueID();
                e = var5;
                break;
            case WOLF:
                Wolf var6 = new Wolf(location, player, name);
                uuid = var6.getUniqueID();
                e = var6;
                break;
            case CAT:
                Cat var7 = new Cat(location, player, name);
                uuid = var7.getUniqueID();
                e = var7;
                break;
            case IRON_GOLEM:
                Golem var8 = new Golem(location, player, name);
                uuid = var8.getUniqueID();
                e = var8;
                break;
            case GHAST:
                Ghast var9 = new Ghast(location, player, name);
                uuid = var9.getUniqueID();
                e = var9;
                break;
            default:
                return;
        }

        pets.remove(player.getUniqueId());
        pets.put(player.getUniqueId(), uuid);
        ((CraftWorld)player.getWorld()).getHandle().addEntity(e);
        if (entity.getType() == EntityType.GHAST)
            e.enderTeleportAndLoad(location.getX(), location.getY()+10, location.getZ());
        else
            e.enderTeleportAndLoad(location.getX(), location.getY(), location.getZ());
        entity.remove();
    }

    public static boolean hasPet(Player player) {
        return pets.containsKey(player.getUniqueId());
    }
    
    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

}
