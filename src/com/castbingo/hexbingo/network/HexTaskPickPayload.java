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

public record HexTaskPickPayload(int index) implements CustomPacketPayload {
   public static final Type<HexTaskPickPayload> TYPE = new Type(Identifier.fromNamespaceAndPath("hexbingo", "hex_task_pick"));
   public static final StreamCodec<RegistryFriendlyByteBuf, HexTaskPickPayload> STREAM_CODEC = StreamCodec.of(
      (var0, var1) -> var0.writeVarInt(var1.index()), var0 -> new HexTaskPickPayload(var0.readVarInt())
   );

   public Type<? extends CustomPacketPayload> type() {
      return TYPE;
   }
}
