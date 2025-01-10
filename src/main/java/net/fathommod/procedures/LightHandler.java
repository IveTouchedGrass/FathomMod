package net.fathommod.procedures;

import net.fathommod.DevUtils;
import net.fathommod.FathommodMod;
import net.fathommod.Trinkets;
import net.fathommod.network.FathommodModVariables;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = FathommodMod.MOD_ID)
public class LightHandler {

    @SubscribeEvent
    public static void execute(PlayerTickEvent.Pre event) {
        Player player = event.getEntity();
        if (player.level() instanceof ServerLevel world) {
            BlockPos playerPos = player.blockPosition();
            lightItemCheck(player);
            if (player.getData(FathommodModVariables.ENTITY_VARIABLES).hasLightItem) {
                if (placeLight(world, playerPos))
                    clearSurroundingBlocks(world, playerPos, 15, false);
            } else clearSurroundingBlocks(world, playerPos, 18, true);
        }
    }

    private static void lightItemCheck(Player player) {
        FathommodModVariables.EntityVariables vars = player.getData(FathommodModVariables.ENTITY_VARIABLES);
        vars.hasLightItem = DevUtils.hasTrinket(player, Trinkets.LIGHT);
        vars.syncPlayerVariables(player);
    }

    private static boolean placeLight(ServerLevel world, BlockPos pos) {
        if (world.getBlockState(pos).getBlock() == Blocks.AIR) {
            world.setBlockAndUpdate(pos, Blocks.LIGHT.defaultBlockState());
            return true;
        } else if (world.getBlockState(pos.above(1)).getBlock() == Blocks.AIR) {
            world.setBlockAndUpdate(pos.above(1), Blocks.LIGHT.defaultBlockState());
            return true;
        } else if (world.getBlockState(pos.above(2)).getBlock() == Blocks.AIR) {
            world.setBlockAndUpdate(pos.above(2), Blocks.LIGHT.defaultBlockState());
            return true;
        } else return false;
    }

    private static void clearSurroundingBlocks(ServerLevel world, BlockPos centerPos, int radius, boolean bypass) {
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos pos = centerPos.offset(x, y, z);
                    if ((!(pos.equals(centerPos) ||  pos.equals(centerPos.above(1)) || pos.equals(centerPos.above(2))) || bypass) && world.getBlockState(pos).getBlock() == Blocks.LIGHT) {
                        world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                    }
                }
            }
        }
    }
}

