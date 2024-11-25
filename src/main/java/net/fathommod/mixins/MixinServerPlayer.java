package net.fathommod.mixins;

import net.fathommod.network.packets.ResetAttackStrengthMessage;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class MixinServerPlayer {
    @Inject(method = "resetAttackStrengthTicker", at = @At("HEAD"))
    public void resetAttackStrengthTicker(CallbackInfo ci) {
        Player instance = (Player) (Object) this;
        if (instance instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(serverPlayer, new ResetAttackStrengthMessage.ResetAttackStrengthPacket(0));
        }
    }
}
