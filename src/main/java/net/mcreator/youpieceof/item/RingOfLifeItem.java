
package net.mcreator.youpieceof.item;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;

public class RingOfLifeItem extends Item {
	public RingOfLifeItem() {
		super(new Item.Properties().stacksTo(1).rarity(Rarity.COMMON));
	}
}
