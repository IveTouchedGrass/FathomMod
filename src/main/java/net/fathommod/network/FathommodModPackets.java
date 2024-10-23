package net.fathommod.network;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class FathommodModPackets {

    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playBidirectional(
                FlyMessage.FlyKeyMessage.TYPE,
                FlyMessage.FlyKeyMessage.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        FlyKeyMessageHandler::handleDataOnClient,
                        FlyKeyMessageHandler::handleDataOnServer
                )
        );

        registrar.playToServer(
                FlyShiftMessage.FlyShiftPacket.TYPE,
                FlyShiftMessage.FlyShiftPacket.STREAM_CODEC,
                FlyShiftMessageHandler::handleDataOnServer
        );

        registrar.playBidirectional(
                DoubleJumpMessage.DoubleJumpPacket.TYPE,
                DoubleJumpMessage.DoubleJumpPacket.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        DoubleJumpMessageHandler::handleOnClient,
                        DoubleJumpMessageHandler::handleOnServer
                )
        );

//        registrar.playBidirectional(
//                FlyMessage.FlyKeyMessage.TYPE,
//                FlyMessage.FlyKeyMessage.STREAM_CODEC,
//                new DirectionalPayloadHandler<>(
//                        FlyKeyMessageHandler::handleDataOnClient,
//                        FlyKeyMessageHandler::handleDataOnServer
//                )
//        );
//
//        registrar.playToServer(
//                FlyShiftMessage.FlyShiftPacket.TYPE,
//                FlyShiftMessage.FlyShiftPacket.STREAM_CODEC,
//                FlyShiftMessageHandler::handleDataOnServer
//        );
    }
}