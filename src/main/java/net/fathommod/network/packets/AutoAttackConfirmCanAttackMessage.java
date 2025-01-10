package net.fathommod.network.packets;

import io.netty.buffer.ByteBuf;
import net.fathommod.FathommodMod;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class AutoAttackConfirmCanAttackMessage {
    public record AutoAttackConfirmCanAttackPacket(boolean canUse) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<AutoAttackConfirmCanAttackMessage.AutoAttackConfirmCanAttackPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "confirm_auto_attack_ability"));

        public static final StreamCodec<ByteBuf, AutoAttackConfirmCanAttackMessage.AutoAttackConfirmCanAttackPacket> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.BOOL,
                AutoAttackConfirmCanAttackMessage.AutoAttackConfirmCanAttackPacket::canUse,
                AutoAttackConfirmCanAttackMessage.AutoAttackConfirmCanAttackPacket::new
        );

        @Override
        public CustomPacketPayload.@NotNull Type<? extends CustomPacketPayload> type() {
                return TYPE;
            }
    }
}
