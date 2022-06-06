package at.gotzi.dungeonmanager.commands.group.commands;

import at.gotzi.dungeonmanager.commands.group.GroupCommand;
import at.gotzi.dungeonmanager.commands.group.SkillCommand;
import at.gotzi.dungeonmanager.data.PlayerData;
import at.gotzi.dungeonmanager.manager.file.GroupManager;
import at.gotzi.dungeonmanager.objects.groups.Group;
import at.gotzi.dungeonmanager.objects.skills.Skill;
import at.gotzi.dungeonmanager.objects.skills.Skills;
import at.gotzi.dungeonmanager.utils.Messages;
import org.bukkit.WeatherType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PWeatherCommand extends GroupCommand implements TabCompleter {

    @Override
    public boolean execute() {
        if (sender instanceof Player player) {
            if (SkillCommand.isDisabled(player, false)) return false;
            List<Skill> skills = PlayerData.getSkills(player.getUniqueId());
            Skill skill = null;
            boolean bool = false;
            for (Skill sks : skills) {
                if (sks.getSkills() == Skills.CRAFTCMD) {
                    bool = true;
                    skill = sks;
                    break;
                }
            }
            if (player.hasPermission("groups.all") || bool) {
                if (args.length != 1) {
                    player.sendMessage(Messages.falseSyntaxCmd.replace("%CMD%", "/pweather clear/rain"));
                    return false;
                }

                if (args[0].equalsIgnoreCase("clear")) {
                    player.setPlayerWeather(WeatherType.CLEAR);
                    player.sendMessage(Messages.pWeatherMsg.replace("%W%", "weather clear"));
                } else if(args[0].equalsIgnoreCase("rain")) {
                    player.setPlayerWeather(WeatherType.DOWNFALL);
                    player.sendMessage(Messages.pWeatherMsg.replace("%W%", "weather rain"));
                }

            } else
                player.sendMessage(Messages.needGroup.replace("%CLASS%", GroupManager.getSkillGroups(Skills.PWEATHERCMD)).toString());
        } else
            sender.sendMessage(Messages.onlyPlayer);
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<String> commands = new ArrayList<>();
        if(sender instanceof Player player) {
            if (args.length == 1) {
                commands.add("clear");
                commands.add("rain");
            }
        }

        if(commands.isEmpty()) {
            return null;
        } else return commands;
    }
}
