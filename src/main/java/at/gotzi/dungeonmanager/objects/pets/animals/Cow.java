package at.gotzi.dungeonmanager.objects.pets.animals;

import at.gotzi.dungeonmanager.objects.pets.CustomPet;
import at.gotzi.dungeonmanager.objects.pets.pathfinder.pthfindergoalpet.PathfinderGoalPet;
import at.gotzi.dungeonmanager.objects.pets.pathfinder.pathfindergoalpetnearesttarget.PathfinderGoalPetNearestTarget;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.EntityCow;
import net.minecraft.world.entity.player.EntityHuman;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityTargetEvent;

public class Cow extends EntityCow implements CustomPet {

    private final Player player;

    public Cow(Location location, Player player, String name) {
        super(EntityTypes.n, ((CraftWorld)location.getWorld()).getHandle());
        this.setPosition(location.getX(), location.getY(), locZ());
        this.setBaby(true);
        this.player = player;
        this.setInvulnerable(true);
        IChatBaseComponent iChatBaseComponent = IChatBaseComponent.a(name);
        this.setCustomName(iChatBaseComponent);
        this.setGoalTarget(((EntityLiving) ((CraftPlayer) player).getHandle()), EntityTargetEvent.TargetReason.CUSTOM, true);
    }

    @Override
    public void initPathfinder() {
        this.bP.a(2, new PathfinderGoalPet(this, 1, 25));
        this.bP.a(1, new PathfinderGoalPetNearestTarget(this, 1, 5));
    }
}
