
package net.mcreator.youpieceof.item;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;

public class TNTArrowItemItem extends Item {
	public TNTArrowItemItem() {
		super(new Item.Properties().stacksTo(64).rarity(Rarity.COMMON));
	}
}
