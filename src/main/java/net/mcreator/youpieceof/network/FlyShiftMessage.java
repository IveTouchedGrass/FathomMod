package net.mcreator.youpieceof.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class FlyShiftMessage {
    public record FlyShiftPacket(boolean keyPressed) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<FlyShiftPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("fathommod", "fly_shift_message"));

        public static final StreamCodec<ByteBuf, FlyShiftPacket> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.BOOL,
                FlyShiftPacket::keyPressed,
                FlyShiftPacket::new
        );

        @Override
        public CustomPacketPayload.@NotNull Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }
}