package net.fathommod.mixins;

import net.fathommod.item.SweetSpotItem;
import net.fathommod.item.SweetSpotRange;
import net.minecraft.world.item.AxeItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(AxeItem.class)
public class AxeMixin implements SweetSpotItem {

    @Override
    @Unique
    public SweetSpotRange getSweetSpotRange() {
        return new SweetSpotRange(.75, .9);
    }
}
