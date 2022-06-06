package at.gotzi.dungeonmanager.commands.group.commands.home;

import at.gotzi.dungeonmanager.commands.group.GroupCommand;
import at.gotzi.dungeonmanager.commands.group.SkillCommand;
import at.gotzi.dungeonmanager.data.PlayerData;
import at.gotzi.dungeonmanager.manager.file.GroupManager;
import at.gotzi.dungeonmanager.objects.groups.others.Home;
import at.gotzi.dungeonmanager.objects.skills.Skill;
import at.gotzi.dungeonmanager.objects.skills.Skills;
import at.gotzi.dungeonmanager.utils.Messages;
import org.bukkit.entity.Player;

import java.util.List;

public class SetHomeCommand extends GroupCommand {

    @Override
    public boolean execute() {
        if (sender instanceof Player player) {
            if (SkillCommand.isDisabled(player, false)) return false;
            List<Skill> skills = PlayerData.getSkills(player.getUniqueId());
            Skill skill = null;
            double boost = 0;
            boolean bool = false;
            for (Skill sks : skills) {
                if (sks.getSkills() == Skills.HOMECMD) {
                    bool = true;
                    boost += sks.getStrength();
                    break;
                }
            }

            if (player.hasPermission("groups.all") || bool) {
                if (boost == 0) boost = 3;
                if (args.length == 1) {
                    if (PlayerData.getHomes(player.getUniqueId()).size()+1 > boost) {
                        sender.sendMessage(Messages.reachedMaxHomes);
                        return false;
                    }
                    Home home = PlayerData.getHome(args[0], player.getUniqueId());
                    if (home == null) {
                        PlayerData.addHome(args[0], player.getUniqueId(), player.getLocation());
                        sender.sendMessage(Messages.homeAdd);
                    } else
                        sender.sendMessage(Messages.homeAlreadyExist);
                } else {
                    sender.sendMessage(Messages.falseSyntaxCmd);
                }
            } else
                player.sendMessage(Messages.needGroup.replace("%CLASS%", GroupManager.getSkillGroups(Skills.HOMECMD)));
        } else
            sender.sendMessage(Messages.onlyPlayer);
        return false;
    }

}
