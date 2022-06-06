package at.gotzi.dungeonmanager.objects.pets.animals;

import at.gotzi.dungeonmanager.objects.pets.CustomPet;
import at.gotzi.dungeonmanager.objects.pets.pathfinder.pthfindergoalpet.PathfinderGoalPetFly;
import at.gotzi.dungeonmanager.objects.pets.pathfinder.pathfindergoalpetnearesttarget.PathfinderGoalPetNearestTargetLong;
import at.gotzi.dungeonmanager.objects.pets.pathfinder.pthfindergoalpet.PathfinderGoalPetGhast;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import net.minecraft.world.entity.ai.goal.PathfinderGoalLookAtPlayer;
import net.minecraft.world.entity.monster.EntityGhast;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityTargetEvent;

import java.util.EnumSet;

public class Ghast extends EntityGhast implements CustomPet {


    public Ghast(Location location, Player player, String name) {
        super(EntityTypes.F, ((CraftWorld)location.getWorld()).getHandle());
        this.setPosition(location.getX(), location.getY(), locZ());
        this.setBaby(true);
        this.setInvulnerable(true);
        IChatBaseComponent iChatBaseComponent = IChatBaseComponent.a(name);
        this.setCustomName(iChatBaseComponent);
        this.setGoalTarget(((EntityLiving) ((CraftPlayer) player).getHandle()), EntityTargetEvent.TargetReason.CUSTOM, true);
    }

    @Override
    public void initPathfinder() {
        super.initPathfinder();
        this.bP.a(2, new PathfinderGoalGhastMoveTowardsTarget(this));
        this.bP.a(1, new PathfinderGoalPetNearestTargetLong(this, 1, 5));
    }

    private static class PathfinderGoalGhastMoveTowardsTarget extends PathfinderGoal {
        private final EntityGhast a;

        public PathfinderGoalGhastMoveTowardsTarget(EntityGhast entityghast) {
            this.a = entityghast;
            this.a(EnumSet.of(Type.b));
        }

        public boolean a() {
            return true;
        }

        public void e() {
            if (this.a.getGoalTarget() == null) {
                Vec3D vec3d = this.a.getMot();
                this.a.setYRot(-((float) MathHelper.d(vec3d.b, vec3d.d)) * 57.295776F);
                this.a.aX = this.a.getYRot();
            } else {
                EntityLiving entityliving = this.a.getGoalTarget();
                double d0 = 64.0D;
                if (entityliving.f(this.a) < 4096.0D) {
                    double d1 = entityliving.locX() - this.a.locX();
                    double d2 = entityliving.locZ() - this.a.locZ();
                    this.a.setYRot(-((float)MathHelper.d(d1, d2)) * 57.295776F);
                    this.a.aX = this.a.getYRot();
                }
            }

        }
    }
}
