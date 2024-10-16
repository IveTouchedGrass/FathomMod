
package net.mcreator.youpieceof.entity;

import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.util.RandomSource;
import net.minecraft.sounds.SoundSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;

import net.mcreator.youpieceof.init.FathommodModItems;
import net.mcreator.youpieceof.init.FathommodModEntities;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;

@OnlyIn(value = Dist.CLIENT, _interface = ItemSupplier.class)
public class ThrowingKnivesEntityEntity extends AbstractArrow implements ItemSupplier {
	public static final ItemStack PROJECTILE_ITEM = new ItemStack(FathommodModItems.THROWING_KNIVES.get());

	public ThrowingKnivesEntityEntity(EntityType<? extends ThrowingKnivesEntityEntity> type, Level world) {
		super(type, world);
	}

	public ThrowingKnivesEntityEntity(EntityType<? extends ThrowingKnivesEntityEntity> type, double x, double y, double z, Level world) {
		super(type, x, y, z, world, PROJECTILE_ITEM, PROJECTILE_ITEM);
	}

	public ThrowingKnivesEntityEntity(EntityType<? extends ThrowingKnivesEntityEntity> type, LivingEntity entity, Level world) {
		super(type, entity, world, PROJECTILE_ITEM, null);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public ItemStack getItem() {
		return PROJECTILE_ITEM;
	}

	@Override
	protected ItemStack getDefaultPickupItem() {
		return new ItemStack(FathommodModItems.THROWING_KNIVES.get());
	}

	@Override
	protected void doPostHurtEffects(LivingEntity entity) {
		super.doPostHurtEffects(entity);
		entity.setArrowCount(entity.getArrowCount() - 1);
	}

	@Override
	public void tick() {
		super.tick();
		if (this.inGround)
			this.discard();
	}

	public static ThrowingKnivesEntityEntity shoot(Level world, LivingEntity entity, RandomSource source) {
		return shoot(world, entity, source, 0.7f, 2, 0);
	}

	public static ThrowingKnivesEntityEntity shoot(Level world, LivingEntity entity, RandomSource source, float pullingPower) {
		return shoot(world, entity, source, pullingPower * 0.7f, 2, 0);
	}

	public static ThrowingKnivesEntityEntity shoot(Level world, LivingEntity entity, RandomSource random, float power, double damage, int knockback) {
		ThrowingKnivesEntityEntity entityarrow = new ThrowingKnivesEntityEntity(FathommodModEntities.THROWING_KNIVES_ENTITY.get(), entity, world);
		entityarrow.shoot(entity.getViewVector(1).x, entity.getViewVector(1).y, entity.getViewVector(1).z, power * 2, 0);
		entityarrow.setSilent(true);
		entityarrow.setCritArrow(false);
		entityarrow.setBaseDamage(damage);
		world.addFreshEntity(entityarrow);
		world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.arrow.shoot")), SoundSource.PLAYERS, 1, 1f / (random.nextFloat() * 0.5f + 1) + (power / 2));
		return entityarrow;
	}

	public static ThrowingKnivesEntityEntity shoot(LivingEntity entity, LivingEntity target) {
		ThrowingKnivesEntityEntity entityarrow = new ThrowingKnivesEntityEntity(FathommodModEntities.THROWING_KNIVES_ENTITY.get(), entity, entity.level());
		double dx = target.getX() - entity.getX();
		double dy = target.getY() + target.getEyeHeight() - 1.1;
		double dz = target.getZ() - entity.getZ();
		entityarrow.shoot(dx, dy - entityarrow.getY() + Math.hypot(dx, dz) * 0.2F, dz, 0.7f * 2, 12.0F);
		entityarrow.setSilent(true);
		entityarrow.setBaseDamage(2);
		entityarrow.setCritArrow(false);
		entity.level().addFreshEntity(entityarrow);
		entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.arrow.shoot")), SoundSource.PLAYERS, 1, 1f / (RandomSource.create().nextFloat() * 0.5f + 1));
		return entityarrow;
	}
}
