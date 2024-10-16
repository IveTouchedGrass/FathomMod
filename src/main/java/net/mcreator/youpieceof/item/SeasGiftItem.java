
package net.mcreator.youpieceof.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.food.FoodProperties;

public class SeasGiftItem extends Item {
	public SeasGiftItem() {
		super(new Item.Properties().stacksTo(1).rarity(Rarity.COMMON).food((new FoodProperties.Builder()).nutrition(10).saturationModifier(10f).alwaysEdible().build()));
	}

	@Override
	public UseAnim getUseAnimation(ItemStack itemstack) {
		return UseAnim.NONE;
	}

	@Override
	public int getUseDuration(ItemStack itemstack, LivingEntity entity) {
		return 0;
	}
}
