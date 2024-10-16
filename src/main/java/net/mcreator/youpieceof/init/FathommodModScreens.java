
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.youpieceof.init;

import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.mcreator.youpieceof.client.gui.TrinkeryScreen;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class FathommodModScreens {
	@SubscribeEvent
	public static void clientLoad(RegisterMenuScreensEvent event) {
		event.register(FathommodModMenus.TRINKERY.get(), TrinkeryScreen::new);
	}
}
