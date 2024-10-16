
package net.mcreator.youpieceof.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlotGroup;

import net.mcreator.youpieceof.procedures.ArianPlayerFinishesUsingItemProcedure;

public class ArianItem extends Item {
	public ArianItem() {
		super(new Item.Properties().stacksTo(1).fireResistant().rarity(Rarity.COMMON).food((new FoodProperties.Builder()).nutrition(8).saturationModifier(1f).alwaysEdible().build())
				.attributes(ItemAttributeModifiers.builder().add(Attributes.ATTACK_DAMAGE, new AttributeModifier(ResourceLocation.fromNamespaceAndPath("fathommod", "arian_modifier"), 3, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
						.add(Attributes.ATTACK_SPEED, new AttributeModifier(ResourceLocation.fromNamespaceAndPath("fathommod","arian_modifier2"), -2.4, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).build()));
	}

	@Override
	public UseAnim getUseAnimation(ItemStack itemstack) {
		return UseAnim.DRINK;
	}

	@Override
	public float getDestroySpeed(ItemStack itemstack, BlockState state) {
		return 0f;
	}

	@Override
	public ItemStack finishUsingItem(ItemStack itemstack, Level world, LivingEntity entity) {
		ItemStack retval = super.finishUsingItem(itemstack, world, entity);
		double x = entity.getX();
		double y = entity.getY();
		double z = entity.getZ();
		ArianPlayerFinishesUsingItemProcedure.execute(entity);
		return retval;
	}
}
