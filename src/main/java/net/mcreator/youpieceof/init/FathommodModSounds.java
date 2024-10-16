
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.youpieceof.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;

import net.mcreator.youpieceof.FathommodMod;

public class FathommodModSounds {
	public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(Registries.SOUND_EVENT, FathommodMod.MODID);
	public static final DeferredHolder<SoundEvent, SoundEvent> PEPSIMAN = REGISTRY.register("pepsiman", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("fathommod", "pepsiman")));
	public static final DeferredHolder<SoundEvent, SoundEvent> SMACK = REGISTRY.register("smack", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("fathommod", "smack")));

}
