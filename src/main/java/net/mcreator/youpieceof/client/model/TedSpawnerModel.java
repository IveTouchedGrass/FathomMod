package net.mcreator.youpieceof.client.model;

import net.mcreator.youpieceof.entity.ted.TedEntity;
import net.mcreator.youpieceof.entity.ted.TedSpawner;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class TedSpawnerModel extends GeoModel<TedSpawner> {
    @Override
    public ResourceLocation getModelResource(TedSpawner animatable) {
        return ResourceLocation.parse("fathommod:geo/ted.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(TedSpawner animatable) {
        return TedSpawner.TEXTURE_LOCATION;
    }

    @Override
    public ResourceLocation getAnimationResource(TedSpawner animatable) {
        return ResourceLocation.parse("fathommod:animations/ted_animation_set_no_rabbits.json");
    }
}
