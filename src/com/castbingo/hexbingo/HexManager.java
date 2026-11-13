/*
 * Copyright (c) 2026 Big_Jls and DeepSeek-v4.1-Flash-1017.
 * Licensed under the Apache License, Version 2.0.
 * See LICENSE and NOTICE.
 */
package com.castbingo.hexbingo;

import com.castbingo.hexbingo.network.HexListPayload;
import com.castbingo.hexbingo.network.HexMirrorPayload;
import com.castbingo.hexbingo.network.HexOfferPayload;
import com.castbingo.hexbingo.network.HexRosterPayload;
import com.castbingo.hexbingo.network.HexTaskListPayload;
import io.github.noeppi_noeppi.mods.bongo.Bingo;
import io.github.noeppi_noeppi.mods.bongo.data.Team;
import io.github.noeppi_noeppi.mods.bongo.data.TeamCompletion;
import io.github.noeppi_noeppi.mods.bongo.data.WinCondition;
import io.github.noeppi_noeppi.mods.bongo.event.BingoTaskEvent;
import io.github.noeppi_noeppi.mods.bongo.task.Task;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.CommandEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerRespawnEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent.Post;
import net.neoforged.neoforge.network.PacketDistributor;

public final class HexManager {
   private static final Set<UUID> FREE_PICK = new HashSet<>();
   private static final Set<UUID> BREAK_LOCK_SEEN = new HashSet<>();
   private static final Set<UUID> MIRROR_OPEN = new HashSet<>();
   private static final Map<UUID, String> TASK_PICK_MODE = new HashMap<>();

   /** 玩家下线时清理选择/镜像/任务选择模式，防止重连后仍持有资格。 */
   public static void onLogout(ServerPlayer var0) {
      UUID var1 = var0.getUUID();
      FREE_PICK.remove(var1);
      MIRROR_OPEN.remove(var1);
      TASK_PICK_MODE.remove(var1);
      BREAK_LOCK_SEEN.remove(var1);
   }

   private static void updateBreakLock(MinecraftServer var0) {
      for (ServerPlayer var2 : var0.getPlayerList().getPlayers()) {
         long var3 = HexEffects.breakLockRemainingMs(var2);
         if (var3 > 0L) {
            int var5 = (int)((var3 + 999L) / 1000L);
            var2.sendOverlayMessage(Component.translatable("hexbingo.msg.break_locked_tick", new Object[]{var5}).withStyle(ChatFormatting.RED));
            BREAK_LOCK_SEEN.add(var2.getUUID());
         } else if (BREAK_LOCK_SEEN.remove(var2.getUUID())) {
            var2.sendSystemMessage(Component.translatable("hexbingo.msg.break_unlocked").withStyle(ChatFormatting.GRAY));
         }
      }
   }

   public static void onCommand(CommandEvent var0) {
      String var1 = var0.getParseResults().getReader().getString().trim();

      while (var1.startsWith("/")) {
         var1 = var1.substring(1).trim();
      }

      String[] var2 = var1.split("\\s+");
      if (var2.length >= 1) {
         CommandSourceStack var3 = (CommandSourceStack)var0.getParseResults().getContext().getSource();
         if (var3.getEntity() instanceof ServerPlayer var4) {
            String var15 = var2[0].toLowerCase(Locale.ROOT);
            boolean var6 = var15.equals("tp") || var15.equals("bp");
            if (var15.equals("bingo") && var2.length >= 2) {
               String var7 = var2[1].toLowerCase(Locale.ROOT);
               var6 = var7.equals("tp") || var7.equals("teleport") || var7.equals("backpack");
            }

            if (var6 && HexEffects.isNetherLocked(var4)) {
               var3.sendFailure(Component.translatable("hexbingo.msg.nether_locked"));
               var0.setCanceled(true);
               return;
            }

            boolean var19 = var15.equals("tp")
               || var15.equals("teleport")
               || var15.equals("spreadplayers")
               || isBingoTp(var2);
            if (var19) {
               if (HexEffects.isEndExiled(var4)) {
                  var3.sendFailure(Component.translatable("hexbingo.msg.end_exile_no_tp"));
                  var0.setCanceled(true);
                  return;
               }

               MinecraftServer var20 = var3.getServer();
               if (var20 != null) {
                  // /bingo tp <target>：ReBongo 的参数是 EntitySelector（不是 EntityArgument），
                  // 直接按解析结果取目标玩家，避免用 EntityArgument.getPlayer 取错类型而静默漏拦
                  if (isBingoTp(var2)) {
                     try {
                        net.minecraft.commands.arguments.selector.EntitySelector var21 = var0.getParseResults()
                           .getContext()
                           .build(var0.getParseResults().getReader().getString())
                           .getArgument("target", net.minecraft.commands.arguments.selector.EntitySelector.class);
                        ServerPlayer var22 = var21.findSinglePlayer(var3);
                        if (HexEffects.isEndExiled(var22)) {
                           var3.sendFailure(Component.translatable("hexbingo.msg.end_exile_no_tp"));
                           var0.setCanceled(true);
                           return;
                        }
                     } catch (Exception var23) {
                     }
                  }

                  // /tp、/teleport：参数中出现的被放逐玩家同样拦截
                  for (int var24 = 1; var24 < var2.length; var24++) {
                     ServerPlayer var25 = var20.getPlayerList().getPlayerByName(var2[var24]);
                     if (var25 != null && HexEffects.isEndExiled(var25)) {
                        var3.sendFailure(Component.translatable("hexbingo.msg.end_exile_no_tp"));
                        var0.setCanceled(true);
                        return;
                     }
                  }
               }
            }

            boolean var18 = var15.equals("bp")
               || var15.equals("bingo")
                  && var2.length >= 2
                  && (var2[1].equalsIgnoreCase("backpack") || var2[1].equalsIgnoreCase("bp"));
            if (var18 && HexEffects.isBpJammed(var4)) {
               HexBingo.LOGGER.info("Blocked '/{}' for {} (BP jam active)", var1, var4.getScoreboardName());
               var3.sendFailure(Component.translatable("hexbingo.msg.bp_jammed"));
               var0.setCanceled(true);
               return;
            }
         }
      }

      if (var2.length >= 2 && var2[0].equalsIgnoreCase("bingo")) {
         MinecraftServer var13 = ((CommandSourceStack)var0.getParseResults().getContext().getSource()).getServer();
         if (var13 != null) {
            String var14 = var2[1].toLowerCase(Locale.ROOT);
            if (var14.equals("create")) {
               if (var2.length < 3) {
                  return;
               }

               Bingo var16 = Bingo.get(var13.overworld());
               if (var16.running()) {
                  return;
               }

               String var17 = var2[2].toLowerCase(Locale.ROOT);
               boolean var19 = var17.equals("bongo:hard");

               for (int var8 = 3; var8 < var2.length; var8++) {
                  for (String var12 : var2[var8].split(",")) {
                     if (var12.equalsIgnoreCase("bongo:mayhem")) {
                        var19 = true;
                     }
                  }
               }

               resetGame(var13, var19);
            } else if (var14.equals("stop")) {
               endGame(var13);
            }
         }
      }
   }

   private static boolean isBingoTp(String[] var0) {
      for (int var1 = 0; var1 + 1 < var0.length; var1++) {
         if (var0[var1].equalsIgnoreCase("bingo")
            && (var0[var1 + 1].equalsIgnoreCase("tp") || var0[var1 + 1].equalsIgnoreCase("teleport"))) {
            return true;
         }
      }

      return false;
   }

   private static void resetGame(MinecraftServer var0, boolean var1) {
      HexGameData var2 = HexGameData.get(var0);
      FREE_PICK.clear();
      TASK_PICK_MODE.clear();
      HexEffects.clearJams();
      HexEffects.abortTrap(var0);
      HexEffects.resetSpy();
      HexEffects.resetTrapCooldown();
      HexEffects.clearBreakLocks();
      BREAK_LOCK_SEEN.clear();
      MIRROR_OPEN.clear();
      HexEffects.clearNewStates(var0);
      clearOnlineEffects(var0);
      var2.reset(var1);
      var2.setAllPrismatic(var1 && ThreadLocalRandom.current().nextDouble() < 0.05);
      if (var1 && (Boolean)HexConfig.ANNOUNCE.get()) {
         broadcast(var0, Component.translatable("hexbingo.msg.mode").withStyle(ChatFormatting.LIGHT_PURPLE));
         if (var2.allPrismatic()) {
            broadcast(var0, Component.translatable("hexbingo.msg.all_prismatic").withStyle(ChatFormatting.LIGHT_PURPLE));
         }
      }
   }

   private static void endGame(MinecraftServer var0) {
      HexGameData var1 = HexGameData.get(var0);
      FREE_PICK.clear();
      TASK_PICK_MODE.clear();
      HexEffects.clearJams();
      if (!var1.mayhem()) {
         var1.endGame();
      } else {
         HexEffects.abortTrap(var0);
         HexEffects.resetSpy();
         HexEffects.resetTrapCooldown();
         HexEffects.clearBreakLocks();
         BREAK_LOCK_SEEN.clear();
         MIRROR_OPEN.clear();
         HexEffects.clearNewStates(var0);
         clearOnlineEffects(var0);
         var1.endGame();
         if ((Boolean)HexConfig.ANNOUNCE.get()) {
            broadcast(var0, Component.translatable("hexbingo.msg.off").withStyle(ChatFormatting.GRAY));
         }
      }
   }

   private static void clearOnlineEffects(MinecraftServer var0) {
      for (ServerPlayer var2 : var0.getPlayerList().getPlayers()) {
         HexEffects.clearAll(var2);
      }
   }

   private static void broadcast(MinecraftServer var0, Component var1) {
      for (ServerPlayer var3 : var0.getPlayerList().getPlayers()) {
         var3.sendSystemMessage(var1);
      }
   }

   @SubscribeEvent
   public static void onServerTick(Post var0) {
      MinecraftServer var1 = var0.getServer();
      if (var1.getTickCount() % 20 == 0) {
         HexGameData var2 = HexGameData.get(var1);
         HexEffects.ensureNightVision(var1);
         updateBreakLock(var1);
         if (var2.mayhem()) {
            HexEffects.tick(var1);
            Bingo var3 = Bingo.get(var1.overworld());
            if (!var3.running()) {
               if (var2.gameStart() != 0L) {
                  var2.setGameStart(0L);
               }
            } else {
               if (var2.gameStart() == 0L) {
                  var2.setGameStart(System.currentTimeMillis());
               }

               long var4 = (System.currentTimeMillis() - var2.gameStart()) / 1000L;

               for (Team var7 : var3.getTeams()) {
                  if (!var7.getPlayers().isEmpty()) {
                     int[] var8 = var2.flags(var7.color.getName());
                     if (var8[0] == 0 && firstTriggered(var7)) {
                        var8[0] = 1;
                        var2.markTeamsDirty();
                        grantTeam(var1, var2, var7, 0);
                     }

                     if (var8[1] == 0 && var4 >= ((Integer)HexConfig.SECOND_MINUTES.get()).intValue() * 60L) {
                        var8[1] = 1;
                        var2.markTeamsDirty();
                        grantTeam(var1, var2, var7, 1);
                     }

                     if (var8[2] == 0 && var4 >= ((Integer)HexConfig.THIRD_MINUTES.get()).intValue() * 60L) {
                        var8[2] = 1;
                        var2.markTeamsDirty();
                        grantTeam(var1, var2, var7, 2);
                     }

                     if (var8[3] == 0 && var4 >= ((Integer)HexConfig.FOURTH_MINUTES.get()).intValue() * 60L) {
                        var8[3] = 1;
                        var2.markTeamsDirty();
                        grantTeam(var1, var2, var7, 3);
                     }
                  }
               }
            }
         }

         Bingo var9 = Bingo.get(var1.overworld());
         if (var9.running()) {
            for (ServerPlayer var5 : var1.getPlayerList().getPlayers()) {
               var9.checkCompleted(EnchantTaskType.INSTANCE, var5, EnchantTaskType.DUMMY);
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
            Team var4 = var0.getBingo().getTeam(var1);
            if (var4 != null) {
               int[] var5 = var3.flags(var4.color.getName());
               if (var5[0] == 0 && (!(Boolean)HexConfig.FIRST_REWARD_ON_LINE.get() || lineComplete(var4))) {
                  var5[0] = 1;
                  var3.markTeamsDirty();
                  grantTeam(var2, var3, var4, 0);
               }
            }
         }
      }
   }

   private static boolean firstTriggered(Team var0) {
      return HexConfig.FIRST_REWARD_ON_LINE.get() ? lineComplete(var0) : var0.completion().count() > 0;
   }

   private static boolean lineComplete(Team var0) {
      TeamCompletion var1 = var0.completion();
      if (var1.count() < 5) {
         return false;
      }

      for (int var2 = 0; var2 < 5; var2++) {
         boolean var3 = true;

         for (int var4 = 0; var4 < 5; var4++) {
            if (!var1.has(var2 * 5 + var4)) {
               var3 = false;
               break;
            }
         }

         if (var3) {
            return true;
         }
      }

      for (int var5 = 0; var5 < 5; var5++) {
         boolean var7 = true;

         for (int var9 = 0; var9 < 5; var9++) {
            if (!var1.has(var9 * 5 + var5)) {
               var7 = false;
               break;
            }
         }

         if (var7) {
            return true;
         }
      }

      boolean var6 = true;
      boolean var8 = true;

      for (int var10 = 0; var10 < 5; var10++) {
         var6 &= var1.has(var10 * 6);
         var8 &= var1.has(var10 * 4 + 4);
      }

      return var6 || var8;
   }

   private static void grantTeam(MinecraftServer var0, HexGameData var1, Team var2, int var3) {
      for (UUID var5 : var2.getPlayers()) {
         ServerPlayer var6 = var0.getPlayerList().getPlayer(var5);
         if (var6 != null) {
            enqueue(var1, var6, var3);
         }
      }
   }

   private static void enqueue(HexGameData var0, ServerPlayer var1, int var2) {
      if (var0.isHexLocked(var1.getUUID())) {
         return;
      }

      var0.pending(var1.getUUID()).add(var2);
      var0.setDirty();
      if (var0.offer(var1.getUUID()) == null) {
         presentNext(var1, var0);
      }
   }

   /** 星际航行固定传送到 y=256，在地狱没有意义：玩家在地狱时不刷出该海克斯。 */
   private static List<Hex> rollOfferFor(ServerPlayer var0, HexGameData var1, int var2, Collection<String> var3) {
      List<Hex> var4 = rollOffer(var1, var0.getUUID(), var2, var3);
      if (var0.level().dimension() == net.minecraft.world.level.Level.NETHER) {
         for (int var5 = 0; var5 < 8 && var4.stream().anyMatch(var6 -> var6.id().equals("star_voyage")); var5++) {
            var4 = rollOffer(var1, var0.getUUID(), var2, var3);
         }
      }

      MinecraftServer var7 = var0.level().getServer();
      if (var7 != null) {
         // 世界缩小：第四个海克斯不刷；游戏剩余不足 15 分钟不刷（不限时游戏不受限）
         Bingo var8 = Bingo.get(var7.overworld());
         long var9 = var8.runningUntil();
         boolean var10 = var2 == 3 || (var9 != 0L && var9 - System.currentTimeMillis() < 900000L);
         if (var10) {
            for (int var11 = 0; var11 < 8 && var4.stream().anyMatch(var12 -> var12.id().equals("world_shrink")); var11++) {
               var4 = rollOffer(var1, var0.getUUID(), var2, var3);
            }
         }

         // 全是我的：仅第三个海克斯、非占点模式（default/海克斯模式）、30% 概率替换一个选项
         if (var2 == 2 && !var3.contains(HexRegistry.ALL_MINE)) {
            boolean var13 = !var8.getSettings().game().lockout() && var8.getSettings().game().winCondition() != WinCondition.LOCK;
            if (var13 && ThreadLocalRandom.current().nextDouble() < 0.3) {
               Hex var14 = HexRegistry.byId(HexRegistry.ALL_MINE);
               if (var14 != null && !var4.isEmpty()) {
                  var4.set(ThreadLocalRandom.current().nextInt(var4.size()), var14);
               }
            }
         }
      }

      return var4;
   }

   private static void presentNext(ServerPlayer var0, HexGameData var1) {
      UUID var2 = var0.getUUID();
      if (!var1.isHexLocked(var2) && var1.offer(var2) == null) {
         List var3 = var1.pending(var2);

         while (!var3.isEmpty()) {
            int var4 = (Integer)var3.remove(0);
            int var5 = var1.qualityUp(var2);
            List<Hex> var6 = rollOfferFor(var0, var1, var4, var1.hexes(var2));
            if (var6.size() >= 3) {
               if (var5 > 0) {
                  var1.clearQualityUp(var2);
               }

               ArrayList<String> var7 = new ArrayList<>(var6.size());

               for (Hex var9 : var6) {
                  var7.add(var9.id());
               }

               var1.setOffer(var2, var4, var7);
               PacketDistributor.sendToPlayer(var0, new HexOfferPayload(var4, var7, var1.refreshCharges(var2) > 0), new CustomPacketPayload[0]);
               return;
            }

            // 掷不满 3 个（海克斯几乎集齐）：把该档奖励放回队列，不要白白吞掉
            var3.add(0, var4);
            break;
         }

         var1.setDirty();
      }
   }

   private static List<Hex> rollOffer(HexGameData var0, UUID var1, int var2, Collection<String> var3) {
      int var4 = var0.qualityUp(var1);
      List<Hex> var5;
      if (var2 == 3) {
         // 第四档海克斯固定为纯彩色，但静水监狱不能出现在最后一个海克斯中
         var5 = HexRegistry.offerPrismatic(var3, var0.hasExtraChoice(var1), "still_water");
      } else if (var0.allPrismatic()) {
         // 全彩对局：第三次海克斯也保持纯彩色，苦行者不注入
         var5 = HexRegistry.offerPrismatic(var3, var0.hasExtraChoice(var1), null, false);
         if (var5.size() < 3) {
            var5 = HexRegistry.offer(var2, var3, var0.hasExtraChoice(var1), var4);
            // 兜底池也可能注入苦行者，全彩对局里剔除
            var5.removeIf(var6 -> var6.id().equals(HexRegistry.ASCETIC));
         }
      } else if (var2 >= 2 && !ownsPrismatic(var0, var1)) {
         // 第三次海克斯池子允许出现苦行者，第四次仍然排除
         var5 = HexRegistry.offerPrismatic(var3, var0.hasExtraChoice(var1), null, var2 == 2);
         if (var5.size() < 3) {
            var5 = HexRegistry.offer(var2, var3, var0.hasExtraChoice(var1), var4);
         }
      } else {
         var5 = HexRegistry.offer(var2, var3, var0.hasExtraChoice(var1), var4);
      }

      // 品质提升：下一次选择必定出现至少 1 个彩色
      if (var4 > 0) {
         HexRegistry.forcePrismatic(var5, var3);
      }

      return var5;
   }

   public static void refresh(ServerPlayer var0) {
      MinecraftServer var1 = var0.level().getServer();
      if (var1 != null) {
         HexGameData var2 = HexGameData.get(var1);
         UUID var3 = var0.getUUID();
         HexGameData.Offer var4 = var2.offer(var3);
         if (var4 != null && !var2.isHexLocked(var3)) {
            if (var2.refreshCharges(var3) <= 0) {
               var0.sendSystemMessage(Component.translatable("hexbingo.msg.refresh_none").withStyle(ChatFormatting.GRAY));
               return;
            }

            ArrayList<String> var5 = new ArrayList<>(var2.hexes(var3));
            var5.addAll(var4.ids());
            List<Hex> var6 = rollOfferFor(var0, var2, var4.rewardIndex(), var5);
            if (var6.size() < 3) {
               var0.sendSystemMessage(Component.translatable("hexbingo.msg.refresh_fail").withStyle(ChatFormatting.GRAY));
               return;
            }

            var2.consumeRefreshCharge(var3);
            ArrayList<String> var7 = new ArrayList<>(var6.size());

            for (Hex var9 : var6) {
               var7.add(var9.id());
            }

            var2.setOffer(var3, var4.rewardIndex(), var7);
            PacketDistributor.sendToPlayer(var0, new HexOfferPayload(var4.rewardIndex(), var7, var2.refreshCharges(var3) > 0), new CustomPacketPayload[0]);
         }
      }
   }

   private static boolean ownsPrismatic(HexGameData var0, UUID var1) {
      for (String var3 : var0.hexes(var1)) {
         Hex var4 = HexRegistry.byId(var3);
         if (var4 != null && var4.color() == HexColor.PRISMATIC) {
            return true;
         }
      }

      return false;
   }

   public static void select(ServerPlayer var0, String var1) {
      MinecraftServer var2 = var0.level().getServer();
      if (var2 != null) {
         HexGameData var3 = HexGameData.get(var2);
         if (var3.mayhem()) {
            UUID var4 = var0.getUUID();
            if (var3.isHexLocked(var4)) {
               var0.sendSystemMessage(Component.translatable("hexbingo.msg.hex_locked").withStyle(ChatFormatting.DARK_GRAY));
               return;
            }

            HexGameData.Offer var5 = var3.offer(var4);
            if (var5 != null && var5.ids().contains(var1)) {
               Hex var6 = HexRegistry.byId(var1);
               if (var6 != null && !var3.hexes(var4).contains(var1)) {
                  var3.removeOffer(var4);
                  var3.addHex(var4, var1);
                  HexEffects.apply(var0, var6);
                  var0.sendSystemMessage(
                     Component.translatable("hexbingo.msg.selected", new Object[]{Component.translatable("hexbingo.hex." + var1 + ".name")})
                        .withStyle(ChatFormatting.LIGHT_PURPLE)
                  );
                  if ((Boolean)HexConfig.ANNOUNCE.get()) {
                     Team var7 = Bingo.get(var0.level()).getTeam(var0);
                     if (var7 != null) {
                        MutableComponent var8 = Component.translatable(
                              "hexbingo.msg.team_selected", new Object[]{var0.getDisplayName(), Component.translatable("hexbingo.hex." + var1 + ".name")}
                           )
                           .withStyle(ChatFormatting.GRAY);

                        for (UUID var10 : var7.getPlayers()) {
                           ServerPlayer var11 = var2.getPlayerList().getPlayer(var10);
                           if (var11 != null) {
                              var11.sendSystemMessage(var8);
                           }
                        }
                     }
                  }

                  presentNext(var0, var3);
               } else {
                  var0.sendSystemMessage(Component.translatable("hexbingo.msg.no_offer").withStyle(ChatFormatting.RED));
               }
            } else {
               var0.sendSystemMessage(Component.translatable("hexbingo.msg.no_offer").withStyle(ChatFormatting.RED));
            }
         }
      }
   }

   public static int openSelfPick(ServerPlayer var0) {
      MinecraftServer var1 = var0.level().getServer();
      if (var1 == null) {
         return 0;
      }

      HexGameData var2 = HexGameData.get(var1);
      if (!var2.mayhem()) {
         return 0;
      }

      if (var2.isHexLocked(var0.getUUID())) {
         var0.sendSystemMessage(Component.translatable("hexbingo.msg.hex_locked").withStyle(ChatFormatting.DARK_GRAY));
         return 0;
      }

      ArrayList var3 = new ArrayList();

      for (Hex var5 : HexRegistry.all()) {
         if (!var2.hexes(var0.getUUID()).contains(var5.id()) && !var5.id().equals(HexRegistry.OFFER_UPGRADE)) {
            var3.add(var5.id());
         }
      }

      if (var3.isEmpty()) {
         return 0;
      }

      FREE_PICK.add(var0.getUUID());
      PacketDistributor.sendToPlayer(var0, new HexListPayload(List.copyOf(var3)), new CustomPacketPayload[0]);
      HexBingo.LOGGER.info("Sent self hex pick list ({} entries) to {}", var3.size(), var0.getScoreboardName());
      return var3.size();
   }

   public static int openFreePick(MinecraftServer var0) {
      HexGameData var1 = HexGameData.get(var0);
      if (!var1.mayhem()) {
         return 0;
      }

      List<Hex> var2 = HexRegistry.all();
      int var3 = 0;

      for (ServerPlayer var5 : var0.getPlayerList().getPlayers()) {
         if (var1.isHexLocked(var5.getUUID())) {
            continue;
         }

         ArrayList<String> var6 = new ArrayList<>();

         for (Hex var8 : var2) {
            if (!var1.hexes(var5.getUUID()).contains(var8.id()) && !var8.id().equals(HexRegistry.OFFER_UPGRADE)) {
               var6.add(var8.id());
            }
         }

         if (!var6.isEmpty()) {
            FREE_PICK.add(var5.getUUID());
            PacketDistributor.sendToPlayer(var5, new HexListPayload(List.copyOf(var6)), new CustomPacketPayload[0]);
            HexBingo.LOGGER.info("Sent hex list ({} entries) to {}", var6.size(), var5.getScoreboardName());
            var3++;
         }
      }

      return var3;
   }

   public static void freePick(ServerPlayer var0, String var1) {
      MinecraftServer var2 = var0.level().getServer();
      if (var2 != null) {
         HexGameData var3 = HexGameData.get(var2);
         if (var3.mayhem() && FREE_PICK.contains(var0.getUUID())) {
            if (var3.isHexLocked(var0.getUUID())) {
               var0.sendSystemMessage(Component.translatable("hexbingo.msg.hex_locked").withStyle(ChatFormatting.DARK_GRAY));
               return;
            }

            Hex var4 = HexRegistry.byId(var1);
            if (var4 != null && !var1.equals(HexRegistry.OFFER_UPGRADE) && !var1.equals(HexRegistry.ALL_MINE)) {
               if (var3.hexes(var0.getUUID()).contains(var1)) {
                  var0.sendSystemMessage(Component.translatable("hexbingo.msg.hex_pick_owned").withStyle(ChatFormatting.GRAY));
               } else {
                  FREE_PICK.remove(var0.getUUID());
                  var3.addHex(var0.getUUID(), var1);
                  HexEffects.apply(var0, var4);
                  var0.sendSystemMessage(
                     Component.translatable("hexbingo.msg.selected", new Object[]{Component.translatable("hexbingo.hex." + var1 + ".name")})
                        .withStyle(ChatFormatting.LIGHT_PURPLE)
                  );
               }
            }
         }
      }
   }

   public static HexRosterPayload roster(MinecraftServer var0) {
      HexGameData var1 = HexGameData.get(var0);
      Bingo var2 = Bingo.get(var0.overworld());
      ArrayList var3 = new ArrayList();
      HashSet var4 = new HashSet();

      for (Team var6 : var2.getTeams()) {
         if (!var6.getPlayers().isEmpty()) {
            ArrayList var7 = new ArrayList();

            for (UUID var9 : var6.getPlayers()) {
               ServerPlayer var10 = var0.getPlayerList().getPlayer(var9);
               if (var10 != null) {
                  var7.add(
                     new HexRosterPayload.PlayerEntry(
                        var10.getName(), List.copyOf(var1.hexes(var9)), var1.grantedPrefixes(var9), hexCounts(var9)
                     )
                  );
                  var4.add(var9);
               }
            }

            if (!var7.isEmpty()) {
               var3.add(new HexRosterPayload.TeamEntry(var6.getName(), var6.color.getTextureDiffuseColor(), List.copyOf(var7)));
            }
         }
      }

      ArrayList var11 = new ArrayList();

      for (ServerPlayer var13 : var0.getPlayerList().getPlayers()) {
         if (!var4.contains(var13.getUUID())) {
            var11.add(
               new HexRosterPayload.PlayerEntry(
                  var13.getName(), List.copyOf(var1.hexes(var13.getUUID())), var1.grantedPrefixes(var13.getUUID()), hexCounts(var13.getUUID())
               )
            );
         }
      }

      if (!var11.isEmpty()) {
         var3.add(new HexRosterPayload.TeamEntry(Component.translatable("hexbingo.roster.solo"), -1, List.copyOf(var11)));
      }

      return new HexRosterPayload(List.copyOf(var3));
   }

   public static void openRoster(ServerPlayer var0) {
      MinecraftServer var1 = var0.level().getServer();
      if (var1 != null) {
         HexRosterPayload var2 = roster(var1);
         PacketDistributor.sendToPlayer(var0, var2, new CustomPacketPayload[0]);
         HexBingo.LOGGER.info("Sent hex roster ({} entries) to {}", var2.teams().size(), var0.getScoreboardName());
      }
   }

   private static Map<String, Integer> hexCounts(UUID var0) {
      int var1 = HexEffects.killStacks(var0);
      return var1 > 0 ? Map.of("stack_dragon", var1) : Map.of();
   }

   // 镜像复制：列出最近一支其它队伍中自己未拥有的海克斯
   public static boolean openMirror(ServerPlayer var0) {
      MinecraftServer var1 = var0.level().getServer();
      if (var1 == null) {
         return false;
      }

      HexGameData var2 = HexGameData.get(var1);
      if (!var2.mayhem()) {
         return false;
      }

      Bingo var3 = Bingo.get(var0.level());
      Team var4 = var3.getTeam(var0);
      ArrayList<Team> var5 = new ArrayList<>();

      for (Team var7 : var3.getTeams()) {
         if (var7 != var4 && !var7.getPlayers().isEmpty()) {
            var5.add(var7);
         }
      }

      var5.sort(Comparator.comparingDouble(var7 -> nearestTeamDistance(var0, var7)));
      ArrayList<String> var12 = new ArrayList<>();

      for (Team var8 : var5) {
         LinkedHashSet<String> var9 = new LinkedHashSet<>();

         for (UUID var11 : var8.getPlayers()) {
            for (String var14 : var2.hexes(var11)) {
               if (!var2.hexes(var0.getUUID()).contains(var14)
                  && !var14.equals(HexRegistry.OFFER_UPGRADE)
                  && !var14.equals(HexRegistry.ASCETIC)
                  && !var14.equals("mirror_hex")) {
                  var9.add(var14);
               }
            }
         }

         if (!var9.isEmpty()) {
            var12.addAll(var9);
            break;
         }
      }

      if (var12.isEmpty()) {
         var0.sendSystemMessage(Component.translatable("hexbingo.msg.mirror_none").withStyle(ChatFormatting.GRAY));
         return false;
      }

      MIRROR_OPEN.add(var0.getUUID());
      PacketDistributor.sendToPlayer(var0, new HexMirrorPayload(List.copyOf(var12)), new CustomPacketPayload[0]);
      return true;
   }

   private static double nearestTeamDistance(ServerPlayer var0, Team var1) {
      double var2 = Double.MAX_VALUE;

      for (UUID var5 : var1.getPlayers()) {
         ServerPlayer var6 = var0.level().getServer() == null ? null : var0.level().getServer().getPlayerList().getPlayer(var5);
         if (var6 != null) {
            var2 = Math.min(var2, var0.distanceToSqr(var6));
         }
      }

      return var2;
   }

   public static void mirrorPick(ServerPlayer var0, String var1) {
      MinecraftServer var2 = var0.level().getServer();
      if (var2 != null) {
         HexGameData var3 = HexGameData.get(var2);
         if (var3.mayhem() && MIRROR_OPEN.remove(var0.getUUID())) {
            if (var3.isHexLocked(var0.getUUID())) {
               var0.sendSystemMessage(Component.translatable("hexbingo.msg.hex_locked").withStyle(ChatFormatting.DARK_GRAY));
               return;
            }

            Hex var4 = HexRegistry.byId(var1);
            if (var4 == null
               || var1.equals(HexRegistry.OFFER_UPGRADE)
               || var1.equals(HexRegistry.ASCETIC)
               || var1.equals("mirror_hex")) {
               var0.sendSystemMessage(Component.translatable("hexbingo.msg.mirror_invalid").withStyle(ChatFormatting.RED));
               return;
            }

            if (var3.hexes(var0.getUUID()).contains(var1)) {
               var0.sendSystemMessage(Component.translatable("hexbingo.msg.hex_pick_owned").withStyle(ChatFormatting.GRAY));
               return;
            }

            if (!ownedByOtherTeam(var2, var0, var1)) {
               var0.sendSystemMessage(Component.translatable("hexbingo.msg.mirror_invalid").withStyle(ChatFormatting.RED));
               return;
            }

            var3.addHex(var0.getUUID(), var1);
            HexEffects.apply(var0, var4);
            var0.sendSystemMessage(
               Component.translatable("hexbingo.msg.selected", new Object[]{Component.translatable("hexbingo.hex." + var1 + ".name")})
                  .withStyle(ChatFormatting.LIGHT_PURPLE)
            );
         }
      }
   }

   private static boolean ownedByOtherTeam(MinecraftServer var0, ServerPlayer var1, String var2) {
      HexGameData var3 = HexGameData.get(var0);
      Team var4 = Bingo.get(var1.level()).getTeam(var1);

      for (ServerPlayer var6 : var0.getPlayerList().getPlayers()) {
         if (var6 != var1 && (var4 == null || !var4.hasPlayer(var6)) && var3.hexes(var6.getUUID()).contains(var2)) {
            return true;
         }
      }

      return false;
   }

   // 目标封锁 / 空白格：打开任务选择界面
   public static void openTaskPick(ServerPlayer var0, String var1) {
      MinecraftServer var2 = var0.level().getServer();
      if (var2 == null) {
         return;
      }

      HexGameData var3 = HexGameData.get(var2);
      if (!var3.mayhem()) {
         return;
      }

      Bingo var4 = Bingo.get(var2.overworld());
      Team var5 = var4.getTeam(var0);
      if ("deny".equals(var1)) {
         if (!HexEffects.denyReady(var0)) {
            return;
         }
      } else if ("blank".equals(var1)) {
         if (!HexEffects.canUseBlankCell(var0) || var5 == null) {
            return;
         }
      } else if ("seal".equals(var1)) {
         if (!HexEffects.sealReady(var0)) {
            return;
         }
      } else if ("mine".equals(var1)) {
         if (!HexEffects.allMineReady(var0)) {
            return;
         }
      } else {
         return;
      }

      ArrayList<HexTaskListPayload.Entry> var6 = new ArrayList<>();

      for (int var7 = 0; var7 < var4.tasks().size(); var7++) {
         Task var8 = var4.task(var7);
         if (var8 != Task.EMPTY) {
            if ("blank".equals(var1) && (var5.completed(var7) || var5.locked(var7))) {
               continue;
            }

            if ("seal".equals(var1) && HexEffects.isSealed(var7)) {
               continue;
            }

            var6.add(new HexTaskListPayload.Entry(var7, var8.contentName(var2)));
         }
      }

      if (var6.isEmpty()) {
         var0.sendSystemMessage(Component.translatable("hexbingo.msg.seal_none").withStyle(ChatFormatting.GRAY));
         return;
      }

      TASK_PICK_MODE.put(var0.getUUID(), var1);
      PacketDistributor.sendToPlayer(var0, new HexTaskListPayload(var1, List.copyOf(var6)), new CustomPacketPayload[0]);
   }

   public static void denyTaskPick(ServerPlayer var0, int var1) {
      String var2 = TASK_PICK_MODE.remove(var0.getUUID());
      if (var2 == null) {
         // 未打开过选择界面（或重复包）：直接丢弃，客户端 index 不可信
         return;
      }

      if ("blank".equals(var2)) {
         HexEffects.applyBlankCell(var0, var1);
      } else if ("seal".equals(var2)) {
         HexEffects.applySeal(var0, var1);
      } else if ("mine".equals(var2)) {
         HexEffects.applyAllMine(var0, var1);
      } else {
         HexEffects.applyObjectiveDeny(var0, var1);
      }
   }

   public static boolean reopen(ServerPlayer var0) {
      MinecraftServer var1 = var0.level().getServer();
      if (var1 == null) {
         return false;
      } else {
         HexGameData var2 = HexGameData.get(var1);
         if (!var2.mayhem()) {
            return false;
         } else {
            HexGameData.Offer var3 = var2.offer(var0.getUUID());
            if (var3 != null) {
               PacketDistributor.sendToPlayer(
                  var0,
                  new HexOfferPayload(var3.rewardIndex(), var3.ids(), var2.refreshCharges(var0.getUUID()) > 0),
                  new CustomPacketPayload[0]
               );
               return true;
            } else if (!var2.pending(var0.getUUID()).isEmpty()) {
               presentNext(var0, var2);
               return true;
            } else {
               return false;
            }
         }
      }
   }

   @SubscribeEvent
   public static void onPlayerJoin(PlayerLoggedInEvent var0) {
      if (var0.getEntity() instanceof ServerPlayer var1) {
         MinecraftServer var3 = var1.level().getServer();
         if (var3 == null) {
            return;
         }

         if (HexGameData.get(var3).mayhem()) {
            HexEffects.reapply(var1);
            HexEffects.ensureFae(var1);
            HexEffects.ensureQuestItems(var1);
            HexEffects.syncSealsTo(var3, var1);
            resume(var1);
         } else {
            HexEffects.clearAll(var1);
         }
      }
   }

   @SubscribeEvent
   public static void onPlayerRespawn(PlayerRespawnEvent var0) {
      if (var0.getEntity() instanceof ServerPlayer var1) {
         MinecraftServer var3 = var1.level().getServer();
         if (var3 == null) {
            return;
         }

         if (HexGameData.get(var3).mayhem()) {
            HexEffects.reapply(var1);
            HexEffects.ensureFae(var1);
            HexEffects.ensureQuestItems(var1);
            HexEffects.syncSealsTo(var3, var1);
            resume(var1);
         } else {
            HexEffects.clearAll(var1);
         }
      }
   }

   private static void resume(ServerPlayer var0) {
      MinecraftServer var1 = var0.level().getServer();
      if (var1 != null) {
         HexGameData var2 = HexGameData.get(var1);
         if (var2.mayhem()) {
            HexGameData.Offer var3 = var2.offer(var0.getUUID());
            if (var3 != null) {
               PacketDistributor.sendToPlayer(
                  var0,
                  new HexOfferPayload(var3.rewardIndex(), var3.ids(), var2.refreshCharges(var0.getUUID()) > 0),
                  new CustomPacketPayload[0]
               );
            } else if (!var2.pending(var0.getUUID()).isEmpty()) {
               presentNext(var0, var2);
            }
         }
      }
   }

   private HexManager() {
   }
}
