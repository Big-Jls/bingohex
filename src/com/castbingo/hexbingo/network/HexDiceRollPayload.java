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

public record HexDiceRollPayload(int durationTicks) implements CustomPacketPayload {
   public static final Type<HexDiceRollPayload> TYPE = new Type(Identifier.fromNamespaceAndPath("hexbingo", "hex_dice_roll"));
   public static final StreamCodec<RegistryFriendlyByteBuf, HexDiceRollPayload> STREAM_CODEC = StreamCodec.of(
      (var0, var1) -> var0.writeVarInt(var1.durationTicks()), var0 -> new HexDiceRollPayload(var0.readVarInt())
   );

   public Type<? extends CustomPacketPayload> type() {
      return TYPE;
   }
}
