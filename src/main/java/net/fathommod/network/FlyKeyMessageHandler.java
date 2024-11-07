package net.fathommod.network;

import net.fathommod.DevUtils;
import net.fathommod.Trinkets;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class FlyKeyMessageHandler {
    public static void handleDataOnServer(final FlyMessage.FlyKeyMessage data, final IPayloadContext context) {
        Player entity = context.player();
        Integer damage;
        Integer maxDamage;

        if (entity.getItemBySlot(EquipmentSlot.HEAD).isEmpty())
            return;


        damage = entity.getItemBySlot(EquipmentSlot.HEAD).getComponents().get(DataComponents.DAMAGE);
        maxDamage = entity.getItemBySlot(EquipmentSlot.HEAD).getComponents().get(DataComponents.MAX_DAMAGE);

        if (entity.getItemBySlot(EquipmentSlot.HEAD).getItem() == Trinkets.WINGS && entity.getData(FathommodModVariables.ENTITY_VARIABLES).doubleJumpCooldownInt <= 0 && entity.getData(FathommodModVariables.ENTITY_VARIABLES).doubleJumpCooldownInt == 0 && !entity.onGround()) { // gold helmet is a placeholder
            if (!(damage + 1 == maxDamage)) {
                entity.getItemBySlot(EquipmentSlot.HEAD).hurtAndBreak(1, entity, EquipmentSlot.HEAD);
                PacketDistributor.sendToPlayer((ServerPlayer) entity, new FlyMessage.FlyKeyMessage(true, (DevUtils.hasTrinket(entity, Trinkets.DOUBLE_DOUBLE_JUMP_AND_JUMP_HEIGHT_ON_CRACK_BRO_THIS_IS_SO_OVERPOWERED_AND_TRASH_AT_THE_SAME_TIME_BECAUSE_YOU_TAKE_HELLA_FALL_DAMAGE)) ? 4 : (DevUtils.hasTrinket(entity, Trinkets.JUMP_HEIGHT)) ? 2.5 : 1));
            } else {
                PacketDistributor.sendToPlayer((ServerPlayer) entity, new FlyMessage.FlyKeyMessage(false, (DevUtils.hasTrinket(entity, Trinkets.DOUBLE_DOUBLE_JUMP_AND_JUMP_HEIGHT_ON_CRACK_BRO_THIS_IS_SO_OVERPOWERED_AND_TRASH_AT_THE_SAME_TIME_BECAUSE_YOU_TAKE_HELLA_FALL_DAMAGE)) ? 4 : (DevUtils.hasTrinket(entity, Trinkets.JUMP_HEIGHT)) ? 2.5 : 1));
            }
        }
    }

    public static void handleDataOnClient(final FlyMessage.FlyKeyMessage data, final IPayloadContext context) {
        Player entity = context.player();
        double minFallingSpeed = -0.3;
        double fallNegatingSpeed = 0.15;
        double maxFlyingSpeed = data.flySpeed();
        double flyingAcceleration = 0.15; // per tick
        if (data.keyPressed()) {
            entity.addDeltaMovement(new Vec3(0, (flyingAcceleration + entity.getDeltaMovement().y > maxFlyingSpeed) ? maxFlyingSpeed - entity.getDeltaMovement().y : flyingAcceleration, 0));
        } else {
            Vec3 currentMotion = entity.getDeltaMovement();
            if (entity.getDeltaMovement().y < minFallingSpeed) {
                entity.fallDistance = 0;
                entity.addDeltaMovement(new Vec3(0, (currentMotion.y + minFallingSpeed) > minFallingSpeed ? Math.abs(minFallingSpeed - currentMotion.y) : fallNegatingSpeed, 0));
            }
        }
    }
}