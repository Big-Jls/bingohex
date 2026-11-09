/*
 * Copyright (c) 2026 Big_Jls and DeepSeek-v4.1-Flash-1017.
 * Licensed under the Apache License, Version 2.0.
 * See LICENSE and NOTICE.
 */
package com.castbingo.hexbingo.network;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.resources.Identifier;

public record HexTaskListPayload(List<HexTaskListPayload.Entry> tasks) implements CustomPacketPayload {
   public static final Type<HexTaskListPayload> TYPE = new Type(Identifier.fromNamespaceAndPath("hexbingo", "hex_task_list"));
   public static final StreamCodec<RegistryFriendlyByteBuf, HexTaskListPayload> STREAM_CODEC = StreamCodec.of((var0, var1) -> {
      var0.writeVarInt(var1.tasks().size());

      for (HexTaskListPayload.Entry var3 : var1.tasks()) {
         var0.writeVarInt(var3.index());
         ComponentSerialization.STREAM_CODEC.encode(var0, var3.name());
      }
   }, var0 -> {
      int var1 = var0.readVarInt();
      ArrayList var2 = new ArrayList(var1);

      for (int var3 = 0; var3 < var1; var3++) {
         int var4 = var0.readVarInt();
         Component var5 = (Component)ComponentSerialization.STREAM_CODEC.decode(var0);
         var2.add(new HexTaskListPayload.Entry(var4, var5));
      }

      return new HexTaskListPayload(List.copyOf(var2));
   });

   public Type<? extends CustomPacketPayload> type() {
      return TYPE;
   }

   public record Entry(int index, Component name) {
   }
}
