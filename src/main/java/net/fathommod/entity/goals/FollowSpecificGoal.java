package net.fathommod.entity.goals;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.List;

public class FollowSpecificGoal extends Goal {
    private final Mob self;
    private final LivingEntity target;
    private final double speed;
    private final double range;
    private final boolean canFollowCreative;

    public FollowSpecificGoal(Mob self, LivingEntity target) {
        this.self = self;
        this.target = target;
        this.speed = 1;
        this.range = 15 * 2 - 1;
        this.canFollowCreative = false;
    }

    public FollowSpecificGoal(Mob self, LivingEntity target, double speed) {
        this.self = self;
        this.target = target;
        this.speed = speed;
        this.range = 15;
        this.canFollowCreative = false;
    }

    public FollowSpecificGoal(Mob self, LivingEntity target, double speed, double range, boolean canFollowCreative) {
        this.self = self;
        this.target = target;
        this.speed = speed;
        this.range = range;
        this.canFollowCreative = canFollowCreative;
    }

    @Override
    public boolean canUse() {
        final Vec3 _center = new Vec3(self.getX(), self.getY(), self.getZ());

        List<Entity> players = self.level().getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(range), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();

        boolean keepGoal = false;
        for (Entity entity : players) {
            if (entity == this.target) {
                keepGoal = true;
                break;
            }
        }

        return this.self.isAlive() && this.target.isAlive() && keepGoal && ((this.target instanceof Player && this.canFollowCreative || !((Player) this.target).isCreative()) || !(this.target instanceof Player));
    }

    @Override
    public void start() {
        this.self.getNavigation().moveTo(this.target, this.speed);
    }

    @Override
    public void stop() {
        this.self.getNavigation().stop();
    }

    @Override
    public void tick() {
        this.self.getNavigation().moveTo(this.target, this.speed);
    }
}