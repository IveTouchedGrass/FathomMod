package net.fathommod;

import net.fathommod.entity.ted.TedEntity;
import net.fathommod.init.*;
import net.fathommod.item.SweetSpotItem;
import net.fathommod.network.FathommodModVariables;
import net.fathommod.network.packets.AutoAttackConfirmCanAttackMessage;
import net.fathommod.network.packets.AutoAttackMessage;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Bogged;
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Unbreakable;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderNameTagEvent;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.event.entity.living.*;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.*;

@SuppressWarnings({"DataFlowIssue", "unused"})
@EventBusSubscriber(modid = FathommodMod.MOD_ID)
public class EventHandler {

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft instance = Minecraft.getInstance();
        HitResult hitResult = instance.hitResult;

        if (Config.isDevelopment && instance.player != null && instance.player.getMainHandItem().getItem() instanceof SweetSpotItem item  && hitResult != null) {
            if (hitResult instanceof EntityHitResult result) {
                instance.player.sendSystemMessage(Component.literal(String.valueOf(item.getSweetSpotRange().isInRange(Math.sqrt(instance.player.distanceToSqr(result.getEntity())), instance.player.getAttribute(Attributes.ENTITY_INTERACTION_RANGE).getValue()))));
            }
        }

//        boolean isJumpHeld = instance.options.keyJump.isDown();

//        if (instance.level != null && isJumpHeld && !instance.options.keyShift.isDown())
//            PacketDistributor.sendToServer(new FlyMessage.FlyKeyMessage(isJumpHeld, 0));
//        if (!isJumpHeld && instance.level != null && instance.options.keyShift.isDown())
//            PacketDistributor.sendToServer(new FlyShiftMessage.FlyShiftPacket(Minecraft.getInstance().options.keyShift.isDown()));

        instance.smartCull = true;
        instance.options.skipMultiplayerWarning = true;
//
//        if (instance.options.keyShift.isDown() && instance.options.keyJump.isDown() && instance.level != null)
//            PacketDistributor.sendToServer(new DoubleJumpMessage.DoubleJumpPacket(0f));
//
        String blockName = "";
        if (instance.level != null && hitResult instanceof BlockHitResult blockHitResult)
            blockName = String.valueOf(instance.level.getBlockState(blockHitResult.getBlockPos()).getBlock()).substring(6);

        if (instance.options.keyAttack.isDown() && instance.level != null && ((blockName.contains("air") && blockName.contains("minecraft:")) || hitResult instanceof EntityHitResult || hitResult.getType() == HitResult.Type.MISS) && (instance.player.getAttackStrengthScale(1f) >= 1 || instance.player.getAttribute(Attributes.ATTACK_SPEED).getValue() >= 20) && ClientVars.canAutoAttack) {
            PacketDistributor.sendToServer(new AutoAttackMessage.AutoAttackPacket(0));
        }

        if (ClientVars.clientTickAge % 20 == 0 && instance.level != null) {
            PacketDistributor.sendToServer(new AutoAttackConfirmCanAttackMessage.AutoAttackConfirmCanAttackPacket(false));
        }

        if (instance.player != null && ((instance.options.keyUp.isDown() && !instance.options.keyDown.isDown()) || (instance.options.keyDown.isDown() && !instance.options.keyUp.isDown()) || (instance.options.keyLeft.isDown() && !instance.options.keyRight.isDown()) || (!instance.options.keyLeft.isDown() && instance.options.keyRight.isDown()))) {
            if (ClientVars.movementHeldTimeTicks < 80)
                ClientVars.movementHeldTimeTicks++;
            ClientVars.pressedKeys.clear();
            if (instance.options.keyUp.isDown())
                ClientVars.pressedKeys.add(instance.options.keyUp);
            if (instance.options.keyDown.isDown())
                ClientVars.pressedKeys.add(instance.options.keyDown);
            if (instance.options.keyLeft.isDown())
                ClientVars.pressedKeys.add(instance.options.keyLeft);
            if (instance.options.keyRight.isDown())
                ClientVars.pressedKeys.add(instance.options.keyRight);
            instance.player.sendSystemMessage(Component.literal(ClientVars.pressedKeys + String.valueOf(ClientVars.pressedKeys)));
            for (KeyMapping mapping : ClientVars.pressedKeys) {
                if (!ClientVars.lastPressedKeys.contains(mapping)) {
                    ClientVars.movementHeldTimeTicks *= 0.75;
                    ClientVars.movementHeldTimeTicks = Math.round(ClientVars.movementHeldTimeTicks);
                }
            }
        } else {
            ClientVars.movementHeldTimeTicks = 0;
        }

        ClientVars.clientTickAge++;
        ClientVars.lastPressedKeys = new ArrayList<>(ClientVars.pressedKeys);
        if (instance.player != null && ClientVars.movementHeldTimeTicks > 0)
            instance.player.sendSystemMessage(Component.literal(String.valueOf(ClientVars.movementHeldTimeTicks)));
    }

    @SubscribeEvent
    public static void onEntitySetTarget(LivingChangeTargetEvent event) {
        if (!(event.getEntity().level() instanceof ServerLevel))
            return;
        if (event.getNewAboutToBeSetTarget() != null && event.getNewAboutToBeSetTarget().getData(FathommodModVariables.ENTITY_VARIABLES).isGodMode)
            event.setNewAboutToBeSetTarget(null);
        if (event.getNewAboutToBeSetTarget() != null && event.getEntity().getData(FathommodModVariables.ENTITY_VARIABLES).isSummon && Objects.equals(event.getEntity().getData(FathommodModVariables.ENTITY_VARIABLES).summonOwner, event.getNewAboutToBeSetTarget().getUUID().toString())) {
            event.setNewAboutToBeSetTarget(null);
        }
        LivingEntity entity = event.getEntity();
        if (event.getNewAboutToBeSetTarget() == null && event.getEntity().getData(FathommodModVariables.ENTITY_VARIABLES).isSummon) {
            for (LivingEntity entityiterator : event.getEntity().level().getEntitiesOfClass(Mob.class, new AABB(entity.getX() - 25, entity.getY() - 25, entity.getZ() - 25, entity.getX() + 25,  entity.getY() + 25 ,entity.getZ() + 25))) {
                if (entityiterator != ((ServerLevel) entity.level()).getEntity(UUID.fromString(entity.getData(FathommodModVariables.ENTITY_VARIABLES).summonOwner)) && (entityiterator instanceof Mob mob && mob.getTarget() == ((ServerLevel) entity.level()).getEntity(UUID.fromString(entity.getData(FathommodModVariables.ENTITY_VARIABLES).summonOwner))) && entity.canAttack(entityiterator)) {
                    event.setNewAboutToBeSetTarget(entityiterator);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingIncomingDamageEvent event) {
        if (event.getEntity().getData(FathommodModVariables.ENTITY_VARIABLES).isGodMode)
            event.setCanceled(true);
    }

    private static ArrayList<int[]> generateSpawnLocationsArray(double originX, double originZ) {
        int A = (int) originX;
        int B = (int) originZ;

        ArrayList<int[]> points = new ArrayList<>();

        for (int x = A - 50; x <= A + 50; x++) {
            for (int z = B - 50; z <= B + 50; z++) {
                if (Math.abs(x - A) > 25 || Math.abs(z - B) > 25) {
                    points.add(new int[]{x, z});
                }
            }
        }

        return points;
    }

    @SubscribeEvent
    public static void onPlayerRightClick(PlayerInteractEvent.RightClickItem event) {
        ItemStack itemstack = event.getItemStack();
        Player entity = event.getEntity();
        Level world = event.getLevel();
        if (itemstack.getItem() == Items.FIREWORK_ROCKET && itemstack.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.INFINITY)) > 0 && !entity.hasInfiniteMaterials()) {
            entity.addItem(itemstack);
        }
        if (itemstack.getItem() == FathommodModItems.TED_SPAWNER.get() && !world.isClientSide()) {
            ArrayList<int[]> list = generateSpawnLocationsArray(entity.getX(), entity.getZ());
            Collections.shuffle(list);
            for (int[] point : list) {
                ChunkAccess chunk = world.getChunk(point[0] >> 4, point[1] >> 4);
                if (chunk instanceof LevelChunk lvlChunk) {
                    if (!BuiltInRegistries.BLOCK.getKey(world.getBlockState(new BlockPos(point[0], lvlChunk.getHeight(Heightmap.Types.MOTION_BLOCKING, point[0], point[1]), point[1])).getBlock()).getPath().contains("air")) {
                        TedEntity ted = new TedEntity(FathommodModEntities.TED.get(), world);
                        ted.teleportTo(point[0], lvlChunk.getHeight(Heightmap.Types.MOTION_BLOCKING, point[0], point[1]) + 1, point[1]);
                        world.addFreshEntity(ted);
                        itemstack.shrink(1);
                        break;
                    }
                }
            }
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
        FathommodModVariables.EntityVariables vars = entity.getData(FathommodModVariables.ENTITY_VARIABLES);
        if (entity instanceof ServerPlayer) {
            if (vars.isGodMode)
                entity.getAbilities().invulnerable = true;
            vars.lastAutoAttack += vars.lastAutoAttack <= 100 ? 1 : 0;
            vars.syncPlayerVariables(entity);
        }
    }

    @SubscribeEvent
    public static void onEffectAdded(MobEffectEvent.Applicable event) {
        MobEffect effect = event.getEffectInstance().getEffect().value();
        if (event.getEntity() instanceof TedEntity)
            event.setResult(effect.isBeneficial() || effect.getClass().getName().contains("ComboHitEffect") ? MobEffectEvent.Applicable.Result.APPLY : MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
    }

    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Pre event) {
        if (event.getEntity() instanceof LivingEntity entity && entity.level() instanceof ServerLevel world) {
            FathommodModVariables.EntityVariables vars = entity.getData(FathommodModVariables.ENTITY_VARIABLES);
            if (vars.isSummon && (world.getEntity(UUID.fromString(vars.summonOwner)) != null || vars.summonTimeLeft-- <= 0)) {
                entity.discard();
            }
            try {
                if (vars.isSummon && (entity instanceof Mob mob) && mob.getTarget() == null) {
                    mob.getNavigation().moveTo(world.getEntity(UUID.fromString(entity.getData(FathommodModVariables.ENTITY_VARIABLES).summonOwner)), 0.75d);
                }
            } catch (NullPointerException ignored) {}
            vars.syncPlayerVariables(entity);

            if (entity instanceof Mob mob && mob.getData(FathommodModVariables.ENTITY_VARIABLES).isTedRabbit) {
                for (Entity entityiterator : event.getEntity().level().getEntities(entity, new AABB(entity.getX() - 50, entity.getY() - 50, entity.getZ() - 50, entity.getX() + 50, entity.getY() + 50,entity.getZ() + 50))) {
                    if (entityiterator instanceof Player player && mob.getTarget() == null && mob.canAttack(player)) {
                        mob.setTarget(player);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Post event) {
        ServerTempVars.serverTickAge++;
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Pre event) {
        Player entity = event.getEntity();

        if (entity.level().isClientSide())
            return;

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


        if (ServerTempVars.serverTickAge % 20 == 0) { // normal trinkets only run every 20 ticks (optimization reasons)
            legRingHandler(entity);

            jumpHeightHandler(entity);

            balloonHandler(entity);

            lifesGambleAttributeHandler(entity);

            bootsHandler(entity);

            ringOfLifeHealing(entity);

            chainedHandleCode(entity);

            handleExtensionCode(entity);

            digFasterHandler(entity);

            flightAttributeHandler(entity);

            armorPolishHandler(entity);

            unbreakableHandler(entity);

            rangeHandler(entity);

//            notBoxingGloveHandler(entity);
        }

        if (ServerTempVars.serverTickAge % 4 == 0) // conditional trinkets run every 4 ticks
            seasGiftHandler(entity);
    }

    @SubscribeEvent
    public static void onUseItem(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        if (player.level().isClientSide())
            return;
        if (player.getItemBySlot(EquipmentSlot.MAINHAND).getItem() == FathommodModItems.KILLERS_PAW.get()) {
            player.getCooldowns().addCooldown(FathommodModItems.KILLERS_PAW.get(), 900);
            for (int i = 0; i < 3; i++) {
                Rabbit rabbit = new Rabbit(EntityType.RABBIT, player.level());
                String command = switch (i) {
                    case 0 -> "^ ^ ^1";
                    case 1 -> "^-1 ^ ^";
                    case 2 -> "^1 ^ ^";
                    default -> "^ ^ ^";
                };

                rabbit.setVariant(Rabbit.Variant.EVIL);
                rabbit.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(new AttributeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "killer_rabbit_ted_health"), 3, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
                rabbit.getAttribute(Attributes.MOVEMENT_SPEED).addPermanentModifier(new AttributeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "killer_rabbit_ted_movement_speed"), 1.5, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
                rabbit.getAttribute(Attributes.ATTACK_DAMAGE).addPermanentModifier(new AttributeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "killer_rabbit_ted_damage_boost"), 3, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
                rabbit.getAttribute(Attributes.FOLLOW_RANGE).addPermanentModifier(new AttributeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "killer_rabbit_ted_follow_range"), 69, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
                rabbit.setHealth(rabbit.getMaxHealth());
                for (Entity entity : player.level().getEntities(player, new AABB(player.getX() - 12.5, player.getY() - 12.5, player.getZ() - 12.5, player.getX() + 12.5, player.getY() + 12.5, player.getZ() + 12.5)).stream().sorted(Comparator
                        .comparingDouble(entityToSort -> entityToSort.distanceToSqr(player.getX(), player.getY(), player.getZ()))).toList()) {
                    if (entity instanceof LivingEntity && !(entity instanceof Player) && (entity instanceof Monster || (entity instanceof Mob mob && mob.getTarget() == player))) {
                        rabbit.setTarget((LivingEntity) entity);
                        break;
                    }
                }
                rabbit.teleportTo(player.getX(), player.getY(), player.getZ());
                player.level().addFreshEntity(rabbit);
                FathommodModVariables.EntityVariables vars = rabbit.getData(FathommodModVariables.ENTITY_VARIABLES);
                vars.summonOwner = player.getUUID().toString();
                vars.isSummon = true;
                vars.summonTimeLeft = 600;
                vars.syncPlayerVariables(rabbit);
                DevUtils.executeCommandAs(rabbit, "tp @s " + command);
            }
        }
    }

    private static void rangeHandler(LivingEntity entity) {
        if (entity instanceof Player player) {
            if (player.getItemBySlot(EquipmentSlot.MAINHAND).getItem() == FathommodModItems.BOXING_GLOVES.get()) {
                player.getAttribute(Attributes.ENTITY_INTERACTION_RANGE).addOrReplacePermanentModifier(new AttributeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "boxing_gloves_modifier"), -1.5, AttributeModifier.Operation.ADD_VALUE));
                player.getAttribute(Attributes.ENTITY_INTERACTION_RANGE).removeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "basic_spear_modifier"));
            } else if (player.getItemBySlot(EquipmentSlot.MAINHAND).getItem() == FathommodModItems.BASIC_SPEAR.get()) {
                player.getAttribute(Attributes.ENTITY_INTERACTION_RANGE).removeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "boxing_gloves_modifier"));
                player.getAttribute(Attributes.ENTITY_INTERACTION_RANGE).addOrReplacePermanentModifier(new AttributeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "basic_spear_modifier"), 3.5, AttributeModifier.Operation.ADD_VALUE));
            } else {
                player.getAttribute(Attributes.ENTITY_INTERACTION_RANGE).removeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "boxing_gloves_modifier"));
                player.getAttribute(Attributes.ENTITY_INTERACTION_RANGE).removeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "basic_spear_modifier"));
            }
        }
    }

//    private static void notBoxingGloveHandler(LivingEntity entity) {
//        if (DevUtils.hasTrinket(entity, Trinkets.AUTO_ATTACK))
//            entity.getAttribute(Attributes.ATTACK_SPEED).addOrUpdateTransientModifier(new AttributeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "not_boxing_glove_modifier"), 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
//        else
//            entity.getAttribute(Attributes.ATTACK_SPEED).removeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "not_boxing_glove_modifier"));
//    }

    private static void unbreakableHandler(LivingEntity entity) {
        unbreakableToggler((DevUtils.hasTrinket(entity, Trinkets.UNBREAKABILITY)), entity);
    }

    @SubscribeEvent
    private static void sweetSpotHandler(LivingIncomingDamageEvent event) {
        if (event.getSource().getEntity() instanceof LivingEntity livingEntity && livingEntity.getMainHandItem().getItem() instanceof SweetSpotItem item) {
            if (item.getSweetSpotRange().isInRange(Math.sqrt(livingEntity.distanceToSqr(event.getEntity())), livingEntity.getAttribute(Attributes.ENTITY_INTERACTION_RANGE).getValue())) {
                event.setAmount(event.getAmount() * item.getSweetSpotRange().damageMultiplier);
            }
        }
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
        if (event.getEntity().level().isClientSide())
            return;
        if (event.getSource().getEntity() instanceof ServerPlayer player && player.getItemBySlot(EquipmentSlot.MAINHAND).getItem() == FathommodModItems.METAL_BAT.get()) {
            player.level().playSound(null, player.blockPosition(), FathommodModSounds.BONK.get(), SoundSource.PLAYERS, 2.75f, 1f);
        }
    }

    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        if (event.getEntity().level().isClientSide())
            return;
        double seed = Math.random();
        LivingEntity entity = event.getEntity();
        Level world = entity.level();
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();

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
        } else if (entity instanceof Zombie) {
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
        } else if (entity instanceof TedEntity) {
            if (world instanceof ServerLevel level) {
                ItemEntity entityToSpawn = new ItemEntity(level, x, y, z, new ItemStack(FathommodModItems.TED_CLAWS));
                ItemEntity entityToSpawn2 = new ItemEntity(level, x, y, z, new ItemStack(FathommodModItems.TED_CLAWS));
                entityToSpawn.setPickUpDelay(15);
                level.addFreshEntity(entityToSpawn);
            }
        }
    }

    @SubscribeEvent
    public static void onEntityHurt(LivingIncomingDamageEvent event) {
        if (event.getEntity().level().isClientSide())
            return;
        LivingEntity entity = event.getEntity();
        DamageSource source = event.getSource();
        Entity sourceentity = source.getEntity();

        if (sourceentity instanceof BossEntity && entity.invulnerableTime > 0) {
            entity.invulnerableTime = 0;
            entity.hurt(source, event.getOriginalAmount());
        }

        if (sourceentity instanceof Rabbit rabbit && rabbit.getData(FathommodModVariables.ENTITY_VARIABLES).isTedRabbit) {
            event.setCanceled(true);
            entity.hurt(new DamageSource(entity.level().holderOrThrow(FathommodModDamageTypes.SKILL_ISSUE)), event.getAmount());
        }
    }

    @SubscribeEvent
    public static void onEntityAttacked(LivingDamageEvent.Pre event) {
        if (event.getEntity().level().isClientSide())
            return;
        Entity sourceentity = event.getSource().getEntity();
        LivingEntity entity = event.getEntity();
        DamageSource source = event.getSource();

        if (sourceentity instanceof TedEntity ted) {
            ted.currentAttack = TedEntity.Attacks.PURSUIT;
            ted.setRockTimer(160);
        }

        if (arrowSharpenerHandler(source) && sourceentity != null) {
            event.setNewDamage(event.getNewDamage() * 1.2f);
        }
        if (source.getEntity() instanceof LivingEntity && DevUtils.hasTrinket((LivingEntity) source.getEntity(), Trinkets.RING_OF_POWER))
            event.setNewDamage(event.getNewDamage() + 2);
        if (DevUtils.hasTrinket(entity, Trinkets.LIFES_GAMBLE))
            event.setNewDamage(event.getNewDamage() * 2);
        if (sourceentity instanceof LivingEntity livingEntity && livingEntity.getItemBySlot(EquipmentSlot.MAINHAND).getItem() == FathommodModItems.TED_CLAWS.get() && !source.is(FathommodModDamageTypes.TED_WEAPON_COMBO)) {
            FathommodModVariables.EntityVariables vars = entity.getData(FathommodModVariables.ENTITY_VARIABLES);
            vars.comboHitSource = livingEntity.getUUID().toString();
            vars.syncPlayerVariables(livingEntity);
            entity.addEffect(new MobEffectInstance(FathommodModMobEffects.COMBO_HIT, 10, Math.round(event.getNewDamage() * 5), false, false));
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
        if (event.getEntity().level().isClientSide())
            return;
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
    }

    private static void balloonHandler(LivingEntity entity) {
        if (DevUtils.hasTrinket(entity, Trinkets.BALLOON)) {
            entity.getAttribute(Attributes.JUMP_STRENGTH).addOrUpdateTransientModifier(new AttributeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "balloon_jump_modifier"), 0.25, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
            entity.getAttribute(Attributes.FALL_DAMAGE_MULTIPLIER).addOrUpdateTransientModifier(new AttributeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "balloon_fall_dmg_modifier"), -999, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        } else {
            entity.getAttribute(Attributes.JUMP_STRENGTH).removeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "balloon_jump_modifier"));
            entity.getAttribute(Attributes.FALL_DAMAGE_MULTIPLIER).removeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "balloon_fall_dmg_modifier"));
        }
    }

    private static void lifesGambleAttributeHandler(LivingEntity entity) {
        if (DevUtils.hasTrinket(entity, Trinkets.LIFES_GAMBLE)) {
                entity.getAttribute(Attributes.MAX_HEALTH).addOrUpdateTransientModifier(new AttributeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "life_gamble_modifier"), 20, AttributeModifier.Operation.ADD_VALUE));
        } else {
            entity.getAttribute(Attributes.MAX_HEALTH).removeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "life_gamble_modifier"));
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
        if (DevUtils.hasTrinket(entity, Trinkets.CHAINED_HANDLE) && entity.getItemBySlot(EquipmentSlot.MAINHAND).getItem() != FathommodModItems.BOXING_GLOVES.get()) {
            entity.getAttribute(Attributes.ENTITY_INTERACTION_RANGE).addOrUpdateTransientModifier(new AttributeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "chain_handle_entity_reach_modifier"), 3, AttributeModifier.Operation.ADD_VALUE));
            entity.getAttribute(Attributes.ATTACK_DAMAGE).addOrUpdateTransientModifier(new AttributeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "chain_handle_damage_modifier"), -0.3, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
            entity.getAttribute(Attributes.BLOCK_INTERACTION_RANGE).addOrUpdateTransientModifier(new AttributeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "chain_handle_entity_reach_modifier"), 3, AttributeModifier.Operation.ADD_VALUE));
        } else {
            entity.getAttribute(Attributes.ENTITY_INTERACTION_RANGE).removeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "chain_handle_entity_reach_modifier"));
            entity.getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "chain_handle_damage_modifier"));
            entity.getAttribute(Attributes.BLOCK_INTERACTION_RANGE).removeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "chain_handle_block_reach_modifier"));
        }
    }

    private static void handleExtensionCode(LivingEntity entity) {
        if (DevUtils.hasTrinket(entity, Trinkets.HANDLE_EXTENSION)) {
            entity.getAttribute(Attributes.ENTITY_INTERACTION_RANGE).addOrUpdateTransientModifier(new AttributeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "handle_extension_entity_modifier"), 1, AttributeModifier.Operation.ADD_VALUE));
            entity.getAttribute(Attributes.BLOCK_INTERACTION_RANGE).addOrUpdateTransientModifier(new AttributeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "handle_extension_block_modifier"), 1, AttributeModifier.Operation.ADD_VALUE));
        } else {
            entity.getAttribute(Attributes.ENTITY_INTERACTION_RANGE).removeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "handle_extension_entity_modifier"));
            entity.getAttribute(Attributes.BLOCK_INTERACTION_RANGE).removeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "handle_extension_block_modifier"));
        }
    }

    private static void flightAttributeHandler(LivingEntity entity) {
        if (entity.getItemBySlot(EquipmentSlot.HEAD).getItem() == Trinkets.WINGS) {
            if (DevUtils.hasTrinket(entity, Trinkets.DOUBLE_DOUBLE_JUMP)) {
                entity.getItemBySlot(EquipmentSlot.HEAD).set(DataComponents.MAX_DAMAGE, 169);
            } else if (DevUtils.hasTrinket(entity, Trinkets.JUMP_HEIGHT)) {
                entity.getItemBySlot(EquipmentSlot.HEAD).set(DataComponents.MAX_DAMAGE, 100);
            } else {
                entity.getItemBySlot(EquipmentSlot.HEAD).set(DataComponents.MAX_DAMAGE, 77);
            }
            entity.getAttribute(Attributes.FALL_DAMAGE_MULTIPLIER).addOrReplacePermanentModifier(new AttributeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "wings_fall_dmg_modifier"), -999, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
            entity.getAttribute(Attributes.GRAVITY).addOrReplacePermanentModifier(new AttributeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "wings_gravity_modifier"), -0.15, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        } else {
            entity.getAttribute(Attributes.FALL_DAMAGE_MULTIPLIER).removeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "wings_fall_dmg_modifier"));
            entity.getAttribute(Attributes.GRAVITY).removeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "wings_gravity_modifier"));
        }

        if (entity.onGround() && entity.getItemBySlot(EquipmentSlot.HEAD).getItem() == Trinkets.WINGS) {
            entity.getItemBySlot(EquipmentSlot.HEAD).set(DataComponents.DAMAGE, 0);
        }
    }

    private static void jumpHeightHandler(LivingEntity entity) {
        if (DevUtils.hasTrinket(entity, Trinkets.DOUBLE_DOUBLE_JUMP)) {
            Objects.requireNonNull(entity.getAttribute(Attributes.JUMP_STRENGTH)).addOrUpdateTransientModifier(new AttributeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "jump_trinkets_jump_power_modifier"), 0.9, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
            entity.getAttribute(Attributes.SAFE_FALL_DISTANCE).addOrUpdateTransientModifier(new AttributeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "jump_trinkets_safe_fall_dist_mod"), 3, AttributeModifier.Operation.ADD_VALUE));
        } else if (DevUtils.hasTrinket(entity, Trinkets.JUMP_HEIGHT)) {
            Objects.requireNonNull(entity.getAttribute(Attributes.JUMP_STRENGTH)).addOrUpdateTransientModifier(new AttributeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "jump_trinkets_jump_power_modifier"), 0.489, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
            entity.getAttribute(Attributes.SAFE_FALL_DISTANCE).addOrUpdateTransientModifier(new AttributeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "jump_trinkets_safe_fall_dist_mod"), 2, AttributeModifier.Operation.ADD_VALUE));
        } else {
            entity.getAttribute(Attributes.SAFE_FALL_DISTANCE).removeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "jump_trinkets_safe_fall_dist_mod"));
            entity.getAttribute(Attributes.JUMP_STRENGTH).removeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "jump_trinkets_jump_power_modifier"));
        }
    }

    private static void digFasterHandler(LivingEntity entity) {
        if (DevUtils.hasTrinket(entity, Trinkets.DIG_FASTER)) {
            entity.getAttribute(Attributes.BLOCK_BREAK_SPEED).addOrReplacePermanentModifier(new AttributeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "dig_faster_trinket_modifier"), 0.25, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        } else {
            entity.getAttribute(Attributes.BLOCK_BREAK_SPEED).removeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "dig_faster_trinket_modifier"));
        }
    }

    private static void armorPolishHandler(LivingEntity entity) {
        if (DevUtils.hasTrinket(entity, Trinkets.ARMOR_POLISH)) {
            entity.getAttribute(Attributes.ARMOR_TOUGHNESS).addOrReplacePermanentModifier(new AttributeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "armor_polish_trinket_modifier"), 0.5, AttributeModifier.Operation.ADD_VALUE));
        } else {
            entity.getAttribute(Attributes.ARMOR_TOUGHNESS).removeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "armor_polish_trinket_modifier"));
        }
    }

    private static void legRingHandler(LivingEntity entity) {
        if (DevUtils.hasTrinket(entity, Trinkets.LEG_RING)) {
            entity.getAttribute(Attributes.MOVEMENT_SPEED).addOrReplacePermanentModifier(new AttributeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "leg_ring_trinket_modifier"), 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        } else {
            entity.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "leg_ring_trinket_modifier"));
        }
    }

    private static void seasGiftHandler(LivingEntity entity) {
        if (DevUtils.hasTrinket(entity, Trinkets.SEAS_GIFT) && entity.isUnderWater()) {
            entity.getAttribute(Attributes.ARMOR).addOrReplacePermanentModifier(new AttributeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "seas_gift_trinket_modifier"), 2, AttributeModifier.Operation.ADD_VALUE));
        } else {
            entity.getAttribute(Attributes.ARMOR).removeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "seas_gift_trinket_modifier"));
        }
    }
    
    public static boolean hasWings(LivingEntity entity) {
        return DevUtils.hasTrinket(entity, Items.GOLDEN_HELMET);
    }

    private static void bootsHandler(LivingEntity entity) {
        if (DevUtils.hasTrinket(entity, Trinkets.CRACK_ON_CRACK)) {
            entity.getAttribute(Attributes.STEP_HEIGHT).addOrUpdateTransientModifier(new AttributeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "crack_step"), 0.5, AttributeModifier.Operation.ADD_VALUE));
            entity.getAttribute(Attributes.MOVEMENT_SPEED).addOrReplacePermanentModifier(new AttributeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "crack"), 2, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
            entity.getAttribute(NeoForgeMod.SWIM_SPEED).addOrReplacePermanentModifier(new AttributeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "water_crack"), 4, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        } else if (DevUtils.hasTrinket(entity, Trinkets.CRACK_BUT_FOR_WATER) && DevUtils.hasTrinket(entity, Trinkets.CRACK)) {
            entity.getAttribute(Attributes.STEP_HEIGHT).addOrUpdateTransientModifier(new AttributeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "crack_step"), 0.5, AttributeModifier.Operation.ADD_VALUE));
            entity.getAttribute(Attributes.MOVEMENT_SPEED).addOrReplacePermanentModifier(new AttributeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "crack"), 1, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
            entity.getAttribute(NeoForgeMod.SWIM_SPEED).addOrReplacePermanentModifier(new AttributeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "water_crack"), 2, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        } else if (DevUtils.hasTrinket(entity, Trinkets.CRACK_BUT_FOR_WATER)) {
            entity.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "crack"));
            entity.getAttribute(Attributes.STEP_HEIGHT).removeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "crack_step"));
            entity.getAttribute(NeoForgeMod.SWIM_SPEED).addOrReplacePermanentModifier(new AttributeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "water_crack"), 2, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        } else if (DevUtils.hasTrinket(entity, Trinkets.CRACK)) {
            entity.getAttribute(Attributes.STEP_HEIGHT).addOrUpdateTransientModifier(new AttributeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "crack_step"), 0.5, AttributeModifier.Operation.ADD_VALUE));
            entity.getAttribute(Attributes.MOVEMENT_SPEED).addOrReplacePermanentModifier(new AttributeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "crack"), 1, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
            entity.getAttribute(NeoForgeMod.SWIM_SPEED).removeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "water_crack"));
        } else {
            entity.getAttribute(NeoForgeMod.SWIM_SPEED).removeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "water_crack"));
            entity.getAttribute(Attributes.STEP_HEIGHT).removeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "crack_step"));
            entity.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(ResourceLocation.fromNamespaceAndPath(FathommodMod.MOD_ID, "crack"));
        }
    }
}