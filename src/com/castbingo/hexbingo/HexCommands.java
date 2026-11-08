/*
 * Copyright (c) 2026 Big_Jls and DeepSeek-v4.1-Flash-1017.
 * Licensed under the Apache License, Version 2.0.
 * See LICENSE and NOTICE.
 */
package com.castbingo.hexbingo;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import io.github.noeppi_noeppi.mods.bongo.Bingo;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permissions;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

public final class HexCommands {
   public static void register(RegisterCommandsEvent var0) {
      var0.getDispatcher()
         .register(
            (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal(
                                    "bingo"
                                 )
                                 .then(
                                    Commands.literal("hex")
                                       .executes(
                                          var0x -> {
                                             requireRunning(var0x.getSource());
                                             ServerPlayer var1 = ((CommandSourceStack)var0x.getSource()).getPlayerOrException();
                                             boolean var2 = HexManager.reopen(var1);
                                             ((CommandSourceStack)var0x.getSource())
                                                .sendSuccess(() -> Component.translatable(var2 ? "hexbingo.msg.reopened" : "hexbingo.msg.no_offer"), false);
                                             return var2 ? 1 : 0;
                                          }
                                       )
                                 ))
                              .then(Commands.literal("hexes").executes(var0x -> {
                                 requireRunning(var0x.getSource());
                                 ServerPlayer var1 = ((CommandSourceStack)var0x.getSource()).getPlayerOrException();
                                 if (HexManager.openSelfPick(var1) <= 0) {
                                    throw new SimpleCommandExceptionType(Component.translatable("hexbingo.msg.no_self_pick")).create();
                                 } else {
                                    return 1;
                                 }
                              }))
                              .then(Commands.literal("rtp").executes(var0x -> {
                                 requireRunning(var0x.getSource());
                                 ServerPlayer var1 = ((CommandSourceStack)var0x.getSource()).getPlayerOrException();
                                 if (!HexEffects.randomTeleport(var1)) {
                                    throw new SimpleCommandExceptionType(Component.translatable("hexbingo.rtp.no_hex")).create();
                                 } else {
                                    return 1;
                                 }
                              })))
                           .then(Commands.literal("hook").executes(var0x -> {
                              requireRunning(var0x.getSource());
                              ServerPlayer var1 = ((CommandSourceStack)var0x.getSource()).getPlayerOrException();
                              if (!HexEffects.grapple(var1)) {
                                 throw new SimpleCommandExceptionType(Component.translatable("hexbingo.hook.no_hex")).create();
                              } else {
                                 return 1;
                              }
                           })))
                        .then(Commands.literal("boomerang").executes(var0x -> {
                           requireRunning(var0x.getSource());
                           ServerPlayer var1 = ((CommandSourceStack)var0x.getSource()).getPlayerOrException();
                           if (!HexEffects.launchBoomerang(var1)) {
                              throw new SimpleCommandExceptionType(Component.translatable("hexbingo.hook.no_boomerang")).create();
                           } else {
                              return 1;
                           }
                        })))
                     .then(Commands.literal("trap").executes(var0x -> {
                        requireRunning(var0x.getSource());
                        ServerPlayer var1 = ((CommandSourceStack)var0x.getSource()).getPlayerOrException();
                        if (!HexEffects.startTrap(var1)) {
                           throw new SimpleCommandExceptionType(Component.translatable("hexbingo.hook.no_trap")).create();
                        } else {
                           return 1;
                        }
                     })))
                  .then(
                     Commands.literal("spy")
                        .then(
                           Commands.argument("teammate", EntityArgument.player()).then(Commands.argument("target", EntityArgument.player()).executes(var0x -> {
                              requireRunning(var0x.getSource());
                              ServerPlayer var1 = ((CommandSourceStack)var0x.getSource()).getPlayerOrException();
                              ServerPlayer var2 = EntityArgument.getPlayer(var0x, "teammate");
                              ServerPlayer var3 = EntityArgument.getPlayer(var0x, "target");
                              if (!HexEffects.spySwap(var1, var2, var3)) {
                                 throw new SimpleCommandExceptionType(Component.translatable("hexbingo.hook.no_spy")).create();
                              } else {
                                 return 1;
                              }
                           }))
                        )
                  ))
               .then(
                  ((LiteralArgumentBuilder)Commands.literal("hexall").requires(var0x -> var0x.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER)))
                     .executes(var0x -> {
                        requireRunning((CommandSourceStack)var0x.getSource());
                        MinecraftServer var1 = ((CommandSourceStack)var0x.getSource()).getServer();
                        int var2 = HexManager.openFreePick(var1);
                        ((CommandSourceStack)var0x.getSource()).sendSuccess(() -> Component.translatable("hexbingo.msg.hexall_ui", new Object[]{var2}), true);
                        return var2;
                     })
               )
         );
   }

   private static void requireRunning(CommandSourceStack var0) throws CommandSyntaxException {
      MinecraftServer var1 = var0.getServer();
      if (!Bingo.get(var1.overworld()).running()) {
         throw new SimpleCommandExceptionType(Component.translatable("hexbingo.msg.not_started")).create();
      }
   }

   private HexCommands() {
   }
}
