/*
 * Copyright (c) 2026 Big_Jls and DeepSeek-v4.1-Flash-1017.
 * Licensed under the Apache License, Version 2.0.
 * See LICENSE and NOTICE.
 */
package com.castbingo.hexbingo.client;

import com.castbingo.hexbingo.Hex;
import com.castbingo.hexbingo.HexBingo;
import com.castbingo.hexbingo.HexRegistry;
import com.castbingo.hexbingo.network.HexRosterPayload;
import com.castbingo.hexbingo.network.HexTaskListPayload;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.client.Minecraft;

public final class HexClient {
   /** 客户端缓存：被「封印」海克斯封印的任务下标。 */
   private static final Set<Integer> SEALED = new HashSet<>();

   public static boolean isSealed(int var0) {
      return SEALED.contains(var0);
   }

   public static void applySeals(int[] var0) {
      SEALED.clear();
      for (int var1 : var0) {
         SEALED.add(var1);
      }
   }

   public static void open(int var0, List<String> var1, boolean var2) {
      ArrayList var3 = new ArrayList(3);

      for (String var5 : var1) {
         Hex var6 = HexRegistry.byId(var5);
         if (var6 != null) {
            var3.add(var6);
         }
      }

      if (var3.size() >= 3) {
         Minecraft.getInstance().setScreenAndShow(new HexSelectScreen(var0, var3, var2));
      }
   }

   public static void openList(List<String> var0) {
      ArrayList var1 = new ArrayList(var0.size());

      for (String var3 : var0) {
         Hex var4 = HexRegistry.byId(var3);
         if (var4 != null) {
            var1.add(var4);
         }
      }

      HexBingo.LOGGER.info("Opening hex list screen with {} of {} entries", var1.size(), var0.size());
      if (!var1.isEmpty()) {
         Minecraft.getInstance().setScreenAndShow(new HexListScreen(var1));
      }
   }

   public static void openRoster(HexRosterPayload var0) {
      HexBingo.LOGGER.info("Opening hex roster screen with {} teams", var0.teams().size());
      Minecraft.getInstance().setScreenAndShow(new HexRosterScreen(var0));
   }

   public static void openMirror(List<String> var0) {
      ArrayList var1 = new ArrayList(var0.size());

      for (String var3 : var0) {
         Hex var4 = HexRegistry.byId(var3);
         if (var4 != null) {
            var1.add(var4);
         }
      }

      if (!var1.isEmpty()) {
         Minecraft.getInstance().setScreenAndShow(new HexListScreen(var1, true));
      }
   }

   public static void openTasks(HexTaskListPayload var0) {
      if (!var0.tasks().isEmpty()) {
         Minecraft.getInstance().setScreenAndShow(new HexTaskScreen(var0));
      }
   }

   private HexClient() {
   }
}
