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

public record HexMirrorPayload(List<String> ids) implements CustomPacketPayload {
   public static final Type<HexMirrorPayload> TYPE = new Type(Identifier.fromNamespaceAndPath("hexbingo", "hex_mirror"));
   public static final StreamCodec<RegistryFriendlyByteBuf, HexMirrorPayload> STREAM_CODEC = StreamCodec.of((var0, var1) -> {
      var0.writeVarInt(var1.ids().size());

      for (String var3 : var1.ids()) {
         var0.writeUtf(var3);
      }
   }, var0 -> {
      int var1 = var0.readVarInt();
      ArrayList var2 = new ArrayList(var1);

      for (int var3 = 0; var3 < var1; var3++) {
         var2.add(var0.readUtf());
      }

      return new HexMirrorPayload(List.copyOf(var2));
   });

   public Type<? extends CustomPacketPayload> type() {
      return TYPE;
   }
}
