package net.fathommod.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Objects;

public class FlyShiftMessageHandler {
    public static void handleDataOnServer(final FlyShiftMessage.FlyShiftPacket data, final IPayloadContext context) {
        Player entity = context.player();
        if (data.keyPressed()) {
            if (entity.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.GOLDEN_HELMET && entity.getData(FathommodModVariables.PLAYER_VARIABLES).doubleJumpCooldownInt == 0)
                Objects.requireNonNull(entity.getAttribute(Attributes.GRAVITY)).addOrReplacePermanentModifier(new AttributeModifier(ResourceLocation.parse("fathommod:wings_shift_attr"), 2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        } else {
            Objects.requireNonNull(entity.getAttribute(Attributes.GRAVITY)).removeModifier(ResourceLocation.parse("fathommod:wings_shift_attr"));
        }
    }
}
