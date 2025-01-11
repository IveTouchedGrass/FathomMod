package net.fathommod.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class GlovesTrinketItem extends Item { // NOT to be confused with the boxing gloves trinket
    public GlovesTrinketItem() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, TooltipContext p_339594_, List<Component> p_41423_, TooltipFlag p_41424_) {
        super.appendHoverText(p_41421_, p_339594_, p_41423_, p_41424_);
        p_41423_.add(Component.translatable("tooltip.fathommod.gloves_trinket.first_line").setStyle(net.minecraft.network.chat.Style.EMPTY.withColor(ChatFormatting.GRAY).withBold(true)));
        p_41423_.add(Component.translatable("tooltip.fathommod.gloves_trinket.second_line").setStyle(net.minecraft.network.chat.Style.EMPTY.withColor(0x58a7bf)));
        p_41423_.add(Component.translatable("tooltip.fathommod.gloves_trinket.third_line").setStyle(net.minecraft.network.chat.Style.EMPTY.withColor(0x58a7bf)));
        p_41423_.add(Component.translatable("tooltip.fathommod.generic_trinket.keybind").setStyle(net.minecraft.network.chat.Style.EMPTY.withColor(ChatFormatting.LIGHT_PURPLE)));
    }
}
