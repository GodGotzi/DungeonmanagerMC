package at.gotzi.dungeonmanager.commands.group.commands;

import at.gotzi.dungeonmanager.commands.group.GroupCommand;
import at.gotzi.dungeonmanager.commands.group.SkillCommand;
import at.gotzi.dungeonmanager.data.PlayerData;
import at.gotzi.dungeonmanager.manager.file.GroupManager;
import at.gotzi.dungeonmanager.objects.skills.Skill;
import at.gotzi.dungeonmanager.objects.skills.Skills;
import at.gotzi.dungeonmanager.utils.Messages;
import me.gsit.main.GSitMain;
import me.gsit.manager.PSitManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SitPlayerHead extends GroupCommand implements TabCompleter {


    @Override
    public boolean execute() {
        if (sender instanceof Player player) {
            if (SkillCommand.isDisabled(player, false)) return false;
            List<Skill> skills = PlayerData.getSkills(player.getUniqueId());
            Skill skill = null;
            boolean bool = false;
            for (Skill sks : skills) {
                if (sks.getSkills() == Skills.SITONPLAYERHEADS) {
                    bool = true;
                    skill = sks;
                    break;
                }
            }
            if (player.hasPermission("groups.all") || bool) {
                if (args.length == 1) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target == null) {
                        sender.sendMessage(Messages.playerNotExist);
                        return false;
                    }

                    if (target.getWorld() == player.getWorld() && target.getLocation().distance(player.getLocation()) < 5) {
                        PSitManager pSitManager = new PSitManager(GSitMain.getInstance());
                        if (!pSitManager.sitOnPlayer(player, target)) {
                            sender.sendMessage(Messages.sitPlayerHeadNotRange);
                        }
                    } else
                        sender.sendMessage(Messages.sitPlayerHeadNotRange);
                } else
                    sender.sendMessage(Messages.falseSyntaxCmd);
            } else
                player.sendMessage(Messages.needGroup.replace("%CLASS%", GroupManager.getSkillGroups(Skills.SITONPLAYERHEADS)).toString());
        } else
            sender.sendMessage(Messages.onlyPlayer);
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<String> commands = new ArrayList<>();
        if(sender instanceof Player player) {
            if (args.length == 1) {
                for (Player player1 : Bukkit.getOnlinePlayers()) {
                    commands.add(player1.getName());
                }
            }
        }

        if(commands.isEmpty()) {
            return null;
        } else return commands;
    }
}
