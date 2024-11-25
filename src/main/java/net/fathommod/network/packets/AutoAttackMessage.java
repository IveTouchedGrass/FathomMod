package net.fathommod.network.packets;

import io.netty.buffer.ByteBuf;
import net.fathommod.FathommodMod;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class AutoAttackMessage {
    public record AutoAttackPacket(int thisDoesNothing) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<AutoAttackMessage.AutoAttackPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "auto_attack_packet"));

        public static final StreamCodec<ByteBuf, AutoAttackMessage.AutoAttackPacket> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.INT,
                AutoAttackMessage.AutoAttackPacket::thisDoesNothing,
                AutoAttackMessage.AutoAttackPacket::new
        );

        @Override
        public CustomPacketPayload.@NotNull Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }
}
