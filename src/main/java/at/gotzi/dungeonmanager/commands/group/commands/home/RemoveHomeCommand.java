package at.gotzi.dungeonmanager.commands.group.commands.home;

import at.gotzi.dungeonmanager.commands.group.GroupCommand;
import at.gotzi.dungeonmanager.commands.group.SkillCommand;
import at.gotzi.dungeonmanager.data.PlayerData;
import at.gotzi.dungeonmanager.manager.file.GroupManager;
import at.gotzi.dungeonmanager.manager.player.TeleportManager;
import at.gotzi.dungeonmanager.objects.groups.others.Home;
import at.gotzi.dungeonmanager.objects.skills.Skill;
import at.gotzi.dungeonmanager.objects.skills.Skills;
import at.gotzi.dungeonmanager.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RemoveHomeCommand extends GroupCommand implements TabCompleter {

    @Override
    public boolean execute() {
        if (sender instanceof Player player) {
            if (SkillCommand.isDisabled(player, false)) return false;
            List<Skill> skills = PlayerData.getSkills(player.getUniqueId());
            Skill skill = null;
            boolean bool = false;
            for (Skill sks : skills) {
                if (sks.getSkills() == Skills.HOMECMD) {
                    bool = true;
                    skill = sks;
                    break;
                }
            }

            if (player.hasPermission("groups.all") || bool) {
                if (args.length == 1) {
                    Home home = PlayerData.getHome(args[0], player.getUniqueId());
                    if (home != null) {
                        PlayerData.removeHome(home.getId(), player.getUniqueId());
                        sender.sendMessage(Messages.homeRemove);
                    } else
                        sender.sendMessage(Messages.homeNotExist);
                } else {
                    sender.sendMessage(Messages.falseSyntaxCmd);
                }
            } else
                player.sendMessage(Messages.needGroup.replace("%CLASS%", GroupManager.getSkillGroups(Skills.HOMECMD)));
        } else
            sender.sendMessage(Messages.onlyPlayer);
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<String> commands = new ArrayList<>();
        if(sender instanceof Player player) {
            if (args.length == 1) {
                for (Home home : PlayerData.getHomes(player.getUniqueId())) {
                    commands.add(home.getId());
                }
            }
        }

        if(commands.isEmpty()) {
            return null;
        } else return commands;
    }
}
