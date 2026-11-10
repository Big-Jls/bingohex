/*
 * Copyright (c) 2026 Big_Jls and DeepSeek-v4.1-Flash-1017.
 * Licensed under the Apache License, Version 2.0.
 * See LICENSE and NOTICE.
 */
package com.castbingo.hexbingo;

import com.mojang.datafixers.util.Pair;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.noeppi_noeppi.mods.bongo.Bingo;
import io.github.noeppi_noeppi.mods.bongo.data.Team;
import io.github.noeppi_noeppi.mods.bongo.data.WinCondition;
import io.github.noeppi_noeppi.mods.bongo.data.task.GameTasks;
import io.github.noeppi_noeppi.mods.bongo.event.BingoTaskEvent;
import io.github.noeppi_noeppi.mods.bongo.task.Task;
import io.github.noeppi_noeppi.mods.bongo.task.TaskType;
import io.github.noeppi_noeppi.mods.bongo.task.TaskTypeAdvancement;
import io.github.noeppi_noeppi.mods.bongo.task.TaskTypeItem;
import io.github.noeppi_noeppi.mods.bongo.util.TagWithCount;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.StructureTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.ProblemReporter;
import net.minecraft.util.Unit;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.Container;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.clock.ServerClockManager;
import net.minecraft.world.clock.WorldClock;
import net.minecraft.world.clock.WorldClocks;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.bee.Bee;
import net.minecraft.world.entity.animal.feline.Cat;
import net.minecraft.world.entity.animal.feline.CatVariant;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.animal.parrot.Parrot;
import net.minecraft.world.entity.animal.parrot.Parrot.Variant;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.animal.wolf.WolfVariant;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.skeleton.Skeleton;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.npc.villager.VillagerData;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.entity.projectile.EyeOfEnder;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.throwableitemprojectile.Snowball;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.FurnaceMenu;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import net.neoforged.neoforge.event.CommandEvent;
import net.neoforged.neoforge.event.ItemStackedOnOtherEvent;
import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent.Post;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent.Pre;
import net.neoforged.neoforge.event.entity.player.PlayerEvent.ItemCraftedEvent;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
import net.neoforged.neoforge.items.IItemHandlerModifiable;

/**
 * 海克斯效果实现集合。
 * <p>hexes.json 的 effects[].type 对应这里的处理逻辑：</p>
 * <ul>
 *   <li>即时效果：applyImmediate 中的 switch case；</li>
 *   <li>被动/特殊效果（无 case，由 tick 或事件驱动）：bone_meal_spray（每 10 秒催熟）、
 *   dice_roll（定时骰子）、magnet（吸附掉落物）、spider_man（爬墙）、random_teleport（/bingo rtp）、
 *   sheep_wool（羊额外掉羊毛）、homeguard_speed（家园卫士移速）、craft_refund（大法师合成返还）、
 *   no_crafting_table（回归基本功禁用工作台）、kill_random_buff（飞升仪式击杀随机 buff）、
 *   kill_stack_crate（叠角龙击杀叠加）、drop_multiplier（玻璃大炮/回归基本功掉落倍率）。</li>
 * </ul>
 * <p>通用类型 effect / attribute / item 的子键分别由 effect() / attribute() / item() 解析。</p>
 */
public final class HexEffects {
   private static final ThreadLocal<Boolean> THORNS = ThreadLocal.withInitial(() -> Boolean.FALSE);
   private static final Map<UUID, HexEffects.Temp> TEMP_DAMAGE = new HashMap<>();
   private static final Map<UUID, HexEffects.Temp> TEMP_LIFESTEAL = new HashMap<>();
   private static final Map<UUID, Long> RTP_COOLDOWN = new HashMap<>();
   private static final Map<UUID, Long> HOOK_COOLDOWN = new HashMap<>();
   private static final long HOOK_COOLDOWN_MS = 5000L;
   private static final Map<UUID, Long> DASH_COOLDOWN = new HashMap<>();
   private static final long DASH_COOLDOWN_MS = 15000L;
   private static final long DASH_DURATION_MS = 8000L;
   private static final Map<UUID, HexEffects.DashState> DASHING = new HashMap<>();
   private static final Map<UUID, HexEffects.PortableFurnace> PORTABLE_FURNACES = new HashMap<>();
   private static final Map<UUID, List<BlockPos>> BOOSTED_FURNACES = new HashMap<>();
   private static final Map<UUID, HexEffects.SoulState> SOULS = new HashMap<>();
   private static final Set<UUID> VOYAGE_ACTIVE = new HashSet<>();
   private static final Map<UUID, Long> JAM_COOLDOWN = new HashMap<>();
   private static final Map<DyeColor, Long> BP_JAM = new HashMap<>();
   private static final Map<UUID, Long> BP_JAM_PLAYER = new HashMap<>();
   private static final Map<UUID, Long> SHIELD_READY = new HashMap<>();
   private static final long SHIELD_COOLDOWN_MS = 60000L;
   private static final Map<UUID, HexEffects.BanishState> BANISHED = new HashMap<>();
   private static final Map<UUID, Long> BANISH_COOLDOWN = new HashMap<>();
   private static long DAY_NIGHT_UNTIL = 0L;
   private static final Map<Mob, Vec3> THE_WORLD_MOBS = new LinkedHashMap<>();
   private static long THE_WORLD_UNTIL = 0L;
   private static final Map<UUID, Long> THE_WORLD_COOLDOWN = new HashMap<>();
   private static final long THE_WORLD_COOLDOWN_MS = 180000L;
   private static final Map<UUID, ItemStack> PUMPKIN_HEADS = new HashMap<>();
   private static final ThreadLocal<Boolean> PUMPKIN_GUARD = ThreadLocal.withInitial(() -> Boolean.FALSE);
   private static final Map<UUID, Long> EYE_MARK = new HashMap<>();
   private static final List<Item> FLOWERS = List.of(
      Items.DANDELION,
      Items.POPPY,
      Items.BLUE_ORCHID,
      Items.ALLIUM,
      Items.AZURE_BLUET,
      Items.RED_TULIP,
      Items.ORANGE_TULIP,
      Items.WHITE_TULIP,
      Items.PINK_TULIP,
      Items.OXEYE_DAISY,
      Items.CORNFLOWER,
      Items.LILY_OF_THE_VALLEY,
      Items.SUNFLOWER,
      Items.LILAC,
      Items.ROSE_BUSH,
      Items.PEONY,
      Items.TORCHFLOWER,
      Items.PITCHER_PLANT
   );
   private static final Map<UUID, Holder<MobEffect>> SONG_BUFFS = new HashMap<>();
   private static final Set<UUID> SPY_USED = new HashSet<>();
   private static final Set<String> BOUND_ITEM_NAMES = Set.of("转到我眩晕了", "ok回力镖", "彩虹大作战", "玻璃大炮锁定槽", "中国人会飞·鞘翅", "中国人会飞·烟花", "和我一起困在这里", "精怪魔法", "冲冲冲", "随身工作台", "便携熔炉", "The World", "维度放逐", "对方不在服务区", "海神", "你摸不到", "混沌智");
   private static final Identifier WILDCARD_TEXTURE = Identifier.fromNamespaceAndPath("hexbingo", "textures/gui/wildcard.png");
   private static final String BLACK_MARKET_USED_TAG = "hexbingo_black_market_used";
   private static final Map<UUID, Long> DICE_NEXT = new HashMap<>();
   private static final long DICE_INTERVAL_MS = 300000L;
   private static final Set<UUID> LOUIS_PENDING = new HashSet<>();
   private static final Set<UUID> MIRROR_USED = new HashSet<>();
   private static final Set<UUID> BLANK_USED = new HashSet<>();
   private static final Map<UUID, Long> GOLD_RUSH = new HashMap<>();
   private static final Set<UUID> HEX_BEES = new HashSet<>();
   private static final Map<UUID, UUID> BEE_TARGETS = new HashMap<>();
   private static final Map<UUID, Integer> PET_TIMER = new HashMap<>();
   private static final Map<UUID, Long> UNTOUCHABLE_COOLDOWN = new HashMap<>();
   private static final long UNTOUCHABLE_COOLDOWN_MS = 300000L;
   private static final Map<UUID, Long> CHAOS_MIND_COOLDOWN = new HashMap<>();
   private static final long CHAOS_MIND_COOLDOWN_MS = 480000L;
   private static final Set<UUID> SPIDER_CLIMB_OFF = new HashSet<>();
   private static final Set<Item> VALUABLE_PET_ITEMS = Set.of(
      Items.DIAMOND,
      Items.DIAMOND_BLOCK,
      Items.NETHERITE_INGOT,
      Items.NETHERITE_BLOCK,
      Items.NETHERITE_SCRAP,
      Items.ANCIENT_DEBRIS,
      Items.EMERALD,
      Items.EMERALD_BLOCK,
      Items.GOLD_BLOCK,
      Items.GOLD_INGOT,
      Items.GOLDEN_APPLE,
      Items.ENCHANTED_GOLDEN_APPLE,
      Items.ELYTRA,
      Items.TOTEM_OF_UNDYING,
      Items.NETHER_STAR,
      Items.WITHER_SKELETON_SKULL,
      Items.DRAGON_EGG,
      Items.ENCHANTED_BOOK,
      Items.SHULKER_SHELL,
      Items.ECHO_SHARD,
      Items.HEART_OF_THE_SEA,
      Items.TRIDENT,
      Items.END_CRYSTAL,
      Items.BEACON,
      Items.CONDUIT
   );
   private static final Set<Item> PET_ITEM_BLACKLIST = Set.of(
      Items.COMMAND_BLOCK,
      Items.CHAIN_COMMAND_BLOCK,
      Items.REPEATING_COMMAND_BLOCK,
      Items.COMMAND_BLOCK_MINECART,
      Items.STRUCTURE_BLOCK,
      Items.STRUCTURE_VOID,
      Items.JIGSAW,
      Items.BARRIER,
      Items.LIGHT,
      Items.DEBUG_STICK,
      Items.BEDROCK,
      Items.SPAWNER,
      Items.TRIAL_SPAWNER,
      Items.VAULT,
      Items.END_PORTAL_FRAME,
      Items.KNOWLEDGE_BOOK,
      Items.INFESTED_STONE,
      Items.INFESTED_COBBLESTONE,
      Items.INFESTED_STONE_BRICKS,
      Items.INFESTED_MOSSY_STONE_BRICKS,
      Items.INFESTED_CRACKED_STONE_BRICKS,
      Items.INFESTED_CHISELED_STONE_BRICKS,
      Items.INFESTED_DEEPSLATE
   );
   private static List<Item> PET_ITEM_POOL;
   private static int[] PET_ITEM_WEIGHTS;
   private static int PET_ITEM_TOTAL_WEIGHT;
   private static final Map<UUID, Integer> VOYAGE_LOCK_MASK = new HashMap<>();
   private static final Set<UUID> NETHER_SENT = new HashSet<>();
   private static final Set<UUID> NETHER_ESCAPED = new HashSet<>();
   private static final TagKey<Structure> ANCIENT_CITY = TagKey.create(
      Registries.STRUCTURE, Identifier.fromNamespaceAndPath("hexbingo", "ancient_city")
   );
   private static final TagKey<Structure> PILLAGER_OUTPOST = TagKey.create(
      Registries.STRUCTURE, Identifier.fromNamespaceAndPath("hexbingo", "pillager_outpost")
   );
   private static final TagKey<Structure> DESERT_PYRAMID = TagKey.create(
      Registries.STRUCTURE, Identifier.fromNamespaceAndPath("hexbingo", "desert_pyramid")
   );
   private static final TagKey<Structure> JUNGLE_PYRAMID = TagKey.create(
      Registries.STRUCTURE, Identifier.fromNamespaceAndPath("hexbingo", "jungle_pyramid")
   );
   private static final TagKey<Structure> TRIAL_CHAMBERS = TagKey.create(
      Registries.STRUCTURE, Identifier.fromNamespaceAndPath("hexbingo", "trial_chambers")
   );
   private static final TagKey<Structure> BASTION_REMNANT = TagKey.create(
      Registries.STRUCTURE, Identifier.fromNamespaceAndPath("hexbingo", "bastion_remnant")
   );
   private static final TagKey<Structure> NETHER_FORTRESS = TagKey.create(
      Registries.STRUCTURE, Identifier.fromNamespaceAndPath("hexbingo", "fortress")
   );
   private static final TagKey<Structure> NETHER_RUINED_PORTAL = TagKey.create(
      Registries.STRUCTURE, Identifier.fromNamespaceAndPath("hexbingo", "nether_ruined_portal")
   );
   private static final Map<UUID, HexEffects.HomeguardState> HOMEGUARD = new HashMap<>();
   private static final Map<UUID, Long> BREAK_LOCK = new HashMap<>();
   private static final Map<UUID, Integer> KILL_STACKS = new HashMap<>();
   // 陨石：正在坠落的陨石
   private static final List<HexEffects.MeteorState> METEORS = new ArrayList<>();
   // 陨石引发的地震：逐格崩塌
   private static final List<HexEffects.QuakeState> QUAKES = new ArrayList<>();
   // 预支胜利：临时点亮的格子
   private static final List<HexEffects.AdvanceState> ADVANCES = new ArrayList<>();
   // 驯龙高手：环绕玩家的幼龙
   private static final Map<UUID, EnderDragon> DRAGON_PETS = new HashMap<>();
   private static final Map<UUID, Double> DRAGON_ANGLES = new HashMap<>();
   private static final Map<UUID, Integer> DRAGON_SOUND_TIMER = new HashMap<>();
   private static final List<HexEffects.EggIncubation> INCUBATIONS = new ArrayList<>();
   private static final long DRAGON_INCUBATION_MS = 180000L;
   // TextDisplay 的 setText 在 26.2 中是 private，只能用反射调用
   private static Method TEXT_DISPLAY_SET_TEXT;
   private static final SoundEvent BABY_DRAGON_SOUND = SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath("hexbingo", "baby_dragon"));
   // 致盲
   private static final Map<UUID, Long> BLIND_COOLDOWN = new HashMap<>();
   // 结束了？：被放逐到末地外岛
   private static final Map<UUID, HexEffects.ExileState> END_EXILE = new HashMap<>();
   // 点石成金
   private static final Map<UUID, Long> MIDAS_COOLDOWN = new HashMap<>();
   // 蝙蝠形态
   private static final Map<UUID, Long> BAT_UNTIL = new HashMap<>();
   private static final Map<UUID, Long> BAT_COOLDOWN = new HashMap<>();
   private static final Map<UUID, Float> BAT_FLYING_SPEED = new HashMap<>();
   private static final Map<UUID, Bat> BAT_DISGUISES = new HashMap<>();
   // 精神控制
   private static final Map<UUID, HexEffects.MindState> MIND_CONTROL = new HashMap<>();
   private static final Map<UUID, Long> MIND_COOLDOWN = new HashMap<>();
   // 目标封锁
   private static HexEffects.DenyState OBJECTIVE_DENY;
   private static final Map<UUID, Long> DENY_COOLDOWN = new HashMap<>();
   // 透视
   private static final Map<UUID, Long> XRAY_UNTIL = new HashMap<>();
   private static final Map<UUID, Long> XRAY_COOLDOWN = new HashMap<>();
   private static final Map<UUID, List<ItemEntity>> XRAY_MARKERS = new HashMap<>();
   private static final String XRAY_TAG = "hexbingo_xray_marker";
   // 任务护盾：队伍 -> 受保护的卡牌
   private static final Map<String, Set<Task>> TASK_SHIELDS = new HashMap<>();
   private static final Map<UUID, HexEffects.TransformState> TRANSFORMS = new HashMap<>();
   private static final Map<UUID, TamableAnimal> PETS = new HashMap<>();
   // 被 /kill 移除的宠物不再自动补发，直到下一局重新获得该海克斯
   private static final Set<UUID> PET_DISMISSED = new HashSet<>();
   private static final Map<UUID, BlockPos> BEACONS = new HashMap<>();
   private static final Map<UUID, HexEffects.BoomerangState> BOOMERANGS = new HashMap<>();
   private static final Map<UUID, Long> FAE_UNTIL = new HashMap<>();
   private static final Map<UUID, Mob> FAE_DISGUISES = new HashMap<>();
   private static final Map<UUID, Long> FAE_COOLDOWN = new HashMap<>();
   private static final long FAE_DURATION_MS = 60000L;
   private static final long FAE_COOLDOWN_MS = 300000L;
   private static HexEffects.TrapRoom TRAP;
   private static final Map<UUID, Long> TRAP_COOLDOWN = new HashMap<>();
   private static final int TRAP_HALF = 3;
   private static final int TRAP_DURATION_TICKS = 200;
   private static final long TRAP_COOLDOWN_MS = 180000L;
   private static final ThreadLocal<Integer> GRANT_DEPTH = ThreadLocal.withInitial(() -> 0);
   private static final List<Item> RANDOM_ITEMS = List.of(
      Items.IRON_INGOT,
      Items.GOLD_INGOT,
      Items.COPPER_INGOT,
      Items.DIAMOND,
      Items.EMERALD,
      Items.REDSTONE,
      Items.LAPIS_LAZULI,
      Items.COAL,
      Items.GOLDEN_APPLE,
      Items.ENDER_PEARL,
      Items.ARROW,
      Items.COOKED_BEEF,
      Items.BREAD,
      Items.OAK_PLANKS,
      Items.STONE,
      Items.TORCH,
      Items.IRON_PICKAXE,
      Items.IRON_SWORD,
      Items.SHIELD,
      Items.WATER_BUCKET,
      Items.EXPERIENCE_BOTTLE,
      Items.SNOWBALL,
      Items.OAK_LOG,
      Items.STRING,
      Items.LEATHER,
      Items.BOOK,
      Items.SUGAR,
      Items.GUNPOWDER
   );
   private static final List<Item> WOODS = List.of(
      Items.OAK_LOG,
      Items.SPRUCE_LOG,
      Items.BIRCH_LOG,
      Items.JUNGLE_LOG,
      Items.ACACIA_LOG,
      Items.DARK_OAK_LOG,
      Items.MANGROVE_LOG,
      Items.CHERRY_LOG,
      Items.CRIMSON_STEM,
      Items.WARPED_STEM
   );
   private static final Holder<Attribute>[] ALL_ATTRIBUTES = new Holder[]{
      Attributes.MAX_HEALTH,
      Attributes.ARMOR,
      Attributes.ARMOR_TOUGHNESS,
      Attributes.ATTACK_DAMAGE,
      Attributes.ATTACK_SPEED,
      Attributes.MOVEMENT_SPEED,
      Attributes.KNOCKBACK_RESISTANCE,
      Attributes.LUCK,
      Attributes.BLOCK_BREAK_SPEED,
      Attributes.BLOCK_INTERACTION_RANGE,
      Attributes.ENTITY_INTERACTION_RANGE,
      Attributes.SCALE
   };
   private static final List<EntityType<? extends Mob>> FAE_TYPES = List.of(EntityTypes.WOLF, EntityTypes.CAT, EntityTypes.FOX, EntityTypes.RABBIT);
   private static final Holder<MobEffect>[] ALL_EFFECTS = new Holder[]{
      MobEffects.SPEED,
      MobEffects.HASTE,
      MobEffects.STRENGTH,
      MobEffects.RESISTANCE,
      MobEffects.REGENERATION,
      MobEffects.ABSORPTION,
      MobEffects.NIGHT_VISION,
      MobEffects.WATER_BREATHING,
      MobEffects.FIRE_RESISTANCE,
      MobEffects.JUMP_BOOST,
      MobEffects.SLOW_FALLING,
      MobEffects.DOLPHINS_GRACE,
      MobEffects.HUNGER,
      MobEffects.HERO_OF_THE_VILLAGE
   };
   private static final Holder<MobEffect>[] FEAST_BUFFS = new Holder[]{
      MobEffects.SPEED,
      MobEffects.HASTE,
      MobEffects.STRENGTH,
      MobEffects.RESISTANCE,
      MobEffects.REGENERATION,
      MobEffects.ABSORPTION,
      MobEffects.JUMP_BOOST,
      MobEffects.FIRE_RESISTANCE,
      MobEffects.WATER_BREATHING,
      MobEffects.NIGHT_VISION,
      MobEffects.SLOW_FALLING,
      MobEffects.DOLPHINS_GRACE
   };

   public static void apply(ServerPlayer var0, Hex var1) {
      List var2 = var1.effects();

      for (int var3 = 0; var3 < var2.size(); var3++) {
         applyImmediate(var0, var1.id(), var3, (HexEffect)var2.get(var3));
      }
   }

   private static void applyImmediate(ServerPlayer var0, String var1, int var2, HexEffect var3) {
      switch (var3.type) {
         // 更万用瞄准镜、更更万用瞄准镜、更更更万用瞄准镜、歌莉娅巨人
         case "attribute":
            applyAttribute(var0, var1, var2, var3);
            break;
         // 练腿日、大力、饕餮盛宴、回归基本功、练手日、村庄英雄、跳跳虎
         case "effect":
            applyPermanentEffect(var0, var3);
            break;
         // 珍珠储备、灵魂炸弹
         case "item":
            Item var6 = item(var3.key);
            if (var6 != null && var3.count > 0) {
               var0.addItem(new ItemStack(var6, var3.count));
            }
            break;
         // 旧版：团队传送次数（当前数据未使用）
         case "teleports":
            Team var7 = team(var0);
            if (var7 != null) {
               var7.teleportsLeft(var7.teleportsLeft() + (int)var3.amount);
            }
            break;
         // 旧版：吸收与复活保护（当前数据未使用）
         case "absorption_respawn":
            applyAbsorption(var0, var3.amount, 6000);
            break;
         // 旧版：给队伍背包发物品（当前数据未使用）
         case "backpack_item":
            Team var8 = team(var0);
            Item var9 = item(var3.key);
            if (var8 != null && var9 != null && var3.count > 0) {
               giveTeamItem(var8, var0, var9, var3.count);
            }
            break;
         // 旧版：全队属性加成（当前数据未使用）
         case "team_hex_buff":
            Team var10 = team(var0);
            if (var10 != null) {
               for (ServerPlayer var18 : onlineTeamPlayers(var0.level().getServer(), var10)) {
                  Holder var19 = attribute(var3.key);
                  if (var19 != null) {
                     AttributeInstance var20 = var18.getAttribute(var19);
                     if (var20 != null) {
                        var20.addOrReplacePermanentModifier(new AttributeModifier(modifierId(var1, var2), var3.amount, operation(var3.operation)));
                     }
                  }
               }
            }
            break;
         // 旧版：额外选海克斯次数（当前数据未使用）
         case "extra_choice":
            HexGameData.get(var0.level().getServer()).setExtraChoice(var0.getUUID());
            break;
         // 旧版：海克斯品质提升（当前数据未使用）
         case "quality_up":
            HexGameData.get(var0.level().getServer()).addQualityUp(var0.getUUID(), Math.max(1, (int)var3.amount));
            break;
         // 质变黄金阶、质变棱彩阶
         case "grant_random_hex":
            grantRandomHex(var0, var3.key.isEmpty() ? "gold" : var3.key, "transformed");
            break;
         // 我想要、我很想要、我真的很想要
         case "random_items":
            giveRandomItems(var0, Math.max(1, var3.count));
            break;
         // 经验专精
         case "xp_lapis":
            var0.giveExperiencePoints(Math.max(1, (int)var3.amount));
            int var11 = Math.max(1, var3.min) + ThreadLocalRandom.current().nextInt(Math.max(1, Math.max(var3.min, var3.max) - Math.max(1, var3.min) + 1));
            var0.addItem(new ItemStack(Items.LAPIS_LAZULI, var11));
            var0.sendSystemMessage(
               Component.translatable("hexbingo.msg.xp_lapis", new Object[]{Component.literal(Integer.toString(var11))}).withStyle(ChatFormatting.AQUA)
            );
            break;
         // 枯木生花
         case "random_wood":
            giveRandomWoodAndFlowers(var0);
            break;
         // 换位思考
         case "swap_tasks":
            swapTasks(var0);
            break;
         // 迅捷斥候
         case "mushroom_scout":
            growMushrooms(var0);
            break;
         // 虫洞
         case "wormhole":
            wormhole(var0);
            break;
         // 飞天猫猫神教
         case "summon_cats":
            summonCats(var0);
            break;
         // 汪汪队
         case "summon_wolves":
            summonWolves(var0);
            break;
         // 对方不在服务区
         case "jam_bp":
            giveJamItem(var0);
            break;
         // 淘金热
         case "sand_gold":
            var0.sendSystemMessage(Component.translatable("hexbingo.msg.sand_gold").withStyle(ChatFormatting.GOLD));
            break;
         // 滴滴打眼
         case "eye_taxi":
            var0.sendSystemMessage(Component.translatable("hexbingo.msg.eye_taxi").withStyle(ChatFormatting.AQUA));
            break;
         // 步步生花
         case "flowers_walk":
            var0.sendSystemMessage(Component.translatable("hexbingo.msg.flowers_walk").withStyle(ChatFormatting.LIGHT_PURPLE));
            break;
         // 锻造大师
         case "forge_master":
            replaceTools(var0);
            break;
         // 条条大路通罗马
         case "nearest_village":
            nearestVillage(var0);
            break;
         // 旅行青蛙
         case "travel_frog":
            summonTravelFrog(var0);
            break;
         // 健忘症
         case "amnesia":
            amnesia(var0);
            break;
         // 冰！
         case "ice_boots":
            giveIceBoots(var0);
            break;
         // 雪傀儡互助会
         case "snow_pumpkin":
            givePumpkins(var0);
            break;
         // 宝可梦大师
         case "random_spawn_eggs":
            giveSpawnEggs(var0);
            break;
         // 乌鸦嘴
         case "raven_mouth":
            ravenMouth(var0);
            break;
         // 和朋友一起困在这里
         case "friend_aura":
            var0.sendSystemMessage(Component.translatable("hexbingo.msg.friend_aura").withStyle(ChatFormatting.LIGHT_PURPLE));
            break;
         // 共同富裕
         case "common_wealth":
            commonWealth(var0);
            break;
         // 质变混沌
         case "chaos_shift":
            chaosShift(var0);
            break;
         // 地狱无门闯进来
         case "nether_start":
            startNetherStart(var0);
            break;
         // 星际航行
         case "star_voyage":
            startVoyage(var0);
            break;
         // 小蜜蜂
         case "summon_bees":
            summonBees(var0, Math.max(1, var3.count));
            break;
         // 随便
         case "random_bingo":
            randomBingo(var0);
            break;
         // 致敬爱情（沉船只在主世界）
         case "nearest_shipwreck": {
            MinecraftServer var17 = var0.level().getServer();
            if (var17 != null) {
               teleportToStructure(
                  var0,
                  var17.overworld(),
                  StructureTags.SHIPWRECK,
                  "hexbingo.msg.love_tribute",
                  "hexbingo.msg.love_tribute_none",
                  "minecraft:shipwreck"
               );
            }
            break;
         }
         // 地狱开局（地狱找废弃地狱门，其它维度回主世界找）
         case "nearest_ruined_portal": {
            MinecraftServer var17 = var0.level().getServer();
            if (var17 != null) {
               ServerLevel var18 = var0.level().dimension() == Level.NETHER ? var17.getLevel(Level.NETHER) : var17.overworld();
               teleportToStructure(
                  var0,
                  var18 == null ? var17.overworld() : var18,
                  StructureTags.RUINED_PORTAL,
                  "hexbingo.msg.ruined_portal",
                  "hexbingo.msg.ruined_portal_none",
                  "minecraft:ruined_portal"
               );
            }
            break;
         }
         // 凝视深渊：传送到最近的 deep_dark 群系
         case "gaze_abyss":
            gazeAbyss(var0);
            break;
         // 谁才是掠夺者：传送到最近的掠夺者前哨站
         case "pillager_outpost":
            pillagerOutpost(var0);
            break;
         // 故乡的樱花开了：传送到最近的樱花群系
         case "cherry_grove":
            cherryGrove(var0);
            break;
         // 一二三木头人：给予最近苍白花园群系的地图
         case "pale_garden_map":
            givePaleGardenMap(var0);
            break;
         // 尖端发明家：技能型海克斯冷却缩短 30%
         case "cutting_edge":
            var0.sendSystemMessage(Component.translatable("hexbingo.msg.cutting_edge").withStyle(ChatFormatting.AQUA));
            break;
         // 古代遗迹
         case "ancient_ruins":
            ancientRuins(var0);
            break;
         // 静水监狱
         case "lock_hexes":
            MinecraftServer var12 = var0.level().getServer();
            if (var12 != null) {
               HexGameData var13 = HexGameData.get(var12);
               var13.lockAllHexes();
               HashSet<UUID> var14 = new HashSet<>();
               var14.add(var0.getUUID());

               for (Team var16 : Bingo.get(var12.overworld()).getTeams()) {
                  var14.addAll(var16.getPlayers());
               }

               for (ServerPlayer var24 : var12.getPlayerList().getPlayers()) {
                  var14.add(var24.getUUID());
               }

               for (UUID var23 : var14) {
                  var13.lockHexes(var23);
                  var13.clearPending(var23);
                  var13.clearOffer(var23);
               }

               for (ServerPlayer var24 : var12.getPlayerList().getPlayers()) {
                  var24.sendSystemMessage(Component.translatable("hexbingo.msg.hex_locked").withStyle(ChatFormatting.DARK_GRAY));
               }
            }
            break;
         // 潘多拉的盒子
         case "reroll_hexes":
            rerollHexes(var0);
            break;
         // 孤勇者（已禁用：音频移除）
         // case "play_song_buff":
         //    playSongBuff(var0);
         //    break;
         // 得吃小曲
         case "dechi_song":
            playDechiSong(var0, var3.seconds);
            break;
         // 理想国
         case "utopia":
            utopia(var0);
            break;
         // 你摸不到：改为右键技能物品
         case "block_break_block":
            giveUntouchableItem(var0);
            break;
         // 超强大脑
         case "inventory_transform":
            startInventoryTransform(var0, var3.seconds);
            break;
         // 尖端发明家
         case "random_enchant":
            enchantGear(var0);
            var0.sendSystemMessage(Component.translatable("hexbingo.msg.inventor").withStyle(ChatFormatting.AQUA));
            break;
         // 仆从大师
         case "pet_companion":
            summonPet(var0);
            break;
         // 尊我为王
         case "beacon_target":
            placeBeacon(var0);
            break;
         // 掀桌子！、掀桌子？
         case "reroll_tasks":
            rerollTasks(var0, var3.mode);
            break;
         // ok回力镖
         case "boomerang_biome":
            giveBoomerangItem(var0);
            break;
         // 和我一起困在这里
         case "trap_room":
            giveTrapItem(var0);
            break;
         // 精怪魔法
         case "fae_magic":
            giveFaeItem(var0);
            break;
         // 间谍
         case "spy_swap":
            var0.sendSystemMessage(Component.translatable("hexbingo.msg.spy_usage").withStyle(ChatFormatting.LIGHT_PURPLE));
            break;
         // 转到我眩晕了
         case "grapple_hook":
            giveHookItem(var0);
            break;
         // 冲冲冲
         case "dash_charge":
            giveDashItem(var0);
            break;
         // 随身工作台
         case "portable_bench":
            givePortableBench(var0);
            break;
         // 灵魂出窍
         case "soul_out":
            startSoulOut(var0);
            break;
         // CV工程师
         case "task_copy":
            copyOpponentTask(var0);
            break;
         // 中国人会飞
         case "elytra_flight":
            giveFlightKit(var0);
            break;
         // 旧版：末地传送门定位（当前数据未使用）
         case "end_portal_locator":
            locateEndPortal(var0);
            break;
         // 旧版：直接完成随机任务（当前数据未使用）
         case "task_complete":
            completeRandomTasks(var0, (int)var3.amount);
            break;
         // 玻璃大炮任务版生命上限（当前数据未使用）
         case "glass_cannon_task":
            applyNegativeAttribute(var0, var1, var2, Attributes.MAX_HEALTH, -var3.percent, Operation.ADD_MULTIPLIED_BASE);
            break;
         // 无甲任务版护甲降低（当前数据未使用）
         case "no_armor_task_xp":
            applyNegativeAttribute(var0, var1, var2, Attributes.ARMOR, -var3.amount, Operation.ADD_VALUE);
            break;
         // 种子礼包
         case "seed_gift":
            giveSeedGift(var0);
            break;
         // 考古刷
         case "archaeology_brush":
            giveBrush(var0);
            break;
         // 免疫护盾
         case "immunity_shield":
            var0.sendSystemMessage(Component.translatable("hexbingo.msg.immunity_shield").withStyle(ChatFormatting.AQUA));
            break;
         // 九九八十一难
         case "trial_chambers":
            teleportTrialChambers(var0);
            break;
         // 昼夜颠倒
         case "day_night_flip":
            flipDayNight(var0);
            break;
         // 混沌智：改为右键技能物品
         case "inventory_shuffle":
            giveChaosMindItem(var0);
            break;
         // 维度放逐
         case "nether_banish":
            giveBanishItem(var0);
            break;
         // 万能牌
         case "wildcard":
            wildcard(var0);
            break;
         // 预支胜利
         case "advance_victory":
            startAdvanceVictory(var0, Math.max(1, var3.seconds));
            break;
         // 偷天换日
         case "cell_swap":
            cellSwap(var0);
            break;
         // 地心游记
         case "journey_core":
            journeyCore(var0);
            break;
         // 苦行者
         case "ascetic":
            ascetic(var0, Math.max(1, var3.count));
            break;
         // 命运骰子
         case "destiny_dice":
            destinyDice(var0);
            break;
         // 随机大迁徙
         case "great_migration":
            greatMigration(var0, var3.radius > 0.0 ? var3.radius : 500.0);
            break;
         // 驯龙高手
         case "dragon_rider":
            giveDragonEgg(var0);
            break;
         // 空白格
         case "blank_cell":
            giveBlankCellItem(var0);
            break;
         // The World！
         case "the_world":
            giveWorldItem(var0);
            break;
         // 腐蚀背包
         case "corrupt_inventory":
            corruptInventories(var0);
            break;
         // 黑市开张
         case "black_market":
            grantBlackMarket(var0);
            break;
         // 恶魔契约
         case "demon_pact":
            applyDemonPact(var0, var1, var2);
            break;
         // 终极刷新
         case "ultimate_refresh":
            grantUltimateRefresh(var0);
            break;
         // 便携熔炉
         case "portable_furnace":
            givePortableFurnace(var0);
            break;
         // 工具箱
         case "toolbox":
            giveToolbox(var0);
            break;
         // 路易十六
         case "louis_xvi":
            LOUIS_PENDING.add(var0.getUUID());
            var0.sendSystemMessage(Component.translatable("hexbingo.msg.louis_xvi_ready").withStyle(ChatFormatting.GOLD));
            break;
         // 快递到了
         case "airdrop":
            airdrop(var0);
            break;
         // 海神
         case "sea_god":
            giveSeaGod(var0);
            break;
         // 陨石
         case "meteor":
            castMeteor(var0, var3);
            break;
         // 致盲
         case "blind":
            giveBlindItem(var0);
            break;
         // 结束了？
         case "is_it_over":
            startEndExile(var0, Math.max(1, var3.seconds));
            break;
         // 自动熔炼
         case "auto_smelt":
            var0.sendSystemMessage(Component.translatable("hexbingo.msg.auto_smelt").withStyle(ChatFormatting.GOLD));
            break;
         // 任务护盾
         case "task_shield":
            applyTaskShield(var0, Math.max(1, var3.count));
            break;
         // 点石成金
         case "midas_touch":
            var0.sendSystemMessage(Component.translatable("hexbingo.msg.midas_info").withStyle(ChatFormatting.GOLD));
            break;
         // 蝙蝠形态
         case "bat_form":
            giveBatItem(var0);
            break;
         // 精神控制
         case "mind_control":
            giveMindItem(var0);
            break;
         // 目标封锁
         case "objective_deny":
            giveDenyItem(var0);
            break;
         // 透视
         case "xray":
            giveXrayItem(var0);
            break;
         // 拾荒者
         case "scavenger":
            var0.sendSystemMessage(Component.translatable("hexbingo.msg.scavenger").withStyle(ChatFormatting.GOLD));
            break;
         // 洗牌
         case "card_shuffle":
            cardShuffle(var0);
            break;
         // 镜像复制
         case "mirror_hex":
            giveMirrorItem(var0);
            break;
         // 图腾批发
         case "totem_bulk":
            giveTotems(var0);
      }
   }

   private static void applyAttribute(ServerPlayer var0, String var1, int var2, HexEffect var3) {
      Holder var4 = attribute(var3.key);
      if (var4 != null) {
         AttributeInstance var5 = var0.getAttribute(var4);
         if (var5 != null) {
            var5.addOrUpdateTransientModifier(new AttributeModifier(modifierId(var1, var2), var3.amount, operation(var3.operation)));
         }
      }
   }

   private static void applyNegativeAttribute(ServerPlayer var0, String var1, int var2, Holder<Attribute> var3, double var4, Operation var6) {
      AttributeInstance var7 = var0.getAttribute(var3);
      if (var7 != null) {
         var7.addOrUpdateTransientModifier(new AttributeModifier(modifierId(var1, var2), var4, var6));
      }
   }

   private static void applyPermanentEffect(ServerPlayer var0, HexEffect var1) {
      Holder var2 = effect(var1.key);
      if (var2 != null) {
         var0.addEffect(new MobEffectInstance(var2, -1, var1.amplifier, true, false, false));
      }
   }

   public static void feastFoodBuff(ServerPlayer var0) {
      if (hasHex(var0, "gluttonous_feast")) {
         Holder var1 = FEAST_BUFFS[ThreadLocalRandom.current().nextInt(FEAST_BUFFS.length)];
         var0.addEffect(new MobEffectInstance(var1, 300, 1, false, true, true));
         var0.sendOverlayMessage(
            Component.translatable("hexbingo.msg.feast_buff", new Object[]{((MobEffect)var1.value()).getDisplayName()}).withStyle(ChatFormatting.GOLD)
         );
      }
   }

   public static void checkEnchantedItemTasks(ServerPlayer var0) {
      Bingo var1 = Bingo.get(var0.level());
      if (var1.canCompleteTasks(var0)) {
         List<ItemStack> var2 = var1.getElementsOf(TaskTypeItem.INSTANCE).toList();
         if (!var2.isEmpty()) {
            NonNullList<ItemStack> var3 = var0.getInventory().getNonEquipmentItems();

            for (ItemStack var5 : var2) {
               int var6 = 0;

               for (ItemStack var7 : var3) {
                  if (!var7.isEmpty() && !isHexGranted(var7) && matchesItemIgnoringEnchantments(var7, var5)) {
                     var6 += var7.getCount();
                  }
               }

               if (var6 > 0) {
                  ItemStack var8 = var5.copy();
                  var8.setCount(var6);
                  var1.checkCompleted(TaskTypeItem.INSTANCE, var0, var8);
               }
            }
         }
      }
   }

   private static boolean matchesItemIgnoringEnchantments(ItemStack var0, ItemStack var1) {
      if (ItemStack.isSameItemSameComponents(var0, var1)) {
         return true;
      } else if (var1.has(DataComponents.ENCHANTMENTS) || var1.has(DataComponents.STORED_ENCHANTMENTS)) {
         return false;
      } else {
         ItemStack var2 = var0.copy();
         var2.remove(DataComponents.ENCHANTMENTS);
         var2.remove(DataComponents.STORED_ENCHANTMENTS);
         var2.remove(DataComponents.REPAIR_COST);
         return ItemStack.isSameItemSameComponents(var2, var1);
      }
   }

   public static void feastHunger(ServerPlayer var0) {
      if (hasHex(var0, "gluttonous_feast") && var0.tickCount % 10 == 0) {
         MinecraftServer var1 = var0.level().getServer();
         if (var1 != null) {
            Bingo var2 = Bingo.get(var1.overworld());
            if (var2.running() && var2.getTeam(var0) != null && var2.getSettings().game().invulnerable()) {
               FoodData var3 = var0.getFoodData();
               if (var3.getFoodLevel() >= 20) {
                  var3.setFoodLevel(19);
               }
            }
         }
      }
   }

   public static void reapply(ServerPlayer var0) {
      MinecraftServer var1 = var0.level().getServer();
      if (var1 != null) {
         HexGameData var2 = HexGameData.get(var1);
         if (var2.mayhem()) {
            for (String var4 : var2.hexes(var0.getUUID())) {
               Hex var5 = HexRegistry.byId(var4);
               if (var5 != null) {
                  List var6 = var5.effects();

                  for (int var7 = 0; var7 < var6.size(); var7++) {
                     HexEffect var8 = (HexEffect)var6.get(var7);
                     switch (var8.type) {
                        case "attribute":
                           applyAttribute(var0, var4, var7, var8);
                           break;
                        case "effect":
                           applyPermanentEffect(var0, var8);
                           break;
                        case "absorption_respawn":
                           applyAbsorption(var0, var8.amount, 6000);
                           break;
                        case "glass_cannon_task":
                           applyNegativeAttribute(var0, var4, var7, Attributes.MAX_HEALTH, -var8.percent, Operation.ADD_MULTIPLIED_BASE);
                           break;
                        case "no_armor_task_xp":
                           applyNegativeAttribute(var0, var4, var7, Attributes.ARMOR, -var8.amount, Operation.ADD_VALUE);
                           break;
                        // 孤勇者已禁用
                        // case "play_song_buff":
                        //    ensureSongBuff(var0);
                        //    break;
                        case "elytra_flight":
                           ensureFlightRocket(var0);
                           break;
                        case "demon_pact":
                           applyDemonPactAttribute(var0, var4, var7);
                     }
                  }
               }
            }
         }
      }
   }

   public static void clearAll(ServerPlayer var0) {
      restoreFae(var0);
      restorePumpkin(var0);
      endSoulOut(var0);
      FAE_COOLDOWN.remove(var0.getUUID());
      HOOK_COOLDOWN.remove(var0.getUUID());
      DASH_COOLDOWN.remove(var0.getUUID());
      DASHING.remove(var0.getUUID());
      HexEffects.PortableFurnace var8 = PORTABLE_FURNACES.remove(var0.getUUID());
      if (var8 != null) {
         MinecraftServer var9 = var0.level().getServer();
         if (var9 != null) {
            HexGameData.get(var9).setPortableFurnace(var0.getUUID(), var8.save());
         }
      }
      BOOSTED_FURNACES.remove(var0.getUUID());
      JAM_COOLDOWN.remove(var0.getUUID());
      EYE_MARK.remove(var0.getUUID());
      VOYAGE_ACTIVE.remove(var0.getUUID());
      clearLockedSlotMarkers(var0);
      HOMEGUARD.remove(var0.getUUID());
      SONG_BUFFS.remove(var0.getUUID());
      NETHER_SENT.remove(var0.getUUID());
      NETHER_ESCAPED.remove(var0.getUUID());
      SHIELD_READY.remove(var0.getUUID());
      BP_JAM_PLAYER.remove(var0.getUUID());
      var0.removeTag(BLACK_MARKET_USED_TAG);
      DICE_NEXT.remove(var0.getUUID());
      LOUIS_PENDING.remove(var0.getUUID());
      MIRROR_USED.remove(var0.getUUID());
      BLANK_USED.remove(var0.getUUID());
      restoreTransform(var0);
      TamableAnimal var13 = PETS.remove(var0.getUUID());
      if (var13 != null) {
         var13.discard();
      }

      PET_TIMER.remove(var0.getUUID());
      PET_DISMISSED.remove(var0.getUUID());
      EnderDragon var14 = DRAGON_PETS.remove(var0.getUUID());
      if (var14 != null) {
         var14.discard();
      }

      DRAGON_ANGLES.remove(var0.getUUID());
      DRAGON_SOUND_TIMER.remove(var0.getUUID());
      cancelIncubation(var0.getUUID(), false);
      endBatForm(var0, false);
      endEndExile(var0, false);
      clearXrayMarkers(var0);
      BLIND_COOLDOWN.remove(var0.getUUID());
      MIDAS_COOLDOWN.remove(var0.getUUID());
      MIND_COOLDOWN.remove(var0.getUUID());
      DENY_COOLDOWN.remove(var0.getUUID());
      XRAY_UNTIL.remove(var0.getUUID());
      XRAY_COOLDOWN.remove(var0.getUUID());
      MIND_CONTROL.remove(var0.getUUID());
      MIND_CONTROL.entrySet().removeIf(var1 -> var1.getValue().target.equals(var0.getUUID()));

      for (Holder var4 : ALL_ATTRIBUTES) {
         AttributeInstance var5 = var0.getAttribute(var4);
         if (var5 != null) {
            for (AttributeModifier var7 : new ArrayList<AttributeModifier>(var5.getModifiers())) {
               if (var7.id().getNamespace().equals("hexbingo")) {
                  var5.removeModifier(var7.id());
               }
            }
         }
      }

      for (Holder var11 : ALL_EFFECTS) {
         if (var11 != MobEffects.NIGHT_VISION) {
            MobEffectInstance var12 = var0.getEffect(var11);
            if (var12 != null && var12.isInfiniteDuration()) {
               var0.removeEffect(var11);
            }
         }
      }
   }

   public static void ensureNightVision(MinecraftServer var0) {
      for (ServerPlayer var2 : var0.getPlayerList().getPlayers()) {
         MobEffectInstance var3 = var2.getEffect(MobEffects.NIGHT_VISION);
         if (var3 == null || !var3.isInfiniteDuration() || var3.isAmbient()) {
            var2.removeEffect(MobEffects.NIGHT_VISION);
            var2.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, -1, 0, false, false, false));
         }
      }
   }

   public static void tick(MinecraftServer var0) {
      long var1 = System.currentTimeMillis();
      TEMP_DAMAGE.entrySet().removeIf(var2 -> var2.getValue().until() < var1);
      TEMP_LIFESTEAL.entrySet().removeIf(var2 -> var2.getValue().until() < var1);
      tickTheWorld(var0);
      tickHexBees(var0);
      tickEndExile(var0);
      if (DAY_NIGHT_UNTIL > 0L && var1 >= DAY_NIGHT_UNTIL) {
         DAY_NIGHT_UNTIL = 0L;
         Registry<WorldClock> var21 = var0.registryAccess().lookupOrThrow(Registries.WORLD_CLOCK);
         var0.clockManager().setPaused(var21.getOrThrow(WorldClocks.OVERWORLD), false);
      }

      Iterator<Map.Entry<UUID, HexEffects.BanishState>> var22 = BANISHED.entrySet().iterator();

      while (var22.hasNext()) {
         Map.Entry<UUID, HexEffects.BanishState> var23 = var22.next();
         HexEffects.BanishState var24 = var23.getValue();
         if (var1 >= var24.until) {
            ServerPlayer var25 = var0.getPlayerList().getPlayer(var23.getKey());
            if (var25 != null) {
               ServerLevel var26 = var0.getLevel(var24.dimension);
               if (var26 != null) {
                  var25.teleportTo(var26, var24.x, var24.y, var24.z, Set.of(), var24.yaw, var24.pitch, false);
                  var25.resetFallDistance();
                  var25.setRemainingFireTicks(0);
                  var25.sendSystemMessage(Component.translatable("hexbingo.msg.banish_return").withStyle(ChatFormatting.GREEN));
               }

               var22.remove();
            }
         }
      }

      Bingo var3 = Bingo.get(var0.overworld());

      for (ServerPlayer var5 : var0.getPlayerList().getPlayers()) {
         UUID var20 = var5.getUUID();
         if (NETHER_SENT.contains(var20) && !NETHER_ESCAPED.contains(var20) && var5.level().dimension() == Level.OVERWORLD) {
            NETHER_ESCAPED.add(var20);
            var5.sendSystemMessage(Component.translatable("hexbingo.msg.nether_return").withStyle(ChatFormatting.GREEN));
         }

         List<Hex> var6 = hexes(var5);
         tickXray(var5);
         boolean var7 = false;
         if (var6.isEmpty()) {
            clearLockedSlotMarkers(var5);
         } else {
            Team var8 = var3.getTeam(var5);
            int var9 = var8 == null ? 0 : var8.completion().count();

            for (Hex var11 : var6) {
               List var12 = var11.effects();

               for (int var13 = 0; var13 < var12.size(); var13++) {
                  HexEffect var14 = (HexEffect)var12.get(var13);
                  switch (var14.type) {
                     // 常量药水效果：每 tick 补发，防止被覆盖或移除
                     case "effect":
                        applyPermanentEffect(var5, var14);
                        break;
                     // 持续回血（每 5 秒）
                     case "regen":
                        if (var5.tickCount % 100 == 0) {
                           var5.heal((float)var14.amount);
                        }
                        break;
                     // 永不饥饿：饥饿度始终补满
                     case "no_hunger":
                        FoodData var17 = var5.getFoodData();
                        var17.setFoodLevel(20);
                        var17.setSaturation(20.0F);
                        break;
                     // 饕餮盛宴：锁饱食度到 6，靠进食触发回血
                     case "feast_buff":
                        FoodData var19 = var5.getFoodData();
                        if (var19.getFoodLevel() > 6 && !(var3.running() && var3.getTeam(var5) != null && var3.getSettings().game().invulnerable())) {
                           var19.setFoodLevel(6);
                           var19.setSaturation(0.0F);
                        }
                        break;
                     // 净化解：定期移除负面状态
                     case "cleanse":
                        if (var5.tickCount % 100 == 0) {
                           cleanse(var5);
                        }
                        break;
                     // 光环：给范围内自身与队友上药水
                     case "aura":
                        if (var5.tickCount % 40 == 0) {
                           aura(var5, var8, var14);
                        }
                        break;
                     // 敌方高亮：玩家与怪物发光
                     case "glow_enemies":
                     case "enemy_scout":
                        if (var5.tickCount % 40 == 0) {
                           glowEnemies(var5, var8, var14);
                        }
                        break;
                     // 随团队完成数成长的属性
                     case "scaling_attribute":
                        updateScalingAttribute(var5, var11.id(), var13, var14, var9);
                        break;
                     // 随附近队友数成长的属性
                     case "team_count_attr":
                        updateTeamCountAttribute(var5, var11.id(), var13, var14, var8);
                        break;
                     // 手持任务物品时提速
                     case "holding_item_speed":
                        updateHoldingItemSpeed(var5, var11.id(), var13, var14, var3, var8);
                        break;
                     // 定期补给雪球
                     case "snowball_supply":
                        int var18 = Math.max(1, var14.interval);
                        if (var5.tickCount % (var18 * 20) == 0) {
                           var5.addItem(new ItemStack(Items.SNOWBALL));
                        }
                        break;
                     // 超强大脑：倒计时结束还原背包
                     case "inventory_transform":
                        tickInventoryTransform(var5);
                        break;
                     // 仆从大师：维持宠物并定期补给
                     case "pet_companion":
                        tickPet(var5, var14);
                        break;
                     // 尊我为王：靠近信标发放奖励
                     case "beacon_target":
                        tickBeacon(var5);
                        break;
                     // 精怪魔法 / 未知类型：无每 tick 逻辑
                     case "fae_magic":
                     default:
                        break;
                     // 孤勇者已禁用（音频移除）
                     // case "play_song_buff":
                     //    ensureSongBuff(var5);
                     //    break;
                     // 中国人会飞：补充飞行烟花
                     case "elytra_flight":
                        ensureFlightRocket(var5);
                        break;
                     // 玻璃大炮：维持锁定槽标记
                     case "locked_slots":
                        var7 = true;
                        enforceLockedSlots(var5, var14);
                        break;
                     // 星际航行：落地结算
                     case "star_voyage":
                        tickVoyage(var5);
                        break;
                     // 和朋友一起：附近队友加成
                     case "friend_aura":
                        updateFriendAura(var5, var11.id(), var13, var14);
                        break;
                     // 步步生花：脚下生成花朵
                     case "flowers_walk":
                        tickFlowersWalk(var5);
                  }
               }
            }

            if (!var7) {
               clearLockedSlotMarkers(var5);
            }
         }
      }

      tickBoomerangs(var0);
   }

   private static void updateScalingAttribute(ServerPlayer var0, String var1, int var2, HexEffect var3, int var4) {
      Holder var5 = attribute(var3.key);
      if (var5 != null) {
         AttributeInstance var6 = var0.getAttribute(var5);
         if (var6 != null) {
            double var7 = Math.min(var3.cap <= 0.0 ? Double.MAX_VALUE : var3.cap, var3.perTask * var4);
            if (var7 <= 0.0) {
               var6.removeModifier(modifierId(var1, var2));
            } else {
               var6.addOrUpdateTransientModifier(new AttributeModifier(modifierId(var1, var2), var7, operation(var3.operation)));
            }
         }
      }
   }

   private static void updateTeamCountAttribute(ServerPlayer var0, String var1, int var2, HexEffect var3, Team var4) {
      Holder var5 = attribute(var3.key);
      if (var5 != null && var4 != null) {
         AttributeInstance var6 = var0.getAttribute(var5);
         if (var6 != null) {
            double var7 = var3.radius > 0.0 ? var3.radius : 16.0;
            double var9 = var7 * var7;
            int var11 = 0;
            if (var0.level() instanceof ServerLevel var12) {
               for (ServerPlayer var14 : var12.players()) {
                  if (var14 != var0 && var4.hasPlayer(var14) && var14.distanceToSqr(var0) <= var9) {
                     var11++;
                  }
               }
            }

            double var15 = Math.min(var3.cap <= 0.0 ? Double.MAX_VALUE : var3.cap, var3.amount * var11);
            if (var15 <= 0.0) {
               var6.removeModifier(modifierId(var1, var2));
            } else {
               var6.addOrUpdateTransientModifier(new AttributeModifier(modifierId(var1, var2), var15, operation(var3.operation)));
            }
         }
      }
   }

   private static void updateHoldingItemSpeed(ServerPlayer var0, String var1, int var2, HexEffect var3, Bingo var4, Team var5) {
      AttributeInstance var6 = var0.getAttribute(Attributes.MOVEMENT_SPEED);
      if (var6 != null && var5 != null) {
         boolean var7 = false;

         for (int var9 : incompleteTasks(var4, var5)) {
            Task var10 = var4.task(var9);
            if (var10.getType().id().equals("bingo.item")) {
               ItemStack var11 = itemTaskStack(var10);
               if (var11 != null && holdsItem(var0, var11.getItem())) {
                  var7 = true;
                  break;
               }
            }
         }

         if (var7) {
            var6.addOrUpdateTransientModifier(new AttributeModifier(modifierId(var1, var2), var3.percent, Operation.ADD_MULTIPLIED_BASE));
         } else {
            var6.removeModifier(modifierId(var1, var2));
         }
      }
   }

   private static boolean holdsItem(ServerPlayer var0, Item var1) {
      for (ItemStack var3 : var0.getInventory().getNonEquipmentItems()) {
         if (!var3.isEmpty() && var3.getItem() == var1) {
            return true;
         }
      }

      return false;
   }

   private static void cleanse(ServerPlayer var0) {
      Holder[] var1 = new Holder[]{
         MobEffects.SLOWNESS,
         MobEffects.MINING_FATIGUE,
         MobEffects.WEAKNESS,
         MobEffects.POISON,
         MobEffects.WITHER,
         MobEffects.BLINDNESS,
         MobEffects.NAUSEA,
         MobEffects.HUNGER,
         MobEffects.LEVITATION,
         MobEffects.DARKNESS,
         MobEffects.UNLUCK
      };

      for (Holder var5 : var1) {
         var0.removeEffect(var5);
      }
   }

   private static void aura(ServerPlayer var0, Team var1, HexEffect var2) {
      Holder var3 = effect(var2.key);
      if (var3 != null) {
         double var4 = var2.radius > 0.0 ? var2.radius : 12.0;
         double var6 = var4 * var4;
         if (var0.level() instanceof ServerLevel var8) {
            for (ServerPlayer var10 : var8.players()) {
               if (!(var10.distanceToSqr(var0) > var6) && (var10 == var0 || var1 != null && var1.hasPlayer(var10))) {
                  var10.addEffect(new MobEffectInstance(var3, 80, var2.amplifier, true, false, false));
               }
            }
         }
      }
   }

   private static void glowEnemies(ServerPlayer var0, Team var1, HexEffect var2) {
      double var3 = var2.radius > 0.0 ? var2.radius : 20.0;
      double var5 = var3 * var3;
      if (var0.level() instanceof ServerLevel var7) {
         for (ServerPlayer var9 : var7.players()) {
            if (var9 != var0 && !(var9.distanceToSqr(var0) > var5) && (var1 == null || !var1.hasPlayer(var9))) {
               var9.addEffect(new MobEffectInstance(MobEffects.GLOWING, 80, 0, false, false, false));
            }
         }

         for (Entity var13 : var7.getEntities(var0, var0.getBoundingBox().inflate(var3))) {
            if (var13 instanceof Monster var10 && var10.isAlive()) {
               var10.addEffect(new MobEffectInstance(MobEffects.GLOWING, 80, 0, false, false, false));
            }
         }
      }
   }

   public static void onTask(BingoTaskEvent var0) {
      ServerPlayer var1 = var0.getPlayer();
      MinecraftServer var2 = var1.level().getServer();
      if (var2 != null) {
         HexGameData var3 = HexGameData.get(var2);
         if (var3.mayhem()) {
            Bingo var4 = var0.getBingo();
            Team var5 = var4.getTeam(var1);
            if (var5 != null) {
               int var6 = var4.tasks().indexOf(var0.getTask());
               markAdvanceGenuine(var5, var6);
               int var7 = var5.completion().count();
               List<ServerPlayer> var8 = onlineTeamPlayers(var2, var5);
               ThreadLocalRandom var9 = ThreadLocalRandom.current();

               for (Hex var11 : hexes(var1)) {
                  for (HexEffect var13 : var11.effects()) {
                     switch (var13.type) {
                        // 连锁完成：概率自动完成相邻格子
                        case "task_chain":
                           double var16 = var13.chance + var13.perTask * var7;
                           if (var13.cap > 0.0) {
                              var16 = Math.min(var13.cap, var16);
                           }

                           if (var9.nextDouble() < var16) {
                              for (int var31 = 0; var31 < Math.max(1, (int)var13.amount); var31++) {
                                 int var35 = randomAdjacent(var4, var5, var6);
                                 if (var35 >= 0) {
                                    completeTask(var4, var5, var35);
                                 }
                              }
                           }
                           break;
                        // 每完成 N 格，锁定敌方若干格
                        case "task_lock_enemy":
                           if (var13.tasks > 0 && var7 % var13.tasks == 0) {
                              lockEnemyTasks(var4, var5, Math.max(1, (int)var13.amount));
                           }
                           break;
                        // 每完成 N 格，揭示己方若干隐藏格
                        case "task_reveal":
                           if (var13.tasks > 0 && var7 % var13.tasks == 0) {
                              revealTasks(var2, var4, var5, Math.max(1, (int)var13.amount));
                           }
                           break;
                        // 复制/返还任务物品
                        case "item_copy":
                        case "item_back":
                           if (var9.nextDouble() < (var13.chance <= 0.0 ? 1.0 : var13.chance)) {
                              ItemStack var30 = itemTaskStack(var0.getTask());
                              if (var30 != null) {
                                 var1.addItem(var30.copy());
                              }
                           }
                           break;
                        // 每完成 N 格发放补给
                        case "task_supply":
                           if (var13.tasks > 0 && var7 % var13.tasks == 0) {
                              grantReward(var2, var5, var1, var13);
                           }
                           break;
                        // 每完成 N 格增加团队传送次数
                        case "task_teleport":
                           if (var13.tasks > 0 && var7 % var13.tasks == 0) {
                              var5.teleportsLeft(var5.teleportsLeft() + Math.max(1, (int)var13.amount));
                           }
                           break;
                        // 完成指定类型任务时给经验
                        case "task_type_xp":
                           if (!var13.key.isEmpty() && var13.key.equals(var0.getTask().getType().id())) {
                              var1.giveExperiencePoints(Math.max(1, (int)var13.amount));
                           }
                           break;
                        // 完成任意任务时给经验
                        case "task_any_xp":
                           var1.giveExperiencePoints(Math.max(1, (int)var13.amount));
                           break;
                        // 完成后上短暂正面药水
                        case "task_power":
                           applyTimedEffect(var1, var13);
                           break;
                        // 完成后自身加速
                        case "task_speed_self":
                           applySpeed(var1, var13.seconds, var13.amplifier);
                           break;
                        // 完成后自身回血
                        case "task_heal_self":
                           var1.heal((float)var13.amount);
                           break;
                        // 完成后自身套盾
                        case "task_shield_self":
                           applyAbsorption(var1, var13.amount, 600);
                           break;
                        // 完成中心格额外奖励
                        case "center_bonus":
                           if (var6 == 12) {
                              grantReward(var2, var5, var1, var13);
                           }
                           break;
                        // 全队回血
                        case "team_heal":
                           for (ServerPlayer var34 : var8) {
                              var34.heal((float)var13.amount);
                           }
                           break;
                        // 全队套盾
                        case "team_shield":
                           for (ServerPlayer var33 : var8) {
                              applyAbsorption(var33, var13.amount, 600);
                           }
                           break;
                        // 全队加速
                        case "team_speed":
                           for (ServerPlayer var32 : var8) {
                              applySpeed(var32, var13.seconds, var13.amplifier);
                           }
                           break;
                        // 全队经验
                        case "team_xp":
                           for (ServerPlayer var19 : var8) {
                              var19.giveExperiencePoints(Math.max(1, (int)var13.amount));
                           }
                           break;
                        // 全队侦察敌人
                        case "team_scout":
                           scoutEnemies(var1, var5, var13);
                           break;
                        // 完成任务后短时间吸血
                        case "task_lifesteal":
                           TEMP_LIFESTEAL.put(var1.getUUID(), new HexEffects.Temp((float)var13.percent, System.currentTimeMillis() + var13.seconds * 1000L));
                           break;
                        // 完成任务后短时间增伤
                        case "task_damage_boost":
                           TEMP_DAMAGE.put(var1.getUUID(), new HexEffects.Temp((float)var13.percent, System.currentTimeMillis() + var13.seconds * 1000L));
                           break;
                        // 低血量时完成任务回血
                        case "low_health_task_heal":
                           if (var1.getHealth() < var1.getMaxHealth() * (var13.percent > 0.0 ? var13.percent : 0.4)) {
                              var1.heal((float)var13.amount);
                           }
                           break;
                        // 无护甲完成任务给经验补偿
                        case "no_armor_task_xp":
                           var1.giveExperiencePoints(Math.max(1, var13.count));
                           break;
                        // 玻璃大炮完成任务给短时增伤
                        case "glass_cannon_task":
                           TEMP_DAMAGE.put(var1.getUUID(), new HexEffects.Temp((float)var13.amount, System.currentTimeMillis() + var13.seconds * 1000L));
                     }
                  }
               }

               for (ServerPlayer var21 : var8) {
                  if (var21 != var1) {
                     for (Hex var23 : hexes(var21)) {
                        for (HexEffect var25 : var23.effects()) {
                           if (var25.type.equals("team_rally")) {
                              Holder var26 = effect(var25.key);
                              if (var26 != null) {
                                 var21.addEffect(new MobEffectInstance(var26, var25.seconds * 20, var25.amplifier, true, false, false));
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private static void applyTimedEffect(ServerPlayer var0, HexEffect var1) {
      Holder var2 = effect(var1.key);
      if (var2 != null && var1.seconds > 0) {
         var0.addEffect(new MobEffectInstance(var2, var1.seconds * 20, var1.amplifier, true, false, false));
      }
   }

   private static void applySpeed(ServerPlayer var0, int var1, int var2) {
      if (var1 <= 0) {
         var1 = 5;
      }

      var0.addEffect(new MobEffectInstance(MobEffects.SPEED, var1 * 20, var2, true, false, false));
   }

   private static void scoutEnemies(ServerPlayer var0, Team var1, HexEffect var2) {
      double var3 = var2.radius > 0.0 ? var2.radius : 24.0;
      double var5 = var3 * var3;
      if (var0.level() instanceof ServerLevel var7) {
         int var11 = var2.seconds > 0 ? var2.seconds : 8;

         for (ServerPlayer var10 : var7.players()) {
            if (var10 != var0 && !(var10.distanceToSqr(var0) > var5) && (var1 == null || !var1.hasPlayer(var10))) {
               var10.addEffect(new MobEffectInstance(MobEffects.GLOWING, var11 * 20, 0, false, false, false));
            }
         }
      }
   }

   private static void rerollTasks(ServerPlayer var0, String var1) {
      MinecraftServer var2 = var0.level().getServer();
      if (var2 != null) {
         Bingo var3 = Bingo.get(var2.overworld());
         if (var3.running()) {
            ArrayList<Task> var4 = new ArrayList<>(var3.tasks());
            int var5 = var4.size();
            if (var5 != 0) {
               ArrayList<Task> var6 = new ArrayList<>();

               for (GameTasks var8 : GameTasks.gameTasks().values()) {
                  var8.getBingoTasks().ifLeft(var6::addAll);
               }

               LinkedHashMap<String, Task> var16 = new LinkedHashMap<>();

               for (Task var9 : var6) {
                  if (var9 != null && var9 != Task.EMPTY) {
                     var16.putIfAbsent(var9.contentName(var2).getString(), var9);
                  }
               }

               for (Task var20 : var4) {
                  var16.remove(var20.contentName(var2).getString());
               }

               if (!var16.isEmpty()) {
                  ArrayList<Task> var19 = new ArrayList<>(var16.values());
                  ThreadLocalRandom var21 = ThreadLocalRandom.current();
                  boolean[] var10 = new boolean[var5];

                  for (Team var12 : var3.getTeams()) {
                     for (int var13 = 0; var13 < var5; var13++) {
                        if (var12.completed(var13)) {
                           var10[var13] = true;
                        }
                     }
                  }

                  ArrayList<Component> var22 = new ArrayList<>();
                  if ("all_incomplete".equals(var1)) {
                     for (int var24 = 0; var24 < var5; var24++) {
                        if (!var10[var24] && !var19.isEmpty()) {
                           Task var28 = (Task)var4.get(var24);
                           Task var30 = (Task)var19.remove(var21.nextInt(var19.size()));
                           var4.set(var24, var30);
                           var22.add(taskChangeMessage(var2, var28, var30));
                        }
                     }

                     if (var22.isEmpty()) {
                        return;
                     }

                     var3.setTasks(var4);
                     var3.markDirty(true);
                     var3.checkWin();
                     var0.sendSystemMessage(
                        Component.translatable("hexbingo.msg.flip_tasks", new Object[]{var22.size()}).withStyle(ChatFormatting.LIGHT_PURPLE)
                     );
                  } else {
                     ArrayList<Integer> var23 = new ArrayList<>();

                     for (int var26 = 0; var26 < var5; var26++) {
                        if (!var10[var26]) {
                           var23.add(var26);
                        }
                     }

                     if (var23.isEmpty()) {
                        return;
                     }

                     int var27 = (Integer)var23.get(var21.nextInt(var23.size()));
                     Task var14 = (Task)var4.get(var27);
                     Task var15 = (Task)var19.get(var21.nextInt(var19.size()));
                     var4.set(var27, var15);
                     var3.setTasks(var4);
                     var3.markDirty(true);
                     var3.checkWin();
                     var22.add(taskChangeMessage(var2, var14, var15));
                  }

                  for (Component var29 : var22) {
                     var0.sendSystemMessage(var29);
                  }
               }
            }
         }
      }
   }

   private static Component taskChangeMessage(MinecraftServer var0, Task var1, Task var2) {
      return Component.translatable("hexbingo.msg.flip_task_changed", new Object[]{var1.contentName(var0), var2.contentName(var0)})
         .withStyle(ChatFormatting.GRAY);
   }

   private static int completeRandomTasks(ServerPlayer var0, int var1) {
      MinecraftServer var2 = var0.level().getServer();
      int var7 = -1;
      if (var2 != null) {
         Bingo var3 = Bingo.get(var2.overworld());
         Team var4 = var3.getTeam(var0);
         if (var4 != null) {
            for (int var5 = 0; var5 < var1; var5++) {
               int var6 = randomIncomplete(var3, var4);
               if (var6 < 0) {
                  return var7;
               }

               completeTask(var3, var4, var6);
               var7 = var6;
            }
         }
      }

      return var7;
   }

   private static int randomAdjacent(Bingo var0, Team var1, int var2) {
      if (var2 >= 0 && var0.tasks().size() == 25) {
         ArrayList<Integer> var3 = new ArrayList<>(4);
         int var4 = var2 / 5;
         int var5 = var2 % 5;
         if (var4 > 0) {
            var3.add(var2 - 5);
         }

         if (var4 < 4) {
            var3.add(var2 + 5);
         }

         if (var5 > 0) {
            var3.add(var2 - 1);
         }

         if (var5 < 4) {
            var3.add(var2 + 1);
         }

         var3.removeIf(var1x -> var1.completed(var1x) || var1.locked(var1x));
         return var3.isEmpty() ? randomIncomplete(var0, var1) : (Integer)var3.get(ThreadLocalRandom.current().nextInt(var3.size()));
      } else {
         return randomIncomplete(var0, var1);
      }
   }

   private static int randomIncomplete(Bingo var0, Team var1) {
      List var2 = incompleteTasks(var0, var1);
      return var2.isEmpty() ? -1 : (Integer)var2.get(ThreadLocalRandom.current().nextInt(var2.size()));
   }

   private static List<Integer> incompleteTasks(Bingo var0, Team var1) {
      ArrayList var2 = new ArrayList();
      int var3 = var0.tasks().size();

      for (int var4 = 0; var4 < var3; var4++) {
         if (!var1.completed(var4) && !var1.locked(var4)) {
            var2.add(var4);
         }
      }

      return var2;
   }

   private static void completeTask(Bingo var0, Team var1, int var2) {
      if (var2 >= 0 && var2 < var0.tasks().size() && !var1.completed(var2) && !var1.locked(var2)) {
         var1.complete(var2);
         var0.checkWin();
      }
   }

   private static void lockEnemyTasks(Bingo var0, Team var1, int var2) {
      for (Team var4 : var0.getTeams()) {
         if (var4 != var1 && !var4.getPlayers().isEmpty()) {
            for (int var5 = 0; var5 < var2; var5++) {
               ArrayList var6 = new ArrayList();

               for (int var7 = 0; var7 < var0.tasks().size(); var7++) {
                  if (!var4.completed(var7) && !var4.locked(var7)) {
                     var6.add(var7);
                  }
               }

               if (var6.isEmpty()) {
                  break;
               }

               var4.lock((Integer)var6.get(ThreadLocalRandom.current().nextInt(var6.size())));
            }
         }
      }
   }

   private static void revealTasks(MinecraftServer var0, Bingo var1, Team var2, int var3) {
      List var4 = incompleteTasks(var1, var2);
      if (!var4.isEmpty()) {
         MutableComponent var5 = Component.literal("[海克斯] 未完成任务: ");

         for (int var6 = 0; var6 < var3 && !var4.isEmpty(); var6++) {
            int var7 = (Integer)var4.remove(ThreadLocalRandom.current().nextInt(var4.size()));
            if (var6 > 0) {
               var5.append(Component.literal("、"));
            }

            var5.append(var1.task(var7).contentName(var0));
         }

         for (ServerPlayer var9 : onlineTeamPlayers(var0, var2)) {
            var9.sendSystemMessage(var5);
         }
      }
   }

   private static void grantReward(MinecraftServer var0, Team var1, ServerPlayer var2, HexEffect var3) {
      switch (var3.reward) {
         case "item":
            Item var6 = item(var3.key);
            if (var6 != null) {
               int var10 = var3.count > 0 ? var3.count : Math.max(1, (int)var3.amount);
               giveTeamItem(var1, var2, var6, var10);
            }
            break;
         case "xp":
            int var7 = Math.max(1, (int)(var3.amount > 0.0 ? var3.amount : var3.count));

            for (ServerPlayer var12 : onlineTeamPlayers(var0, var1)) {
               var12.giveExperiencePoints(var7);
            }
            break;
         case "teleport":
            var1.teleportsLeft(var1.teleportsLeft() + Math.max(1, (int)var3.amount));
            break;
         case "shield":
            for (ServerPlayer var9 : onlineTeamPlayers(var0, var1)) {
               applyAbsorption(var9, var3.amount > 0.0 ? var3.amount : var3.count, 600);
            }
      }
   }

   private static void giveTeamItem(Team var0, ServerPlayer var1, Item var2, int var3) {
      ItemStack var4 = new ItemStack(var2, var3);
      IItemHandlerModifiable var5 = var0.getBackPack();

      for (int var6 = 0; var6 < var5.getSlots(); var6++) {
         if (var5.getStackInSlot(var6).isEmpty()) {
            var5.setStackInSlot(var6, var4);
            var0.setDirty();
            return;
         }
      }

      var1.addItem(var4);
   }

   private static ItemStack itemTaskStack(Task var0) {
      if (!var0.getType().id().equals("bingo.item")) {
         return null;
      } else {
         return taskElement(var0) instanceof ItemStack var1 ? var1 : null;
      }
   }

   private static Object taskElement(Task var0) {
      return var0.getElement(var0.getType()).orElse(null);
   }

   public static void incomingDamage(LivingIncomingDamageEvent var0) {
      // 无敌小宠物：拦截创造模式等绕过无敌的伤害，但放行 /kill（generic_kill）
      if (isProtectedPet(var0.getEntity()) && !var0.getSource().is(DamageTypes.GENERIC_KILL)) {
         var0.setCanceled(true);
         return;
      }

      if (var0.getEntity() instanceof ServerPlayer var1) {
         List<Hex> var19 = hexes(var1);
         if (!var19.isEmpty()) {
            DamageSource var3 = var0.getSource();
            Entity var4 = var3.getEntity();
            float var5 = var0.getAmount();
            boolean var6 = var1.getHealth() < var1.getMaxHealth() * 0.4F;
            ThreadLocalRandom var7 = ThreadLocalRandom.current();
            boolean var8 = isBehind(var1);

            for (Hex var10 : var19) {
               for (HexEffect var12 : var10.effects()) {
                  switch (var12.type) {
                     case "fall_immune":
                        if (var3.is(DamageTypeTags.IS_FALL)) {
                           var0.setCanceled(true);
                        }
                        break;
                     case "fire_immune":
                        if (var3.is(DamageTypeTags.IS_FIRE)) {
                           var0.setCanceled(true);
                        }
                        break;
                     case "projectile_dodge":
                        if (var3.is(DamageTypeTags.IS_PROJECTILE) && var7.nextDouble() < var12.chance) {
                           var0.setCanceled(true);
                        }
                        break;
                     case "low_health_resist":
                        if (var6) {
                           var0.setAmount((float)(var0.getAmount() * (1.0 - var12.percent)));
                        }
                        break;
                     case "behind_resist":
                        if (var8) {
                           var0.setAmount((float)(var0.getAmount() * (1.0 - var12.percent)));
                        }
                        break;
                     case "thorns":
                        if (var4 instanceof LivingEntity var20 && var4 != var1 && !THORNS.get()) {
                           THORNS.set(Boolean.TRUE);

                           try {
                              var20.hurtServer(var1.level(), var1.damageSources().thorns(var1), (float)(var5 * var12.percent));
                           } finally {
                              THORNS.set(Boolean.FALSE);
                           }
                        }
                        break;
                     case "slow_attacker":
                        if (var4 instanceof LivingEntity var15 && var7.nextDouble() < var12.chance) {
                           var15.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, var12.seconds * 20, var12.amplifier, false, false, false));
                        }
                        break;
                     case "ignite_attacker":
                        if (var4 != null && var7.nextDouble() < var12.chance) {
                           var4.setRemainingFireTicks(var12.seconds * 20);
                        }
                  }
               }
            }
         }
      }
   }

   public static void damageDealt(Pre var0) {
      if (var0.getSource().getEntity() instanceof ServerPlayer var1) {
         // 蝙蝠形态无法攻击
         if (isBatForm(var1)) {
            var0.setNewDamage(0.0F);
            return;
         }

         HexEffects.Temp var7 = TEMP_DAMAGE.get(var1.getUUID());
         if (var7 != null && var7.active()) {
            var0.setNewDamage(var0.getNewDamage() * (1.0F + var7.value()));
         }

         if (isAhead(var1)) {
            for (Hex var4 : hexes(var1)) {
               for (HexEffect var6 : var4.effects()) {
                  if (var6.type.equals("ahead_power")) {
                     var0.setNewDamage((float)(var0.getNewDamage() * (1.0 + var6.percent)));
                  }
               }
            }
         }
      }
   }

   public static void damagePost(Post var0) {
      if (var0.getSource().getEntity() instanceof ServerPlayer var1) {
         List<Hex> var18 = hexes(var1);
         HexEffects.Temp var3 = TEMP_LIFESTEAL.get(var1.getUUID());
         if (var3 != null && var3.active()) {
            var1.heal(var0.getHealthDamage() * var3.value());
         }

         if (!var18.isEmpty()) {
            LivingEntity var4 = var0.getEntity();
            ThreadLocalRandom var5 = ThreadLocalRandom.current();

            for (Hex var7 : var18) {
               for (HexEffect var9 : var7.effects()) {
                  switch (var9.type) {
                     case "lifesteal":
                        var1.heal((float)(var0.getHealthDamage() * var9.percent));
                        break;
                     case "gold_on_hit":
                        if (!(var4 instanceof LivingEntity var10) || var4 instanceof ServerPlayer) {
                           break;
                        }

                        long var13 = System.currentTimeMillis();
                        Long var15 = GOLD_RUSH.get(var10.getUUID());
                        if (var15 == null || var13 >= var15) {
                           GOLD_RUSH.put(var10.getUUID(), var13 + Math.max(1, var9.cooldown) * 1000L);
                           int var19 = Math.max(1, var9.min);
                           int var17 = Math.max(var19, var9.max);
                           var1.addItem(new ItemStack(Items.GOLD_NUGGET, var19 + var5.nextInt(var17 - var19 + 1)));
                        }
                        break;
                     case "poison_blade":
                        if (var4 instanceof LivingEntity var16 && var4 != var1 && var5.nextDouble() < var9.chance) {
                           var16.addEffect(new MobEffectInstance(MobEffects.POISON, var9.seconds * 20, var9.amplifier, false, false, false));
                        }
                  }
               }
            }
         }
      }
   }

   public static void death(LivingDeathEvent var0) {
      if (var0.getSource().getEntity() instanceof ServerPlayer var1 && var0.getEntity() != var1) {
         for (Hex var3 : hexes(var1)) {
            for (HexEffect var5 : var3.effects()) {
               if (var5.type.equals("heal_on_kill")) {
                  var1.heal((float)var5.amount);
               }

               if (var5.type.equals("kill_random_buff") && var0.getEntity() instanceof Monster) {
                  Holder var6 = randomBuff();
                  var1.addEffect(new MobEffectInstance(var6, 1200, ThreadLocalRandom.current().nextInt(2), true, false, false));
               }
            }
         }

         if (!(var0.getEntity() instanceof ServerPlayer)) {
            killStack(var1, var0.getEntity());
         }
      }
   }

   private static Holder<MobEffect> randomBuff() {
      Holder[] var0 = new Holder[]{
         MobEffects.SPEED, MobEffects.HASTE, MobEffects.STRENGTH, MobEffects.RESISTANCE, MobEffects.REGENERATION, MobEffects.ABSORPTION
      };
      return var0[ThreadLocalRandom.current().nextInt(var0.length)];
   }

   private static boolean isBehind(ServerPlayer var0) {
      Bingo var1 = Bingo.get(var0.level());
      Team var2 = var1.getTeam(var0);
      if (var2 == null) {
         return false;
      }

      int var3 = var2.completion().count();
      int var4 = 0;

      for (Team var6 : var1.getTeams()) {
         if (var6 != var2 && !var6.getPlayers().isEmpty()) {
            var4 = Math.max(var4, var6.completion().count());
         }
      }

      return var3 < var4;
   }

   private static boolean isAhead(ServerPlayer var0) {
      Bingo var1 = Bingo.get(var0.level());
      Team var2 = var1.getTeam(var0);
      if (var2 == null) {
         return false;
      }

      int var3 = var2.completion().count();
      int var4 = 0;

      for (Team var6 : var1.getTeams()) {
         if (var6 != var2 && !var6.getPlayers().isEmpty()) {
            var4 = Math.max(var4, var6.completion().count());
         }
      }

      return var3 > var4;
   }

   public static boolean hasHex(ServerPlayer var0, String var1) {
      MinecraftServer var2 = var0.level().getServer();
      if (var2 == null) {
         return false;
      }

      HexGameData var3 = HexGameData.get(var2);
      return var3.mayhem() && var3.hexes(var0.getUUID()).contains(var1);
   }

   // 尖端发明家：所有技能型海克斯的冷却时间缩短 30%
   private static long skillCooldown(ServerPlayer var0, long var1) {
      return hasHex(var0, "cutting_edge") ? (long)(var1 * 0.7) : var1;
   }

   public static boolean randomTeleport(ServerPlayer var0) {
      MinecraftServer var1 = var0.level().getServer();
      if (var1 != null && hasHex(var0, "random_teleport")) {
         long var2 = System.currentTimeMillis();
         Long var4 = RTP_COOLDOWN.get(var0.getUUID());
         if (var4 != null && var2 < var4) {
            long var16 = (var4 - var2 + 999L) / 1000L;
            var0.sendSystemMessage(
               Component.translatable("hexbingo.rtp.cooldown", new Object[]{Component.literal(Long.toString(var16))}).withStyle(ChatFormatting.GOLD)
            );
            return true;
         } else if (!(var0.level() instanceof ServerLevel var5)) {
            return false;
         } else {
            ThreadLocalRandom var15 = ThreadLocalRandom.current();
            BlockPos var7 = null;
            int var8 = var0.blockPosition().getX();
            int var9 = var0.blockPosition().getZ();

            for (int var10 = 0; var10 < 32 && var7 == null; var10++) {
               int var11 = var8 + var15.nextInt(801) - 400;
               int var12 = var9 + var15.nextInt(801) - 400;
               int var13 = var5.getHeight(Types.MOTION_BLOCKING_NO_LEAVES, var11, var12);
               BlockPos var14 = new BlockPos(var11, var13, var12);
               if (var5.isInWorldBounds(var14)
                  && var5.getBlockState(var14).isAir()
                  && var5.getBlockState(var14.above()).isAir()
                  && var5.getBlockState(var14.below()).getFluidState().isEmpty()) {
                  var7 = var14;
               }
            }

            if (var7 == null) {
               var0.sendSystemMessage(Component.translatable("hexbingo.rtp.failed").withStyle(ChatFormatting.RED));
               return true;
            } else {
               var0.teleportTo(var5, var7.getX() + 0.5, var7.getY(), var7.getZ() + 0.5, Set.of(), var0.getYRot(), var0.getXRot(), false);
               RTP_COOLDOWN.put(var0.getUUID(), var2 + skillCooldown(var0, 5000L));
               var0.sendSystemMessage(Component.translatable("hexbingo.rtp.success").withStyle(ChatFormatting.GREEN));
               return true;
            }
         }
      } else {
         return false;
      }
   }

   private static void giveRandomItems(ServerPlayer var0, int var1) {
      ThreadLocalRandom var2 = ThreadLocalRandom.current();

      for (int var3 = 0; var3 < var1; var3++) {
         var0.addItem(new ItemStack((ItemLike)RANDOM_ITEMS.get(var2.nextInt(RANDOM_ITEMS.size()))));
      }
   }

   private static void givePetRandomItem(ServerPlayer var0) {
      Item var1 = randomPetItem();
      if (var1 != null) {
         var0.addItem(new ItemStack(var1));
      }
   }

   private static Item randomPetItem() {
      buildPetItemPool();
      if (PET_ITEM_TOTAL_WEIGHT <= 0) {
         return null;
      }

      int var0 = ThreadLocalRandom.current().nextInt(PET_ITEM_TOTAL_WEIGHT);

      for (int var1 = 0; var1 < PET_ITEM_POOL.size(); var1++) {
         var0 -= PET_ITEM_WEIGHTS[var1];
         if (var0 < 0) {
            return PET_ITEM_POOL.get(var1);
         }
      }

      return PET_ITEM_POOL.isEmpty() ? null : PET_ITEM_POOL.get(PET_ITEM_POOL.size() - 1);
   }

   private static void buildPetItemPool() {
      if (PET_ITEM_POOL != null) {
         return;
      }

      ArrayList<Item> var0 = new ArrayList<>();
      ArrayList<Integer> var1 = new ArrayList<>();
      int var2 = 0;

      for (Item var4 : BuiltInRegistries.ITEM.stream().toList()) {
         if (var4 == Items.AIR || PET_ITEM_BLACKLIST.contains(var4) || var4 instanceof SpawnEggItem) {
            continue;
         }

         ItemStack var5 = var4.getDefaultInstance();
         if (var5.isEmpty()) {
            continue;
         }

         int var6 = petItemWeight(var4, var5);
         if (var6 > 0) {
            var0.add(var4);
            var1.add(var6);
            var2 += var6;
         }
      }

      PET_ITEM_POOL = var0;
      PET_ITEM_WEIGHTS = new int[var1.size()];

      for (int var7 = 0; var7 < var1.size(); var7++) {
         PET_ITEM_WEIGHTS[var7] = var1.get(var7);
      }

      PET_ITEM_TOTAL_WEIGHT = var2;
   }

   private static int petItemWeight(Item var0, ItemStack var1) {
      if (VALUABLE_PET_ITEMS.contains(var0)) {
         return 1;
      }

      Rarity var2 = var1.getRarity();
      if (var2 == Rarity.COMMON) {
         return 30;
      } else if (var2 == Rarity.UNCOMMON) {
         return 8;
      } else if (var2 == Rarity.RARE) {
         return 3;
      } else {
         return var2 == Rarity.EPIC ? 2 : 30;
      }
   }

   private static void giveRandomWood(ServerPlayer var0) {
      ArrayList var1 = new ArrayList<>(WOODS);
      Collections.shuffle(var1, ThreadLocalRandom.current());

      for (int var2 = 0; var2 < 7 && var2 < var1.size(); var2++) {
         var0.addItem(new ItemStack((ItemLike)var1.get(var2), 5));
      }
   }

   private static void giveRandomFlowers(ServerPlayer var0) {
      ArrayList var1 = new ArrayList<>(FLOWERS);
      Collections.shuffle(var1, ThreadLocalRandom.current());

      for (int var2 = 0; var2 < 7 && var2 < var1.size(); var2++) {
         var0.addItem(new ItemStack((ItemLike)var1.get(var2), 3));
      }
   }

   private static void giveRandomWoodAndFlowers(ServerPlayer var0) {
      ArrayList var1 = new ArrayList<>(WOODS);
      Collections.shuffle(var1, ThreadLocalRandom.current());
      ArrayList var2 = new ArrayList<>(FLOWERS);
      Collections.shuffle(var2, ThreadLocalRandom.current());
      ThreadLocalRandom var3 = ThreadLocalRandom.current();

      for (int var4 = 0; var4 < 5 && var4 < var1.size(); var4++) {
         var0.addItem(new ItemStack((ItemLike)var1.get(var4), 1 + var3.nextInt(5)));
      }

      for (int var5 = 0; var5 < 5 && var5 < var2.size(); var5++) {
         var0.addItem(new ItemStack((ItemLike)var2.get(var5), 1 + var3.nextInt(5)));
      }
   }

   private static boolean hasEffect(ServerPlayer var0, String var1) {
      for (Hex var3 : hexes(var0)) {
         for (HexEffect var5 : var3.effects()) {
            if (var5.type.equals(var1)) {
               return true;
            }
         }
      }

      return false;
   }

   private static void swapTasks(ServerPlayer var0) {
      MinecraftServer var1 = var0.level().getServer();
      if (var1 != null) {
         Bingo var2 = Bingo.get(var1.overworld());
         if (var2.running()) {
            ArrayList var3 = new ArrayList(var2.tasks());
            if (var3.size() >= 2) {
               ThreadLocalRandom var4 = ThreadLocalRandom.current();
               int var5 = var4.nextInt(var3.size());
               int var6 = var4.nextInt(var3.size() - 1);
               if (var6 >= var5) {
                  var6++;
               }

               Task var7 = (Task)var3.get(var5);
               var3.set(var5, (Task)var3.get(var6));
               var3.set(var6, var7);
               var2.setTasks(var3);
               var2.markDirty(true);
               var2.checkWin();
               var0.sendSystemMessage(
                  Component.translatable("hexbingo.msg.swap_tasks", new Object[]{var7.contentName(var1), ((Task)var3.get(var5)).contentName(var1)})
                     .withStyle(ChatFormatting.LIGHT_PURPLE)
               );
            }
         }
      }
   }

   private static void growMushrooms(ServerPlayer var0) {
      if (var0.level() instanceof ServerLevel var1) {
         ThreadLocalRandom var9 = ThreadLocalRandom.current();
         ItemLike[] var3 = new ItemLike[]{Blocks.BROWN_MUSHROOM, Blocks.RED_MUSHROOM, Blocks.CRIMSON_FUNGUS, Blocks.WARPED_FUNGUS};
         BlockPos var4 = var0.blockPosition();

         for (int var5 = -3; var5 <= 3; var5++) {
            for (int var6 = -1; var6 <= 1; var6++) {
               for (int var7 = -3; var7 <= 3; var7++) {
                  BlockPos var8 = var4.offset(var5, var6, var7);
                  if (var1.getBlockState(var8).isAir()
                     && !var1.getBlockState(var8.below()).isAir()
                     && var1.getBlockState(var8.below()).getFluidState().isEmpty()
                     && var9.nextDouble() < 0.35) {
                     var1.setBlockAndUpdate(var8, ((Block)var3[var9.nextInt(var3.length)]).defaultBlockState());
                  }
               }
            }
         }

         var0.sendSystemMessage(Component.translatable("hexbingo.msg.mushrooms").withStyle(ChatFormatting.LIGHT_PURPLE));
      }
   }

   private static void wormhole(ServerPlayer var0) {
      if (var0.level() instanceof ServerLevel var1) {
         ThreadLocalRandom var12 = ThreadLocalRandom.current();
         BlockPos var3 = null;

         for (int var4 = 0; var4 < 48 && var3 == null; var4++) {
            double var5 = var12.nextDouble() * Math.PI * 2.0;
            int var7 = 1 + var12.nextInt(1000);
            int var8 = var0.blockPosition().getX() + (int)Math.round(Math.cos(var5) * var7);
            int var9 = var0.blockPosition().getZ() + (int)Math.round(Math.sin(var5) * var7);
            int var10 = var1.getHeight(Types.MOTION_BLOCKING_NO_LEAVES, var8, var9);
            BlockPos var11 = new BlockPos(var8, var10, var9);
            if (var1.isInWorldBounds(var11)
               && var1.getBlockState(var11).isAir()
               && var1.getBlockState(var11.above()).isAir()
               && var1.getBlockState(var11.below()).getFluidState().isEmpty()) {
               var3 = var11;
            }
         }

         if (var3 == null) {
            var0.sendSystemMessage(Component.translatable("hexbingo.rtp.failed").withStyle(ChatFormatting.RED));
         } else {
            var0.teleportTo(var1, var3.getX() + 0.5, var3.getY(), var3.getZ() + 0.5, Set.of(), var0.getYRot(), var0.getXRot(), false);
            var0.sendSystemMessage(Component.translatable("hexbingo.msg.wormhole").withStyle(ChatFormatting.AQUA));
         }
      }
   }

   private static void setVariant(Entity var0, Class<?> var1, String var2, Object var3) {
      try {
         Field var4 = var1.getDeclaredField(var2);
         var4.setAccessible(true);
         Object var5 = var4.get(null);
         var0.getEntityData().set((EntityDataAccessor)var5, var3);
      } catch (Exception var6) {
      }
   }

   private static void summonCats(ServerPlayer var0) {
      if (var0.level() instanceof ServerLevel var1) {
         Registry var12 = var1.registryAccess().lookupOrThrow(Registries.CAT_VARIANT);
         ThreadLocalRandom var3 = ThreadLocalRandom.current();
         String[] var4 = new String[]{"all_black", "black", "british_shorthair", "calico", "jellie", "persian", "ragdoll", "red", "siamese", "tabby", "white"};
         int var5 = 0;

         for (String var9 : var4) {
            CatVariant var10 = (CatVariant)var12.getValue(Identifier.fromNamespaceAndPath("minecraft", var9));
            if (var10 != null) {
               Cat var11 = (Cat)EntityTypes.CAT.create(var1, EntitySpawnReason.MOB_SUMMONED);
               if (var11 != null) {
                  var11.setPos(var0.getX() + var3.nextDouble() * 6.0 - 3.0, var0.getY(), var0.getZ() + var3.nextDouble() * 6.0 - 3.0);
                  setVariant(var11, Cat.class, "DATA_VARIANT_ID", var12.wrapAsHolder(var10));
                  var11.setTame(true, true);
                  var11.setOwner(var0);
                  var11.setPersistenceRequired();
                  var1.addFreshEntity(var11);
                  var5++;
               }
            }
         }

         var0.sendSystemMessage(Component.translatable("hexbingo.msg.cats", new Object[]{var5}).withStyle(ChatFormatting.LIGHT_PURPLE));
      }
   }

   private static void summonWolves(ServerPlayer var0) {
      if (var0.level() instanceof ServerLevel var1) {
         Registry var12 = var1.registryAccess().lookupOrThrow(Registries.WOLF_VARIANT);
         ThreadLocalRandom var3 = ThreadLocalRandom.current();
         String[] var4 = new String[]{"ashen", "black", "chestnut", "pale", "rusty", "snowy", "spotted", "striped", "woods"};
         int var5 = 0;

         for (String var9 : var4) {
            WolfVariant var10 = (WolfVariant)var12.getValue(Identifier.fromNamespaceAndPath("minecraft", var9));
            if (var10 != null) {
               Wolf var11 = (Wolf)EntityTypes.WOLF.create(var1, EntitySpawnReason.MOB_SUMMONED);
               if (var11 != null) {
                  var11.setPos(var0.getX() + var3.nextDouble() * 6.0 - 3.0, var0.getY(), var0.getZ() + var3.nextDouble() * 6.0 - 3.0);
                  setVariant(var11, Wolf.class, "DATA_VARIANT_ID", var12.wrapAsHolder(var10));
                  var11.setTame(true, true);
                  var11.setOwner(var0);
                  var11.setPersistenceRequired();
                  var1.addFreshEntity(var11);
                  var5++;
               }
            }
         }

         var0.sendSystemMessage(Component.translatable("hexbingo.msg.wolves", new Object[]{var5}).withStyle(ChatFormatting.LIGHT_PURPLE));
      }
   }

   private static void giveJamItem(ServerPlayer var0) {
      if (!hasNamedItem(var0, "对方不在服务区")) {
         var0.addItem(questItem(Items.RECOVERY_COMPASS, "对方不在服务区", "hexbingo.item.jam.lore"));
      }

      var0.sendSystemMessage(Component.translatable("hexbingo.msg.jam_item").withStyle(ChatFormatting.LIGHT_PURPLE));
   }

   public static boolean castJam(ServerPlayer var0) {
      if (!hasHex(var0, "jam_bp")) {
         return false;
      }

      MinecraftServer var1 = var0.level().getServer();
      if (var1 == null) {
         return false;
      }

      long var2 = System.currentTimeMillis();
      Long var4 = JAM_COOLDOWN.get(var0.getUUID());
      if (var4 != null && var2 < var4) {
         long var9 = (var4 - var2 + 999L) / 1000L;
         var0.sendSystemMessage(
            Component.translatable("hexbingo.jam.cooldown", new Object[]{Component.literal(Long.toString(var9))}).withStyle(ChatFormatting.GRAY)
         );
         return true;
      }

      Team var5 = team(var0);
      int var6 = 0;

      for (Team var8 : Bingo.get(var1.overworld()).getTeams()) {
         if (var8 != var5 && !var8.getPlayers().isEmpty()) {
            long jamEnd = var2 + 180000L;
            BP_JAM.put(var8.color, jamEnd);

            for (UUID id : var8.getPlayers()) {
               ServerPlayer member = var1.getPlayerList().getPlayer(id);
               if (member != null && shieldBlocks(member, "jam_bp")) {
                  BP_JAM_PLAYER.put(id, jamEnd);
               }
            }

            var6++;
         }
      }

      if (var6 == 0) {
         var0.sendSystemMessage(Component.translatable("hexbingo.jam.none").withStyle(ChatFormatting.GRAY));
         return true;
      } else {
         JAM_COOLDOWN.put(var0.getUUID(), var2 + skillCooldown(var0, 540000L));
         HexBingo.LOGGER.info("BP jam cast by {} against {} teams for 180s", var0.getScoreboardName(), var6);
         var0.sendSystemMessage(Component.translatable("hexbingo.msg.jam_cast", new Object[]{var6}).withStyle(ChatFormatting.LIGHT_PURPLE));
         return true;
      }
   }

   public static boolean isBpJammed(ServerPlayer var0) {
      Long var3 = BP_JAM_PLAYER.get(var0.getUUID());
      if (var3 != null && System.currentTimeMillis() < var3) {
         return false;
      }

      Team var1 = team(var0);
      if (var1 == null) {
         return false;
      }

      Long var2 = BP_JAM.get(var1.color);
      return var2 != null && System.currentTimeMillis() < var2;
   }

   public static void clearJams() {
      BP_JAM.clear();
      BP_JAM_PLAYER.clear();
   }

   private static void replaceTools(ServerPlayer var0) {
      ThreadLocalRandom var1 = ThreadLocalRandom.current();

      for (int var2 = 0; var2 < var0.getInventory().getContainerSize(); var2++) {
         ItemStack var3 = var0.getInventory().getItem(var2);
         if (!var3.isEmpty()) {
            Item var4 = randomTierTool(var3, var1);
            if (var4 != null) {
               var0.getInventory().setItem(var2, markHexGranted(new ItemStack(var4)));
            }
         }
      }

      var0.sendSystemMessage(Component.translatable("hexbingo.msg.forge_master").withStyle(ChatFormatting.LIGHT_PURPLE));
   }

   private static Item randomTierTool(ItemStack var0, ThreadLocalRandom var1) {
      Item[] var2;
      if (var0.is(ItemTags.SWORDS)) {
         var2 = new Item[]{Items.WOODEN_SWORD, Items.STONE_SWORD, Items.IRON_SWORD, Items.GOLDEN_SWORD, Items.DIAMOND_SWORD, Items.NETHERITE_SWORD};
      } else if (var0.is(ItemTags.PICKAXES)) {
         var2 = new Item[]{Items.WOODEN_PICKAXE, Items.STONE_PICKAXE, Items.IRON_PICKAXE, Items.GOLDEN_PICKAXE, Items.DIAMOND_PICKAXE, Items.NETHERITE_PICKAXE};
      } else if (var0.is(ItemTags.AXES)) {
         var2 = new Item[]{Items.WOODEN_AXE, Items.STONE_AXE, Items.IRON_AXE, Items.GOLDEN_AXE, Items.DIAMOND_AXE, Items.NETHERITE_AXE};
      } else if (var0.is(ItemTags.SHOVELS)) {
         var2 = new Item[]{Items.WOODEN_SHOVEL, Items.STONE_SHOVEL, Items.IRON_SHOVEL, Items.GOLDEN_SHOVEL, Items.DIAMOND_SHOVEL, Items.NETHERITE_SHOVEL};
      } else {
         if (!var0.is(ItemTags.HOES)) {
            return null;
         }

         var2 = new Item[]{Items.WOODEN_HOE, Items.STONE_HOE, Items.IRON_HOE, Items.GOLDEN_HOE, Items.DIAMOND_HOE, Items.NETHERITE_HOE};
      }

      return var2[var1.nextInt(var2.length)];
   }

   private static void nearestVillage(ServerPlayer var0) {
      MinecraftServer var1 = var0.level().getServer();
      if (var1 != null) {
         ServerLevel var2 = var1.overworld();
         BlockPos var3 = var2.findNearestMapStructure(StructureTags.VILLAGE, var0.blockPosition(), 100, false);
         if (var3 == null) {
            if (!generateStructure(var0, var2, "minecraft:village", "hexbingo.msg.village_tp")) {
               var0.sendSystemMessage(Component.translatable("hexbingo.msg.village_none").withStyle(ChatFormatting.GRAY));
            }
         } else {
            var2.getChunk(var3.getX() >> 4, var3.getZ() >> 4);
            int var4 = var2.getHeight(Types.MOTION_BLOCKING_NO_LEAVES, var3.getX(), var3.getZ());
            int var5 = safeGroundY(var2, var3.getX(), var3.getZ(), var4 + 2);
            if (var5 < 0) {
               if (!generateStructure(var0, var2, "minecraft:village", "hexbingo.msg.village_tp")) {
                  var0.sendSystemMessage(Component.translatable("hexbingo.msg.village_fail").withStyle(ChatFormatting.RED));
               }
            } else {
               var0.teleportTo(var2, var3.getX() + 0.5, var5, var3.getZ() + 0.5, Set.of(), var0.getYRot(), var0.getXRot(), false);
               var0.sendSystemMessage(Component.translatable("hexbingo.msg.village_tp").withStyle(ChatFormatting.GOLD));
            }
         }
      }
   }

   // 凝视深渊：传送到最近的 deep_dark 群系
   private static void gazeAbyss(ServerPlayer var0) {
      MinecraftServer var1 = var0.level().getServer();
      if (var1 == null) {
         return;
      }

      ServerLevel var2 = var1.overworld();
      Pair<BlockPos, Holder<Biome>> var3 = var2.findClosestBiome3d(var0x -> var0x.is(Biomes.DEEP_DARK), var0.blockPosition(), 12800, 32, 96);
      if (var3 == null) {
         BlockPos var5 = createArtificialDeepDark(var2, var0, 500);
         if (var5 == null) {
            var0.sendSystemMessage(Component.translatable("hexbingo.msg.gaze_abyss_none").withStyle(ChatFormatting.GRAY));
            return;
         }

         var0.teleportTo(var2, var5.getX() + 0.5, var5.getY(), var5.getZ() + 0.5, Set.of(), var0.getYRot(), var0.getXRot(), false);
         var0.resetFallDistance();
         var0.sendSystemMessage(Component.translatable("hexbingo.msg.gaze_abyss_gen").withStyle(ChatFormatting.DARK_PURPLE));
         return;
      }

      BlockPos var4 = findDeepDarkSpot(var2, var3.getFirst());
      var0.teleportTo(var2, var4.getX() + 0.5, var4.getY(), var4.getZ() + 0.5, Set.of(), var0.getYRot(), var0.getXRot(), false);
      var0.resetFallDistance();
      var0.sendSystemMessage(Component.translatable("hexbingo.msg.gaze_abyss").withStyle(ChatFormatting.DARK_PURPLE));
   }

   private static BlockPos findDeepDarkSpot(ServerLevel var0, BlockPos var1) {
      // 先加载目标附近区块，避免读取到未生成的空气
      for (int var7 = -1; var7 <= 1; var7++) {
         for (int var8 = -1; var8 <= 1; var8++) {
            var0.getChunk((var1.getX() >> 4) + var7, (var1.getZ() >> 4) + var8);
         }
      }

      int var6 = Math.max(var0.getMinY() + 2, var1.getY() - 40);

      for (int var2 = var1.getY() + 24; var2 >= var6; var2 -= 2) {
         for (int var3 = -8; var3 <= 8; var3 += 2) {
            for (int var4 = -8; var4 <= 8; var4 += 2) {
               BlockPos var5 = var1.offset(var3, var2 - var1.getY(), var4);
               if (var0.getBlockState(var5).isAir() && var0.getBlockState(var5.above()).isAir() && !var0.getBlockState(var5.below()).isAir()) {
                  return var5;
               }
            }
         }
      }

      return carveSafeSpot(var0, var1);
   }

   // 谁才是掠夺者：传送到最近的掠夺者前哨站，找不到就在 8 个方向 200 格外生成一座
   private static void pillagerOutpost(ServerPlayer var0) {
      MinecraftServer var1 = var0.level().getServer();
      if (var1 == null) {
         return;
      }

      ServerLevel var2 = var1.overworld();
      BlockPos var3 = var2.findNearestMapStructure(PILLAGER_OUTPOST, var0.blockPosition(), 100, false);
      if (var3 != null) {
         BlockPos var4 = safeSurfaceSpot(var2, var3);
         if (var4 != null) {
            teleportSurface(var0, var2, var4, "hexbingo.msg.pillager_outpost");
            return;
         }
      }

      BlockPos var5 = generatePillagerOutpostAt(var2, var0, 200);
      if (var5 == null) {
         var0.sendSystemMessage(Component.translatable("hexbingo.msg.pillager_outpost_none").withStyle(ChatFormatting.GRAY));
         return;
      }

      BlockPos var6 = safeSurfaceSpot(var2, var5);
      if (var6 == null) {
         int var7 = Math.max(var2.getMinY() + 2, var5.getY() + 1);
         var6 = carveSafeSpot(var2, new BlockPos(var5.getX(), var7, var5.getZ()));
      }

      teleportSurface(var0, var2, var6, "hexbingo.msg.pillager_outpost_gen");
   }

   // 兜底生成：按 8 个方位各 200 格寻找陆地，实在找不到再退化为任意地表
   private static BlockPos generatePillagerOutpostAt(ServerLevel var2, ServerPlayer var0, int var1) {
      MinecraftServer var3 = var2.getServer();
      if (var3 == null) {
         return null;
      }

      int var4 = var0.getBlockX();
      int var5 = var0.getBlockZ();

      for (int var6 = 0; var6 < 2; var6++) {
         for (int var7 = 0; var7 < 8; var7++) {
            double var8 = (Math.PI * 2.0) * (double)var7 / 8.0;
            int var10 = var4 + (int)Math.round(Math.cos(var8) * (double)var1);
            int var11 = var5 + (int)Math.round(Math.sin(var8) * (double)var1);
            var2.getChunk(var10 >> 4, var11 >> 4);
            int var12 = var2.getHeight(Types.MOTION_BLOCKING_NO_LEAVES, var10, var11);

            if (var6 == 0) {
               Holder<Biome> var13 = var2.getBiome(new BlockPos(var10, var12, var11));
               if (var13.is(BiomeTags.IS_OCEAN) || var13.is(BiomeTags.IS_RIVER) || var13.is(BiomeTags.IS_BEACH)) {
                  continue;
               }

               if (!var2.getBlockState(new BlockPos(var10, var12 - 1, var11)).getFluidState().isEmpty()) {
                  continue;
               }
            }

            BlockPos var16 = new BlockPos(var10, var12, var11);
            CommandSourceStack var14 = var3.createCommandSourceStack().withLevel(var2).withPosition(Vec3.atCenterOf(var16)).withSuppressedOutput();
            int var15;
            try {
               var15 = var3.getCommands()
                  .getDispatcher()
                  .execute("place structure minecraft:pillager_outpost " + var10 + " " + var12 + " " + var11, var14);
            } catch (CommandSyntaxException var17) {
               var15 = 0;
            }

            if (var15 > 0) {
               return var16;
            }
         }
      }

      return null;
   }

   // 故乡的樱花开了：传送到最近的樱花群系
   private static void cherryGrove(ServerPlayer var0) {
      MinecraftServer var1 = var0.level().getServer();
      if (var1 == null) {
         return;
      }

      ServerLevel var2 = var1.overworld();
      Pair<BlockPos, Holder<Biome>> var3 = var2.findClosestBiome3d(var0x -> var0x.is(Biomes.CHERRY_GROVE), var0.blockPosition(), 12800, 32, 96);
      if (var3 == null) {
         BlockPos var6 = createArtificialBiome(var2, var0, 500, "minecraft:cherry_grove", "cherry");
         if (var6 == null) {
            var0.sendSystemMessage(Component.translatable("hexbingo.msg.cherry_grove_none").withStyle(ChatFormatting.GRAY));
            return;
         }

         teleportSurface(var0, var2, var6, "hexbingo.msg.cherry_grove_gen");
         return;
      }

      BlockPos var4 = safeSurfaceSpot(var2, var3.getFirst());
      if (var4 == null) {
         int var5 = Math.max(var2.getMinY() + 2, var3.getFirst().getY());
         var4 = carveSafeSpot(var2, new BlockPos(var3.getFirst().getX(), var5, var3.getFirst().getZ()));
      }

      teleportSurface(var0, var2, var4, "hexbingo.msg.cherry_grove");
   }

   // 一二三木头人：给予一张指向最近苍白花园群系的地图
   private static void givePaleGardenMap(ServerPlayer var0) {
      MinecraftServer var1 = var0.level().getServer();
      if (var1 == null) {
         return;
      }

      ServerLevel var2 = var1.overworld();
      Pair<BlockPos, Holder<Biome>> var3 = var2.findClosestBiome3d(var0x -> var0x.is(Biomes.PALE_GARDEN), var0.blockPosition(), 12800, 32, 96);
      if (var3 == null) {
         BlockPos var6 = createArtificialBiome(var2, var0, 500, "minecraft:pale_garden", "pale");
         if (var6 == null) {
            var0.sendSystemMessage(Component.translatable("hexbingo.msg.pale_garden_none").withStyle(ChatFormatting.GRAY));
            return;
         }

         var0.addItem(paleGardenMap(var2, var6.getX(), var6.getZ()));
         var0.sendSystemMessage(Component.translatable("hexbingo.msg.pale_garden_gen").withStyle(ChatFormatting.GOLD));
         return;
      }

      BlockPos var4 = var3.getFirst();
      var0.addItem(paleGardenMap(var2, var4.getX(), var4.getZ()));
      var0.sendSystemMessage(Component.translatable("hexbingo.msg.pale_garden_map").withStyle(ChatFormatting.GOLD));
   }

   private static ItemStack paleGardenMap(ServerLevel var0, int var1, int var2) {
      var0.getChunk(var1 >> 4, var2 >> 4);
      ItemStack var3 = MapItem.create(var0, var1, var2, (byte)1, true, true);
      var3.set(DataComponents.CUSTOM_NAME, Component.translatable("hexbingo.item.pale_garden_map"));
      int var4 = var0.getHeight(Types.MOTION_BLOCKING_NO_LEAVES, var1, var2);
      MapItemSavedData.addTargetDecoration(var3, new BlockPos(var1, var4, var2), "+", MapDecorationTypes.RED_X);
      return var3;
   }

   // 群系兜底：在玩家 500 格外选一块陆地，改造成目标群系并补种对应植被
   private static BlockPos createArtificialBiome(ServerLevel var2, ServerPlayer var0, int var1, String var3, String var4) {
      MinecraftServer var5 = var2.getServer();
      if (var5 == null) {
         return null;
      }

      BlockPos var6 = findLandSpot(var2, var0, var1);
      if (var6 == null) {
         return null;
      }

      int var7 = var6.getX();
      int var8 = var6.getY();
      int var9 = var6.getZ();
      int var10 = var7 - 16;
      int var11 = var7 + 15;
      int var12 = var9 - 16;
      int var13 = var9 + 15;
      // 先清掉地表植被和树木，给新树腾出空间
      runCommand(var5, var2, var6, "fill " + var10 + " " + (var8 + 1) + " " + var12 + " " + var11 + " " + (var8 + 14) + " " + var13 + " air");
      // 把这一片区域的群系改成目标群系
      runCommand(var5, var2, var6, "fillbiome " + var10 + " " + (var8 - 8) + " " + var12 + " " + var11 + " " + (var8 + 8) + " " + var13 + " " + var3);

      if (var4.equals("cherry")) {
         runCommand(var5, var2, var6, "place feature minecraft:trees_cherry " + (var7 - 4) + " " + var8 + " " + (var9 - 4));
         runCommand(var5, var2, var6, "place feature minecraft:trees_cherry " + (var7 + 4) + " " + var8 + " " + (var9 + 4));
         runCommand(var5, var2, var6, "place feature minecraft:flower_cherry " + var7 + " " + var8 + " " + var9);
      } else {
         int[][] var14 = new int[][]{{-10, -10}, {-4, -11}, {3, -9}, {10, -10}, {-11, 2}, {-5, 4}, {4, 3}, {11, 1}, {-9, 10}, {-2, 11}, {5, 10}, {10, 11}};

         for (int[] var15 : var14) {
            runCommand(var5, var2, var6, "place feature minecraft:pale_oak_creaking_checked " + (var7 + var15[0]) + " " + var8 + " " + (var9 + var15[1]));
         }

         runCommand(var5, var2, var6, "place feature minecraft:pale_moss_patch " + (var7 - 6) + " " + var8 + " " + (var9 - 6));
         runCommand(var5, var2, var6, "place feature minecraft:pale_moss_patch " + (var7 + 6) + " " + var8 + " " + (var9 + 6));
         runCommand(var5, var2, var6, "place feature minecraft:pale_garden_vegetation " + var7 + " " + var8 + " " + var9);
      }

      BlockPos var16 = safeSurfaceSpot(var2, new BlockPos(var7, var8, var9));
      return var16 == null ? new BlockPos(var7, var8, var9) : var16;
   }

   // 回力镖兜底：任意群系都在 500 格外生成一块目标群系区域
   private static BlockPos createArtificialBiomeGeneric(ServerLevel var0, ServerPlayer var1, String var2) {
      MinecraftServer var3 = var0.getServer();
      if (var3 == null) {
         return null;
      }

      BlockPos var4 = findLandSpot(var0, var1, 500);
      if (var4 == null) {
         return null;
      }

      int var5 = var4.getX();
      int var6 = var4.getY();
      int var7 = var4.getZ();
      runCommand(var3, var0, var4, "fillbiome " + (var5 - 16) + " " + (var6 - 8) + " " + (var7 - 16) + " " + (var5 + 15) + " " + (var6 + 8) + " " + (var7 + 15) + " " + var2);
      return var4;
   }

   private static BlockPos findLandSpot(ServerLevel var2, ServerPlayer var0, int var1) {
      for (int var3 = 0; var3 < 8; var3++) {
         double var4 = (Math.PI * 2.0) * (double)var3 / 8.0;
         int var6 = var0.getBlockX() + (int)Math.round(Math.cos(var4) * (double)var1);
         int var7 = var0.getBlockZ() + (int)Math.round(Math.sin(var4) * (double)var1);
         var2.getChunk(var6 >> 4, var7 >> 4);
         int var8 = var2.getHeight(Types.MOTION_BLOCKING_NO_LEAVES, var6, var7);
         if (var8 <= var2.getMinY() + 2 || var8 >= var2.getMaxY() - 2) {
            continue;
         }

         if (!var2.getBlockState(new BlockPos(var6, var8 - 1, var7)).getFluidState().isEmpty()) {
            continue;
         }

         // 只选草方块/泥土类地表，保证樱花树与苍白橡树能正常生长
         if (!var2.getBlockState(new BlockPos(var6, var8 - 1, var7)).is(BlockTags.DIRT)) {
            continue;
         }

         String var9 = var2.getBiome(new BlockPos(var6, var8, var7))
            .unwrapKey()
            .map(var10 -> var10.identifier().getPath())
            .orElse("");
         if (var9.contains("ocean")
            || var9.contains("river")
            || var9.contains("beach")
            || var9.contains("desert")
            || var9.contains("badlands")
            || var9.contains("snow")
            || var9.contains("frozen")
            || var9.contains("icy")) {
            continue;
         }

         return new BlockPos(var6, var8, var7);
      }

      return null;
   }

   private static int runCommand(MinecraftServer var0, ServerLevel var1, BlockPos var2, String var3) {
      CommandSourceStack var4 = var0.createCommandSourceStack().withLevel(var1).withPosition(Vec3.atCenterOf(var2)).withSuppressedOutput();

      try {
         return var0.getCommands().getDispatcher().execute(var3, var4);
      } catch (CommandSyntaxException var6) {
         return 0;
      }
   }

   // 深暗兜底：在 500 格外地下挖一间深板岩洞穴，改成 deep_dark 并铺幽匿块
   private static BlockPos createArtificialDeepDark(ServerLevel var2, ServerPlayer var0, int var1) {
      MinecraftServer var3 = var2.getServer();
      if (var3 == null) {
         return null;
      }

      BlockPos var4 = findLandSpot(var2, var0, var1);
      if (var4 == null) {
         return null;
      }

      int var5 = var4.getX();
      int var6 = var4.getZ();
      int var7 = Math.max(var2.getMinY() + 20, -44);
      var2.getChunk(var5 >> 4, var6 >> 4);
      BlockPos var8 = new BlockPos(var5, var7, var6);
      // 挖出 17×7×17 的洞穴，并铺深板岩地板与天花板
      runCommand(var3, var2, var8, "fill " + (var5 - 8) + " " + var7 + " " + (var6 - 8) + " " + (var5 + 8) + " " + (var7 + 6) + " " + (var6 + 8) + " air");
      runCommand(
         var3, var2, var8, "fill " + (var5 - 8) + " " + (var7 - 1) + " " + (var6 - 8) + " " + (var5 + 8) + " " + (var7 - 1) + " " + (var6 + 8) + " minecraft:deepslate"
      );
      runCommand(
         var3, var2, var8, "fill " + (var5 - 8) + " " + (var7 + 7) + " " + (var6 - 8) + " " + (var5 + 8) + " " + (var7 + 7) + " " + (var6 + 8) + " minecraft:deepslate"
      );
      // 群系覆盖整个洞穴及外围一圈
      runCommand(
         var3, var2, var8, "fillbiome " + (var5 - 8) + " " + (var7 - 8) + " " + (var6 - 8) + " " + (var5 + 8) + " " + (var7 + 16) + " " + (var6 + 8) + " minecraft:deep_dark"
      );
      ThreadLocalRandom var9 = ThreadLocalRandom.current();

      for (int var10 = 0; var10 < 80; var10++) {
         int var11 = var5 + var9.nextInt(17) - 8;
         int var12 = var6 + var9.nextInt(17) - 8;
         BlockPos var13 = new BlockPos(var11, var7 - 1, var12);
         if (!var2.getBlockState(var13).isAir()) {
            var2.setBlockAndUpdate(var13, Blocks.SCULK.defaultBlockState());
            if (var9.nextDouble() < 0.6) {
               var2.setBlockAndUpdate(var13.above(), Blocks.SCULK_VEIN.defaultBlockState());
            }
         }
      }

      var2.setBlockAndUpdate(new BlockPos(var5, var7, var6), Blocks.SCULK_CATALYST.defaultBlockState());
      var2.setBlockAndUpdate(new BlockPos(var5 - 4, var7, var6 + 3), Blocks.SCULK_SENSOR.defaultBlockState());
      var2.setBlockAndUpdate(new BlockPos(var5 + 5, var7, var6 - 2), Blocks.SCULK_SENSOR.defaultBlockState());
      return new BlockPos(var5, var7, var6);
   }

   private static BlockPos safeSurfaceSpot(ServerLevel var0, BlockPos var1) {
      var0.getChunk(var1.getX() >> 4, var1.getZ() >> 4);
      int var2 = var0.getHeight(Types.MOTION_BLOCKING_NO_LEAVES, var1.getX(), var1.getZ());
      int var3 = safeGroundY(var0, var1.getX(), var1.getZ(), var2 + 2);
      return var3 < 0 ? null : new BlockPos(var1.getX(), var3, var1.getZ());
   }

   private static void teleportSurface(ServerPlayer var0, ServerLevel var1, BlockPos var2, String var3) {
      var0.teleportTo(var1, var2.getX() + 0.5, var2.getY(), var2.getZ() + 0.5, Set.of(), var0.getYRot(), var0.getXRot(), false);
      var0.resetFallDistance();
      var0.sendSystemMessage(Component.translatable(var3).withStyle(ChatFormatting.GOLD));
   }

   private static void summonTravelFrog(ServerPlayer var0) {
      if (var0.level() instanceof ServerLevel var1) {
         Frog var3 = (Frog)EntityTypes.FROG.create(var1, EntitySpawnReason.MOB_SUMMONED);
         if (var3 != null) {
            var3.setPos(var0.getX() + 1.0, var0.getY(), var0.getZ());
            var3.setCustomName(Component.literal("旅行青蛙"));
            var3.setCustomNameVisible(true);
            var3.setPersistenceRequired();
            var3.setInvulnerable(true);
            var1.addFreshEntity(var3);
            var0.sendSystemMessage(Component.translatable("hexbingo.msg.frog_summon").withStyle(ChatFormatting.AQUA));
         }
      }
   }

   public static void tradeFrog(ServerPlayer var0, Frog var1) {
      ItemStack var2 = var0.getMainHandItem();
      if (var2.getCount() >= 32 && var2.has(DataComponents.FOOD)) {
         ItemStack var3 = treasureMap(var0);
         if (var3 == null) {
            var0.sendSystemMessage(Component.translatable("hexbingo.msg.frog_none").withStyle(ChatFormatting.GRAY));
            return;
         }

         var2.shrink(32);
         var0.addItem(var3);
         var0.sendSystemMessage(Component.translatable("hexbingo.msg.frog_map").withStyle(ChatFormatting.GOLD));
         var1.discard();
      } else {
         var0.sendSystemMessage(Component.translatable("hexbingo.msg.frog_food").withStyle(ChatFormatting.GRAY));
      }
   }

   private static ItemStack treasureMap(ServerPlayer var0) {
      MinecraftServer var1 = var0.level().getServer();
      if (var1 == null) {
         return null;
      } else {
         boolean var2 = var0.level().dimension() == Level.NETHER;
         ServerLevel var3 = var2 ? var1.getLevel(Level.NETHER) : var1.overworld();
         if (var3 == null) {
            return null;
         } else {
            TagKey<Structure>[] var4;
            String[] var5;
            int[] var6;
            String var7;
            if (var2) {
               var4 = new TagKey[]{BASTION_REMNANT, NETHER_FORTRESS, NETHER_RUINED_PORTAL};
               var5 = new String[]{"猪灵堡垒地图", "地狱堡垒地图", "废弃地狱门地图"};
               var6 = new int[]{1, 1, 1};
               var7 = "minecraft:fortress";
            } else {
               boolean var8 = var0.level()
                  .getBiome(var0.blockPosition())
                  .unwrapKey()
                  .map(var9 -> var9.identifier().getPath())
                  .orElse("")
                  .contains("dark_forest");
               var4 = new TagKey[]{
                  StructureTags.ON_TREASURE_MAPS,
                  StructureTags.EYE_OF_ENDER_LOCATED,
                  StructureTags.ON_WOODLAND_EXPLORER_MAPS,
                  StructureTags.VILLAGE,
                  ANCIENT_CITY,
                  PILLAGER_OUTPOST,
                  DESERT_PYRAMID,
                  JUNGLE_PYRAMID
               };
               var5 = new String[]{"沉船藏宝图", "末地遗迹地图", "林地府邸地图", "村庄地图", "幽匿城市地图", "前哨站地图", "沙漠神殿地图", "丛林神庙地图"};
               var6 = new int[]{5, 1, var8 ? 2 : 1, 3, 1, 2, 2, 2};
               var7 = "minecraft:village";
            }

            int var8 = 0;

            for (int var9 : var6) {
               var8 += var9;
            }

            boolean[] var18 = new boolean[var4.length];

            for (int var10 = 0; var10 < var4.length; var10++) {
               int var11 = ThreadLocalRandom.current().nextInt(var8);
               int var12 = -1;

               for (int var13 = 0; var13 < var4.length; var13++) {
                  if (!var18[var13]) {
                     if (var11 < var6[var13]) {
                        var12 = var13;
                        break;
                     }

                     var11 -= var6[var13];
                  }
               }

               if (var12 < 0) {
                  for (int var19 = 0; var19 < var4.length; var19++) {
                     if (!var18[var19]) {
                        var12 = var19;
                        break;
                     }
                  }
               }

               var18[var12] = true;
               var8 -= var6[var12];
               BlockPos var20 = var3.findNearestMapStructure(var4[var12], var0.blockPosition(), 100, false);
               if (var20 != null) {
                  return structureMap(var3, var20.getX(), var20.getZ(), var5[var12]);
               }
            }

            BlockPos var21 = generateStructureAt(var3, var0, var7, 200);
            return var21 == null ? null : structureMap(var3, var21.getX(), var21.getZ(), var2 ? "地狱堡垒地图" : "村庄地图");
         }
      }
   }

   private static ItemStack structureMap(ServerLevel var0, int var1, int var2, String var3) {
      ItemStack var4 = MapItem.create(var0, var1, var2, (byte)1, true, true);
      var4.set(DataComponents.CUSTOM_NAME, Component.literal(var3));
      MapItemSavedData.addTargetDecoration(
         var4, new BlockPos(var1, var0.getHeight(Types.MOTION_BLOCKING_NO_LEAVES, var1, var2), var2), "+", MapDecorationTypes.RED_X
      );
      return var4;
   }

   private static void amnesia(ServerPlayer var0) {
      MinecraftServer var1 = var0.level().getServer();
      if (var1 != null) {
         Bingo var2 = Bingo.get(var1.overworld());
         Team var3 = var2.getTeam(var0);
         ArrayList var4 = new ArrayList();

         for (Team var6 : var2.getTeams()) {
            if (var6 != var3 && hasCompleted(var6)) {
               var4.add(var6);
            }
         }

         if (var4.isEmpty()) {
            var0.sendSystemMessage(Component.translatable("hexbingo.msg.amnesia_none").withStyle(ChatFormatting.GRAY));
            return;
         }

         Team var7 = (Team)var4.get(ThreadLocalRandom.current().nextInt(var4.size()));
         int var8 = randomCompleted(var7);
         uncompleteSlot(var1, var2, var7, var8);
         var0.sendSystemMessage(Component.translatable("hexbingo.msg.amnesia", new Object[]{var2.task(var8).contentName(var1)}).withStyle(ChatFormatting.GOLD));
      }
   }

   private static boolean hasCompleted(Team var0) {
      for (int var1 = 0; var1 < 25; var1++) {
         if (var0.completed(var1)) {
            return true;
         }
      }

      return false;
   }

   private static int randomCompleted(Team var0) {
      ArrayList var1 = new ArrayList();

      for (int var2 = 0; var2 < 25; var2++) {
         if (var0.completed(var2)) {
            var1.add(var2);
         }
      }

      return var1.isEmpty() ? -1 : (Integer)var1.get(ThreadLocalRandom.current().nextInt(var1.size()));
   }

   private static void uncompleteSlot(MinecraftServer var0, Bingo var1, Team var2, int var3) {
      if (var3 >= 0) {
         // 任务护盾：被锁定的卡牌不会被退回
         if (isTaskShielded(var2, var1, var3)) {
            return;
         }

         ArrayList<Integer> var4 = new ArrayList<>();

         for (int var5 = 0; var5 < 25; var5++) {
            if (var2.completed(var5)) {
               var4.add(var5);
            }
         }

         clearTaskItems(var0, var1, var2, var3);
         var2.resetCompleted();

         for (int var6 : var4) {
            if (var6 != var3) {
               var2.complete(var6);
            }
         }

         // 占点/锁王模式：被退回的队伍在该格可能因其它队伍的占点而处于锁定状态，清除后才能重新完成
         releaseTeamSlotLock(var1, var2, var3);
         var1.markDirty(true);
         var1.checkWin();
      }
   }

   private static void releaseTeamSlotLock(Bingo var0, Team var1, int var2) {
      if (var2 < 0 || !(var0.getSettings().game().lockout() || var0.getSettings().game().winCondition() == WinCondition.LOCK)) {
         return;
      }

      if (var1.locked(var2)) {
         ArrayList<Integer> var3 = new ArrayList<>();

         for (int var4 = 0; var4 < 25; var4++) {
            if (var1.locked(var4)) {
               var3.add(var4);
            }
         }

         var1.resetLocked();
         for (int var4 : var3) {
            if (var4 != var2) {
               var1.lock(var4);
            }
         }
      }
   }

   private static void clearTaskItems(MinecraftServer var0, Bingo var1, Team var2, int var3) {
      Task var4 = var1.task(var3);
      if (var4 != null) {
         Object var5 = taskElement(var4);
         if (var5 instanceof ItemStack var6) {
            for (ServerPlayer var10 : onlineTeamPlayers(var0, var2)) {
               removeMatchingItems(var10, var1x -> ItemStack.isSameItemSameComponents(var1x, var6));
            }
         } else if (var5 instanceof TagWithCount var7) {
            for (ServerPlayer var22 : onlineTeamPlayers(var0, var2)) {
               removeMatchingItems(var22, var1x -> var7.contains(var1x.getItem()));
            }
         } else if (var4.getType() == EnchantTaskType.INSTANCE && var5 instanceof Identifier var8) {
            Registry var21 = var0.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
            Enchantment var23 = (Enchantment)var21.getValue(var8);
            if (var23 != null) {
               Holder var11 = var21.wrapAsHolder(var23);

               for (ServerPlayer var13 : onlineTeamPlayers(var0, var2)) {
                  boolean var14 = false;

                  for (ItemStack var16 : var13.getInventory().getNonEquipmentItems()) {
                     if (!var16.isEmpty() && EnchantmentHelper.getItemEnchantmentLevel(var11, var16) > 0) {
                        stripEnchantment(var16, var11);
                        var14 = true;
                     }
                  }

                  for (EquipmentSlot var18 : EquipmentSlot.values()) {
                     ItemStack var19 = var13.getItemBySlot(var18);
                     if (!var19.isEmpty() && EnchantmentHelper.getItemEnchantmentLevel(var11, var19) > 0) {
                        stripEnchantment(var19, var11);
                        var14 = true;
                     }
                  }

                  if (var14) {
                     var13.containerMenu.broadcastChanges();
                  }
               }
            }
         }
      }
   }

   private static void stripEnchantment(ItemStack var0, Holder<Enchantment> var1) {
      EnchantmentHelper.updateEnchantments(var0, var1x -> var1x.set(var1, 0));
   }

   private static void removeMatchingItems(ServerPlayer var0, Predicate<ItemStack> var1) {
      NonNullList var2 = var0.getInventory().getNonEquipmentItems();
      boolean var3 = false;

      for (int var4 = 0; var4 < var2.size(); var4++) {
         ItemStack var5 = (ItemStack)var2.get(var4);
         if (!var5.isEmpty() && var1.test(var5)) {
            var2.set(var4, ItemStack.EMPTY);
            var3 = true;
         }
      }

      if (var3) {
         var0.containerMenu.broadcastChanges();
      }
   }

   private static void giveIceBoots(ServerPlayer var0) {
      Registry<Enchantment> var1 = var0.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
      ItemStack var2 = new ItemStack(Items.DIAMOND_BOOTS);
      var2.enchant(var1.wrapAsHolder((Enchantment)var1.getValueOrThrow(Enchantments.FROST_WALKER)), 2);
      var0.addItem(markHexGranted(var2));
      var0.sendSystemMessage(Component.translatable("hexbingo.msg.ice_boots").withStyle(ChatFormatting.AQUA));
   }

   private static void givePumpkins(ServerPlayer var0) {
      Team var1 = team(var0);
      if (var1 != null && var0.level() instanceof ServerLevel var2) {
         MinecraftServer var10 = var2.getServer();
         int var4 = 0;

         for (Team var6 : Bingo.get(var10.overworld()).getTeams()) {
            if (var6 != var1) {
               for (UUID var8 : var6.getPlayers()) {
                  ServerPlayer var9 = var10.getPlayerList().getPlayer(var8);
                  if (var9 != null) {
                     if (shieldBlocks(var9, "snow_pumpkin")) {
                        continue;
                     }

                     PUMPKIN_HEADS.put(var8, var9.getItemBySlot(EquipmentSlot.HEAD).copy());
                     var9.setItemSlot(EquipmentSlot.HEAD, cursedPumpkin(var2));
                     var4++;
                  }
               }
            }
         }

         var0.sendSystemMessage(Component.translatable("hexbingo.msg.pumpkins", new Object[]{var4}).withStyle(ChatFormatting.GOLD));
      }
   }

   private static ItemStack cursedPumpkin(ServerLevel var0) {
      ItemStack var1 = new ItemStack(Items.CARVED_PUMPKIN);
      Registry<Enchantment> var2 = var0.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
      var1.enchant(var2.wrapAsHolder((Enchantment)var2.getValueOrThrow(Enchantments.BINDING_CURSE)), 1);
      return var1;
   }

   public static boolean removePumpkin(ServerPlayer var0, ServerPlayer var1) {
      ItemStack var2 = PUMPKIN_HEADS.remove(var1.getUUID());
      if (var2 == null) {
         return false;
      }

      var1.setItemSlot(EquipmentSlot.HEAD, var2);
      var1.sendSystemMessage(Component.translatable("hexbingo.msg.pumpkin_off").withStyle(ChatFormatting.GREEN));
      var0.sendSystemMessage(Component.translatable("hexbingo.msg.pumpkin_off_other", new Object[]{var1.getDisplayName()}).withStyle(ChatFormatting.GREEN));
      return true;
   }

   public static void restorePumpkin(ServerPlayer var0) {
      ItemStack var1 = PUMPKIN_HEADS.remove(var0.getUUID());
      if (var1 != null) {
         var0.setItemSlot(EquipmentSlot.HEAD, var1);
      }
   }

   public static void enforcePumpkin(ServerPlayer var0) {
      if (PUMPKIN_HEADS.containsKey(var0.getUUID()) && !PUMPKIN_GUARD.get()) {
         if (!var0.getItemBySlot(EquipmentSlot.HEAD).is(Items.CARVED_PUMPKIN)) {
            PUMPKIN_GUARD.set(Boolean.TRUE);

            try {
               var0.setItemSlot(EquipmentSlot.HEAD, cursedPumpkin((ServerLevel)var0.level()));
               if (var0.containerMenu.getCarried().is(Items.CARVED_PUMPKIN)) {
                  var0.containerMenu.setCarried(ItemStack.EMPTY);
               }

               for (int var1 = 0; var1 < var0.getInventory().getContainerSize(); var1++) {
                  ItemStack var2 = var0.getInventory().getItem(var1);
                  if (var2.is(Items.CARVED_PUMPKIN)) {
                     var2.shrink(1);
                     if (var2.isEmpty()) {
                        var0.getInventory().setItem(var1, ItemStack.EMPTY);
                     }
                     break;
                  }
               }

               var0.sendOverlayMessage(Component.translatable("hexbingo.msg.pumpkin_locked").withStyle(ChatFormatting.GRAY));
            } finally {
               PUMPKIN_GUARD.set(Boolean.FALSE);
            }
         }
      }
   }

   public static boolean removePumpkinSelf(ServerPlayer var0) {
      if (!PUMPKIN_HEADS.containsKey(var0.getUUID())) {
         return false;
      }

      Team var1 = team(var0);
      MinecraftServer var2 = var0.level().getServer();
      int var3 = 0;
      if (var1 != null && var2 != null) {
         for (UUID var5 : var1.getPlayers()) {
            if (var2.getPlayerList().getPlayer(var5) != null) {
               var3++;
            }
         }
      }

      if (var3 > 1) {
         var0.sendOverlayMessage(Component.translatable("hexbingo.msg.pumpkin_need_teammate").withStyle(ChatFormatting.GRAY));
         return false;
      }

      ItemStack var6 = PUMPKIN_HEADS.remove(var0.getUUID());
      if (var6 == null) {
         return false;
      }

      var0.setItemSlot(EquipmentSlot.HEAD, var6);
      var0.sendSystemMessage(Component.translatable("hexbingo.msg.pumpkin_self").withStyle(ChatFormatting.GREEN));
      return true;
   }

   private static void giveSpawnEggs(ServerPlayer var0) {
      ArrayList var1 = new ArrayList();

      for (Item var3 : BuiltInRegistries.ITEM) {
         if (var3 instanceof SpawnEggItem) {
            var1.add(var3);
         }
      }

      if (!var1.isEmpty()) {
         ThreadLocalRandom var4 = ThreadLocalRandom.current();

         for (int var5 = 0; var5 < 6; var5++) {
            var0.addItem(new ItemStack((ItemLike)var1.get(var4.nextInt(var1.size())), 1));
         }

         var0.sendSystemMessage(Component.translatable("hexbingo.msg.spawn_eggs").withStyle(ChatFormatting.GOLD));
      }
   }

   private static void ravenMouth(ServerPlayer var0) {
      Parrot var1 = null;
      if (var0.level() instanceof ServerLevel var2) {
         var1 = (Parrot)EntityTypes.PARROT.create(var2, EntitySpawnReason.MOB_SUMMONED);
         if (var1 != null) {
            var1.setPos(var0.getX() + 1.0, var0.getY(), var0.getZ());
            var1.setCustomName(Component.literal("乌鸦嘴"));
            var1.setCustomNameVisible(true);
            var1.setPersistenceRequired();
            var1.setInvulnerable(true);
            setVariant(var1, Parrot.class, "DATA_VARIANT_ID", Variant.GRAY.getId());
            var2.addFreshEntity(var1);
         }
      }

      MinecraftServer var7 = var0.level().getServer();
      if (var7 != null) {
         Bingo var8 = Bingo.get(var7.overworld());
         Team var4 = var8.getTeam(var0);
         if (var4 != null) {
            ThreadLocalRandom var5 = ThreadLocalRandom.current();
            if (var5.nextBoolean() && hasCompleted(var4)) {
               int var9 = randomCompleted(var4);
               uncompleteSlot(var7, var8, var4, var9);
               var0.sendSystemMessage(
                  Component.translatable("hexbingo.msg.raven_bad", new Object[]{var8.task(var9).contentName(var7)}).withStyle(ChatFormatting.DARK_PURPLE)
               );
            } else {
               int var6 = randomIncomplete(var8, var4);
               if (var6 >= 0) {
                  completeTask(var8, var4, var6);
                  var0.sendSystemMessage(
                     Component.translatable("hexbingo.msg.raven_good", new Object[]{var8.task(var6).contentName(var7)}).withStyle(ChatFormatting.LIGHT_PURPLE)
                  );
               }
            }
         }
      }

      if (var1 != null) {
         var1.discard();
      }
   }

   private static void updateFriendAura(ServerPlayer var0, String var1, int var2, HexEffect var3) {
      Team var4 = team(var0);
      if (var4 != null) {
         double var5 = var3.radius > 0.0 ? var3.radius : 8.0;
         int var7 = 0;

         for (UUID var9 : var4.getPlayers()) {
            if (!var9.equals(var0.getUUID())) {
               ServerPlayer var10 = var0.level().getServer().getPlayerList().getPlayer(var9);
               if (var10 != null && var10.level() == var0.level() && var10.distanceToSqr(var0) <= var5 * var5) {
                  var7++;
               }
            }
         }

         AttributeInstance var11 = var0.getAttribute(Attributes.MOVEMENT_SPEED);
         if (var11 != null) {
            if (var7 > 0) {
               var11.addOrUpdateTransientModifier(new AttributeModifier(modifierId(var1, var2), 0.02 * Math.min(var7, 5), Operation.ADD_MULTIPLIED_BASE));
               var0.addEffect(new MobEffectInstance(MobEffects.HASTE, 60, 0, true, false, false));
            } else {
               var11.removeModifier(modifierId(var1, var2));
               var0.removeEffect(MobEffects.HASTE);
            }
         }
      }
   }

   private static void tickFlowersWalk(ServerPlayer var0) {
      if (var0.level() instanceof ServerLevel var1) {
         BlockPos var5 = var0.blockPosition();
         BlockState var3 = var1.getBlockState(var5);
         if (var1.getBlockState(var5.below()).is(Blocks.GRASS_BLOCK)
            && (var3.isAir() || var3.is(Blocks.SHORT_GRASS))
            && ThreadLocalRandom.current().nextDouble() < 0.5) {
            ItemLike[] var4 = new ItemLike[]{
               Blocks.DANDELION,
               Blocks.POPPY,
               Blocks.AZURE_BLUET,
               Blocks.OXEYE_DAISY,
               Blocks.CORNFLOWER,
               Blocks.RED_TULIP,
               Blocks.ORANGE_TULIP,
               Blocks.WHITE_TULIP,
               Blocks.PINK_TULIP
            };
            var1.setBlockAndUpdate(var5, ((Block)var4[ThreadLocalRandom.current().nextInt(var4.length)]).defaultBlockState());
         }
      }
   }

   private static void commonWealth(ServerPlayer var0) {
      MinecraftServer var1 = var0.level().getServer();
      if (var1 != null) {
         ArrayList<ServerPlayer> var2 = new ArrayList<>();

         for (ServerPlayer var4 : var1.getPlayerList().getPlayers()) {
            if (!var4.isSpectator()) {
               var2.add(var4);
            }
         }

         if (var2.isEmpty()) {
            return;
         }

         ArrayList<ItemStack> var9 = new ArrayList<>();

         for (ServerPlayer var5 : var2) {
            for (int var6 = 0; var6 < 36; var6++) {
               ItemStack var7 = var5.getInventory().getItem(var6);
               if (!var7.isEmpty() && !isBoundItem(var7)) {
                  var9.add(var7.copy());
                  var5.getInventory().setItem(var6, ItemStack.EMPTY);
               }
            }

            var5.containerMenu.broadcastChanges();
         }

         Collections.shuffle(var9, ThreadLocalRandom.current());
         int var11 = 0;

         for (ItemStack var14 : var9) {
            if (!var14.isEmpty()) {
               ServerPlayer var16 = (ServerPlayer)var2.get(var11 % var2.size());
               ItemStack var8 = var14.copy();
               if (!var16.getInventory().add(var8)) {
                  var16.drop(var8, false);
               }

               var11++;
            }
         }

         for (ServerPlayer var15 : var2) {
            var15.containerMenu.broadcastChanges();
            var15.sendSystemMessage(Component.translatable("hexbingo.msg.common_wealth").withStyle(ChatFormatting.GOLD));
         }
      }
   }

   private static void chaosShift(ServerPlayer var0) {
      MinecraftServer var1 = var0.level().getServer();
      if (var1 != null) {
         HexGameData var2 = HexGameData.get(var1);
         if (var2.hexesLocked()) {
            var0.sendSystemMessage(Component.translatable("hexbingo.msg.hex_locked").withStyle(ChatFormatting.DARK_GRAY));
            return;
         }

         ArrayList<Hex> var3 = new ArrayList<>();

         for (Hex var5 : HexRegistry.all()) {
            if (!var2.hexes(var0.getUUID()).contains(var5.id())
               && !var5.id().equals(HexRegistry.OFFER_UPGRADE)
               && !var5.id().equals(HexRegistry.ASCETIC)) {
               var3.add(var5);
            }
         }

         if (!var3.isEmpty()) {
            ThreadLocalRandom var8 = ThreadLocalRandom.current();
            ArrayList<Hex> var9 = new ArrayList<>();

            for (int var6 = 0; var6 < 2 && !var3.isEmpty(); var6++) {
               var9.add((Hex)var3.remove(var8.nextInt(var3.size())));
            }

            for (Hex var7 : var9) {
               if (var2.addHex(var0.getUUID(), var7.id())) {
                  var2.markGranted(var0.getUUID(), var7.id(), "transformed");
               }

               apply(var0, var7);
            }

            var0.sendSystemMessage(
               Component.translatable(
                     "hexbingo.msg.chaos",
                     new Object[]{
                        Component.translatable("hexbingo.hex." + ((Hex)var9.get(0)).id() + ".name"),
                        var9.size() > 1 ? Component.translatable("hexbingo.hex." + ((Hex)var9.get(1)).id() + ".name") : Component.empty()
                     }
                  )
                  .withStyle(ChatFormatting.LIGHT_PURPLE)
            );
         }
      }
   }

   private static int safeGroundY(ServerLevel var0, int var1, int var2, int var3) {
      var0.getChunk(var1 >> 4, var2 >> 4);

      for (int var4 = Math.min(var3, var0.getMaxY() - 2); var4 > var0.getMinY() + 1; var4--) {
         BlockState var5 = var0.getBlockState(new BlockPos(var1, var4 - 1, var2));
         if (!var5.isAir()
            && var5.getFluidState().isEmpty()
            && !var5.is(Blocks.BEDROCK)
            && !var5.is(BlockTags.LEAVES)
            && var0.getBlockState(new BlockPos(var1, var4, var2)).isAir()
            && var0.getBlockState(new BlockPos(var1, var4 + 1, var2)).isAir()) {
            return var4;
         }
      }

      return -1;
   }

   private static void startNetherStart(ServerPlayer var0) {
      MinecraftServer var1 = var0.level().getServer();
      if (var1 != null) {
         ServerLevel var2 = var1.getLevel(Level.NETHER);
         if (var2 != null) {
            int var3 = Mth.floor(var0.getX() / 8.0);
            int var4 = Mth.floor(var0.getZ() / 8.0);
            int var5 = safeGroundY(var2, var3, var4, 120);
            if (var5 < 0) {
               var5 = 100;
            }

            for (int var6 = -1; var6 <= 1; var6++) {
               for (int var7 = -1; var7 <= 1; var7++) {
                  var2.setBlockAndUpdate(new BlockPos(var3 + var6, var5 - 1, var4 + var7), Blocks.COBBLESTONE.defaultBlockState());
                  var2.setBlockAndUpdate(new BlockPos(var3 + var6, var5, var4 + var7), Blocks.AIR.defaultBlockState());
                  var2.setBlockAndUpdate(new BlockPos(var3 + var6, var5 + 1, var4 + var7), Blocks.AIR.defaultBlockState());
               }
            }

            var0.teleportTo(var2, var3 + 0.5, var5, var4 + 0.5, Set.of(), var0.getYRot(), var0.getXRot(), false);
            NETHER_SENT.add(var0.getUUID());
            NETHER_ESCAPED.remove(var0.getUUID());
            var0.sendSystemMessage(Component.translatable("hexbingo.msg.nether_start").withStyle(ChatFormatting.RED));
         }
      }
   }

   public static boolean isNetherLocked(ServerPlayer var0) {
      return hasHex(var0, "nether_start") && NETHER_SENT.contains(var0.getUUID()) && !NETHER_ESCAPED.contains(var0.getUUID());
   }

   private static void randomBingo(ServerPlayer var0) {
      MinecraftServer var1 = var0.level().getServer();
      if (var1 != null) {
         Bingo var2 = Bingo.get(var1.overworld());
         Team var3 = var2.getTeam(var0);
         if (var3 != null) {
            int var4 = -1;
            if (ThreadLocalRandom.current().nextDouble() < 0.85 && hasCompleted(var3)) {
               var4 = randomCompleted(var3);
            } else {
               var4 = randomIncomplete(var2, var3);
               if (var4 >= 0) {
                  completeTask(var2, var3, var4);
               }
            }

            if (var4 < 0) {
               var4 = randomCompleted(var3);
            }

            if (var4 >= 0) {
               var0.sendSystemMessage(
                  Component.translatable("hexbingo.msg.random_bingo", new Object[]{var2.task(var4).contentName(var1)}).withStyle(ChatFormatting.LIGHT_PURPLE)
               );
            } else {
               var0.sendSystemMessage(Component.translatable("hexbingo.msg.random_bingo_none").withStyle(ChatFormatting.GRAY));
            }
         }
      }
   }

   private static void teleportToStructure(ServerPlayer var0, ServerLevel var5, TagKey<Structure> var1, String var2, String var3, String var4) {
      BlockPos var6 = var5.findNearestMapStructure(var1, var0.blockPosition(), 100, false);
      if (var6 == null) {
         if (!generateStructure(var0, var5, var4, var2)) {
            var0.sendSystemMessage(Component.translatable(var3).withStyle(ChatFormatting.GRAY));
         }
      } else {
         int var7 = var5.getHeight(Types.MOTION_BLOCKING_NO_LEAVES, var6.getX(), var6.getZ());
         int var8 = safeGroundY(var5, var6.getX(), var6.getZ(), var7 + 2);
         if (var8 < 0) {
            // 水下结构（沉船/海底废弃门）找不到干燥落脚点：直接传送到水面上方
            boolean var10 = !var5.getBlockState(new BlockPos(var6.getX(), var7 - 1, var6.getZ())).getFluidState().isEmpty();
            if (var10) {
               int var9 = var7 + 1;
               var0.teleportTo(var5, var6.getX() + 0.5, var9, var6.getZ() + 0.5, Set.of(), var0.getYRot(), var0.getXRot(), false);
               var0.resetFallDistance();
               var0.sendSystemMessage(Component.translatable(var2).withStyle(ChatFormatting.GOLD));
            } else if (!generateStructure(var0, var5, var4, var2)) {
               var0.sendSystemMessage(Component.translatable(var3).withStyle(ChatFormatting.GRAY));
            }
         } else {
            var0.teleportTo(var5, var6.getX() + 0.5, var8, var6.getZ() + 0.5, Set.of(), var0.getYRot(), var0.getXRot(), false);
            var0.sendSystemMessage(Component.translatable(var2).withStyle(ChatFormatting.GOLD));
         }
      }
   }

   private static boolean generateStructure(ServerPlayer var0, ServerLevel var1, String var2, String var3) {
      BlockPos var4 = var2.equals("minecraft:shipwreck") ? generateShipwreckAt(var1, var0, 250) : generateStructureAt(var1, var0, var2, 200);
      if (var4 == null) {
         return false;
      } else {
         int var5 = safeGroundY(var1, var4.getX(), var4.getZ(), var4.getY() + 3);
         if (var5 < 0) {
            var5 = var4.getY() + 1;
         }

         var0.teleportTo(var1, var4.getX() + 0.5, var5, var4.getZ() + 0.5, Set.of(), var0.getYRot(), var0.getXRot(), false);
         var0.sendSystemMessage(Component.translatable(var3).withStyle(ChatFormatting.GOLD));
         return true;
      }
   }

   // 致敬爱情兜底：沉船只在海洋/沙滩生物群系可放置，故由近及远搜寻合适位置再生成
   private static BlockPos generateShipwreckAt(ServerLevel var2, ServerPlayer var0, int var1) {
      MinecraftServer var3 = var2.getServer();
      if (var3 == null) {
         return null;
      }

      int var4 = var0.getBlockX();
      int var5 = var0.getBlockZ();

      for (int var6 = 8; var6 <= var1; var6 += 8) {
         int var7 = Math.max(12, var6 / 2);

         for (int var8 = 0; var8 < var7; var8++) {
            double var9 = (double)var8 * Math.PI * 2.0 / (double)var7;
            int var10 = var4 + (int)Math.round(Math.cos(var9) * (double)var6);
            int var11 = var5 + (int)Math.round(Math.sin(var9) * (double)var6);
            Holder<Biome> var12 = var2.getBiome(new BlockPos(var10, 64, var11));
            boolean var13 = var12.is(BiomeTags.IS_OCEAN);
            boolean var14 = var12.is(BiomeTags.IS_BEACH);
            if (!var13 && !var14) {
               continue;
            }

            var2.getChunk(var10 >> 4, var11 >> 4);
            int var15 = var2.getHeight(Types.MOTION_BLOCKING_NO_LEAVES, var10, var11);
            BlockPos var16 = new BlockPos(var10, var15, var11);
            String var17 = var13 ? "minecraft:shipwreck" : "minecraft:shipwreck_beached";
            CommandSourceStack var18 = var3.createCommandSourceStack().withLevel(var2).withPosition(Vec3.atCenterOf(var16)).withSuppressedOutput();

            int var19;
            try {
               var19 = var3.getCommands().getDispatcher().execute("place structure " + var17 + " " + var10 + " " + var15 + " " + var11, var18);
            } catch (CommandSyntaxException var20) {
               var19 = 0;
            }

            if (var19 > 0) {
               return var16;
            }
         }
      }

      return null;
   }

   // 种子礼包：随机种子 1-8 个
   private static void giveSeedGift(ServerPlayer var0) {
      Item[] var1 = new Item[]{Items.WHEAT_SEEDS, Items.CARROT, Items.POTATO, Items.BEETROOT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS};
      ThreadLocalRandom var2 = ThreadLocalRandom.current();
      int var3 = 0;

      for (int var4 = 0; var4 < var1.length; var4++) {
         if (var2.nextDouble() < 0.75) {
            int var5 = 1 + var2.nextInt(8);
            var0.addItem(new ItemStack(var1[var4], var5));
            var3 += var5;
         }
      }

      if (var3 == 0) {
         var3 = 1 + var2.nextInt(8);
         var0.addItem(new ItemStack(Items.WHEAT_SEEDS, var3));
      }

      var0.sendSystemMessage(
         Component.translatable("hexbingo.msg.seed_gift", new Object[]{Component.literal(Integer.toString(var3))}).withStyle(ChatFormatting.GOLD)
      );
   }

   // 考古刷：不可损坏刷子
   private static void giveBrush(ServerPlayer var0) {
      ItemStack var1 = new ItemStack(Items.BRUSH);
      var1.set(DataComponents.UNBREAKABLE, Unit.INSTANCE);
      var0.addItem(var1);
      var0.sendSystemMessage(Component.translatable("hexbingo.msg.brush_gift").withStyle(ChatFormatting.GOLD));
   }

   // 免疫护盾：消耗一次护盾并进入 60 秒冷却
   private static boolean shieldBlocks(ServerPlayer var0, String var1) {
      if (!hasHex(var0, "immunity_shield")) {
         return false;
      }

      long var2 = System.currentTimeMillis();
      Long var4 = SHIELD_READY.get(var0.getUUID());
      if (var4 != null && var2 < var4) {
         return false;
      }

      SHIELD_READY.put(var0.getUUID(), var2 + SHIELD_COOLDOWN_MS);
      var0.sendSystemMessage(
         Component.translatable("hexbingo.msg.shield_block", new Object[]{Component.translatable("hexbingo.hex." + var1 + ".name")})
            .withStyle(ChatFormatting.AQUA)
      );
      return true;
   }

   // 九九八十一难：定位或生成试炼大厅并安全传送
   private static void teleportTrialChambers(ServerPlayer var0) {
      MinecraftServer var6 = var0.level().getServer();
      ServerLevel var1 = var6 == null ? null : var6.overworld();
      if (var1 == null) {
         return;
      }

      BlockPos var2 = var1.findNearestMapStructure(TRIAL_CHAMBERS, var0.blockPosition(), 100, false);
      if (var2 != null) {
         BlockPos var3 = findUndergroundSpot(var1, var2);
         if (var3 != null) {
            teleportUnderground(var0, var1, var3);
            var0.sendSystemMessage(Component.translatable("hexbingo.msg.trial_chambers").withStyle(ChatFormatting.GOLD));
            return;
         }
      }

      BlockPos var4 = generateTrialChambers(var1, var0, 200);
      if (var4 == null) {
         var0.sendSystemMessage(Component.translatable("hexbingo.msg.trial_chambers_fail").withStyle(ChatFormatting.GRAY));
      } else {
         BlockPos var5 = findUndergroundSpot(var1, var4);
         if (var5 == null) {
            var5 = carveSafeSpot(var1, var4);
         }

         teleportUnderground(var0, var1, var5);
         var0.sendSystemMessage(Component.translatable("hexbingo.msg.trial_chambers_gen").withStyle(ChatFormatting.GOLD));
      }
   }

   private static void teleportUnderground(ServerPlayer var0, ServerLevel var1, BlockPos var2) {
      var0.teleportTo(var1, var2.getX() + 0.5, var2.getY(), var2.getZ() + 0.5, Set.of(), var0.getYRot(), var0.getXRot(), false);
      var0.resetFallDistance();
      var0.setRemainingFireTicks(0);
   }

   private static BlockPos findUndergroundSpot(ServerLevel var0, BlockPos var1) {
      int var2 = var0.getMinY() + 1;
      int[] var4 = new int[]{16, 32};

      for (int var5 : var4) {
         for (int var6 = -var5; var6 <= var5; var6 += 2) {
            for (int var7 = -var5; var7 <= var5; var7 += 2) {
               int var8 = var1.getX() + var6;
               int var9 = var1.getZ() + var7;
               var0.getChunk(var8 >> 4, var9 >> 4);

               // 试炼大厅生成在地底，从 Y=-5 向下找「脚下凝灰岩/铜地板 + 头顶两格空气」的内部落脚点
               for (int var12 = -5; var12 >= var2; var12--) {
                  BlockPos var13 = new BlockPos(var8, var12, var9);
                  if (var0.getBlockState(var13).isAir() && var0.getBlockState(var13.above()).isAir()) {
                     BlockState var14 = var0.getBlockState(var13.below());
                     if (isTrialFloor(var14)) {
                        return var13;
                     }
                  }
               }
            }
         }
      }

      return null;
   }

   private static boolean isTrialFloor(BlockState var0) {
      String var1 = var0.getBlock().getDescriptionId();
      return var1.contains("tuff") || var1.contains("copper");
   }

   private static BlockPos carveSafeSpot(ServerLevel var0, BlockPos var1) {
      for (int var2 = -1; var2 <= 1; var2++) {
         for (int var3 = -1; var3 <= 1; var3++) {
            var0.setBlockAndUpdate(new BlockPos(var1.getX() + var2, var1.getY() - 1, var1.getZ() + var3), Blocks.COBBLESTONE.defaultBlockState());
            var0.setBlockAndUpdate(new BlockPos(var1.getX() + var2, var1.getY(), var1.getZ() + var3), Blocks.AIR.defaultBlockState());
            var0.setBlockAndUpdate(new BlockPos(var1.getX() + var2, var1.getY() + 1, var1.getZ() + var3), Blocks.AIR.defaultBlockState());
         }
      }

      return var1;
   }

   private static BlockPos generateTrialChambers(ServerLevel var2, ServerPlayer var0, int var1) {
      MinecraftServer var3 = var2.getServer();
      if (var3 == null) {
         return null;
      }

      ThreadLocalRandom var4 = ThreadLocalRandom.current();

      for (int var5 = 0; var5 < 8; var5++) {
         double var6 = var4.nextDouble() * Math.PI * 2.0;
         int var8 = var0.getBlockX() + (int)Math.round(Math.cos(var6) * (double)var1);
         int var9 = var0.getBlockZ() + (int)Math.round(Math.sin(var6) * (double)var1);
         var2.getChunk(var8 >> 4, var9 >> 4);
         int var11 = -40;
         BlockPos var12 = new BlockPos(var8, var11, var9);
         CommandSourceStack var13 = var3.createCommandSourceStack().withLevel(var2).withPosition(Vec3.atCenterOf(var12)).withSuppressedOutput();
         int var14;

         try {
            var14 = var3.getCommands()
               .getDispatcher()
               .execute("place structure minecraft:trial_chambers " + var8 + " " + var11 + " " + var9, var13);
         } catch (CommandSyntaxException var17) {
            var14 = 0;
         }

         if (var14 > 0) {
            return var12;
         }
      }

      return null;
   }

   private static BlockPos generateStructureAt(ServerLevel var3, ServerPlayer var0, String var1, int var2) {
      MinecraftServer var4 = var3.getServer();
      if (var4 == null) {
         return null;
      } else {
         ThreadLocalRandom var5 = ThreadLocalRandom.current();

         for (int var6 = 0; var6 < 8; var6++) {
            double var7 = var5.nextDouble() * Math.PI * 2.0;
            int var9 = var0.getBlockX() + (int)Math.round(Math.cos(var7) * (double)var2);
            int var10 = var0.getBlockZ() + (int)Math.round(Math.sin(var7) * (double)var2);
            var3.getChunk(var9 >> 4, var10 >> 4);
            int var11 = var3.getHeight(Types.MOTION_BLOCKING_NO_LEAVES, var9, var10);
            BlockPos var12 = new BlockPos(var9, var11, var10);
            String var13 = structureForBiome(var1, var3, var12);
            CommandSourceStack var14 = var4.createCommandSourceStack().withLevel(var3).withPosition(Vec3.atCenterOf(var12)).withSuppressedOutput();
            int var15;
            try {
               var15 = var4.getCommands()
                  .getDispatcher()
                  .execute("place structure " + var13 + " " + var9 + " " + var11 + " " + var10, var14);
            } catch (CommandSyntaxException var18) {
               var15 = 0;
            }

            if (var15 > 0) {
               return var12;
            }
         }

         return null;
      }
   }

   private static String structureForBiome(String var0, ServerLevel var1, BlockPos var2) {
      String var3 = var1.getBiome(var2).unwrapKey().map(var4 -> var4.identifier().getPath()).orElse("");
      if (var0.endsWith("village")) {
         if (var3.contains("desert")) {
            return "minecraft:village_desert";
         } else if (var3.contains("savanna")) {
            return "minecraft:village_savanna";
         } else if (var3.contains("snowy")) {
            return "minecraft:village_snowy";
         } else if (var3.contains("taiga")) {
            return "minecraft:village_taiga";
         } else {
            return "minecraft:village_plains";
         }
      } else if (var0.endsWith("ruined_portal")) {
         if (var1.dimension() == Level.NETHER) {
            return "minecraft:ruined_portal_nether";
         } else if (var3.contains("desert")) {
            return "minecraft:ruined_portal_desert";
         } else if (var3.contains("jungle")) {
            return "minecraft:ruined_portal_jungle";
         } else if (var3.contains("swamp")) {
            return "minecraft:ruined_portal_swamp";
         } else if (var3.contains("ocean")) {
            return "minecraft:ruined_portal_ocean";
         } else if (var3.contains("peak") || var3.contains("mountain") || var3.contains("windswept")) {
            return "minecraft:ruined_portal_mountain";
         } else {
            return "minecraft:ruined_portal";
         }
      } else if (var0.endsWith("fortress")) {
         return "minecraft:fortress";
      } else {
         return var3.contains("ocean") || var3.contains("river") ? "minecraft:shipwreck" : "minecraft:shipwreck_beached";
      }
   }

   private static void ancientRuins(ServerPlayer var0) {
      if (var0.level() instanceof ServerLevel var1) {
         ThreadLocalRandom var2 = ThreadLocalRandom.current();
         String[] var3 = new String[]{
            "archaeology/desert_pyramid",
            "archaeology/desert_well",
            "archaeology/ocean_ruin_cold",
            "archaeology/ocean_ruin_warm",
            "archaeology/trail_ruins_common",
            "archaeology/trail_ruins_rare"
         };
         int var4 = 4 + var2.nextInt(12);
         int var5 = 0;

         for (int var6 = 0; var6 < var4; var6++) {
            BlockPos var7 = var0.blockPosition().offset(var2.nextInt(11) - 5, var2.nextInt(3) - 1, var2.nextInt(11) - 5);

            for (int var8 = 0; var8 < 6 && !var1.getBlockState(var7).isAir(); var8++) {
               var7 = var0.blockPosition().offset(var2.nextInt(11) - 5, var2.nextInt(3) - 1, var2.nextInt(11) - 5);
            }

            Block var9 = var2.nextBoolean() ? Blocks.SUSPICIOUS_GRAVEL : Blocks.SUSPICIOUS_SAND;
            if (var1.setBlockAndUpdate(var7, var9.defaultBlockState()) && var1.getBlockEntity(var7) instanceof BrushableBlockEntity var10) {
               ResourceKey<LootTable> var11 = ResourceKey.create(
                  Registries.LOOT_TABLE, Identifier.fromNamespaceAndPath("minecraft", var3[var2.nextInt(var3.length)])
               );
               var10.setLootTable(var11, var2.nextLong());
               var10.setChanged();
               var5++;
            }
         }

         var0.sendSystemMessage(Component.translatable("hexbingo.msg.ancient_ruins", new Object[]{var5}).withStyle(ChatFormatting.GOLD));
      }
   }

   private static void startVoyage(ServerPlayer var0) {
      if (var0.level() instanceof ServerLevel var1) {
         double var9 = var0.getX();
         double var4 = var0.getZ();
         var0.teleportTo(var1, var9, 256.0, var4, Set.of(), var0.getYRot(), var0.getXRot(), false);
         BlockPos var6 = BlockPos.containing(var9, 255.0, var4);

         for (int var7 = -1; var7 <= 1; var7++) {
            for (int var8 = -1; var8 <= 1; var8++) {
               var1.setBlockAndUpdate(var6.offset(var7, 0, var8), Blocks.STONE.defaultBlockState());
               var1.setBlockAndUpdate(var6.offset(var7, 1, var8), Blocks.AIR.defaultBlockState());
               var1.setBlockAndUpdate(var6.offset(var7, 2, var8), Blocks.AIR.defaultBlockState());
            }
         }

         var0.addItem(new ItemStack(Items.WATER_BUCKET));
         VOYAGE_ACTIVE.add(var0.getUUID());
         Team var10 = team(var0);
         VOYAGE_LOCK_MASK.put(var0.getUUID(), var10 == null ? 0 : lockedMask(var10));
         var0.sendSystemMessage(Component.translatable("hexbingo.msg.voyage_start").withStyle(ChatFormatting.AQUA));
      }
   }

   private static void tickVoyage(ServerPlayer var0) {
      if (VOYAGE_ACTIVE.contains(var0.getUUID()) && var0.getY() < 250.0 && (var0.onGround() || var0.isInWater())) {
         VOYAGE_ACTIVE.remove(var0.getUUID());
         if (!var0.isInWater() && var0.isAlive()) {
            var0.hurtServer(var0.level(), var0.damageSources().genericKill(), Float.MAX_VALUE);
            var0.sendSystemMessage(Component.translatable("hexbingo.msg.voyage_fail").withStyle(ChatFormatting.RED));
         } else {
            int var2 = completeRandomTasks(var0, 1);
            VOYAGE_LOCK_MASK.remove(var0.getUUID());
            releaseSlotLock(var0, var2);
            var0.sendSystemMessage(Component.translatable("hexbingo.msg.voyage_done").withStyle(ChatFormatting.AQUA));
         }
      }
   }

   public static boolean isVoyageActive(ServerPlayer var0) {
      return VOYAGE_ACTIVE.contains(var0.getUUID());
   }

   public static void onVoyageDeath(ServerPlayer var0) {
      VOYAGE_ACTIVE.remove(var0.getUUID());
   }

   private static int lockedMask(Team var0) {
      int var1 = 0;

      for (int var2 = 0; var2 < 25; var2++) {
         if (var0.locked(var2)) {
            var1 |= 1 << var2;
         }
      }

      return var1;
   }

   /** 非占点模式下，星际航行完成格子后清除所有队伍对该格的锁定，避免其它队伍无法完成。 */
   private static void releaseSlotLock(ServerPlayer var0, int var1) {
      if (var1 < 0) {
         return;
      }

      MinecraftServer var2 = var0.level().getServer();
      if (var2 == null) {
         return;
      }

      Bingo var3 = Bingo.get(var2.overworld());
      if (var3.getSettings().game().lockout() || var3.getSettings().game().winCondition() == WinCondition.LOCK) {
         return;
      }

      for (Team var5 : var3.getTeams()) {
         if (var5.locked(var1)) {
            ArrayList<Integer> var6 = new ArrayList<>();

            for (int var7 = 0; var7 < var3.tasks().size(); var7++) {
               if (var7 != var1 && var5.locked(var7)) {
                  var6.add(var7);
               }
            }

            var5.resetLocked();

            for (int var7 : var6) {
               var5.lock(var7);
            }
         }
      }
   }

   /** 星际航行失败摔死时，撤销死亡导致的随机格子锁定，避免队友无法完成。 */
   public static void restoreVoyageDeathLock(ServerPlayer var0) {
      Integer var1 = VOYAGE_LOCK_MASK.remove(var0.getUUID());
      if (var1 == null) {
         return;
      }

      MinecraftServer var2 = var0.level().getServer();
      if (var2 == null) {
         return;
      }

      Bingo var3 = Bingo.get(var2.overworld());
      Team var4 = var3.getTeam(var0);
      if (var4 == null) {
         return;
      }

      int var5 = lockedMask(var4);
      int var6 = var5 & ~var1;
      if (var6 == 0) {
         return;
      }

      var4.resetLocked();

      for (int var7 = 0; var7 < 25; var7++) {
         if ((var5 & 1 << var7) != 0 && (var6 & 1 << var7) == 0) {
            var4.lock(var7);
         }
      }
   }

   public static void markEye(ServerPlayer var0) {
      EYE_MARK.put(var0.getUUID(), System.currentTimeMillis() + 1500L);
   }

   public static boolean hasEyeMark(ServerPlayer var0) {
      Long var1 = EYE_MARK.get(var0.getUUID());
      return var1 != null && System.currentTimeMillis() < var1;
   }

   public static void makeEyeSurvive(EyeOfEnder var0) {
      try {
         Field var1 = EyeOfEnder.class.getDeclaredField("surviveAfterDeath");
         var1.setAccessible(true);
         var1.setBoolean(var0, true);
      } catch (Exception var2) {
      }
   }

   private static void summonBees(ServerPlayer var0, int var1) {
      if (var0.level() instanceof ServerLevel var2) {
         Team var9 = team(var0);
         ArrayList var4 = new ArrayList();

         for (ServerPlayer var6 : var2.players()) {
            if (var6 != var0 && !var6.isSpectator() && (var9 == null || !var9.hasPlayer(var6)) && !shieldBlocks(var6, "bees")) {
               var4.add(var6);
            }
         }

         if (!var4.isEmpty()) {
            ThreadLocalRandom var10 = ThreadLocalRandom.current();

            for (int var11 = 0; var11 < var1; var11++) {
               ServerPlayer var7 = (ServerPlayer)var4.get(var11 % var4.size());
               Bee var8 = (Bee)EntityTypes.BEE.create(var2, EntitySpawnReason.MOB_SUMMONED);
               if (var8 != null) {
                  var8.setPos(var7.getX() + var10.nextDouble() * 4.0 - 2.0, var7.getY() + 1.0, var7.getZ() + var10.nextDouble() * 4.0 - 2.0);
                  var2.addFreshEntity(var8);
                  var8.setLastHurtByMob(var7);
                  var8.setTarget(var7);
                  var8.setPersistentAngerTarget(EntityReference.of(var7));
                  var8.setPersistentAngerEndTime(var2.getGameTime() + 1200L);
                  var8.startPersistentAngerTimer();
                  HEX_BEES.add(var8.getUUID());
                  BEE_TARGETS.put(var8.getUUID(), var7.getUUID());
               }
            }
         }
      }
   }

   public static boolean isHexBee(Bee var0) {
      return HEX_BEES.contains(var0.getUUID());
   }

   /** 每秒重新指定蜜蜂的仇人，防止蜜蜂丢失仇恨目标。 */
   private static void tickHexBees(MinecraftServer var0) {
      if (BEE_TARGETS.isEmpty()) {
         return;
      }

      Iterator<Map.Entry<UUID, UUID>> var1 = BEE_TARGETS.entrySet().iterator();

      while (var1.hasNext()) {
         Map.Entry<UUID, UUID> var2 = var1.next();
         ServerPlayer var3 = var0.getPlayerList().getPlayer(var2.getValue());
         if (var3 == null || !var3.isAlive() || var3.isSpectator()) {
            HEX_BEES.remove(var2.getKey());
            var1.remove();
            continue;
         }

         Entity var4 = null;

         for (ServerLevel var6 : var0.getAllLevels()) {
            var4 = var6.getEntity(var2.getKey());
            if (var4 != null) {
               break;
            }
         }

         if (!(var4 instanceof Bee var7) || !var7.isAlive()) {
            HEX_BEES.remove(var2.getKey());
            var1.remove();
         } else {
            if (var7.getTarget() != var3) {
               var7.setTarget(var3);
            }

            var7.setPersistentAngerTarget(EntityReference.of(var3));
            var7.setPersistentAngerEndTime(var7.level().getGameTime() + 1200L);
         }
      }
   }

   private static void rerollHexes(ServerPlayer var0) {
      MinecraftServer var1 = var0.level().getServer();
      if (var1 != null && GRANT_DEPTH.get() < 4) {
         HexGameData var2 = HexGameData.get(var1);
         if (var2.hexesLocked()) {
            var0.sendSystemMessage(Component.translatable("hexbingo.msg.hex_locked").withStyle(ChatFormatting.DARK_GRAY));
            return;
         }

         if (var2.mayhem()) {
            UUID var3 = var0.getUUID();
            ArrayList<String> var4 = new ArrayList<>(var2.hexes(var3));
            if (!var4.isEmpty()) {
               boolean var5 = var4.contains("extra_choice_2");
               boolean var6 = var4.contains("quality_up_3");
               clearAll(var0);
               var2.hexes(var3).clear();
               var2.clearGranted(var3);
               var2.setDirty();
               ArrayList<Hex> var7 = new ArrayList<>();
               var7.addAll(HexRegistry.byColor(HexColor.SILVER));
               var7.addAll(HexRegistry.byColor(HexColor.GOLD));
               var7.addAll(HexRegistry.byColor(HexColor.PRISMATIC));
               var7.removeIf(
                  var1x -> var1x.id().equals("pandora")
                     || var1x.id().equals(HexRegistry.OFFER_UPGRADE)
                     || var1x.id().equals(HexRegistry.ASCETIC)
                     || var4.contains(var1x.id())
               );
               GRANT_DEPTH.set(GRANT_DEPTH.get() + 1);
               ArrayList<Component> var8 = new ArrayList<>();

               try {
                  ThreadLocalRandom var9 = ThreadLocalRandom.current();

                  for (String var11 : var4) {
                     if (var7.isEmpty()) {
                        break;
                     }

                     Hex var12 = (Hex)var7.remove(var9.nextInt(var7.size()));
                     if (var2.addHex(var3, var12.id())) {
                        var2.markGranted(var3, var12.id(), "pandora");
                     }

                     List<HexEffect> var13 = var12.effects();

                     for (int var14 = 0; var14 < var13.size(); var14++) {
                        applyImmediate(var0, var12.id(), var14, (HexEffect)var13.get(var14));
                     }

                     Hex var21 = HexRegistry.byId(var11);
                     MutableComponent var15 = var21 == null ? Component.literal(var11) : Component.translatable("hexbingo.hex." + var11 + ".name");
                     var8.add(
                        Component.translatable(
                              "hexbingo.msg.pandora_changed", new Object[]{var15, Component.translatable("hexbingo.hex." + var12.id() + ".name")}
                           )
                           .withStyle(ChatFormatting.GRAY)
                     );
                  }
               } finally {
                  GRANT_DEPTH.set(GRANT_DEPTH.get() - 1);
               }

               if (var5) {
                  var2.clearExtraChoice(var3);
               }

               if (var6) {
                  var2.clearQualityUp(var3);
               }

               var0.sendSystemMessage(Component.translatable("hexbingo.msg.rerolled").withStyle(ChatFormatting.LIGHT_PURPLE));

               for (Component var20 : var8) {
                  var0.sendSystemMessage(var20);
               }
            }
         }
      }
   }

   private static void updateHomeguard(ServerPlayer var0) {
      HexEffects.HomeguardState var1 = HOMEGUARD.computeIfAbsent(var0.getUUID(), var0x -> new HexEffects.HomeguardState());
      double var2 = var0.getX();
      double var4 = var0.getZ();
      if (!var1.initialized) {
         var1.lastX = var2;
         var1.lastZ = var4;
         var1.initialized = true;
      } else {
         double var6 = Math.hypot(var2 - var1.lastX, var4 - var1.lastZ);
         var1.lastX = var2;
         var1.lastZ = var4;
         if (var6 > 0.03) {
            var1.idleTicks = 0;
            if (var1.runTicks == 0) {
               var1.amplifier = 1;
            }

            var1.runTicks++;
            if (var1.runTicks % 200 == 0 && var1.amplifier < 4) {
               var1.amplifier++;
            }
         } else {
            var1.idleTicks++;
            if (var1.idleTicks >= 4) {
               var1.runTicks = 0;
               var1.amplifier = 0;
            }
         }

         if (var1.amplifier > 0) {
            var0.addEffect(new MobEffectInstance(MobEffects.SPEED, 10, var1.amplifier - 1, true, false, false));
         }
      }
   }

   public static void tickHomeguard(MinecraftServer var0) {
      for (ServerPlayer var2 : var0.getPlayerList().getPlayers()) {
         if (hasHex(var2, "homeguard")) {
            updateHomeguard(var2);
         }
      }
   }

   public static void snowballDamage(LivingIncomingDamageEvent var0) {
      if (var0.getSource().getDirectEntity() instanceof Snowball var1 && var1.getOwner() instanceof ServerPlayer var2) {
         float var9 = 0.0F;

         for (Hex var5 : hexes(var2)) {
            for (HexEffect var7 : var5.effects()) {
               if (var7.type.equals("snowball_damage")) {
                  var9 += (float)var7.amount;
               }
            }
         }

         if (var9 > 0.0F) {
            var0.setAmount(var0.getAmount() + var9);
         }
      }
   }

   private static void startInventoryTransform(ServerPlayer var0, int var1) {
      UUID var2 = var0.getUUID();
      if (!TRANSFORMS.containsKey(var2)) {
         ArrayList var3 = new ArrayList();

         for (int var4 = 0; var4 < 36; var4++) {
            var3.add(var0.getInventory().getItem(var4).copy());
         }

         ThreadLocalRandom var7 = ThreadLocalRandom.current();

         for (int var5 = 0; var5 < 36; var5++) {
            Item var6 = RANDOM_ITEMS.get(var7.nextInt(RANDOM_ITEMS.size()));
            var0.getInventory().setItem(var5, markHexGranted(new ItemStack(var6, 1 + var7.nextInt(3))));
         }

         TRANSFORMS.put(var2, new HexEffects.TransformState(var3, System.currentTimeMillis() + Math.max(1, var1) * 1000L));
         var0.containerMenu.broadcastChanges();
      }
   }

   private static void tickInventoryTransform(ServerPlayer var0) {
      HexEffects.TransformState var1 = TRANSFORMS.get(var0.getUUID());
      if (var1 != null && System.currentTimeMillis() >= var1.until) {
         restoreTransform(var0);
      }
   }

   public static void restoreTransform(ServerPlayer var0) {
      HexEffects.TransformState var1 = TRANSFORMS.remove(var0.getUUID());
      if (var1 != null) {
         for (int var2 = 0; var2 < var1.snapshot.size(); var2++) {
            var0.getInventory().setItem(var2, var1.snapshot.get(var2));
         }

         var0.containerMenu.broadcastChanges();
      }
   }

   private static void enchantGear(ServerPlayer var0) {
      Registry<Enchantment> var1 = var0.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
      List<Holder<Enchantment>> var2 = var1.entrySet().stream().map(var1x -> var1.wrapAsHolder((Enchantment)var1x.getValue())).toList();
      if (!var2.isEmpty()) {
         ThreadLocalRandom var3 = ThreadLocalRandom.current();

         for (int var4 = 0; var4 < 36; var4++) {
            ItemStack var5 = var0.getInventory().getItem(var4);
            if (!var5.isEmpty() && var5.isDamageableItem()) {
               int var6 = 1 + var3.nextInt(5);

               for (int var7 = 0; var7 < var6; var7++) {
                  var5.enchant((Holder)var2.get(var3.nextInt(var2.size())), 1 + var3.nextInt(3));
               }
            }
         }

         var0.containerMenu.broadcastChanges();
      }
   }

   private static void summonPet(ServerPlayer var0) {
      PET_DISMISSED.remove(var0.getUUID());
      // 先清理旧宠物，避免传送后重复生成
      TamableAnimal var2 = PETS.remove(var0.getUUID());
      if (var2 != null) {
         var2.discard();
      }

      if (var0.level() instanceof ServerLevel var1) {
         ThreadLocalRandom var4 = ThreadLocalRandom.current();
         TamableAnimal var3 = var4.nextBoolean()
            ? (TamableAnimal)EntityTypes.CAT.create(var1, EntitySpawnReason.MOB_SUMMONED)
            : (TamableAnimal)EntityTypes.WOLF.create(var1, EntitySpawnReason.MOB_SUMMONED);
         if (var3 != null) {
            var3.setPos(var0.getX() + var4.nextDouble() * 2.0 - 1.0, var0.getY(), var0.getZ() + var4.nextDouble() * 2.0 - 1.0);
            var3.setTame(true, true);
            var3.setOwner(var0);
            var3.setInvulnerable(true);
            var3.setPersistenceRequired();
            var3.setCustomName(Component.literal("无敌小宠物"));
            var3.setCustomNameVisible(true);
            var1.addFreshEntity(var3);
            PETS.put(var0.getUUID(), var3);
         }
      }
   }

   private static boolean isProtectedPet(Entity var0) {
      if (!(var0 instanceof TamableAnimal)) {
         return false;
      }

      if (PETS.containsValue(var0)) {
         return true;
      }

      Component var1 = var0.getCustomName();
      return var1 != null && "无敌小宠物".equals(var1.getString());
   }

   private static void tickPet(ServerPlayer var0, HexEffect var1) {
      if (PET_DISMISSED.contains(var0.getUUID())) {
         return;
      }

      TamableAnimal var2 = PETS.get(var0.getUUID());
      if (var2 == null) {
         summonPet(var0);
      } else if (!var2.isAlive()) {
         // 已被 /kill 等方式杀死：不再自动补发
         PETS.remove(var0.getUUID());
         PET_DISMISSED.add(var0.getUUID());
         return;
      } else if (var2.level() != var0.level() || var2.distanceToSqr(var0) > 1024.0) {
         // 玩家传送或跨维度后把宠物一并带过去，避免在旧位置遗留后再生成第二只
         var2.teleportTo((ServerLevel)var0.level(), var0.getX(), var0.getY(), var0.getZ(), Set.of(), var0.getYRot(), var0.getXRot(), false);
      } else {
         // 兜底维持无敌状态，避免被绕过无敌的伤害击杀
         var2.setInvulnerable(true);
         if (var2.getHealth() < var2.getMaxHealth()) {
            var2.setHealth(var2.getMaxHealth());
         }
      }

      int var3 = Math.max(1, var1.interval);
      int var4 = PET_TIMER.getOrDefault(var0.getUUID(), 0) + 1;
      if (var4 >= var3) {
         var4 = 0;
         givePetRandomItem(var0);
      }

      PET_TIMER.put(var0.getUUID(), var4);
   }

   private static void placeBeacon(ServerPlayer var0) {
      if (var0.level() instanceof ServerLevel var1) {
         Team var10 = team(var0);
         ServerPlayer var3 = null;
         double var4 = Double.MAX_VALUE;

         for (ServerPlayer var7 : var1.players()) {
            if (var7 != var0 && (var10 == null || !var10.hasPlayer(var7))) {
               double var8 = var7.distanceToSqr(var0);
               if (var8 < var4) {
                  var4 = var8;
                  var3 = var7;
               }
            }
         }

         if (var3 == null) {
            var0.sendSystemMessage(Component.translatable("hexbingo.msg.beacon_none").withStyle(ChatFormatting.GRAY));
         } else {
            BlockPos var11 = var3.blockPosition();

            for (int var12 = -1; var12 <= 1; var12++) {
               for (int var13 = -1; var13 <= 1; var13++) {
                  var1.setBlockAndUpdate(var11.offset(var12, -1, var13), Blocks.IRON_BLOCK.defaultBlockState());
               }
            }

            var1.setBlockAndUpdate(var11, Blocks.BEACON.defaultBlockState());
            var1.setBlockAndUpdate(var11.above(), Blocks.AIR.defaultBlockState());
            var1.setBlockAndUpdate(var11.above(2), Blocks.AIR.defaultBlockState());
            BEACONS.put(var0.getUUID(), var11.immutable());
            var0.sendSystemMessage(
               Component.translatable("hexbingo.msg.beacon", new Object[]{Component.literal(var11.getX() + ", " + var11.getZ())})
                  .withStyle(ChatFormatting.GOLD)
            );
         }
      }
   }

   private static void tickBeacon(ServerPlayer var0) {
      BlockPos var1 = BEACONS.get(var0.getUUID());
      if (var1 != null && !(var0.blockPosition().distSqr(var1) > 64.0)) {
         BEACONS.remove(var0.getUUID());
         grantRandomHex(var0, "prismatic", "beacon");
         giveBeaconGear(var0);
         var0.sendSystemMessage(Component.translatable("hexbingo.msg.beacon_reward").withStyle(ChatFormatting.GOLD));
      }
   }

   private static void giveBeaconGear(ServerPlayer var0) {
      Registry<Enchantment> var1 = var0.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
      ItemStack var2 = new ItemStack(Items.DIAMOND_PICKAXE);
      var2.enchant(var1.wrapAsHolder((Enchantment)var1.getValueOrThrow(Enchantments.EFFICIENCY)), 4);
      var0.addItem(markHexGranted(var2));
      ItemStack var3 = new ItemStack(Items.DIAMOND_SWORD);
      var3.enchant(var1.wrapAsHolder((Enchantment)var1.getValueOrThrow(Enchantments.SHARPNESS)), 4);
      var3.enchant(var1.wrapAsHolder((Enchantment)var1.getValueOrThrow(Enchantments.FIRE_ASPECT)), 1);
      var0.addItem(markHexGranted(var3));

      for (Item var7 : new Item[]{Items.DIAMOND_HELMET, Items.DIAMOND_CHESTPLATE, Items.DIAMOND_LEGGINGS, Items.DIAMOND_BOOTS}) {
         ItemStack var8 = new ItemStack(var7);
         enchantStack(var0, var8);
         var0.addItem(markHexGranted(var8));
      }
   }

   public static boolean grapple(ServerPlayer var0) {
      if (!hasHex(var0, "hook_dizzy")) {
         return false;
      }

      long var1 = System.currentTimeMillis();
      Long var3 = HOOK_COOLDOWN.get(var0.getUUID());
      if (var3 != null && var1 < var3) {
         long var6 = (var3 - var1 + 999L) / 1000L;
         var0.sendSystemMessage(
            Component.translatable("hexbingo.hook.cooldown", new Object[]{Component.literal(Long.toString(var6))}).withStyle(ChatFormatting.GRAY)
         );
         return true;
      }

      HitResult var4 = var0.pick(30.0, 0.0F, false);
      if (var4.getType() == Type.BLOCK) {
         Vec3 var5 = var4.getLocation().subtract(var0.position()).normalize();
         var0.setDeltaMovement(var5.x * 2.0, var5.y * 2.0 + 0.4, var5.z * 2.0);
         var0.hurtMarked = true;
         HOOK_COOLDOWN.put(var0.getUUID(), var1 + skillCooldown(var0, 5000L));
         var0.sendSystemMessage(Component.translatable("hexbingo.hook.success").withStyle(ChatFormatting.AQUA));
      } else {
         var0.sendSystemMessage(Component.translatable("hexbingo.hook.no_target").withStyle(ChatFormatting.GRAY));
      }

      return true;
   }

   private static void tickBoomerangs(MinecraftServer var0) {
      Iterator var1 = BOOMERANGS.entrySet().iterator();

      while (var1.hasNext()) {
         Entry var2 = (Entry)var1.next();
         HexEffects.BoomerangState var3 = (HexEffects.BoomerangState)var2.getValue();
         ServerPlayer var4 = var0.getPlayerList().getPlayer((UUID)var2.getKey());
         if (var4 == null || !var3.entity.isAlive()) {
            var3.entity.discard();
            var1.remove();
            continue;
         }

         var3.ticks++;
         if (var3.ticks >= 40) {
            var3.entity.discard();
            var1.remove();
            giveBoomerangItem(var4);
            continue;
         }

         var3.entity.setDeltaMovement(var3.direction.scale(1.5));
         if (var4.level() instanceof ServerLevel var9) {
            var9.sendParticles(ParticleTypes.END_ROD, var3.entity.getX(), var3.entity.getY(), var3.entity.getZ(), 1, 0.0, 0.0, 0.0, 0.0);
         }
      }
   }

   private static void giveBoomerangItem(ServerPlayer var0) {
      var0.addItem(questItem(Items.NAUTILUS_SHELL, "ok回力镖", "hexbingo.item.boomerang.lore"));
      var0.sendSystemMessage(Component.translatable("hexbingo.msg.boomerang_item"));
   }

   private static void consumeBoomerang(ServerPlayer var0) {
      for (int var1 = 0; var1 < var0.getInventory().getContainerSize(); var1++) {
         ItemStack var2 = var0.getInventory().getItem(var1);
         if (!var2.isEmpty() && var2.getHoverName().getString().equals("ok回力镖")) {
            var2.shrink(1);
            return;
         }
      }
   }

   public static boolean launchBoomerang(ServerPlayer var0) {
      if (!hasHex(var0, "boomerang_biome")) {
         return false;
      }

      if (var0.level() instanceof ServerLevel var1) {
         Bingo var15 = Bingo.get(var1);
         Team var3 = var15.getTeam(var0);
         if (var3 == null) {
            return true;
         }

         ArrayList var4 = new ArrayList();

         for (int var5 = 0; var5 < var15.tasks().size(); var5++) {
            if (!var3.completed(var5) && !var3.locked(var5) && var15.task(var5).getType().id().equals("bingo.biome")) {
               var4.add(var5);
            }
         }

         if (var4.isEmpty()) {
            var0.sendSystemMessage(Component.translatable("hexbingo.msg.boomerang_none").withStyle(ChatFormatting.GRAY));
            return true;
         }

         int var16 = (Integer)var4.get(ThreadLocalRandom.current().nextInt(var4.size()));
         if (taskElement(var15.task(var16)) instanceof Identifier var6) {
            ResourceKey var17 = ResourceKey.create(Registries.BIOME, var6);
            Predicate<Holder<Biome>> var8 = var1x -> var1x.unwrapKey().map(var1xx -> var1xx.equals(var17)).orElse(false);
            Pair var9 = var1.findClosestBiome3d(var8, var0.blockPosition(), 12800, 32, 96);
            boolean var18 = false;
            BlockPos var10;
            if (var9 == null) {
               // 附近找不到时直接在原地生成一处目标群系，保证回力镖一定能指向
               var10 = createArtificialBiomeGeneric(var1, var0, var6.toString());
               if (var10 == null) {
                  var0.sendSystemMessage(Component.translatable("hexbingo.msg.boomerang_none").withStyle(ChatFormatting.GRAY));
                  return true;
               }
               var18 = true;
            } else {
               var10 = (BlockPos)var9.getFirst();
            }

            if (var18) {
               var0.sendSystemMessage(Component.translatable("hexbingo.msg.boomerang_gen", var6.toString()).withStyle(ChatFormatting.GOLD));
            }

            Vec3 var11 = new Vec3(var10.getX() + 0.5 - var0.getX(), 0.0, var10.getZ() + 0.5 - var0.getZ()).normalize();
            ItemStack var12 = new ItemStack(Items.NAUTILUS_SHELL);
            var12.set(DataComponents.CUSTOM_NAME, Component.literal("ok回力镖"));
            ItemEntity var13 = new ItemEntity(var1, var0.getX(), var0.getEyeY(), var0.getZ(), var12);
            var13.setNoGravity(true);
            var13.setNeverPickUp();
            var13.setUnlimitedLifetime();
            var13.setDeltaMovement(var11.scale(1.5));
            var1.addFreshEntity(var13);
            BOOMERANGS.put(var0.getUUID(), new HexEffects.BoomerangState(var13, var11, 0));
            consumeBoomerang(var0);
            int var14 = (int)Math.sqrt(var0.distanceToSqr(var10.getX() + 0.5, var10.getY(), var10.getZ() + 0.5));
            var0.sendSystemMessage(
               Component.translatable("hexbingo.msg.boomerang", new Object[]{Component.literal(var6.toString()), Component.literal(Integer.toString(var14))})
                  .withStyle(ChatFormatting.AQUA)
            );
            return true;
         } else {
            var0.sendSystemMessage(Component.translatable("hexbingo.msg.boomerang_none").withStyle(ChatFormatting.GRAY));
            return true;
         }
      } else {
         return true;
      }
   }

   private static void giveHookItem(ServerPlayer var0) {
      var0.addItem(questItem(Items.FISHING_ROD, "转到我眩晕了", "hexbingo.item.hook.lore"));
      var0.sendSystemMessage(Component.translatable("hexbingo.msg.hook_item"));
   }

   private static void giveDashItem(ServerPlayer var0) {
      var0.addItem(questItem(Items.GOAT_HORN, "冲冲冲", "hexbingo.item.dash.lore"));
      var0.sendSystemMessage(Component.translatable("hexbingo.msg.dash_item"));
   }

   public static boolean dashCharge(ServerPlayer var0) {
      if (!hasHex(var0, "dash_charge")) {
         return false;
      }

      long var1 = System.currentTimeMillis();
      Long var3 = DASH_COOLDOWN.get(var0.getUUID());
      if (var3 != null && var1 < var3) {
         long var21 = (var3 - var1 + 999L) / 1000L;
         var0.sendSystemMessage(
            Component.translatable("hexbingo.msg.dash_cooldown", new Object[]{Component.literal(Long.toString(var21))}).withStyle(ChatFormatting.GRAY)
         );
         return true;
      }

      if (!(var0.level() instanceof ServerLevel) || var0.isSpectator() || DASHING.containsKey(var0.getUUID())) {
         return true;
      }

      Vec3 var4 = var0.getLookAngle();
      double var5 = Math.sqrt(var4.x * var4.x + var4.z * var4.z);
      if (var5 < 1.0E-4) {
         var0.sendSystemMessage(Component.translatable("hexbingo.msg.dash_none").withStyle(ChatFormatting.GRAY));
         return true;
      }

      DASHING.put(var0.getUUID(), new HexEffects.DashState(var1 + DASH_DURATION_MS, var0.getY(), var4.x / var5, var4.z / var5));
      var0.sendSystemMessage(Component.translatable("hexbingo.msg.dash_start").withStyle(ChatFormatting.GOLD));
      return true;
   }

   public static void tickDash(MinecraftServer var0) {
      if (DASHING.isEmpty()) {
         return;
      }

      Iterator<Map.Entry<UUID, HexEffects.DashState>> var1 = DASHING.entrySet().iterator();

      while (var1.hasNext()) {
         Map.Entry<UUID, HexEffects.DashState> var2 = var1.next();
         HexEffects.DashState var3 = var2.getValue();
         ServerPlayer var4 = var0.getPlayerList().getPlayer(var2.getKey());
         if (var4 != null && hasHex(var4, "dash_charge") && var4.level() instanceof ServerLevel var5 && !var4.isSpectator() && !var4.isDeadOrDying()) {
            long var6 = System.currentTimeMillis();
            if (var6 >= var3.endsAt) {
               var1.remove();
               finishDash(var4, var3, var6);
            } else {
               Vec3 var8 = var4.getLookAngle();
               double var9 = Math.sqrt(var8.x * var8.x + var8.z * var8.z);
               if (var9 >= 1.0E-4) {
                  var3.dirX = var8.x / var9;
                  var3.dirZ = var8.z / var9;
               }

               double var11 = var4.getX() + var3.dirX * 0.6;
               double var13 = var4.getZ() + var3.dirZ * 0.6;
               int var15 = Mth.floor(var11);
               int var16 = Mth.floor(var3.y);
               int var17 = Mth.floor(var13);
               boolean var18 = false;

               for (int var19 = 0; var19 <= 1; var19++) {
                  BlockPos var20 = new BlockPos(var15, var16 + var19, var17);
                  BlockState var21 = var5.getBlockState(var20);
                  if (dashBlocked(var21, var5, var20)) {
                     var18 = true;
                     break;
                  }
               }

               if (var18) {
                  var1.remove();
                  var4.sendSystemMessage(Component.translatable("hexbingo.msg.dash_blocked").withStyle(ChatFormatting.GRAY));
                  finishDash(var4, var3, var6);
               } else {
                  for (int var28 = -1; var28 <= 1; var28++) {
                     for (int var29 = -1; var29 <= 1; var29++) {
                        for (int var30 = 0; var30 <= 1; var30++) {
                           BlockPos var31 = new BlockPos(var15 + var28, var16 + var30, var17 + var29);
                           BlockState var32 = var5.getBlockState(var31);
                           if (var32.isAir() || !var32.getFluidState().isEmpty() || dashBlocked(var32, var5, var31)) {
                              continue;
                           }

                           var5.destroyBlock(var31, true, var4);
                           var3.blocks++;
                        }
                     }
                  }

                  var4.teleportTo(var5, var11, var3.y, var13, Set.of(), var4.getYRot(), var4.getXRot(), false);
                  var4.setDeltaMovement(Vec3.ZERO);
                  var4.resetFallDistance();
                  var3.moved += 0.6;
                  AABB var24 = new AABB(var11 - 0.9, var3.y - 0.1, var13 - 0.9, var11 + 0.9, var3.y + 2.0, var13 + 0.9);

                  for (LivingEntity var26 : var5.getEntitiesOfClass(LivingEntity.class, var24, var27 -> var27 != var4 && var27.isAlive() && !var27.isSpectator())) {
                     if (var3.hit.add(var26.getUUID())) {
                        var26.hurtServer(var5, var4.damageSources().playerAttack(var4), 10.0F);
                        var3.mobs++;
                     }
                  }
               }
            }
         } else {
            var1.remove();
         }
      }
   }

   private static void finishDash(ServerPlayer var0, HexEffects.DashState var1, long var2) {
      DASH_COOLDOWN.put(var0.getUUID(), var2 + skillCooldown(var0, DASH_COOLDOWN_MS));
      var0.resetFallDistance();
      var0.sendSystemMessage(
         Component.translatable(
               "hexbingo.msg.dash",
               new Object[]{
                  Component.literal(Integer.toString(Math.round((float)var1.moved))),
                  Component.literal(Integer.toString(var1.blocks)),
                  Component.literal(Integer.toString(var1.mobs))
               }
            )
            .withStyle(ChatFormatting.GOLD)
      );
   }

   private static boolean dashBlocked(BlockState var0, ServerLevel var1, BlockPos var2) {
      return var0.is(Blocks.OBSIDIAN) || var0.is(Blocks.BEDROCK) || var0.getDestroySpeed(var1, var2) < 0.0F;
   }

   private static void givePortableBench(ServerPlayer var0) {
      var0.addItem(questItem(Items.CRAFTING_TABLE, "随身工作台", "hexbingo.item.portable_bench.lore"));
      var0.sendSystemMessage(Component.translatable("hexbingo.msg.portable_bench_item"));
   }

   public static boolean openPortableCrafting(ServerPlayer var0) {
      if (!hasHex(var0, "portable_bench")) {
         return false;
      }

      ServerPlayer var1 = var0;
      ContainerLevelAccess var2 = new ContainerLevelAccess() {
         public <T> Optional<T> evaluate(BiFunction<Level, BlockPos, T> var0x) {
            return Optional.ofNullable(var0x.apply(var1.level(), var1.blockPosition()));
         }

         public <T> T evaluate(BiFunction<Level, BlockPos, T> var0x, T var1x) {
            return var1x;
         }

         public void execute(BiConsumer<Level, BlockPos> var0x) {
            var0x.accept(var1.level(), var1.blockPosition());
         }
      };
      var0.openMenu(
         new SimpleMenuProvider((var3, var4, var5) -> new CraftingMenu(var3, var4, var2), Component.translatable("hexbingo.menu.portable_bench"))
      );
      return true;
   }

   private static void givePortableFurnace(ServerPlayer var0) {
      var0.addItem(questItem(Items.FURNACE, "便携熔炉", "hexbingo.item.portable_furnace.lore"));
      var0.sendSystemMessage(Component.translatable("hexbingo.msg.portable_furnace_item"));
   }

   public static boolean openPortableFurnace(ServerPlayer var0) {
      if (!hasHex(var0, "portable_furnace")) {
         return false;
      }

      MinecraftServer var1 = var0.level().getServer();
      if (var1 == null) {
         return false;
      }

      HexEffects.PortableFurnace var2 = PORTABLE_FURNACES.computeIfAbsent(var0.getUUID(), var2x -> {
         HexEffects.PortableFurnace var3 = new HexEffects.PortableFurnace();
         var3.load(HexGameData.get(var1).portableFurnace(var2x));
         return var3;
      });
      var2.menu = null;
      var0.openMenu(
         new SimpleMenuProvider(
            (var3, var4, var5) -> {
               FurnaceMenu var6 = new FurnaceMenu(var3, var4, var2.items, var2);
               var2.menu = var6;
               return var6;
            },
            Component.translatable("hexbingo.menu.portable_furnace")
         )
      );
      return true;
   }

   public static void tickPortableFurnaces(MinecraftServer var0) {
      if (!PORTABLE_FURNACES.isEmpty()) {
         Iterator<Map.Entry<UUID, HexEffects.PortableFurnace>> var1 = PORTABLE_FURNACES.entrySet().iterator();

         while (var1.hasNext()) {
            Map.Entry<UUID, HexEffects.PortableFurnace> var2 = var1.next();
            ServerPlayer var3 = var0.getPlayerList().getPlayer(var2.getKey());
            HexEffects.PortableFurnace var4 = var2.getValue();
            if (var3 == null) {
               HexGameData.get(var0).setPortableFurnace(var2.getKey(), var4.save());
               var1.remove();
            } else {
               // 关闭界面后仍继续烧制：不再要求 containerMenu 与记录一致
               tickPortableFurnace(var3, var4);
               if (++var4.saveTicks >= 100) {
                  var4.saveTicks = 0;
                  HexGameData.get(var0).setPortableFurnace(var3.getUUID(), var4.save());
               }
            }
         }
      }
   }

   private static void tickPortableFurnace(ServerPlayer var0, HexEffects.PortableFurnace var1) {
      if (var0.level() instanceof ServerLevel var2) {
         ItemStack var3 = var1.items.getItem(0);
         ItemStack var4 = var1.items.getItem(1);
         ItemStack var5 = var1.items.getItem(2);
         ItemStack var6 = ItemStack.EMPTY;
         int var7 = 200;
         if (!var3.isEmpty()) {
            SingleRecipeInput var8 = new SingleRecipeInput(var3);
            Optional<RecipeHolder<SmeltingRecipe>> var9 = var2.recipeAccess().getRecipeFor(RecipeType.SMELTING, var8, var2);
            if (var9.isPresent()) {
               var6 = ((SmeltingRecipe)var9.get().value()).assemble(var8);
               var7 = ((SmeltingRecipe)var9.get().value()).cookingTime();
            }
         }

         boolean var10 = !var6.isEmpty()
            && (var5.isEmpty() || ItemStack.isSameItemSameComponents(var5, var6) && var5.getCount() + var6.getCount() <= var5.getMaxStackSize());
         if (var10 && var1.litTime <= 0 && !var4.isEmpty()) {
            int var11 = var2.fuelValues().burnDuration(var4);
            if (var11 > 0) {
               ItemStackTemplate var12 = var4.getItem().getCraftingRemainder();
               var1.litTime = var11;
               var1.litTotal = var11;
               var4.shrink(1);
               if (var4.isEmpty() && var12 != null) {
                  var1.items.setItem(1, var12.create());
               }
            }
         }

         if (var1.litTime > 0) {
            var1.litTime--;
         }

         if (var10 && var1.litTime > 0) {
            var1.cookTotal = var7;
            var1.cookProgress += hasHex(var0, "smelt_speed") ? 2 : 1;
            if (var1.cookProgress >= var7) {
               var1.cookProgress = 0;
               var3.shrink(1);
               if (var5.isEmpty()) {
                  var1.items.setItem(2, var6.copy());
               } else {
                  var5.grow(var6.getCount());
               }
            }
         } else {
            var1.cookProgress = 0;
         }
      }
   }

   public static void tickSmeltBoost(MinecraftServer var0) {
      boolean var1 = false;

      for (ServerPlayer var3 : var0.getPlayerList().getPlayers()) {
         if (hasHex(var3, "smelt_speed")) {
            var1 = true;
            break;
         }
      }

      if (!var1) {
         BOOSTED_FURNACES.clear();
      } else {
         if (var0.overworld().getGameTime() % 20L == 0L) {
            scanFurnaces(var0);
         }

         HashSet<String> var2 = new HashSet<>();

         for (Map.Entry<UUID, List<BlockPos>> var4 : BOOSTED_FURNACES.entrySet()) {
            ServerPlayer var5 = var0.getPlayerList().getPlayer(var4.getKey());
            if (var5 != null && hasHex(var5, "smelt_speed") && var5.level() instanceof ServerLevel var6) {
               for (BlockPos var8 : var4.getValue()) {
                  if (!var6.isLoaded(var8)) {
                     continue;
                  }

                  if (!(var6.getBlockEntity(var8) instanceof AbstractFurnaceBlockEntity var9) || var9.isRemoved()) {
                     continue;
                  }

                  String var10 = var6.dimension().identifier() + "|" + var8.asLong();
                  if (var2.add(var10)) {
                     AbstractFurnaceBlockEntity.serverTick(var6, var8, var6.getBlockState(var8), var9);
                  }
               }
            }
         }
      }
   }

   private static void scanFurnaces(MinecraftServer var0) {
      BOOSTED_FURNACES.clear();

      for (ServerPlayer var2 : var0.getPlayerList().getPlayers()) {
         if (hasHex(var2, "smelt_speed") && var2.level() instanceof ServerLevel var3) {
            int var4 = var2.blockPosition().getX() >> 4;
            int var5 = var2.blockPosition().getZ() >> 4;
            ArrayList<BlockPos> var6 = new ArrayList<>();

            for (int var7 = -2; var7 <= 2; var7++) {
               for (int var8 = -2; var8 <= 2; var8++) {
                  LevelChunk var9 = var3.getChunkSource().getChunkNow(var4 + var7, var5 + var8);
                  if (var9 != null) {
                     for (Map.Entry<BlockPos, BlockEntity> var11 : var9.getBlockEntities().entrySet()) {
                        if (var11.getValue() instanceof AbstractFurnaceBlockEntity) {
                           var6.add(var11.getKey().immutable());
                        }
                     }
                  }
               }
            }

            BOOSTED_FURNACES.put(var2.getUUID(), var6);
         }
      }
   }

   private static final class PortableFurnace implements ContainerData {
      private final SimpleContainer items = new SimpleContainer(3);
      private int litTime;
      private int litTotal;
      private int cookProgress;
      private int cookTotal = 200;
      private int saveTicks;
      private FurnaceMenu menu;

      public int get(int var1) {
         return switch (var1) {
            case 0 -> this.litTime;
            case 1 -> this.litTotal;
            case 2 -> this.cookProgress;
            case 3 -> this.cookTotal;
            default -> 0;
         };
      }

      public void set(int var1, int var2) {
         switch (var1) {
            case 0 -> this.litTime = var2;
            case 1 -> this.litTotal = var2;
            case 2 -> this.cookProgress = var2;
            case 3 -> this.cookTotal = var2;
         }
      }

      public int getCount() {
         return 4;
      }

      private void load(CompoundTag var1) {
         ListTag var2 = var1.getListOrEmpty("items");

         for (int var3 = 0; var3 < 3 && var3 < var2.size(); var3++) {
            int var4 = var3;
            ItemStack.OPTIONAL_CODEC.parse(NbtOps.INSTANCE, var2.get(var4)).result().ifPresent(var2x -> this.items.setItem(var4, var2x));
         }

         this.litTime = var1.getIntOr("litTime", 0);
         this.litTotal = var1.getIntOr("litTotal", 0);
         this.cookProgress = var1.getIntOr("cookProgress", 0);
         this.cookTotal = var1.getIntOr("cookTotal", 200);
      }

      private CompoundTag save() {
         CompoundTag var1 = new CompoundTag();
         ListTag var2 = new ListTag();

         for (int var3 = 0; var3 < 3; var3++) {
            ItemStack.OPTIONAL_CODEC.encodeStart(NbtOps.INSTANCE, this.items.getItem(var3)).result().ifPresent(var2::add);
         }

         var1.put("items", var2);
         var1.putInt("litTime", this.litTime);
         var1.putInt("litTotal", this.litTotal);
         var1.putInt("cookProgress", this.cookProgress);
         var1.putInt("cookTotal", this.cookTotal);
         return var1;
      }
   }

   private static void startSoulOut(ServerPlayer var0) {
      if (!SOULS.containsKey(var0.getUUID())) {
         GameType var1 = var0.gameMode();
         if (var1 != GameType.SPECTATOR) {
            SOULS.put(
               var0.getUUID(),
               new HexEffects.SoulState(
                  var0.level(),
                  var0.getX(),
                  var0.getY(),
                  var0.getZ(),
                  var0.getYRot(),
                  var0.getXRot(),
                  var1,
                  System.currentTimeMillis() + 30000L
               )
            );
            var0.setGameMode(GameType.SPECTATOR);
            var0.sendSystemMessage(Component.translatable("hexbingo.msg.soul_out").withStyle(ChatFormatting.LIGHT_PURPLE));
         }
      }
   }

   public static void tickSoulOut(ServerPlayer var0) {
      HexEffects.SoulState var1 = SOULS.get(var0.getUUID());
      if (var1 != null && System.currentTimeMillis() >= var1.until) {
         endSoulOut(var0);
      }
   }

   public static void onSoulLogout(ServerPlayer var0) {
      if (SOULS.containsKey(var0.getUUID())) {
         endSoulOut(var0);
      }
   }

   public static void abortSouls(MinecraftServer var0) {
      for (UUID var2 : new ArrayList<>(SOULS.keySet())) {
         ServerPlayer var3 = var0.getPlayerList().getPlayer(var2);
         if (var3 != null) {
            endSoulOut(var3);
         } else {
            SOULS.remove(var2);
         }
      }
   }

   private static void endSoulOut(ServerPlayer var0) {
      HexEffects.SoulState var1 = SOULS.remove(var0.getUUID());
      if (var1 != null) {
         if (var0.gameMode() == GameType.SPECTATOR) {
            var0.setGameMode(var1.mode);
         }

         var0.teleportTo(var1.level, var1.x, var1.y, var1.z, Set.of(), var1.yaw, var1.pitch, false);
         var0.resetFallDistance();
         var0.setRemainingFireTicks(0);
         var0.sendSystemMessage(Component.translatable("hexbingo.msg.soul_return").withStyle(ChatFormatting.AQUA));
      }
   }

   // 催熟喷雾：每 10 秒自动催熟周围作物
   public static void boneMealSpray(ServerPlayer var0) {
      if (hasHex(var0, "bone_meal_spray") && var0.level() instanceof ServerLevel var1) {
         BlockPos var2 = var0.blockPosition();
         var1.sendParticles(ParticleTypes.HAPPY_VILLAGER, var0.getX(), var0.getY() + 0.5, var0.getZ(), 50, 6.0, 2.0, 6.0, 0.0);
         ArrayList<BlockPos> var3 = new ArrayList<>();

         for (int var4 = -9; var4 <= 9; var4++) {
            for (int var5 = -9; var5 <= 9; var5++) {
               if (var4 * var4 + var5 * var5 <= 81) {
                  for (int var6 = -3; var6 <= 3; var6++) {
                     BlockPos var7 = var2.offset(var4, var6, var5);
                     if (var1.getBlockState(var7).getBlock() instanceof BonemealableBlock) {
                        var3.add(var7.immutable());
                     }
                  }
               }
            }
         }

         if (!var3.isEmpty()) {
            Collections.shuffle(var3, ThreadLocalRandom.current());
            int var11 = 0;

            for (BlockPos var12 : var3) {
               if (BoneMealItem.growCrop(new ItemStack(Items.BONE_MEAL), var1, var12)) {
                  BoneMealItem.addGrowthParticles(var1, var12, 15);
                  var11++;
                  if (var11 >= 12) {
                     break;
                  }
               }
            }
         }
      }
   }

   private static void copyOpponentTask(ServerPlayer var0) {
      MinecraftServer var1 = var0.level().getServer();
      if (var1 == null) {
         return;
      }

      Bingo var2 = Bingo.get(var1.overworld());
      Team var3 = var2.getTeam(var0);
      if (var3 != null && var2.running()) {
         boolean var4 = var2.getSettings().game().lockout();
         ArrayList<Integer> var5 = new ArrayList<>();

         for (int var6 = 0; var6 < var2.tasks().size(); var6++) {
            if (!var3.completed(var6) && (var4 || !var3.locked(var6))) {
               for (Team var7 : var2.getTeams()) {
                  if (var7 != var3 && var7.completed(var6)) {
                     var5.add(var6);
                     break;
                  }
               }
            }
         }

         if (var5.isEmpty()) {
            var0.sendSystemMessage(Component.translatable("hexbingo.msg.task_copy_none").withStyle(ChatFormatting.GRAY));
         } else {
            int var14 = (Integer)var5.get(ThreadLocalRandom.current().nextInt(var5.size()));
            var3.complete(var14);
            if (var4) {
               for (Team var8 : var2.getTeams()) {
                  if (var8 != var3 && !var8.completed(var14)) {
                     var8.lock(var14);
                  }
               }
            }

            var2.checkWin();
            Component var15 = var2.task(var14).contentName(var1);
            var0.sendSystemMessage(
               Component.translatable(var4 ? "hexbingo.msg.task_copy_lockout" : "hexbingo.msg.task_copy", new Object[]{var15})
                  .withStyle(var4 ? ChatFormatting.GOLD : ChatFormatting.LIGHT_PURPLE)
            );
         }
      } else {
         var0.sendSystemMessage(Component.translatable("hexbingo.msg.task_copy_none").withStyle(ChatFormatting.GRAY));
      }
   }

   private static void giveFlightKit(ServerPlayer var0) {
      ItemStack var1 = questItem(Items.ELYTRA, "中国人会飞·鞘翅", "hexbingo.item.elytra.lore");
      var1.set(DataComponents.UNBREAKABLE, Unit.INSTANCE);
      var0.addItem(var1);
      ensureFlightRocket(var0);
      var0.sendSystemMessage(Component.translatable("hexbingo.msg.flight_kit"));
   }

   private static void ensureFlightRocket(ServerPlayer var0) {
      if (!hasNamedItem(var0, "中国人会飞·烟花")) {
         var0.addItem(questItem(Items.FIREWORK_ROCKET, "中国人会飞·烟花", "hexbingo.item.fly.lore"));
      }
   }

   private static void locateEndPortal(ServerPlayer var0) {
      MinecraftServer var1 = var0.level().getServer();
      if (var1 != null) {
         ServerLevel var2 = var1.overworld();
         BlockPos var3 = new BlockPos(var0.getBlockX(), var0.getBlockY(), var0.getBlockZ());
         BlockPos var4 = var2.findNearestMapStructure(StructureTags.EYE_OF_ENDER_LOCATED, var3, 100, false);
         if (var4 == null) {
            var0.sendSystemMessage(Component.translatable("hexbingo.msg.end_portal_none").withStyle(ChatFormatting.GRAY));
         } else {
            double var5 = var0.getX() - (var4.getX() + 0.5);
            double var7 = var0.getZ() - (var4.getZ() + 0.5);
            int var9 = (int)Math.sqrt(var5 * var5 + var7 * var7);
            var0.sendSystemMessage(
               Component.translatable(
                     "hexbingo.msg.end_portal", new Object[]{Component.literal(var4.getX() + ", " + var4.getZ()), Component.literal(Integer.toString(var9))}
                  )
                  .withStyle(ChatFormatting.AQUA)
            );
         }
      }
   }

   public static void ensureQuestItems(ServerPlayer var0) {
      if (hasHex(var0, "hook_dizzy") && !hasNamedItem(var0, "转到我眩晕了")) {
         giveHookItem(var0);
      }

      if (hasHex(var0, "boomerang_biome") && !hasNamedItem(var0, "ok回力镖")) {
         giveBoomerangItem(var0);
      }

      if (hasHex(var0, "the_world") && !hasNamedItem(var0, "The World")) {
         giveWorldItem(var0);
      }

      if (hasHex(var0, "dash_charge") && !hasNamedItem(var0, "冲冲冲")) {
         giveDashItem(var0);
      }

      if (hasHex(var0, "portable_bench") && !hasNamedItem(var0, "随身工作台")) {
         givePortableBench(var0);
      }

      if (hasHex(var0, "portable_furnace") && !hasNamedItem(var0, "便携熔炉")) {
         givePortableFurnace(var0);
      }

      if (hasHex(var0, "rainbow_duel") && !hasNamedItem(var0, "和我一起困在这里")) {
         giveTrapItem(var0);
      }

      if (hasHex(var0, "fae_magic") && !hasNamedItem(var0, "精怪魔法")) {
         giveFaeItem(var0);
      }

      if (hasHex(var0, "nether_banish") && !hasNamedItem(var0, "维度放逐")) {
         giveBanishItem(var0);
      }

      if (hasHex(var0, "jam_bp") && !hasNamedItem(var0, "对方不在服务区")) {
         giveJamItem(var0);
      }

      if (hasHex(var0, "sea_god") && !hasNamedItem(var0, "海神")) {
         giveSeaGod(var0);
      }

      if (hasHex(var0, "untouchable") && !hasNamedItem(var0, "你摸不到")) {
         giveUntouchableItem(var0);
      }

      if (hasHex(var0, "inventory_shuffle") && !hasNamedItem(var0, "混沌智")) {
         giveChaosMindItem(var0);
      }

      if (hasHex(var0, "chinese_can_fly")) {
         ensureFlightRocket(var0);
      }

      if (hasHex(var0, "blind") && !hasNamedItem(var0, "致盲")) {
         giveBlindItem(var0);
      }

      if (hasHex(var0, "bat_form") && !hasNamedItem(var0, "蝙蝠形态")) {
         giveBatItem(var0);
      }

      if (hasHex(var0, "mind_control") && !hasNamedItem(var0, "精神控制")) {
         giveMindItem(var0);
      }

      if (hasHex(var0, "objective_deny") && !hasNamedItem(var0, "目标封锁")) {
         giveDenyItem(var0);
      }

      if (hasHex(var0, "xray") && !hasNamedItem(var0, "矿物透视")) {
         giveXrayItem(var0);
      }

      if (hasHex(var0, "mirror_hex") && !MIRROR_USED.contains(var0.getUUID()) && !hasNamedItem(var0, "镜像复制")) {
         giveMirrorItem(var0);
      }

      if (hasHex(var0, "blank_cell") && !BLANK_USED.contains(var0.getUUID()) && !hasNamedItem(var0, "空白格")) {
         giveBlankCellItem(var0);
      }
   }

   private static boolean hasNamedItem(ServerPlayer var0, String var1) {
      for (int var2 = 0; var2 < var0.getInventory().getContainerSize(); var2++) {
         ItemStack var3 = var0.getInventory().getItem(var2);
         if (!var3.isEmpty() && var3.getHoverName().getString().equals(var1)) {
            return true;
         }
      }

      return false;
   }

   private static ItemStack questItem(Item var0, String var1, String var2) {
      ItemStack var3 = new ItemStack(var0);
      var3.set(DataComponents.CUSTOM_NAME, Component.literal(var1));
      var3.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);
      var3.set(DataComponents.LORE, new ItemLore(List.of(Component.translatable(var2).withStyle(ChatFormatting.GRAY))));
      return markHexGranted(var3);
   }

   private static ItemStack markHexGranted(ItemStack var0) {
      CustomData.update(DataComponents.CUSTOM_DATA, var0, var1 -> var1.putBoolean("hexbingo_granted", true));
      return var0;
   }

   private static boolean isHexGranted(ItemStack var0) {
      CustomData var1 = var0.get(DataComponents.CUSTOM_DATA);
      return var1 != null && var1.copyTag().getBooleanOr("hexbingo_granted", false);
   }

   private static void giveTrapItem(ServerPlayer var0) {
      var0.addItem(questItem(Items.IRON_CHAIN, "和我一起困在这里", "hexbingo.item.trap.lore"));
      var0.sendSystemMessage(Component.translatable("hexbingo.msg.trap_item"));
   }

   public static boolean startTrap(ServerPlayer var0) {
      if (!hasHex(var0, "rainbow_duel")) {
         return false;
      }

      long var1 = System.currentTimeMillis();
      Long var3 = TRAP_COOLDOWN.get(var0.getUUID());
      if (var3 != null && var1 < var3) {
         long var11 = (var3 - var1 + 999L) / 1000L;
         var0.sendSystemMessage(
            Component.translatable("hexbingo.msg.trap_cooldown", new Object[]{Component.literal(Long.toString(var11))}).withStyle(ChatFormatting.GOLD)
         );
         return true;
      }

      if (TRAP != null) {
         var0.sendSystemMessage(Component.translatable("hexbingo.msg.trap_busy").withStyle(ChatFormatting.GRAY));
         return true;
      }

      if (var0.level() instanceof ServerLevel var4 && !var0.isSpectator()) {
         ServerPlayer var12 = findTrapTarget(var0, var4);
         if (var12 == null) {
            var0.sendSystemMessage(Component.translatable("hexbingo.msg.trap_none").withStyle(ChatFormatting.GRAY));
            return true;
         } else {
            if (shieldBlocks(var12, "rainbow_duel")) {
               // 目标被免疫护盾抵挡：效果被挡，但技能已发动，仍进入冷却
               TRAP_COOLDOWN.put(var0.getUUID(), var1 + skillCooldown(var0, 180000L));
               var0.sendSystemMessage(
                  Component.translatable("hexbingo.msg.shield_attacker", new Object[]{Component.translatable("hexbingo.hex.rainbow_duel.name")})
                     .withStyle(ChatFormatting.GRAY)
               );
               return true;
            }

            int var6 = (var0.getBlockX() + var12.getBlockX()) / 2;
            int var7 = (var0.getBlockZ() + var12.getBlockZ()) / 2;
            int var8 = Math.min(Math.max(var0.getBlockY(), var12.getBlockY()) + 30, var4.getMaxY() - 10);
            HexEffects.TrapRoom var9 = new HexEffects.TrapRoom(var0, var12, var4, var6, var7, var8);
            TRAP = var9;
            var9.pvpWasAllowed = var4.getGameRules().get(GameRules.PVP);
            if (!var9.pvpWasAllowed) {
               var4.getGameRules().set(GameRules.PVP, true, var4.getServer());
            }

            buildTrapRoom(var9);
            var0.teleportTo(var4, var6 + 0.5, var8 + 2.0, var7 - 1.5, Set.of(), 0.0F, 0.0F, false);
            var12.teleportTo(var4, var6 + 0.5, var8 + 2.0, var7 + 1.5, Set.of(), 180.0F, 0.0F, false);
            var0.resetFallDistance();
            var12.resetFallDistance();
            var0.setRemainingFireTicks(0);
            var12.setRemainingFireTicks(0);
            TRAP_COOLDOWN.put(var0.getUUID(), var1 + skillCooldown(var0, 180000L));
            MutableComponent var10 = Component.translatable("hexbingo.msg.trap_start").withStyle(ChatFormatting.LIGHT_PURPLE);
            var0.sendSystemMessage(var10);
            var12.sendSystemMessage(var10);
            return true;
         }
      } else {
         return true;
      }
   }

   private static ServerPlayer findTrapTarget(ServerPlayer var0, ServerLevel var1) {
      Team var2 = team(var0);
      ServerPlayer var3 = null;
      double var4 = Double.MAX_VALUE;

      for (ServerPlayer var7 : var1.players()) {
         if (var7 != var0 && !var7.isSpectator() && (var2 == null || !var2.hasPlayer(var7))) {
            double var8 = var7.distanceToSqr(var0);
            if (var8 < var4) {
               var4 = var8;
               var3 = var7;
            }
         }
      }

      return var3;
   }

   private static void buildTrapRoom(HexEffects.TrapRoom var0) {
      for (int var1 = -3; var1 <= 3; var1++) {
         for (int var2 = -3; var2 <= 3; var2++) {
            BlockPos var3 = new BlockPos(var0.centerX + var1, var0.floorY, var0.centerZ + var2);
            trapSet(var0, var3, Blocks.BEDROCK.defaultBlockState());
            boolean var4 = Math.abs(var1) == 3 || Math.abs(var2) == 3;

            for (int var5 = 1; var5 <= 3; var5++) {
               trapSet(var0, var3.above(var5), var4 ? Blocks.BEDROCK.defaultBlockState() : Blocks.AIR.defaultBlockState());
            }

            trapSet(var0, var3.above(4), Blocks.BEDROCK.defaultBlockState());
         }
      }
   }

   private static void trapSet(HexEffects.TrapRoom var0, BlockPos var1, BlockState var2) {
      BlockPos var3 = var1.immutable();
      var0.backup.putIfAbsent(var3, var0.roomLevel.getBlockState(var3));
      var0.roomLevel.setBlockAndUpdate(var3, var2);
   }

   public static void tickTrap(MinecraftServer var0) {
      HexEffects.TrapRoom var1 = TRAP;
      if (var1 != null) {
         if (var1.loserId != null) {
            ServerPlayer var4 = var0.getPlayerList().getPlayer(var1.loserId);
            if (var4 == null) {
               finishTrap(var0, var1, false);
            } else if (var1.penaltyStarted && var4.isAlive()) {
               var1.penaltyTicks++;
               if (var1.penaltyTicks % 20 == 0) {
                  int var5 = Math.max(0, (400 - var1.penaltyTicks + 19) / 20);
                  var4.sendOverlayMessage(Component.translatable("hexbingo.msg.trap_penalty", new Object[]{var5}).withStyle(ChatFormatting.RED));
               }

               if (var1.penaltyTicks >= 400) {
                  finishTrap(var0, var1, true);
               }
            }
         } else {
            ServerPlayer var2 = var0.getPlayerList().getPlayer(var1.firstId);
            ServerPlayer var3 = var0.getPlayerList().getPlayer(var1.secondId);
            if (var2 != null && var3 != null) {
               // 决斗期间双方都不允许自然回血：每 tick 压掉 Bingo 无敌模式补满的饥饿值
               suppressTrapRegen(var2);
               suppressTrapRegen(var3);
               var1.ticks++;
               if (!var1.pvpOccurred && var1.ticks >= 6000) {
                  finishTrap(var0, var1, true);
               }
            } else {
               finishTrap(var0, var1, false);
            }
         }
      }
   }

   public static void trapIncomingDamage(LivingIncomingDamageEvent var0) {
      HexEffects.TrapRoom var1 = TRAP;
      if (var1 != null && var0.getEntity() instanceof ServerPlayer var2 && var1.involves(var2.getUUID())) {
         Entity var3 = var0.getSource().getEntity();
         if (var3 == null) {
            var3 = var0.getSource().getDirectEntity();
         }

         ServerPlayer var4 = null;
         if (var3 instanceof ServerPlayer var5) {
            var4 = var5;
         } else if (var3 instanceof Projectile var6 && var6.getOwner() instanceof ServerPlayer var7) {
            var4 = var7;
         }

         if (var4 != null && var4 != var2 && var1.involves(var4.getUUID())) {
            var0.setCanceled(false);
            var1.pvpOccurred = true;
         }
      }
   }

   // 决斗期间禁止自然回血：饥饿值压到 17 以下，并将饱和度清零
   private static void suppressTrapRegen(ServerPlayer var0) {
      FoodData var1 = var0.getFoodData();
      if (var1.getFoodLevel() > 17) {
         var1.setFoodLevel(17);
      }

      var1.setSaturation(0.0F);
   }

   private static void teleportTrapHome(ServerPlayer var0, HexEffects.TrapRoom var1) {
      boolean var2 = var1.firstId.equals(var0.getUUID());
      ServerLevel var3 = var2 ? var1.firstLevel : var1.secondLevel;
      Vec3 var4 = var2 ? var1.firstHome : var1.secondHome;
      float var5 = var2 ? var1.firstYaw : var1.secondYaw;
      float var6 = var2 ? var1.firstPitch : var1.secondPitch;
      var0.teleportTo(var3, var4.x, var4.y, var4.z, Set.of(), var5, var6, false);
      var0.resetFallDistance();
      var0.setRemainingFireTicks(0);
   }

   public static void onTrapLogout(ServerPlayer var0) {
      HexEffects.TrapRoom var1 = TRAP;
      if (var1 != null && var1.involves(var0.getUUID())) {
         MinecraftServer var2 = var0.level().getServer();
         if (var2 != null) {
            finishTrap(var2, var1, false);
         }
      }
   }

   public static void onTrapDeath(ServerPlayer var0) {
      HexEffects.TrapRoom var1 = TRAP;
      if (var1 != null && var1.involves(var0.getUUID()) && var1.loserId == null) {
         MinecraftServer var2 = var0.level().getServer();
         if (var2 != null) {
            var1.loserId = var0.getUUID();
            var1.penaltyTicks = 0;
            var1.penaltyStarted = false;
            UUID var3 = var1.firstId.equals(var0.getUUID()) ? var1.secondId : var1.firstId;
            var1.releasedId = var3;
            ServerPlayer var4 = var2.getPlayerList().getPlayer(var3);
            if (var4 != null) {
               var4.sendSystemMessage(Component.translatable("hexbingo.msg.trap_win").withStyle(ChatFormatting.GREEN));
               teleportTrapHome(var4, var1);
            }

            restoreTrapPvp(var1, var2);
            var0.sendSystemMessage(Component.translatable("hexbingo.msg.trap_lose", new Object[]{20}).withStyle(ChatFormatting.RED));
         }
      }
   }

   public static void onTrapRespawn(ServerPlayer var0) {
      HexEffects.TrapRoom var1 = TRAP;
      if (var1 != null && var1.loserId != null && var1.loserId.equals(var0.getUUID())) {
         var0.teleportTo(var1.roomLevel, var1.centerX + 0.5, var1.floorY + 1, var1.centerZ + 0.5, Set.of(), 0.0F, 0.0F, false);
         var0.resetFallDistance();
         var0.setRemainingFireTicks(0);
         var1.penaltyTicks = 0;
         var1.penaltyStarted = true;
         var0.sendOverlayMessage(Component.translatable("hexbingo.msg.trap_penalty", new Object[]{20}).withStyle(ChatFormatting.RED));
      }
   }

   private static void restoreTrapPvp(HexEffects.TrapRoom var0, MinecraftServer var1) {
      if (!var0.pvpRestored) {
         var0.pvpRestored = true;
         var0.roomLevel.getGameRules().set(GameRules.PVP, var0.pvpWasAllowed, var1);
      }
   }

   public static void abortTrap(MinecraftServer var0) {
      if (TRAP != null) {
         finishTrap(var0, TRAP, false);
      }
   }

   public static void resetTrapCooldown() {
      TRAP_COOLDOWN.clear();
   }

   private static void finishTrap(MinecraftServer var0, HexEffects.TrapRoom var1, boolean var2) {
      if (TRAP == var1) {
         TRAP = null;
         restoreTrapPvp(var1, var0);
         ServerPlayer var3 = var0.getPlayerList().getPlayer(var1.firstId);
         ServerPlayer var4 = var0.getPlayerList().getPlayer(var1.secondId);
         if (var3 != null && var3.isAlive() && !var3.getUUID().equals(var1.releasedId)) {
            teleportTrapHome(var3, var1);
         }

         if (var4 != null && var4.isAlive() && !var4.getUUID().equals(var1.releasedId)) {
            teleportTrapHome(var4, var1);
         }

         for (Entry var6 : var1.backup.entrySet()) {
            var1.roomLevel.setBlockAndUpdate((BlockPos)var6.getKey(), (BlockState)var6.getValue());
         }

         var1.backup.clear();
         if (var2) {
            MutableComponent var7 = Component.translatable("hexbingo.msg.trap_end").withStyle(ChatFormatting.GRAY);
            if (var3 != null && !var3.getUUID().equals(var1.releasedId)) {
               var3.sendSystemMessage(var7);
            }

            if (var4 != null && !var4.getUUID().equals(var1.releasedId)) {
               var4.sendSystemMessage(var7);
            }
         }
      }
   }

   public static boolean spySwap(ServerPlayer var0, ServerPlayer var1, ServerPlayer var2) {
      if (!hasHex(var0, "spy")) {
         return false;
      }

      MinecraftServer var3 = var0.level().getServer();
      if (var3 == null) {
         return true;
      }

      if (SPY_USED.contains(var0.getUUID())) {
         var0.sendSystemMessage(Component.translatable("hexbingo.msg.spy_used").withStyle(ChatFormatting.GRAY));
         return true;
      }

      Bingo var4 = Bingo.get(var3.overworld());
      Team var5 = var4.getTeam(var0);
      Team var6 = var4.getTeam(var2);
      boolean var7 = var5 == null
         || var6 == null
         || var2 == var1
         || !var5.hasPlayer(var1)
         || var5.hasPlayer(var2)
         || !var0.isAlive()
         || !var1.isAlive()
         || !var2.isAlive()
         || var1.isSpectator()
         || var2.isSpectator()
         || TRAP != null && (TRAP.involves(var1.getUUID()) || TRAP.involves(var2.getUUID()));
      if (var7) {
         var0.sendSystemMessage(Component.translatable("hexbingo.msg.spy_invalid").withStyle(ChatFormatting.GRAY));
         return true;
      }

      if (shieldBlocks(var2, "spy")) {
         // 目标被免疫护盾抵挡：技能已消耗，标记为已使用
         SPY_USED.add(var0.getUUID());
         var0.sendSystemMessage(Component.translatable("hexbingo.msg.shield_spy").withStyle(ChatFormatting.GRAY));
         return true;
      }

      List var8 = snapshotInventory(var1);
      List var9 = snapshotInventory(var2);
      List var10 = emptyInventory(var1);
      List var11 = emptyInventory(var2);

      for (int var12 = 0; var12 < var8.size() && var12 < var9.size(); var12++) {
         ItemStack var13 = (ItemStack)var8.get(var12);
         ItemStack var14 = (ItemStack)var9.get(var12);
         if (isBoundItem(var13)) {
            var10.set(var12, var13);
         }

         if (isBoundItem(var14)) {
            var11.set(var12, var14);
         }
      }

      ArrayList<ItemStack> var31 = new ArrayList<>();
      ArrayList<ItemStack> var32 = new ArrayList<>();
      int var33 = Math.min(var8.size(), var9.size());

      for (int var15 = 0; var15 < var33; var15++) {
         ItemStack var16 = (ItemStack)var8.get(var15);
         ItemStack var17 = (ItemStack)var9.get(var15);
         if (!var16.isEmpty() && !isBoundItem(var16)) {
            if (((ItemStack)var11.get(var15)).isEmpty()) {
               var11.set(var15, var16);
            } else {
               var31.add(var16);
            }
         }

         if (!var17.isEmpty() && !isBoundItem(var17)) {
            if (((ItemStack)var10.get(var15)).isEmpty()) {
               var10.set(var15, var17);
            } else {
               var32.add(var17);
            }
         }
      }

      List<ItemStack> var34 = fillEmptySlots(var11, var31);
      List<ItemStack> var35 = fillEmptySlots(var10, var32);
      ServerLevel var36 = var1.level();
      ServerLevel var18 = var2.level();
      Vec3 var19 = var1.position();
      Vec3 var20 = var2.position();
      float var21 = var1.getYRot();
      float var22 = var1.getXRot();
      float var23 = var2.getYRot();
      float var24 = var2.getXRot();
      applyInventory(var1, var10);
      applyInventory(var2, var11);
      var1.teleportTo(var18, var20.x, var20.y, var20.z, Set.of(), var23, var24, false);
      var2.teleportTo(var36, var19.x, var19.y, var19.z, Set.of(), var21, var22, false);
      var1.resetFallDistance();
      var2.resetFallDistance();
      var1.setRemainingFireTicks(0);
      var2.setRemainingFireTicks(0);

      for (ItemStack var26 : var34) {
         var1.drop(var26, false);
      }

      for (ItemStack var39 : var35) {
         var2.drop(var39, false);
      }

      SPY_USED.add(var0.getUUID());
      MutableComponent var38 = Component.translatable("hexbingo.msg.spy_success", new Object[]{var1.getDisplayName(), var2.getDisplayName()})
         .withStyle(ChatFormatting.LIGHT_PURPLE);
      HashSet var40 = new HashSet();

      for (ServerPlayer var30 : new ServerPlayer[]{var0, var1, var2}) {
         if (var40.add(var30.getUUID())) {
            var30.sendSystemMessage(var38);
         }
      }

      return true;
   }

   private static void applyInventory(ServerPlayer var0, List<ItemStack> var1) {
      for (int var2 = 0; var2 < var1.size(); var2++) {
         var0.getInventory().setItem(var2, (ItemStack)var1.get(var2));
      }

      var0.containerMenu.broadcastChanges();
   }

   private static List<ItemStack> snapshotInventory(ServerPlayer var0) {
      ArrayList var1 = new ArrayList();
      int var2 = var0.getInventory().getContainerSize();

      for (int var3 = 0; var3 < var2; var3++) {
         var1.add(var0.getInventory().getItem(var3).copy());
      }

      return var1;
   }

   private static List<ItemStack> emptyInventory(ServerPlayer var0) {
      ArrayList var1 = new ArrayList();
      int var2 = var0.getInventory().getContainerSize();

      for (int var3 = 0; var3 < var2; var3++) {
         var1.add(ItemStack.EMPTY);
      }

      return var1;
   }

   private static List<ItemStack> fillEmptySlots(List<ItemStack> var0, List<ItemStack> var1) {
      ArrayList var2 = new ArrayList();
      int var3 = 0;

      for (ItemStack var5 : var1) {
         while (var3 < var0.size() && !((ItemStack)var0.get(var3)).isEmpty()) {
            var3++;
         }

         if (var3 < var0.size()) {
            var0.set(var3, var5);
            var3++;
         } else {
            var2.add(var5);
         }
      }

      return var2;
   }

   public static boolean isBoundItem(ItemStack var0) {
      return var0.isEmpty() ? false : BOUND_ITEM_NAMES.contains(var0.getHoverName().getString());
   }

   public static void sendBoundNoDrop(ServerPlayer var0) {
      var0.sendOverlayMessage(Component.translatable("hexbingo.msg.bound_no_drop"));
   }

   public static void sendBoundNoStore(ServerPlayer var0) {
      var0.sendOverlayMessage(Component.translatable("hexbingo.msg.bound_no_store"));
   }

   public static void onItemToss(ItemTossEvent var0) {
      if (var0.getPlayer() instanceof ServerPlayer var1 && isBoundItem(var0.getEntity().getItem())) {
         ItemStack var2 = var0.getEntity().getItem().copy();
         var0.setCanceled(true);
         var1.getInventory().add(var2);
         var1.containerMenu.broadcastChanges();
         sendBoundNoDrop(var1);
      }
   }

   public static void onItemStackedOnOther(ItemStackedOnOtherEvent var0) {
      if (var0.getPlayer() instanceof ServerPlayer var1) {
         Slot var2 = var0.getSlot();
         boolean var3 = isBoundItem(var0.getCarriedItem())
            && (var2.container != var1.getInventory() || var2.getItem().has(DataComponents.BUNDLE_CONTENTS));
         boolean var4 = isBoundItem(var0.getStackedOnItem()) && var0.getCarriedItem().has(DataComponents.BUNDLE_CONTENTS);
         if (var3 || var4) {
            var0.setCanceled(true);
            var0.setCancellationResult(true);
            sendBoundNoStore(var1);
         }
      }
   }

   public static void tickBoundItems(ServerPlayer var0) {
      extractBoundItems(var0, var0.containerMenu, true);
   }

   public static void extractBoundItems(ServerPlayer var0, AbstractContainerMenu var1, boolean var2sync) {
      if (var1 == null) {
         return;
      }

      boolean var2 = false;

      for (int var3 = 0; var3 < var1.slots.size(); var3++) {
         Slot var4 = var1.slots.get(var3);
         if (var4.container != var0.getInventory() && isBoundItem(var4.getItem())) {
            ItemStack var5 = var4.getItem().copy();
            var4.set(ItemStack.EMPTY);
            var4.setChanged();
            if (var0.getInventory().add(var5)) {
               var2 = true;
            } else {
               var4.set(var5);
               var4.setChanged();
            }
         }
      }

      if (var2) {
         if (var2sync) {
            var1.broadcastChanges();
         }
         sendBoundNoStore(var0);
      }
   }

   public static void resetSpy() {
      SPY_USED.clear();
   }

   private static void giveFaeItem(ServerPlayer var0) {
      if (!hasNamedItem(var0, "精怪魔法")) {
         var0.addItem(questItem(Items.AMETHYST_SHARD, "精怪魔法", "hexbingo.item.fae.lore"));
      }

      var0.sendSystemMessage(Component.translatable("hexbingo.msg.fae_item").withStyle(ChatFormatting.LIGHT_PURPLE));
   }

   public static boolean castFae(ServerPlayer var0) {
      if (!hasHex(var0, "fae_magic")) {
         return false;
      } else {
         long var1 = System.currentTimeMillis();
         Long var3 = FAE_COOLDOWN.get(var0.getUUID());
         if (var3 != null && var1 < var3) {
            long var11 = (var3 - var1 + 999L) / 1000L;
            var0.sendSystemMessage(
               Component.translatable("hexbingo.msg.fae_cooldown", new Object[]{Component.literal(Long.toString(var11))}).withStyle(ChatFormatting.GRAY)
            );
            return true;
         } else if (!(var0.level() instanceof ServerLevel var4)) {
            return false;
         } else {
            Team var12 = team(var0);
            ArrayList<ServerPlayer> var6 = new ArrayList<>();

            // 先收集所有有效的敌方目标（不判断护盾），用于判断技能是否“有目标发动”
            for (ServerPlayer var8 : var4.players()) {
               if (var8 != var0 && !var8.isSpectator() && (var12 == null || !var12.hasPlayer(var8))) {
                  var6.add(var8);
               }
            }

            if (var6.isEmpty()) {
               var0.sendSystemMessage(Component.translatable("hexbingo.msg.fae_no_target").withStyle(ChatFormatting.GRAY));
               return true;
            }

            // 有目标即视为技能已发动：先进入冷却；免疫护盾仅抵挡个别目标的诅咒效果
            FAE_COOLDOWN.put(var0.getUUID(), var1 + skillCooldown(var0, 300000L));
            long var13 = var1 + 60000L;
            int var14 = 0;

            for (ServerPlayer var10 : var6) {
               if (!shieldBlocks(var10, "fae_magic")) {
                  curseFae(var10, var13);
                  var14++;
               }
            }

            var0.sendSystemMessage(
               Component.translatable("hexbingo.msg.fae_cast", new Object[]{Component.literal(Integer.toString(var14))})
                  .withStyle(ChatFormatting.LIGHT_PURPLE)
            );
            return true;
         }
      }
   }

   private static void curseFae(ServerPlayer var0, long var1) {
      boolean var3 = !FAE_UNTIL.containsKey(var0.getUUID());
      FAE_UNTIL.put(var0.getUUID(), var1);
      var0.setInvisible(true);
      AttributeInstance var4 = var0.getAttribute(Attributes.MAX_HEALTH);
      if (var4 != null) {
         var4.addOrUpdateTransientModifier(new AttributeModifier(modifierId("fae_magic", 0), -0.5, Operation.ADD_MULTIPLIED_BASE));
      }

      AttributeInstance var5 = var0.getAttribute(Attributes.MOVEMENT_SPEED);
      if (var5 != null) {
         var5.addOrUpdateTransientModifier(new AttributeModifier(modifierId("fae_magic", 1), 0.15, Operation.ADD_MULTIPLIED_BASE));
      }

      AttributeInstance var6 = var0.getAttribute(Attributes.SCALE);
      if (var6 != null) {
         var6.addOrUpdateTransientModifier(new AttributeModifier(modifierId("fae_magic", 2), -0.3, Operation.ADD_MULTIPLIED_BASE));
      }

      if (var0.getHealth() > var0.getMaxHealth()) {
         var0.setHealth(var0.getMaxHealth());
      }

      ensureFaeDisguise(var0);
      if (var3) {
         var0.sendSystemMessage(Component.translatable("hexbingo.msg.fae_cursed").withStyle(ChatFormatting.LIGHT_PURPLE));
      }
   }

   private static void ensureFaeDisguise(ServerPlayer var0) {
      if (var0.level() instanceof ServerLevel var1) {
         Mob var5 = FAE_DISGUISES.get(var0.getUUID());
         if (var5 != null && var5.isAlive()) {
            // 已有伪装：跨维度时直接带过去，避免在旧维度遗留无 AI 生物
            if (var5.level() != var1) {
               var5.teleportTo(var1, var0.getX(), var0.getY(), var0.getZ(), Set.of(), var0.getYRot(), var0.getXRot(), false);
            }

            return;
         }

         if (var5 != null) {
            var5.discard();
         }

         EntityType var3 = FAE_TYPES.get(ThreadLocalRandom.current().nextInt(FAE_TYPES.size()));
         Mob var4 = (Mob)var3.create(var1, EntitySpawnReason.MOB_SUMMONED);
         if (var4 != null) {
            var4.setPos(var0.getX(), var0.getY(), var0.getZ());
            var4.setYRot(var0.getYRot());
            var4.setYHeadRot(var0.getYHeadRot());
            var4.setNoAi(true);
            var4.setNoGravity(true);
            var4.setInvulnerable(true);
            var4.setSilent(true);
            var4.setPersistenceRequired();
            var4.noPhysics = true;
            var4.setCustomName(Component.literal("精怪魔法"));
            var4.setCustomNameVisible(false);
            var1.addFreshEntity(var4);
            FAE_DISGUISES.put(var0.getUUID(), var4);
         }
      }
   }

   public static void tickFaeDisguises(MinecraftServer var0) {
      if (!FAE_UNTIL.isEmpty()) {
         long var1 = System.currentTimeMillis();

         for (UUID var4 : new ArrayList<>(FAE_UNTIL.keySet())) {
            ServerPlayer var5 = var0.getPlayerList().getPlayer(var4);
            Long var6 = FAE_UNTIL.get(var4);
            Mob var7 = FAE_DISGUISES.get(var4);
            if (var5 == null) {
               if (var7 != null) {
                  var7.discard();
                  FAE_DISGUISES.remove(var4);
               }

               FAE_UNTIL.remove(var4);
            } else if (var6 != null && var1 < var6) {
               if (var7 != null && var7.isAlive()) {
                  if (var7.level() != var5.level()) {
                     // 跨维度时把伪装一起带过去，避免旧维度留下无 AI 生物
                     var7.teleportTo((ServerLevel)var5.level(), var5.getX(), var5.getY(), var5.getZ(), Set.of(), var5.getYRot(), var5.getXRot(), false);
                  }

                  var7.setPos(var5.getX(), var5.getY(), var5.getZ());
                  var7.setYRot(var5.getYRot());
                  var7.setYHeadRot(var5.getYHeadRot());
                  var7.setXRot(0.0F);
                  var7.setDeltaMovement(Vec3.ZERO);
               } else {
                  if (var7 != null) {
                     var7.discard();
                     FAE_DISGUISES.remove(var4);
                  }

                  ensureFaeDisguise(var5);
               }
            } else {
               restoreFae(var5);
               if (var6 != null) {
                  var5.sendSystemMessage(Component.translatable("hexbingo.msg.fae_end").withStyle(ChatFormatting.LIGHT_PURPLE));
               }
            }

            if (var5 != null && var6 != null && var1 < var6 && var0.getTickCount() % 20 == 0) {
               int var8 = Math.max(0, (int)((var6 - var1 + 999L) / 1000L));
               var5.sendOverlayMessage(Component.translatable("hexbingo.msg.fae_timer", new Object[]{var8}).withStyle(ChatFormatting.LIGHT_PURPLE));
            }
         }
      }
   }

   public static void ensureFae(ServerPlayer var0) {
      Long var1 = FAE_UNTIL.get(var0.getUUID());
      if (var1 != null) {
         if (System.currentTimeMillis() >= var1) {
            restoreFae(var0);
         } else {
            curseFae(var0, var1);
         }
      }
   }

   public static void onFaeDeath(ServerPlayer var0) {
      Mob var1 = FAE_DISGUISES.remove(var0.getUUID());
      if (var1 != null) {
         var1.discard();
      }
   }

   public static void onFaeLogout(ServerPlayer var0) {
      Mob var1 = FAE_DISGUISES.remove(var0.getUUID());
      if (var1 != null) {
         var1.discard();
      }
   }

   public static void restoreFae(ServerPlayer var0) {
      boolean var1 = FAE_UNTIL.remove(var0.getUUID()) != null;
      Mob var2 = FAE_DISGUISES.remove(var0.getUUID());
      if (var2 != null) {
         var2.discard();
      }

      if (var1) {
         var0.setInvisible(false);
      }

      removeFaeModifier(var0, Attributes.MAX_HEALTH, 0);
      removeFaeModifier(var0, Attributes.MOVEMENT_SPEED, 1);
      removeFaeModifier(var0, Attributes.SCALE, 2);
      if (var0.getHealth() > var0.getMaxHealth()) {
         var0.setHealth(var0.getMaxHealth());
      }
   }

   private static void removeFaeModifier(ServerPlayer var0, Holder<Attribute> var1, int var2) {
      AttributeInstance var3 = var0.getAttribute(var1);
      if (var3 != null) {
         var3.removeModifier(modifierId("fae_magic", var2));
      }
   }

   private static void enforceLockedSlots(ServerPlayer var0, HexEffect var1) {
      int var2 = Math.max(0, var1.min);
      int var3 = Math.min(35, var1.max);
      if (var3 >= var2) {
         boolean var4 = false;

         for (int var5 = var2; var5 <= var3; var5++) {
            ItemStack var6 = var0.getInventory().getItem(var5);
            if (!isLockMarker(var6)) {
               if (!var6.isEmpty()) {
                  ItemStack var7 = var6.copy();
                  var0.getInventory().setItem(var5, ItemStack.EMPTY);
                  int var8 = -1;

                  for (int var9 = 0; var9 < var2; var9++) {
                     if (var0.getInventory().getItem(var9).isEmpty()) {
                        var8 = var9;
                        break;
                     }
                  }

                  if (var8 >= 0) {
                     var0.getInventory().setItem(var8, var7);
                  } else {
                     var0.drop(var7, false);
                  }
               }

               var0.getInventory().setItem(var5, lockMarker());
               var4 = true;
            }
         }

         for (int var10 = 0; var10 < var0.getInventory().getContainerSize(); var10++) {
            if ((var10 < var2 || var10 > var3) && isLockMarker(var0.getInventory().getItem(var10))) {
               var0.getInventory().setItem(var10, ItemStack.EMPTY);
               var4 = true;
            }
         }

         if (var4) {
            var0.containerMenu.broadcastChanges();
         }
      }
   }

   public static void clearLockedSlotMarkers(ServerPlayer var0) {
      boolean var1 = false;

      for (int var2 = 0; var2 < var0.getInventory().getContainerSize(); var2++) {
         if (isLockMarker(var0.getInventory().getItem(var2))) {
            var0.getInventory().setItem(var2, ItemStack.EMPTY);
            var1 = true;
         }
      }

      if (var1) {
         var0.containerMenu.broadcastChanges();
      }
   }

   private static ItemStack lockMarker() {
      ItemStack var0 = new ItemStack(Items.BARRIER);
      var0.set(DataComponents.CUSTOM_NAME, Component.literal("玻璃大炮锁定槽"));
      return var0;
   }

   private static boolean isLockMarker(ItemStack var0) {
      return var0.is(Items.BARRIER) && var0.getHoverName().getString().equals("玻璃大炮锁定槽");
   }

   private static void lockEnemyBreaking(ServerPlayer var0, int var1) {
      if (var0.level() instanceof ServerLevel var2) {
         Team var8 = team(var0);
         long var4 = System.currentTimeMillis() + Math.max(1, var1) * 1000L;

         for (ServerPlayer var7 : var2.players()) {
            if (var7 != var0 && (var8 == null || !var8.hasPlayer(var7))) {
               if (shieldBlocks(var7, "block_break_block")) {
                  continue;
               }

               BREAK_LOCK.put(var7.getUUID(), var4);
               var7.sendSystemMessage(Component.translatable("hexbingo.msg.break_locked", new Object[]{var1}).withStyle(ChatFormatting.RED));
            }
         }
      }
   }

   private static void giveUntouchableItem(ServerPlayer var0) {
      var0.addItem(questItem(Items.BARRIER, "你摸不到", "hexbingo.item.untouchable.lore"));
   }

   public static boolean castUntouchable(ServerPlayer var0) {
      if (!hasHex(var0, "untouchable")) {
         return false;
      }

      long var1 = System.currentTimeMillis();
      Long var3 = UNTOUCHABLE_COOLDOWN.get(var0.getUUID());
      if (var3 != null && var1 < var3) {
         long var4 = (var3 - var1 + 999L) / 1000L;
         var0.sendSystemMessage(
            Component.translatable("hexbingo.msg.untouchable_cooldown", new Object[]{Component.literal(Long.toString(var4))})
               .withStyle(ChatFormatting.GRAY)
         );
         return true;
      }

      lockEnemyBreaking(var0, 30);
      UNTOUCHABLE_COOLDOWN.put(var0.getUUID(), var1 + skillCooldown(var0, UNTOUCHABLE_COOLDOWN_MS));
      var0.sendSystemMessage(Component.translatable("hexbingo.msg.untouchable_used").withStyle(ChatFormatting.AQUA));
      return true;
   }

   public static long breakLockRemainingMs(ServerPlayer var0) {
      Long var1 = BREAK_LOCK.get(var0.getUUID());
      if (var1 == null) {
         return 0L;
      }

      long var2 = var1 - System.currentTimeMillis();
      return Math.max(0L, var2);
   }

   public static boolean isBreakLocked(ServerPlayer var0) {
      Long var1 = BREAK_LOCK.get(var0.getUUID());
      return var1 != null && System.currentTimeMillis() < var1;
   }

   public static void clearBreakLocks() {
      BREAK_LOCK.clear();
   }

   public static boolean isCraftingBlocked(ServerPlayer var0) {
      return hasHex(var0, "back_to_basics");
   }

   private static void playSongBuff(ServerPlayer var0) {
      MinecraftServer var1 = var0.level().getServer();
      SoundEvent var2 = SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath("hexbingo", "guyongzhe"));
      if (var1 != null) {
         for (ServerPlayer var4 : var1.getPlayerList().getPlayers()) {
            var4.level().playSound(null, var4.getX(), var4.getY(), var4.getZ(), var2, SoundSource.RECORDS, 1.0F, 1.0F);
         }
      }

      Holder var5 = randomSongBuff(var0);
      SONG_BUFFS.put(var0.getUUID(), var5);
      var0.addEffect(new MobEffectInstance(var5, -1, 0, true, false, false));
      var0.sendSystemMessage(
         Component.translatable("hexbingo.msg.song_buff", new Object[]{((MobEffect)var5.value()).getDisplayName()}).withStyle(ChatFormatting.LIGHT_PURPLE)
      );
   }

   private static void playDechiSong(ServerPlayer var0, int var1) {
      SoundEvent var2 = SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath("hexbingo", "dechi_song"));
      var0.connection
         .send(
            new ClientboundSoundPacket(
               Holder.direct(var2), SoundSource.RECORDS, var0.getX(), var0.getY(), var0.getZ(), 1.0F, 1.0F, var0.getRandom().nextLong()
            )
         );

      var0.addEffect(new MobEffectInstance(MobEffects.LUCK, Math.max(1, var1) * 20, 2 + ThreadLocalRandom.current().nextInt(3), false, true, true));
      Registry<Enchantment> var6 = var0.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
      Holder<Enchantment> var7 = var6.wrapAsHolder((Enchantment)var6.getValueOrThrow(Enchantments.FORTUNE));
      int var8 = 0;

      for (int var9 = 0; var9 < var0.getInventory().getContainerSize(); var9++) {
         ItemStack var10 = var0.getInventory().getItem(var9);
         if (!var10.isEmpty() && var10.is(ItemTags.PICKAXES)) {
            int var11 = Math.max(2 + ThreadLocalRandom.current().nextInt(2), EnchantmentHelper.getItemEnchantmentLevel(var7, var10));
            EnchantmentHelper.updateEnchantments(var10, var12 -> var12.set(var7, var11));
            var8++;
         }
      }

      ItemStack var13 = var0.getOffhandItem();
      if (!var13.isEmpty() && var13.is(ItemTags.PICKAXES)) {
         int var14 = Math.max(2 + ThreadLocalRandom.current().nextInt(2), EnchantmentHelper.getItemEnchantmentLevel(var7, var13));
         EnchantmentHelper.updateEnchantments(var13, var15 -> var15.set(var7, var14));
         var8++;
      }

      var0.containerMenu.broadcastChanges();
      var0.sendSystemMessage(Component.translatable("hexbingo.msg.dechi_song", new Object[]{var8}).withStyle(ChatFormatting.GOLD));
   }

   private static void ensureSongBuff(ServerPlayer var0) {
      Holder var1 = SONG_BUFFS.get(var0.getUUID());
      if (var1 == null) {
         var1 = randomSongBuff(var0);
         SONG_BUFFS.put(var0.getUUID(), var1);
      }

      if (var0.getEffect(var1) == null) {
         var0.addEffect(new MobEffectInstance(var1, -1, 0, true, false, false));
      }
   }

   private static Holder<MobEffect> randomSongBuff(ServerPlayer var0) {
      Holder<MobEffect>[] var1 = new Holder[]{
         MobEffects.SPEED,
         MobEffects.HASTE,
         MobEffects.STRENGTH,
         MobEffects.RESISTANCE,
         MobEffects.REGENERATION,
         MobEffects.ABSORPTION,
         MobEffects.NIGHT_VISION,
         MobEffects.WATER_BREATHING,
         MobEffects.FIRE_RESISTANCE,
         MobEffects.JUMP_BOOST
      };
      ArrayList<Holder<MobEffect>> var2 = new ArrayList<>();

      for (Holder<MobEffect> var3 : var1) {
         MobEffectInstance var4 = var0.getEffect(var3);
         if (var4 == null || !var4.isInfiniteDuration()) {
            var2.add(var3);
         }
      }

      if (var2.isEmpty()) {
         for (Holder<MobEffect> var6 : var1) {
            var2.add(var6);
         }
      }

      return var2.get(ThreadLocalRandom.current().nextInt(var2.size()));
   }

   private static void utopia(ServerPlayer var0) {
      for (MobEffectInstance var2 : new ArrayList<>(var0.getActiveEffects())) {
         var0.removeEffect(var2.getEffect());
      }

      Holder<MobEffect>[] var3 = new Holder[]{
         MobEffects.SPEED,
         MobEffects.HASTE,
         MobEffects.STRENGTH,
         MobEffects.RESISTANCE,
         MobEffects.REGENERATION,
         MobEffects.ABSORPTION,
         MobEffects.NIGHT_VISION,
         MobEffects.WATER_BREATHING,
         MobEffects.FIRE_RESISTANCE,
         MobEffects.JUMP_BOOST,
         MobEffects.LUCK,
         MobEffects.HEALTH_BOOST,
         MobEffects.SATURATION,
         MobEffects.DOLPHINS_GRACE,
         MobEffects.SLOW_FALLING,
         MobEffects.SLOWNESS,
         MobEffects.MINING_FATIGUE,
         MobEffects.WEAKNESS,
         MobEffects.POISON,
         MobEffects.HUNGER,
         MobEffects.NAUSEA,
         MobEffects.BLINDNESS,
         MobEffects.DARKNESS,
         MobEffects.UNLUCK
      };
      ArrayList<Holder<MobEffect>> var4 = new ArrayList<>();

      for (Holder<MobEffect> var8 : var3) {
         var4.add(var8);
      }

      MutableComponent var5 = Component.empty();
      int var6 = Math.min(5, var4.size());

      for (int var7 = 0; var7 < var6; var7++) {
         Holder<MobEffect> var9 = var4.remove(ThreadLocalRandom.current().nextInt(var4.size()));
         var0.addEffect(new MobEffectInstance(var9, -1, 0, true, false, false));
         if (var7 > 0) {
            var5.append(Component.literal("、"));
         }

         var5.append(((MobEffect)var9.value()).getDisplayName());
      }

      var0.sendSystemMessage(Component.translatable("hexbingo.msg.utopia", new Object[]{var5}).withStyle(ChatFormatting.GOLD));
   }

   private static void killStack(ServerPlayer var0, LivingEntity var1) {
      int var2 = 50;
      boolean var3 = false;

      for (Hex var5 : hexes(var0)) {
         for (HexEffect var7 : var5.effects()) {
            if (var7.type.equals("kill_stack_crate")) {
               var3 = true;
               if (var7.interval > 0) {
                  var2 = var7.interval;
               }
            }
         }
      }

      if (var3) {
         int var8 = KILL_STACKS.merge(var0.getUUID(), 1, Integer::sum);
         if (var8 % Math.max(1, var2) == 0) {
            spawnLootChest(var0, var8 / Math.max(1, var2));
            giveStackExtras(var0, var8);
         }

         if (var8 == 165) {
            grantRandomHex(var0, "prismatic", "stack");
         }

         if (var8 == 300) {
            stackDragonVictory(var0);
         }
      }
   }

   private static void stackDragonVictory(ServerPlayer var0) {
      MinecraftServer var1 = var0.level().getServer();
      if (var1 != null) {
         Bingo var2 = Bingo.get(var0.level());
         if (var2.running()) {
            Team var3 = var2.getTeam(var0);
            if (var3 != null) {
               Component var4 = Component.translatable("hexbingo.msg.stack_dragon_win").withStyle(ChatFormatting.RED, ChatFormatting.BOLD);
               var1.getPlayerList().getPlayers().forEach(var1x -> var1x.sendSystemMessage(var4));

               try {
                  Method var5 = Bingo.class.getDeclaredMethod("setWin", Team.class);
                  var5.setAccessible(true);
                  var5.invoke(var2, var3);
               } catch (Exception var6) {
                  HexBingo.LOGGER.error("Failed to force a stack dragon victory", var6);
               }
            }
         }
      }
   }

   private static void giveStackExtras(ServerPlayer var0, int var1) {
      ThreadLocalRandom var2 = ThreadLocalRandom.current();
      if (var1 >= 60) {
         var0.addItem(stackBook(var0, 2 + var2.nextInt(4)));
      }

      if (var1 >= 105) {
         Item[] var3 = new Item[]{
            Items.NETHERITE_HELMET,
            Items.NETHERITE_CHESTPLATE,
            Items.NETHERITE_LEGGINGS,
            Items.NETHERITE_BOOTS,
            Items.NETHERITE_SWORD,
            Items.NETHERITE_PICKAXE,
            Items.NETHERITE_AXE
         };
         var0.addItem(new ItemStack(var3[var2.nextInt(var3.length)]));
      }

      if (var1 >= 120) {
         var0.addItem(new ItemStack(Items.BLAZE_POWDER, 4 + var2.nextInt(5)));
      }

      if (var1 >= 135) {
         var0.addItem(new ItemStack(Items.WITHER_SKELETON_SKULL));
      }

      if (var1 >= 150) {
         var0.addItem(new ItemStack(Items.NETHER_STAR));
      }
   }

   private static ItemStack stackBook(ServerPlayer var0, int var1) {
      ItemStack var2 = new ItemStack(Items.ENCHANTED_BOOK);
      Registry<Enchantment> var3 = var0.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
      List<Holder<Enchantment>> var4 = new ArrayList<>(var3.listElements().toList());
      Collections.shuffle(var4, ThreadLocalRandom.current());
      ItemEnchantments.Mutable var5 = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);

      for (int var6 = 0; var6 < Math.min(var1, var4.size()); var6++) {
         var5.set(var4.get(var6), 1 + ThreadLocalRandom.current().nextInt(3));
      }

      var2.set(DataComponents.STORED_ENCHANTMENTS, var5.toImmutable());
      return var2;
   }

   private static void spawnLootChest(ServerPlayer var0, int var1) {
      if (var0.level() instanceof ServerLevel var2) {
         ThreadLocalRandom var11 = ThreadLocalRandom.current();
         BlockPos var4 = var0.blockPosition();

         for (int var5 = 0; var5 < 16; var5++) {
            BlockPos var6 = var4.offset(var11.nextInt(5) - 2, var11.nextInt(3), var11.nextInt(5) - 2);
            if (var2.getBlockState(var6).isAir()) {
               var2.setBlockAndUpdate(var6, Blocks.CHEST.defaultBlockState());
               if (var2.getBlockEntity(var6) instanceof ChestBlockEntity var7) {
                  int var13 = Math.min(27, 3 + var1 * 2);

                  for (int var9 = 0; var9 < var13; var9++) {
                     Item var10 = lootItem(var11, var1);
                     var7.setItem(var9, new ItemStack(var10, 1 + var11.nextInt(3)));
                  }

                  var7.setChanged();
                  // 叠角龙开箱：不死图腾粒子 + 经验“叮”声
                  var2.sendParticles(ParticleTypes.TOTEM_OF_UNDYING, var6.getX() + 0.5, var6.getY() + 1.0, var6.getZ() + 0.5, 40, 0.6, 0.8, 0.6, 0.45);
                  var2.playSound(
                     null, var6.getX() + 0.5, var6.getY() + 0.5, var6.getZ() + 0.5, SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 1.0F, 1.2F
                  );
                  var0.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 1.0F, 1.2F);
                  return;
               }
            }
         }

         for (int var12 = 0; var12 < 3 + var1; var12++) {
            var0.addItem(new ItemStack(lootItem(var11, var1)));
         }
      }
   }

   private static Item lootItem(ThreadLocalRandom var0, int var1) {
      ArrayList var2 = new ArrayList<>(RANDOM_ITEMS);
      if (var1 >= 2) {
         var2.add(Items.DIAMOND);
         var2.add(Items.EMERALD);
         var2.add(Items.GOLDEN_APPLE);
         var2.add(Items.ENDER_PEARL);
      }

      if (var1 >= 4) {
         var2.add(Items.NETHERITE_SCRAP);
         var2.add(Items.ENCHANTED_GOLDEN_APPLE);
      }

      return (Item)var2.get(var0.nextInt(var2.size()));
   }

   public static void blockDrops(BlockDropsEvent var0) {
      if (var0.getBreaker() instanceof ServerPlayer var1) {
         double var4 = dropMultiplier(var1, var0.getTool(), true);
         if (var4 > 1.0) {
            multiplyDrops(var0.getLevel(), var0.getDrops(), var4);
         }

         if (hasEffect(var1, "sand_gold") && var0.getState().is(BlockTags.SAND) && ThreadLocalRandom.current().nextDouble() < 0.03) {
            var0.getDrops()
               .add(
                  new ItemEntity(
                     var0.getLevel(), var0.getPos().getX() + 0.5, var0.getPos().getY() + 0.5, var0.getPos().getZ() + 0.5, new ItemStack(Items.GOLD_NUGGET)
                  )
               );
         }

         if (hasHex(var1, "auto_smelt")) {
            autoSmeltDrops(var1, var0.getDrops());
         }

         if (hasHex(var1, "scavenger")) {
            scavengerDrop(var1, var0);
         }
      }
   }

   public static void livingDrops(LivingDropsEvent var0) {
      ServerPlayer var20 = killCredit(var0);

      if (var20 != null && var0.getEntity().level() instanceof ServerLevel var21 && LOUIS_PENDING.contains(var20.getUUID()) && hasHex(var20, "louis_xvi")) {
         louisXviDrop(var20, var0, var21);
      }

      if (var0.getSource().getEntity() instanceof ServerPlayer var1 && var0.getEntity().level() instanceof ServerLevel var2) {
         double var6 = dropMultiplier(var1, ItemStack.EMPTY, false);
         if (var6 > 1.0) {
            multiplyDrops(var2, var0.getDrops(), var6);
         }

         if (var0.getEntity() instanceof Sheep && hasHex(var1, "sheep_wool")) {
            Entity var7 = var0.getEntity();

            for (int var8 = 0; var8 < 2; var8++) {
               Item var9 = Items.WOOL.pick(DyeColor.byId(ThreadLocalRandom.current().nextInt(16)));
               var0.getDrops().add(new ItemEntity(var2, var7.getX(), var7.getY() + 0.5, var7.getZ(), new ItemStack(var9)));
            }
         }

         if (var0.getEntity() instanceof EnderMan && hasHex(var1, "ender_slayer")) {
            enderSlayerDrop(var1, var0, var2);
         }

         if (LOUIS_PENDING.contains(var1.getUUID()) && hasHex(var1, "louis_xvi")) {
            louisXviDrop(var1, var0, var2);
         }
      }
   }

   // 末影杀手：击杀末影人 1% 掉落附魔书（精准采集 0.2% / 经验修补 0.4% / 无限 0.4%）
   private static void enderSlayerDrop(ServerPlayer var0, LivingDropsEvent var1, ServerLevel var2) {
      ThreadLocalRandom var3 = ThreadLocalRandom.current();
      if (var3.nextDouble() < 0.01) {
         Registry<Enchantment> var4 = var0.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
         double var5 = var3.nextDouble();
         Holder<Enchantment> var6;
         if (var5 < 0.2) {
            var6 = var4.wrapAsHolder((Enchantment)var4.getValueOrThrow(Enchantments.SILK_TOUCH));
         } else if (var5 < 0.6) {
            var6 = var4.wrapAsHolder((Enchantment)var4.getValueOrThrow(Enchantments.MENDING));
         } else {
            var6 = var4.wrapAsHolder((Enchantment)var4.getValueOrThrow(Enchantments.INFINITY));
         }

         ItemStack var7 = EnchantmentHelper.createBook(new EnchantmentInstance(var6, 1));
         Entity var8 = var1.getEntity();
         var1.getDrops().add(new ItemEntity(var2, var8.getX(), var8.getY() + 0.5, var8.getZ(), var7));
         var0.sendSystemMessage(Component.translatable("hexbingo.msg.ender_slayer", new Object[]{var7.getHoverName()}).withStyle(ChatFormatting.GOLD));
      }
   }

   private static ServerPlayer killCredit(LivingDropsEvent var0) {
      Entity var1 = var0.getSource().getEntity();
      if (var1 instanceof ServerPlayer var2) {
         return var2;
      } else {
         return var1 instanceof Projectile var3 && var3.getOwner() instanceof ServerPlayer var4 ? var4 : null;
      }
   }

   private static double dropMultiplier(ServerPlayer var0, ItemStack var1, boolean var2) {
      double var3 = 1.0;

      for (Hex var6 : hexes(var0)) {
         for (HexEffect var8 : var6.effects()) {
            if (var8.type.equals("drop_multiplier")) {
               if (var8.amount > 1.0) {
                  double var9 = var8.amount;
                  if (var2) {
                     Registry var11 = var0.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
                     Holder var12 = var11.wrapAsHolder((Enchantment)var11.getValueOrThrow(Enchantments.FORTUNE));
                     int var13 = EnchantmentHelper.getItemEnchantmentLevel(var12, var1);
                     int var14 = (int)var0.getAttributeValue(Attributes.LUCK);
                     var9 *= 1.0 + 0.25 * var13 + 0.25 * var14;
                  }

                  var3 = Math.max(var3, var9);
               } else {
                  double var15 = var8.percent;
                  if (!var2) {
                     Registry var16 = var0.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
                     Holder var17 = var16.wrapAsHolder((Enchantment)var16.getValueOrThrow(Enchantments.LOOTING));
                     var15 += 0.25 * EnchantmentHelper.getItemEnchantmentLevel(var17, var0.getMainHandItem());
                  }

                  var3 += var15;
               }
            }
         }
      }

      return var3;
   }

   private static void multiplyDrops(ServerLevel var0, Collection<ItemEntity> var1, double var2) {
      ArrayList<ItemEntity> var4 = new ArrayList<>();

      for (ItemEntity var6 : var1) {
         ItemStack var7 = var6.getItem();
         if (!var7.isEmpty()) {
            int var8 = Math.max(1, var7.getMaxStackSize());
            int var9 = (int)Math.round(var7.getCount() * var2);
            if (var9 > var7.getCount()) {
               var7.setCount(Math.min(var8, var9));
               int var10 = var9 - var7.getCount();

               while (var10 > 0) {
                  int var11 = Math.min(var8, var10);
                  ItemEntity var12 = new ItemEntity(var0, var6.getX(), var6.getY(), var6.getZ(), new ItemStack(var7.getItem(), var11));
                  var12.setDeltaMovement(var6.getDeltaMovement());
                  var4.add(var12);
                  var10 -= var11;
               }
            }
         }
      }

      for (ItemEntity var14 : var4) {
         var0.addFreshEntity(var14);
      }
   }

   public static void crafted(ItemCraftedEvent var0) {
      if (var0.getEntity() instanceof ServerPlayer var1) {
         if (hasHex(var1, "archmage")) {
            Container var7 = var0.getInventory();
            ThreadLocalRandom var3 = ThreadLocalRandom.current();

            for (int var4 = 0; var4 < var7.getContainerSize(); var4++) {
               ItemStack var5 = var7.getItem(var4);
               if (!var5.isEmpty() && var3.nextDouble() < 0.1) {
                  ItemStack var6 = var5.copy();
                  var6.setCount(1);
                  var1.addItem(var6);
               }
            }
         }

         if (hasHex(var1, "inventor")) {
            ItemStack var8 = var0.getCrafting();
            if (!var8.isEmpty() && isToolOrArmor(var8)) {
               enchantStack(var1, var8);
            }
         }
      }
   }

   private static boolean isToolOrArmor(ItemStack var0) {
      return var0.is(ItemTags.SWORDS)
         || var0.is(ItemTags.AXES)
         || var0.is(ItemTags.PICKAXES)
         || var0.is(ItemTags.SHOVELS)
         || var0.is(ItemTags.HOES)
         || var0.is(ItemTags.ARMOR_ENCHANTABLE);
   }

   private static void enchantStack(ServerPlayer var0, ItemStack var1) {
      Registry<Enchantment> var2 = var0.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
      List<Holder<Enchantment>> var3 = var2.entrySet().stream().map(var1x -> var2.wrapAsHolder((Enchantment)var1x.getValue())).toList();
      if (!var3.isEmpty()) {
         ThreadLocalRandom var4 = ThreadLocalRandom.current();
         int var5 = 1 + var4.nextInt(5);

         for (int var6 = 0; var6 < var5; var6++) {
            var1.enchant((Holder)var3.get(var4.nextInt(var3.size())), 1 + var4.nextInt(3));
         }

         var0.containerMenu.broadcastChanges();
      }
   }

   private static void grantRandomHex(ServerPlayer var0, String var1) {
      grantRandomHex(var0, var1, null);
   }

   private static void grantRandomHex(ServerPlayer var0, String var1, String prefix) {
      MinecraftServer var2 = var0.level().getServer();
      if (var2 != null && GRANT_DEPTH.get() < 4) {
         HexGameData var3 = HexGameData.get(var2);
         if (var3.mayhem()) {
            if (var3.hexesLocked()) {
               var0.sendSystemMessage(Component.translatable("hexbingo.msg.hex_locked").withStyle(ChatFormatting.DARK_GRAY));
               return;
            }

            UUID var4 = var0.getUUID();
            ArrayList var5 = new ArrayList();

            for (Hex var7 : HexRegistry.byColor(HexColor.byId(var1))) {
               if (!var3.hexes(var4).contains(var7.id())
                  && !var7.id().equals(HexRegistry.OFFER_UPGRADE)
                  && !var7.id().equals(HexRegistry.ASCETIC)) {
                  var5.add(var7);
               }
            }

            if (!var5.isEmpty()) {
               Hex var12 = (Hex)var5.get(ThreadLocalRandom.current().nextInt(var5.size()));
               GRANT_DEPTH.set(GRANT_DEPTH.get() + 1);

               try {
                  if (var3.addHex(var4, var12.id()) && prefix != null) {
                     var3.markGranted(var4, var12.id(), prefix);
                  }

                  List var13 = var12.effects();

                  for (int var8 = 0; var8 < var13.size(); var8++) {
                     applyImmediate(var0, var12.id(), var8, (HexEffect)var13.get(var8));
                  }

                  var0.sendSystemMessage(
                     Component.translatable("hexbingo.msg.random_hex", new Object[]{Component.translatable("hexbingo.hex." + var12.id() + ".name")})
                        .withStyle(ChatFormatting.LIGHT_PURPLE)
                  );
               } finally {
                  GRANT_DEPTH.set(GRANT_DEPTH.get() - 1);
               }
            }
         }
      }
   }

   public static int grantRandomHexToAll(MinecraftServer var0) {
      HexGameData var1 = HexGameData.get(var0);
      if (!var1.mayhem() || var1.hexesLocked()) {
         return 0;
      }

      Bingo var2 = Bingo.get(var0.overworld());
      int var3 = 0;

      for (ServerPlayer var5 : var0.getPlayerList().getPlayers()) {
         if (var2.getTeam(var5) != null && grantRandomAnyHex(var5)) {
            var3++;
         }
      }

      return var3;
   }

   private static boolean grantRandomAnyHex(ServerPlayer var0) {
      MinecraftServer var1 = var0.level().getServer();
      if (var1 != null && GRANT_DEPTH.get() < 4) {
         HexGameData var2 = HexGameData.get(var1);
         if (!var2.mayhem() || var2.hexesLocked()) {
            return false;
         }

         UUID var3 = var0.getUUID();
         ArrayList var4 = new ArrayList();

         for (HexColor var8 : HexColor.values()) {
            for (Hex var10 : HexRegistry.byColor(var8)) {
               if (!var2.hexes(var3).contains(var10.id())
                  && !var10.id().equals(HexRegistry.OFFER_UPGRADE)
                  && !var10.id().equals(HexRegistry.ASCETIC)) {
                  var4.add(var10);
               }
            }
         }

         if (var4.isEmpty()) {
            return false;
         }

         Hex var14 = (Hex)var4.get(ThreadLocalRandom.current().nextInt(var4.size()));
         GRANT_DEPTH.set(GRANT_DEPTH.get() + 1);

         try {
            var2.addHex(var3, var14.id());
            List var15 = var14.effects();

            for (int var16 = 0; var16 < var15.size(); var16++) {
               applyImmediate(var0, var14.id(), var16, (HexEffect)var15.get(var16));
            }

            var0.sendSystemMessage(
               Component.translatable("hexbingo.msg.random_hex", new Object[]{Component.translatable("hexbingo.hex." + var14.id() + ".name")})
                  .withStyle(ChatFormatting.LIGHT_PURPLE)
            );
         } finally {
            GRANT_DEPTH.set(GRANT_DEPTH.get() - 1);
         }

         return true;
      } else {
         return false;
      }
   }

   private static void applyAbsorption(ServerPlayer var0, double var1, int var3) {
      int var4 = Math.max(0, (int)Math.round(var1 / 4.0) - 1);
      var0.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, var3, var4, false, false, false));
   }

   private static Team team(ServerPlayer var0) {
      return Bingo.get(var0.level()).getTeam(var0);
   }

   private static List<ServerPlayer> onlineTeamPlayers(MinecraftServer var0, Team var1) {
      ArrayList var2 = new ArrayList();
      if (var0 == null) {
         return var2;
      }

      for (UUID var4 : var1.getPlayers()) {
         ServerPlayer var5 = var0.getPlayerList().getPlayer(var4);
         if (var5 != null) {
            var2.add(var5);
         }
      }

      return var2;
   }

   public static List<Hex> hexes(ServerPlayer var0) {
      MinecraftServer var1 = var0.level().getServer();
      if (var1 == null) {
         return List.of();
      }

      HexGameData var2 = HexGameData.get(var1);
      if (!var2.mayhem()) {
         return List.of();
      }

      List<String> var3 = var2.hexes(var0.getUUID());
      if (var3.isEmpty()) {
         return List.of();
      }

      ArrayList<Hex> var4 = new ArrayList<>(var3.size());

      for (String var6 : var3) {
         Hex var7 = HexRegistry.byId(var6);
         if (var7 != null) {
            var4.add(var7);
         }
      }

      return var4;
   }

   private static Identifier modifierId(String var0, int var1) {
      return Identifier.fromNamespaceAndPath("hexbingo", "hex/" + var0 + "/" + var1);
   }

   private static Operation operation(String var0) {
      return switch (var0) {
         case "add_multiplied_base" -> Operation.ADD_MULTIPLIED_BASE;
         case "add_multiplied_total" -> Operation.ADD_MULTIPLIED_TOTAL;
         default -> Operation.ADD_VALUE;
      };
   }

   private static Holder<Attribute> attribute(String var0) {
      return switch (var0) {
         case "max_health" -> Attributes.MAX_HEALTH;
         case "armor" -> Attributes.ARMOR;
         case "armor_toughness" -> Attributes.ARMOR_TOUGHNESS;
         case "attack_damage" -> Attributes.ATTACK_DAMAGE;
         case "attack_speed" -> Attributes.ATTACK_SPEED;
         case "movement_speed" -> Attributes.MOVEMENT_SPEED;
         case "knockback_resistance" -> Attributes.KNOCKBACK_RESISTANCE;
         case "luck" -> Attributes.LUCK;
         case "block_break_speed" -> Attributes.BLOCK_BREAK_SPEED;
         case "block_interaction_range" -> Attributes.BLOCK_INTERACTION_RANGE;
         case "entity_interaction_range" -> Attributes.ENTITY_INTERACTION_RANGE;
         case "scale" -> Attributes.SCALE;
         default -> null;
      };
   }

   private static Holder<MobEffect> effect(String var0) {
      return switch (var0) {
         case "speed" -> MobEffects.SPEED;
         case "haste" -> MobEffects.HASTE;
         case "strength" -> MobEffects.STRENGTH;
         case "resistance" -> MobEffects.RESISTANCE;
         case "regeneration" -> MobEffects.REGENERATION;
         case "absorption" -> MobEffects.ABSORPTION;
         case "night_vision" -> MobEffects.NIGHT_VISION;
         case "water_breathing" -> MobEffects.WATER_BREATHING;
         case "fire_resistance" -> MobEffects.FIRE_RESISTANCE;
         case "jump_boost" -> MobEffects.JUMP_BOOST;
         case "village_hero" -> MobEffects.HERO_OF_THE_VILLAGE;
         case "slow_falling" -> MobEffects.SLOW_FALLING;
         case "dolphins_grace" -> MobEffects.DOLPHINS_GRACE;
         case "hunger" -> MobEffects.HUNGER;
         default -> null;
      };
   }

   private static Item item(String var0) {
      return switch (var0) {
         case "golden_apple" -> Items.GOLDEN_APPLE;
         case "ender_pearl" -> Items.ENDER_PEARL;
         case "cooked_beef" -> Items.COOKED_BEEF;
         case "oak_planks" -> Items.OAK_PLANKS;
         case "arrow" -> Items.ARROW;
         case "experience_bottle" -> Items.EXPERIENCE_BOTTLE;
         case "soul_sand" -> Items.SOUL_SAND;
         case "wither_skeleton_skull" -> Items.WITHER_SKELETON_SKULL;
         case "flint_and_steel" -> Items.FLINT_AND_STEEL;
         case "blaze_powder" -> Items.BLAZE_POWDER;
         default -> null;
      };
   }

   // 昼夜颠倒：切换时间并暂停世界时钟 10 分钟
   private static void flipDayNight(ServerPlayer var0) {
      MinecraftServer var1 = var0.level().getServer();
      if (var1 != null) {
         ServerClockManager var2 = var1.clockManager();
         Registry<WorldClock> var3 = var1.registryAccess().lookupOrThrow(Registries.WORLD_CLOCK);
         Holder<WorldClock> var4 = var3.getOrThrow(WorldClocks.OVERWORLD);
         long var5 = var2.getTotalTicks(var4);
         long var6 = var5 - var5 % 24000L;
         long var7 = var5 % 24000L;
         long var8 = var7 < 13000L ? var6 + 18000L : var6 + 1000L;
         var2.setTotalTicks(var4, var8);
         var2.setPaused(var4, true);
         DAY_NIGHT_UNTIL = System.currentTimeMillis() + 600000L;
         var0.sendSystemMessage(Component.translatable("hexbingo.msg.day_night").withStyle(ChatFormatting.GOLD));
      }
   }

   // 混沌智：打乱敌方队伍背包/副手/盔甲槽
   private static int shuffleEnemyInventories(ServerPlayer var0) {
      MinecraftServer var1 = var0.level().getServer();
      if (var1 == null) {
         return 0;
      }

      Team var2 = team(var0);
      int var3 = 0;

      for (ServerPlayer var5 : var1.getPlayerList().getPlayers()) {
         if (var5 != var0 && !var5.isSpectator() && (var2 == null || !var2.hasPlayer(var5))) {
            shuffleInventory(var5);
            var5.sendSystemMessage(Component.translatable("hexbingo.msg.inventory_shuffled").withStyle(ChatFormatting.RED));
            var3++;
         }
      }

      return var3;
   }

   private static void giveChaosMindItem(ServerPlayer var0) {
      var0.addItem(questItem(Items.BARRIER, "混沌智", "hexbingo.item.chaos_mind.lore"));
   }

   public static boolean castChaosMind(ServerPlayer var0) {
      if (!hasHex(var0, "inventory_shuffle")) {
         return false;
      }

      long var1 = System.currentTimeMillis();
      Long var3 = CHAOS_MIND_COOLDOWN.get(var0.getUUID());
      if (var3 != null && var1 < var3) {
         long var4 = (var3 - var1 + 999L) / 1000L;
         var0.sendSystemMessage(
            Component.translatable("hexbingo.msg.chaos_mind_cooldown", new Object[]{Component.literal(Long.toString(var4))})
               .withStyle(ChatFormatting.GRAY)
         );
         return true;
      }

      int var5 = shuffleEnemyInventories(var0);
      if (var5 <= 0) {
         var0.sendSystemMessage(Component.translatable("hexbingo.msg.chaos_mind_none").withStyle(ChatFormatting.GRAY));
         return true;
      }

      CHAOS_MIND_COOLDOWN.put(var0.getUUID(), var1 + skillCooldown(var0, CHAOS_MIND_COOLDOWN_MS));
      var0.sendSystemMessage(
         Component.translatable("hexbingo.msg.inventory_shuffle", new Object[]{Component.literal(Integer.toString(var5))})
            .withStyle(ChatFormatting.GOLD)
      );
      return true;
   }

   private static void shuffleInventory(ServerPlayer var0) {
      NonNullList<ItemStack> var1 = var0.getInventory().getNonEquipmentItems();
      ArrayList<ItemStack> var2 = new ArrayList<>();

      for (int var3 = 0; var3 < var1.size(); var3++) {
         ItemStack var4 = var1.get(var3);
         if (!var4.isEmpty()) {
            var2.add(var4.copy());
            var1.set(var3, ItemStack.EMPTY);
         }
      }

      EquipmentSlot[] var5 = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET, EquipmentSlot.OFFHAND};

      for (EquipmentSlot var6 : var5) {
         ItemStack var7 = var0.getItemBySlot(var6);
         if (!var7.isEmpty()) {
            var2.add(var7.copy());
            var0.setItemSlot(var6, ItemStack.EMPTY);
         }
      }

      Collections.shuffle(var2, ThreadLocalRandom.current());
      int var8 = 0;

      for (int var9 = 0; var9 < var1.size(); var9++) {
         var1.set(var9, var8 < var2.size() ? var2.get(var8++) : ItemStack.EMPTY);
      }

      for (EquipmentSlot var11 : var5) {
         var0.setItemSlot(var11, var8 < var2.size() ? var2.get(var8++) : ItemStack.EMPTY);
      }

      var0.containerMenu.broadcastChanges();
   }

   private static void giveBanishItem(ServerPlayer var0) {
      var0.addItem(questItem(Items.END_CRYSTAL, "维度放逐", "hexbingo.item.banish.lore"));
      var0.sendSystemMessage(Component.translatable("hexbingo.msg.banish_item"));
   }

   public static boolean castBanish(ServerPlayer var0) {
      if (!hasHex(var0, "nether_banish")) {
         return false;
      }

      MinecraftServer var1 = var0.level().getServer();
      if (var1 == null) {
         return false;
      }

      long var2 = System.currentTimeMillis();
      Long var3 = BANISH_COOLDOWN.get(var0.getUUID());
      if (var3 != null && var2 < var3) {
         long var4 = (var3 - var2 + 999L) / 1000L;
         var0.sendSystemMessage(
            Component.translatable("hexbingo.msg.banish_cooldown", new Object[]{Component.literal(Long.toString(var4))}).withStyle(ChatFormatting.GRAY)
         );
         return true;
      }

      ServerLevel var5 = var1.getLevel(Level.NETHER);
      if (var5 == null) {
         return false;
      }

      Team var6 = team(var0);
      ArrayList<ServerPlayer> var7 = new ArrayList<>();

      for (Team var9 : Bingo.get(var1.overworld()).getTeams()) {
         if (var9 != var6 && !var9.getPlayers().isEmpty()) {
            ArrayList<ServerPlayer> var10 = new ArrayList<>();

            for (UUID var12 : var9.getPlayers()) {
               ServerPlayer var13 = var1.getPlayerList().getPlayer(var12);
               if (var13 != null && !var13.isSpectator()) {
                  var10.add(var13);
               }
            }

            if (!var10.isEmpty()) {
               var7.add(var10.get(ThreadLocalRandom.current().nextInt(var10.size())));
            }
         }
      }

      if (var7.isEmpty()) {
         var0.sendSystemMessage(Component.translatable("hexbingo.msg.banish_none").withStyle(ChatFormatting.GRAY));
         return true;
      }

      ThreadLocalRandom var16 = ThreadLocalRandom.current();

      for (ServerPlayer var17 : var7) {
         int var19 = var16.nextInt(-500, 501);
         int var20 = var16.nextInt(-500, 501);
         int var21 = safeGroundY(var5, var19, var20, 120);
         BlockPos var18;
         if (var21 < 0) {
            var18 = carveSafeSpot(var5, new BlockPos(var19, 100, var20));
         } else {
            var18 = new BlockPos(var19, var21, var20);
         }

         BANISHED.put(
            var17.getUUID(),
            new HexEffects.BanishState(
               var17.level().dimension(), var17.getX(), var17.getY(), var17.getZ(), var17.getYRot(), var17.getXRot(), var2 + 60000L
            )
         );
         var17.teleportTo(var5, var18.getX() + 0.5, var18.getY(), var18.getZ() + 0.5, Set.of(), var17.getYRot(), var17.getXRot(), false);
         var17.resetFallDistance();
         var17.setRemainingFireTicks(0);
         var17.sendSystemMessage(Component.translatable("hexbingo.msg.banished").withStyle(ChatFormatting.RED));
      }

      BANISH_COOLDOWN.put(var0.getUUID(), var2 + skillCooldown(var0, 480000L));
      var0.sendSystemMessage(
         Component.translatable("hexbingo.msg.banish_cast", new Object[]{Component.literal(Integer.toString(var7.size()))})
            .withStyle(ChatFormatting.DARK_PURPLE)
      );
      return true;
   }

   /** 被维度放逐且身处下界时，禁止自己发起传送命令；被别人传送仍然允许。 */
   public static void onCommand(CommandEvent var0) {
      ServerPlayer var1 = var0.getParseResults().getContext().getSource().getPlayer();
      if (var1 == null || !isBanishBlocked(var1)) {
         return;
      }

      String var2 = var0.getParseResults().getReader().getString();
      if (isTeleportCommand(var2)) {
         var0.setCanceled(true);
         var1.sendSystemMessage(Component.translatable("hexbingo.msg.banish_no_tp").withStyle(ChatFormatting.RED));
      }
   }

   public static boolean isBanishBlocked(ServerPlayer var0) {
      return BANISHED.containsKey(var0.getUUID()) && var0.level().dimension().equals(Level.NETHER);
   }

   private static boolean isTeleportCommand(String var0) {
      String var1 = var0.trim();
      if (var1.startsWith("/")) {
         var1 = var1.substring(1);
      }

      for (String var2 : var1.split("\\s+")) {
         if (var2.equalsIgnoreCase("tp") || var2.equalsIgnoreCase("teleport")
            || var2.equalsIgnoreCase("spreadplayers") || var2.equalsIgnoreCase("rtp")) {
            return true;
         }
      }

      return false;
   }

   // 万能牌：随机一格换成 UNO 图标并让所有队伍完成
   private static void wildcard(ServerPlayer var0) {
      MinecraftServer var1 = var0.level().getServer();
      if (var1 != null) {
         Bingo var2 = Bingo.get(var1.overworld());
         Team var3 = var2.getTeam(var0);
         int var4 = var2.tasks().size();
         if (var3 != null && var4 > 0) {
            ArrayList<Integer> var5 = new ArrayList<>();

            for (int var6 = 0; var6 < var4; var6++) {
               Task var7 = var2.task(var6);
               if (!var7.getType().id().equals("bingo.empty") && !var7.inverted() && var7.customTexture() == null) {
                  var5.add(var6);
               }
            }

            if (var5.isEmpty()) {
               for (int var8 = 0; var8 < var4; var8++) {
                  if (!var2.task(var8).getType().id().equals("bingo.empty")) {
                     var5.add(var8);
                  }
               }
            }

            if (var5.isEmpty()) {
               for (int var9 = 0; var9 < var4; var9++) {
                  var5.add(var9);
               }
            }

            int var10 = var5.get(ThreadLocalRandom.current().nextInt(var5.size()));
            Task var11 = var2.task(var10);
            TaskType<Object> var12 = (TaskType<Object>)var11.getType();
            Object var13 = var11.getElement(var12).orElse(null);
            if (var13 != null) {
               ArrayList<Task> var14 = new ArrayList<>(var2.tasks());
               var14.set(var10, new Task(var12, var13, var11.inverted(), WILDCARD_TEXTURE));
               var2.setTasks(var14);
            }

            for (Team var15 : var2.getTeams()) {
               if (!var15.getPlayers().isEmpty() && !var15.completed(var10)) {
                  var15.complete(var10);
               }
            }

            var2.checkWin();

            Component var16 = var2.task(var10).contentName(var1);
            Component var17 = Component.translatable("hexbingo.msg.wildcard_all", new Object[]{var16}).withStyle(ChatFormatting.GOLD);

            for (ServerPlayer var18 : var1.getPlayerList().getPlayers()) {
               var18.sendSystemMessage(var17);
            }
         }
      }
   }

   // 预支胜利：临时点亮一格，5 分钟内真正完成才保留
   private static void startAdvanceVictory(ServerPlayer var0, int var1) {
      MinecraftServer var2 = var0.level().getServer();
      if (var2 == null) {
         return;
      }

      Bingo var3 = Bingo.get(var2.overworld());
      Team var4 = var3.getTeam(var0);
      if (var4 == null) {
         return;
      }

      ArrayList<Integer> var5 = new ArrayList<>();

      for (int var6 = 0; var6 < var3.tasks().size(); var6++) {
         if (!var4.completed(var6) && !var4.locked(var6) && var3.task(var6) != Task.EMPTY) {
            var5.add(var6);
         }
      }

      if (var5.isEmpty()) {
         var0.sendSystemMessage(Component.translatable("hexbingo.msg.advance_victory_none").withStyle(ChatFormatting.GRAY));
         return;
      }

      int var9 = var5.get(ThreadLocalRandom.current().nextInt(var5.size()));
      var4.complete(var9);
      ADVANCES.add(new HexEffects.AdvanceState(var4, var9, System.currentTimeMillis() + (long)var1 * 1000L));
      Component var7 = var3.task(var9).contentName(var2);
      messageTeam(var2, var4, Component.translatable("hexbingo.msg.advance_victory", var7).withStyle(ChatFormatting.GOLD));
   }

   private static void markAdvanceGenuine(Team var0, int var1) {
      for (HexEffects.AdvanceState var3 : ADVANCES) {
         if (var3.team == var0 && var3.slot == var1) {
            var3.genuine = true;
         }
      }
   }

   public static void tickAdvances(MinecraftServer var0) {
      if (!ADVANCES.isEmpty()) {
         long var1 = System.currentTimeMillis();
         Bingo var3 = Bingo.get(var0.overworld());
         Iterator<HexEffects.AdvanceState> var4 = ADVANCES.iterator();

         while (var4.hasNext()) {
            HexEffects.AdvanceState var5 = var4.next();
            if (var1 >= var5.until) {
               var4.remove();
               if (!var5.genuine && var3.running()) {
                  uncompleteSlot(var0, var3, var5.team, var5.slot);
                  Component var6 = var3.task(var5.slot).contentName(var0);
                  messageTeam(var0, var5.team, Component.translatable("hexbingo.msg.advance_victory_expire", var6).withStyle(ChatFormatting.GRAY));
               }
            }
         }
      }
   }

   private static void cellSwap(ServerPlayer var0) {
      MinecraftServer var1 = var0.level().getServer();
      if (var1 == null) {
         return;
      }

      Bingo var2 = Bingo.get(var1.overworld());
      Team var3 = var2.getTeam(var0);
      if (var3 == null) {
         return;
      }

      ArrayList<Integer> var4 = new ArrayList<>();

      for (int var5 = 0; var5 < var2.tasks().size(); var5++) {
         if (var3.completed(var5)) {
            var4.add(var5);
         }
      }

      ArrayList<Team> var5 = new ArrayList<>();

      for (Team var7 : var2.getTeams()) {
         if (var7 != var3 && !var7.getPlayers().isEmpty() && randomCompleted(var7) >= 0) {
            var5.add(var7);
         }
      }

      if (var4.isEmpty() || var5.isEmpty()) {
         var0.sendSystemMessage(Component.translatable("hexbingo.msg.cell_swap_none").withStyle(ChatFormatting.GRAY));
         return;
      }

      ThreadLocalRandom var11 = ThreadLocalRandom.current();
      Team var7 = var5.get(var11.nextInt(var5.size()));
      int var8 = -1;
      int var9 = -1;

      for (int var13 = 0; var13 < 32; var13++) {
         int var14 = var4.get(var11.nextInt(var4.size()));
         int var15 = randomCompleted(var7);
         if (var15 >= 0 && var15 != var14) {
            var8 = var14;
            var9 = var15;
            break;
         }
      }

      if (var8 < 0 || var9 < 0) {
         var0.sendSystemMessage(Component.translatable("hexbingo.msg.cell_swap_none").withStyle(ChatFormatting.GRAY));
         return;
      }

      setTeamSlot(var3, var8, false);
      setTeamSlot(var3, var9, true);
      setTeamSlot(var7, var9, false);
      setTeamSlot(var7, var8, true);
      var2.markDirty(true);
      var2.checkWin();
      Component var10 = Component.translatable(
         "hexbingo.msg.cell_swap", var2.task(var8).contentName(var1), var2.task(var9).contentName(var1)
      ).withStyle(ChatFormatting.LIGHT_PURPLE);
      broadcast(var1, var10);
   }

   private static void setTeamSlot(Team var0, int var1, boolean var2) {
      try {
         Field var3 = Team.class.getDeclaredField("completed");
         var3.setAccessible(true);
         int var4 = var3.getInt(var0);
         int var5 = 1 << var1 % 25;
         var3.setInt(var0, var2 ? var4 | var5 : var4 & ~var5);
         Field var6 = Team.class.getDeclaredField("completion");
         var6.setAccessible(true);
         var6.set(var0, null);
         var0.setDirty();
      } catch (Exception var7) {
         HexBingo.LOGGER.warn("Failed to update team completion", var7);
      }
   }

   // 地心游记：传送到 Y=-50 附近的洞穴
   private static void journeyCore(ServerPlayer var0) {
      if (var0.level() instanceof ServerLevel var1) {
         int var2 = var0.getBlockX();
         int var3 = var0.getBlockZ();
         BlockPos var4 = findCaveNear(var1, var2, var3, 24);

         if (var4 == null) {
            var4 = carveSafeSpot(var1, new BlockPos(var2, Math.max(var1.getMinY() + 2, -50), var3));
         }

         var0.teleportTo(var1, var4.getX() + 0.5, var4.getY(), var4.getZ() + 0.5, Set.of(), var0.getYRot(), var0.getXRot(), false);
         var0.resetFallDistance();
         var0.setRemainingFireTicks(0);
         var0.sendSystemMessage(Component.translatable("hexbingo.msg.journey_core").withStyle(ChatFormatting.GOLD));
      }
   }

   private static BlockPos findCaveNear(ServerLevel var0, int var1, int var2, int var3) {
      for (int var4 = -var3; var4 <= var3; var4 += 4) {
         for (int var5 = -var3; var5 <= var3; var5 += 4) {
            int var6 = var1 + var4;
            int var7 = var2 + var5;
            var0.getChunk(var6 >> 4, var7 >> 4);
            int var8 = safeGroundY(var0, var6, var7, -40);
            if (var8 > var0.getMinY() + 1
               && var8 <= -40
               && var0.getBlockState(new BlockPos(var6, var8, var7)).getFluidState().isEmpty()) {
               return new BlockPos(var6, var8, var7);
            }
         }
      }

      return null;
   }

   // 苦行者：清空本队已完成格，换取随机彩色海克斯
   private static void ascetic(ServerPlayer var0, int var1) {
      MinecraftServer var2 = var0.level().getServer();
      if (var2 == null) {
         return;
      }

      Bingo var3 = Bingo.get(var2.overworld());
      Team var4 = var3.getTeam(var0);
      if (var4 != null) {
         var4.resetCompleted();
         var3.markDirty(true);

         for (int var5 = 0; var5 < var1; var5++) {
            grantRandomHex(var0, "prismatic", "ascetic");
         }

         var3.checkWin();
         var0.sendSystemMessage(Component.translatable("hexbingo.msg.ascetic").withStyle(ChatFormatting.LIGHT_PURPLE));
      }
   }

   // 命运骰子
   private static void destinyDice(ServerPlayer var0) {
      MinecraftServer var1 = var0.level().getServer();
      if (var1 == null) {
         return;
      }

      Bingo var2 = Bingo.get(var1.overworld());
      Team var3 = var2.getTeam(var0);
      if (var3 == null) {
         return;
      }

      int var4 = ThreadLocalRandom.current().nextInt(4) + 1;
      Team var5 = randomEnemyTeam(var2, var3);
      switch (var4) {
         case 1: {
            int var6 = randomIncomplete(var2, var3);
            if (var6 < 0) {
               broadcast(var1, Component.translatable("hexbingo.msg.destiny_dice_none").withStyle(ChatFormatting.GRAY));
               return;
            }

            completeTask(var2, var3, var6);
            broadcast(var1, Component.translatable("hexbingo.msg.destiny_dice_1").withStyle(ChatFormatting.LIGHT_PURPLE));
            break;
         }
         case 2: {
            int var7 = var5 == null ? -1 : randomCompleted(var5);
            if (var7 < 0) {
               broadcast(var1, Component.translatable("hexbingo.msg.destiny_dice_none").withStyle(ChatFormatting.GRAY));
               return;
            }

            uncompleteSlot(var1, var2, var5, var7);
            broadcast(var1, Component.translatable("hexbingo.msg.destiny_dice_2").withStyle(ChatFormatting.LIGHT_PURPLE));
            break;
         }
         case 3: {
            int var8 = randomIncomplete(var2, var3);
            int var9 = var5 == null ? -1 : randomCompleted(var5);
            if (var8 < 0 || var9 < 0) {
               broadcast(var1, Component.translatable("hexbingo.msg.destiny_dice_none").withStyle(ChatFormatting.GRAY));
               return;
            }

            completeTask(var2, var3, var8);
            uncompleteSlot(var1, var2, var5, var9);
            broadcast(var1, Component.translatable("hexbingo.msg.destiny_dice_3").withStyle(ChatFormatting.LIGHT_PURPLE));
            break;
         }
         default:
            cardShuffle(var0);
            broadcast(var1, Component.translatable("hexbingo.msg.destiny_dice_4").withStyle(ChatFormatting.LIGHT_PURPLE));
      }
   }

   private static Team randomEnemyTeam(Bingo var0, Team var1) {
      ArrayList<Team> var2 = new ArrayList<>();

      for (Team var4 : var0.getTeams()) {
         if (var4 != var1 && !var4.getPlayers().isEmpty()) {
            var2.add(var4);
         }
      }

      return var2.isEmpty() ? null : var2.get(ThreadLocalRandom.current().nextInt(var2.size()));
   }

   // 随机大迁徙：所有玩家打散到主世界同一片区域
   private static void greatMigration(ServerPlayer var0, double var1) {
      MinecraftServer var2 = var0.level().getServer();
      if (var2 == null) {
         return;
      }

      ServerLevel var3 = var2.overworld();
      ThreadLocalRandom var4 = ThreadLocalRandom.current();
      double var5 = var0.getX();
      double var6 = var0.getZ();

      for (ServerPlayer var8 : var2.getPlayerList().getPlayers()) {
         if (!var8.isSpectator()) {
            double var9 = var4.nextDouble() * Math.PI * 2.0;
            double var11 = 32.0 + var4.nextDouble() * Math.max(0.0, var1 - 32.0);
            int var13 = Mth.floor(var5 + Math.cos(var9) * var11);
            int var14 = Mth.floor(var6 + Math.sin(var9) * var11);
            BlockPos var15 = safeSurfaceSpot(var3, new BlockPos(var13, 0, var14));
            if (var15 == null) {
               int var16 = var3.getHeight(Types.MOTION_BLOCKING_NO_LEAVES, var13, var14);
               var15 = carveSafeSpot(var3, new BlockPos(var13, Math.max(var3.getMinY() + 2, var16), var14));
            }

            var8.teleportTo(var3, var15.getX() + 0.5, var15.getY(), var15.getZ() + 0.5, Set.of(), var8.getYRot(), var8.getXRot(), false);
            var8.resetFallDistance();
         }
      }

      broadcast(var2, Component.translatable("hexbingo.msg.great_migration").withStyle(ChatFormatting.LIGHT_PURPLE));
   }

   public static void startDragonIncubation(ServerPlayer var0, BlockPos var1) {
      if (!hasHex(var0, "dragon_rider")) {
         return;
      }

      if (DRAGON_PETS.containsKey(var0.getUUID())) {
         var0.sendSystemMessage(Component.translatable("hexbingo.msg.dragon_exists").withStyle(ChatFormatting.GRAY));
         return;
      }

      if (!(var0.level() instanceof ServerLevel var2)) {
         return;
      }

      // 同一玩家重新放蛋时，取消旧孵化（同坐标时不要删掉刚放下的新蛋）
      for (Iterator<HexEffects.EggIncubation> var3 = INCUBATIONS.iterator(); var3.hasNext();) {
         HexEffects.EggIncubation var4 = var3.next();
         if (var4.owner.equals(var0.getUUID())) {
            discardIncubationDisplay(var4);
            if (!var4.pos.equals(var1) && var4.level.getBlockState(var4.pos).is(Blocks.DRAGON_EGG)) {
               var4.level.removeBlock(var4.pos, false);
            }
            var3.remove();
         }
      }

      // 用文字展示实体显示倒计时，隐形盔甲架的名字标签在隐形时不会渲染
      Display.TextDisplay var8 = createIncubationDisplay(var2, var1, DRAGON_INCUBATION_MS);
      INCUBATIONS.add(new HexEffects.EggIncubation(var0.getUUID(), var2, var1.immutable(), System.currentTimeMillis() + DRAGON_INCUBATION_MS, var8));
      var0.sendSystemMessage(Component.translatable("hexbingo.msg.dragon_incubation_start").withStyle(ChatFormatting.GOLD));
   }

   private static Display.TextDisplay createIncubationDisplay(ServerLevel var0, BlockPos var1, long var2) {
      Display.TextDisplay var4 = (Display.TextDisplay)EntityTypes.TEXT_DISPLAY.create(var0, EntitySpawnReason.MOB_SUMMONED);
      if (var4 == null) {
         return null;
      }

      var4.setPos(var1.getX() + 0.5, var1.getY() + 1.15, var1.getZ() + 0.5);
      var4.setNoGravity(true);
      var4.setInvulnerable(true);
      var4.setSilent(true);
      // 用 NBT 初始化朝向/穿透/背景，避免反射设置 billboard 不生效
      CompoundTag var5 = new CompoundTag();
      var5.put("text", (Tag)ComponentSerialization.CODEC.encodeStart(NbtOps.INSTANCE, incubationName(var2)).getOrThrow());
      var5.put("Pos", (Tag)Vec3.CODEC.encodeStart(NbtOps.INSTANCE, var4.position()).getOrThrow());
      var5.putString("billboard", "center");
      var5.putString("alignment", "center");
      var5.putInt("background", 0x66000000);
      var5.putByte("shadow", (byte)1);
      var5.putByte("see_through", (byte)1);
      var5.putFloat("view_range", 2.0F);
      var4.load(TagValueInput.create(ProblemReporter.DISCARDING, var0.registryAccess(), var5));
      var0.addFreshEntity(var4);
      return var4;
   }

   private static void applyIncubationText(Display.TextDisplay var0, long var1) {
      try {
         if (TEXT_DISPLAY_SET_TEXT == null) {
            TEXT_DISPLAY_SET_TEXT = Display.TextDisplay.class.getDeclaredMethod("setText", Component.class);
            TEXT_DISPLAY_SET_TEXT.setAccessible(true);
         }

         TEXT_DISPLAY_SET_TEXT.invoke(var0, incubationName(var1));
      } catch (ReflectiveOperationException var3) {
         HexBingo.LOGGER.warn("Failed to update dragon incubation text display", var3);
      }
   }

   private static Component incubationName(long var0) {
      long var2 = Math.max(0L, (var0 + 999L) / 1000L);
      long var4 = var2 / 60L;
      long var6 = var2 % 60L;
      String var8 = (var4 < 10L ? "0" : "") + var4 + ":" + (var6 < 10L ? "0" : "") + var6;
      return Component.translatable("hexbingo.msg.dragon_incubating", Component.literal(var8));
   }

   private static void discardIncubationDisplay(HexEffects.EggIncubation var0) {
      if (var0.display != null && !var0.display.isRemoved()) {
         var0.display.discard();
      }
   }

   public static void tickIncubations(MinecraftServer var0) {
      if (!INCUBATIONS.isEmpty()) {
         long var1 = System.currentTimeMillis();
         Iterator<HexEffects.EggIncubation> var3 = INCUBATIONS.iterator();

         while (var3.hasNext()) {
            HexEffects.EggIncubation var4 = var3.next();
            ServerPlayer var5 = var0.getPlayerList().getPlayer(var4.owner);
            if (!var4.level.isLoaded(var4.pos)) {
               continue;
            }

            if (!var4.level.getBlockState(var4.pos).is(Blocks.DRAGON_EGG)) {
               BlockPos var9 = findMovedEgg(var4);
               if (var9 != null) {
                  // 龙蛋被活塞推动或原版瞬移：在新位置重新计时并显示倒计时
                  discardIncubationDisplay(var4);
                  var4.pos = var9;
                  var4.readyAt = System.currentTimeMillis() + DRAGON_INCUBATION_MS;
                  var4.lastSecond = -1;
                  var4.display = createIncubationDisplay(var4.level, var4.pos, DRAGON_INCUBATION_MS);
               } else {
                  discardIncubationDisplay(var4);
                  var3.remove();
               }

               if (var5 != null) {
                  var5.sendSystemMessage(Component.translatable("hexbingo.msg.dragon_incubation_broken").withStyle(ChatFormatting.GRAY));
               }
               continue;
            }

            if (var5 == null) {
               continue;
            }

            long var6 = var4.readyAt - var1;
            if (var6 <= 0L) {
               discardIncubationDisplay(var4);
               hatchDragon(var5, var4.level, var4.pos);
               var3.remove();
            } else {
               // 区块重载后实体引用会失效，重新生成倒计时文字
               if (var4.display == null || var4.display.isRemoved()) {
                  var4.display = createIncubationDisplay(var4.level, var4.pos, var6);
               }

               int var8 = (int)((var6 + 999L) / 1000L);
               if (var8 != var4.lastSecond) {
                  var4.lastSecond = var8;
                  if (var4.display != null && !var4.display.isRemoved()) {
                     applyIncubationText(var4.display, var6);
                  }
               }
            }
         }
      }
   }

   /** 在旧位置附近寻找被活塞或原版随机瞬移带走的龙蛋。 */
   private static BlockPos findMovedEgg(HexEffects.EggIncubation var0) {
      BlockPos var1 = null;
      double var2 = Double.MAX_VALUE;

      for (BlockPos var5 : BlockPos.betweenClosed(var0.pos.offset(-16, -8, -16), var0.pos.offset(16, 8, 16))) {
         if (var0.level.getBlockState(var5).is(Blocks.DRAGON_EGG) && !var5.equals(var0.pos) && !isIncubatingAt(var0.level, var5)) {
            double var6 = var5.distSqr(var0.pos);
            if (var6 < var2) {
               var2 = var6;
               var1 = var5.immutable();
            }
         }
      }

      return var1;
   }

   private static boolean isIncubatingAt(ServerLevel var0, BlockPos var1) {
      for (HexEffects.EggIncubation var3 : INCUBATIONS) {
         if (var3.level == var0 && var3.pos.equals(var1)) {
            return true;
         }
      }

      return false;
   }

   private static void cancelIncubation(UUID var0, boolean var1) {
      Iterator<HexEffects.EggIncubation> var2 = INCUBATIONS.iterator();

      while (var2.hasNext()) {
         HexEffects.EggIncubation var3 = var2.next();
         if (var3.owner.equals(var0)) {
            discardIncubationDisplay(var3);
            if (var1 && var3.level.getBlockState(var3.pos).is(Blocks.DRAGON_EGG)) {
               var3.level.removeBlock(var3.pos, false);
            }

            var2.remove();
         }
      }
   }

   private static void giveDragonEgg(ServerPlayer var0) {
      if (DRAGON_PETS.containsKey(var0.getUUID())) {
         var0.sendSystemMessage(Component.translatable("hexbingo.msg.dragon_exists").withStyle(ChatFormatting.GRAY));
         return;
      }

      if (!hasNamedItem(var0, "龙蛋")) {
         var0.addItem(questItem(Items.DRAGON_EGG, "龙蛋", "hexbingo.item.dragon_egg.lore"));
      }

      var0.sendSystemMessage(Component.translatable("hexbingo.msg.dragon_egg").withStyle(ChatFormatting.GOLD));
   }

   public static boolean hatchDragon(ServerPlayer var0, ServerLevel var2, BlockPos var1) {
      if (!hasHex(var0, "dragon_rider")) {
         return false;
      }

      if (DRAGON_PETS.containsKey(var0.getUUID())) {
         var0.sendSystemMessage(Component.translatable("hexbingo.msg.dragon_exists").withStyle(ChatFormatting.GRAY));
         return false;
      }

      EnderDragon var3 = (EnderDragon)EntityTypes.ENDER_DRAGON.create(var2, EntitySpawnReason.MOB_SUMMONED);
      if (var3 == null) {
         return false;
      }

      var3.setPos(var1.getX() + 0.5, var1.getY() + 1.0, var1.getZ() + 0.5);
      var3.setNoAi(true);
      var3.setSilent(true);
      var3.setInvulnerable(true);
      var3.setPersistenceRequired();
      var3.setCustomName(Component.literal("幼龙"));
      var3.setCustomNameVisible(true);
      AttributeInstance var4 = var3.getAttribute(Attributes.SCALE);
      if (var4 != null) {
         var4.setBaseValue(0.35);
      }

      var2.addFreshEntity(var3);
      var2.removeBlock(var1, false);
      DRAGON_PETS.put(var0.getUUID(), var3);
      DRAGON_ANGLES.put(var0.getUUID(), 0.0);
      DRAGON_SOUND_TIMER.put(var0.getUUID(), 40);
      var2.playSound(null, var1, SoundEvents.ENDER_DRAGON_GROWL, SoundSource.NEUTRAL, 1.0F, 1.4F);
      var0.sendSystemMessage(Component.translatable("hexbingo.msg.dragon_hatched").withStyle(ChatFormatting.GOLD));
      MinecraftServer var5 = var2.getServer();
      Team var6 = team(var0);

      if (var5 != null && var6 != null) {
         Component var7 = Component.translatable("hexbingo.msg.dragon_hatched_team", var0.getDisplayName()).withStyle(ChatFormatting.GOLD);

         for (ServerPlayer var9 : onlineTeamPlayers(var5, var6)) {
            if (var9 != var0) {
               var9.sendSystemMessage(var7);
            }
         }
      }

      return true;
   }

   public static void tickDragons(MinecraftServer var0) {
      if (!DRAGON_PETS.isEmpty()) {
         Iterator<Map.Entry<UUID, EnderDragon>> var1 = DRAGON_PETS.entrySet().iterator();

         while (var1.hasNext()) {
            Map.Entry<UUID, EnderDragon> var2 = var1.next();
            ServerPlayer var3 = var0.getPlayerList().getPlayer(var2.getKey());
            EnderDragon var4 = var2.getValue();
            if (var4 == null || !var4.isAlive() || var4.isRemoved()) {
               var1.remove();
               DRAGON_ANGLES.remove(var2.getKey());
            } else if (var3 != null) {
               ServerLevel var5 = (ServerLevel)var3.level();
               if (var4.level() != var5) {
                  var4.teleportTo(var5, var3.getX(), var3.getY() + 2.0, var3.getZ(), Set.of(), var4.getYRot(), var4.getXRot(), false);
               }

               double var6 = DRAGON_ANGLES.getOrDefault(var3.getUUID(), 0.0) + 0.07;
               DRAGON_ANGLES.put(var3.getUUID(), var6);
               double var8 = var3.getX() + Math.cos(var6) * 4.0;
               double var10 = var3.getZ() + Math.sin(var6) * 4.0;
               double var12 = var3.getY() + 2.2 + Math.sin(var6 * 2.0) * 0.4;
               float var14 = (float)(Math.toDegrees(Math.atan2(var3.getX() - var8, var3.getZ() - var10)));
               var4.setPos(var8, var12, var10);
               var4.setDeltaMovement(Vec3.ZERO);
               var4.setYRot(var14);
               var4.setYHeadRot(var14);

               int soundWait = DRAGON_SOUND_TIMER.getOrDefault(var3.getUUID(), 40);
               if (--soundWait <= 0) {
                  var5.playSound(
                     null,
                     var8,
                     var12,
                     var10,
                     BABY_DRAGON_SOUND,
                     SoundSource.NEUTRAL,
                     0.7F,
                     0.9F + ThreadLocalRandom.current().nextFloat() * 0.2F
                  );
                  soundWait = 80 + ThreadLocalRandom.current().nextInt(80);
               }

               DRAGON_SOUND_TIMER.put(var3.getUUID(), soundWait);

               if ((var3.tickCount & 1) == 0) {
                  int var15 = 0;

                  for (BlockPos var17 : BlockPos.betweenClosed(
                     Mth.floor(var8) - 1, Mth.floor(var12) - 1, Mth.floor(var10) - 1, Mth.floor(var8) + 1, Mth.floor(var12) + 1, Mth.floor(var10) + 1
                  )) {
                     BlockState var18 = var5.getBlockState(var17);
                     if (!var18.isAir() && var18.getDestroySpeed(var5, var17) >= 0.0F) {
                        var5.destroyBlock(var17.immutable(), true, var3);
                        if (++var15 >= 6) {
                           break;
                        }
                     }
                  }
               }

               AABB var19 = new AABB(var8 - 2.0, var12 - 1.5, var10 - 2.0, var8 + 2.0, var12 + 1.5, var10 + 2.0);

               for (LivingEntity var22 : var5.getEntitiesOfClass(
                  LivingEntity.class, var19, var21 -> var21.isAlive() && var21 != var3 && !(var21 instanceof EnderDragon) && !isDragonProtected(var3, var21)
               )) {
                  DamageSource var20 = var5.damageSources().mobAttack(var4);
                  var22.hurtServer(var5, var20, 4.0F);
                  var22.knockback(0.8, var8 - var22.getX(), var10 - var22.getZ(), var20, 4.0F);
               }
            }
         }
      }
   }

   private static boolean isDragonProtected(ServerPlayer var0, LivingEntity var1) {
      if (var1 == var0) {
         return true;
      } else if (var1 instanceof ServerPlayer var2) {
         Team var3 = team(var0);
         return var3 != null && var3.hasPlayer(var2) || hasHex(var2, "dragon_rider");
      } else {
         return false;
      }
   }

   private static void giveBlankCellItem(ServerPlayer var0) {
      if (!hasNamedItem(var0, "空白格")) {
         var0.addItem(questItem(Items.PAPER, "空白格", "hexbingo.item.blank_cell.lore"));
      }

      var0.sendSystemMessage(Component.translatable("hexbingo.msg.blank_item").withStyle(ChatFormatting.LIGHT_PURPLE));
   }

   public static boolean canUseBlankCell(ServerPlayer var0) {
      return hasHex(var0, "blank_cell") && hasNamedItem(var0, "空白格");
   }

   public static void applyBlankCell(ServerPlayer var0, int var1) {
      MinecraftServer var2 = var0.level().getServer();
      if (var2 == null) {
         return;
      }

      Bingo var3 = Bingo.get(var2.overworld());
      Team var4 = var3.getTeam(var0);
      if (!hasHex(var0, "blank_cell") || var4 == null || var1 < 0 || var1 >= var3.tasks().size()) {
         return;
      }

      Task var5 = var3.task(var1);
      if (var5 == Task.EMPTY || var4.completed(var1) || var4.locked(var1)) {
         var0.sendSystemMessage(Component.translatable("hexbingo.msg.blank_invalid").withStyle(ChatFormatting.GRAY));
         return;
      }

      // 只为本队点亮该格，保留原任务图标，不替换成万能牌
      var4.complete(var1);
      var3.markDirty(true);
      var3.checkWin();
      BLANK_USED.add(var0.getUUID());
      consumeNamedItem(var0, "空白格");
      Component var6 = var5.contentName(var2);
      messageTeam(var2, var4, Component.translatable("hexbingo.msg.blank_done", var6).withStyle(ChatFormatting.LIGHT_PURPLE));
   }

   private static void consumeNamedItem(ServerPlayer var0, String var1) {
      for (int var2 = 0; var2 < var0.getInventory().getContainerSize(); var2++) {
         ItemStack var3 = var0.getInventory().getItem(var2);
         if (!var3.isEmpty() && var3.getHoverName().getString().equals(var1)) {
            var3.shrink(1);
            if (var3.isEmpty()) {
               var0.getInventory().setItem(var2, ItemStack.EMPTY);
            }
            return;
         }
      }
   }

   private static void messageTeam(MinecraftServer var0, Team var1, Component var2) {
      for (ServerPlayer var3 : onlineTeamPlayers(var0, var1)) {
         var3.sendSystemMessage(var2);
      }
   }

   private static void broadcast(MinecraftServer var0, Component var1) {
      for (ServerPlayer var2 : var0.getPlayerList().getPlayers()) {
         var2.sendSystemMessage(var1);
      }
   }

   private static void giveWorldItem(ServerPlayer var0) {
      var0.addItem(questItem(Items.CLOCK, "The World", "hexbingo.item.world.lore"));
      var0.sendSystemMessage(Component.translatable("hexbingo.msg.world_item"));
   }

   public static boolean castTheWorld(ServerPlayer var0) {
      if (!hasHex(var0, "the_world")) {
         return false;
      }

      long var1 = System.currentTimeMillis();
      Long var3 = THE_WORLD_COOLDOWN.get(var0.getUUID());
      if (var3 != null && var1 < var3) {
         long var21 = (var3 - var1 + 999L) / 1000L;
         var0.sendSystemMessage(
            Component.translatable("hexbingo.msg.world_cooldown", new Object[]{Component.literal(Long.toString(var21))}).withStyle(ChatFormatting.GRAY)
         );
         return true;
      }

      theWorld(var0);
      THE_WORLD_COOLDOWN.put(var0.getUUID(), var1 + skillCooldown(var0, THE_WORLD_COOLDOWN_MS));
      return true;
   }

   // The World！：冻结其他玩家与生物 8 秒
   private static void theWorld(ServerPlayer var0) {
      MinecraftServer var1 = var0.level().getServer();
      if (var1 != null) {
         THE_WORLD_MOBS.clear();

         for (ServerLevel var3 : var1.getAllLevels()) {
            for (Entity var4 : var3.getAllEntities()) {
               if (var4 instanceof Mob var5 && !var4.isRemoved()) {
                  THE_WORLD_MOBS.put(var5, var5.position());
                  var5.setDeltaMovement(Vec3.ZERO);
                  var5.getNavigation().stop();
               }
            }
         }

         Holder<Attribute>[] var6 = new Holder[]{Attributes.MOVEMENT_SPEED, Attributes.FLYING_SPEED, Attributes.GRAVITY};
         double[] var7 = new double[]{-1.0, -1.0, 200.0};

         for (ServerPlayer var8 : var1.getPlayerList().getPlayers()) {
            if (var8 != var0 && !var8.isSpectator()) {
               if (shieldBlocks(var8, "the_world")) {
                  continue;
               }

               for (int var9 = 0; var9 < var6.length; var9++) {
                  AttributeInstance var10 = var8.getAttribute(var6[var9]);
                  if (var10 != null) {
                     var10.addTransientModifier(new AttributeModifier(modifierId("the_world", var9), var7[var9], Operation.ADD_MULTIPLIED_BASE));
                  }
               }

               var8.sendSystemMessage(Component.translatable("hexbingo.msg.the_world_target").withStyle(ChatFormatting.AQUA));
            }
         }

         THE_WORLD_UNTIL = System.currentTimeMillis() + 8000L;
         var0.sendSystemMessage(Component.translatable("hexbingo.msg.the_world").withStyle(ChatFormatting.LIGHT_PURPLE));
      }
   }

   // The World！的每 tick 维护与恢复
   private static void tickTheWorld(MinecraftServer var0) {
      if (THE_WORLD_UNTIL > 0L) {
         if (System.currentTimeMillis() >= THE_WORLD_UNTIL) {
            THE_WORLD_UNTIL = 0L;
            THE_WORLD_MOBS.clear();
            Holder<Attribute>[] var1 = new Holder[]{Attributes.MOVEMENT_SPEED, Attributes.FLYING_SPEED, Attributes.GRAVITY};

            for (ServerPlayer var3 : var0.getPlayerList().getPlayers()) {
               for (int var4 = 0; var4 < var1.length; var4++) {
                  AttributeInstance var5 = var3.getAttribute(var1[var4]);
                  if (var5 != null) {
                     var5.removeModifier(modifierId("the_world", var4));
                  }
               }
            }
         } else {
            for (Map.Entry<Mob, Vec3> var7 : THE_WORLD_MOBS.entrySet()) {
               Mob var8 = var7.getKey();
               if (!var8.isRemoved()) {
                  Vec3 var9 = var7.getValue();
                  var8.setPos(var9.x, var9.y, var9.z);
                  var8.setDeltaMovement(Vec3.ZERO);
                  var8.getNavigation().stop();
               }
            }
         }
      }
   }

   // 腐蚀背包：每队随机一人 5 件物品变腐肉
   private static void corruptInventories(ServerPlayer var0) {
      MinecraftServer var1 = var0.level().getServer();
      if (var1 != null) {
         Team var2 = team(var0);
         ThreadLocalRandom var3 = ThreadLocalRandom.current();
         int var4 = 0;

         for (Team var6 : Bingo.get(var1.overworld()).getTeams()) {
            if (var6 != var2 && !var6.getPlayers().isEmpty()) {
               ArrayList<ServerPlayer> var7 = new ArrayList<>();

               for (UUID var8 : var6.getPlayers()) {
                  ServerPlayer var9 = var1.getPlayerList().getPlayer(var8);
                  if (var9 != null && !var9.isSpectator()) {
                     var7.add(var9);
                  }
               }

               if (var7.isEmpty()) {
                  continue;
               }

               ServerPlayer var10 = var7.get(var3.nextInt(var7.size()));
               NonNullList<ItemStack> var11 = var10.getInventory().getNonEquipmentItems();
               ArrayList<Integer> var12 = new ArrayList<>();

               for (int var13 = 0; var13 < var11.size(); var13++) {
                  ItemStack var14 = var11.get(var13);
                  if (!var14.isEmpty() && !isBoundItem(var14)) {
                     var12.add(var13);
                  }
               }

               Collections.shuffle(var12, var3);

               for (int var15 = 0; var15 < 5 && var15 < var12.size(); var15++) {
                  int var16 = var12.get(var15);
                  ItemStack var17 = var11.get(var16);
                  var11.set(var16, new ItemStack(Items.ROTTEN_FLESH, var17.getCount()));
               }

               var10.containerMenu.broadcastChanges();
               var10.sendSystemMessage(Component.translatable("hexbingo.msg.corrupted").withStyle(ChatFormatting.RED));
               var4++;
            }
         }

         var0.sendSystemMessage(
            Component.translatable("hexbingo.msg.corrupt_inventory", new Object[]{Component.literal(Integer.toString(var4))})
               .withStyle(ChatFormatting.DARK_PURPLE)
         );
      }
   }

   private static void grantBlackMarket(ServerPlayer var0) {
      var0.removeTag(BLACK_MARKET_USED_TAG);
      var0.sendSystemMessage(Component.translatable("hexbingo.msg.black_market").withStyle(ChatFormatting.GOLD));
   }

   // 黑市开张：村民升到最高级并给永久村庄英雄 II
   public static boolean useBlackMarket(ServerPlayer var0, Villager var1) {
      if (!hasHex(var0, "black_market")) {
         return false;
      }

      if (var0.entityTags().contains(BLACK_MARKET_USED_TAG)) {
         return false;
      }

      var0.addTag(BLACK_MARKET_USED_TAG);

      try {
         Method var2 = Villager.class.getDeclaredMethod("updateTrades", ServerLevel.class);
         var2.setAccessible(true);
         Method var3 = Villager.class.getDeclaredMethod("increaseMerchantCareer", ServerLevel.class);
         var3.setAccessible(true);
         ServerLevel var4 = (ServerLevel)var0.level();
         var1.getOffers().clear();
         var1.setVillagerData(var1.getVillagerData().withLevel(1));
         var2.invoke(var1, var4);

         while (var1.getVillagerData().level() < VillagerData.MAX_VILLAGER_LEVEL) {
            var3.invoke(var1, var4);
         }
      } catch (Exception var5) {
      }

      var0.addEffect(new MobEffectInstance(MobEffects.HERO_OF_THE_VILLAGE, -1, 1, true, false, false));
      var0.sendSystemMessage(Component.translatable("hexbingo.msg.black_market_used").withStyle(ChatFormatting.GOLD));
      return true;
   }

   // 恶魔契约：扣生命上限、允许受伤、完成 1 个 Bingo 任务
   private static void applyDemonPact(ServerPlayer var0, String var1, int var2) {
      applyDemonPactAttribute(var0, var1, var2);
      if (var0.getHealth() > var0.getMaxHealth()) {
         var0.setHealth(var0.getMaxHealth());
      }

      giveDemonPactArmor(var0);
      MinecraftServer var3 = var0.level().getServer();
      if (var3 != null) {
         Bingo var4 = Bingo.get(var3.overworld());
         Team var5 = var4.getTeam(var0);
         int var6 = var4.tasks().size();
         if (var5 != null && var6 > 0) {
            int var7 = -1;
            int var8 = -1;

            for (int var9 = 0; var9 < var6; var9++) {
               if (!var5.completed(var9)
                  && !var5.locked(var9)
                  && !var4.task(var9).getType().id().equals("bingo.empty")
                  && var4.task(var9).weight() > var8) {
                  var8 = var4.task(var9).weight();
                  var7 = var9;
               }
            }

            if (var7 >= 0) {
               var5.complete(var7);
               var4.checkWin();
               var0.sendSystemMessage(
                  Component.translatable("hexbingo.msg.demon_pact_task", new Object[]{var4.task(var7).contentName(var3)})
                     .withStyle(ChatFormatting.GOLD)
               );
            } else {
               var0.sendSystemMessage(Component.translatable("hexbingo.msg.demon_pact_none").withStyle(ChatFormatting.GRAY));
            }
         }
      }

      var0.sendSystemMessage(Component.translatable("hexbingo.msg.demon_pact").withStyle(ChatFormatting.RED));
   }

   private static void applyDemonPactAttribute(ServerPlayer var0, String var1, int var2) {
      applyNegativeAttribute(var0, var1, var2, Attributes.MAX_HEALTH, -10.0, Operation.ADD_VALUE);
   }

   private static void giveDemonPactArmor(ServerPlayer var0) {
      Item[] var1 = new Item[]{Items.IRON_HELMET, Items.IRON_CHESTPLATE, Items.IRON_LEGGINGS, Items.IRON_BOOTS};

      for (Item var5 : var1) {
         var0.addItem(randomlyEnchantedArmor(var0, var5));
      }

      var0.sendSystemMessage(Component.translatable("hexbingo.msg.demon_pact_armor").withStyle(ChatFormatting.GOLD));
   }

   private static ItemStack randomlyEnchantedArmor(ServerPlayer var0, Item var1) {
      ItemStack var2 = new ItemStack(var1);
      Registry<Enchantment> var3 = var0.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
      ArrayList<Holder<Enchantment>> var4 = new ArrayList<>();

      for (Holder<Enchantment> var6 : var3.listElements().toList()) {
         if (var6.value().canEnchant(var2)) {
            var4.add(var6);
         }
      }

      Collections.shuffle(var4, ThreadLocalRandom.current());
      ArrayList<Holder<Enchantment>> var15 = new ArrayList<>();

      for (Holder<Enchantment> var7 : var4) {
         boolean var8 = true;

         for (Holder<Enchantment> var10 : var15) {
            if (!Enchantment.areCompatible(var10, var7)) {
               var8 = false;
               break;
            }
         }

         if (var8) {
            var15.add(var7);
         }

         if (var15.size() >= 2) {
            break;
         }
      }

      ItemEnchantments.Mutable var16 = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);

      for (Holder<Enchantment> var18 : var15) {
         var16.set(var18, 1 + ThreadLocalRandom.current().nextInt(Math.max(1, Math.min(3, var18.value().getMaxLevel()))));
      }

      EnchantmentHelper.setEnchantments(var2, var16.toImmutable());
      return var2;
   }

   public static boolean isDemonPact(ServerPlayer var0) {
      return hasHex(var0, "demon_pact");
   }

   // 终极刷新：记录一次可用的刷新次数，并给 2 个原版战利品箱
   private static void grantUltimateRefresh(ServerPlayer var0) {
      MinecraftServer var1 = var0.level().getServer();
      if (var1 != null) {
         HexGameData.get(var1).addRefreshCharge(var0.getUUID());
      }

      for (int var2 = 0; var2 < 2; var2++) {
         spawnVanillaLootChest(var0);
      }

      var0.sendSystemMessage(Component.translatable("hexbingo.msg.ultimate_refresh").withStyle(ChatFormatting.GOLD));
   }

   private static void spawnVanillaLootChest(ServerPlayer var0) {
      if (var0.level() instanceof ServerLevel var1) {
         ThreadLocalRandom var2 = ThreadLocalRandom.current();
         BlockPos var3 = var0.blockPosition();

         for (int var4 = 0; var4 < 16; var4++) {
            BlockPos var5 = var3.offset(var2.nextInt(5) - 2, var2.nextInt(3), var2.nextInt(5) - 2);
            if (var1.getBlockState(var5).isAir()) {
               var1.setBlockAndUpdate(var5, Blocks.CHEST.defaultBlockState());
               if (var1.getBlockEntity(var5) instanceof ChestBlockEntity var6) {
                  var6.setLootTable(randomVanillaLootTable(var2));
                  var6.setLootTableSeed(var2.nextLong());
                  var6.setChanged();
                  return;
               }
            }
         }
      }
   }

   // 工具箱：随机发工作台/熔炉/铁砧/砂轮/切石机
   private static void giveToolbox(ServerPlayer var0) {
      Item[] var1 = new Item[]{Items.CRAFTING_TABLE, Items.FURNACE, Items.ANVIL, Items.GRINDSTONE, Items.STONECUTTER};
      Item var2 = var1[ThreadLocalRandom.current().nextInt(var1.length)];
      var0.addItem(new ItemStack(var2));
      var0.sendSystemMessage(
         Component.translatable("hexbingo.msg.toolbox", new Object[]{Component.translatable(var2.getDescriptionId())})
            .withStyle(ChatFormatting.GOLD)
      );
   }

   // Spider Man：贴墙按住前进向上爬，不按前进则贴墙不下落
   public static void tickSpiderMan(ServerPlayer var0) {
      if (!hasHex(var0, "spider_man")
         || SPIDER_CLIMB_OFF.contains(var0.getUUID())
         || var0.isSpectator()
         || var0.getAbilities().flying
         || var0.isFallFlying()
         || var0.isInWater()
         || var0.isInLava()
         || var0.onClimbable()
         || var0.isPassenger()) {
         return;
      }

      // 只吸附完整碰撞方块，避免火把/门/活板门/花草等无完整碰撞方块造成误触
      boolean var1 = hasWallInFront(var0);
      if (!var1) {
         return;
      }

      Input var2 = var0.getLastClientInput();
      Vec3 var3 = var0.getDeltaMovement();
      if (var2 != null && var2.forward() && !var2.shift()) {
         var0.setDeltaMovement(var3.x, Math.max(var3.y, 0.2), var3.z);
         var0.hurtMarked = true;
      } else if (var3.y < 0.0) {
         var0.setDeltaMovement(var3.x, 0.0, var3.z);
         var0.hurtMarked = true;
      }

      var0.resetFallDistance();
   }

   private static boolean hasWallInFront(ServerPlayer var0) {
      Direction var1 = var0.getDirection();
      BlockPos var2 = var0.blockPosition();

      for (int var3 = 1; var3 <= 2; var3++) {
         BlockPos var4 = var2.relative(var1).above(var3);
         BlockState var5 = var0.level().getBlockState(var4);
         if (var5.isCollisionShapeFullBlock(var0.level(), var4)) {
            return true;
         }
      }

      return false;
   }

   public static void toggleSpiderClimb(ServerPlayer var0) {
      if (!hasHex(var0, "spider_man")) {
         return;
      }

      boolean var1;
      if (SPIDER_CLIMB_OFF.contains(var0.getUUID())) {
         SPIDER_CLIMB_OFF.remove(var0.getUUID());
         var1 = true;
      } else {
         SPIDER_CLIMB_OFF.add(var0.getUUID());
         var1 = false;
      }

      var0.sendOverlayMessage(Component.translatable(var1 ? "hexbingo.msg.spider_climb_on" : "hexbingo.msg.spider_climb_off"));
   }

   // 吸铁石：每 tick 把 16 格内掉落物拉向玩家
   public static void tickMagnet(ServerPlayer var0) {
      if (hasHex(var0, "magnet") && var0.level() instanceof ServerLevel var1) {
         for (ItemEntity var3 : var1.getEntitiesOfClass(ItemEntity.class, var0.getBoundingBox().inflate(16.0))) {
            if (!var3.isRemoved()) {
               Vec3 var4 = var0.position().add(0.0, 0.8, 0.0).subtract(var3.position());
               double var5 = var4.length();
               if (var5 < 1.2) {
                  var3.setNoPickUpDelay();
               } else {
                  var3.setDeltaMovement(var4.normalize().scale(Math.min(0.35, 0.1 + var5 * 0.015)));
                  var3.hurtMarked = true;
                  var3.setNoPickUpDelay();
               }
            }
         }
      }
   }

   // 3面骰：每 5 分钟投 d6，按点数给速度/缓慢/禁锢
   public static void tickDice(ServerPlayer var0) {
      UUID var1 = var0.getUUID();
      if (!hasHex(var0, "dice_roll")) {
         DICE_NEXT.remove(var1);
         return;
      }

      long var2 = System.currentTimeMillis();
      Long var4 = DICE_NEXT.get(var1);
      if (var4 == null) {
         DICE_NEXT.put(var1, var2 + DICE_INTERVAL_MS);
         return;
      }

      if (var2 < var4) {
         return;
      }

      // 用当前时间重排下一次骰子，避免暂停/离线过久后连续补投
      DICE_NEXT.put(var1, var2 + DICE_INTERVAL_MS);
      int var5 = ThreadLocalRandom.current().nextInt(1, 7);
      if (var5 > 3) {
         var0.addEffect(new MobEffectInstance(MobEffects.SPEED, 4800, 1, false, true, true));
         var0.sendSystemMessage(
            Component.translatable("hexbingo.msg.dice_speed", new Object[]{Component.literal(Integer.toString(var5))})
               .withStyle(ChatFormatting.AQUA)
         );
      } else if (var5 < 3) {
         var0.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 4800, 0, false, true, true));
         var0.sendSystemMessage(
            Component.translatable("hexbingo.msg.dice_slow", new Object[]{Component.literal(Integer.toString(var5))})
               .withStyle(ChatFormatting.GRAY)
         );
      } else {
         var0.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 120, 255, false, true, true));
         var0.sendSystemMessage(Component.translatable("hexbingo.msg.dice_lock").withStyle(ChatFormatting.RED));
      }
   }

   // 路易十六：首次击杀掉对应头颅
   private static void louisXviDrop(ServerPlayer var0, LivingDropsEvent var1, ServerLevel var2) {
      LOUIS_PENDING.remove(var0.getUUID());
      Item var3 = headFor(var1.getEntity().getType());
      if (var3 == null) {
         var0.sendSystemMessage(Component.translatable("hexbingo.msg.louis_xvi_none").withStyle(ChatFormatting.GRAY));
      } else {
         Entity var4 = var1.getEntity();
         var1.getDrops().add(new ItemEntity(var2, var4.getX(), var4.getY() + 0.5, var4.getZ(), new ItemStack(var3)));
         var0.sendSystemMessage(
            Component.translatable("hexbingo.msg.louis_xvi", new Object[]{var4.getDisplayName()}).withStyle(ChatFormatting.GOLD)
         );
      }
   }

   // 生物类型 → 头颅物品映射
   private static Item headFor(EntityType<?> var0) {
      if (var0 == EntityTypes.ZOMBIE) {
         return Items.ZOMBIE_HEAD;
      } else if (var0 == EntityTypes.HUSK) {
         return Items.ZOMBIE_HEAD;
      } else if (var0 == EntityTypes.SKELETON) {
         return Items.SKELETON_SKULL;
      } else if (var0 == EntityTypes.STRAY) {
         return Items.SKELETON_SKULL;
      } else if (var0 == EntityTypes.WITHER_SKELETON) {
         return Items.WITHER_SKELETON_SKULL;
      } else if (var0 == EntityTypes.CREEPER) {
         return Items.CREEPER_HEAD;
      } else if (var0 == EntityTypes.ENDER_DRAGON) {
         return Items.DRAGON_HEAD;
      } else if (var0 == EntityTypes.PIGLIN) {
         return Items.PIGLIN_HEAD;
      } else {
         return var0 == EntityTypes.PLAYER ? Items.PLAYER_HEAD : null;
      }
   }

   private static final String[] VANILLA_LOOT_TABLES = new String[]{
      "chests/simple_dungeon",
      "chests/abandoned_mineshaft",
      "chests/desert_pyramid",
      "chests/jungle_temple",
      "chests/nether_bridge",
      "chests/stronghold_corridor",
      "chests/stronghold_crossing",
      "chests/stronghold_library",
      "chests/village/village_armorer",
      "chests/village/village_toolsmith",
      "chests/village/village_weaponsmith",
      "chests/end_city_treasure",
      "chests/bastion_treasure",
      "chests/ancient_city",
      "chests/pillager_outpost",
      "chests/shipwreck_supply",
      "chests/buried_treasure",
      "chests/woodland_mansion"
   };

   private static ResourceKey<LootTable> randomVanillaLootTable(ThreadLocalRandom var0) {
      return ResourceKey.create(
         Registries.LOOT_TABLE, Identifier.fromNamespaceAndPath("minecraft", VANILLA_LOOT_TABLES[var0.nextInt(VANILLA_LOOT_TABLES.length)])
      );
   }

   // 快递到了：头顶生成装有随机原版战利品箱的箱子
   private static void airdrop(ServerPlayer var0) {
      if (var0.level() instanceof ServerLevel var1) {
         ThreadLocalRandom var3 = ThreadLocalRandom.current();
         ResourceKey<LootTable> var4 = randomVanillaLootTable(var3);
         BlockPos var5 = var0.blockPosition().above(5);
         var1.setBlockAndUpdate(var5, Blocks.CHEST.defaultBlockState());
         if (var1.getBlockEntity(var5) instanceof ChestBlockEntity var6) {
            var6.setLootTable(var4);
            var6.setLootTableSeed(var3.nextLong());
            var6.setChanged();
         }

         var0.sendSystemMessage(Component.translatable("hexbingo.msg.airdrop").withStyle(ChatFormatting.GOLD));
      }
   }

   // 海神：附魔三叉戟（忠诚/引雷/经验修补）。不带激流，保证陆地可正常掷出
   private static void giveSeaGod(ServerPlayer var0) {
      Registry<Enchantment> var1 = var0.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
      ItemStack var2 = questItem(Items.TRIDENT, "海神", "hexbingo.item.sea_god.lore");
      var2.enchant(var1.wrapAsHolder((Enchantment)var1.getValueOrThrow(Enchantments.LOYALTY)), 3);
      var2.enchant(var1.wrapAsHolder((Enchantment)var1.getValueOrThrow(Enchantments.CHANNELING)), 1);
      var2.enchant(var1.wrapAsHolder((Enchantment)var1.getValueOrThrow(Enchantments.MENDING)), 1);
      var0.addItem(var2);
      var0.sendSystemMessage(Component.translatable("hexbingo.msg.sea_god").withStyle(ChatFormatting.AQUA));
   }

   // 图腾批发：3 个不死图腾
   private static void giveTotems(ServerPlayer var0) {
      for (int var1 = 0; var1 < 3; var1++) {
         var0.addItem(new ItemStack(Items.TOTEM_OF_UNDYING));
      }

      var0.sendSystemMessage(Component.translatable("hexbingo.msg.totem_bulk").withStyle(ChatFormatting.GOLD));
   }

   // 图腾批发：手持不死图腾时解除无敌，允许受到伤害/触发图腾（完成不死图腾成就）
   public static boolean isHoldingTotemBulk(ServerPlayer var0) {
      return hasHex(var0, "totem_bulk") && (var0.getMainHandItem().is(Items.TOTEM_OF_UNDYING) || var0.getOffhandItem().is(Items.TOTEM_OF_UNDYING));
   }

   // ===== 26.2-1.35.0 新增海克斯 =====

   private static final Set<Item> VALUABLE_ITEMS = Set.of(
      Items.DIAMOND,
      Items.DIAMOND_BLOCK,
      Items.EMERALD,
      Items.EMERALD_BLOCK,
      Items.NETHERITE_INGOT,
      Items.NETHERITE_SCRAP,
      Items.NETHERITE_BLOCK,
      Items.ANCIENT_DEBRIS,
      Items.ENCHANTED_GOLDEN_APPLE,
      Items.GOLDEN_APPLE,
      Items.TOTEM_OF_UNDYING,
      Items.ELYTRA,
      Items.TRIDENT,
      Items.NETHER_STAR,
      Items.DRAGON_EGG,
      Items.HEART_OF_THE_SEA
   );

   // 陨石：取所有在线玩家的平均坐标，生成 5-10 颗大小不一的紫水晶陨石
   private static void castMeteor(ServerPlayer var0, HexEffect var1) {
      MinecraftServer var2 = var0.level().getServer();
      if (var2 != null && var0.level() instanceof ServerLevel var3) {
         List<ServerPlayer> var4 = var2.getPlayerList().getPlayers();
         double var5 = 0.0;
         double var6 = 0.0;
         double var7 = 0.0;
         int var8 = 0;

         for (ServerPlayer var9 : var4) {
            if (!var9.isSpectator()) {
               var5 += var9.getX();
               var6 += var9.getY();
               var7 += var9.getZ();
               var8++;
            }
         }

         if (var8 == 0) {
            var5 = var0.getX();
            var6 = var0.getY();
            var7 = var0.getZ();
            var8 = 1;
         }

         var5 /= var8;
         var6 /= var8;
         var7 /= var8;
         int var10 = Math.max(1, var1.min > 0 ? var1.min : 5);
         int var11 = Math.max(var10, var1.max > 0 ? var1.max : 10);
         ThreadLocalRandom var12 = ThreadLocalRandom.current();
         int var13 = var10 + var12.nextInt(var11 - var10 + 1);
         ItemStack var14 = MapItem.create(var3, Mth.floor(var5), Mth.floor(var7), (byte)1, true, true);
         var14.set(DataComponents.CUSTOM_NAME, Component.translatable("hexbingo.item.meteor_map"));

         for (int var15 = 0; var15 < var13; var15++) {
            double var16 = var12.nextDouble() * Math.PI * 2.0;
            double var18 = 24.0 + var12.nextDouble() * 64.0;
            int var20 = Mth.floor(var5 + Math.cos(var16) * var18);
            int var21 = Mth.floor(var7 + Math.sin(var16) * var18);
            int var22 = meteorSurfaceY(var3, var20, var21);
            int var23 = 2 + var12.nextInt(4);
            METEORS.add(new HexEffects.MeteorState(var3, var20 + 0.5, var22 + 72.0, var21 + 0.5, var22, var23, var0.getUUID()));
            MapItemSavedData.addTargetDecoration(var14, new BlockPos(var20, var22, var21), "meteor" + var15, MapDecorationTypes.RED_X);
         }

         var0.addItem(var14);
         var0.sendSystemMessage(Component.translatable("hexbingo.msg.meteor", var13).withStyle(ChatFormatting.DARK_PURPLE));
         meteorQuake(var0, var4);
      }
   }

   // 目标点的地表高度：先前未加载的区块上 getHeight 会返回世界最低点，导致陨石砸进基岩层
   private static int meteorSurfaceY(ServerLevel var0, int var1, int var2) {
      var0.getChunk(var1 >> 4, var2 >> 4);
      int var3 = var0.getHeight(Types.MOTION_BLOCKING_NO_LEAVES, var1, var2);
      if (var3 > var0.getMinY() + 1) {
         return var3;
      }

      for (int var4 = var0.getMaxY() - 1; var4 > var0.getMinY(); var4--) {
         if (!var0.getBlockState(new BlockPos(var1, var4, var2)).isAir()) {
            return var4 + 1;
         }
      }

      return var3;
   }

   private static void meteorQuake(ServerPlayer var0, List<ServerPlayer> var1) {
      Team var2 = team(var0);

      for (ServerPlayer var3 : var1) {
         if (!var3.isSpectator() && var3 != var0 && (var2 == null || !var2.hasPlayer(var3))) {
            var3.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 200, 2, false, true, true));
            quakeBreak(var3);
            if (var3.level() instanceof ServerLevel var4) {
               var4.sendParticles(ParticleTypes.EXPLOSION, var3.getX(), var3.getY(), var3.getZ(), 8, 3.0, 1.0, 3.0, 0.0);
               var4.playSound(null, var3.getX(), var3.getY(), var3.getZ(), SoundEvents.GENERIC_EXPLODE.value(), SoundSource.PLAYERS, 0.6F, 0.6F);
               var3.sendSystemMessage(Component.translatable("hexbingo.msg.meteor_quake").withStyle(ChatFormatting.DARK_PURPLE));
            }
         }
      }
   }

   private static void quakeBreak(ServerPlayer var0) {
      if (var0.level() instanceof ServerLevel var1) {
         BlockPos var2 = var0.blockPosition();
         List<BlockPos> var3 = new ArrayList<>();

         for (int var4 = -5; var4 < 5; var4++) {
            for (int var5 = -5; var5 < 5; var5++) {
               for (int var6 = 0; var6 >= -2; var6--) {
                  BlockPos var7 = var2.offset(var4, var6, var5);
                  BlockState var8 = var1.getBlockState(var7);
                  if (!var8.isAir() && var8.getDestroySpeed(var1, var7) >= 0.0F) {
                     var3.add(var7);
                  }
               }
            }
         }

         if (!var3.isEmpty()) {
            // 打乱顺序后逐 tick 崩塌，模拟真实地震
            Collections.shuffle(var3, ThreadLocalRandom.current());
            QUAKES.add(new HexEffects.QuakeState(var1, var3));
         }
      }
   }

   public static void tickQuakes(MinecraftServer var0) {
      if (!QUAKES.isEmpty()) {
         Iterator<HexEffects.QuakeState> var1 = QUAKES.iterator();

         while (var1.hasNext()) {
            HexEffects.QuakeState var2 = var1.next();
            if (var2.index >= var2.blocks.size()) {
               var1.remove();
               continue;
            }

            BlockPos var3 = var2.blocks.get(var2.index++);
            BlockState var4 = var2.level.getBlockState(var3);
            if (!var4.isAir() && var4.getDestroySpeed(var2.level, var3) >= 0.0F) {
               var2.level.setBlockAndUpdate(var3, Blocks.AIR.defaultBlockState());
               if (var2.index % 8 == 0) {
                  var2.level.sendParticles(
                     ParticleTypes.POOF, var3.getX() + 0.5, var3.getY() + 0.5, var3.getZ() + 0.5, 2, 0.3, 0.3, 0.3, 0.0
                  );
                  var2.level.playSound(
                     null, var3.getX(), var3.getY(), var3.getZ(), SoundEvents.GRAVEL_BREAK, SoundSource.BLOCKS, 0.5F, 0.6F
                  );
               }
            }
         }
      }
   }

   public static void tickMeteors(MinecraftServer var0) {
      if (!METEORS.isEmpty()) {
         Iterator<HexEffects.MeteorState> var1 = METEORS.iterator();

         while (var1.hasNext()) {
            HexEffects.MeteorState var2 = var1.next();
            var2.y -= 4.5;
            if (var2.y > var2.targetY + 1.0) {
               var2.level.sendParticles(ParticleTypes.FLAME, var2.x, var2.y, var2.z, 6, 0.5, 0.8, 0.5, 0.02);
               var2.level.sendParticles(ParticleTypes.LARGE_SMOKE, var2.x, var2.y + 1.5, var2.z, 4, 0.5, 1.0, 0.5, 0.01);
            } else {
               var1.remove();
               meteorImpact(var0, var2);
            }
         }
      }
   }

   private static void meteorImpact(MinecraftServer var0, HexEffects.MeteorState var1) {
      ServerLevel var2 = var1.level;
      ThreadLocalRandom var3 = ThreadLocalRandom.current();
      int var4 = var1.radius;
      BlockPos var5 = BlockPos.containing(var1.x, var1.targetY, var1.z);

      for (int var6 = -var4; var6 <= var4; var6++) {
         for (int var7 = -var4; var7 <= var4; var7++) {
            for (int var8 = -var4; var8 <= 1; var8++) {
               if (var6 * var6 + var8 * var8 + var7 * var7 <= var4 * var4) {
                  BlockPos var9 = var5.offset(var6, var8, var7);
                  BlockState var10 = var2.getBlockState(var9);
                  if (!var10.isAir() && var10.getDestroySpeed(var2, var9) >= 0.0F) {
                     var2.setBlockAndUpdate(var9, Blocks.AIR.defaultBlockState());
                  }
               }
            }
         }
      }

      var2.setBlockAndUpdate(var5, Blocks.AMETHYST_BLOCK.defaultBlockState());
      var2.setBlockAndUpdate(var5.above(), Blocks.AMETHYST_BLOCK.defaultBlockState());
      var2.setBlockAndUpdate(var5.above(2), Blocks.BUDDING_AMETHYST.defaultBlockState());

      for (int var11 = 0; var11 < 4; var11++) {
         BlockPos var12 = var5.offset(var3.nextInt(7) - 3, 0, var3.nextInt(7) - 3);
         if (var2.getBlockState(var12).isAir()) {
            var2.setBlockAndUpdate(var12, Blocks.CALCITE.defaultBlockState());
         }
      }

      spawnMeteorChest(var1, var5);
      ServerPlayer var16 = var0.getPlayerList().getPlayer(var1.owner);
      int var17 = 1 + var3.nextInt(3);

      for (int var18 = 0; var18 < var17; var18++) {
         Skeleton var19 = (Skeleton)EntityTypes.SKELETON.create(var2, EntitySpawnReason.MOB_SUMMONED);
         if (var19 != null) {
            var19.setPos(var5.getX() + var3.nextDouble() * 8.0 - 4.0 + 0.5, var5.getY() + 1.0, var5.getZ() + var3.nextDouble() * 8.0 - 4.0 + 0.5);
            Item[] var20 = new Item[]{Items.IRON_HELMET, Items.CHAINMAIL_HELMET, Items.GOLDEN_HELMET, Items.DIAMOND_HELMET};
            Item[] var21 = new Item[]{Items.IRON_CHESTPLATE, Items.CHAINMAIL_CHESTPLATE, Items.GOLDEN_CHESTPLATE, Items.DIAMOND_CHESTPLATE};
            Item[] var22 = new Item[]{Items.IRON_LEGGINGS, Items.CHAINMAIL_LEGGINGS, Items.GOLDEN_LEGGINGS, Items.DIAMOND_LEGGINGS};
            Item[] var23 = new Item[]{Items.IRON_BOOTS, Items.CHAINMAIL_BOOTS, Items.GOLDEN_BOOTS, Items.DIAMOND_BOOTS};
            Item[] var24 = new Item[]{Items.IRON_SWORD, Items.BOW, Items.DIAMOND_SWORD, Items.STONE_SWORD};
            var19.setItemSlot(EquipmentSlot.HEAD, meteorArmor(var16, var20[var3.nextInt(var20.length)]));
            var19.setItemSlot(EquipmentSlot.CHEST, meteorArmor(var16, var21[var3.nextInt(var21.length)]));
            var19.setItemSlot(EquipmentSlot.LEGS, meteorArmor(var16, var22[var3.nextInt(var22.length)]));
            var19.setItemSlot(EquipmentSlot.FEET, meteorArmor(var16, var23[var3.nextInt(var23.length)]));
            var19.setItemSlot(EquipmentSlot.MAINHAND, meteorArmor(var16, var24[var3.nextInt(var24.length)]));
            var19.setPersistenceRequired();
            var2.addFreshEntity(var19);
         }
      }

      var2.sendParticles(ParticleTypes.EXPLOSION_EMITTER, var1.x, var1.targetY + 0.5, var1.z, 2, 1.0, 1.0, 1.0, 0.0);
      var2.playSound(null, var1.x, var1.targetY, var1.z, SoundEvents.GENERIC_EXPLODE.value(), SoundSource.WEATHER, 1.2F, 0.7F);
   }

   private static ItemStack meteorArmor(ServerPlayer var0, Item var1) {
      return var0 == null ? new ItemStack(var1) : randomlyEnchantedArmor(var0, var1);
   }

   private static void spawnMeteorChest(HexEffects.MeteorState var0, BlockPos var1) {
      ThreadLocalRandom var2 = ThreadLocalRandom.current();

      for (int var3 = 0; var3 < 12; var3++) {
         BlockPos var4 = var1.offset(var2.nextInt(9) - 4, 1, var2.nextInt(9) - 4);
         if (var0.level.getBlockState(var4).isAir()) {
            var0.level.setBlockAndUpdate(var4, Blocks.CHEST.defaultBlockState());
            if (var0.level.getBlockEntity(var4) instanceof ChestBlockEntity var5) {
               int var6 = 9 + var2.nextInt(10);

               for (int var7 = 0; var7 < var6; var7++) {
                  var5.setItem(var7, new ItemStack(lootItem(var2, 3), 1 + var2.nextInt(2)));
               }

               var5.setChanged();
               return;
            }
         }
      }
   }

   // 致盲：右键使所有敌人失明 8 秒，冷却 3 分钟
   private static void giveBlindItem(ServerPlayer var0) {
      if (!hasNamedItem(var0, "致盲")) {
         var0.addItem(questItem(Items.INK_SAC, "致盲", "hexbingo.item.blind.lore"));
         var0.sendSystemMessage(Component.translatable("hexbingo.msg.blind_item").withStyle(ChatFormatting.GRAY));
      }
   }

   public static boolean castBlind(ServerPlayer var0) {
      if (!hasHex(var0, "blind")) {
         return false;
      }

      long var1 = System.currentTimeMillis();
      Long var3 = BLIND_COOLDOWN.get(var0.getUUID());
      if (var3 != null && var1 < var3) {
         long var4 = (var3 - var1 + 999L) / 1000L;
         var0.sendSystemMessage(Component.translatable("hexbingo.msg.blind_cooldown", var4).withStyle(ChatFormatting.GRAY));
         return true;
      }

      if (!(var0.level() instanceof ServerLevel var10)) {
         return false;
      }

      Team var11 = team(var0);
      int var5 = 0;

      for (ServerPlayer var7 : var10.players()) {
         if (var7 != var0 && !var7.isSpectator() && (var11 == null || !var11.hasPlayer(var7))) {
            var7.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 160, 0, false, true, true));
            var7.sendSystemMessage(Component.translatable("hexbingo.msg.blinded").withStyle(ChatFormatting.DARK_GRAY));
            var5++;
         }
      }

      if (var5 == 0) {
         var0.sendSystemMessage(Component.translatable("hexbingo.msg.blind_no_target").withStyle(ChatFormatting.GRAY));
         return true;
      }

      BLIND_COOLDOWN.put(var0.getUUID(), var1 + skillCooldown(var0, 180000L));
      var10.playSound(null, var0.getX(), var0.getY(), var0.getZ(), SoundEvents.SQUID_SQUIRT, SoundSource.PLAYERS, 1.0F, 0.9F);
      var0.sendSystemMessage(Component.translatable("hexbingo.msg.blind_cast", var5).withStyle(ChatFormatting.DARK_GRAY));
      return true;
   }

   // 结束了？：传送到末地外岛 10 分钟，期间无法被传送
   private static final ThreadLocal<Boolean> EXILE_BYPASS = ThreadLocal.withInitial(() -> Boolean.FALSE);

   private static void exileTeleport(ServerPlayer var0, ServerLevel var1, double var2, double var4, double var6, float var8, float var9) {
      EXILE_BYPASS.set(Boolean.TRUE);

      try {
         var0.teleportTo(var1, var2, var4, var6, Set.of(), var8, var9, false);
      } finally {
         EXILE_BYPASS.set(Boolean.FALSE);
      }
   }

   public static boolean isExileBypassing() {
      return EXILE_BYPASS.get();
   }

   public static void onSpecialDeath(ServerPlayer var0) {
      endBatForm(var0, false);
      END_EXILE.remove(var0.getUUID());
   }

   private static void startEndExile(ServerPlayer var0, int var1) {
      MinecraftServer var2 = var0.level().getServer();
      if (var2 == null) {
         return;
      }

      ServerLevel var3 = var2.getLevel(Level.END);
      if (var3 == null) {
         var0.sendSystemMessage(Component.translatable("hexbingo.msg.end_exile_fail").withStyle(ChatFormatting.GRAY));
         return;
      }

      ThreadLocalRandom var4 = ThreadLocalRandom.current();
      int var9 = 0;
      int var10 = 0;
      int var11 = -1;

      for (int var12 = 0; var12 < 12; var12++) {
         double var5 = var4.nextDouble() * Math.PI * 2.0;
         double var7 = 1200.0 + var4.nextDouble() * 600.0;
         var9 = Mth.floor(Math.cos(var5) * var7);
         var10 = Mth.floor(Math.sin(var5) * var7);
         // 末地外岛区块默认未加载，getHeight 对未加载区块会返回最低点，必须先加载区块
         var3.getChunk(var9 >> 4, var10 >> 4);
         var11 = var3.getHeight(Types.MOTION_BLOCKING_NO_LEAVES, var9, var10);
         if (var11 > var3.getMinY()) {
            break;
         }
      }

      if (var11 <= var3.getMinY()) {
         var0.sendSystemMessage(Component.translatable("hexbingo.msg.end_exile_no_spot").withStyle(ChatFormatting.GRAY));
         return;
      }
      END_EXILE.put(
         var0.getUUID(),
         new HexEffects.ExileState(
            var0.level().dimension(),
            var0.getX(),
            var0.getY(),
            var0.getZ(),
            var0.getYRot(),
            var0.getXRot(),
            System.currentTimeMillis() + var1 * 1000L,
            var9 + 0.5,
            var11 + 1.0,
            var10 + 0.5
         )
      );
      exileTeleport(var0, var3, var9 + 0.5, var11 + 1.0, var10 + 0.5, var0.getYRot(), var0.getXRot());
      var0.resetFallDistance();
      var0.sendSystemMessage(Component.translatable("hexbingo.msg.end_exile_start", var1 / 60).withStyle(ChatFormatting.DARK_PURPLE));
   }

   public static boolean isEndExiled(ServerPlayer var0) {
      return END_EXILE.containsKey(var0.getUUID());
   }

   public static void tickEndExile(MinecraftServer var0) {
      long var1 = System.currentTimeMillis();
      Iterator<Map.Entry<UUID, HexEffects.ExileState>> var3 = END_EXILE.entrySet().iterator();

      while (var3.hasNext()) {
         Map.Entry<UUID, HexEffects.ExileState> var4 = var3.next();
         ServerPlayer var5 = var0.getPlayerList().getPlayer(var4.getKey());
         if (var5 == null) {
            var3.remove();
         } else {
            HexEffects.ExileState var6 = var4.getValue();
            if (var1 >= var6.until) {
               ServerLevel var7 = var0.getLevel(var6.dimension);
               if (var7 != null) {
                  exileTeleport(var5, var7, var6.x, var6.y, var6.z, var6.yaw, var6.pitch);
               }

               var3.remove();
               var5.sendSystemMessage(Component.translatable("hexbingo.msg.end_exile_end").withStyle(ChatFormatting.GREEN));
            } else if (!var5.level().dimension().equals(Level.END)) {
               ServerLevel var8 = var0.getLevel(Level.END);
               if (var8 != null) {
                  exileTeleport(var5, var8, var6.endX, var6.endY, var6.endZ, var5.getYRot(), var5.getXRot());
               }

               var5.sendSystemMessage(Component.translatable("hexbingo.msg.end_exile_blocked").withStyle(ChatFormatting.DARK_PURPLE));
            }
         }
      }
   }

   private static void endEndExile(ServerPlayer var0, boolean var1) {
      HexEffects.ExileState var2 = END_EXILE.remove(var0.getUUID());
      if (var2 != null && var0.level().getServer() != null) {
         ServerLevel var3 = var0.level().getServer().getLevel(var2.dimension);
         if (var3 != null) {
            exileTeleport(var0, var3, var2.x, var2.y, var2.z, var2.yaw, var2.pitch);
         }

         if (var1) {
            var0.sendSystemMessage(Component.translatable("hexbingo.msg.end_exile_end").withStyle(ChatFormatting.GREEN));
         }
      }
   }

   // 自动熔炼：原矿掉落直接变成成品并额外给经验
   private static Item autoSmeltResult(Item var0) {
      if (var0 == Items.RAW_IRON) {
         return Items.IRON_INGOT;
      } else if (var0 == Items.RAW_GOLD) {
         return Items.GOLD_INGOT;
      } else if (var0 == Items.RAW_COPPER) {
         return Items.COPPER_INGOT;
      } else {
         return var0 == Items.ANCIENT_DEBRIS ? Items.NETHERITE_SCRAP : null;
      }
   }

   private static void autoSmeltDrops(ServerPlayer var0, Collection<ItemEntity> var1) {
      int var2 = 0;

      for (ItemEntity var4 : var1) {
         ItemStack var5 = var4.getItem();
         Item var6 = autoSmeltResult(var5.getItem());
         if (var6 != null) {
            var4.setItem(new ItemStack(var6, var5.getCount()));
            var2 += var5.getCount();
         }
      }

      if (var2 > 0) {
         var0.giveExperiencePoints(Math.max(1, var2 + ThreadLocalRandom.current().nextInt(var2 + 1)));
      }
   }

   // 任务护盾：锁定权重最高的 3 个任务，其他海克斯无法将其退回
   private static void applyTaskShield(ServerPlayer var0, int var1) {
      Bingo var2 = Bingo.get(var0.level());
      Team var3 = var2.getTeam(var0);
      if (var3 != null) {
         ArrayList<Integer> var4 = new ArrayList<>();

         for (int var5 = 0; var5 < var2.tasks().size(); var5++) {
            if (var2.task(var5) != Task.EMPTY) {
               var4.add(var5);
            }
         }

         var4.sort((var5x, var6x) -> Integer.compare(var2.task(var6x).weight(), var2.task(var5x).weight()));
         Set<Task> var7 = TASK_SHIELDS.computeIfAbsent(var3.color.getName(), var0x -> new HashSet<>());
         MutableComponent var8 = Component.empty();
         int var9 = 0;

         for (int var10 : var4) {
            if (var9 >= var1) {
               break;
            }

            Task var11 = var2.task(var10);
            if (var7.add(var11)) {
               if (var9 > 0) {
                  var8.append(Component.literal("、"));
               }

               var8.append(var11.contentName(var0.level().getServer()));
               var9++;
            }
         }

         var0.sendSystemMessage(Component.translatable("hexbingo.msg.task_shield", var9, var8).withStyle(ChatFormatting.GOLD));
      }
   }

   public static boolean isTaskShielded(Team var0, Bingo var1, int var2) {
      Set<Task> var3 = TASK_SHIELDS.get(var0.color.getName());
      return var3 != null && var3.contains(var1.task(var2));
   }

   public static void clearTaskShields() {
      TASK_SHIELDS.clear();
   }

   // 点石成金：按 G 把副手物品变成等量金锭
   public static void castMidas(ServerPlayer var0) {
      if (hasHex(var0, "midas_touch")) {
         long var1 = System.currentTimeMillis();
         Long var3 = MIDAS_COOLDOWN.get(var0.getUUID());
         if (var3 != null && var1 < var3) {
            long var4 = (var3 - var1 + 999L) / 1000L;
            var0.sendSystemMessage(Component.translatable("hexbingo.msg.midas_cooldown", var4).withStyle(ChatFormatting.GRAY));
         } else {
            ItemStack var6 = var0.getOffhandItem();
            if (var6.isEmpty()) {
               var0.sendSystemMessage(Component.translatable("hexbingo.msg.midas_empty").withStyle(ChatFormatting.GRAY));
            } else if (!var6.has(DataComponents.CUSTOM_NAME) && !VALUABLE_ITEMS.contains(var6.getItem()) && !var6.isEnchanted()) {
               int var7 = var6.getCount();
               var0.setItemInHand(InteractionHand.OFF_HAND, new ItemStack(Items.GOLD_INGOT, var7));
               MIDAS_COOLDOWN.put(var0.getUUID(), var1 + skillCooldown(var0, 60000L));
               var0.sendSystemMessage(Component.translatable("hexbingo.msg.midas_done", var7).withStyle(ChatFormatting.GOLD));
               var0.level().playSound(null, var0.getX(), var0.getY(), var0.getZ(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 0.5F, 2.0F);
            } else {
               var0.sendSystemMessage(Component.translatable("hexbingo.msg.midas_valuable").withStyle(ChatFormatting.GRAY));
            }
         }
      }
   }

   // 蝙蝠形态：右键变成蝙蝠 60 秒，可飞行、无法攻击、血量与体型变小
   private static void giveBatItem(ServerPlayer var0) {
      if (!hasNamedItem(var0, "蝙蝠形态")) {
         var0.addItem(questItem(Items.PHANTOM_MEMBRANE, "蝙蝠形态", "hexbingo.item.bat.lore"));
         var0.sendSystemMessage(Component.translatable("hexbingo.msg.bat_item").withStyle(ChatFormatting.GOLD));
      }
   }

   public static boolean castBatForm(ServerPlayer var0) {
      if (!hasHex(var0, "bat_form")) {
         return false;
      }

      long var1 = System.currentTimeMillis();
      if (BAT_UNTIL.containsKey(var0.getUUID())) {
         return true;
      }

      Long var3 = BAT_COOLDOWN.get(var0.getUUID());
      if (var3 != null && var1 < var3) {
         long var4 = (var3 - var1 + 999L) / 1000L;
         var0.sendSystemMessage(Component.translatable("hexbingo.msg.bat_cooldown", var4).withStyle(ChatFormatting.GRAY));
         return true;
      }

      BAT_UNTIL.put(var0.getUUID(), var1 + 60000L);
      BAT_COOLDOWN.put(var0.getUUID(), var1 + skillCooldown(var0, 420000L));
      AttributeInstance var6 = var0.getAttribute(Attributes.SCALE);
      if (var6 != null) {
         // 约为 0.5 格高（玩家默认 1.8 格）
         var6.addOrUpdateTransientModifier(new AttributeModifier(modifierId("bat_form", 0), -0.7222, Operation.ADD_MULTIPLIED_BASE));
      }

      AttributeInstance var7 = var0.getAttribute(Attributes.MAX_HEALTH);
      if (var7 != null) {
         var7.addOrUpdateTransientModifier(new AttributeModifier(modifierId("bat_form", 1), -14.0, Operation.ADD_VALUE));
      }

      // 玩家飞行速度来自 Abilities，不能用 Attributes.FLYING_SPEED
      BAT_FLYING_SPEED.put(var0.getUUID(), var0.getAbilities().getFlyingSpeed());
      var0.getAbilities().setFlyingSpeed(var0.getAbilities().getFlyingSpeed() * 0.65F);

      var0.setHealth(Math.min(var0.getHealth(), 6.0F));
      var0.getAbilities().mayfly = true;
      var0.getAbilities().flying = false;
      var0.onUpdateAbilities();
      var0.setInvisible(true);
      ensureBatDisguise(var0);
      var0.sendSystemMessage(Component.translatable("hexbingo.msg.bat_start").withStyle(ChatFormatting.GOLD));
      return true;
   }

   // 蝙蝠形态：本体隐形 + 一只无 AI 的真蝙蝠跟随，参照精怪魔法的伪装实现
   private static void ensureBatDisguise(ServerPlayer var0) {
      if (var0.level() instanceof ServerLevel var1) {
         Bat var2 = BAT_DISGUISES.get(var0.getUUID());
         if (var2 != null && var2.isAlive()) {
            if (var2.level() != var1) {
               var2.teleportTo(var1, var0.getX(), batFootY(var0), var0.getZ(), Set.of(), var0.getYRot(), 0.0F, false);
            }

            return;
         }

         if (var2 != null) {
            var2.discard();
         }

         Bat var3 = EntityTypes.BAT.create(var1, EntitySpawnReason.MOB_SUMMONED);
         if (var3 != null) {
            var3.setPos(var0.getX(), batFootY(var0), var0.getZ());
            var3.setYRot(var0.getYRot());
            var3.setYHeadRot(var0.getYHeadRot());
            var3.setResting(false);
            var3.setNoGravity(true);
            var3.setInvulnerable(true);
            var3.setPersistenceRequired();
            var3.noPhysics = true;
            // 缩到约 0.4 格，贴在玩家脚边，避免第一人称时糊在镜头上
            AttributeInstance var4 = var3.getAttribute(Attributes.SCALE);
            if (var4 != null) {
               var4.addOrUpdateTransientModifier(new AttributeModifier(modifierId("bat_form", 0), -0.55, Operation.ADD_MULTIPLIED_BASE));
            }
            var3.setCustomName(Component.literal("蝙蝠形态"));
            var3.setCustomNameVisible(false);
            var1.addFreshEntity(var3);
            BAT_DISGUISES.put(var0.getUUID(), var3);
         }
      }
   }

   /** 蝙蝠贴地位置：玩家脚部（略微抬高避免卡进地面）。 */
   private static double batFootY(ServerPlayer var0) {
      return var0.getY() + 0.02;
   }

   public static boolean isBatForm(ServerPlayer var0) {
      return BAT_UNTIL.containsKey(var0.getUUID());
   }

   public static void tickBatForms(MinecraftServer var0) {
      if (!BAT_UNTIL.isEmpty()) {
         long var1 = System.currentTimeMillis();

         for (UUID var4 : new ArrayList<>(BAT_UNTIL.keySet())) {
            ServerPlayer var5 = var0.getPlayerList().getPlayer(var4);
            Long var6 = BAT_UNTIL.get(var4);
            if (var5 == null) {
               Bat var7x = BAT_DISGUISES.remove(var4);
               if (var7x != null) {
                  var7x.discard();
               }
            } else if (var6 != null && var1 < var6) {
               ensureBatDisguise(var5);
               Bat var7 = BAT_DISGUISES.get(var4);
               if (var7 != null && var7.isAlive()) {
                  double var8 = batFootY(var5);
                  if (var7.level() != var5.level()) {
                     var7.teleportTo((ServerLevel)var5.level(), var5.getX(), var8, var5.getZ(), Set.of(), var5.getYRot(), 0.0F, false);
                  }

                  var7.setPos(var5.getX(), var8, var5.getZ());
                  var7.setYRot(var5.getYRot());
                  var7.setYHeadRot(var5.getYHeadRot());
                  var7.setXRot(0.0F);
                  var7.setResting(false);
                  var7.setDeltaMovement(Vec3.ZERO);
               }
            } else {
               endBatForm(var5, true);
            }
         }
      }
   }

   public static void onBatLogout(ServerPlayer var0) {
      endBatForm(var0, false);
   }

   private static void endBatForm(ServerPlayer var0, boolean var1) {
      if (BAT_UNTIL.remove(var0.getUUID()) != null) {
         Bat var4 = BAT_DISGUISES.remove(var0.getUUID());
         if (var4 != null) {
            var4.discard();
         }

         var0.setInvisible(false);
         AttributeInstance var2 = var0.getAttribute(Attributes.SCALE);
         if (var2 != null) {
            var2.removeModifier(modifierId("bat_form", 0));
         }

         AttributeInstance var3 = var0.getAttribute(Attributes.MAX_HEALTH);
         if (var3 != null) {
            var3.removeModifier(modifierId("bat_form", 1));
         }

         Float var8 = BAT_FLYING_SPEED.remove(var0.getUUID());
         if (var8 != null) {
            var0.getAbilities().setFlyingSpeed(var8);
            var0.onUpdateAbilities();
         }

         if (!var0.isCreative() && !var0.isSpectator()) {
            var0.getAbilities().mayfly = false;
            var0.getAbilities().flying = false;
            var0.onUpdateAbilities();
         }

         var0.setHealth(Math.min(var0.getHealth(), var0.getMaxHealth()));
         if (var1) {
            // 形态结束给 1 分钟缓降，避免从飞行状态直接摔死
            var0.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 1200, 0, true, false, true));
            var0.sendSystemMessage(Component.translatable("hexbingo.msg.bat_end").withStyle(ChatFormatting.GRAY));
         }
      }
   }

   // 精神控制：右键控制一名敌人 15 秒的移动方向
   private static void giveMindItem(ServerPlayer var0) {
      if (!hasNamedItem(var0, "精神控制")) {
         var0.addItem(questItem(Items.PRISMARINE_CRYSTALS, "精神控制", "hexbingo.item.mind.lore"));
         var0.sendSystemMessage(Component.translatable("hexbingo.msg.mind_item").withStyle(ChatFormatting.LIGHT_PURPLE));
      }
   }

   public static boolean castMindControl(ServerPlayer var0) {
      if (!hasHex(var0, "mind_control")) {
         return false;
      }

      long var1 = System.currentTimeMillis();
      Long var3 = MIND_COOLDOWN.get(var0.getUUID());
      if (var3 != null && var1 < var3) {
         long var4 = (var3 - var1 + 999L) / 1000L;
         var0.sendSystemMessage(Component.translatable("hexbingo.msg.mind_cooldown", var4).withStyle(ChatFormatting.GRAY));
         return true;
      }

      ServerPlayer var6 = nearestEnemy(var0, 30.0);
      if (var6 == null) {
         var0.sendSystemMessage(Component.translatable("hexbingo.msg.mind_no_target").withStyle(ChatFormatting.GRAY));
         return true;
      }

      MIND_CONTROL.put(var0.getUUID(), new HexEffects.MindState(var6.getUUID(), var1 + 15000L));
      MIND_COOLDOWN.put(var0.getUUID(), var1 + skillCooldown(var0, 300000L));
      var0.sendSystemMessage(Component.translatable("hexbingo.msg.mind_cast", var6.getDisplayName()).withStyle(ChatFormatting.LIGHT_PURPLE));
      var6.sendSystemMessage(Component.translatable("hexbingo.msg.mind_controlled").withStyle(ChatFormatting.DARK_PURPLE));
      return true;
   }

   private static ServerPlayer nearestEnemy(ServerPlayer var0, double var1) {
      if (!(var0.level() instanceof ServerLevel var3)) {
         return null;
      }

      Team var4 = team(var0);
      ServerPlayer var5 = null;
      double var6 = var1 * var1;

      for (ServerPlayer var8 : var3.players()) {
         if (var8 != var0 && !var8.isSpectator() && (var4 == null || !var4.hasPlayer(var8))) {
            double var9 = var0.distanceToSqr(var8);
            if (var9 <= var6) {
               var6 = var9;
               var5 = var8;
            }
         }
      }

      return var5;
   }

   /** 每 tick 由被控制者的 PlayerTickEvent 调用。 */
   public static void tickMindControl(ServerPlayer var0) {
      if (!MIND_CONTROL.isEmpty()) {
         long var1 = System.currentTimeMillis();
         Iterator<Map.Entry<UUID, HexEffects.MindState>> var3 = MIND_CONTROL.entrySet().iterator();

         while (var3.hasNext()) {
            Map.Entry<UUID, HexEffects.MindState> var4 = var3.next();
            HexEffects.MindState var5 = var4.getValue();
            if (!var5.target.equals(var0.getUUID())) {
               continue;
            }

            if (var1 >= var5.until) {
               var3.remove();
               var0.sendSystemMessage(Component.translatable("hexbingo.msg.mind_end").withStyle(ChatFormatting.GRAY));
               continue;
            }

            MinecraftServer var6 = var0.level().getServer();
            ServerPlayer var7 = var6 == null ? null : var6.getPlayerList().getPlayer(var4.getKey());
            if (var7 == null) {
               var3.remove();
               continue;
            }

            Vec3 var8 = var7.getLookAngle();
            var0.setDeltaMovement(var8.x * 0.35, var0.getDeltaMovement().y, var8.z * 0.35);
            var0.hurtMarked = true;
         }
      }
   }

   // 目标封锁：指定一个任务，30 秒内所有队伍无法完成
   private static void giveDenyItem(ServerPlayer var0) {
      if (!hasNamedItem(var0, "目标封锁")) {
         var0.addItem(questItem(Items.BARRIER, "目标封锁", "hexbingo.item.deny.lore"));
         var0.sendSystemMessage(Component.translatable("hexbingo.msg.deny_item").withStyle(ChatFormatting.LIGHT_PURPLE));
      }
   }

   public static boolean denyReady(ServerPlayer var0) {
      if (!hasHex(var0, "objective_deny")) {
         return false;
      }

      Long var1 = DENY_COOLDOWN.get(var0.getUUID());
      if (var1 != null && System.currentTimeMillis() < var1) {
         long var2 = (var1 - System.currentTimeMillis() + 999L) / 1000L;
         var0.sendSystemMessage(Component.translatable("hexbingo.msg.deny_cooldown", var2).withStyle(ChatFormatting.GRAY));
         return false;
      }

      return true;
   }

   public static void applyObjectiveDeny(ServerPlayer var0, int var1) {
      MinecraftServer var2 = var0.level().getServer();
      if (var2 != null && hasHex(var0, "objective_deny") && denyReady(var0)) {
         Bingo var3 = Bingo.get(var2.overworld());
         if (var1 >= 0 && var1 < var3.tasks().size()) {
            OBJECTIVE_DENY = new HexEffects.DenyState(var1, System.currentTimeMillis() + 30000L);
            DENY_COOLDOWN.put(var0.getUUID(), System.currentTimeMillis() + skillCooldown(var0, 360000L));
            Component var4 = var3.task(var1).contentName(var2);
            Component var5 = Component.translatable("hexbingo.msg.deny_cast", var0.getDisplayName(), var4).withStyle(ChatFormatting.RED);

            for (ServerPlayer var7 : var2.getPlayerList().getPlayers()) {
               var7.sendSystemMessage(var5);
            }
         }
      }
   }

   /** 由 BingoTaskEvent 调用；命中被封锁任务时立即退回并返回 true。 */
   public static boolean handleObjectiveDeny(BingoTaskEvent var0) {
      if (OBJECTIVE_DENY == null) {
         return false;
      }

      long var1 = System.currentTimeMillis();
      if (var1 >= OBJECTIVE_DENY.until) {
         OBJECTIVE_DENY = null;
         return false;
      }

      Bingo var3 = var0.getBingo();
      int var4 = -1;

      for (int var5 = 0; var5 < var3.tasks().size(); var5++) {
         if (var3.task(var5) == var0.getTask()) {
            var4 = var5;
            break;
         }
      }

      if (var4 != OBJECTIVE_DENY.taskIndex) {
         return false;
      }

      Team var10 = var3.getTeam(var0.getPlayer());
      MinecraftServer var6 = var0.getLevel().getServer();
      if (var10 != null && var6 != null) {
         uncompleteSlot(var6, var3, var10, var4);
         Component var7 = Component.translatable("hexbingo.msg.deny_blocked", var0.getTask().contentName(var6)).withStyle(ChatFormatting.RED);

         for (ServerPlayer var9 : var6.getPlayerList().getPlayers()) {
            var9.sendSystemMessage(var7);
         }
      }

      return true;
   }

   // 海克斯发放的道具（鞘翅、龙蛋等）触发的成就不计入 Bingo 成就任务，但成就本身照常显示
   public static boolean handleHexItemAdvancement(BingoTaskEvent var0) {
      if (var0.getTask().getType() != TaskTypeAdvancement.INSTANCE) {
         return false;
      }

      MinecraftServer var1 = var0.getLevel().getServer();
      if (var1 == null) {
         return false;
      }

      Object var2 = taskElement(var0.getTask());
      if (!(var2 instanceof Identifier var3)) {
         return false;
      }

      AdvancementHolder var4 = var1.getAdvancements().get(var3);
      if (var4 == null) {
         return false;
      }

      Optional<DisplayInfo> var5 = var4.value().display();
      if (var5.isEmpty()) {
         return false;
      }

      ItemStack var6 = var5.get().getIcon().create();
      ServerPlayer var7 = var0.getPlayer();
      if (var6.isEmpty() || !hasHexGrantedItem(var7, var6.getItem())) {
         return false;
      }

      Bingo var8 = var0.getBingo();
      Team var9 = var8.getTeam(var7);
      int var10 = var8.tasks().indexOf(var0.getTask());
      if (var9 == null || var10 < 0 || !var9.completed(var10) || isTaskShielded(var9, var8, var10)) {
         return false;
      }

      uncompleteSlot(var1, var8, var9, var10);
      releaseSlotLocks(var8, var10);
      Component var11 = Component.translatable("hexbingo.msg.hex_item_advancement", var0.getTask().contentName(var1)).withStyle(ChatFormatting.GRAY);

      for (ServerPlayer var13 : var1.getPlayerList().getPlayers()) {
         var13.sendSystemMessage(var11);
      }

      return true;
   }

   /** 退回完成时同时解除其它队伍因占点/锁王模式被连带锁定的该格。 */
   private static void releaseSlotLocks(Bingo var0, int var1) {
      for (DyeColor var2 : DyeColor.values()) {
         Team var3 = var0.getTeam(var2);
         if (var3 == null || var3.isEmpty() || !var3.locked(var1)) {
            continue;
         }

         ArrayList<Integer> var4 = new ArrayList<>();

         for (int var5 = 0; var5 < 25; var5++) {
            if (var3.locked(var5)) {
               var4.add(var5);
            }
         }

         var3.resetLocked();

         for (int var6 : var4) {
            if (var6 != var1) {
               var3.lock(var6);
            }
         }
      }
   }

   private static boolean hasHexGrantedItem(ServerPlayer var0, Item var1) {
      for (int var2 = 0; var2 < var0.getInventory().getContainerSize(); var2++) {
         ItemStack var3 = var0.getInventory().getItem(var2);
         if (!var3.isEmpty() && var3.is(var1) && isHexGranted(var3)) {
            return true;
         }
      }

      return false;
   }

   // 透视：右键 20 秒矿物透视（用发光物品实体标出附近矿石）
   private static void giveXrayItem(ServerPlayer var0) {
      if (!hasNamedItem(var0, "矿物透视")) {
         var0.addItem(questItem(Items.SPYGLASS, "矿物透视", "hexbingo.item.xray.lore"));
         var0.sendSystemMessage(Component.translatable("hexbingo.msg.xray_item").withStyle(ChatFormatting.LIGHT_PURPLE));
      }
   }

   public static boolean castXray(ServerPlayer var0) {
      if (!hasHex(var0, "xray")) {
         return false;
      }

      long var1 = System.currentTimeMillis();
      Long var3 = XRAY_COOLDOWN.get(var0.getUUID());
      if (var3 != null && var1 < var3) {
         long var4 = (var3 - var1 + 999L) / 1000L;
         var0.sendSystemMessage(Component.translatable("hexbingo.msg.xray_cooldown", var4).withStyle(ChatFormatting.GRAY));
         return true;
      }

      XRAY_UNTIL.put(var0.getUUID(), var1 + 20000L);
      XRAY_COOLDOWN.put(var0.getUUID(), var1 + skillCooldown(var0, 300000L));
      var0.sendSystemMessage(Component.translatable("hexbingo.msg.xray_start").withStyle(ChatFormatting.AQUA));
      tickXray(var0);
      return true;
   }

   public static void tickXray(ServerPlayer var0) {
      Long var1 = XRAY_UNTIL.get(var0.getUUID());
      if (var1 != null) {
         long var2 = System.currentTimeMillis();
         if (var2 >= var1) {
            XRAY_UNTIL.remove(var0.getUUID());
            clearXrayMarkers(var0);
            var0.sendSystemMessage(Component.translatable("hexbingo.msg.xray_end").withStyle(ChatFormatting.GRAY));
         } else if (var0.level() instanceof ServerLevel var4) {
            clearXrayMarkers(var0);
            ArrayList<ItemEntity> var5 = new ArrayList<>();
            BlockPos var6 = var0.blockPosition();

            for (int var7 = -16; var7 <= 16 && var5.size() < 96; var7++) {
               for (int var8 = -16; var8 <= 16 && var5.size() < 96; var8++) {
                  for (int var9 = -12; var9 <= 12 && var5.size() < 96; var9++) {
                     BlockPos var10 = var6.offset(var7, var9, var8);
                     BlockState var11 = var4.getBlockState(var10);
                     if (isOreBlock(var11)) {
                        Item var12 = var11.getBlock().asItem();
                        if (var12 != Items.AIR) {
                           ItemEntity var13 = new ItemEntity(
                              var4, var10.getX() + 0.5, var10.getY() + 0.5, var10.getZ() + 0.5, new ItemStack(var12)
                           );
                           var13.setNoGravity(true);
                           var13.setNeverPickUp();
                           var13.setPickUpDelay(32767);
                           var13.setInvulnerable(true);
                           var13.setGlowingTag(true);
                           var13.setSilent(true);
                           var13.setCustomNameVisible(false);
                           var13.setDeltaMovement(Vec3.ZERO);
                           var13.addTag(XRAY_TAG);
                           var4.addFreshEntity(var13);
                           var5.add(var13);
                        }
                     }
                  }
               }
            }

            XRAY_MARKERS.put(var0.getUUID(), var5);
         }
      }
   }

   private static boolean isOreBlock(BlockState var0) {
      String var1 = BuiltInRegistries.BLOCK.getKey(var0.getBlock()).getPath();
      return var1.endsWith("_ore") || var1.equals("ancient_debris") || var1.equals("gilded_blackstone");
   }

   private static void clearXrayMarkers(ServerPlayer var0) {
      List<ItemEntity> var1 = XRAY_MARKERS.remove(var0.getUUID());
      if (var1 != null) {
         for (ItemEntity var3 : var1) {
            var3.discard();
         }
      }
   }

   // 拾荒者：破坏方块有 3% 概率额外掉落自身（在 blockDrops 中调用）
   private static void scavengerDrop(ServerPlayer var0, BlockDropsEvent var1) {
      if (ThreadLocalRandom.current().nextDouble() < 0.03) {
         Item var2 = var1.getState().getBlock().asItem();
         if (var2 != Items.AIR) {
            var1.getDrops()
               .add(
                  new ItemEntity(
                     var1.getLevel(),
                     var1.getPos().getX() + 0.5,
                     var1.getPos().getY() + 0.5,
                     var1.getPos().getZ() + 0.5,
                     new ItemStack(var2)
                  )
               );
         }
      }
   }

   // 洗牌：所有队伍卡面位置随机重排，完成/锁定状态跟随任务
   private static void cardShuffle(ServerPlayer var0) {
      MinecraftServer var1 = var0.level().getServer();
      if (var1 != null) {
         Bingo var2 = Bingo.get(var1.overworld());
         int var3 = var2.tasks().size();
         if (var3 > 1) {
            ArrayList<Task> var4 = new ArrayList<>(var2.tasks());
            int[] var5 = new int[var3];

            for (int var6 = 0; var6 < var3; var6++) {
               var5[var6] = var6;
            }

            ThreadLocalRandom var15 = ThreadLocalRandom.current();

            for (int var7 = var3 - 1; var7 > 0; var7--) {
               int var8 = var15.nextInt(var7 + 1);
               int var9 = var5[var7];
               var5[var7] = var5[var8];
               var5[var8] = var9;
            }

            ArrayList<Task> var16 = new ArrayList<>(var3);

            for (int var10 = 0; var10 < var3; var10++) {
               var16.add(var4.get(var5[var10]));
            }

            var2.setTasks(var16);

            for (Team var17 : var2.getTeams()) {
               boolean[] var18 = new boolean[var3];
               boolean[] var19 = new boolean[var3];

               for (int var11 = 0; var11 < var3; var11++) {
                  var18[var11] = var17.completed(var11);
                  var19[var11] = var17.locked(var11);
               }

               var17.resetCompleted();
               var17.resetLocked();

               for (int var12 = 0; var12 < var3; var12++) {
                  if (var18[var5[var12]]) {
                     var17.complete(var12);
                  }

                  if (var19[var5[var12]]) {
                     var17.lock(var12);
                  }
               }
            }

            var2.markDirty(true);
            var2.checkWin();
            Component var20 = Component.translatable("hexbingo.msg.card_shuffle").withStyle(ChatFormatting.LIGHT_PURPLE);

            for (ServerPlayer var21 : var1.getPlayerList().getPlayers()) {
               var21.sendSystemMessage(var20);
            }
         }
      }
   }

   // 镜像复制：打开最近队伍的海克斯列表（UI 在 HexManager 中发送）
   private static void giveMirrorItem(ServerPlayer var0) {
      if (!hasNamedItem(var0, "镜像复制")) {
         var0.addItem(questItem(Items.ECHO_SHARD, "镜像复制", "hexbingo.item.mirror.lore"));
         var0.sendSystemMessage(Component.translatable("hexbingo.msg.mirror_item").withStyle(ChatFormatting.LIGHT_PURPLE));
      }
   }

   public static boolean castMirror(ServerPlayer var0, ItemStack var1) {
      if (!hasHex(var0, "mirror_hex") || MIRROR_USED.contains(var0.getUUID())) {
         return true;
      }

      if (!HexManager.openMirror(var0)) {
         return false;
      }

      // 一次性道具：选中列表弹出即消耗，之后不再补发
      var1.shrink(1);
      MIRROR_USED.add(var0.getUUID());
      return true;
   }

   public static int killStacks(UUID var0) {
      return KILL_STACKS.getOrDefault(var0, 0);
   }

   public static void clearNewStates(MinecraftServer var0) {
      METEORS.clear();
      BLIND_COOLDOWN.clear();
      MIDAS_COOLDOWN.clear();
      BAT_UNTIL.clear();
      BAT_COOLDOWN.clear();
      BAT_DISGUISES.clear();
      MIND_CONTROL.clear();
      MIND_COOLDOWN.clear();
      OBJECTIVE_DENY = null;
      DENY_COOLDOWN.clear();
      XRAY_UNTIL.clear();
      XRAY_COOLDOWN.clear();

      for (ServerPlayer var2 : var0.getPlayerList().getPlayers()) {
         endEndExile(var2, false);
         clearXrayMarkers(var2);
      }

      END_EXILE.clear();
      XRAY_MARKERS.clear();
      TASK_SHIELDS.clear();

      for (TamableAnimal var3 : PETS.values()) {
         var3.discard();
      }

      PETS.clear();
      PET_TIMER.clear();
      PET_DISMISSED.clear();
      QUAKES.clear();
      BLANK_USED.clear();
      ADVANCES.clear();

      for (EnderDragon var5 : DRAGON_PETS.values()) {
         var5.discard();
      }

      DRAGON_PETS.clear();
      DRAGON_ANGLES.clear();
      DRAGON_SOUND_TIMER.clear();

      for (HexEffects.EggIncubation var6 : new ArrayList<>(INCUBATIONS)) {
         cancelIncubation(var6.owner, true);
      }

      INCUBATIONS.clear();

      // 清理历史上遗留在世界里的精怪魔法伪装
      for (ServerLevel var2 : var0.getAllLevels()) {
         for (Entity var4 : var2.getAllEntities()) {
            if (var4.getCustomName() != null
               && (var4.getCustomName().getString().equals("精怪魔法")
                  || var4.getCustomName().getString().equals("蝙蝠形态")
                  || var4.getCustomName().getString().equals("幼龙"))) {
               var4.discard();
            }
         }
      }
   }

   private HexEffects() {
   }

   private static final class MeteorState {
      private final ServerLevel level;
      private final double x;
      private double y;
      private final double z;
      private final int targetY;
      private final int radius;
      private final UUID owner;

      private MeteorState(ServerLevel var0, double var1, double var2, double var3, int var4, int var5, UUID var6) {
         this.level = var0;
         this.x = var1;
         this.y = var2;
         this.z = var3;
         this.targetY = var4;
         this.radius = var5;
         this.owner = var6;
      }
   }

   private static final class QuakeState {
      private final ServerLevel level;
      private final List<BlockPos> blocks;
      private int index;

      private QuakeState(ServerLevel var0, List<BlockPos> var1) {
         this.level = var0;
         this.blocks = var1;
      }
   }

   private static final class AdvanceState {
      private final Team team;
      private final int slot;
      private final long until;
      private boolean genuine;

      private AdvanceState(Team var0, int var1, long var2) {
         this.team = var0;
         this.slot = var1;
         this.until = var2;
      }
   }

   private static final class EggIncubation {
      private final UUID owner;
      private final ServerLevel level;
      private BlockPos pos;
      private long readyAt;
      private Display.TextDisplay display;
      private int lastSecond = -1;

      private EggIncubation(UUID var0, ServerLevel var1, BlockPos var2, long var3, Display.TextDisplay var5) {
         this.owner = var0;
         this.level = var1;
         this.pos = var2;
         this.readyAt = var3;
         this.display = var5;
      }
   }

   private record ExileState(
      ResourceKey<Level> dimension,
      double x,
      double y,
      double z,
      float yaw,
      float pitch,
      long until,
      double endX,
      double endY,
      double endZ
   ) {
   }

   private record MindState(UUID target, long until) {
   }

   private record DenyState(int taskIndex, long until) {
   }

   private static final class BanishState {
      private final ResourceKey<Level> dimension;
      private final double x;
      private final double y;
      private final double z;
      private final float yaw;
      private final float pitch;
      private final long until;

      private BanishState(ResourceKey<Level> var0, double var1, double var2, double var3, float var4, float var5, long var6) {
         this.dimension = var0;
         this.x = var1;
         this.y = var2;
         this.z = var3;
         this.yaw = var4;
         this.pitch = var5;
         this.until = var6;
      }
   }

   private static final class BoomerangState {
      private final ItemEntity entity;
      private final Vec3 direction;
      private int ticks;

      private BoomerangState(ItemEntity var1, Vec3 var2, int var3) {
         this.entity = var1;
         this.direction = var2;
         this.ticks = var3;
      }
   }

   private static final class DashState {
      private final long endsAt;
      private final double y;
      private double dirX;
      private double dirZ;
      private double moved;
      private int blocks;
      private int mobs;
      private final Set<UUID> hit = new HashSet<>();

      private DashState(long var1, double var3, double var5, double var7) {
         this.endsAt = var1;
         this.y = var3;
         this.dirX = var5;
         this.dirZ = var7;
      }
   }

   private static final class HomeguardState {
      private int runTicks;
      private int idleTicks;
      private int amplifier;
      private double lastX;
      private double lastZ;
      private boolean initialized;
   }

   private record Temp(float value, long until) {
      private boolean active() {
         return System.currentTimeMillis() < this.until;
      }
   }

   private static final class TransformState {
      private final List<ItemStack> snapshot;
      private final long until;

      private TransformState(List<ItemStack> var1, long var2) {
         this.snapshot = var1;
         this.until = var2;
      }
   }

   private static final class SoulState {
      private final ServerLevel level;
      private final double x;
      private final double y;
      private final double z;
      private final float yaw;
      private final float pitch;
      private final GameType mode;
      private final long until;

      private SoulState(ServerLevel var1, double var2, double var4, double var6, float var8, float var9, GameType var10, long var11) {
         this.level = var1;
         this.x = var2;
         this.y = var4;
         this.z = var6;
         this.yaw = var8;
         this.pitch = var9;
         this.mode = var10;
         this.until = var11;
      }
   }

   private static final class TrapRoom {
      private final UUID firstId;
      private final UUID secondId;
      private final ServerLevel firstLevel;
      private final ServerLevel secondLevel;
      private final Vec3 firstHome;
      private final Vec3 secondHome;
      private final float firstYaw;
      private final float firstPitch;
      private final float secondYaw;
      private final float secondPitch;
      private final ServerLevel roomLevel;
      private final Map<BlockPos, BlockState> backup = new HashMap<>();
      private final int centerX;
      private final int centerZ;
      private final int floorY;
      private int ticks;
      private boolean pvpOccurred;
      private boolean pvpWasAllowed = true;
      private boolean pvpRestored;
      private UUID loserId;
      private UUID releasedId;
      private int penaltyTicks;
      private boolean penaltyStarted;

      private TrapRoom(ServerPlayer var1, ServerPlayer var2, ServerLevel var3, int var4, int var5, int var6) {
         this.firstId = var1.getUUID();
         this.secondId = var2.getUUID();
         this.firstLevel = var1.level();
         this.secondLevel = var2.level();
         this.firstHome = var1.position();
         this.secondHome = var2.position();
         this.firstYaw = var1.getYRot();
         this.firstPitch = var1.getXRot();
         this.secondYaw = var2.getYRot();
         this.secondPitch = var2.getXRot();
         this.roomLevel = var3;
         this.centerX = var4;
         this.centerZ = var5;
         this.floorY = var6;
      }

      private boolean involves(UUID var1) {
         return this.firstId.equals(var1) || this.secondId.equals(var1);
      }
   }
}
