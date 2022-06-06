package at.gotzi.dungeonmanager.listener.group;

import at.gotzi.dungeonmanager.Main;
import at.gotzi.dungeonmanager.commands.group.SkillCommand;
import at.gotzi.dungeonmanager.data.PlayerData;
import at.gotzi.dungeonmanager.listener.world.PlayerHitListener;
import at.gotzi.dungeonmanager.objects.skills.Skill;
import at.gotzi.dungeonmanager.objects.skills.Skills;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Random;

public class HitEntityEvent implements Listener {

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof Player player) {
            if (SkillCommand.isDisabled(player, true)) return;
            List<Skill> skills = PlayerData.getSkills(player.getUniqueId());
            Skill skill = null;
            boolean bool = false;
            for (Skill sks : skills) {
                if (sks.getSkills() == Skills.CHANCELIGHTNING) {
                    bool = true;
                    skill = sks;
                    break;
                }
            }
            if(bool) {
                int random = new Random().nextInt(100);
                if (skill == null) {
                    if (random <= 15) {
                        PlayerHitListener.lightningUUIDs.add(player.getUniqueId());
                        player.getWorld().strikeLightning(event.getEntity().getLocation());
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                PlayerHitListener.lightningUUIDs.remove(player.getUniqueId());
                            }
                        }.runTaskLaterAsynchronously(Main.getInstance(), 20);
                        return;
                    }
                    return;
                }

                if (random <= skill.getStrength()) {
                    PlayerHitListener.lightningUUIDs.add(player.getUniqueId());
                    player.getWorld().strikeLightning(event.getEntity().getLocation());
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            PlayerHitListener.lightningUUIDs.remove(player.getUniqueId());
                        }
                    }.runTaskLaterAsynchronously(Main.getInstance(), 20);
                }
            }
        }
    }
}
