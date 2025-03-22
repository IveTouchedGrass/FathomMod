package net.fathommod.mixins;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GLX.class)
public class CrosshairMixin {
    @Inject(method = "_renderCrosshair", at = @At("HEAD"), cancellable = true)
    private static void renderCrosshair(int p_69348_, boolean p_69349_, boolean p_69350_, boolean p_69351_, CallbackInfo ci) {
        ci.cancel();
        if (p_69349_ || p_69350_ || p_69351_) {
            RenderSystem.assertOnRenderThread();
            GlStateManager._depthMask(false);
            GlStateManager._disableCull();
            RenderSystem.setShader(GameRenderer::getRendertypeLinesShader);
            Tesselator tesselator = RenderSystem.renderThreadTesselator();
            BufferBuilder bufferbuilder = tesselator.begin(Mode.LINES, DefaultVertexFormat.POSITION_COLOR_NORMAL);
            RenderSystem.lineWidth(4.0F);
            if (p_69349_) {
                bufferbuilder.addVertex(0.0F, 0.0F, 0.0F).setColor(0xF7D24A).setNormal(1.0F, 0.0F, 0.0F);
                bufferbuilder.addVertex((float)p_69348_, 0.0F, 0.0F).setColor(0xF7D24A).setNormal(1.0F, 0.0F, 0.0F);
            }

            if (p_69350_) {
                bufferbuilder.addVertex(0.0F, 0.0F, 0.0F).setColor(0xF7D24A).setNormal(0.0F, 1.0F, 0.0F);
                bufferbuilder.addVertex(0.0F, (float)p_69348_, 0.0F).setColor(0xF7D24A).setNormal(0.0F, 1.0F, 0.0F);
            }

            if (p_69351_) {
                bufferbuilder.addVertex(0.0F, 0.0F, 0.0F).setColor(0xF7D24A).setNormal(0.0F, 0.0F, 1.0F);
                bufferbuilder.addVertex(0.0F, 0.0F, (float)p_69348_).setColor(0xF7D24A).setNormal(0.0F, 0.0F, 1.0F);
            }

            BufferUploader.drawWithShader(bufferbuilder.buildOrThrow());
            RenderSystem.lineWidth(2.0F);
            bufferbuilder = tesselator.begin(Mode.LINES, DefaultVertexFormat.POSITION_COLOR_NORMAL);
            if (p_69349_) {
                bufferbuilder.addVertex(0.0F, 0.0F, 0.0F).setColor(0xF7D24A).setNormal(1.0F, 0.0F, 0.0F);
                bufferbuilder.addVertex((float)p_69348_, 0.0F, 0.0F).setColor(0xF7D24A).setNormal(1.0F, 0.0F, 0.0F);
            }

            if (p_69350_) {
                bufferbuilder.addVertex(0.0F, 0.0F, 0.0F).setColor(0xF7D24A).setNormal(0.0F, 1.0F, 0.0F);
                bufferbuilder.addVertex(0.0F, (float)p_69348_, 0.0F).setColor(0xF7D24A).setNormal(0.0F, 1.0F, 0.0F);
            }

            if (p_69351_) {
                bufferbuilder.addVertex(0.0F, 0.0F, 0.0F).setColor(0xF7D24A).setNormal(0.0F, 0.0F, 1.0F);
                bufferbuilder.addVertex(0.0F, 0.0F, (float)p_69348_).setColor(0xF7D24A).setNormal(0.0F, 0.0F, 1.0F);
            }

            BufferUploader.drawWithShader(bufferbuilder.buildOrThrow());
            RenderSystem.lineWidth(1.0F);
            GlStateManager._enableCull();
            GlStateManager._depthMask(true);
        }
    }
}
