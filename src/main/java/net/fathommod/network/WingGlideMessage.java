package net.fathommod.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class WingGlideMessage {
    public record WingGlidePacket(boolean keyPressed) implements CustomPacketPayload {
        public static final Type<WingGlidePacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath("randomthings", "glide_message"));

        public static final StreamCodec<ByteBuf, WingGlidePacket> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.BOOL,
                WingGlidePacket::keyPressed,
                WingGlidePacket::new
        );

        @Override
        public @NotNull Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }
}
