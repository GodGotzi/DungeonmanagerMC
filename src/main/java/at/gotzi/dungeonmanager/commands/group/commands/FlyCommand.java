package at.gotzi.dungeonmanager.commands.group.commands;

import at.gotzi.dungeonmanager.commands.group.GroupCommand;
import at.gotzi.dungeonmanager.commands.group.SkillCommand;
import at.gotzi.dungeonmanager.data.PlayerData;
import at.gotzi.dungeonmanager.manager.file.GroupManager;
import at.gotzi.dungeonmanager.objects.skills.Skill;
import at.gotzi.dungeonmanager.objects.skills.Skills;
import at.gotzi.dungeonmanager.utils.Messages;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FlyCommand extends GroupCommand {

    public static List<UUID> overworldList = new ArrayList<>();
    public static List<UUID> netherendList = new ArrayList<>();

    @Override
    public boolean execute() {
        if (sender instanceof Player player) {
            if (SkillCommand.isDisabled(player, false)) return false;
            if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) {
                player.sendMessage("§6You are in " + player.getGameMode() + " Mode. Cannot apply /fly!");
            }

            List<Skill> skills = PlayerData.getSkills(player.getUniqueId());
            boolean overworld = false;
            boolean netherend = false;
            for (Skill sks : skills) {
                if (sks.getSkills() == Skills.FLYCMDOVERWORLD) {
                    overworld = true;
                } else if (sks.getSkills() == Skills.FLYCMDNETHEREND) {
                    netherend = true;
                }
            }
            String worldName = player.getWorld().getName();
            if (player.hasPermission("groups.all") || overworld && netherend) {
                if (worldName.contains("resource")) {
                    if (!player.getAllowFlight()) {
                        player.setAllowFlight(true);
                        player.setFlying(true);
                        player.setFlySpeed(0.05f);
                        overworldList.add(player.getUniqueId());
                        netherendList.add(player.getUniqueId());
                    } else {
                        player.setAllowFlight(false);
                        player.setFlying(false);
                        overworldList.remove(player.getUniqueId());
                        netherendList.remove(player.getUniqueId());
                    }
                } else {
                    player.sendMessage(Messages.flyNotAllowed);
                }
            } else if (overworld) {
                if (worldName.contains("resource/overworld")) {
                    if (!player.getAllowFlight()) {
                        player.setAllowFlight(true);
                        player.setFlying(true);
                        player.setFlySpeed(0.05f);
                        overworldList.add(player.getUniqueId());
                    } else {
                        player.setAllowFlight(false);
                        player.setFlying(false);
                        overworldList.remove(player.getUniqueId());
                    }
                } else if (worldName.contains("resource/end") || worldName.contains("resource/nether")) {
                    player.sendMessage(Messages.needGroup.replace("%CLASS%", GroupManager.getSkillGroups(Skills.FLYCMDNETHEREND)));
                } else
                    player.sendMessage(Messages.flyNotAllowed);
            } else if (netherend) {
                if (worldName.contains("resource/end") || worldName.contains("resource/nether")) {
                    if (!player.getAllowFlight()) {
                        player.setAllowFlight(true);
                        player.setFlying(true);
                        player.setFlySpeed(0.05f);
                        netherendList.add(player.getUniqueId());
                    } else {
                        player.setAllowFlight(false);
                        player.setFlying(false);
                        netherendList.remove(player.getUniqueId());
                    }
                } else if (worldName.contains("resource/overworld")) {
                    player.sendMessage(Messages.needGroup.replace("%CLASS%", GroupManager.getSkillGroups(Skills.FLYCMDOVERWORLD)));
                } else
                    player.sendMessage(Messages.flyNotAllowed);
            } else {
                if (worldName.contains("resource/end") || worldName.contains("resource/nether"))
                    player.sendMessage(Messages.needGroup.replace("%CLASS%", GroupManager.getSkillGroups(Skills.FLYCMDOVERWORLD)));
                else if (worldName.contains("resource/overworld"))
                    player.sendMessage(Messages.needGroup.replace("%CLASS%", GroupManager.getSkillGroups(Skills.FLYCMDOVERWORLD)));
                else
                    player.sendMessage(Messages.needGroup.replace("%CLASS%", GroupManager.getSkillGroups(Skills.FLYCMDOVERWORLD)) + " or " + GroupManager.getSkillGroups(Skills.FLYCMDNETHEREND));
            }
        } else
            sender.sendMessage(Messages.onlyPlayer);
        return false;
    }
}
