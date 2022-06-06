package at.gotzi.dungeonmanager.commands;

import at.gotzi.dungeonmanager.manager.file.HunterFishManager;
import at.gotzi.dungeonmanager.utils.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class HuntOnOffCommand implements CommandExecutor {

    private static boolean isEnabled = true;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("hunt.manage")) {
            sender.sendMessage(Messages.noPermission);
            return false;
        }
        if (isEnabled) {
            HunterFishManager.removeFish(false);
            sender.sendMessage("§6You §7disabled§6 Hunterfish!");
            isEnabled = false;
        } else {
            isEnabled = true;
            HunterFishManager.startFirst();
            sender.sendMessage("§6You §7enabled§6 Hunterfish!");
        }
        return false;
    }
}
