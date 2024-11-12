
package net.fathommod.item;

import net.fathommod.DamageClasses;
import net.fathommod.DamageTypedWeapon;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MetalBatItem extends AxeItem implements DamageTypedWeapon {
	private static final Tier TOOL_TIER = new Tier() {
		@Override
		public int getUses() {
			return 2500;
		}

		@Override
		public float getSpeed() {
			return 1f;
		}

		@Override
		public float getAttackDamageBonus() {
			return 0;
		}

		@Override
		public TagKey<Block> getIncorrectBlocksForDrops() {
			return BlockTags.INCORRECT_FOR_WOODEN_TOOL;
		}

		@Override
		public int getEnchantmentValue() {
			return 0;
		}

		@Override
		public Ingredient getRepairIngredient() {
			return Ingredient.of(new ItemStack(Items.IRON_INGOT));
		}
	};

	public MetalBatItem() {
		super(TOOL_TIER, new Item.Properties().attributes(DiggerItem.createAttributes(TOOL_TIER, 8f, -3f)));
	}

	@Override
	public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, List<Component> components, @NotNull TooltipFlag tooltipFlag) {
		components.add(Component.translatable("tooltip.fathommod.metal_bat.first_line").setStyle(net.minecraft.network.chat.Style.EMPTY.withColor(ChatFormatting.GRAY)));

		super.appendHoverText(stack, context, components, tooltipFlag);
	}

	@Override
	public DamageClasses getDamageClass() {
		return DamageClasses.MELEE;
	}
}
