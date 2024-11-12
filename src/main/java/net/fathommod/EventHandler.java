package net.fathommod;

import net.fathommod.entity.ted.TedEntity;
import net.fathommod.init.FathommodModItems;
import net.fathommod.init.FathommodModMobEffects;
import net.fathommod.init.FathommodModSounds;
import net.fathommod.network.DoubleJumpMessage;
import net.fathommod.network.FathommodModVariables;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Bogged;
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Unbreakable;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderNameTagEvent;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.event.entity.living.*;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Objects;

@SuppressWarnings("DataFlowIssue")
@EventBusSubscriber(modid = FathommodMod.MOD_ID)
public class EventHandler {

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft instance = Minecraft.getInstance();

//        boolean isJumpHeld = instance.options.keyJump.isDown();

//        if (instance.level != null && isJumpHeld && !instance.options.keyShift.isDown())
//            PacketDistributor.sendToServer(new FlyMessage.FlyKeyMessage(isJumpHeld, 0));
//        if (!isJumpHeld && instance.level != null && instance.options.keyShift.isDown())
//            PacketDistributor.sendToServer(new FlyShiftMessage.FlyShiftPacket(Minecraft.getInstance().options.keyShift.isDown()));

        instance.smartCull = true;
        instance.options.skipMultiplayerWarning = true;

        if (instance.options.keyShift.isDown() && instance.options.keyJump.isDown() && instance.level != null)
            PacketDistributor.sendToServer(new DoubleJumpMessage.DoubleJumpPacket(0f));
    }

    @SubscribeEvent
    public static void onEntitySetTarget(LivingChangeTargetEvent event) {
        if (event.getNewAboutToBeSetTarget() != null && event.getNewAboutToBeSetTarget().getData(FathommodModVariables.ENTITY_VARIABLES).isGodMode)
            event.setNewAboutToBeSetTarget(null);
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingIncomingDamageEvent event) {
        if (event.getEntity().getData(FathommodModVariables.ENTITY_VARIABLES).isGodMode)
            event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onPlayerRightClick(PlayerInteractEvent.RightClickItem event) {
        ItemStack itemstack = event.getItemStack();
        Player entity = event.getEntity();
        Level world = event.getLevel();
        if (itemstack.getItem() == Items.FIREWORK_ROCKET && itemstack.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.INFINITY)) > 0) {
            entity.addItem(itemstack);
        }
    }

    @SubscribeEvent
    public static void onTagRender(RenderNameTagEvent event) {
        if (event.getEntity() instanceof TedEntity) {
            event.setContent(Component.literal("You gonna die :3"));
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player entity = event.getEntity();
        if (entity instanceof ServerPlayer && entity.getData(FathommodModVariables.ENTITY_VARIABLES).isGodMode) {
            entity.getAbilities().invulnerable = true;
        }
    }

    @SubscribeEvent
    public static void onEffectAdded(MobEffectEvent.Applicable event) {
        MobEffect effect = event.getEffectInstance().getEffect().value();
        if (event.getEntity() instanceof TedEntity)
            event.setResult(effect.isBeneficial() || effect.getClass().getName().contains("ComboHitEffect") ? MobEffectEvent.Applicable.Result.APPLY : MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Pre event) {
        Player entity = event.getEntity();

        blankHandler(entity);

        jumpHeightHandler(entity);

        balloonHandler(entity);

        lifesGambleAttributeHandler(entity);
        {
            FathommodModVariables.EntityVariables vars = entity.getData(FathommodModVariables.ENTITY_VARIABLES);
            if (entity.onGround()) {
                vars.doubleJumpCooldownInt -= 1;
                vars.secondDoubleJumpUsed = false;
                vars.doubleJumpCooldown = false;
            }
            else if (vars.doubleJumpCooldownInt > 0)
                vars.doubleJumpCooldownInt -= 1;
            else
                vars.doubleJumpCooldownInt = 0;
            vars.syncPlayerVariables(entity);
        }
        bootsHandler(entity);

        ringOfLifeHealing(entity);

        chainedHandleCode(entity);

        handleExtensionCode(entity);

        digFasterHandler(entity);

        flightAttributeHandler(entity);

        armorPolishHandler(entity);

        seasGiftHandler(entity);

        unbreakableHandler(entity);

        rangeHandler(entity);
    }

    private static void rangeHandler(LivingEntity entity) {
        if (entity instanceof Player player) {
            if (player.getItemBySlot(EquipmentSlot.MAINHAND).getItem() == FathommodModItems.BOXING_GLOVES.get()) {
                player.getAttribute(Attributes.ENTITY_INTERACTION_RANGE).addOrReplacePermanentModifier(new AttributeModifier(ResourceLocation.parse("fathommod:boxing_gloves_modifier"), -2, AttributeModifier.Operation.ADD_VALUE));
                player.getAttribute(Attributes.ENTITY_INTERACTION_RANGE).removeModifier(ResourceLocation.parse("fathommod:basic_spear_modifier"));
            } else if (player.getItemBySlot(EquipmentSlot.MAINHAND).getItem() == FathommodModItems.BASIC_SPEAR.get()) {
                player.getAttribute(Attributes.ENTITY_INTERACTION_RANGE).removeModifier(ResourceLocation.parse("fathommod:boxing_gloves_modifier"));
                player.getAttribute(Attributes.ENTITY_INTERACTION_RANGE).addOrReplacePermanentModifier(new AttributeModifier(ResourceLocation.parse("fathommod:basic_spear_modifier"), 3.5, AttributeModifier.Operation.ADD_VALUE));
            } else {
                player.getAttribute(Attributes.ENTITY_INTERACTION_RANGE).removeModifier(ResourceLocation.parse("fathommod:boxing_gloves_modifier"));
                player.getAttribute(Attributes.ENTITY_INTERACTION_RANGE).removeModifier(ResourceLocation.parse("fathommod:basic_spear_modifier"));
            }
        }
    }

    private static void unbreakableHandler(LivingEntity entity) {
        unbreakableToggler((DevUtils.hasTrinket(entity, Trinkets.UNBREAKABILITY)), entity);
    }

    public static void unbreakableToggler(boolean enable, Entity entity) {
        ItemStack feet;
        ItemStack legs;
        ItemStack chest;
        ItemStack head;
        if (entity instanceof Player _plr) {
            feet = _plr.getItemBySlot(EquipmentSlot.FEET);
            legs = _plr.getItemBySlot(EquipmentSlot.LEGS);
            chest = _plr.getItemBySlot(EquipmentSlot.CHEST);
            head = _plr.getItemBySlot(EquipmentSlot.HEAD);
        } else return;
        if (enable) {
            feet.set(DataComponents.UNBREAKABLE, new Unbreakable(true));
            legs.set(DataComponents.UNBREAKABLE, new Unbreakable(true));
            chest.set(DataComponents.UNBREAKABLE, new Unbreakable(true));
            head.set(DataComponents.UNBREAKABLE, new Unbreakable(true));
        } else {
            feet.remove(DataComponents.UNBREAKABLE);
            legs.remove(DataComponents.UNBREAKABLE);
            chest.remove(DataComponents.UNBREAKABLE);
            head.remove(DataComponents.UNBREAKABLE);
        }
    }

    @SubscribeEvent
    public static void afterEntityDamage(LivingDamageEvent.Post event) {
        if (event.getSource().getEntity() instanceof ServerPlayer player && player.getItemBySlot(EquipmentSlot.MAINHAND).getItem() == FathommodModItems.METAL_BAT.get()) {
            player.level().playSound(null, player.blockPosition(), FathommodModSounds.BONK.get(), SoundSource.PLAYERS, 2.75f, 1f);
        }
    }

    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        double seed = Math.random();
        LivingEntity entity = event.getEntity();
        Level world = entity.level();
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();

        if (entity instanceof ServerPlayer) {
            for (Entity entityiterator : ((ServerLevel) entity.level()).getEntities().getAll()) {
                if (entityiterator instanceof TedEntity ted) {
                    boolean flag = false;
                    for (Entity entityiteratoriterator : ((ServerLevel) ted.level()).getEntities(ted, ted.getTeleportAABB())) {
                        if (entityiteratoriterator instanceof Player player && !player.isDeadOrDying()) {
                            flag = true;
                            ted.target = player;
                            break;
                        }
                    }
                    if (!flag) {
                        ted.discard();
                    }
                }
            }
        }

        if (event.getSource().getEntity() != null && event.getSource().getEntity() instanceof Rabbit) {
            event.setCanceled(true);
            entity.invulnerableTime = 0;
            entity.die(new DamageSource(entity.level().holderOrThrow(ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath("fathommod", "skill_issue")))));
        }

        if (event.getSource().getEntity() instanceof Player player)
            dropHandler(seed, entity, world, x, y, z, DevUtils.getEnchantLevel(player.getItemBySlot(EquipmentSlot.MAINHAND), Enchantments.LOOTING, player.level()));
    }

    @SuppressWarnings("DuplicateExpressions")
    private static void dropHandler(double seed, LivingEntity entity, Level world, double x, double y, double z, short looting) {
        if (entity instanceof Frog) {
            if (world instanceof ServerLevel _level) {
                ItemEntity entityToSpawn = new ItemEntity(_level, x, y, z, new ItemStack(Trinkets.JUMP_HEIGHT));
                entityToSpawn.setPickUpDelay(10);
                _level.addFreshEntity(entityToSpawn);
            }
            if (seed <= (0.25 * ((1.5 * looting > 0) ? 1 : 1.5 * looting))) {
                if (world instanceof ServerLevel _level) {
                    ItemEntity entityToSpawn = new ItemEntity(_level, x, y, z, new ItemStack(Trinkets.DOUBLE_JUMP));
                    entityToSpawn.setPickUpDelay(10);
                    _level.addFreshEntity(entityToSpawn);
                }
            }
        } else if (entity instanceof ElderGuardian) {
            if (seed <= (0.1 * ((1.5 * looting > 0) ? 1 : 1.5 * looting))) {
                if (world instanceof ServerLevel _level) {
                    ItemEntity entityToSpawn = new ItemEntity(_level, x, y, z, new ItemStack(Trinkets.SEAS_GIFT));
                    entityToSpawn.setPickUpDelay(10);
                    _level.addFreshEntity(entityToSpawn);
                }
            }
        } else if (entity instanceof Zombie || entity instanceof ZombieVillager) {
            if (seed <= (0.05 * ((1.5 * looting > 0) ? 1 : 1.5 * looting))) {
                if (world instanceof ServerLevel _level) {
                    ItemEntity entityToSpawn = new ItemEntity(_level, x, y, z, new ItemStack(Trinkets.ARMOR_POLISH));
                    entityToSpawn.setPickUpDelay(10);
                    _level.addFreshEntity(entityToSpawn);
                }
            }
        } else if (entity instanceof Bogged) {
            if (seed <= (0.05 * ((1.5 * looting > 0) ? 1 : 1.5 * looting))) {
                if (world instanceof ServerLevel _level) {
                    ItemEntity entityToSpawn = new ItemEntity(_level, x, y, z, new ItemStack(Trinkets.DOUBLE_JUMP));
                    entityToSpawn.setPickUpDelay(10);
                    _level.addFreshEntity(entityToSpawn);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onEntityHurt(LivingIncomingDamageEvent event) {
        LivingEntity entity = event.getEntity();
        DamageSource source = event.getSource();
        Entity sourceentity = source.getEntity();

        if (sourceentity instanceof Rabbit rabbit && rabbit.getData(FathommodModVariables.ENTITY_VARIABLES).isTedRabbit) {
            event.setCanceled(true);
            entity.hurt(new DamageSource(entity.level().holderOrThrow(ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "skill_issue")))), event.getAmount());
        }
    }

    @SubscribeEvent
    public static void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer player && player.level() instanceof ServerLevel world && world.players().size() <= 1) {
            for (Entity entity : world.getAllEntities()) {
                if (entity instanceof BossEntity) {
                    entity.discard();
                }
            }
        }
    }

    @SubscribeEvent
    public static void onEntityAttacked(LivingDamageEvent.Pre event) {
        Entity sourceentity = event.getSource().getEntity();
        LivingEntity entity = event.getEntity();
        DamageSource source = event.getSource();

        if (sourceentity instanceof TedEntity ted) {
            ted.currentAttack = TedEntity.Attacks.PURSUIT;
            ted.setRockTimer(240);
        }

        if (arrowSharpenerHandler(source) && sourceentity != null) {
            event.setNewDamage(event.getNewDamage() * 1.2f);
        }
        if (source.getEntity() instanceof LivingEntity && DevUtils.hasTrinket((LivingEntity) source.getEntity(), Trinkets.RING_OF_POWER))
            event.setNewDamage(event.getNewDamage() + 1);
        if (DevUtils.hasTrinket(entity, Trinkets.LIFES_GAMBLE))
            event.setNewDamage(event.getNewDamage() * 2);
        if (sourceentity instanceof LivingEntity livingEntity && livingEntity.getItemBySlot(EquipmentSlot.MAINHAND).getItem() == FathommodModItems.TED_CLAWS.get() && !source.is(ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "ted_weapon_combo")))) {
            FathommodModVariables.EntityVariables vars = entity.getData(FathommodModVariables.ENTITY_VARIABLES);
            vars.comboHitSource = livingEntity.getUUID().toString();
            vars.syncPlayerVariables(livingEntity);
            entity.addEffect(new MobEffectInstance(FathommodModMobEffects.COMBO_HIT, 25, Math.round(event.getNewDamage() * 5), false, false));
//            FathommodMod.queueServerWork(entity.invulnerableTime + 1, () -> entity.hurt(new DamageSource(entity.level().holderOrThrow(DamageTypes.PLAYER_ATTACK), ((ServerLevel) entity.level()).getEntity(UUID.fromString(entity.getData(FathommodModAttachments.COMBO_HIT_SOURCE)))), event.getNewDamage()));
//            FathommodMod.queueServerWork((entity.invulnerableTime * 2) + 1, () -> entity.hurt(new DamageSource(entity.level().holderOrThrow(DamageTypes.PLAYER_ATTACK), ((ServerLevel) entity.level()).getEntity(UUID.fromString(entity.getData(FathommodModAttachments.COMBO_HIT_SOURCE)))), event.getNewDamage()));
        }
    }

    public static boolean arrowSharpenerHandler(DamageSource source) {
        Entity _sourceentity = source.getEntity();
        if (_sourceentity instanceof LivingEntity sourceentity)
            return (DevUtils.hasTrinket(sourceentity, Trinkets.ARROW_SHARPENER));
        else
            return false;
    }

    @SubscribeEvent
    public static void onLivingJump(LivingEvent.LivingJumpEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;
        if (event.getEntity().isShiftKeyDown()) {
            FathommodModVariables.EntityVariables vars = event.getEntity().getData(FathommodModVariables.ENTITY_VARIABLES);
            vars.doubleJumpCooldownInt = 8;
            vars.syncPlayerVariables(event.getEntity());
        } else {
            FathommodModVariables.EntityVariables vars = event.getEntity().getData(FathommodModVariables.ENTITY_VARIABLES);
            vars.doubleJumpCooldownInt = 5;
            vars.syncPlayerVariables(event.getEntity());
        }

        event.getEntity().getAttribute(Attributes.GRAVITY).removeModifier(ResourceLocation.parse("fathommod:test11"));
    }

    private static void balloonHandler(LivingEntity entity) {
        if (DevUtils.hasTrinket(entity, Trinkets.BALLOON)) {
            if (!(entity.getAttributes().hasModifier(Attributes.JUMP_STRENGTH, ResourceLocation.parse("fathommod:test")))) {
                entity.getAttribute(Attributes.JUMP_STRENGTH).addPermanentModifier(new AttributeModifier(ResourceLocation.parse("fathommod:test"), 0.5, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
            }
            if (!(entity.getAttributes().hasModifier(Attributes.FALL_DAMAGE_MULTIPLIER, ResourceLocation.parse("fathommod:test2")))) {
                entity.getAttribute(Attributes.FALL_DAMAGE_MULTIPLIER).addPermanentModifier(new AttributeModifier(ResourceLocation.parse("fathommod:test2"), -999, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
            }
        } else {
            entity.getAttribute(Attributes.JUMP_STRENGTH).removeModifier(ResourceLocation.parse("fathommod:test"));
            entity.getAttribute(Attributes.FALL_DAMAGE_MULTIPLIER).removeModifier(ResourceLocation.parse("fathommod:test2"));
        }
    }

    private static void lifesGambleAttributeHandler(LivingEntity entity) {
        if (DevUtils.hasTrinket(entity, Trinkets.LIFES_GAMBLE)) {
            if (!(entity.getAttributes().hasModifier(Attributes.MAX_HEALTH, ResourceLocation.parse("fathommod:test3")))) {
                entity.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(new AttributeModifier(ResourceLocation.parse("fathommod:test3"), 20, AttributeModifier.Operation.ADD_VALUE));
            }
        } else {
            entity.getAttribute(Attributes.MAX_HEALTH).removeModifier(ResourceLocation.parse("fathommod:test3"));
        }
    }

    private static void ringOfLifeHealing(LivingEntity entity) {
        if (entity.getData(FathommodModVariables.ENTITY_VARIABLES).ringOfLifeRegenCooldown <= 0 && DevUtils.hasTrinket(entity, Trinkets.LIFES_RING)) {
            entity.heal(1F);
            {
                FathommodModVariables.EntityVariables vars = entity.getData(FathommodModVariables.ENTITY_VARIABLES);
                vars.ringOfLifeRegenCooldown = 60;
                vars.syncPlayerVariables(entity);
            }
        } else if (DevUtils.hasTrinket(entity, Trinkets.LIFES_RING)) {
            FathommodModVariables.EntityVariables vars = entity.getData(FathommodModVariables.ENTITY_VARIABLES);
            vars.ringOfLifeRegenCooldown -= 1;
            vars.syncPlayerVariables(entity);
        } else {
            FathommodModVariables.EntityVariables vars = entity.getData(FathommodModVariables.ENTITY_VARIABLES);
            vars.ringOfLifeRegenCooldown = 60;
            vars.syncPlayerVariables(entity);
        }
    }

    private static void chainedHandleCode(LivingEntity entity) {
        if (DevUtils.hasTrinket(entity, Trinkets.CHAINED_HANDLE)) {
            if (!(entity.getAttributes().hasModifier(Attributes.ENTITY_INTERACTION_RANGE, ResourceLocation.parse("fathommod:test4")))) {
                entity.getAttribute(Attributes.ENTITY_INTERACTION_RANGE).addPermanentModifier(new AttributeModifier(ResourceLocation.parse("fathommod:test4"), 3, AttributeModifier.Operation.ADD_VALUE));
            }
            if (!(entity.getAttributes().hasModifier(Attributes.ATTACK_DAMAGE, ResourceLocation.parse("fathommod:test5")))) {
                entity.getAttribute(Attributes.ATTACK_DAMAGE).addPermanentModifier(new AttributeModifier(ResourceLocation.parse("fathommod:test5"), -0.3, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
            }
            entity.getAttribute(Attributes.BLOCK_INTERACTION_RANGE).addOrUpdateTransientModifier(new AttributeModifier(ResourceLocation.parse("fathommod:chain_handle_block"), 3, AttributeModifier.Operation.ADD_VALUE));
        } else {
            entity.getAttribute(Attributes.ENTITY_INTERACTION_RANGE).removeModifier(ResourceLocation.parse("fathommod:test4"));
            entity.getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(ResourceLocation.parse("fathommod:test5"));
            entity.getAttribute(Attributes.BLOCK_INTERACTION_RANGE).removeModifier(ResourceLocation.parse("fathommod:chain_handle_block"));
        }
    }

    private static void handleExtensionCode(LivingEntity entity) {
        if (DevUtils.hasTrinket(entity, Trinkets.HANDLE_EXTENSION)) {
            if (!(entity.getAttributes().hasModifier(Attributes.ENTITY_INTERACTION_RANGE, ResourceLocation.parse("fathommod:handle_extension_entity")))) {
                entity.getAttribute(Attributes.ENTITY_INTERACTION_RANGE).addOrUpdateTransientModifier(new AttributeModifier(ResourceLocation.parse("fathommod:handle_extension_entity"), 1, AttributeModifier.Operation.ADD_VALUE));
                entity.getAttribute(Attributes.BLOCK_INTERACTION_RANGE).addOrUpdateTransientModifier(new AttributeModifier(ResourceLocation.parse("fathommod:handle_extension_block"), 1, AttributeModifier.Operation.ADD_VALUE));
            }
        } else {
            entity.getAttribute(Attributes.ENTITY_INTERACTION_RANGE).removeModifier(ResourceLocation.parse("fathommod:handle_extension_entity"));
            entity.getAttribute(Attributes.BLOCK_INTERACTION_RANGE).removeModifier(ResourceLocation.parse("fathommod:handle_extension_block"));
        }
    }

    private static void flightAttributeHandler(LivingEntity entity) {
        if (entity.getItemBySlot(EquipmentSlot.HEAD).getItem() == Trinkets.WINGS) {
            if (DevUtils.hasTrinket(entity, Trinkets.DOUBLE_DOUBLE_JUMP_AND_JUMP_HEIGHT_ON_CRACK_BRO_THIS_IS_SO_OVERPOWERED_AND_TRASH_AT_THE_SAME_TIME_BECAUSE_YOU_TAKE_HELLA_FALL_DAMAGE)) {
                entity.getItemBySlot(EquipmentSlot.HEAD).set(DataComponents.MAX_DAMAGE, 169);
            } else if (DevUtils.hasTrinket(entity, Trinkets.JUMP_HEIGHT)) {
                entity.getItemBySlot(EquipmentSlot.HEAD).set(DataComponents.MAX_DAMAGE, 100);
            } else {
                entity.getItemBySlot(EquipmentSlot.HEAD).set(DataComponents.MAX_DAMAGE, 77);
            }
            entity.getAttribute(Attributes.FALL_DAMAGE_MULTIPLIER).addOrReplacePermanentModifier(new AttributeModifier(ResourceLocation.parse("test9"), -999, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
            entity.getAttribute(Attributes.GRAVITY).addOrReplacePermanentModifier(new AttributeModifier(ResourceLocation.parse("test10"), -0.15, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        } else {
            entity.getAttribute(Attributes.FALL_DAMAGE_MULTIPLIER).removeModifier(ResourceLocation.parse("fathommod:test9"));
            entity.getAttribute(Attributes.GRAVITY).removeModifier(ResourceLocation.parse("fathommod:test10"));
        }

        if (entity.onGround() && entity.getItemBySlot(EquipmentSlot.HEAD).getItem() == Trinkets.WINGS) {
            entity.getItemBySlot(EquipmentSlot.HEAD).set(DataComponents.DAMAGE, 0);
        }
    }

    private static void jumpHeightHandler(LivingEntity entity) {
        if (DevUtils.hasTrinket(entity, Trinkets.DOUBLE_DOUBLE_JUMP_AND_JUMP_HEIGHT_ON_CRACK_BRO_THIS_IS_SO_OVERPOWERED_AND_TRASH_AT_THE_SAME_TIME_BECAUSE_YOU_TAKE_HELLA_FALL_DAMAGE)) {
            Objects.requireNonNull(entity.getAttribute(Attributes.JUMP_STRENGTH)).addOrUpdateTransientModifier(new AttributeModifier(ResourceLocation.parse("fathommod:hhhhhhhhhhhhhhhhhhhhhhhh"), 2.1, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
            entity.getAttribute(Attributes.SAFE_FALL_DISTANCE).addOrUpdateTransientModifier(new AttributeModifier(ResourceLocation.parse("fathommod:fffdfdfdfdfdfdfdfdfdff"), 9, AttributeModifier.Operation.ADD_VALUE));
            entity.getAttribute(Attributes.SAFE_FALL_DISTANCE).removeModifier(ResourceLocation.parse("fathommod:fffdfdfdfdfdfdfdfdfdf"));
            entity.getAttribute(Attributes.JUMP_STRENGTH).removeModifier(ResourceLocation.parse("fathommod:hhhhhhhhhhhhhhhhhhhhhhhhh"));
        } else if (DevUtils.hasTrinket(entity, Trinkets.JUMP_HEIGHT)) {
            entity.getAttribute(Attributes.JUMP_STRENGTH).removeModifier(ResourceLocation.parse("fathommod:hhhhhhhhhhhhhhhhhhhhhhhh"));
            entity.getAttribute(Attributes.SAFE_FALL_DISTANCE).removeModifier(ResourceLocation.parse("fathommod:fffdfdfdfdfdfdfdfdfdff"));
            Objects.requireNonNull(entity.getAttribute(Attributes.JUMP_STRENGTH)).addOrUpdateTransientModifier(new AttributeModifier(ResourceLocation.parse("fathommod:hhhhhhhhhhhhhhhhhhhhhhhhh"), 0.8, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
            entity.getAttribute(Attributes.SAFE_FALL_DISTANCE).addOrUpdateTransientModifier(new AttributeModifier(ResourceLocation.parse("fathommod:fffdfdfdfdfdfdfdfdfdf"), 3, AttributeModifier.Operation.ADD_VALUE));
        } else {
            entity.getAttribute(Attributes.JUMP_STRENGTH).removeModifier(ResourceLocation.parse("fathommod:hhhhhhhhhhhhhhhhhhhhhhhhh"));
            entity.getAttribute(Attributes.SAFE_FALL_DISTANCE).removeModifier(ResourceLocation.parse("fathommod:fffdfdfdfdfdfdfdfdfdff"));
            entity.getAttribute(Attributes.SAFE_FALL_DISTANCE).removeModifier(ResourceLocation.parse("fathommod:fffdfdfdfdfdfdfdfdfdf"));
            entity.getAttribute(Attributes.JUMP_STRENGTH).removeModifier(ResourceLocation.parse("fathommod:hhhhhhhhhhhhhhhhhhhhhhhh"));
        }
    }

    private static void digFasterHandler(LivingEntity entity) {
        if (DevUtils.hasTrinket(entity, Trinkets.DIG_FASTER)) {
            entity.getAttribute(Attributes.BLOCK_BREAK_SPEED).addOrReplacePermanentModifier(new AttributeModifier(ResourceLocation.parse("fathommod:test8"), 0.25, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        } else {
            if (entity.getAttribute(Attributes.BLOCK_BREAK_SPEED).hasModifier(ResourceLocation.parse("fathommod:test8"))) {
                entity.getAttribute(Attributes.BLOCK_BREAK_SPEED).removeModifier(ResourceLocation.parse("fathommod:test8"));
            }
        }
    }

    private static void armorPolishHandler(LivingEntity entity) {
        if (DevUtils.hasTrinket(entity, Trinkets.ARMOR_POLISH)) {
            entity.getAttribute(Attributes.ARMOR_TOUGHNESS).addOrReplacePermanentModifier(new AttributeModifier(ResourceLocation.parse("fathommod:test12"), 0.5, AttributeModifier.Operation.ADD_VALUE));
        } else {
            entity.getAttribute(Attributes.ARMOR_TOUGHNESS).removeModifier(ResourceLocation.parse("fathommod:test12"));
        }
    }

    private static void blankHandler(LivingEntity entity) {
        if (DevUtils.hasTrinket(entity, Trinkets.LEG_RING)) {
            entity.getAttribute(Attributes.MOVEMENT_SPEED).addOrReplacePermanentModifier(new AttributeModifier(ResourceLocation.parse("fathommod:test13"), 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        } else {
            entity.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(ResourceLocation.parse("fathommod:test13"));
        }
    }

    private static void seasGiftHandler(LivingEntity entity) {
        if (DevUtils.hasTrinket(entity, Trinkets.SEAS_GIFT) && entity.isUnderWater()) {
            entity.getAttribute(Attributes.ARMOR).addOrReplacePermanentModifier(new AttributeModifier(ResourceLocation.parse("test14"), 2, AttributeModifier.Operation.ADD_VALUE));
        } else {
            entity.getAttribute(Attributes.ARMOR).removeModifier(ResourceLocation.parse("test14"));
        }
    }

    @SuppressWarnings("unused")
    public static boolean hasWings(LivingEntity entity) {
        return DevUtils.hasTrinket(entity, Items.GOLDEN_HELMET);
    }

//    @SubscribeEvent
//    public static void onGatherData(GatherDataEvent event) {
//        event.getGenerator().addProvider(event.includeServer(), new MineshaftLootTableModifProvider());
//    }

    private static void bootsHandler(LivingEntity entity) {
        if (DevUtils.hasTrinket(entity, Trinkets.CRACK_ON_CRACK)) {
            entity.getAttribute(Attributes.STEP_HEIGHT).addOrUpdateTransientModifier(new AttributeModifier(ResourceLocation.parse("fathommod:crack_step"), 0.5, AttributeModifier.Operation.ADD_VALUE));
            entity.getAttribute(Attributes.MOVEMENT_SPEED).addOrReplacePermanentModifier(new AttributeModifier(ResourceLocation.parse("fathommod:crack"), 2, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
            entity.getAttribute(NeoForgeMod.SWIM_SPEED).addOrReplacePermanentModifier(new AttributeModifier(ResourceLocation.parse("fathommod:water_crack"), 4, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        } else if (DevUtils.hasTrinket(entity, Trinkets.CRACK_BUT_FOR_WATER) && DevUtils.hasTrinket(entity, Trinkets.CRACK)) {
            entity.getAttribute(Attributes.STEP_HEIGHT).addOrUpdateTransientModifier(new AttributeModifier(ResourceLocation.parse("fathommod:crack_step"), 0.5, AttributeModifier.Operation.ADD_VALUE));
            entity.getAttribute(Attributes.MOVEMENT_SPEED).addOrReplacePermanentModifier(new AttributeModifier(ResourceLocation.parse("fathommod:crack"), 1, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
            entity.getAttribute(NeoForgeMod.SWIM_SPEED).addOrReplacePermanentModifier(new AttributeModifier(ResourceLocation.parse("fathommod:water_crack"), 2, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        } else if (DevUtils.hasTrinket(entity, Trinkets.CRACK_BUT_FOR_WATER)) {
            entity.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(ResourceLocation.parse("fathommod:crack"));
            entity.getAttribute(Attributes.STEP_HEIGHT).removeModifier(ResourceLocation.parse("fathommod:crack_step"));
            entity.getAttribute(NeoForgeMod.SWIM_SPEED).addOrReplacePermanentModifier(new AttributeModifier(ResourceLocation.parse("fathommod:water_crack"), 2, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        } else if (DevUtils.hasTrinket(entity, Trinkets.CRACK)) {
            entity.getAttribute(Attributes.STEP_HEIGHT).addOrUpdateTransientModifier(new AttributeModifier(ResourceLocation.parse("fathommod:crack_step"), 0.5, AttributeModifier.Operation.ADD_VALUE));
            entity.getAttribute(Attributes.MOVEMENT_SPEED).addOrReplacePermanentModifier(new AttributeModifier(ResourceLocation.parse("fathommod:crack"), 1, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
            entity.getAttribute(NeoForgeMod.SWIM_SPEED).removeModifier(ResourceLocation.parse("fathommod:water_crack"));
        } else {
            entity.getAttribute(NeoForgeMod.SWIM_SPEED).removeModifier(ResourceLocation.parse("fathommod:water_crack"));
            entity.getAttribute(Attributes.STEP_HEIGHT).removeModifier(ResourceLocation.parse("fathommod:crack_step"));
            entity.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(ResourceLocation.parse("fathommod:crack"));
        }
    }
}
