package at.gotzi.dungeonmanager.commands.group.commands;

import at.gotzi.dungeonmanager.Main;
import at.gotzi.dungeonmanager.commands.group.GroupCommand;
import at.gotzi.dungeonmanager.commands.group.SkillCommand;
import at.gotzi.dungeonmanager.data.PlayerData;
import at.gotzi.dungeonmanager.manager.file.GroupManager;
import at.gotzi.dungeonmanager.manager.player.TeleportManager;
import at.gotzi.dungeonmanager.objects.groups.Group;
import at.gotzi.dungeonmanager.objects.skills.Skill;
import at.gotzi.dungeonmanager.objects.skills.Skills;
import at.gotzi.dungeonmanager.utils.Messages;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class TpahereCommand extends GroupCommand implements TabCompleter {

    private Main main;

    private static HashMap<UUID, Integer> coolDown = new HashMap<>();
    private static HashMap<UUID, Player> link = new HashMap<>();

    public TpahereCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean execute() {
        if(sender instanceof Player player) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("accept")) {
                    Player target = link.get(player.getUniqueId());
                    if (target == null) {
                        execute2(player, args);
                        return false;
                    }
                    player.sendMessage(Messages.pTpahereAccept);
                    target.sendMessage(Messages.selfTpahereAccept);
                    TeleportManager.registerTeleport(player, target.getLocation());
                    coolDown.remove(target.getUniqueId());
                    link.remove(player.getUniqueId());
                } else {
                    this.execute2(player, args);
                }
            }
        } else
            sender.sendMessage(Messages.onlyPlayer);
        return false;
    }

    private void execute2(Player player, String[] args) {
        if (SkillCommand.isDisabled(player, false)) return;
        List<Skill> skills = PlayerData.getSkills(player.getUniqueId());
        Skill skill = null;
        boolean bool = false;
        for (Skill sks : skills) {
            if (sks.getSkills() == Skills.TPAHERECMD) {
                bool = true;
                skill = sks;
                break;
            }
        }
        if (player.hasPermission("groups.all") || bool) {
            if (!coolDown.containsKey(player.getUniqueId())) {

                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    player.sendMessage(Messages.playerNotExist);
                    return;
                }
                TextComponent msg = new TextComponent(Messages.pTpahereRequestMsg.replace("%PLAYER%", player.getName()));
                msg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpahere accept"));
                msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(Messages.hoverTpaMsg.replace("%PLAYER%", player.getName()))));
                player.sendMessage(Messages.selfTpahereRequestMsg.replace("%PLAYER%", target.getName()));
                target.spigot().sendMessage(msg);
                link.put(target.getUniqueId(), player);
                coolDown.put(player.getUniqueId(), 0);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (coolDown.get(player.getUniqueId()) == null) {
                            this.cancel();
                            return;
                        }
                        int count = coolDown.get(player.getUniqueId());
                        if (player == null) {
                            coolDown.remove(player.getUniqueId());
                            return;
                        }
                        if (!link.containsValue(player)) {
                            this.cancel();
                        } else {
                            count++;
                            coolDown.put(player.getUniqueId(), count);
                        }
                    }
                }.runTaskTimerAsynchronously(main, 20,20);
            } else
                player.sendMessage(Messages.coolDownTpa.replace("%AMOUNT%", String.valueOf(coolDown.get(player.getUniqueId()))));
        } else
            player.sendMessage(Messages.needGroup.replace("%CLASS%", GroupManager.getSkillGroups(Skills.TPAHERECMD)).toString());
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
