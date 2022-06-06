package at.gotzi.dungeonmanager.manager.groups;

import at.gotzi.dungeonmanager.Main;
import at.gotzi.dungeonmanager.commands.group.SkillCommand;
import at.gotzi.dungeonmanager.data.PlayerData;
import at.gotzi.dungeonmanager.objects.groups.Group;
import at.gotzi.dungeonmanager.objects.skills.Skill;
import at.gotzi.dungeonmanager.objects.skills.SkillType;
import at.gotzi.dungeonmanager.objects.skills.Skills;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class EffectManager {

    private final Main main;

    public EffectManager(Main main) {
        this.main = main;
    }

    public void runScheduler() {
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(main, new Runnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (SkillCommand.isDisabled(player, true)) return;
                     Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
                        List<Group> groups = PlayerData.getGroupRAM(player.getUniqueId());
                        if (!groups.isEmpty()) {
                            List<Skills> sks = new ArrayList<>();
                            int health = 0;
                            int armor = 0;
                            int regen = 0;
                            int fireRes = 0;
                            int res = 0;
                            int jumpBoost = 0;
                            int miningSpeed = 0;
                            int movementBoost = 0;

                            for (Skill skill : PlayerData.getSkills(player.getUniqueId())) {
                                Skills skills = skill.getSkills();
                                sks.add(skills);
                                if (skills.isNeedScheduler()) {
                                    if (skills.getSkillType() != SkillType.Command) {
                                        switch (skills) {
                                            case REGENERATION -> regen += skill.getStrength();
                                            case FIRERESISTANCE -> fireRes += skill.getStrength();
                                            case RESISTANCE -> res += skill.getStrength();
                                            case JUMPBOOST -> jumpBoost += skill.getStrength();
                                            case MININGSPEEDBOOST -> miningSpeed += skill.getStrength();
                                            case MOVEMENTBOOST -> movementBoost += skill.getStrength();
                                            default -> {
                                            }
                                        }
                                    }
                                    if (skills == Skills.HEALTHPLUS) {
                                        health += skill.getStrength();
                                    } else if (skills == Skills.ARMORPLUS) {
                                        armor += skill.getStrength();
                                    }
                                }
                            }
                            int finalHealth = health;
                            int finalArmor = armor;
                            int finalRegen = regen;
                            int finalFireRes = fireRes;
                            int finalRes = res;
                            int finalJumpBoost = jumpBoost;
                            int finalMiningSpeed = miningSpeed;
                            int finalMovementBoost = movementBoost;
                            Bukkit.getScheduler().runTask(main, () -> {
                                player.setMaxHealth(20 + finalHealth);
                                player.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(finalArmor);
                                if (finalRegen != 0)
                                    player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 120, finalRegen-1, false, false, false));
                                if (finalFireRes != 0)
                                    player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 120, finalFireRes-1, false, false, false));
                                if (finalRes != 0)
                                    player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 120, finalRes-1, false, false, false));
                                if (finalJumpBoost != 0)
                                    player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 120, finalJumpBoost-1, false, false, false));
                                if (finalMiningSpeed != 0)
                                    player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 120, finalMiningSpeed-1, false, false, false));
                                if (finalMovementBoost != 0)
                                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 120, finalMovementBoost-1, false, false, false));
                            });
                        }
                    });

                }
            }

        }, 100, 100);
    }
}
