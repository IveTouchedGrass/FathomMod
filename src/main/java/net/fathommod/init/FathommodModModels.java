
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.fathommod.init;

import net.fathommod.client.model.ModelWood_Bat_Projectile;
import net.fathommod.client.model.Modelthrowing_knife_Converted;
import net.fathommod.client.model.Modeltnt_arrow_Converted;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class FathommodModModels {
	@SubscribeEvent
	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(ModelWood_Bat_Projectile.LAYER_LOCATION, ModelWood_Bat_Projectile::createBodyLayer);
		event.registerLayerDefinition(Modeltnt_arrow_Converted.LAYER_LOCATION, Modeltnt_arrow_Converted::createBodyLayer);
		event.registerLayerDefinition(Modelthrowing_knife_Converted.LAYER_LOCATION, Modelthrowing_knife_Converted::createBodyLayer);
	}
}
