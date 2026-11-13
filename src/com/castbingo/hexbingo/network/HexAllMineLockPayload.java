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

/** 服务端 -> 客户端：同步被「全是我的」海克斯锁定的任务下标列表。 */
public record HexAllMineLockPayload(int[] indices) implements CustomPacketPayload {
   public static final Type<HexAllMineLockPayload> TYPE = new Type(Identifier.fromNamespaceAndPath("hexbingo", "hex_all_mine_lock"));
   public static final StreamCodec<RegistryFriendlyByteBuf, HexAllMineLockPayload> STREAM_CODEC = StreamCodec.of(
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
         return new HexAllMineLockPayload(var2);
      }
   );

   @Override
   public Type<? extends CustomPacketPayload> type() {
      return TYPE;
   }
}
