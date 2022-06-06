package at.gotzi.dungeonmanager.commands.group.commands;

import at.gotzi.dungeonmanager.commands.group.GroupCommand;
import at.gotzi.dungeonmanager.commands.group.SkillCommand;
import at.gotzi.dungeonmanager.data.PlayerData;
import at.gotzi.dungeonmanager.manager.file.GroupManager;
import at.gotzi.dungeonmanager.manager.player.TeleportManager;
import at.gotzi.dungeonmanager.objects.skills.Skill;
import at.gotzi.dungeonmanager.objects.skills.Skills;
import at.gotzi.dungeonmanager.utils.Messages;
import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import org.bukkit.entity.Player;

import java.util.List;

public class BackCommand extends GroupCommand {

    @Override
    public boolean execute() {
        if (sender instanceof Player player) {
            if (SkillCommand.isDisabled(player, false)) return false;
            List<Skill> skills = PlayerData.getSkills(player.getUniqueId());
            Skill skill = null;
            boolean bool = false;
            for (Skill sks : skills) {
                if (sks.getSkills() == Skills.BACKCMD) {
                    bool = true;
                    skill = sks;
                    break;
                }
            }
            if (player.hasPermission("groups.all") || bool) {
                User user = Essentials.getPlugin(Essentials.class).getUser(player);
                if (user.getLastLocation() == null) {
                    player.sendMessage(Messages.noBackMsg);
                    return false;
                }
                if (user.getLastLocation().getWorld().getName().contains("dungeons")) {
                    player.sendMessage(Messages.noBackMsg);
                    return false;
                }
                TeleportManager.registerTeleport(player, user.getLastLocation());
            } else
                player.sendMessage(Messages.needGroup.replace("%CLASS%", GroupManager.getSkillGroups(Skills.BACKCMD)));
        } else
            sender.sendMessage(Messages.onlyPlayer);
        return false;
    }

}
