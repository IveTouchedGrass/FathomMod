package net.mcreator.youpieceof.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.particles.ParticleTypes;

import java.util.List;
import java.util.Comparator;

public class TNTArrowBokProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, Entity immediatesourceentity) {
		if (entity == null || immediatesourceentity == null)
			return;
		boolean Nuking = false;
		Nuking = world.getLevelData().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
		world.getLevelData().getGameRules().getRule(GameRules.RULE_MOBGRIEFING).set(false, world.getServer());
		if (world instanceof Level _level && !_level.isClientSide())
			_level.explode(null, (immediatesourceentity.getX()), (immediatesourceentity.getY()), (immediatesourceentity.getZ()), (float) 0.01, Level.ExplosionInteraction.MOB);
		if (world instanceof ServerLevel _level)
			_level.sendParticles(ParticleTypes.EXPLOSION_EMITTER, x, y, z, 1, 1, 1, 1, 1);
		{
			final Vec3 _center = new Vec3(x, y, z);
			List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(11 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
			for (Entity entityiterator : _entfound) {
				entityiterator.hurt(new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.EXPLOSION), entity), 15);
			}
		}
		if (!immediatesourceentity.level().isClientSide())
			immediatesourceentity.discard();
		world.getLevelData().getGameRules().getRule(GameRules.RULE_MOBGRIEFING).set(Nuking, world.getServer());
	}
}
