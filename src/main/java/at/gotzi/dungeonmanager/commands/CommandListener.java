package at.gotzi.dungeonmanager.commands;

import at.gotzi.dungeonmanager.utils.Messages;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.ArrayList;
import java.util.List;

public class CommandListener implements Listener {

    private List<String> blocked = new ArrayList<>();

    public CommandListener() {
        blocked.add("bukkit");
        blocked.add("minecraft");
        blocked.add("help");
        blocked.add("?");
        blocked.add("ver");
        blocked.add("version");
        blocked.add("pl");
        blocked.add("plugins");
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (!event.getPlayer().hasPermission("system.bypass.blockedCommands")) {
            for (String c : this.blocked) {
                if (event.getMessage().contains(c)) {
                    event.getPlayer().sendMessage(Messages.noPermission);
                    event.setCancelled(true);
                }
            }
        }
    }

}
