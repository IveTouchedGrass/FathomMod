
package net.fathommod.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BlackSmithsWillItem extends Item {
	public BlackSmithsWillItem() {
		super(new Item.Properties().stacksTo(1).rarity(Rarity.COMMON));
	}

	@Override
	public void appendHoverText(ItemStack p_41421_, TooltipContext p_339594_, @NotNull List<Component> components, TooltipFlag p_41424_) {
		components.add(Component.translatable("tooltip.fathommod.black_smiths_will.first_line"));
		components.add(Component.translatable("tooltip.fathommod.black_smiths_will.second_line"));

		super.appendHoverText(p_41421_, p_339594_, components, p_41424_);
	}
}
