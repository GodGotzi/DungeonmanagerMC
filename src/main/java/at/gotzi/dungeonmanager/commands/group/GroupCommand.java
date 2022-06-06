package at.gotzi.dungeonmanager.commands.group;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public abstract class GroupCommand implements CommandExecutor {

    public CommandSender sender;
    public Command command;
    public String label;
    public String[] args;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        this.sender = sender;
        this.command = command;
        this.label = label;
        this.args = args;
        return this.execute();
    }

    public abstract boolean execute();
}
