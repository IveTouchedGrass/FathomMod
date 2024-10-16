package net.mcreator.youpieceof.entity.ted;

import net.mcreator.youpieceof.Config;
import net.mcreator.youpieceof.DevUtils;
import net.mcreator.youpieceof.init.FathommodModEntities;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

public class TedSpawner extends Monster implements GeoEntity {
    public static final ResourceLocation TEXTURE_LOCATION = ResourceLocation.parse("fathommod:textures/entity/ted_but_rock.png");

    private final AnimatableInstanceCache ANIMATABLE_INSTANCE_CACHE = GeckoLibUtil.createInstanceCache(this);

    private static final EntityDataAccessor<Long> SPAWN_BOX_MIN_X = SynchedEntityData.defineId(TedSpawner.class, EntityDataSerializers.LONG);
    private static final EntityDataAccessor<Float> SPAWN_BOX_MIN_Y = SynchedEntityData.defineId(TedSpawner.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Long> SPAWN_BOX_MIN_Z = SynchedEntityData.defineId(TedSpawner.class, EntityDataSerializers.LONG);
    private static final EntityDataAccessor<Long> SPAWN_BOX_MAX_X = SynchedEntityData.defineId(TedSpawner.class, EntityDataSerializers.LONG);
    private static final EntityDataAccessor<Float> SPAWN_BOX_MAX_Y = SynchedEntityData.defineId(TedSpawner.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Long> SPAWN_BOX_MAX_Z = SynchedEntityData.defineId(TedSpawner.class, EntityDataSerializers.LONG);

    private final RawAnimation TED_ERECT = RawAnimation.begin().then("Ted_Erect", Animation.LoopType.PLAY_ONCE);
    public static final RawAnimation UNDERGROUND_IDLE_ANIM = RawAnimation.begin().thenLoop("Ted_Idle_Underground");

    private boolean isSpawning = false;
    private static final short spawnTicksTotal = 195;
    private short spawnTicks = TedSpawner.spawnTicksTotal;

    public TedSpawner(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean isPushedByFluid(FluidType type) {
        return false;
    }

    public static AttributeSupplier createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 800)
                .add(Attributes.ATTACK_DAMAGE, -238)
                .add(Attributes.ATTACK_SPEED, 1)
                .add(Attributes.MOVEMENT_SPEED, 0.625d)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1)
                .add(Attributes.STEP_HEIGHT, 1)
                .add(Attributes.FOLLOW_RANGE, 64)
                .add(Attributes.ARMOR, 5)
                .add(Attributes.ATTACK_KNOCKBACK, 0)
                .build();
    }

    private PlayState predicate(AnimationState<TedSpawner> animationState) {
        animationState.getController().setAnimation(this.isSpawning ? TED_ERECT : UNDERGROUND_IDLE_ANIM);
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<TedSpawner>(this, "controller", 0, this::predicate));
    }

    public void updateSpawnAABB() {
        this.entityData.set(SPAWN_BOX_MIN_X, Math.round(this.getX() - 6));
        this.entityData.set(SPAWN_BOX_MIN_Y, (float) this.getY() - 6f + 2.5f);
        this.entityData.set(SPAWN_BOX_MIN_Z, Math.round(this.getZ() - 6));
        this.entityData.set(SPAWN_BOX_MAX_X, Math.round(this.getX() + 6));
        this.entityData.set(SPAWN_BOX_MAX_Y, (float) this.getY() + 6f + 2.5f);
        this.entityData.set(SPAWN_BOX_MAX_Z, Math.round(this.getZ() + 6));
    }

    public AABB getSpawnAABB() {
        return new AABB(
                this.entityData.get(SPAWN_BOX_MIN_X),
                this.entityData.get(SPAWN_BOX_MIN_Y),
                this.entityData.get(SPAWN_BOX_MIN_Z),
                this.entityData.get(SPAWN_BOX_MAX_X),
                this.entityData.get(SPAWN_BOX_MAX_Y),
                this.entityData.get(SPAWN_BOX_MAX_Z)
        );
    }

    @Override
    public boolean hurt(DamageSource p_21016_, float p_21017_) {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        this.updateSpawnAABB();
        if (Config.isDevelopment)
            DevUtils.executeCommandAs(this, "say " + this.isSpawning + " " + this.spawnTicks);
        for (Entity entity : this.level().getEntities(this, this.getSpawnAABB())) {
            if (entity instanceof Player && !((Player) entity).getAbilities().invulnerable && !entity.isSpectator()) {
                this.isSpawning = true;
                break;
            }
        }
        if (this.isSpawning)
            this.spawnTicks--;
        if (this.spawnTicks <= 0 && this.level() instanceof ServerLevel) {
            TedEntity ted = FathommodModEntities.TED.get().spawn((ServerLevel) this.level(), this.blockPosition(), MobSpawnType.TRIGGERED);
            assert ted != null;
            ted.setXRot(this.getXRot());
            ted.setYRot(this.getYRot());
            this.level().addFreshEntity(ted);
            this.discard();
        }
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.ANIMATABLE_INSTANCE_CACHE;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SPAWN_BOX_MIN_X, 0L);
        builder.define(SPAWN_BOX_MIN_Y, 0f);
        builder.define(SPAWN_BOX_MIN_Z, 0L);
        builder.define(SPAWN_BOX_MAX_X, 0L);
        builder.define(SPAWN_BOX_MAX_Y, 0f);
        builder.define(SPAWN_BOX_MAX_Z, 0L);
    }
}
