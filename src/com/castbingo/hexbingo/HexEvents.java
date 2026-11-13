/*
 * Copyright (c) 2026 Big_Jls and DeepSeek-v4.1-Flash-1017.
 * Licensed under the Apache License, Version 2.0.
 * See LICENSE and NOTICE.
 */
package com.castbingo.hexbingo;

import io.github.noeppi_noeppi.mods.bongo.Bingo;
import io.github.noeppi_noeppi.mods.bongo.data.Team;
import io.github.noeppi_noeppi.mods.bongo.event.BingoTaskEvent;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.bee.Bee;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.projectile.EyeOfEnder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gamerules.GameRules;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.CommandEvent;
import net.neoforged.neoforge.event.ItemStackedOnOtherEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.EntityTeleportEvent;
import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent.Post;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent.Pre;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent.Finish;
import net.neoforged.neoforge.event.entity.player.PlayerEvent.ItemCraftedEvent;
import net.neoforged.neoforge.event.entity.player.PlayerContainerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerRespawnEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent.LeftClickBlock;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public class HexEvents {
   @SubscribeEvent
   public void command(CommandEvent var1) {
      HexEffects.onCommand(var1);
   }

   @SubscribeEvent
   public void itemToss(ItemTossEvent var1) {
      HexEffects.onItemToss(var1);
   }

   @SubscribeEvent
   public void itemStackedOnOther(ItemStackedOnOtherEvent var1) {
      HexEffects.onItemStackedOnOther(var1);
   }

   @SubscribeEvent
   public void containerClose(PlayerContainerEvent.Close var1) {
      if (var1.getEntity() instanceof ServerPlayer var2) {
         HexEffects.extractBoundItems(var2, var1.getContainer(), false);
      }
   }

   @SubscribeEvent
   public void incomingDamage(LivingIncomingDamageEvent var1) {
      HexEffects.incomingDamage(var1);
   }

   @SubscribeEvent(priority = EventPriority.HIGHEST)
   public void dragonPetDamage(LivingIncomingDamageEvent var1) {
      HexEffects.dragonPetDamage(var1);
   }

   @SubscribeEvent
   public void damagePre(Pre var1) {
      HexEffects.damageDealt(var1);
   }

   @SubscribeEvent
   public void projectileDamage(LivingIncomingDamageEvent var1) {
      HexEffects.snowballDamage(var1);
   }

   @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
   public void beeDamage(LivingIncomingDamageEvent var1) {
      if (var1.isCanceled()
         && var1.getEntity() instanceof ServerPlayer
         && var1.getSource().getDirectEntity() instanceof Bee var3
         && HexEffects.isHexBee(var3)) {
         var1.setCanceled(false);
      }
   }

   @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
   public void voyageDamage(LivingIncomingDamageEvent var1) {
      if (var1.isCanceled()
         && var1.getEntity() instanceof ServerPlayer var2
         && HexEffects.isVoyageActive(var2)
         && var1.getSource().is(DamageTypeTags.IS_FALL)) {
         var1.setCanceled(false);
      }
   }

   @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
   public void trapDamage(LivingIncomingDamageEvent var1) {
      HexEffects.trapIncomingDamage(var1);
   }

   @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
   public void demonPactDamage(LivingIncomingDamageEvent var1) {
      if (var1.getEntity() instanceof ServerPlayer var2 && HexEffects.isDemonPact(var2)) {
         var1.setCanceled(false);
      }
   }

   @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
   public void totemDamage(LivingIncomingDamageEvent var1) {
      if (var1.isCanceled()
         && var1.getEntity() instanceof ServerPlayer var2
         && HexEffects.isHoldingTotemBulk(var2)) {
         var1.setCanceled(false);
      }
   }

   @SubscribeEvent
   public void beePoison(Post var1) {
      if (var1.getEntity() instanceof ServerPlayer var2
         && var1.getSource().getDirectEntity() instanceof Bee var3
         && HexEffects.isHexBee(var3)) {
         var2.addEffect(new MobEffectInstance(MobEffects.POISON, 200, 0, false, true, true));
      }
   }

   @SubscribeEvent(priority = EventPriority.LOWEST)
   public void playerTick(PlayerTickEvent.Post var1) {
      if (var1.getEntity() instanceof ServerPlayer var2) {
         HexEffects.feastHunger(var2);
         HexEffects.tickSoulOut(var2);
         HexEffects.tickSpiderMan(var2);
         HexEffects.tickMagnet(var2);
         HexEffects.tickDice(var2);
         HexEffects.tickMindControl(var2);
         if (var2.tickCount % 10 == 0) {
            HexEffects.tickBoundItems(var2);
         }
         if (var2.tickCount % 20 == 0) {
            HexEffects.checkEnchantedItemTasks(var2);
            if (var2.tickCount % 200 == 0) {
               HexEffects.boneMealSpray(var2);
            }
         }
      }
   }

   @SubscribeEvent
   public void equipmentChange(LivingEquipmentChangeEvent var1) {
      if (var1.getEntity() instanceof ServerPlayer var2 && var1.getSlot() == EquipmentSlot.HEAD && !var1.getTo().is(Items.CARVED_PUMPKIN)) {
         HexEffects.enforcePumpkin(var2);
      }
   }

   @SubscribeEvent
   public void damagePost(Post var1) {
      HexEffects.damagePost(var1);
   }

   @SubscribeEvent
   public void death(LivingDeathEvent var1) {
      if (var1.getEntity() instanceof ServerPlayer var2) {
         HexEffects.onFaeDeath(var2);
         HexEffects.onVoyageDeath(var2);
         HexEffects.onTrapDeath(var2);
         HexEffects.clearLockedSlotMarkers(var2);
         HexEffects.restoreTransform(var2);
         HexEffects.onSpecialDeath(var2);
      }

      HexEffects.death(var1);
   }

   /** 在其它监听器（含 Bingo 的死亡锁格）处理完后再撤销星际航行失败带来的锁格。 */
   @SubscribeEvent(priority = EventPriority.LOWEST)
   public void voyageDeathLock(LivingDeathEvent var1) {
      if (var1.getEntity() instanceof ServerPlayer var2) {
         HexEffects.restoreVoyageDeathLock(var2);
      }
   }

   @SubscribeEvent
   public void blockDrops(BlockDropsEvent var1) {
      HexEffects.blockDrops(var1);
   }

   @SubscribeEvent
   public void livingDrops(LivingDropsEvent var1) {
      HexEffects.livingDrops(var1);
   }

   @SubscribeEvent
   public void crafted(ItemCraftedEvent var1) {
      HexEffects.crafted(var1);
   }

   @SubscribeEvent
   public void useItemFinish(Finish var1) {
      if (var1.getEntity() instanceof ServerPlayer var2 && var1.getItem().has(DataComponents.FOOD)) {
         HexEffects.feastFoodBuff(var2);
      }
   }

   @SubscribeEvent
   public void rightClickBlock(RightClickBlock var1) {
      if (var1.getEntity() instanceof ServerPlayer var0 && useAbilityItem(var0, var1.getItemStack())) {
         var1.setCanceled(true);
         return;
      }

      if (var1.getEntity() instanceof ServerPlayer var2 && isHookItem(var1.getItemStack()) && HexEffects.hasHex(var2, "hook_dizzy")) {
         if (HexEffects.grapple(var2)) {
            var1.setCanceled(true);
         }
      } else if (var1.getEntity() instanceof ServerPlayer var3
         && isTrapItem(var1.getItemStack())
         && HexEffects.hasHex(var3, "rainbow_duel")
         && HexEffects.startTrap(var3)) {
         var1.setCanceled(true);
      } else if (var1.getEntity() instanceof ServerPlayer var4
         && isFaeItem(var1.getItemStack())
         && HexEffects.hasHex(var4, "fae_magic")
         && HexEffects.castFae(var4)) {
         var1.setCanceled(true);
      } else if (var1.getEntity() instanceof ServerPlayer var5
         && isJamItem(var1.getItemStack())
         && HexEffects.hasHex(var5, "jam_bp")
         && HexEffects.castJam(var5)) {
         var1.setCanceled(true);
      } else if (var1.getEntity() instanceof ServerPlayer var20
         && isUntouchableItem(var1.getItemStack())
         && HexEffects.hasHex(var20, "untouchable")
         && HexEffects.castUntouchable(var20)) {
         var1.setCanceled(true);
      } else if (var1.getEntity() instanceof ServerPlayer var21
         && isChaosMindItem(var1.getItemStack())
         && HexEffects.hasHex(var21, "inventory_shuffle")
         && HexEffects.castChaosMind(var21)) {
         var1.setCanceled(true);
      } else if (var1.getEntity() instanceof ServerPlayer var18
         && isBanishItem(var1.getItemStack())
         && HexEffects.hasHex(var18, "nether_banish")
         && HexEffects.castBanish(var18)) {
         var1.setCanceled(true);
      } else if (var1.getEntity() instanceof ServerPlayer var17
         && isPortableBench(var1.getItemStack())
         && HexEffects.openPortableCrafting(var17)) {
         var1.setCanceled(true);
      } else if (var1.getEntity() instanceof ServerPlayer var22
         && isPortableFurnace(var1.getItemStack())
         && HexEffects.openPortableFurnace(var22)) {
         var1.setCanceled(true);
      } else if (var1.getEntity() instanceof ServerPlayer var11
         && var1.getLevel().getBlockState(var1.getPos()).is(Blocks.CRAFTING_TABLE)
         && HexEffects.isCraftingBlocked(var11)) {
         var1.setCanceled(true);
      } else if (var1.getEntity() instanceof ServerPlayer var13
         && isDashItem(var1.getItemStack())
         && HexEffects.hasHex(var13, "dash_charge")
         && HexEffects.dashCharge(var13)) {
         var1.setCanceled(true);
      }
   }

   @SubscribeEvent
   public void rightClickItem(RightClickItem var1) {
      if (var1.getEntity() instanceof ServerPlayer var0 && useAbilityItem(var0, var1.getItemStack())) {
         var1.setCanceled(true);
         return;
      }

      if (var1.getEntity() instanceof ServerPlayer var2 && var1.getItemStack().is(Items.ENDER_EYE) && HexEffects.hasHex(var2, "eye_taxi")) {
         HexEffects.markEye(var2);
      }

      if (var1.getEntity() instanceof ServerPlayer var9 && isHookItem(var1.getItemStack()) && HexEffects.hasHex(var9, "hook_dizzy")) {
         if (HexEffects.grapple(var9)) {
            var1.setCanceled(true);
         }
      } else if (var1.getEntity() instanceof ServerPlayer var10
         && isTrapItem(var1.getItemStack())
         && HexEffects.hasHex(var10, "rainbow_duel")
         && HexEffects.startTrap(var10)) {
         var1.setCanceled(true);
      } else if (var1.getEntity() instanceof ServerPlayer var4
         && isFaeItem(var1.getItemStack())
         && HexEffects.hasHex(var4, "fae_magic")
         && HexEffects.castFae(var4)) {
         var1.setCanceled(true);
      } else if (var1.getEntity() instanceof ServerPlayer var5
         && isJamItem(var1.getItemStack())
         && HexEffects.hasHex(var5, "jam_bp")
         && HexEffects.castJam(var5)) {
         var1.setCanceled(true);
      } else if (var1.getEntity() instanceof ServerPlayer var20
         && isUntouchableItem(var1.getItemStack())
         && HexEffects.hasHex(var20, "untouchable")
         && HexEffects.castUntouchable(var20)) {
         var1.setCanceled(true);
      } else if (var1.getEntity() instanceof ServerPlayer var21
         && isChaosMindItem(var1.getItemStack())
         && HexEffects.hasHex(var21, "inventory_shuffle")
         && HexEffects.castChaosMind(var21)) {
         var1.setCanceled(true);
      } else if (var1.getEntity() instanceof ServerPlayer var18
         && isBanishItem(var1.getItemStack())
         && HexEffects.hasHex(var18, "nether_banish")
         && HexEffects.castBanish(var18)) {
         var1.setCanceled(true);
      } else if (var1.getEntity() instanceof ServerPlayer var15
         && var1.getItemStack().is(Items.SHEARS)
         && var15.isShiftKeyDown()
         && HexEffects.removePumpkinSelf(var15)) {
         var1.setCanceled(true);
      } else if (var1.getEntity() instanceof ServerPlayer var6
         && var1.getItemStack().is(Items.NAUTILUS_SHELL)
         && var1.getItemStack().getHoverName().getString().equals("ok回力镖")
         && HexEffects.hasHex(var6, "boomerang_biome")
         && HexEffects.launchBoomerang(var6)) {
         var1.setCanceled(true);
      } else if (var1.getEntity() instanceof ServerPlayer var16
         && isDashItem(var1.getItemStack())
         && HexEffects.hasHex(var16, "dash_charge")
         && HexEffects.dashCharge(var16)) {
         var1.setCanceled(true);
      } else if (var1.getEntity() instanceof ServerPlayer var19
         && isWorldItem(var1.getItemStack())
         && HexEffects.hasHex(var19, "the_world")
         && HexEffects.castTheWorld(var19)) {
         var1.setCanceled(true);
      } else if (var1.getEntity() instanceof ServerPlayer var17
         && isPortableBench(var1.getItemStack())
         && HexEffects.openPortableCrafting(var17)) {
         var1.setCanceled(true);
      } else if (var1.getEntity() instanceof ServerPlayer var23
         && isPortableFurnace(var1.getItemStack())
         && HexEffects.openPortableFurnace(var23)) {
         var1.setCanceled(true);
      }
   }

   private static boolean useAbilityItem(ServerPlayer var0, ItemStack var1) {
      String var2 = var1.getHoverName().getString();
      if (var1.is(Items.INK_SAC) && var2.equals("致盲")) {
         return HexEffects.castBlind(var0);
      } else if (var1.is(Items.PHANTOM_MEMBRANE) && var2.equals("蝙蝠形态")) {
         return HexEffects.castBatForm(var0);
      } else if (var1.is(Items.PRISMARINE_CRYSTALS) && var2.equals("精神控制")) {
         return HexEffects.castMindControl(var0);
      } else if (var1.is(Items.BARRIER) && var2.equals("目标封锁")) {
         HexManager.openTaskPick(var0, "deny");
         return true;
      } else if (var1.is(Items.PAPER) && var2.equals("空白格")) {
         HexManager.openTaskPick(var0, "blank");
         return true;
      } else if (var1.is(Items.BARRIER) && var2.equals("封印")) {
         HexManager.openTaskPick(var0, "seal");
         return true;
      } else if (var1.is(Items.SPYGLASS) && var2.equals("矿物透视")) {
         return HexEffects.castXray(var0);
      } else if (var1.is(Items.SLIME_BALL) && var2.equals("缩小术")) {
         return HexEffects.castShrink(var0);
      } else if (var1.is(Items.BUNDLE) && var2.equals("背包小偷")) {
         return HexEffects.castBackpackThief(var0);
      } else if (var1.is(Items.NETHER_STAR) && var2.equals("潘是对的")) {
         return HexEffects.castPanIsRight(var0, var1);
      } else if (var1.is(Items.ECHO_SHARD) && var2.equals("镜像复制")) {
         return HexEffects.castMirror(var0, var1);
      } else {
         return false;
      }
   }

   private static boolean isHookItem(ItemStack var0) {
      return var0.is(Items.FISHING_ROD) && var0.getHoverName().getString().equals("转到我眩晕了");
   }

   private static boolean isTrapItem(ItemStack var0) {
      return var0.is(Items.IRON_CHAIN) && var0.getHoverName().getString().equals("和我一起困在这里");
   }

   private static boolean isFaeItem(ItemStack var0) {
      return var0.is(Items.AMETHYST_SHARD) && var0.getHoverName().getString().equals("精怪魔法");
   }

   private static boolean isJamItem(ItemStack var0) {
      return var0.is(Items.RECOVERY_COMPASS) && var0.getHoverName().getString().equals("对方不在服务区");
   }

   private static boolean isBanishItem(ItemStack var0) {
      return var0.is(Items.END_CRYSTAL) && var0.getHoverName().getString().equals("维度放逐");
   }

   private static boolean isDashItem(ItemStack var0) {
      return var0.is(Items.GOAT_HORN) && var0.getHoverName().getString().equals("冲冲冲");
   }

   private static boolean isUntouchableItem(ItemStack var0) {
      return var0.is(Items.BARRIER) && var0.getHoverName().getString().equals("你摸不到");
   }

   private static boolean isChaosMindItem(ItemStack var0) {
      return var0.is(Items.BARRIER) && var0.getHoverName().getString().equals("混沌智");
   }

   private static boolean isPortableBench(ItemStack var0) {
      return var0.is(Items.CRAFTING_TABLE) && var0.getHoverName().getString().equals("随身工作台");
   }

   private static boolean isPortableFurnace(ItemStack var0) {
      return var0.is(Items.FURNACE) && var0.getHoverName().getString().equals("便携熔炉");
   }

   private static boolean isWorldItem(ItemStack var0) {
      return var0.is(Items.CLOCK) && var0.getHoverName().getString().equals("The World");
   }

   @SubscribeEvent
   public void entityInteract(EntityInteract var1) {
      if (var1.getEntity() instanceof ServerPlayer var2) {
         if (var1.getTarget() instanceof Frog var7
            && var7.getCustomName() != null
            && var7.getCustomName().getString().equals("旅行青蛙")
            && HexEffects.hasHex(var2, "travel_frog")) {
            HexEffects.tradeFrog(var2, var7);
            var1.setCanceled(true);
         } else if (var1.getTarget() instanceof ServerPlayer var4 && var1.getItemStack().is(Items.SHEARS)) {
            Bingo var9 = Bingo.get(var2.level());
            Team var6 = var9.getTeam(var2);
            if (var6 != null && var6.hasPlayer(var4) && HexEffects.removePumpkin(var2, var4)) {
               var1.setCanceled(true);
            }
         } else if (var1.getTarget() instanceof Villager var10 && !var2.isShiftKeyDown()) {
            HexEffects.useBlackMarket(var2, var10);
         } else if ((var1.getTarget() instanceof ItemFrame || var1.getTarget() instanceof ArmorStand)
            && HexEffects.isBoundItem(var1.getItemStack())) {
            HexEffects.sendBoundNoStore(var2);
            var1.setCanceled(true);
         }
      }
   }

   @SubscribeEvent
   public void entityJoin(EntityJoinLevelEvent var1) {
      if (var1.getLevel() instanceof ServerLevel var2 && var1.getEntity() instanceof EyeOfEnder var3) {
         for (ServerPlayer var5 : var2.players()) {
            if (var5.distanceToSqr(var3) < 256.0 && HexEffects.hasEyeMark(var5)) {
               HexEffects.makeEyeSurvive(var3);
               break;
            }
         }
      }
   }

   @SubscribeEvent
   public void entityTick(net.neoforged.neoforge.event.tick.EntityTickEvent.Post var1) {
      HexEffects.tickDragonPet(var1.getEntity());
   }

   @SubscribeEvent
   public void serverTick(net.neoforged.neoforge.event.tick.ServerTickEvent.Post var1) {
      HexEffects.tickDash(var1.getServer());
      HexEffects.tickBatForms(var1.getServer());
      HexEffects.tickPortableFurnaces(var1.getServer());
      HexEffects.tickSmeltBoost(var1.getServer());
      HexEffects.tickTrap(var1.getServer());
      HexEffects.tickFaeDisguises(var1.getServer());
      HexEffects.tickHomeguard(var1.getServer());
      HexEffects.tickMeteors(var1.getServer());
      HexEffects.tickQuakes(var1.getServer());
      HexEffects.tickAdvances(var1.getServer());
      HexEffects.tickDragons(var1.getServer());
      HexEffects.tickIncubations(var1.getServer());
      HexEffects.tickShrinks(var1.getServer());
      HexEffects.tickBlackstone(var1.getServer());
   }

   @SubscribeEvent
   public void dragonEggPlaced(BlockEvent.EntityPlaceEvent var1) {
      if (var1.getPlacedBlock().is(Blocks.DRAGON_EGG) && var1.getEntity() instanceof ServerPlayer var2) {
         HexEffects.startDragonIncubation(var2, var1.getPos());
      }
   }

   @SubscribeEvent
   public void teleport(EntityTeleportEvent var1) {
      // 末影珍珠、紫颂果属于玩家自身位移手段，不限制
      if (var1 instanceof EntityTeleportEvent.EnderPearl || var1 instanceof EntityTeleportEvent.ItemConsumption) {
         return;
      }

      if (HexEffects.isExileBypassing()) {
         return;
      }

      if (var1.getEntity() instanceof ServerPlayer var2 && HexEffects.isEndExiled(var2)) {
         var1.setCanceled(true);
         var2.sendSystemMessage(net.minecraft.network.chat.Component.translatable("hexbingo.msg.end_exile_no_tp").withStyle(net.minecraft.ChatFormatting.DARK_PURPLE));
         return;
      }

      // 放逐期间，其他玩家也不能传送到被放逐玩家的位置
      if (var1.getTargetLevel() != null) {
         ServerPlayer var3 = HexEffects.findEndExiledNear(var1.getTargetLevel(), var1.getTargetX(), var1.getTargetY(), var1.getTargetZ(), 4.0);
         if (var3 != null) {
            var1.setCanceled(true);
            if (var1.getEntity() instanceof ServerPlayer var4) {
               var4.sendSystemMessage(
                  net.minecraft.network.chat.Component.translatable("hexbingo.msg.end_exile_no_tp").withStyle(net.minecraft.ChatFormatting.DARK_PURPLE)
               );
            }
         }
      }
   }

   @SubscribeEvent
   public void playerLoggedOut(PlayerLoggedOutEvent var1) {
      if (var1.getEntity() instanceof ServerPlayer var2) {
         HexEffects.onFaeLogout(var2);
         HexEffects.onBatLogout(var2);
         HexEffects.onTrapLogout(var2);
         HexEffects.onSoulLogout(var2);
         HexEffects.onShrinkLogout(var2);
         HexManager.onLogout(var2);
      }
   }

   @SubscribeEvent
   public void playerRespawn(PlayerRespawnEvent var1) {
      if (var1.getEntity() instanceof ServerPlayer var2) {
         HexEffects.onTrapRespawn(var2);
      }
   }

   // 缩小术：被缩小的玩家起跳初速度减半，跳跃高度约为原来的 25%
   @SubscribeEvent
   public void livingJump(net.neoforged.neoforge.event.entity.living.LivingEvent.LivingJumpEvent var1) {
      if (var1.getEntity() instanceof ServerPlayer var2 && HexEffects.isShrunk(var2)) {
         var2.setDeltaMovement(var2.getDeltaMovement().multiply(1.0, 0.5, 1.0));
      }
   }

   @SubscribeEvent
   public void serverStarted(ServerStartedEvent var1) {
      MinecraftServer var2 = var1.getServer();

      for (ServerLevel var4 : var2.getAllLevels()) {
         GameRules var5 = var4.getGameRules();
         if (!(Boolean)var5.get(GameRules.KEEP_INVENTORY)) {
            var5.set(GameRules.KEEP_INVENTORY, true, var2);
         }
      }
   }

   @SubscribeEvent
   public void serverStopping(ServerStoppingEvent var1) {
      HexEffects.abortTrap(var1.getServer());
      HexEffects.abortSouls(var1.getServer());
   }

   @SubscribeEvent
   public void leftClickBlock(LeftClickBlock var1) {
      if (var1.getEntity() instanceof ServerPlayer var2 && HexEffects.isBreakLocked(var2)) {
         var1.setCanceled(true);
      }
   }

   @SubscribeEvent
   public void taskCompleted(BingoTaskEvent var1) {
      MinecraftServer var2 = var1.getLevel().getServer();
      if (var2 != null && HexGameData.get(var2).mayhem()) {
         if (HexEffects.handleHexItemAdvancement(var1)) {
            return;
         }

         if (HexEffects.handleSeal(var1)) {
            return;
         }

         if (HexEffects.handleObjectiveDeny(var1)) {
            return;
         }

         HexManager.onTask(var1);
         HexEffects.onTask(var1);
      }
   }
}
