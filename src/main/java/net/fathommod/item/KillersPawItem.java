package net.fathommod.item;

import net.fathommod.DevUtils;
import net.fathommod.FathommodMod;
import net.fathommod.init.FathommodModItems;
import net.fathommod.network.FathommodModVariables;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

public class KillersPawItem extends Item {

    public KillersPawItem() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        InteractionResultHolder<ItemStack> result = (player.getCooldowns().isOnCooldown(FathommodModItems.KILLERS_PAW.get())) ? InteractionResultHolder.success(player.getItemBySlot(EquipmentSlot.MAINHAND)) : InteractionResultHolder.fail(player.getItemBySlot(EquipmentSlot.MAINHAND));
        return result;
    }
}
