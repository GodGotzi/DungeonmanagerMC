package at.gotzi.dungeonmanager.commands.group.commands;

import at.gotzi.dungeonmanager.commands.group.GroupCommand;
import at.gotzi.dungeonmanager.commands.group.SkillCommand;
import at.gotzi.dungeonmanager.data.PlayerData;
import at.gotzi.dungeonmanager.manager.file.GroupManager;
import at.gotzi.dungeonmanager.objects.groups.Group;
import at.gotzi.dungeonmanager.objects.skills.Skill;
import at.gotzi.dungeonmanager.objects.skills.Skills;
import at.gotzi.dungeonmanager.utils.Messages;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NearCommand extends GroupCommand {

    private final GroupManager groupManager;

    public NearCommand(GroupManager groupManager) {
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
                if (sks.getSkills() == Skills.NEARCMD) {
                    bool = true;
                    skill = sks;
                    break;
                }
            }


            if (player.hasPermission("groups.all") || bool) {
                StringBuilder players = new StringBuilder();
                int range = 150;
                if (skill != null)
                    range = skill.getStrength();
                System.out.println(range);
                List<Entity> entities = player.getNearbyEntities(range, range, range);
                List<Entity> entityList = new ArrayList<>();

                for (Entity entity : entities) {
                    if (entity instanceof Player target) {
                        entityList.add(target);
                        players.append(target.getName()).append(", ");
                    }
                }

                if(entityList.isEmpty()) {
                    player.sendMessage(Messages.nearNoEntity);
                    return false;
                }

                player.sendMessage(Messages.nearMsg.replace("%PLAYERS%", players));
            } else
                player.sendMessage(Messages.needGroup.replace("%CLASS%", GroupManager.getSkillGroups(Skills.NEARCMD)).toString());
        } else
            sender.sendMessage(Messages.onlyPlayer);
        return false;
    }
}
