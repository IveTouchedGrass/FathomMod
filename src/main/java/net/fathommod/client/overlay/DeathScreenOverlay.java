package net.fathommod.client.overlay;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fathommod.FathommodMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

@SuppressWarnings("unused")
@EventBusSubscriber(Dist.CLIENT)
public class DeathScreenOverlay {
    public static final ResourceLocation BLACK_OVERLAY = ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "textures/screens/do_not_ask_why_this_is_8k.png");
//    public static final ResourceLocation TUNNEL_OVERLAY = ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "textures/screens/death_tunnel.png");

    @SubscribeEvent
    public static void render(RenderGuiEvent.Pre event) {
        int w = event.getGuiGraphics().guiWidth();
        int h = event.getGuiGraphics().guiHeight();

        Level world = null;
        double x = 0;
        double y = 0;
        double z = 0;

        Player entity = Minecraft.getInstance().player;
        if (entity != null) {
            world = entity.level();
            x = entity.getX();
            y = entity.getY();
            z = entity.getZ();
        }

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderSystem.setShaderColor(1, 1, 1, 1);

        if (false) {

            event.getGuiGraphics().blit(BLACK_OVERLAY, w / 2 + 94, h / 2 + -32, 0, 0, 64, 64, 64, 64);

        }

        RenderSystem.depthMask(true);
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1, 1, 1, 1);

    }
}
