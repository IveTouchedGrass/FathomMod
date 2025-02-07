package net.fathommod.item;

import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Tier;

public abstract class BatItem extends AxeItem implements SweetSpotItem {
    public BatItem(Tier p_40521_, Properties p_40524_) {
        super(p_40521_, p_40524_);
    }

    @Override
    public SweetSpotRange getSweetSpotRange() {
        return new SweetSpotRange(.75, .9);
    }
}
