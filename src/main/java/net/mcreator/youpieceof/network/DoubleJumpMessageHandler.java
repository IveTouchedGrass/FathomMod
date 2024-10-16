package net.mcreator.youpieceof.network;

import net.mcreator.youpieceof.DevUtils;
import net.mcreator.youpieceof.FathommodMod;
import net.mcreator.youpieceof.TrinketDict;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class DoubleJumpMessageHandler {
    public static void handleOnClient(final DoubleJumpMessage.DoubleJumpPacket data, final IPayloadContext context) {
        try {
        Player entity = context.player();
        entity.fallDistance = 0;
        final Vec3 originalMovement = entity.getDeltaMovement();
        entity.setDeltaMovement(new Vec3(originalMovement.x, 0, originalMovement.z));
        entity.addDeltaMovement(new Vec3(0, data.force(), 0));
        } catch (Exception e) {
        FathommodMod.LOGGER.error(e);
        }
    }

    public static void handleOnServer(final DoubleJumpMessage.DoubleJumpPacket data, final IPayloadContext context) {
        try {
            Player entity = context.player();
            if ((DevUtils.hasTrinket(entity, TrinketDict.DOUBLE_JUMP) || DevUtils.hasTrinket(entity, TrinketDict.DOUBLE_DOUBLE_JUMP_AND_JUMP_HEIGHT_ON_CRACK_BRO_THIS_IS_SO_OVERPOWERED_AND_TRASH_AT_THE_SAME_TIME_BECAUSE_YOU_TAKE_HELLA_FALL_DAMAGE)) && entity.getData(FathommodModVariables.PLAYER_VARIABLES).doubleJumpCooldownInt == 0 && (!entity.getData(FathommodModVariables.PLAYER_VARIABLES).doubleJumpCooldown || (DevUtils.hasTrinket(entity, TrinketDict.DOUBLE_DOUBLE_JUMP_AND_JUMP_HEIGHT_ON_CRACK_BRO_THIS_IS_SO_OVERPOWERED_AND_TRASH_AT_THE_SAME_TIME_BECAUSE_YOU_TAKE_HELLA_FALL_DAMAGE) && !entity.getData(FathommodModVariables.PLAYER_VARIABLES).secondDoubleJumpUsed)) && !entity.isFallFlying() && !entity.onClimbable() && !entity.isInWater()) {
                PacketDistributor.sendToPlayer((ServerPlayer) entity, new DoubleJumpMessage.DoubleJumpPacket(DevUtils.hasTrinket(entity, TrinketDict.DOUBLE_DOUBLE_JUMP_AND_JUMP_HEIGHT_ON_CRACK_BRO_THIS_IS_SO_OVERPOWERED_AND_TRASH_AT_THE_SAME_TIME_BECAUSE_YOU_TAKE_HELLA_FALL_DAMAGE) ? 1.6f : DevUtils.hasTrinket(entity, TrinketDict.JUMP_HEIGHT) ? 1.1f : 0.6f));
                FathommodModVariables.PlayerVariables vars = entity.getData(FathommodModVariables.PLAYER_VARIABLES);
                if (!vars.doubleJumpCooldown) {
                    vars.doubleJumpCooldown = true;
                    vars.secondDoubleJumpUsed = false;
                }
                else
                    vars.secondDoubleJumpUsed = true;
                vars.doubleJumpCooldownInt = 10;
                entity.fallDistance = 0;
                vars.syncPlayerVariables(entity);
            }
        } catch (Exception e) {
            FathommodMod.LOGGER.error(e);
        }
    }
}
