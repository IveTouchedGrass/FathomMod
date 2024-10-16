package net.mcreator.youpieceof.network;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class WingGlideHandler {
    public static void handleOnServer(final WingGlideMessage.WingGlidePacket data, final IPayloadContext context) {
        ServerPlayer entity = (ServerPlayer) context.player();
        if (entity.getItemBySlot(EquipmentSlot.CHEST).getItem() == Items.GOLDEN_CHESTPLATE && !entity.onGround() && !(entity.getItemBySlot(EquipmentSlot.CHEST).getDamageValue() + 1 == entity.getItemBySlot(EquipmentSlot.CHEST).getMaxDamage()) && !entity.isFallFlying()) {
            entity.startFallFlying();
            PacketDistributor.sendToPlayer(entity, new WingGlideMessage.WingGlidePacket(true));
            FathommodModVariables.PlayerVariables vars = entity.getData(FathommodModVariables.PLAYER_VARIABLES);
            vars.wingGliding = true;
            vars.syncPlayerVariables(entity);
        }
    }

    public static void handleOnClient(final WingGlideMessage.WingGlidePacket data, final IPayloadContext context) {
        if (data.keyPressed())
            context.player().startFallFlying();
        else
            context.player().stopFallFlying();
    }
}
