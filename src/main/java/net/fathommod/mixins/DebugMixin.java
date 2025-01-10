package net.fathommod.mixins;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ServerboundPlayerInputPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerboundPlayerInputPacket.class)
public class DebugMixin {
    @Inject(method = "<init>(FFZZ)V", at = @At("CTOR_HEAD"))
    public void ctor(float p_134345_, float p_134346_, boolean p_134347_, boolean p_134348_, CallbackInfo ci) throws Exception {
        throw new Exception("oml i have to THROW ERRORS to figure mojang's spaghetti code ong 🙏🙏");
    }
    @Inject(method = "<init>(Lnet/minecraft/network/FriendlyByteBuf;)V", at = @At("CTOR_HEAD"))
    public void ctor(FriendlyByteBuf thing, CallbackInfo ci) throws Exception {
        throw new Exception("oml i have to THROW ERRORS to figure mojang's spaghetti code ong 🙏🙏");
    }
}
