package net.fathommod.network;

import net.fathommod.network.handlers.*;
import net.fathommod.network.packets.*;
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

        registrar.playToServer(
                AutoAttackMessage.AutoAttackPacket.TYPE,
                AutoAttackMessage.AutoAttackPacket.STREAM_CODEC,
                AutoAttackPacketHandler::handleDataOnServer
        );

        registrar.playToClient(
                ResetAttackStrengthMessage.ResetAttackStrengthPacket.TYPE,
                ResetAttackStrengthMessage.ResetAttackStrengthPacket.STREAM_CODEC,
                ResetAttackStrengthPacketHandler::handleDataOnClient
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