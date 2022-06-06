package at.gotzi.dungeonmanager.commands.group.commands;

import at.gotzi.dungeonmanager.Main;
import at.gotzi.dungeonmanager.commands.group.GroupCommand;
import at.gotzi.dungeonmanager.commands.group.SkillCommand;
import at.gotzi.dungeonmanager.data.PlayerData;
import at.gotzi.dungeonmanager.manager.file.GroupManager;
import at.gotzi.dungeonmanager.objects.groups.Group;
import at.gotzi.dungeonmanager.objects.skills.Skill;
import at.gotzi.dungeonmanager.objects.skills.Skills;
import at.gotzi.dungeonmanager.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.List;

public class StackCommand extends GroupCommand {

    private final Main main;

    public StackCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean execute() {
        if (sender instanceof Player player) {
            if (SkillCommand.isDisabled(player, false)) return false;
            List<Skill> skills = PlayerData.getSkills(player.getUniqueId());
            Skill skill = null;
            boolean bool = false;
            for (Skill sks : skills) {
                if (sks.getSkills() == Skills.STACKCMD) {
                    bool = true;
                    skill = sks;
                    break;
                }
            }
            if (player.hasPermission("groups.all") || bool) {
                PlayerInventory inv = player.getInventory();
                ItemStack itemStack = inv.getItemInMainHand();
                int maxSize = itemStack.getMaxStackSize();
                if (maxSize == 1) {
                    return false;
                }

                Bukkit.getScheduler().runTaskAsynchronously(main, new Runnable() {
                    @Override
                    public void run() {
                        int amount = 0;
                        ItemStack[] itemStacks = inv.getContents();
                        for (ItemStack iS : itemStacks) {
                            if (iS != null) {
                                if (iS.getItemMeta().getDisplayName().equals(itemStack.getItemMeta().getDisplayName()) &&
                                        iS.getType() == itemStack.getType() &&
                                        iS.getItemMeta().getEnchants() == itemStack.getItemMeta().getEnchants()) {
                                    inv.remove(iS);
                                    if (iS.getAmount() == 1) {
                                        amount++;
                                    } else
                                        inv.addItem(iS);
                                }
                            }
                        }
                        for (int i = 0; i < amount; i++) {
                            itemStack.setAmount(1);
                            inv.addItem(itemStack);
                        }
                    }
                });
            } else
                player.sendMessage(Messages.needGroup.replace("%CLASS%", GroupManager.getSkillGroups(Skills.STACKCMD)).toString());
        } else
            sender.sendMessage(Messages.onlyPlayer);
        return false;
    }
}
