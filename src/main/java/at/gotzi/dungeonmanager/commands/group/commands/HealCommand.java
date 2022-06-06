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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HealCommand extends GroupCommand {

    private Main main;

    private static List<UUID> uuids = new ArrayList<>();

    public HealCommand(Main main) {
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
                if (sks.getSkills() == Skills.HEALCMD) {
                    bool = true;
                    skill = sks;
                    break;
                }
            }
            if (player.hasPermission("groups.all") || bool) {
                if (uuids.contains(player.getUniqueId())) {
                    player.sendMessage(Messages.healCoolDown);
                    return false;
                }
                player.setHealth(player.getMaxHealth());
                uuids.add(player.getUniqueId());
                player.sendMessage(Messages.healMsg);

                Bukkit.getScheduler().runTaskLaterAsynchronously(main, new Runnable() {
                    @Override
                    public void run() {
                        uuids.remove(player.getUniqueId());
                    }
                }, 600);

            } else
                player.sendMessage(Messages.needGroup.replace("%CLASS%", GroupManager.getSkillGroups(Skills.HEALCMD)).toString());
        } else
            sender.sendMessage(Messages.onlyPlayer);
        return false;
    }

}
