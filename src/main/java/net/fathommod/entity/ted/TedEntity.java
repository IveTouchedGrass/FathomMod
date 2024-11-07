package net.fathommod.entity.ted;

import net.fathommod.BossEntity;
import net.fathommod.Config;
import net.fathommod.DevUtils;
import net.fathommod.init.FathommodModEntities;
import net.fathommod.init.FathommodModMobEffects;
import net.fathommod.init.FathommodModSounds;
import net.fathommod.network.FathommodModVariables;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("deprecated")
public class TedEntity extends Monster implements GeoEntity, BossEntity {
    public static final ResourceLocation TEXTURE_LOCATION = ResourceLocation.parse("fathommod:textures/entity/teddy_2.png");

    public static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("Ted_Idle");
    public static final RawAnimation WALK_ANIM = RawAnimation.begin().thenLoop("Ted_Walk");
    public static final RawAnimation SWIPE_ANIM = RawAnimation.begin().then("Ted_Attack1", Animation.LoopType.PLAY_ONCE);
    public static final RawAnimation INSTA_KILL_ANIM = RawAnimation.begin().then("Ted_instakill", Animation.LoopType.PLAY_ONCE);
    public static final RawAnimation ROCK_ANIM = RawAnimation.begin().then("Ted_ROCK", Animation.LoopType.PLAY_ONCE);
    public static final RawAnimation RABBIT_ANIM = RawAnimation.begin().then("Ted_Rabbit_Summon", Animation.LoopType.PLAY_ONCE);
    public static final RawAnimation EMERGE_ANIM = RawAnimation.begin().then("Ted_Erect", Animation.LoopType.PLAY_ONCE);

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public Attacks currentAttack = Attacks.PURSUIT;
    private Player target;
    private byte scanCooldown = 0;
    private byte windUpLeft = 0;
    public boolean triedAttacking = false;
//    private short rabbitTimer = 600;
//    private short teleportTimer = 360;
//    public short rockTimer = 240;
    private long age = 0;
    public final ServerBossEvent bossBar = new ServerBossEvent(Component.literal("Ted"), BossEvent.BossBarColor.WHITE, BossEvent.BossBarOverlay.PROGRESS);
    private AABB swipeAABB;
    private short emergeTicks = 1;
    private boolean initializedSpawn = false;

    private static final EntityDataAccessor<Byte> ATTACK_COOLDOWN = SynchedEntityData.defineId(TedEntity.class, EntityDataSerializers.BYTE);
    public static final EntityDataAccessor<Integer> RABBIT_TIMER = SynchedEntityData.defineId(TedEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> ROCK_TIMER = SynchedEntityData.defineId(TedEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> TELEPORT_TIMER = SynchedEntityData.defineId(TedEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Boolean> HAS_TARGET = SynchedEntityData.defineId(TedEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Long> SWIPE_BOX_MIN_X = SynchedEntityData.defineId(TedEntity.class, EntityDataSerializers.LONG);
    private static final EntityDataAccessor<Float> SWIPE_BOX_MIN_Y = SynchedEntityData.defineId(TedEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Long> SWIPE_BOX_MIN_Z = SynchedEntityData.defineId(TedEntity.class, EntityDataSerializers.LONG);
    private static final EntityDataAccessor<Long> SWIPE_BOX_MAX_X = SynchedEntityData.defineId(TedEntity.class, EntityDataSerializers.LONG);
    private static final EntityDataAccessor<Float> SWIPE_BOX_MAX_Y = SynchedEntityData.defineId(TedEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Long> SWIPE_BOX_MAX_Z = SynchedEntityData.defineId(TedEntity.class, EntityDataSerializers.LONG);

    private static final EntityDataAccessor<Long> TELEPORT_BOX_MIN_X = SynchedEntityData.defineId(TedEntity.class, EntityDataSerializers.LONG);
    private static final EntityDataAccessor<Float> TELEPORT_BOX_MIN_Y = SynchedEntityData.defineId(TedEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Long> TELEPORT_BOX_MIN_Z = SynchedEntityData.defineId(TedEntity.class, EntityDataSerializers.LONG);
    private static final EntityDataAccessor<Long> TELEPORT_BOX_MAX_X = SynchedEntityData.defineId(TedEntity.class, EntityDataSerializers.LONG);
    private static final EntityDataAccessor<Float> TELEPORT_BOX_MAX_Y = SynchedEntityData.defineId(TedEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Long> TELEPORT_BOX_MAX_Z = SynchedEntityData.defineId(TedEntity.class, EntityDataSerializers.LONG);

    private static final EntityDataAccessor<Long> INSTA_KILL_BOX_MIN_X = SynchedEntityData.defineId(TedEntity.class, EntityDataSerializers.LONG);
    private static final EntityDataAccessor<Float> INSTA_KILL_BOX_MIN_Y = SynchedEntityData.defineId(TedEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Long> INSTA_KILL_BOX_MIN_Z = SynchedEntityData.defineId(TedEntity.class, EntityDataSerializers.LONG);
    private static final EntityDataAccessor<Long> INSTA_KILL_BOX_MAX_X = SynchedEntityData.defineId(TedEntity.class, EntityDataSerializers.LONG);
    private static final EntityDataAccessor<Float> INSTA_KILL_BOX_MAX_Y = SynchedEntityData.defineId(TedEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Long> INSTA_KILL_BOX_MAX_Z = SynchedEntityData.defineId(TedEntity.class, EntityDataSerializers.LONG);

    private static final EntityDataAccessor<Long> NO_ROCK_BOX_MIN_X = SynchedEntityData.defineId(TedEntity.class, EntityDataSerializers.LONG);
    private static final EntityDataAccessor<Float> NO_ROCK_BOX_MIN_Y = SynchedEntityData.defineId(TedEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Long> NO_ROCK_BOX_MIN_Z = SynchedEntityData.defineId(TedEntity.class, EntityDataSerializers.LONG);
    private static final EntityDataAccessor<Long> NO_ROCK_BOX_MAX_X = SynchedEntityData.defineId(TedEntity.class, EntityDataSerializers.LONG);
    private static final EntityDataAccessor<Float> NO_ROCK_BOX_MAX_Y = SynchedEntityData.defineId(TedEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Long> NO_ROCK_BOX_MAX_Z = SynchedEntityData.defineId(TedEntity.class, EntityDataSerializers.LONG);

    private static final EntityDataAccessor<Long> CAN_ATTACK_BOX_MIN_X = SynchedEntityData.defineId(TedEntity.class, EntityDataSerializers.LONG);
    private static final EntityDataAccessor<Float> CAN_ATTACK_BOX_MIN_Y = SynchedEntityData.defineId(TedEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Long> CAN_ATTACK_BOX_MIN_Z = SynchedEntityData.defineId(TedEntity.class, EntityDataSerializers.LONG);
    private static final EntityDataAccessor<Long> CAN_ATTACK_BOX_MAX_X = SynchedEntityData.defineId(TedEntity.class, EntityDataSerializers.LONG);
    private static final EntityDataAccessor<Float> CAN_ATTACK_BOX_MAX_Y = SynchedEntityData.defineId(TedEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Long> CAN_ATTACK_BOX_MAX_Z = SynchedEntityData.defineId(TedEntity.class, EntityDataSerializers.LONG);

    private static final EntityDataAccessor<Long> SPAWN_BOX_MIN_X = SynchedEntityData.defineId(TedEntity.class, EntityDataSerializers.LONG);
    private static final EntityDataAccessor<Float> SPAWN_BOX_MIN_Y = SynchedEntityData.defineId(TedEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Long> SPAWN_BOX_MIN_Z = SynchedEntityData.defineId(TedEntity.class, EntityDataSerializers.LONG);
    private static final EntityDataAccessor<Long> SPAWN_BOX_MAX_X = SynchedEntityData.defineId(TedEntity.class, EntityDataSerializers.LONG);
    private static final EntityDataAccessor<Float> SPAWN_BOX_MAX_Y = SynchedEntityData.defineId(TedEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Long> SPAWN_BOX_MAX_Z = SynchedEntityData.defineId(TedEntity.class, EntityDataSerializers.LONG);

    public enum Attacks {
        SWIPE,
        INSTA_KILL,
        PURSUIT,
        ROCK,
        SPAWN_RABBITS
    }

    public void updateSwipeAABB() {
        this.entityData.set(SWIPE_BOX_MIN_X, Math.round(this.swipeAABB.minX));
        this.entityData.set(SWIPE_BOX_MIN_Y, (float) this.swipeAABB.minY);
        this.entityData.set(SWIPE_BOX_MIN_Z, Math.round(this.swipeAABB.minZ));
        this.entityData.set(SWIPE_BOX_MAX_X, Math.round(this.swipeAABB.maxX));
        this.entityData.set(SWIPE_BOX_MAX_Y, (float) this.swipeAABB.maxY);
        this.entityData.set(SWIPE_BOX_MAX_Z, Math.round(this.swipeAABB.maxZ));
    }

    public void updateTeleportAABB() {
        this.entityData.set(TELEPORT_BOX_MIN_X, Math.round(this.getX() - 201));
        this.entityData.set(TELEPORT_BOX_MIN_Y, (float) this.getY() - 201f + (4.5f / 2));
        this.entityData.set(TELEPORT_BOX_MIN_Z, Math.round(this.getZ() - 201));
        this.entityData.set(TELEPORT_BOX_MAX_X, Math.round(this.getX() + 201));
        this.entityData.set(TELEPORT_BOX_MAX_Y, (float) this.getY() + 201f - (4.5f / 2));
        this.entityData.set(TELEPORT_BOX_MAX_Z, Math.round(this.getZ() + 201));
    }

    public AABB getTeleportAABB() {
        return new AABB(this.entityData.get(TELEPORT_BOX_MIN_X), this.entityData.get(TELEPORT_BOX_MIN_Y), this.entityData.get(TELEPORT_BOX_MIN_Z), this.entityData.get(TELEPORT_BOX_MAX_X), this.entityData.get(TELEPORT_BOX_MAX_Y), this.entityData.get(TELEPORT_BOX_MAX_Z));
    }

    public TedEntity(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putShort("emergeTicks", this.emergeTicks);
        tag.putBoolean("initializedSpawn", this.initializedSpawn);
        tag.putByte("attackCooldown", this.attackCooldown());
        tag.putByte("windUpLeft", this.windUpLeft);
        switch (this.currentAttack) {
            case PURSUIT:
                tag.putByte("currentAttack", (byte) 0);
                break;
            case INSTA_KILL:
                tag.putByte("currentAttack", (byte) 1);
                break;
            case SWIPE:
                tag.putByte("currentAttack", (byte) 2);
                break;
            case ROCK:
                tag.putByte("currentAttack", (byte) 3);
                break;
            case SPAWN_RABBITS:
                tag.putByte("currentAttack", (byte) 4);
                break;
        }
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.initializedSpawn = tag.getBoolean("initializedSpawn");
        this.emergeTicks = this.initializedSpawn ? 1 : tag.getShort("emergeTicks");
        this.setAttackCooldown(tag.getByte("attackCooldown"));
//        this.windUpLeft = tag.getByte("windUpLeft");
//        this.currentAttack = switch (tag.getByte("currentAttack")) {
//            case 1 ->
//                Attacks.INSTA_KILL;
//            case 2 ->
//                Attacks.SWIPE;
//            case 3 ->
//                Attacks.ROCK;
//            case 4 ->
//                Attacks.SPAWN_RABBITS;
//            default ->
//                Attacks.PURSUIT;
//        };
    }

    public void swipeAttack() {
        if (this.target == null || this.currentAttack != Attacks.PURSUIT || this.attackCooldown() > 0 || !this.isAlive())
            return;
        this.setDeltaMovement(new Vec3(0, this.getDeltaMovement().y, 0));
        Objects.requireNonNull(this.getAttribute(Attributes.MOVEMENT_SPEED)).addOrUpdateTransientModifier(new AttributeModifier(ResourceLocation.parse("fathommod:ted_stopped"), -10, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        this.triggerAnim("swipe_controller", "swipe");
        this.triedAttacking = false;
        this.currentAttack = Attacks.SWIPE;
        this.windUpLeft = 13;
        this.setAttackCooldown((byte) 60);
    }

    public void instaKill() {
        if (this.target == null || this.currentAttack != Attacks.PURSUIT || this.attackCooldown() > 0 || !this.isAlive())
            return;
        this.setDeltaMovement(new Vec3(0, this.getDeltaMovement().y, 0));
        Objects.requireNonNull(this.getAttribute(Attributes.MOVEMENT_SPEED)).addOrUpdateTransientModifier(new AttributeModifier(ResourceLocation.parse("fathommod:ted_stopped"), -10, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        this.currentAttack = Attacks.INSTA_KILL;
        this.setAttackCooldown((byte) 60);
        this.triggerAnim("insta_kill_controller", "insta_kill");
        this.triedAttacking = false;
        this.windUpLeft = 17;
    }

    public void throwRock() {
        this.currentAttack = Attacks.ROCK;
        this.windUpLeft = 28;
        this.triggerAnim("rock_controller", "throw_rock");
    }

    public void spawnRabbits() {
        this.currentAttack = Attacks.SPAWN_RABBITS;
        this.windUpLeft = 25;
        this.triggerAnim("spawn_rabbits_controller", "spawn_rabbits");
    }

    @Override
    public void setCustomName(@Nullable Component p_20053_) {
        super.setCustomName(p_20053_);
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

    @SuppressWarnings("SameReturnValue")
    private PlayState predicate(AnimationState<TedEntity> animationState) {
        if (!this.isDeadOrDying() && this.initializedSpawn && this.emergeTicks <= 1) {
            if (animationState.isMoving()) {
                animationState.setAndContinue(WALK_ANIM);
            } else {
                animationState.getController().setAnimation(IDLE_ANIM);
            }
        } else {
            animationState.getController().setAnimation(IDLE_ANIM);
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
        controllers.add(new AnimationController<GeoAnimatable>(this, "swipe_controller", 2, state -> PlayState.STOP).triggerableAnim("swipe", SWIPE_ANIM));
        controllers.add(new AnimationController<GeoAnimatable>(this, "insta_kill_controller", 2, state -> PlayState.STOP).triggerableAnim("insta_kill", INSTA_KILL_ANIM));
        controllers.add(new AnimationController<GeoAnimatable>(this, "rock_controller", 2, state -> PlayState.STOP).triggerableAnim("throw_rock", ROCK_ANIM));
        controllers.add(new AnimationController<GeoAnimatable>(this, "spawn_rabbits_controller", 2, state -> PlayState.STOP).triggerableAnim("spawn_rabbits", RABBIT_ANIM));
        controllers.add(new AnimationController<>(this, "emerge_controller", 2, state -> PlayState.STOP).triggerableAnim("emerge", EMERGE_ANIM));
    }

    @Override
    public boolean canAttack(@NotNull LivingEntity entity) {
        return entity != null && (!(entity instanceof Player) || (!((Player) entity).getAbilities().invulnerable && !entity.isSpectator())) && super.canAttack(entity) && this.level().dimension() == entity.level().dimension() && this.level().getWorldBorder().isWithinBounds(entity.getBoundingBox()) && super.canAttack(entity);
    }

    @Override
    public void spawnAnim() {
        super.spawnAnim();
    }

    @Override
    public int getCurrentSwingDuration() {
        return this.currentAttack == Attacks.PURSUIT ? 0 : this.currentAttack == Attacks.SWIPE ? 14 : 20;
    }

    public double distance(LivingEntity entity) {
        return Math.sqrt(super.distanceToSqr(entity));
    }

    @Override
    public boolean killedEntity(@NotNull ServerLevel p_216988_, @NotNull LivingEntity p_216989_) {
        boolean result = super.killedEntity(p_216988_, p_216989_);

        if (result)
            this.scanCooldown = 0;

        return result;
    }

    public AABB getSwipeAABB() {
        return new AABB(this.entityData.get(SWIPE_BOX_MIN_X), this.entityData.get(SWIPE_BOX_MIN_Y), this.entityData.get(SWIPE_BOX_MIN_Z), this.entityData.get(SWIPE_BOX_MAX_X), this.entityData.get(SWIPE_BOX_MAX_Y), this.entityData.get(SWIPE_BOX_MAX_Z));
    }

    public void updateInstaKillAABB() {
        double x = this.getX();
        double y = this.getY() + this.getEyeHeight() - 3f;
        double z = this.getZ();

        Vec3 startVec = new Vec3(x, y, z);
        Vec3 endVec = startVec.add(this.getLookAngle().scale(2.5));

        double minX = Math.min(startVec.x, endVec.x) - 0.5;
        double minZ = Math.min(startVec.z, endVec.z) - 0.5;

        double maxX = Math.max(startVec.x, endVec.x) + 0.5;
        double maxZ = Math.max(startVec.z, endVec.z) + 0.5;

        this.entityData.set(INSTA_KILL_BOX_MIN_X, Math.round(minX));
        this.entityData.set(INSTA_KILL_BOX_MIN_Y, ((float) this.getY() + 0.5f));
        this.entityData.set(INSTA_KILL_BOX_MIN_Z, Math.round(minZ));
        this.entityData.set(INSTA_KILL_BOX_MAX_X, Math.round(maxX));
        this.entityData.set(INSTA_KILL_BOX_MAX_Y, (float) this.getY() + 2f);
        this.entityData.set(INSTA_KILL_BOX_MAX_Z, Math.round(maxZ));
    }

    public void updateAttackAABB() {
        this.entityData.set(CAN_ATTACK_BOX_MIN_X, Math.round(this.getX() - 3));
        this.entityData.set(CAN_ATTACK_BOX_MIN_Y, (float) this.getY() - 1.5f);
        this.entityData.set(CAN_ATTACK_BOX_MIN_Z, Math.round(this.getZ() - 3));
        this.entityData.set(CAN_ATTACK_BOX_MAX_X, Math.round(this.getX() + 3));
        this.entityData.set(CAN_ATTACK_BOX_MAX_Y, (float) this.getY() + 5.5f);
        this.entityData.set(CAN_ATTACK_BOX_MAX_Z, Math.round(this.getZ() + 3));
    }

    public void updateSpawnAABB() {
        this.entityData.set(SPAWN_BOX_MIN_X, Math.round(this.getX() - 6));
        this.entityData.set(SPAWN_BOX_MIN_Y, (float) this.getY() - 6f + 2.5f);
        this.entityData.set(SPAWN_BOX_MIN_Z, Math.round(this.getZ() - 6));
        this.entityData.set(SPAWN_BOX_MAX_X, Math.round(this.getX() + 6));
        this.entityData.set(SPAWN_BOX_MAX_Y, (float) this.getY() + 6f + 2.5f);
        this.entityData.set(SPAWN_BOX_MAX_Z, Math.round(this.getZ() + 6));
    }

    @SuppressWarnings("unused")
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

    public AABB getAttackAABB() {
        return new AABB(
                this.entityData.get(CAN_ATTACK_BOX_MIN_X),
                this.entityData.get(CAN_ATTACK_BOX_MIN_Y),
                this.entityData.get(CAN_ATTACK_BOX_MIN_Z),
                this.entityData.get(CAN_ATTACK_BOX_MAX_X),
                this.entityData.get(CAN_ATTACK_BOX_MAX_Y),
                this.entityData.get(CAN_ATTACK_BOX_MAX_Z)
        );
    }

    public AABB getInstaKillAABB() {
        return new AABB(this.entityData.get(INSTA_KILL_BOX_MIN_X), this.entityData.get(INSTA_KILL_BOX_MIN_Y), this.entityData.get(INSTA_KILL_BOX_MIN_Z), this.entityData.get(INSTA_KILL_BOX_MAX_X), this.entityData.get(INSTA_KILL_BOX_MAX_Y), this.entityData.get(INSTA_KILL_BOX_MAX_Z)).inflate(0.01);
    }

    public void updateRockAABB() {
        this.entityData.set(NO_ROCK_BOX_MIN_X, Math.round(this.getX() - 3));
        this.entityData.set(NO_ROCK_BOX_MIN_Y, (float) this.getY() - 1.5f);
        this.entityData.set(NO_ROCK_BOX_MIN_Z, Math.round(this.getZ() - 3));
        this.entityData.set(NO_ROCK_BOX_MAX_X, Math.round(this.getX() + 3));
        this.entityData.set(NO_ROCK_BOX_MAX_Y, (float) this.getY() + 5.5f);
        this.entityData.set(NO_ROCK_BOX_MAX_Z, Math.round(this.getZ() + 3));
    }

    public AABB getRockAABB() {
        return new AABB(this.entityData.get(NO_ROCK_BOX_MIN_X), this.entityData.get(NO_ROCK_BOX_MIN_Y), this.entityData.get(NO_ROCK_BOX_MIN_Z), this.entityData.get(NO_ROCK_BOX_MAX_X), this.entityData.get(NO_ROCK_BOX_MAX_Y), this.entityData.get(NO_ROCK_BOX_MAX_Z));
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean isPushedByFluid(@NotNull FluidType type) {
        return false;
    }

    @Override
    public boolean canSpawnSprintParticle() {
        return (this.getDeltaMovement().x != 0 || this.getDeltaMovement().z != 0) && this.target != null;
    }

    @Override
    @SuppressWarnings("deprecation")
    public @Nullable SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor p_21434_, @NotNull DifficultyInstance p_21435_, @NotNull MobSpawnType p_21436_, @Nullable SpawnGroupData p_21437_) {
        this.emergeTicks = 200;
        this.initializedSpawn = false;
        return super.finalizeSpawn(p_21434_, p_21435_, p_21436_, p_21437_);
    }

    @Override
    public void tick() {
        super.tick();
        this.entityData.set(HAS_TARGET, this.target != null);
        this.setPersistenceRequired();
        this.updateSpawnAABB();
        if (this.isNoAi()) {
            this.bossBar.removeAllPlayers();
            return;
        }
        this.setTarget(this.target);
        this.swipeAABB = switch (this.getDirection()) {
            case NORTH ->
                    new AABB(this.getX() - 2, this.getY(), this.getZ() - 3, this.getX() + 2, this.getY() + 3, this.getZ());
            case SOUTH ->
                    new AABB(this.getX() - 2, this.getY(), this.getZ(), this.getX() + 2, this.getY() + 3, this.getZ() + 3);
            case EAST ->
                    new AABB(this.getX(), this.getY(), this.getZ() - 2, this.getX() + 3, this.getY() + 3, this.getZ() + 2);
            case WEST ->
                    new AABB(this.getX() - 3, this.getY(), this.getZ() - 2, this.getX(), this.getY() + 3, this.getZ() + 2);
            default ->
                    new AABB(this.getX(), this.getY(), this.getZ(), this.getX() + 1, this.getY() + 1, this.getZ() + 1);
        };

        this.updateSwipeAABB();
        this.updateInstaKillAABB();
        this.updateRockAABB();
        this.updateAttackAABB();
        this.updateTeleportAABB();

        this.setTarget(this.target);

        this.setCustomName(Component.translatable("entity.fathommod.ted"));

        if (this.currentAttack == Attacks.PURSUIT) {
            this.windUpLeft = 127;
        } else if (this.windUpLeft > 0) {
            this.windUpLeft -= 1;
        }

        if (this.currentAttack != Attacks.PURSUIT) {
            Objects.requireNonNull(this.getAttribute(Attributes.MOVEMENT_SPEED)).addOrUpdateTransientModifier(new AttributeModifier(ResourceLocation.parse("fathommod:ted_stopped"), -10, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        } else {
            Objects.requireNonNull(this.getAttribute(Attributes.MOVEMENT_SPEED)).removeModifier(ResourceLocation.parse("fathommod:ted_stopped"));
        }
        boolean attackCondition = this.target != null && (this.getAttackAABB().contains(this.target.getX(), this.target.getY(), this.target.getZ()) || this.getAttackAABB().contains(this.target.getX(), this.target.getY() + this.target.getEyeHeight(), this.target.getZ())) && this.canAttack(this.target) && this.isAlive() && this.attackCooldown() <= 0;

        this.bossBar.setProgress(this.getHealth() / this.getMaxHealth());

        Vec3 center = new Vec3(this.getX(), this.getY(), this.getZ());
        List<Player> players = this.level().getEntitiesOfClass(Player.class, new AABB(center, center).inflate((100 * 2 - 1) / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(center))).toList();

        if (deathTime >= 18) {
            this.bossBar.removeAllPlayers();
            for (Player player : players) {
                DevUtils.executeCommandAs(player, "effect clear @s fathommod:zero_build");
            }
        }

        if (this.age % 5 == 0 && this.isAlive())
            for (int x = -6; x <= 6; x++) {
                for (int z = -6; z <= 6; z++) {
                    if (x >= -3 && z >= -3 && x <= 3 && z <= 3 && this.isAlive()) {
                        DevUtils.executeCommandAs(this, String.format("forceload remove ~%1$s ~%2$s", x, z));
                    } else {
                        DevUtils.executeCommandAs(this, String.format("forceload add ~%1$s ~%2$s", x, z));
                    }
                }
            }
        if (this.target == null || !this.canAttack(this.target)) {
            this.target = null;
            this.currentAttack = Attacks.PURSUIT;
            this.setAttackCooldown((byte) 60);
            this.windUpLeft = 127;
            this.triedAttacking = false;
            this.setTeleportTimer(360);
            this.setRockTimer(240);
            this.setRabbitTimer(Config.isDevelopment ? 30 : 600);
        }

        if (this.scanCooldown <= 0 && this.isAlive()) {
            List<Player> teleportTargets = this.level().getEntitiesOfClass(Player.class, this.getTeleportAABB()).stream().sorted(Comparator.comparingDouble(this::distance)).toList();
            for (Player player : teleportTargets) {
                player.addEffect(new MobEffectInstance(FathommodModMobEffects.ZERO_BUILD, 41, 0, false, false));
                boolean hasBossBar = false;
                if (this.target == null) {
                    if (this.canAttack(player)) {
                        this.target = player;
                        this.teleportTo(this.target.getX(), this.target.getY(), this.target.getZ());
                    }
                }
                for (Player _player : bossBar.getPlayers()) {
                    if (_player == player) {
                        hasBossBar = true;
                        break;
                    }
                }
                if (player instanceof ServerPlayer && !hasBossBar)
                    bossBar.addPlayer((ServerPlayer) player);
                if (this.target == null && this.canAttack(player))
                    this.target = player;
            }
            this.scanCooldown = 20;
            if (this.target == null) {
                if (teleportTargets.isEmpty()) {
                    this.discard();
                    return;
                }
            }
        }
        this.scanCooldown--;

        if (this.target != null && (this.getRockAABB().contains(this.target.getX(), this.target.getY(), this.target.getZ()) || this.getRockAABB().contains(this.target.getX(), this.target.getY() + this.target.getEyeHeight(), this.target.getZ()))) {
            this.setRockTimer(240);
        }

        if (this.target != null && this.distance(this.target) > 50 && this.isAlive()) {
            this.teleportTo(this.target.getX(), this.target.getY(), this.target.getZ());
        }

        if (this.target != null)
            this.lookAt(this.target, 30, 30);

        if (this.target != null && this.windUpLeft <= 0 && this.currentAttack != Attacks.PURSUIT && this.isAlive() && this.isAlive()) {
            if (this.currentAttack == Attacks.SWIPE) {
                this.setAttackCooldown((byte) 60);
                boolean playedSound = false;
                for (Entity entity : this.level().getEntities(this, this.getSwipeAABB())) {
                    if (!playedSound) {
                        this.level().playLocalSound(this, FathommodModSounds.SMACK.get(), SoundSource.HOSTILE, 1.0F, 1.0F);
                    }
                    playedSound = true;
                    if ((!(entity instanceof Monster) && !(entity instanceof Rabbit)) || entity == this.target)
                        entity.hurt(new DamageSource(this.level().holderOrThrow(ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.parse("fathommod:ted_swipe"))), this), 15);
                }
            } else if (this.currentAttack == Attacks.INSTA_KILL) {
                this.setAttackCooldown((byte) 60);
                for (Entity entity : this.level().getEntities(this, this.getInstaKillAABB())) {
                    if ((!(entity instanceof Monster) && !(entity instanceof Rabbit)) || entity == this.target)
                        entity.hurt(new DamageSource(this.level().holderOrThrow(ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.parse("fathommod:ted_insta_kill")))), ((LivingEntity) entity).getMaxHealth());
                }
            } else if (this.currentAttack == Attacks.ROCK) {
                Level world = this.level();
                if (world instanceof ServerLevel projectileLevel) {
                    Projectile _entityToSpawn = new Object() {
                        public Projectile getArrow(float damage) {
                            AbstractArrow entityToSpawn = new ROCK(FathommodModEntities.ROCK.get(), level());
                            entityToSpawn.setBaseDamage(damage);
                            return entityToSpawn;
                        }
                    }.getArrow(5);
                    _entityToSpawn.setPos(this.getX(), this.getY() + 2.7, this.getZ());
                    DevUtils.Pusher.toCoords(_entityToSpawn, this.target.getX(), this.target.getY() + this.target.getEyeHeight(), this.target.getZ(), 3);
//                    _entityToSpawn.setDeltaMovement(new Vec3(_entityToSpawn.getDeltaMovement().x, 0.01 * this.distance(this.target), _entityToSpawn.getDeltaMovement().z));
                    projectileLevel.addFreshEntity(_entityToSpawn);
                }
            } else if (this.currentAttack == Attacks.SPAWN_RABBITS) {
                for (int i = 0; i < 3; i++) {
                    Rabbit rabbit = new Rabbit(EntityType.RABBIT, this.level());
                    float yaw = this.getYRot();
                    float pitch = this.getXRot();

                    String command = switch (i) {
                        case 0 -> "^ ^ ^1";
                        case 1 -> "^-1 ^ ^";
                        case 2 -> "^1 ^ ^";
                        default -> "^ ^ ^";
                    };

                    rabbit.setVariant(Rabbit.Variant.EVIL);
                    rabbit.teleportTo(this.getX(), this.getY(), this.getZ());
                    this.level().addFreshEntity(rabbit);
                    FathommodModVariables.EntityVariables vars = rabbit.getData(FathommodModVariables.ENTITY_VARIABLES);
                    vars.isTedRabbit = true;
                    vars.syncPlayerVariables(rabbit);
                    DevUtils.executeCommandAs(rabbit, "tp @s " + command);
                }
            }
            this.windUpLeft = 127;
            this.currentAttack = Attacks.PURSUIT;
        }
        if (this.attackCooldown() > 0)
            this.setAttackCooldown((byte) (this.attackCooldown() - 1));

        if (this.rockTimer() > 0)
            this.setRockTimer(this.rockTimer() - 1);
        if (this.rockTimer() <= 0 && this.isAlive() && !this.level().isClientSide()) {
            this.throwRock();
            this.setRockTimer(240);
        }

        if (this.rabbitTimer() > 0)
            this.setRabbitTimer(this.rabbitTimer() - 1);
        if (this.rabbitTimer() <= 0 && this.isAlive()) {
            this.spawnRabbits();
            this.setRabbitTimer(Config.isDevelopment ? 30 : 600);
        }

        if (this.teleportTimer() > 0)
            this.setTeleportTimer(this.teleportTimer() - 1);
        if (this.teleportTimer() <= 0 && this.isAlive() && this.target != null && !Config.isDevelopment) {
            this.teleportTo(this.target.getX(), this.target.getY(), this.target.getZ());
            this.setTeleportTimer(360);
        }

        age += 1;

        if (this.target != null && this.attackCooldown() <= 0 && attackCondition && this.currentAttack == Attacks.PURSUIT && this.isAlive() && !this.level().isClientSide() && this.entityData.get(HAS_TARGET)) {
            double choice = Mth.lerp(Math.random(), 0, 100);
            if (choice < 66)
                this.swipeAttack();
            else
                this.instaKill();
            this.setAttackCooldown((byte) 60);
        }

        if (Config.isDevelopment) {
            DevUtils.executeCommandAs(this, "say " + this.currentAttack + " Wind up: " + this.windUpLeft + " Attack cooldown: " + this.attackCooldown() + " Direction: " + this.getDirection() + " Scan Cooldown: " + this.scanCooldown + " Rock Timer: " + this.rockTimer() + " Rabbit Timer " + this.rabbitTimer());
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);

        builder.define(ATTACK_COOLDOWN, (byte) 0);
        builder.define(SWIPE_BOX_MIN_X, 0L);
        builder.define(SWIPE_BOX_MIN_Y, 0f);
        builder.define(SWIPE_BOX_MIN_Z, 0L);
        builder.define(SWIPE_BOX_MAX_X, 0L);
        builder.define(SWIPE_BOX_MAX_Y, 0f);
        builder.define(SWIPE_BOX_MAX_Z, 0L);

        builder.define(INSTA_KILL_BOX_MIN_X, 0L);
        builder.define(INSTA_KILL_BOX_MIN_Y, 0f);
        builder.define(INSTA_KILL_BOX_MIN_Z, 0L);
        builder.define(INSTA_KILL_BOX_MAX_X, 0L);
        builder.define(INSTA_KILL_BOX_MAX_Y, 0f);
        builder.define(INSTA_KILL_BOX_MAX_Z, 0L);

        builder.define(NO_ROCK_BOX_MIN_X, 0L);
        builder.define(NO_ROCK_BOX_MIN_Y, 0f);
        builder.define(NO_ROCK_BOX_MIN_Z, 0L);
        builder.define(NO_ROCK_BOX_MAX_X, 0L);
        builder.define(NO_ROCK_BOX_MAX_Y, 0f);
        builder.define(NO_ROCK_BOX_MAX_Z, 0L);

        builder.define(CAN_ATTACK_BOX_MIN_X, 0L);
        builder.define(CAN_ATTACK_BOX_MIN_Y, 0f);
        builder.define(CAN_ATTACK_BOX_MIN_Z, 0L);
        builder.define(CAN_ATTACK_BOX_MAX_X, 0L);
        builder.define(CAN_ATTACK_BOX_MAX_Y, 0f);
        builder.define(CAN_ATTACK_BOX_MAX_Z, 0L);

        builder.define(SPAWN_BOX_MIN_X, 0L);
        builder.define(SPAWN_BOX_MIN_Y, 0f);
        builder.define(SPAWN_BOX_MIN_Z, 0L);
        builder.define(SPAWN_BOX_MAX_X, 0L);
        builder.define(SPAWN_BOX_MAX_Y, 0f);
        builder.define(SPAWN_BOX_MAX_Z, 0L);

        builder.define(RABBIT_TIMER, 600);
        builder.define(ROCK_TIMER, 240);
        builder.define(TELEPORT_TIMER, 360);

        builder.define(TELEPORT_BOX_MIN_X, 0L);
        builder.define(TELEPORT_BOX_MIN_Y, 0f);
        builder.define(TELEPORT_BOX_MIN_Z, 0L);
        builder.define(TELEPORT_BOX_MAX_X, 0L);
        builder.define(TELEPORT_BOX_MAX_Y, 0f);
        builder.define(TELEPORT_BOX_MAX_Z, 0L);

        builder.define(HAS_TARGET, false);
    }

    public byte attackCooldown() {
        return this.entityData.get(ATTACK_COOLDOWN);
    }

    public void setAttackCooldown(byte newValue) {
        this.entityData.set(ATTACK_COOLDOWN, newValue);
    }

    public int rabbitTimer() {
        return this.entityData.get(RABBIT_TIMER);
    }

    public void setRabbitTimer(int newValue) {
        this.entityData.set(RABBIT_TIMER, newValue);
    }

    public int rockTimer() {
        return this.entityData.get(ROCK_TIMER);
    }

    public void setRockTimer(int newValue) {
        this.entityData.set(ROCK_TIMER, newValue);
    }

    public int teleportTimer() {
        return this.entityData.get(TELEPORT_TIMER);
    }

    public void setTeleportTimer(int newValue) {
        this.entityData.set(TELEPORT_TIMER, newValue);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1, true));
        this.goalSelector.addGoal(2, new FloatGoal(this));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.4, 0.005f));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.getEntity() != null && !(source.getEntity() instanceof Player))
            return false;
        return !source.is(DamageTypes.FALL) && !source.is(DamageTypes.WITHER) && !source.is(DamageTypes.WITHER_SKULL) && !source.is(DamageTypes.DROWN) && !source.is(DamageTypes.MAGIC) && !source.is(DamageTypes.CACTUS) && super.hurt(source, amount);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}