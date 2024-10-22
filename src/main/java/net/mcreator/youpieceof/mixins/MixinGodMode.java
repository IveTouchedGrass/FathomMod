package net.mcreator.youpieceof.mixins;

import net.mcreator.youpieceof.init.FathommodModMobEffects;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class MixinElytra {
    @ModifyReturnValue(method = "hurt", at = @At("RETURN"))
    public boolean hurt(boolean original) 
{
        Player instance = (Player) (Object) this;
        return instance.getData(FathommodModVariables.PLAYER_VARIABLES).isGodMode ? false : original
}
}


