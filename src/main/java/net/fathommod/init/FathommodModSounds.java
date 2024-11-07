
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.fathommod.init;

import net.fathommod.FathommodMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class FathommodModSounds {
	public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(Registries.SOUND_EVENT, FathommodMod.MOD_ID);
	public static final DeferredHolder<SoundEvent, SoundEvent> PEPSIMAN = REGISTRY.register("pepsiman", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("fathommod", "pepsiman")));
	public static final DeferredHolder<SoundEvent, SoundEvent> SMACK = REGISTRY.register("smack", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("fathommod", "smack")));

}
