package net.fathommod.network.handlers;

import net.fathommod.network.packets.ResetAttackStrengthMessage;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

@SuppressWarnings("unused")
public class ResetAttackStrengthPacketHandler {
    public static void handleDataOnClient(final ResetAttackStrengthMessage.ResetAttackStrengthPacket data, final IPayloadContext context) {
        LocalPlayer entity = (LocalPlayer) context.player();
        entity.resetAttackStrengthTicker();
    }
}
