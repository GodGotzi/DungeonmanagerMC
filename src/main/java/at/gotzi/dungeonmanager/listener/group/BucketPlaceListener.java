package at.gotzi.dungeonmanager.listener.group;

import at.gotzi.dungeonmanager.commands.group.SkillCommand;
import at.gotzi.dungeonmanager.commands.group.commands.ItemFilterCommand;
import at.gotzi.dungeonmanager.data.PlayerData;
import at.gotzi.dungeonmanager.objects.groups.Group;
import at.gotzi.dungeonmanager.objects.skills.Skill;
import at.gotzi.dungeonmanager.objects.skills.Skills;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class BucketPlaceListener implements Listener {


    @EventHandler
    public void onPlace(PlayerBucketEmptyEvent event) {
        Player player = event.getPlayer();
        if (SkillCommand.isDisabled(player, true)) return;
        List<Skill> skills = PlayerData.getSkills(player.getUniqueId());
        Skill skill = null;
        boolean bool = false;
        for (Skill sks : skills) {
            if (sks.getSkills() == Skills.NEVEREMPTYBUCKET) {
                bool = true;
                skill = sks;
                break;
            }
        }

        if(player.hasPermission("groups.all") || bool) {
            Material bucket = event.getBucket();
            if (bucket == Material.WATER_BUCKET || bucket == Material.LAVA_BUCKET) {
                ItemStack newBucket = new ItemStack(event.getBucket());
                newBucket.setItemMeta(event.getItemStack().getItemMeta());
                event.setItemStack(newBucket);
            }
        }
    }
}
