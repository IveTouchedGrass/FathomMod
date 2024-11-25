package net.fathommod;

import net.fathommod.init.FathommodModItems;
import net.fathommod.network.FathommodModVariables;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandlerModifiable;

import java.util.List;
import java.util.Optional;

public class DevUtils {
    public static boolean hasItem(Entity entity, Item item) {
        if (entity.getCapability(Capabilities.ItemHandler.ENTITY, null) instanceof IItemHandlerModifiable _modHandlerIter) {
            for (int _idx = 0; _idx < _modHandlerIter.getSlots(); _idx++) {
                ItemStack itemstackiterator = _modHandlerIter.getStackInSlot(_idx);
                if (itemstackiterator.getItem() == item) {
                    return true;
                }
            }
        }
        return false;
    }

    public static AABB scaleAABB(AABB originalBox, double scaleFactor) {
        double centerX = (originalBox.minX + originalBox.maxX) / 2.0;
        double centerY = (originalBox.minY + originalBox.maxY) / 2.0;
        double centerZ = (originalBox.minZ + originalBox.maxZ) / 2.0;

        double newHalfWidth = (originalBox.maxX - originalBox.minX) / 2.0 * scaleFactor;
        double newHalfHeight = (originalBox.maxY - originalBox.minY) / 2.0 * scaleFactor;
        double newHalfDepth = (originalBox.maxZ - originalBox.minZ) / 2.0 * scaleFactor;
        return new AABB(
                centerX - newHalfWidth, centerY - newHalfHeight, centerZ - newHalfDepth,  // Min corner
                centerX + newHalfWidth, centerY + newHalfHeight, centerZ + newHalfDepth   // Max corner
        );
    }

    public static Entity performPreciseRaycast(Entity ignoredEntity, Level world, Vec3 origin, Vec3 direction, double maxDistance) {
        direction = direction.normalize();

        Vec3 rayEnd = origin.add(direction.scale(maxDistance));

        List<Entity> entities = world.getEntities(null, new AABB(origin, rayEnd).inflate(1.0));

        Entity closestEntity = null;
        double closestDistance = maxDistance;

        for (Entity entity : entities) {
            if (entity.isSpectator() || entity == ignoredEntity || entity.level().getBlockState(BlockPos.containing(rayEnd.x, rayEnd.y, rayEnd.z)).getBlock() == Blocks.AIR || entity.level().getBlockState(BlockPos.containing(rayEnd.x, rayEnd.y, rayEnd.z)).getBlock() == Blocks.CAVE_AIR || entity.level().getBlockState(BlockPos.containing(rayEnd.x, rayEnd.y, rayEnd.z)).getBlock() == Blocks.VOID_AIR) continue;

            AABB boundingBox = entity.getBoundingBox();
            Optional<Vec3> intersection = boundingBox.clip(origin, rayEnd);

            if (intersection.isPresent()) {
                double distance = origin.distanceTo(intersection.get());
                if (distance < closestDistance) {
                    closestDistance = distance;
                    closestEntity = entity;
                }
            }
        }

        return closestEntity;
    }

    public static class Pusher {
        public static void toCoords(Entity entity, double x, double y, double z, double speed) {
            Vec3 currentPosition = entity.position();

            Vec3 direction = new Vec3(x - currentPosition.x, y - currentPosition.y, z - currentPosition.z);

            Vec3 normalizedVector = direction.normalize();

            Vec3 velocity = normalizedVector.scale(speed);

            entity.setDeltaMovement(velocity);
        }

        public static void toEntity(Entity entity, Entity target, double speed) {
            toCoords(entity, target.getX(), target.getY(), target.getZ(), speed);
        }
    }

    public static void executeCommandAs(Entity entity, String command) {
        if (!entity.level().isClientSide() && entity.getServer() != null) {
            entity.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4,
                    entity.getName().getString(), entity.getDisplayName(), entity.level().getServer(), entity), command);
        }
    }

    public static boolean hasTrinket(LivingEntity entity, Item item) {
        if (entity.getItemBySlot(EquipmentSlot.MAINHAND).getItem() == FathommodModItems.BOXING_GLOVES.get() && item == FathommodModItems.CHAIN_HANDLE.get())
            return false;
        return (entity.getData(FathommodModVariables.ENTITY_VARIABLES).trinket1.getItem() == item || entity.getData(FathommodModVariables.ENTITY_VARIABLES).trinket2.getItem() == item || entity.getData(FathommodModVariables.ENTITY_VARIABLES).trinket3.getItem() == item || entity.getData(FathommodModVariables.ENTITY_VARIABLES).trinket4.getItem() == item);
    }

    public static short getEnchantLevel(ItemStack itemstack, ResourceKey<Enchantment> enchant, Level world) {
        return (short) itemstack.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(enchant));
    }

    public static class Raytracing {
        public static int xRaytracePos(Entity entity, double distance) {
            return entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(distance)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos().getX();

        }
        public static int yRaytracePos(Entity entity, double distance) {
            return entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(distance)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos().getY();

        }
        public static int zRaytracePos(Entity entity, double distance) {
            return entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(distance)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos().getX();
        }
    }

    public static class PositionCalculator {
        public static Vec3 calculateRelativePosition(float yaw, float pitch, Vec3 relativePosition) {
            double yawRad = Math.toRadians(yaw);
            double pitchRad = Math.toRadians(pitch);

            double x = Math.cos(yawRad) * Math.cos(pitchRad);
            double y = Math.sin(pitchRad);
            double z = Math.sin(yawRad) * Math.cos(pitchRad);

            double absX = relativePosition.x * x;
            double absY = relativePosition.y + y;
            double absZ = relativePosition.z * z;

            return new Vec3(absX, absY, absZ);
        }
    }
}