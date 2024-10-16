package net.mcreator.youpieceof.client.model;

import net.mcreator.youpieceof.entity.ted.TedEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class TedModel extends GeoModel<TedEntity> {
    @Override
    public ResourceLocation getModelResource(TedEntity animatable) {
        return ResourceLocation.parse("fathommod:geo/ted.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(TedEntity animatable) {
        return TedEntity.TEXTURE_LOCATION;
    }

    @Override
    public ResourceLocation getAnimationResource(TedEntity animatable) {
        return ResourceLocation.parse("fathommod:animations/ted_animation_set_no_rabbits.json");
    }
}
