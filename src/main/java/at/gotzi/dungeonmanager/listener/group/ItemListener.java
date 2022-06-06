package at.gotzi.dungeonmanager.listener.group;

import at.gotzi.dungeonmanager.commands.group.SkillCommand;
import at.gotzi.dungeonmanager.commands.group.commands.ItemFilterCommand;
import at.gotzi.dungeonmanager.data.PlayerData;
import at.gotzi.dungeonmanager.objects.groups.Group;
import at.gotzi.dungeonmanager.objects.skills.Skill;
import at.gotzi.dungeonmanager.objects.skills.Skills;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;

import java.util.List;

public class ItemListener implements Listener {

    @EventHandler
    public void onItem(PlayerAttemptPickupItemEvent event) {
        Player player = event.getPlayer();
        if (SkillCommand.isDisabled(player, true)) return;
        List<Skill> skills = PlayerData.getSkills(player.getUniqueId());
        Skill skill = null;
        boolean bool = false;
        for (Skill sks : skills) {
            if (sks.getSkills() == Skills.ITEMFILTERCMD) {
                bool = true;
                skill = sks;
                break;
            }
        }

        if(player.hasPermission("groups.all") || bool) {
            if(ItemFilterCommand.materials.get(player.getUniqueId()) == null) return;
            if(ItemFilterCommand.materials.get(player.getUniqueId()).contains(event.getItem().getItemStack().getType())) {
                event.setCancelled(true);
            }
        }
    }
}
