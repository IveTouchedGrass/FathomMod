package net.mcreator.youpieceof.entity;

import net.mcreator.youpieceof.entity.model.RockModel;
import net.mcreator.youpieceof.entity.ted.ROCK;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RockRenderer extends GeoEntityRenderer<ROCK> {
    public RockRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new RockModel());
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull ROCK entity) {
        return ROCK.TEXTURE_LOCATION;
    }
}
