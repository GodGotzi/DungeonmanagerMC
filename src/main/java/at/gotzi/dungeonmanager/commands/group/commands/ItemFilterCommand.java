package at.gotzi.dungeonmanager.commands.group.commands;

import at.gotzi.dungeonmanager.Main;
import at.gotzi.dungeonmanager.commands.group.GroupCommand;
import at.gotzi.dungeonmanager.commands.group.SkillCommand;
import at.gotzi.dungeonmanager.data.PlayerData;
import at.gotzi.dungeonmanager.manager.file.GroupManager;
import at.gotzi.dungeonmanager.manager.InventoryManager;
import at.gotzi.dungeonmanager.objects.groups.Group;
import at.gotzi.dungeonmanager.objects.groups.others.Home;
import at.gotzi.dungeonmanager.objects.skills.Skill;
import at.gotzi.dungeonmanager.objects.skills.Skills;
import at.gotzi.dungeonmanager.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ItemFilterCommand extends GroupCommand implements TabCompleter {

    private final Main main;
    private final GroupManager groupManager;

    public static HashMap<UUID, List<Material>> materials = new HashMap<>();

    public ItemFilterCommand(Main main, GroupManager groupManager) {
        this.main = main;
        this.groupManager = groupManager;
    }

    @Override
    public boolean execute() {
        if (sender instanceof Player player) {
            if (SkillCommand.isDisabled(player, false)) return false;
            List<Skill> skills = PlayerData.getSkills(player.getUniqueId());
            Skill skill = null;
            boolean bool = false;
            for (Skill sks : skills) {
                if (sks.getSkills() == Skills.ITEMFILTERCMD) {
                    bool = true;
                    skill = sks;
                    break;
                }
            }
            if (player.hasPermission("groups.all") || bool) {
                if (args.length > 2 || args.length == 0) {
                    player.sendMessage(Messages.falseSyntaxCmd.replace("%CMD%", "/itemfilter menu/add/remove"));
                    return false;
                }

                if (args[0].equalsIgnoreCase("menu")) {
                    if (materials.get(player.getUniqueId()) == null) {
                        player.sendMessage(Messages.itemFilterNoMaterials);
                        return false;
                    }
                    openMenu(player);
                } else if (args[0].equalsIgnoreCase("add")) {
                    if (args.length == 2) {
                        if(Material.getMaterial(args[1]) == null) {
                            player.sendMessage(Messages.itemFilterNoMaterial);
                            return false;
                        }
                        List<Material> mats = materials.get(player.getUniqueId());
                        if(mats == null) {
                            mats = new ArrayList<>();
                        }
                        if(mats.contains(Material.getMaterial(args[1]))) {
                            player.sendMessage(Messages.itemFilterMaterialExists);
                            return false;
                        }
                        if(mats.size() == (54-18)) {
                            player.sendMessage(Messages.itemFilterMaxAmount);
                            return false;
                        }
                        mats.add(Material.getMaterial(args[1]));
                        materials.put(player.getUniqueId(), mats);
                        player.sendMessage(Messages.itemFilterAdd);
                    }
                } else if (args[0].equalsIgnoreCase("remove")) {
                    if (args.length == 2) {

                        if(materials.get(player.getUniqueId()) == null) {
                            player.sendMessage(Messages.itemFilterNoMaterial);
                            return false;
                        }

                        if(materials.get(player.getUniqueId()).contains(Material.getMaterial(args[1]))) {
                            List<Material> mats = materials.get(player.getUniqueId());
                            mats.remove(Material.getMaterial(args[1]));
                            materials.put(player.getUniqueId(), mats);
                            player.sendMessage(Messages.itemFilterRemove);
                        }
                    }
                }
            } else
                player.sendMessage(Messages.needGroup.replace("%CLASS%", GroupManager.getSkillGroups(Skills.ITEMFILTERCMD)).toString());
        } else
            sender.sendMessage(Messages.onlyPlayer);
        return false;
    }

    private void openMenu(Player player) {
        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            int size;
            int materialsSize = materials.get(player.getUniqueId()).size();
            boolean whileBool = true;
            int whileCount = 9;
            while(whileBool) {
                if(materialsSize <= whileCount) {
                    whileBool = false;
                }
                whileCount += 9;
            }

            size = 18+whileCount-9;

            Bukkit.getScheduler().runTask(main, () -> {
                Inventory inv = Bukkit.createInventory(null, size, groupManager.getString(Group.MATHEMATICIAN, "itemfiltermenuname"));
                int slot = 8;
                for (Material material : materials.get(player.getUniqueId())) {
                    ItemStack item = new ItemStack(material);
                    slot++;
                    inv.setItem(slot, item);
                }

                for (int i = 0; i < 9; i++) {
                    ItemStack itemStack = new ItemStack(Material.GRAY_STAINED_GLASS);
                    inv.setItem(i, itemStack);
                }
                for (int i = size-9; i < size; i++) {
                    ItemStack itemStack = new ItemStack(Material.GRAY_STAINED_GLASS);
                    inv.setItem(i, itemStack);
                }
                player.openInventory(inv);
                InventoryManager.registerInv(inv.hashCode(), "ITEMFILTERGUI");
            });
        });
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<String> commands = new ArrayList<>();
        if(sender instanceof Player player) {
            if (args.length == 1) {
                commands.add("menu");
                commands.add("add");
                commands.add("remove");
            }
        }

        if(commands.isEmpty()) {
            return null;
        } else return commands;
    }

}
