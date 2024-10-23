package net.fathommod.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class FlyMessage {
    public record FlyKeyMessage(boolean keyPressed, double flySpeed) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<FlyKeyMessage> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("fathommod", "fly_message"));

        public static final StreamCodec<ByteBuf, FlyKeyMessage> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.BOOL,
                FlyKeyMessage::keyPressed,
                ByteBufCodecs.DOUBLE,
                FlyKeyMessage::flySpeed,
                FlyKeyMessage::new
        );

        @Override
        public CustomPacketPayload.@NotNull Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }
}
