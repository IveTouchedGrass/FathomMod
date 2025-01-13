package net.fathommod.init;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import net.fathommod.FathommodMod;
import net.fathommod.network.FathommodModVariables;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.GAME, modid = FathommodMod.MOD_ID)
@SuppressWarnings("unused")
public class FathommodModCommands {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        dispatcher.register(
                Commands.literal("godmode").requires(source -> source.hasPermission(2))
                        .executes(context -> {
                            try {
                                ServerPlayer player = context.getSource().getPlayer();
                                if (player == null) {
                                    context.getSource().sendFailure(Component.translatable("commands.fathommod.godmode.fails.not_a_player"));
                                    return 0;
                                }
                                FathommodModVariables.EntityVariables vars = player.getData(FathommodModVariables.ENTITY_VARIABLES);
                                vars.isGodMode = !vars.isGodMode;
                                if (!vars.isGodMode) {
                                    player.getAbilities().invulnerable = false;
                                }
                                context.getSource().sendSuccess(() -> Component.translatable(vars.isGodMode ? "commands.fathommod.godmode.success.enable" : "commands.fathommod.godmode.success.disable"), true);
                                vars.syncPlayerVariables(player);
                                return 1;
                            } catch (Exception e) {
                                FathommodMod.LOGGER.error(e);
                                e.printStackTrace(System.out);
                                context.getSource().sendFailure(Component.translatable("commands.fathommod.generic_fail"));
                                return 0;
                            }
                        })
                        .then(Commands.argument("player", EntityArgument.player())
                                .executes(context -> {
                                    try {
                                        ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                        FathommodModVariables.EntityVariables vars = player.getData(FathommodModVariables.ENTITY_VARIABLES);
                                        vars.isGodMode = !vars.isGodMode;
                                        vars.syncPlayerVariables(player);
                                        if (!vars.isGodMode) {
                                            player.getAbilities().invulnerable = false;
                                        }
                                        context.getSource().sendSuccess(() -> Component.translatable(vars.isGodMode ? "commands.fathommod.godmode.success.enable.target" : "commands.fathommod.godmode.success.disable.target", player.toString()), true);
                                        return 1;
                                    } catch (Exception e) {
                                        FathommodMod.LOGGER.error(e);
                                        e.printStackTrace(System.out);
                                        context.getSource().sendFailure(Component.translatable("commands.fathommod.generic_fail"));
                                        return 0;
                                    }
                                })
                                .then(Commands.argument("enable", BoolArgumentType.bool())
                                        .executes(context -> {
                                            try {
                                                ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                                FathommodModVariables.EntityVariables vars = player.getData(FathommodModVariables.ENTITY_VARIABLES);
                                                vars.isGodMode = context.getArgument("enable", Boolean.class);
                                                if (!vars.isGodMode) {
                                                    player.getAbilities().invulnerable = false;
                                                }
                                                context.getSource().sendSuccess(() -> Component.translatable(vars.isGodMode ? "commands.fathommod.godmode.success.enable.target" : "commands.fathommod.godmode.success.disable.target", player.toString()), true);
                                                return 1;
                                            } catch (Exception e) {
                                                FathommodMod.LOGGER.error(e);
                                                e.printStackTrace(System.out);
                                                context.getSource().sendFailure(Component.translatable("commands.fathommod.generic_fail"));
                                                return 0;
                                            }
                                        })
                                )
                        ));

    }
}
