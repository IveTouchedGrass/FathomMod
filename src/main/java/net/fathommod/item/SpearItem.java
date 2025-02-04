package net.fathommod.item;

import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

public abstract class SpearItem extends SwordItem implements SweetSpotItem {
    public SpearItem(Tier p_43269_, Properties p_43272_) {
        super(p_43269_, p_43272_);
    }

    @Override
    public SweetSpotRange getSweetSpotRange() {
        return new SweetSpotRange(.9, 1);
    }
}
