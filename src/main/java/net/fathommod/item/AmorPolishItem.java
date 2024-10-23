package net.fathommod.item;

import net.fathommod.init.FathommodModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class AmorPolishItem extends Item {
    public AmorPolishItem() {
        super(new Properties().stacksTo(1).rarity(Rarity.COMMON));
    }

    @Override
    public void inventoryTick(ItemStack p_41404_, Level p_41405_, Entity p_41406_, int p_41407_, boolean p_41408_) {
        p_41404_.shrink(1);
        if (p_41406_ instanceof Player) {
            ((Player) p_41406_).addItem(new ItemStack(FathommodModItems.ARMOR_POLISH.get()));
        }

        super.inventoryTick(p_41404_, p_41405_, p_41406_, p_41407_, p_41408_);
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, TooltipContext p_339594_, List<Component> p_41423_, TooltipFlag p_41424_) {
        p_41423_.add(Component.literal("Because of slimey's dyslexia, i had to add this."));
        p_41423_.add(Component.literal("Just put it in your inventory and it will replace itself with the proper item"));

        super.appendHoverText(p_41421_, p_339594_, p_41423_, p_41424_);
    }
}
