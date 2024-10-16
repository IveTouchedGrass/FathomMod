package net.mcreator.youpieceof.client.renderer;

import net.mcreator.youpieceof.client.model.BloodDwellerModel;
import net.mcreator.youpieceof.entity.BloodDwellerEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BloodDwellerRenderer extends GeoEntityRenderer<BloodDwellerEntity> {
    public BloodDwellerRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BloodDwellerModel());
        this.shadowRadius = 0.5f;
    }

    @Override
    public ResourceLocation getTextureLocation(BloodDwellerEntity entity) {
        return ResourceLocation.parse("fathommod:textures/entity/blooddwellertexture.png");
    }
}
