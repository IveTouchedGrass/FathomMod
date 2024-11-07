
package net.fathommod.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class ArrowSharpenerItem extends Item {
	public ArrowSharpenerItem() {
		super(new Item.Properties().stacksTo(64).rarity(Rarity.COMMON));
	}
}
