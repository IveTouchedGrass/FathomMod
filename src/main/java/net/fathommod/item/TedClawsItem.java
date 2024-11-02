package net.fathommod.item;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

public class TedClawsItem extends Item {
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
        super(new Properties().stacksTo(1).rarity(Rarity.COMMON).attributes(SwordItem.createAttributes(TOOL_TIER, 1, -2.5f)));
    }


}
