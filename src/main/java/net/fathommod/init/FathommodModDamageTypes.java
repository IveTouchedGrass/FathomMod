package net.fathommod.init;

import net.fathommod.FathommodMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;

public class FathommodModDamageTypes {
    public static final ResourceKey<DamageType> TED_INSTA_KILL = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "ted_insta_kill"));
    public static final ResourceKey<DamageType> TED_ROCK = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "ted_rock"));
    public static final ResourceKey<DamageType> TED_SWIPE = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "ted_swipe"));
    public static final ResourceKey<DamageType> SKILL_ISSUE = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "skill_issue"));
    public static final ResourceKey<DamageType> TED_WEAPON_COMBO = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "ted_weapon_combo"));
}
