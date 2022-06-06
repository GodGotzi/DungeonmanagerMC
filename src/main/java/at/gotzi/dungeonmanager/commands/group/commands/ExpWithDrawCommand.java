package at.gotzi.dungeonmanager.commands.group.commands;

import at.gotzi.dungeonmanager.commands.group.GroupCommand;
import at.gotzi.dungeonmanager.commands.group.SkillCommand;
import at.gotzi.dungeonmanager.data.PlayerData;
import at.gotzi.dungeonmanager.manager.file.GroupManager;
import at.gotzi.dungeonmanager.objects.groups.Group;
import at.gotzi.dungeonmanager.objects.skills.Skill;
import at.gotzi.dungeonmanager.objects.skills.Skills;
import at.gotzi.dungeonmanager.utils.Messages;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ExpWithDrawCommand extends GroupCommand {

    @Override
    public boolean execute() {
        if (sender instanceof Player player) {
            if (SkillCommand.isDisabled(player, false)) return false;
            List<Skill> skills = PlayerData.getSkills(player.getUniqueId());
            Skill skill = null;
            boolean bool = false;
            for (Skill sks : skills) {
                if (sks.getSkills() == Skills.EXPWITHDRAWCMD) {
                    bool = true;
                    skill = sks;
                    break;
                }
            }
            if (player.hasPermission("groups.all") || bool) {
                int lvl = player.getLevel();
                if (lvl == 0) {
                    player.sendMessage(Messages.expWithDrawNoLevel);
                    return false;
                }
                player.setLevel(0);
                ItemStack itemStack = new ItemStack(Material.EXPERIENCE_BOTTLE);
                ItemMeta itemMeta = itemStack.getItemMeta();
                //itemMeta.setDisplayName(GroupManager.getStringRAM(Group.MONK, 0));
                itemMeta.addEnchant(Enchantment.LOOT_BONUS_MOBS, lvl, true);
                itemStack.setItemMeta(itemMeta);
                player.getInventory().addItem(itemStack);
                player.sendMessage(Messages.expWithDrawMsg);
            } else
                player.sendMessage(Messages.needGroup.replace("%CLASS%", GroupManager.getSkillGroups(Skills.EXPWITHDRAWCMD)));
        } else
            sender.sendMessage(Messages.onlyPlayer);
        return false;
    }
}
