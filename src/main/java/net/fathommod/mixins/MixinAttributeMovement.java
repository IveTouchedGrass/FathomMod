package net.fathommod.mixins;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fathommod.ClientVars;
import net.fathommod.DevUtils;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AttributeInstance.class)
@SuppressWarnings("unused")
public class MixinAttributeMovement {
    @ModifyReturnValue(method = "getValue", at = @At("RETURN"))
    public double getValue(double original) {
        //noinspection DataFlowIssue
        AttributeInstance instance = (AttributeInstance) (Object) this;
        return instance.getAttribute() == Attributes.MOVEMENT_SPEED && DevUtils.getEntityFromAttributeInstance(instance).isPresent() && DevUtils.inverseLerp(-30, 80, ClientVars.movementHeldTimeTicks) <= 1 ? original * DevUtils.inverseLerp(-30, 80, ClientVars.movementHeldTimeTicks) : original;
    }
}
