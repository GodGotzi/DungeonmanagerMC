package at.gotzi.dungeonmanager.objects.pets.pathfinder.pthfindergoalpet;

import net.minecraft.util.MathHelper;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;

import java.util.EnumSet;

public class PathfinderGoalPetGhast extends PathfinderGoal {

    private final EntityInsentient pet;
    private EntityLiving owner;

    private final double speed;
    private final float g;

    private double x;
    private double y;
    private double z;
    public PathfinderGoalPetGhast(EntityInsentient pet, double speed, float distance) {
        this.pet = pet;
        this.speed = speed;
        this.g = distance;
        this.a(EnumSet.of(Type.a));
    }

    @Override
    public boolean a() {
        this.owner = this.pet.getGoalTarget();
        if (this.owner == null)
            return false;
        else if (this.pet.getDisplayName() == null)
            return false;
        else if(!(this.pet.getDisplayName().toString().contains(this.owner.getName())))
            return false;
        else {
            this.x = this.owner.locX();
            this.y = this.owner.locY();
            this.z = this.owner.locZ();

            double d1 = this.owner.locX() - this.pet.locX();
            double d2 = this.owner.locZ() - this.pet.locZ();
            this.pet.setYRot(-((float) MathHelper.d(d1, d2)) * 57.295776F);
            this.pet.aX = this.pet.getYRot();
            if (Math.abs(this.x-this.pet.locX()) > g || Math.abs(this.y-this.pet.locY()) > g || Math.abs(this.z-this.pet.locZ()) > g)
                this.pet.teleportAndSync(this.x, this.y+10, this.z);
            return false;
        }
    }
}
