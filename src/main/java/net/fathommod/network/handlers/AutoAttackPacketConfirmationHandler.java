package net.fathommod.network.handlers;

import net.fathommod.ClientVars;
import net.fathommod.init.FathommodModItems;
import net.fathommod.network.packets.AutoAttackConfirmCanAttackMessage;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class AutoAttackPacketConfirmationHandler {
    public static void handleOnClient(final AutoAttackConfirmCanAttackMessage.AutoAttackConfirmCanAttackPacket data, final IPayloadContext ignored) {
        ClientVars.canAutoAttack = data.canUse();
    }

    public static void handleOnServer(final AutoAttackConfirmCanAttackMessage.AutoAttackConfirmCanAttackPacket ignored, final IPayloadContext context) {
        ServerPlayer player = ((ServerPlayer) context.player());
        PacketDistributor.sendToPlayer(player, new AutoAttackConfirmCanAttackMessage.AutoAttackConfirmCanAttackPacket(player.getMainHandItem().getItem() == FathommodModItems.BOXING_GLOVES.get()));
    }
}
