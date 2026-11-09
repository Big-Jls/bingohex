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

/** 点石成金：客户端按下 G 键时发送。 */
public record HexMidasPayload() implements CustomPacketPayload {
   public static final Type<HexMidasPayload> TYPE = new Type(Identifier.fromNamespaceAndPath("hexbingo", "hex_midas"));
   public static final StreamCodec<RegistryFriendlyByteBuf, HexMidasPayload> STREAM_CODEC = StreamCodec.of(
      (var0, var1) -> {
      }, var0 -> new HexMidasPayload()
   );

   public Type<? extends CustomPacketPayload> type() {
      return TYPE;
   }
}
