package at.gotzi.dungeonmanager.commands.group.commands;

import at.gotzi.dungeonmanager.commands.group.GroupCommand;
import at.gotzi.dungeonmanager.commands.group.SkillCommand;
import at.gotzi.dungeonmanager.data.PlayerData;
import at.gotzi.dungeonmanager.manager.file.GroupManager;
import at.gotzi.dungeonmanager.objects.groups.Group;
import at.gotzi.dungeonmanager.objects.skills.Skill;
import at.gotzi.dungeonmanager.objects.skills.Skills;
import at.gotzi.dungeonmanager.utils.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PTimeCommand extends GroupCommand implements TabCompleter {

    @Override
    public boolean execute() {
        if (sender instanceof Player player) {
            if (SkillCommand.isDisabled(player, false)) return false;
            List<Skill> skills = PlayerData.getSkills(player.getUniqueId());
            Skill skill = null;
            boolean bool = false;
            for (Skill sks : skills) {
                if (sks.getSkills() == Skills.PTIMECMD) {
                    bool = true;
                    skill = sks;
                    break;
                }
            }
            if (player.hasPermission("groups.all") || bool) {
                if (args.length != 1) {
                    player.sendMessage(Messages.falseSyntaxCmd.replace("%CMD%", "/ptime day/night/<ticks>"));
                    return false;
                }

                if (args[0].equalsIgnoreCase("day")) {
                    player.setPlayerTime(6000, true);
                    player.sendMessage(Messages.pTimeMsg.replace("%TIME%", "day"));
                } else if (args[0].equalsIgnoreCase("night")) {
                    player.setPlayerTime(18000, true);
                    player.sendMessage(Messages.pTimeMsg.replace("%TIME%", "night"));
                } else {
                    try {
                        int num = Integer.parseInt(args[0]);
                        player.setPlayerTime(num, true);
                        return false;
                    } catch (NumberFormatException e) {
                        player.sendMessage(Messages.falseSyntaxCmd.replace("%CMD%", "/ptime day/night/<ticks>"));
                        return false;
                    }
                }
            } else
                player.sendMessage(Messages.needGroup.replace("%CLASS%", GroupManager.getSkillGroups(Skills.PTIMECMD)).toString());
        } else
            sender.sendMessage(Messages.onlyPlayer);
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<String> commands = new ArrayList<>();
        if(sender instanceof Player player) {
            if (args.length == 1) {
                commands.add("day");
                commands.add("night");
            }
        }

        if(commands.isEmpty()) {
            return null;
        } else return commands;
    }
}
