//package net.fathommod;
//
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.gui.GuiGraphics;
//import net.minecraft.resources.ResourceLocation;
//import net.neoforged.api.distmarker.Dist;
//import net.neoforged.bus.api.SubscribeEvent;
//import net.neoforged.fml.common.EventBusSubscriber;
//import net.neoforged.neoforge.client.event.RenderGuiEvent;
//
//@EventBusSubscriber(bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT, modid = FathommodMod.MOD_ID)
//public class ClientEvents {
//    @SubscribeEvent
//    public static void onRenderCrosshair(RenderGuiEvent.Post event) {
//        Minecraft mc = Minecraft.getInstance();
//        if (mc.options.getCameraType().isFirstPerson() && mc.screen == null) {
//            GuiGraphics guiGraphics = event.getGuiGraphics();
//
//            // Get the screen width and height using Minecraft's getWindow() method
//            int screenWidth = mc.getWindow().getGuiScaledWidth();
//            int screenHeight = mc.getWindow().getGuiScaledHeight();
//
//            // Calculate the position for the crosshair
//            int x = screenWidth / 2 - 7;
//            int y = screenHeight / 2 - 7;
//
//            // Create ResourceLocation using the correct method
//            ResourceLocation crosshairTexture = ResourceLocation.fromNamespaceAndPath("minecraft", "textures/gui/icons.png");
//
//            // Render only the part of the texture that contains the crosshair (15x15)
//            guiGraphics.blit(crosshairTexture, x, y, 0, 0, 15, 15, 256, 256);
//        }
//    }
//}