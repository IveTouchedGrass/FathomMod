package net.fathommod.init;

import net.fathommod.item.*;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.BlockItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.client.renderer.item.ItemProperties;

import net.fathommod.FathommodMod;

public class FathommodModItems {
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(BuiltInRegistries.ITEM, FathommodMod.MODID);
	public static final DeferredHolder<Item, Item> PALTN = block(FathommodModBlocks.PALTN);
	public static final DeferredHolder<Item, Item> PALTN_SCYTHE = REGISTRY.register("paltn_scythe", PaltnScytheItem::new);
	public static final DeferredHolder<Item, Item> CRUSIFIX = REGISTRY.register("crusifix", CrusifixItem::new);
	public static final DeferredHolder<Item, Item> WOOD_BAT = REGISTRY.register("wood_bat", WoodBatItem::new);
	public static final DeferredHolder<Item, Item> METAL_BAT = REGISTRY.register("metal_bat", MetalBatItem::new);
	public static final DeferredHolder<Item, Item> WHY_THO = REGISTRY.register("why_tho", WhyThoItem::new);
	public static final DeferredHolder<Item, Item> ARIAN = REGISTRY.register("arian", ArianItem::new);
	public static final DeferredHolder<Item, Item> AXEPICK = REGISTRY.register("axepick", AxepickItem::new);
	public static final DeferredHolder<Item, Item> EMPIRIAN_SWORD = REGISTRY.register("empirian_sword", EmpirianSwordItem::new);
	public static final DeferredHolder<Item, Item> PLANT_OF_THE_YEAR = block(FathommodModBlocks.PLANT_OF_THE_YEAR);
	public static final DeferredHolder<Item, Item> SMALLROSERBUSH = block(FathommodModBlocks.SMALLROSERBUSH);
	public static final DeferredHolder<Item, Item> BIG_ROSERBUSH = block(FathommodModBlocks.BIG_ROSERBUSH);
	public static final DeferredHolder<Item, Item> PHASING_TEXAS = REGISTRY.register("phasing_texas", PhasingTexasItem::new);
	public static final DeferredHolder<Item, Item> BASIC_SPEAR = REGISTRY.register("basic_spear", BasicSpearItem::new);
	public static final DeferredHolder<Item, Item> FABRIC = REGISTRY.register("fabric", FabricItem::new);
	public static final DeferredHolder<Item, Item> BLOOD_GRASS = block(FathommodModBlocks.BLOOD_GRASS);
	public static final DeferredHolder<Item, Item> BOXING_GLOVES = REGISTRY.register("boxing_gloves", BoxingGlovesItem::new);
	public static final DeferredHolder<Item, Item> RIOT_SHIELD = REGISTRY.register("riot_shield", RiotShieldItem::new);
	public static final DeferredHolder<Item, Item> TNT_ARROW_ITEM = REGISTRY.register("tnt_arrow_item", TNTArrowItem::new);
	public static final DeferredHolder<Item, Item> LOST_ONES_BOW = REGISTRY.register("lost_ones_bow", LostOnesBowItem::new);
	public static final DeferredHolder<Item, Item> THROWING_KNIVES = REGISTRY.register("throwing_knives", ThrowingKnivesItem::new);
	public static final DeferredHolder<Item, Item> ARROW_SHARPENER = REGISTRY.register("arrow_sharpener", ArrowSharpenerItem::new);
	public static final DeferredHolder<Item, Item> ARMOR_POLISH = REGISTRY.register("armor_polish", ArmorPolishItem::new);
	public static final DeferredHolder<Item, Item> BALLOON = REGISTRY.register("balloon", BalloonItem::new);
	public static final DeferredHolder<Item, Item> CHAIN_HANDLE = REGISTRY.register("chain_handle", ChainHandleItem::new);
	public static final DeferredHolder<Item, Item> DIG_FASTER = REGISTRY.register("dig_faster", DigFasterItem::new);
	public static final DeferredHolder<Item, Item> FROG_LEG = REGISTRY.register("frog_leg", FrogLegItem::new);
	public static final DeferredHolder<Item, Item> FROG_SOUL = REGISTRY.register("frog_soul", FrogSoulItem::new);
	public static final DeferredHolder<Item, Item> FROG_GEAR = REGISTRY.register("frog_gear", FrogGearItem::new);
	public static final DeferredHolder<Item, Item> HANDLE_EXTENSION = REGISTRY.register("handle_extension", HandleExtensionItem::new);
	public static final DeferredHolder<Item, Item> LUMINANCE = REGISTRY.register("luminance", LuminanceItem::new);
	public static final DeferredHolder<Item, Item> LEG_RING = REGISTRY.register("leg_ring", LegRingItem::new);
	public static final DeferredHolder<Item, Item> RING_OF_LIFE = REGISTRY.register("ring_of_life", RingOfLifeItem::new);
	public static final DeferredHolder<Item, Item> RING_OF_POWER = REGISTRY.register("ring_of_power", RingOfPowerItem::new);
	public static final DeferredHolder<Item, Item> SEAS_GIFT = REGISTRY.register("seas_gift", SeasGiftItem::new);
	public static final DeferredHolder<Item, Item> BLACK_SMITHS_WILL = REGISTRY.register("black_smiths_will", BlackSmithsWillItem::new);
	public static final DeferredHolder<Item, Item> HERMES_BOOTS = REGISTRY.register("hermes_boots", HermesBootsItem::new);
	public static final DeferredHolder<Item, Item> ACHELOUS_BOOTS = REGISTRY.register("achelous_boots", AchelousBootsItem::new);
	public static final DeferredHolder<Item, Item> ZEUS_BOOTS = REGISTRY.register("zeus_boots", net.fathommod.item.ZeusBootsItem::new);
	public static final DeferredHolder<Item, Item> THANKS_SLIMEYS_DYSLEXIA = REGISTRY.register("amor_polish", AmorPolishItem::new);

	// Start of user code block custom items
	// End of user code block custom items
	private static DeferredHolder<Item, Item> block(DeferredHolder<Block, Block> block) {
		return REGISTRY.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
	}

	@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
	public static class ItemsClientSideHandler {
		@SubscribeEvent
		@OnlyIn(Dist.CLIENT)
		public static void clientLoad(FMLClientSetupEvent event) {
			event.enqueueWork(() -> {
				ItemProperties.register(RIOT_SHIELD.get(), ResourceLocation.parse("blocking"), ItemProperties.getProperty(new ItemStack(Items.SHIELD), ResourceLocation.parse("blocking")));
			});
		}
	}
}
