
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.fathommod.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;

import net.fathommod.FathommodMod;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class FathommodModTabs {
	public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, FathommodMod.MOD_ID);
	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> FATHOM_MOD = REGISTRY.register("fathom_mod",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.fathommod.fathom_mod")).icon(() -> new ItemStack(FathommodModItems.WHY_THO.get())).displayItems((parameters, tabData) -> {
				tabData.accept(FathommodModBlocks.PALTN.get().asItem());
				tabData.accept(FathommodModItems.PALTN_SCYTHE.get());
				tabData.accept(FathommodModItems.CRUSIFIX.get());
				tabData.accept(FathommodModItems.WOOD_BAT.get());
				tabData.accept(FathommodModItems.METAL_BAT.get());
				tabData.accept(FathommodModItems.ARIAN.get());
				tabData.accept(FathommodModItems.AXEPICK.get());
				tabData.accept(FathommodModItems.EMPIRIAN_SWORD.get());
				tabData.accept(FathommodModItems.PHASING_TEXAS.get());
				tabData.accept(FathommodModItems.BASIC_SPEAR.get());
				tabData.accept(FathommodModItems.FABRIC.get());
				tabData.accept(FathommodModBlocks.BLOOD_GRASS.get().asItem());
				tabData.accept(FathommodModItems.BOXING_GLOVES.get());
				tabData.accept(FathommodModItems.RIOT_SHIELD.get());
				tabData.accept(FathommodModItems.TNT_ARROW_ITEM.get());
				tabData.accept(FathommodModItems.LOST_ONES_BOW.get());
				tabData.accept(FathommodModItems.THROWING_KNIVES.get());
				tabData.accept(FathommodModItems.ARROW_SHARPENER.get());
				tabData.accept(FathommodModItems.ARMOR_POLISH.get());
				tabData.accept(FathommodModItems.BALLOON.get());
				tabData.accept(FathommodModItems.CHAIN_HANDLE.get());
				tabData.accept(FathommodModItems.DIG_FASTER.get());
				tabData.accept(FathommodModItems.FROG_LEG.get());
				tabData.accept(FathommodModItems.FROG_SOUL.get());
				tabData.accept(FathommodModItems.FROG_GEAR.get());
				tabData.accept(FathommodModItems.HANDLE_EXTENSION.get());
				tabData.accept(FathommodModItems.LUMINANCE.get());
				tabData.accept(FathommodModItems.LEG_RING.get());
				tabData.accept(FathommodModItems.RING_OF_LIFE.get());
				tabData.accept(FathommodModItems.RING_OF_POWER.get());
				tabData.accept(FathommodModItems.SEAS_GIFT.get());
				tabData.accept(FathommodModItems.BLACK_SMITHS_WILL.get());
				tabData.accept(FathommodModItems.HERMES_BOOTS.get());
				tabData.accept(FathommodModItems.ACHELOUS_BOOTS.get());
				tabData.accept(FathommodModItems.ZEUS_BOOTS.get());
				tabData.accept(FathommodModItems.TED_CLAWS.get());
			}).withSearchBar().build());

	@SubscribeEvent
	public static void buildTabContentsVanilla(BuildCreativeModeTabContentsEvent tabData) {
		if (tabData.getTabKey() == CreativeModeTabs.COMBAT) {

		} else if (tabData.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
		} else if (tabData.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
			tabData.accept(FathommodModBlocks.PLANT_OF_THE_YEAR.get().asItem());
			tabData.accept(FathommodModBlocks.SMALLROSERBUSH.get().asItem());
			tabData.accept(FathommodModBlocks.BIG_ROSERBUSH.get().asItem());
		}
	}
}
