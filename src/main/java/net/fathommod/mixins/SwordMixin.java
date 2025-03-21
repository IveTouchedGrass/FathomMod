package net.fathommod.mixins;

import net.fathommod.item.SweetSpotItem;
import net.fathommod.item.SweetSpotRange;
import net.minecraft.world.item.SwordItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(SwordItem.class)
public class SwordMixin implements SweetSpotItem {
    @Unique
    public SweetSpotRange getSweetSpotRange() {
        return new SweetSpotRange(.7, .85);
    }
}
