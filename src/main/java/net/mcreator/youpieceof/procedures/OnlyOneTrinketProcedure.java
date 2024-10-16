package net.mcreator.youpieceof.procedures;

import net.mcreator.youpieceof.DevUtils;
import net.mcreator.youpieceof.FathommodMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;

public class OnlyOneTrinketProcedure {
	public static boolean execute(ItemStack itemstack) {
		final TagKey<Item> TRINKETS_TAG = TagKey.create(
				Registries.ITEM,
				ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "trinkets")
		);
		return true;
	}
}
