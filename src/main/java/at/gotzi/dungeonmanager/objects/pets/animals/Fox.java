package at.gotzi.dungeonmanager.objects.pets.animals;

import at.gotzi.dungeonmanager.objects.pets.CustomPet;
import at.gotzi.dungeonmanager.objects.pets.pathfinder.pthfindergoalpet.PathfinderGoalPet;
import at.gotzi.dungeonmanager.objects.pets.pathfinder.pathfindergoalpetnearesttarget.PathfinderGoalPetNearestTarget;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.EntityFox;
import net.minecraft.world.entity.player.EntityHuman;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.event.entity.EntityTargetEvent;

public class Fox extends EntityFox implements CustomPet {

    public Fox(Location location, EntityLiving owner, String name) {
        super(EntityTypes.E, ((CraftWorld)location.getWorld()).getHandle());
        this.setPosition(location.getX(), location.getY(), locZ());
        this.setBaby(true);
        this.setInvulnerable(true);
        IChatBaseComponent iChatBaseComponent = IChatBaseComponent.a(name);
        this.setCustomName(iChatBaseComponent);
        this.setGoalTarget(owner, EntityTargetEvent.TargetReason.CUSTOM, true);
    }

    @Override
    public void initPathfinder() {
        this.bP.a(2, new PathfinderGoalPet(this, 1, 25));
        this.bP.a(1, new PathfinderGoalPetNearestTarget(this, 1, 5));
    }
}
