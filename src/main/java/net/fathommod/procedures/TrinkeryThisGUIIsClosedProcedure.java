package net.fathommod.procedures;

import net.fathommod.network.FathommodModVariables;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.Map;
import java.util.function.Supplier;

public class TrinkeryThisGUIIsClosedProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		{
			FathommodModVariables.EntityVariables _vars = entity.getData(FathommodModVariables.ENTITY_VARIABLES);
			_vars.trinket1 = (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(0)).getItem() : ItemStack.EMPTY);
			_vars.syncPlayerVariables(entity);
		}
		{
			FathommodModVariables.EntityVariables _vars = entity.getData(FathommodModVariables.ENTITY_VARIABLES);
			_vars.trinket2 = (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(1)).getItem() : ItemStack.EMPTY);
			_vars.syncPlayerVariables(entity);
		}
		{
			FathommodModVariables.EntityVariables _vars = entity.getData(FathommodModVariables.ENTITY_VARIABLES);
			_vars.trinket3 = (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(2)).getItem() : ItemStack.EMPTY);
			_vars.syncPlayerVariables(entity);
		}
		{
			FathommodModVariables.EntityVariables _vars = entity.getData(FathommodModVariables.ENTITY_VARIABLES);
			_vars.trinket4 = (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(3)).getItem() : ItemStack.EMPTY);
			_vars.syncPlayerVariables(entity);
		}
	}
}
