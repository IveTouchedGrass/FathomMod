
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.youpieceof.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.world.level.block.Block;

import net.mcreator.youpieceof.block.SmallroserbushBlock;
import net.mcreator.youpieceof.block.PlantOfTheYearBlock;
import net.mcreator.youpieceof.block.PALTNBlock;
import net.mcreator.youpieceof.block.BloodGrassBlock;
import net.mcreator.youpieceof.block.BigRoserbushBlock;
import net.mcreator.youpieceof.FathommodMod;

public class FathommodModBlocks {
	public static final DeferredRegister.Blocks REGISTRY = DeferredRegister.createBlocks(FathommodMod.MODID);
	public static final DeferredHolder<Block, Block> PALTN = REGISTRY.register("paltn", PALTNBlock::new);
	public static final DeferredHolder<Block, Block> PLANT_OF_THE_YEAR = REGISTRY.register("plant_of_the_year", PlantOfTheYearBlock::new);
	public static final DeferredHolder<Block, Block> SMALLROSERBUSH = REGISTRY.register("smallroserbush", SmallroserbushBlock::new);
	public static final DeferredHolder<Block, Block> BIG_ROSERBUSH = REGISTRY.register("big_roserbush", BigRoserbushBlock::new);
	public static final DeferredHolder<Block, Block> BLOOD_GRASS = REGISTRY.register("blood_grass", BloodGrassBlock::new);
	// Start of user code block custom blocks
	// End of user code block custom blocks
}
