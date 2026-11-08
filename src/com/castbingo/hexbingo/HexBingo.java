/*
 * Copyright (c) 2026 Big_Jls and DeepSeek-v4.1-Flash-1017.
 * Licensed under the Apache License, Version 2.0.
 * See LICENSE and NOTICE.
 */
package com.castbingo.hexbingo;

import com.castbingo.hexbingo.client.HexClient;
import com.castbingo.hexbingo.client.HexKeys;
import com.castbingo.hexbingo.network.HexListPayload;
import com.castbingo.hexbingo.network.HexOfferPayload;
import com.castbingo.hexbingo.network.HexPickPayload;
import com.castbingo.hexbingo.network.HexRosterPayload;
import com.castbingo.hexbingo.network.HexRosterRequestPayload;
import com.castbingo.hexbingo.network.HexSelectPayload;
import com.castbingo.hexbingo.network.HexSpiderTogglePayload;
import com.mojang.logging.LogUtils;
import io.github.noeppi_noeppi.mods.bongo.task.TaskTypes;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig.Type;
import net.neoforged.fml.loading.FMLEnvironment;
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
         NeoForge.EVENT_BUS.addListener(HexKeys::clientTick);
      }
   }

   private void registerPayloads(RegisterPayloadHandlersEvent var1) {
      var1.registrar("1")
         .playToClient(
            HexOfferPayload.TYPE, HexOfferPayload.STREAM_CODEC, (var0, var1x) -> var1x.enqueueWork(() -> HexClient.open(var0.rewardIndex(), var0.ids()))
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
         }));
   }
}
