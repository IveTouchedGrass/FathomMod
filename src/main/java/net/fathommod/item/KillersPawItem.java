package net.fathommod.item;

import net.fathommod.init.FathommodModItems;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class KillersPawItem extends Item {

    public KillersPawItem() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        return (player.getCooldowns().isOnCooldown(FathommodModItems.KILLERS_PAW.get())) ? InteractionResultHolder.success(player.getItemBySlot(EquipmentSlot.MAINHAND)) : InteractionResultHolder.fail(player.getItemBySlot(EquipmentSlot.MAINHAND));
    }
}
