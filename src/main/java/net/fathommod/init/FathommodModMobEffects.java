package net.fathommod.init;

import net.fathommod.FathommodMod;
import net.fathommod.effect.ComboHitEffect;
import net.fathommod.effect.MovementStunEffect;
import net.fathommod.effect.NoBuildEffect;
import net.fathommod.network.FathommodModVariables;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber
public class FathommodModMobEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, FathommodMod.MOD_ID);
    public static final DeferredHolder<MobEffect, MobEffect> ZERO_BUILD = MOB_EFFECTS.register("zero_build", () -> new NoBuildEffect(MobEffectCategory.NEUTRAL, 0x000000));
    public static final DeferredHolder<MobEffect, MobEffect> MOVEMENT_STUN = MOB_EFFECTS.register("movement_stun", () -> new MovementStunEffect(MobEffectCategory.HARMFUL, 0x000000));
    public static final DeferredHolder<MobEffect, MobEffect> COMBO_HIT = MOB_EFFECTS.register("combo_hit", () -> new ComboHitEffect(MobEffectCategory.HARMFUL, 0x000000));

    @SuppressWarnings("DataFlowIssue")
    public static void expireEffects(MobEffectInstance effect, LivingEntity entity) {
        if (effect.is(FathommodModMobEffects.ZERO_BUILD))
            entity.getAttribute(Attributes.BLOCK_INTERACTION_RANGE).removeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "no_build_effect_modifier"));
        else if (effect.is(FathommodModMobEffects.MOVEMENT_STUN)) {
            entity.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "movement_stun_effect_modifier_walk"));
            entity.getAttribute(Attributes.JUMP_STRENGTH).removeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "movement_stun_effect_modifier_jump"));
            entity.getAttribute(NeoForgeMod.SWIM_SPEED).removeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "movement_stun_effect_modifier_swim"));
            if (entity instanceof Player)
                entity.getAttribute(NeoForgeMod.CREATIVE_FLIGHT).removeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "movement_stun_effect_modifier_fly"));
        } else if (effect.is(FathommodModMobEffects.COMBO_HIT)) {
            FathommodModVariables.EntityVariables vars = entity.getData(FathommodModVariables.ENTITY_VARIABLES);
            vars.comboHitSource = "";
            vars.syncPlayerVariables(entity);
        }
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void onEffectRemoved(MobEffectEvent.Remove event) {
        MobEffectInstance effectInstance = event.getEffectInstance();
        if (effectInstance != null) {
            expireEffects(effectInstance, event.getEntity());
        }
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void onEffectExpired(MobEffectEvent.Expired event) {
        MobEffectInstance effectInstance = event.getEffectInstance();
        if (effectInstance != null) {
            expireEffects(effectInstance, event.getEntity());
        }
    }
}
