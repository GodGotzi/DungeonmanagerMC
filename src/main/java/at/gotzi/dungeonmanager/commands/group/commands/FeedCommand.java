package at.gotzi.dungeonmanager.commands.group.commands;

import at.gotzi.dungeonmanager.commands.group.GroupCommand;
import at.gotzi.dungeonmanager.commands.group.SkillCommand;
import at.gotzi.dungeonmanager.data.PlayerData;
import at.gotzi.dungeonmanager.listener.group.FoodLevelChangeListener;
import at.gotzi.dungeonmanager.manager.file.GroupManager;
import at.gotzi.dungeonmanager.objects.groups.Group;
import at.gotzi.dungeonmanager.objects.skills.Skill;
import at.gotzi.dungeonmanager.objects.skills.Skills;
import at.gotzi.dungeonmanager.utils.Messages;
import org.bukkit.entity.Player;

import java.util.List;

public class FeedCommand extends GroupCommand {

    @Override
    public boolean execute() {
        if (sender instanceof Player player) {
            if (SkillCommand.isDisabled(player, false)) return false;
            List<Skill> skills = PlayerData.getSkills(player.getUniqueId());
            Skill skill = null;
            boolean bool = false;
            for (Skill sks : skills) {
                if (sks.getSkills() == Skills.FEEDCMD) {
                    bool = true;
                    skill = sks;
                    break;
                }
            }
            if (player.hasPermission("groups.all") || bool) {
                player.setFoodLevel(20);
                FoodLevelChangeListener.setMaxFoodLevel(player);
                player.sendMessage(Messages.feedMsg);
            } else
                player.sendMessage(Messages.needGroup.replace("%CLASS%", GroupManager.getSkillGroups(Skills.FEEDCMD)));
        } else
            sender.sendMessage(Messages.onlyPlayer);
        return false;
    }
}
