
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.youpieceof.init;

import net.mcreator.youpieceof.client.renderer.*;
import net.mcreator.youpieceof.entity.RockRenderer;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class FathommodModEntityRenderers {
	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(FathommodModEntities.TNT_ARROW.get(), TNTArrowRenderer::new);
		event.registerEntityRenderer(FathommodModEntities.THROWING_KNIVES_ENTITY.get(), ThrowingKnivesEntityRenderer::new);
		event.registerEntityRenderer(FathommodModEntities.BLOOD_DWELLER.get(), BloodDwellerRenderer::new);
		event.registerEntityRenderer(FathommodModEntities.TED.get(), TedRenderer::new);
		event.registerEntityRenderer(FathommodModEntities.ROCK.get(), RockRenderer::new);
		event.registerEntityRenderer(FathommodModEntities.TED_SPAWNER.get(), TedSpawnerRenderer::new);
	}
}
