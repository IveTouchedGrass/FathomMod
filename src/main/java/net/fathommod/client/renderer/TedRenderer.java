package net.fathommod.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fathommod.Config;
import net.fathommod.client.model.TedModel;
import net.fathommod.entity.ted.TedEntity;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class TedRenderer extends GeoEntityRenderer<TedEntity> {
    public TedRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new TedModel());
        this.shadowRadius = 1f;
    }

    @Override
    public boolean shouldRender(@NotNull TedEntity entity, @NotNull Frustum frustum, double manwtfisthis, double idkwtfthisislol, double uhsamethinghere) {
        return Config.isDevelopment || super.shouldRender(entity, frustum, manwtfisthis, idkwtfthisislol, uhsamethinghere);
    }

    @Override
    public void render(TedEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        if (Config.isDevelopment) {
            if (entity.getSwipeAABB() != null && !entity.isNoAi()) {
                this.renderAABB(poseStack, entity.getSwipeAABB(), bufferSource, entity, 0, 255, 0, 1);
            }
            if (entity.getInstaKillAABB() != null && !entity.isNoAi()) {
                this.renderAABB(poseStack, entity.getInstaKillAABB(), bufferSource, entity, 255, 0, 0, 1);
            }
            if (entity.getTeleportAABB() != null && !entity.isNoAi()) {
                this.renderAABB(poseStack, entity.getTeleportAABB(), bufferSource, entity, 69, 125, 42, 0.5f);
            }
        } else {
            RenderSystem.enableCull();
        }
    }

    private void renderAABB(PoseStack poseStack, AABB aabb, MultiBufferSource bufferSource, Entity entity, float red, float green, float blue, float alpha) {
        poseStack.pushPose();
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.lines());
        LevelRenderer.renderLineBox(
                poseStack, consumer,
                aabb.move(-entity.getX(), -entity.getY(), -entity.getZ()),
                red / 255, green / 255, blue / 255, alpha
        );
        poseStack.popPose();
    }

    @SuppressWarnings("ConstantConditionalExpression")
    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull TedEntity entity) {
        return true /* any condition */ ? TedEntity.TEXTURE_LOCATION /* if condition is met */ : ResourceLocation.parse("other texture") /* if it isnt met */;
    }
}
