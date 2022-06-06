package at.gotzi.dungeonmanager.listener.group;

import at.gotzi.dungeonmanager.Main;
import at.gotzi.dungeonmanager.commands.group.SkillCommand;
import at.gotzi.dungeonmanager.data.PlayerData;
import at.gotzi.dungeonmanager.listener.world.PlayerHitListener;
import at.gotzi.dungeonmanager.objects.skills.Skill;
import at.gotzi.dungeonmanager.objects.skills.Skills;
import at.gotzi.dungeonmanager.utils.Utils;
import net.minecraft.world.food.FoodMetaData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class FoodLevelChangeListener implements Listener {

    private static HashMap<UUID, Double> foodLevel = new HashMap<>();

    @EventHandler
    public void onChange(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (SkillCommand.isDisabled(player, true)) return;
            List<Skill> skills = PlayerData.getSkills(player.getUniqueId());
            int boost = 0;
            boolean bool = false;
            for (Skill sks : skills) {
                if (sks.getSkills() == Skills.HUNGERBOOST) {
                    bool = true;
                    boost += sks.getStrength();
                    break;
                }
            }

            if (player.hasPermission("groups.all") || bool) {
                if (bool) {
                    if (!foodLevel.containsKey(player.getUniqueId())) {
                        foodLevel.put(player.getUniqueId(), player.getFoodLevel() + Utils.percent(player.getFoodLevel() , boost));
                    }

                    double currentFoodLevel = foodLevel.get(player.getUniqueId());
                    currentFoodLevel = currentFoodLevel-(player.getFoodLevel()-event.getFoodLevel());
                    foodLevel.put(player.getUniqueId(), currentFoodLevel);
                    player.setFoodLevel((int)((currentFoodLevel/(20 + Utils.percent(20, boost)) * 20) +1));
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        if (event.getItem().getItemMeta() instanceof FoodMetaData foodMetaData) {
            if (SkillCommand.isDisabled(player, true)) return;
            List<Skill> skills = PlayerData.getSkills(player.getUniqueId());
            int boost = 0;
            boolean bool = false;
            for (Skill sks : skills) {
                if (sks.getSkills() == Skills.HUNGERBOOST) {
                    bool = true;
                    boost += sks.getStrength();
                    break;
                }
            }

            if (player.hasPermission("groups.all") || bool) {
                if (bool) {
                    if (!foodLevel.containsKey(player.getUniqueId())) {
                        foodLevel.put(player.getUniqueId(), player.getFoodLevel() + Utils.percent(player.getFoodLevel(), boost));
                    }

                    double currentFoodLevel = foodLevel.get(player.getUniqueId());
                    currentFoodLevel += (foodMetaData.getFoodLevel() + Utils.percent(foodMetaData.getFoodLevel(), boost));
                    foodLevel.put(player.getUniqueId(), currentFoodLevel);
                }
            }
        }
    }

    public static void setMaxFoodLevel(Player player) {
        if (SkillCommand.isDisabled(player, true)) return;
        List<Skill> skills = PlayerData.getSkills(player.getUniqueId());
        int boost = 0;
        boolean bool = false;
        for (Skill sks : skills) {
            if (sks.getSkills() == Skills.HUNGERBOOST) {
                bool = true;
                boost += sks.getStrength();
                break;
            }
        }

        if (player.hasPermission("groups.all") || bool) {
            if (bool) {
                foodLevel.remove(player.getUniqueId());
                foodLevel.put(player.getUniqueId(), player.getFoodLevel() + Utils.percent(player.getFoodLevel(), boost));
            }
        }
    }

    public static void removeFoodLevel(Player player) {
        foodLevel.remove(player.getUniqueId());
    }


}
