
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.youpieceof.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;

import net.minecraft.world.inventory.MenuType;
import net.minecraft.core.registries.Registries;

import net.mcreator.youpieceof.world.inventory.TrinkeryMenu;
import net.mcreator.youpieceof.FathommodMod;

public class FathommodModMenus {
	public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(Registries.MENU, FathommodMod.MODID);
	public static final DeferredHolder<MenuType<?>, MenuType<TrinkeryMenu>> TRINKERY = REGISTRY.register("trinkery", () -> IMenuTypeExtension.create(TrinkeryMenu::new));
}
