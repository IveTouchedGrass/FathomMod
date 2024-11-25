package net.fathommod.entity.ted;

import net.fathommod.init.FathommodModDamageTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

@SuppressWarnings("unused")
public class ROCK extends AbstractArrow implements GeoEntity {
    public static final ResourceLocation TEXTURE_LOCATION = ResourceLocation.parse("fathommod:textures/entity/ted_but_rock.png");
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public ROCK(EntityType<? extends AbstractArrow> p_331098_, Level p_331626_) {
        super(p_331098_, p_331626_);
    }

    @Override
    public void onHitBlock(BlockHitResult result) {
        this.discard();
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(Items.AIR);
    }

    @Override
    public void onHitEntity(EntityHitResult result) {
        Entity _entity = result.getEntity();
        LivingEntity entity;

        if (_entity instanceof LivingEntity)
            entity = (LivingEntity) _entity;
        else
            return;

        if (entity instanceof Player && ((Player) entity).getAbilities().invulnerable)
            return;

        entity.hurt(new DamageSource(this.level().holderOrThrow(FathommodModDamageTypes.TED_ROCK)), 25);
        entity.addEffect(new MobEffectInstance(this.level().holderOrThrow(ResourceKey.create(Registries.MOB_EFFECT, ResourceLocation.parse("fathommod:movement_stun"))), 30, 0, false, false));
        this.discard();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        // pass
    }

    @Override
    public void tick() {
        super.tick();
        if (Math.random() < 0.01)
            this.addDeltaMovement(new Vec3(0, 0.01, 0));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
