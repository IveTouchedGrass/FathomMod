package net.fathommod.init;

import com.mojang.serialization.Codec;
import net.fathommod.FathommodMod;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class FathommodModAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, FathommodMod.MOD_ID);

    public static final Supplier<AttachmentType<String>> COMBO_HIT_SOURCE = ATTACHMENT_TYPES.register("combo_hit_source", () -> AttachmentType.builder(() -> "").serialize(Codec.STRING).build());

    public static final Supplier<AttachmentType<Integer>> COMBO_HIT_COUNT = ATTACHMENT_TYPES.register("combo_hit_count", () -> AttachmentType.builder(() -> 2).serialize(Codec.INT).build());
}
