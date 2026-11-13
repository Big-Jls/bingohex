/*
 * Copyright (c) 2026 Big_Jls and DeepSeek-v4.1-Flash-1017.
 * Licensed under the Apache License, Version 2.0.
 * See LICENSE and NOTICE.
 */
package com.castbingo.hexbingo.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.resources.Identifier;

/** 服务端 -> 客户端：打开 72 格团队背包界面（原版界面表无法注册自定义菜单，改由封包手动打开）。 */
public record HexBpOpenPayload(Component title) implements CustomPacketPayload {
   public static final Type<HexBpOpenPayload> TYPE = new Type(Identifier.fromNamespaceAndPath("hexbingo", "hex_bp_open"));
   public static final StreamCodec<RegistryFriendlyByteBuf, HexBpOpenPayload> STREAM_CODEC = StreamCodec.of(
      (var0, var1) -> ComponentSerialization.STREAM_CODEC.encode(var0, var1.title()),
      var0 -> new HexBpOpenPayload((Component)ComponentSerialization.STREAM_CODEC.decode(var0))
   );

   @Override
   public Type<? extends CustomPacketPayload> type() {
      return TYPE;
   }
}
