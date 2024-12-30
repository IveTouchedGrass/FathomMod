package net.fathommod.network.handlers;

import net.fathommod.DamageClasses;
import net.fathommod.DamageTypedWeapon;
import net.fathommod.DevUtils;
import net.fathommod.Trinkets;
import net.fathommod.network.FathommodModVariables;
import net.fathommod.network.packets.AutoAttackMessage;
import net.fathommod.network.packets.ResetAttackStrengthMessage;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class AutoAttackPacketHandler {
    public static void handleDataOnServer(final AutoAttackMessage.AutoAttackPacket data, final IPayloadContext context) {
        ServerPlayer entity = ((ServerPlayer) context.player());
        FathommodModVariables.EntityVariables vars = entity.getData(FathommodModVariables.ENTITY_VARIABLES);
        if ((!(entity.getItemBySlot(EquipmentSlot.MAINHAND).getItem() instanceof DamageTypedWeapon weapon && weapon.getDamageClass() == DamageClasses.ASSASSIN) || vars.lastAutoAttack > weapon.getIFrames() / 2) && DevUtils.hasTrinket(entity, Trinkets.CHAINED_HANDLE) && entity.getAttackStrengthScale(0f) >= 1f) {
            vars.lastAutoAttack = 0;
            vars.syncPlayerVariables(entity);
            Vec3 pos = entity.position();
            pos.y = entity.getEyeY();
            Entity entityToAttack = DevUtils.performPreciseRaycast(entity, entity.level(), pos, entity.getLookAngle(), entity.getAttribute(Attributes.ENTITY_INTERACTION_RANGE).getValue());
            if (entityToAttack != null) {
                entity.attack(entityToAttack);
            }
            entity.swing(InteractionHand.MAIN_HAND, true);
            entity.resetAttackStrengthTicker();
            if (entity.getAttribute(Attributes.ATTACK_SPEED).getValue() < 20)
                PacketDistributor.sendToPlayer(entity, new ResetAttackStrengthMessage.ResetAttackStrengthPacket(0));
        }
    }
}
