package net.fathommod.item;

import net.minecraft.world.item.Item;

@SuppressWarnings("unused")
public abstract class ScytheItem extends Item implements SweetSpotItem {
    public ScytheItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public SweetSpotRange getSweetSpotRange() {
        return new SweetSpotRange(0.8, 1, 1.2);
    }
}
