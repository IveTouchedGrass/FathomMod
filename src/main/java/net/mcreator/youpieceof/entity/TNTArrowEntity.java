
package net.mcreator.youpieceof.entity;

import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.BlockHitResult;
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

import net.mcreator.youpieceof.procedures.TNTArrowHitProcedure;
import net.mcreator.youpieceof.procedures.TNTArrowBokProcedure;
import net.mcreator.youpieceof.init.FathommodModItems;
import net.mcreator.youpieceof.init.FathommodModEntities;

@OnlyIn(value = Dist.CLIENT, _interface = ItemSupplier.class)
public class TNTArrowEntity extends AbstractArrow implements ItemSupplier {
	public static final ItemStack PROJECTILE_ITEM = new ItemStack(FathommodModItems.TNT_ARROW_ITEM.get());

	public TNTArrowEntity(EntityType<? extends TNTArrowEntity> type, Level world) {
		super(type, world);
	}

	public TNTArrowEntity(EntityType<? extends TNTArrowEntity> type, double x, double y, double z, Level world) {
		super(type, x, y, z, world, PROJECTILE_ITEM, PROJECTILE_ITEM);
	}

	public TNTArrowEntity(EntityType<? extends TNTArrowEntity> type, LivingEntity entity, Level world) {
		super(type, entity, world, PROJECTILE_ITEM, null);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public ItemStack getItem() {
		return PROJECTILE_ITEM;
	}

	@Override
	protected ItemStack getDefaultPickupItem() {
		return new ItemStack(FathommodModItems.TNT_ARROW_ITEM.get());
	}

	@Override
	protected void doPostHurtEffects(LivingEntity entity) {
		super.doPostHurtEffects(entity);
		entity.setArrowCount(entity.getArrowCount() - 1);
	}

	@Override
	public void onHitEntity(EntityHitResult entityHitResult) {
		super.onHitEntity(entityHitResult);
		TNTArrowHitProcedure.execute(this.level(), this.getX(), this.getY(), this.getZ(), this, this.getOwner());
	}

	@Override
	public void onHitBlock(BlockHitResult blockHitResult) {
		super.onHitBlock(blockHitResult);
		TNTArrowBokProcedure.execute(this.level(), blockHitResult.getBlockPos().getX(), blockHitResult.getBlockPos().getY(), blockHitResult.getBlockPos().getZ(), this.getOwner(), this);
	}

	@Override
	public void tick() {
		super.tick();
		if (this.inGround)
			this.discard();
	}

	public static TNTArrowEntity shoot(Level world, LivingEntity entity, RandomSource source) {
		return shoot(world, entity, source, 1f, 0, 10);
	}

	public static TNTArrowEntity shoot(Level world, LivingEntity entity, RandomSource source, float pullingPower) {
		return shoot(world, entity, source, pullingPower * 1f, 0, 10);
	}

	public static TNTArrowEntity shoot(Level world, LivingEntity entity, RandomSource random, float power, double damage, int knockback) {
		TNTArrowEntity entityarrow = new TNTArrowEntity(FathommodModEntities.TNT_ARROW.get(), entity, world);
		entityarrow.shoot(entity.getViewVector(1).x, entity.getViewVector(1).y, entity.getViewVector(1).z, power * 2, 0);
		entityarrow.setSilent(true);
		entityarrow.setCritArrow(false);
		entityarrow.setBaseDamage(damage);
		world.addFreshEntity(entityarrow);
		world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.arrow.shoot")), SoundSource.PLAYERS, 1, 1f / (random.nextFloat() * 0.5f + 1) + (power / 2));
		return entityarrow;
	}

	public static TNTArrowEntity shoot(LivingEntity entity, LivingEntity target) {
		TNTArrowEntity entityarrow = new TNTArrowEntity(FathommodModEntities.TNT_ARROW.get(), entity, entity.level());
		double dx = target.getX() - entity.getX();
		double dy = target.getY() + target.getEyeHeight() - 1.1;
		double dz = target.getZ() - entity.getZ();
		entityarrow.shoot(dx, dy - entityarrow.getY() + Math.hypot(dx, dz) * 0.2F, dz, 1f * 2, 12.0F);
		entityarrow.setSilent(true);
		entityarrow.setBaseDamage(0);
		entityarrow.setCritArrow(false);
		entity.level().addFreshEntity(entityarrow);
		entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.arrow.shoot")), SoundSource.PLAYERS, 1, 1f / (RandomSource.create().nextFloat() * 0.5f + 1));
		return entityarrow;
	}
}
