/*
 * Copyright (c) 2026 Big_Jls and DeepSeek-v4.1-Flash-1017.
 * Licensed under the Apache License, Version 2.0.
 * See LICENSE and NOTICE.
 */
package com.castbingo.hexbingo;

import com.castbingo.hexbingo.client.HexClient;
import com.castbingo.hexbingo.client.HexDiceAnim;
import com.castbingo.hexbingo.client.HexKeys;
import com.castbingo.hexbingo.client.HexSealOverlay;
import com.castbingo.hexbingo.network.HexDiceRollPayload;
import com.castbingo.hexbingo.network.HexListPayload;
import com.castbingo.hexbingo.network.HexMagnetTogglePayload;
import com.castbingo.hexbingo.network.HexMidasPayload;
import com.castbingo.hexbingo.network.HexMirrorPayload;
import com.castbingo.hexbingo.network.HexMirrorPickPayload;
import com.castbingo.hexbingo.network.HexOfferPayload;
import com.castbingo.hexbingo.network.HexPickPayload;
import com.castbingo.hexbingo.network.HexRosterPayload;
import com.castbingo.hexbingo.network.HexRefreshPayload;
import com.castbingo.hexbingo.network.HexRosterRequestPayload;
import com.castbingo.hexbingo.network.HexSealPayload;
import com.castbingo.hexbingo.network.HexSelectPayload;
import com.castbingo.hexbingo.network.HexSpiderTogglePayload;
import com.castbingo.hexbingo.network.HexSwordFlyTogglePayload;
import com.castbingo.hexbingo.network.HexTaskListPayload;
import com.castbingo.hexbingo.network.HexTaskPickPayload;
import com.mojang.logging.LogUtils;
import io.github.noeppi_noeppi.mods.bongo.task.TaskTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig.Type;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import org.slf4j.Logger;

@Mod("hexbingo")
public final class HexBingo {
   public static final String MODID = "hexbingo";
   public static final Logger LOGGER = LogUtils.getLogger();

   public HexBingo(ModContainer var1) {
      IEventBus var2 = var1.getEventBus();
      var1.registerConfig(Type.COMMON, HexConfig.SPEC);
      HexRegistry.init();
      TaskTypes.registerType(EnchantTaskType.INSTANCE);
      var2.addListener(this::registerPayloads);
      NeoForge.EVENT_BUS.addListener(HexCommands::register);
      NeoForge.EVENT_BUS.addListener(HexManager::onCommand);
      NeoForge.EVENT_BUS.addListener(HexManager::onServerTick);
      NeoForge.EVENT_BUS.addListener(HexManager::onPlayerJoin);
      NeoForge.EVENT_BUS.addListener(HexManager::onPlayerRespawn);
      NeoForge.EVENT_BUS.register(new HexEvents());
      if (FMLEnvironment.getDist() == Dist.CLIENT) {
         var2.addListener(HexKeys::register);
         var2.addListener(this::registerGuiLayers);
         NeoForge.EVENT_BUS.addListener(HexKeys::clientTick);
         NeoForge.EVENT_BUS.addListener(HexDiceAnim::render);
      }
   }

   @OnlyIn(Dist.CLIENT)
   private void registerGuiLayers(RegisterGuiLayersEvent var1) {
      // 注册在 bongo 卡面之上：封印格子的屏障图标盖在任务图标上
      var1.registerAbove(
         Identifier.fromNamespaceAndPath("bongo", "bongo"),
         Identifier.fromNamespaceAndPath("hexbingo", "hex_seal"),
         HexSealOverlay.INSTANCE
      );
   }

   private void registerPayloads(RegisterPayloadHandlersEvent var1) {
      var1.registrar("1")
         .playToClient(
            HexOfferPayload.TYPE,
            HexOfferPayload.STREAM_CODEC,
            (var0, var1x) -> var1x.enqueueWork(() -> HexClient.open(var0.rewardIndex(), var0.ids(), var0.refreshAvailable()))
         )
         .playToServer(HexSelectPayload.TYPE, HexSelectPayload.STREAM_CODEC, (var0, var1x) -> var1x.enqueueWork(() -> {
            if (var1x.player() instanceof ServerPlayer var2) {
               HexManager.select(var2, var0.id());
            }
         }))
         .playToClient(HexListPayload.TYPE, HexListPayload.STREAM_CODEC, (var0, var1x) -> var1x.enqueueWork(() -> HexClient.openList(var0.ids())))
         .playToServer(HexPickPayload.TYPE, HexPickPayload.STREAM_CODEC, (var0, var1x) -> var1x.enqueueWork(() -> {
            if (var1x.player() instanceof ServerPlayer var2) {
               HexManager.freePick(var2, var0.id());
            }
         }))
         .playToClient(HexRosterPayload.TYPE, HexRosterPayload.STREAM_CODEC, (var0, var1x) -> var1x.enqueueWork(() -> HexClient.openRoster(var0)))
         .playToServer(HexRosterRequestPayload.TYPE, HexRosterRequestPayload.STREAM_CODEC, (var0, var1x) -> var1x.enqueueWork(() -> {
            if (var1x.player() instanceof ServerPlayer var1xx) {
               HexManager.openRoster(var1xx);
            }
         }))
         .playToServer(HexSpiderTogglePayload.TYPE, HexSpiderTogglePayload.STREAM_CODEC, (var0, var1x) -> var1x.enqueueWork(() -> {
            if (var1x.player() instanceof ServerPlayer var2) {
               HexEffects.toggleSpiderClimb(var2);
            }
         }))
         .playToServer(HexMagnetTogglePayload.TYPE, HexMagnetTogglePayload.STREAM_CODEC, (var0, var1x) -> var1x.enqueueWork(() -> {
            if (var1x.player() instanceof ServerPlayer var2) {
               HexEffects.toggleMagnet(var2);
            }
         }))
         .playToServer(HexSwordFlyTogglePayload.TYPE, HexSwordFlyTogglePayload.STREAM_CODEC, (var0, var1x) -> var1x.enqueueWork(() -> {
            if (var1x.player() instanceof ServerPlayer var2) {
               HexEffects.toggleSwordFly(var2);
            }
         }))
         .playToClient(HexDiceRollPayload.TYPE, HexDiceRollPayload.STREAM_CODEC, (var0, var1x) -> var1x.enqueueWork(() -> HexDiceAnim.start(var0.durationTicks())))
         .playToClient(HexSealPayload.TYPE, HexSealPayload.STREAM_CODEC, (var0, var1x) -> var1x.enqueueWork(() -> HexClient.applySeals(var0.indices())))
         .playToServer(HexRefreshPayload.TYPE, HexRefreshPayload.STREAM_CODEC, (var0, var1x) -> var1x.enqueueWork(() -> {
            if (var1x.player() instanceof ServerPlayer var2) {
               HexManager.refresh(var2);
            }
         }))
         .playToServer(HexMidasPayload.TYPE, HexMidasPayload.STREAM_CODEC, (var0, var1x) -> var1x.enqueueWork(() -> {
            if (var1x.player() instanceof ServerPlayer var2) {
               HexEffects.castMidas(var2);
            }
         }))
         .playToClient(HexMirrorPayload.TYPE, HexMirrorPayload.STREAM_CODEC, (var0, var1x) -> var1x.enqueueWork(() -> HexClient.openMirror(var0.ids())))
         .playToServer(HexMirrorPickPayload.TYPE, HexMirrorPickPayload.STREAM_CODEC, (var0, var1x) -> var1x.enqueueWork(() -> {
            if (var1x.player() instanceof ServerPlayer var2) {
               HexManager.mirrorPick(var2, var0.id());
            }
         }))
         .playToClient(HexTaskListPayload.TYPE, HexTaskListPayload.STREAM_CODEC, (var0, var1x) -> var1x.enqueueWork(() -> HexClient.openTasks(var0)))
         .playToServer(HexTaskPickPayload.TYPE, HexTaskPickPayload.STREAM_CODEC, (var0, var1x) -> var1x.enqueueWork(() -> {
            if (var1x.player() instanceof ServerPlayer var2) {
               HexManager.denyTaskPick(var2, var0.index());
            }
         }));
   }
}
