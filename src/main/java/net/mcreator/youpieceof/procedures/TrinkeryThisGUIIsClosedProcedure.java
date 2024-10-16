package net.mcreator.youpieceof.procedures;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;

import net.mcreator.youpieceof.network.FathommodModVariables;

import java.util.function.Supplier;
import java.util.Map;

public class TrinkeryThisGUIIsClosedProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		{
			FathommodModVariables.PlayerVariables _vars = entity.getData(FathommodModVariables.PLAYER_VARIABLES);
			_vars.TrinketKeepINV = (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(0)).getItem() : ItemStack.EMPTY);
			_vars.syncPlayerVariables(entity);
		}
		{
			FathommodModVariables.PlayerVariables _vars = entity.getData(FathommodModVariables.PLAYER_VARIABLES);
			_vars.TrinketKeepINV2 = (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(1)).getItem() : ItemStack.EMPTY);
			_vars.syncPlayerVariables(entity);
		}
		{
			FathommodModVariables.PlayerVariables _vars = entity.getData(FathommodModVariables.PLAYER_VARIABLES);
			_vars.TrinketKeepINV3 = (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(2)).getItem() : ItemStack.EMPTY);
			_vars.syncPlayerVariables(entity);
		}
		{
			FathommodModVariables.PlayerVariables _vars = entity.getData(FathommodModVariables.PLAYER_VARIABLES);
			_vars.TrinketKeepINV4 = (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(3)).getItem() : ItemStack.EMPTY);
			_vars.syncPlayerVariables(entity);
		}
	}
}
