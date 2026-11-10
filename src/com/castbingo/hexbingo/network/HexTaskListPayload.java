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

public record HexTaskListPayload(String mode, List<HexTaskListPayload.Entry> tasks) implements CustomPacketPayload {
   public static final Type<HexTaskListPayload> TYPE = new Type(Identifier.fromNamespaceAndPath("hexbingo", "hex_task_list"));
   public static final StreamCodec<RegistryFriendlyByteBuf, HexTaskListPayload> STREAM_CODEC = StreamCodec.of((var0, var1) -> {
      var0.writeUtf(var1.mode());
      var0.writeVarInt(var1.tasks().size());

      for (HexTaskListPayload.Entry var3 : var1.tasks()) {
         var0.writeVarInt(var3.index());
         ComponentSerialization.STREAM_CODEC.encode(var0, var3.name());
      }
   }, var0 -> {
      String var1 = var0.readUtf();
      int var2 = var0.readVarInt();
      ArrayList var3 = new ArrayList(var2);

      for (int var4 = 0; var4 < var2; var4++) {
         int var5 = var0.readVarInt();
         Component var6 = (Component)ComponentSerialization.STREAM_CODEC.decode(var0);
         var3.add(new HexTaskListPayload.Entry(var5, var6));
      }

      return new HexTaskListPayload(var1, List.copyOf(var3));
   });

   public Type<? extends CustomPacketPayload> type() {
      return TYPE;
   }

   public record Entry(int index, Component name) {
   }
}
