/*
 * Copyright (c) 2026 Big_Jls and DeepSeek-v4.1-Flash-1017.
 * Licensed under the Apache License, Version 2.0.
 * See LICENSE and NOTICE.
 */
package com.castbingo.hexbingo.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.resources.Identifier;

/** 服务端 -> 客户端：同步被「封印」海克斯封印的任务下标列表。 */
public record HexSealPayload(int[] indices) implements CustomPacketPayload {
   public static final Type<HexSealPayload> TYPE = new Type(Identifier.fromNamespaceAndPath("hexbingo", "hex_seal"));
   public static final StreamCodec<RegistryFriendlyByteBuf, HexSealPayload> STREAM_CODEC = StreamCodec.of(
      (var0, var1) -> {
         var0.writeVarInt(var1.indices().length);
         for (int var2 : var1.indices()) {
            var0.writeVarInt(var2);
         }
      },
      var0 -> {
         int var1 = var0.readVarInt();
         int[] var2 = new int[var1];
         for (int var3 = 0; var3 < var1; var3++) {
            var2[var3] = var0.readVarInt();
         }
         return new HexSealPayload(var2);
      }
   );

   @Override
   public Type<? extends CustomPacketPayload> type() {
      return TYPE;
   }
}
