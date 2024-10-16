package net.mcreator.youpieceof.effect;

import net.mcreator.youpieceof.Config;
import net.mcreator.youpieceof.DevUtils;
import net.mcreator.youpieceof.FathommodMod;
import net.mcreator.youpieceof.init.FathommodModMobEffects;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.extensions.common.IClientMobEffectExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.common.NeoForgeMod;

@EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class MovementStunEffect extends MobEffect {
    public MovementStunEffect(MobEffectCategory p_19451_, int p_19452_) {
        super(p_19451_, p_19452_);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (Config.isDevelopment)
            DevUtils.executeCommandAs(entity, "say Applying stun modifiers!");
        entity.getAttribute(Attributes.MOVEMENT_SPEED).addOrUpdateTransientModifier(new AttributeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "movement_stun_effect_modifier_walk"), -238, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        entity.getAttribute(Attributes.JUMP_STRENGTH).addOrUpdateTransientModifier(new AttributeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "movement_stun_effect_modifier_jump"), -238, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        entity.getAttribute(NeoForgeMod.SWIM_SPEED).addOrUpdateTransientModifier(new AttributeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "movement_stun_effect_modifier_swim"), -238, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        if (entity instanceof Player)
            entity.getAttribute(NeoForgeMod.CREATIVE_FLIGHT).addOrUpdateTransientModifier(new AttributeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "movement_stun_effect_modifier_fly"), -238, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        return entity instanceof Player && !((Player) entity).isCreative();
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @SubscribeEvent
    public static void registerMobEffectExtensions(RegisterClientExtensionsEvent event) {
        event.registerMobEffect(new IClientMobEffectExtensions() {
            @Override
            public boolean isVisibleInInventory(MobEffectInstance effect) {
                return false;
            }

            @Override
            public boolean renderInventoryText(MobEffectInstance instance, EffectRenderingInventoryScreen<?> screen, GuiGraphics guiGraphics, int x, int y, int blitOffset) {
                return false;
            }

            @Override
            public boolean isVisibleInGui(MobEffectInstance effect) {
                return false;
            }
        }, FathommodModMobEffects.MOVEMENT_STUN.get());
    }
}