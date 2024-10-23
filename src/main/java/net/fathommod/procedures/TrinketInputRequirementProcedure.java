package net.fathommod.procedures;

import net.fathommod.Trinkets;
import net.minecraft.world.item.ItemStack;

public class TrinketInputRequirementProcedure {
	public static boolean execute(ItemStack itemstack) {
		return Trinkets.ALL_TRINKETS.contains(itemstack.getItem());
	}
}
