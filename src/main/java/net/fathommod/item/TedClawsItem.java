package net.fathommod.item;

import net.fathommod.DamageClasses;
import net.fathommod.DamageTypedWeapon;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TedClawsItem extends Item implements DamageTypedWeapon {
    private static final Tier TOOL_TIER = new Tier() {

        @Override
        public int getUses() {
            return 0;
        }

        @Override
        public float getSpeed() {
            return 0;
        }

        @Override
        public float getAttackDamageBonus() {
            return 0;
        }

        @Override
        public TagKey<Block> getIncorrectBlocksForDrops() {
            return null;
        }

        @Override
        public int getEnchantmentValue() {
            return 0;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return null;
        }
    };

    public TedClawsItem() {
        super(new Properties().stacksTo(1).rarity(Rarity.COMMON).attributes(SwordItem.createAttributes(TOOL_TIER, 5, -2.5f)));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemstack, @NotNull TooltipContext context, @NotNull List<Component> components, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(itemstack, context, components, tooltipFlag);
        components.add(Component.translatable("tooltip.fathommod.ted_claws.first_line").withStyle(ChatFormatting.GRAY));
        components.add(Component.translatable("tooltip.fathommod.ted_claws.second_line").setStyle(net.minecraft.network.chat.Style.EMPTY.withColor(0x58a7bf)));
    }

    @Override
    public DamageClasses getDamageClass() {
        return DamageClasses.MELEE;
    }
}
