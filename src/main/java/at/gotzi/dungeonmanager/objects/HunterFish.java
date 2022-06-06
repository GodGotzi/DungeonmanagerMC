package at.gotzi.dungeonmanager.objects;

import at.gotzi.dungeonmanager.manager.file.HunterFishManager;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import net.minecraft.world.entity.monster.EntitySilverfish;
import net.minecraft.world.level.World;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityTargetEvent;

public class HunterFish extends EntitySilverfish {

    public HunterFish(Player player, World world, Loc loc) {
        super(EntityTypes.aA, world);
        this.setGoalTarget(((CraftPlayer) player).getHandle(),
                EntityTargetEvent.TargetReason.CUSTOM, true);
        this.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        IChatBaseComponent iChatBaseComponent = IChatBaseComponent.a(HunterFishManager.customName);
        super.setCustomName(iChatBaseComponent);
        this.setInvulnerable(true);
    }

    @Override
    protected void initPathfinder() {
        this.bP.a(1, new HunterPathfinder(this, 7, HunterFishManager.speed));
    }

}

class HunterPathfinder extends PathfinderGoal {

    private final EntityInsentient fish;
    private final double distance;
    private final double speed;

    public HunterPathfinder(EntityInsentient fish, float distance, double speed) {
        this.fish = fish;
        this.distance = distance;
        this.speed = speed;
    }

    @Override
    public boolean a() {
        if (Bukkit.getPlayer(HunterFishManager.getTarget()) == null ||
                !Bukkit.getPlayer(HunterFishManager.getTarget()).getWorld().getName().equals("openworld")) {
            if (HunterFishManager.schedulerId == 0)
                HunterFishManager.startNextTarget();
            return false;
        } else {
            HunterFishManager.cancelTask();
        }
        EntityLiving target = (EntityLiving) ((CraftPlayer) Bukkit.getPlayer(HunterFishManager.getTarget())).getHandle();
        double range = Math.sqrt(
                Math.pow(target.locX() - this.fish.locX(), 2) +
                Math.pow(target.locY() - this.fish.locY(), 2) +
                Math.pow(target.locZ() - this.fish.locZ(), 2));
        if (range > this.distance * 5) {
            this.fish.teleportAndSync(target.locX() - 10, HunterFishManager.findYBlock(this.fish.getWorld().getWorld(),
                            (int) target.locX() - 10,
                            (int) target.locZ() + 10),
                    target.locZ() + 10);
        } else if (range < 1.5)
            this.killEntity(target);
        else
            this.fish.getNavigation().a(target.locX(), target.locY(), target.locZ(), this.speed);
        return false;
    }

    public void killEntity(Entity entity) {
        this.fish.t.broadcastEntityEffect(this.fish, (byte)4);
        if (entity instanceof EntityPlayer entityPlayer) {
            if (Bukkit.getPlayer(entityPlayer.getUniqueID()).getGameMode() != GameMode.CREATIVE)
                entityPlayer.setHealth(0);
        }
    }
}
