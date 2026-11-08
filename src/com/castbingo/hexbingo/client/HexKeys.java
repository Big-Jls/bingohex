/*
 * Copyright (c) 2026 Big_Jls and DeepSeek-v4.1-Flash-1017.
 * Licensed under the Apache License, Version 2.0.
 * See LICENSE and NOTICE.
 */
package com.castbingo.hexbingo.client;

import com.castbingo.hexbingo.HexBingo;
import com.castbingo.hexbingo.network.HexRosterRequestPayload;
import com.castbingo.hexbingo.network.HexSpiderTogglePayload;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping.Category;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent.Post;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

public final class HexKeys {
   public static final KeyMapping ROSTER = new KeyMapping("hexbingo.key.roster", 75, Category.MISC);
   public static final KeyMapping SPIDER_TOGGLE = new KeyMapping("hexbingo.key.spider_toggle", 86, Category.MISC);

   public static void register(RegisterKeyMappingsEvent var0) {
      var0.register(ROSTER);
      var0.register(SPIDER_TOGGLE);
   }

   @SubscribeEvent
   public static void clientTick(Post var0) {
      while (ROSTER.consumeClick()) {
         HexBingo.LOGGER.info("Hex roster key pressed");
         if (Minecraft.getInstance().getConnection() != null) {
            ClientPacketDistributor.sendToServer(new HexRosterRequestPayload(), new CustomPacketPayload[0]);
         }
      }

      while (SPIDER_TOGGLE.consumeClick()) {
         if (Minecraft.getInstance().getConnection() != null) {
            ClientPacketDistributor.sendToServer(new HexSpiderTogglePayload(), new CustomPacketPayload[0]);
         }
      }
   }

   private HexKeys() {
   }
}
