package net.mcreator.youpieceof.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;

import net.mcreator.youpieceof.network.FathommodModVariables;
import net.mcreator.youpieceof.FathommodMod;

import java.util.function.Supplier;
import java.util.Map;

public class TrinkeryThisGUIIsOpenedProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		FathommodMod.queueServerWork(1, () -> {
			if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
				ItemStack _setstack = entity.getData(FathommodModVariables.PLAYER_VARIABLES).TrinketKeepINV.copy();
				_setstack.setCount(1);
				((Slot) _slots.get(0)).set(_setstack);
				_player.containerMenu.broadcastChanges();
			}
			if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
				ItemStack _setstack = entity.getData(FathommodModVariables.PLAYER_VARIABLES).TrinketKeepINV2.copy();
				_setstack.setCount(1);
				((Slot) _slots.get(1)).set(_setstack);
				_player.containerMenu.broadcastChanges();
			}
			if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
				ItemStack _setstack = entity.getData(FathommodModVariables.PLAYER_VARIABLES).TrinketKeepINV3.copy();
				_setstack.setCount(1);
				((Slot) _slots.get(2)).set(_setstack);
				_player.containerMenu.broadcastChanges();
			}
			if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
				ItemStack _setstack = entity.getData(FathommodModVariables.PLAYER_VARIABLES).TrinketKeepINV4.copy();
				_setstack.setCount(1);
				((Slot) _slots.get(3)).set(_setstack);
				_player.containerMenu.broadcastChanges();
			}
		});
	}
}
