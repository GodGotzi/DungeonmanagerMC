package at.gotzi.dungeonmanager.objects.pets.pathfinder.pathfindergoalpetnearesttarget;

import at.gotzi.dungeonmanager.Main;
import at.gotzi.dungeonmanager.data.PlayerData;
import at.gotzi.dungeonmanager.manager.file.PetManager;
import at.gotzi.dungeonmanager.utils.Utils;
import com.magmaguy.elitemobs.entitytracker.EliteEntityTracker;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import net.minecraft.world.entity.monster.IMonster;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.UUID;

public class PathfinderGoalPetNearestTarget extends PathfinderGoal {

    private final EntityInsentient pet;

    private final double speed;
    private final float damage;
    private UUID owner;
    private int counter;


    public PathfinderGoalPetNearestTarget(EntityInsentient pet, double speed, float damage) {
        this.pet = pet;
        this.speed = speed;
        this.damage = damage;
        this.a(EnumSet.of(Type.a));
    }

    @Override
    public boolean a() {
        if (this.pet.getGoalTarget() == null)
            this.pet.setGoalTarget((EntityLiving)PetManager.getOwner(this.pet.getBukkitEntity().getUniqueId()));
        this.owner = ((CraftPlayer)this.pet.getGoalTarget().getBukkitEntity()).getUniqueId();
        Entity t = null;
        for (org.bukkit.entity.Entity entity : this.pet.getBukkitEntity().getNearbyEntities(5, 5, 5)) {
            if (!(entity instanceof Player) && ((CraftEntity) entity).getHandle() instanceof IMonster && entity instanceof LivingEntity && !PetManager.isPet(entity.getUniqueId()) || EliteEntityTracker.eliteMobEntities.containsKey(entity.getUniqueId()) && entity instanceof LivingEntity) {
                t = ((CraftEntity) entity).getHandle();
                break;
            }
        }
        if (t != null) {
            this.pet.getNavigation().a(t.locX(), t.locY(), t.locZ(), this.speed);
            if ((this.pet.locX() - t.locX()) * (this.pet.locX() - t.locX()) + (this.pet.locY() - t.locY()) * (this.pet.locZ() - t.locZ()) + (this.pet.locZ() - t.locZ()) * (this.pet.locZ() - t.locZ()) <= 3) {
                int exp = PlayerData.getPetsExp(this.owner, PetManager.entityTypes.indexOf(this.pet.getBukkitEntity().getType()));
                int l = Utils.calPetLvl(exp);
                if ((PetManager.lvls.size()/2) > l)
                    this.pet.setBaby(true);
                else
                    this.pet.setBaby(false);
                PetManager.Lvl lvl;
                if (PetManager.lvls.size() == l)
                    lvl = PetManager.lvls.get(PetManager.lvls.size()-1);
                else
                    lvl = PetManager.lvls.get(l-1);
                if ((counter%lvl.getAttackSpeed()) == 0) {
                    this.attackEntity(t, lvl.getDamage());
                    if (!t.isAlive())
                        PlayerData.setPetsExp(owner, PetManager.entityTypes.indexOf(pet.getBukkitEntity().getType()), exp + 1);
                }
            }
        }
        counter++;
        return false;
    }

    public void attackEntity(Entity entity, float damage) {
        this.pet.t.broadcastEntityEffect(this.pet, (byte)4);
        entity.damageEntity(DamageSource.mobAttack(this.pet), damage);
    }
}
