/*
 * Copyright (c) 2026 Big_Jls and DeepSeek-v4.1-Flash-1017.
 * Licensed under the Apache License, Version 2.0.
 * See LICENSE and NOTICE.
 */
package com.castbingo.hexbingo.network;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.resources.Identifier;

public record HexOfferPayload(int rewardIndex, List<String> ids) implements CustomPacketPayload {
   public static final Type<HexOfferPayload> TYPE = new Type(Identifier.fromNamespaceAndPath("hexbingo", "hex_offer"));
   public static final StreamCodec<RegistryFriendlyByteBuf, HexOfferPayload> STREAM_CODEC = StreamCodec.of((var0, var1) -> {
      var0.writeVarInt(var1.rewardIndex());
      var0.writeVarInt(var1.ids().size());

      for (String var3 : var1.ids()) {
         var0.writeUtf(var3);
      }
   }, var0 -> {
      int var1 = var0.readVarInt();
      int var2 = var0.readVarInt();
      ArrayList var3 = new ArrayList(var2);

      for (int var4 = 0; var4 < var2; var4++) {
         var3.add(var0.readUtf());
      }

      return new HexOfferPayload(var1, List.copyOf(var3));
   });

   public Type<? extends CustomPacketPayload> type() {
      return TYPE;
   }
}
