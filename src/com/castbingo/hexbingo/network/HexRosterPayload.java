/*
 * Copyright (c) 2026 Big_Jls and DeepSeek-v4.1-Flash-1017.
 * Licensed under the Apache License, Version 2.0.
 * See LICENSE and NOTICE.
 */
package com.castbingo.hexbingo.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.resources.Identifier;

public record HexRosterPayload(List<HexRosterPayload.TeamEntry> teams) implements CustomPacketPayload {
   public static final Type<HexRosterPayload> TYPE = new Type(Identifier.fromNamespaceAndPath("hexbingo", "hex_roster"));
   public static final StreamCodec<RegistryFriendlyByteBuf, HexRosterPayload> STREAM_CODEC = StreamCodec.of((var0, var1) -> {
      var0.writeVarInt(var1.teams().size());

      for (HexRosterPayload.TeamEntry var3 : var1.teams()) {
         ComponentSerialization.STREAM_CODEC.encode(var0, var3.name());
         var0.writeInt(var3.color());
         var0.writeVarInt(var3.players().size());

         for (HexRosterPayload.PlayerEntry var5 : var3.players()) {
            ComponentSerialization.STREAM_CODEC.encode(var0, var5.name());
            var0.writeVarInt(var5.hexes().size());

            for (String var7 : var5.hexes()) {
               var0.writeUtf(var7);
            }

            var0.writeVarInt(var5.prefixes().size());
            var5.prefixes().forEach((hexId, prefix) -> {
               var0.writeUtf(hexId);
               var0.writeUtf(prefix);
            });
         }
      }
   }, var0 -> {
      int var1 = var0.readVarInt();
      ArrayList var2 = new ArrayList(var1);

      for (int var3 = 0; var3 < var1; var3++) {
         Component var4 = (Component)ComponentSerialization.STREAM_CODEC.decode(var0);
         int var5 = var0.readInt();
         int var6 = var0.readVarInt();
         ArrayList var7 = new ArrayList(var6);

         for (int var8 = 0; var8 < var6; var8++) {
            Component var9 = (Component)ComponentSerialization.STREAM_CODEC.decode(var0);
            int var10 = var0.readVarInt();
            ArrayList var11 = new ArrayList(var10);

            for (int var12 = 0; var12 < var10; var12++) {
               var11.add(var0.readUtf());
            }

            int var13 = var0.readVarInt();
            HashMap<String, String> var14 = new HashMap<>(var13);

            for (int var15 = 0; var15 < var13; var15++) {
               var14.put(var0.readUtf(), var0.readUtf());
            }

            var7.add(new HexRosterPayload.PlayerEntry(var9, List.copyOf(var11), Map.copyOf(var14)));
         }

         var2.add(new HexRosterPayload.TeamEntry(var4, var5, List.copyOf(var7)));
      }

      return new HexRosterPayload(List.copyOf(var2));
   });

   public Type<? extends CustomPacketPayload> type() {
      return TYPE;
   }

   public record PlayerEntry(Component name, List<String> hexes, Map<String, String> prefixes) {
   }

   public record TeamEntry(Component name, int color, List<HexRosterPayload.PlayerEntry> players) {
   }
}
