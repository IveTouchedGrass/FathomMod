package net.fathommod.effect;

import net.fathommod.FathommodMod;
import net.fathommod.init.FathommodModMobEffects;
import net.fathommod.network.FathommodModVariables;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.extensions.common.IClientMobEffectExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = FathommodMod.MOD_ID)
public class ComboHitEffect extends MobEffect {
    public ComboHitEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int p_295329_, int p_295167_) {
        return true;
    }

    @Override
    public boolean applyEffectTick(@NotNull LivingEntity entity, int amplifier) {
        try {
            if (entity.level() instanceof ServerLevel level) {
                entity.hurt(new DamageSource(entity.level().holderOrThrow(ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "ted_weapon_combo"))), level.getEntity(UUID.fromString(entity.getData(FathommodModVariables.ENTITY_VARIABLES).comboHitSource))), (float) amplifier / 5);
                return true;
            }
            return !entity.isInvulnerable() || entity.level().isClientSide();
        } catch (IllegalArgumentException e) {
            return true;
        }
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
        }, FathommodModMobEffects.COMBO_HIT.get());
    }
}
