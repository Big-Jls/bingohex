/*
 * Copyright (c) 2026 Big_Jls and DeepSeek-v4.1-Flash-1017.
 * Licensed under the Apache License, Version 2.0.
 * See LICENSE and NOTICE.
 */
package com.castbingo.hexbingo;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public final class HexRegistry {
   /** 只在第一次海克斯三选一出现的海克斯，不能被质变/自选列表获得。 */
   public static final String OFFER_UPGRADE = "offer_upgrade";
   /** 只在第三次海克斯三选一出现的海克斯，不能被质变/复制/其它卡池获得。 */
   public static final String ASCETIC = "ascetic";
   /** 只在第三次海克斯三选一出现（非占点模式、有概率），不能被任何质变获得。 */
   public static final String ALL_MINE = "all_mine";
   private static final Gson GSON = new Gson();
   private static final Map<String, Hex> BY_ID = new LinkedHashMap<>();
   private static final Map<HexColor, List<Hex>> BY_COLOR = new LinkedHashMap<>();

   public static void init() {
      HexBingo.LOGGER
         .info(
            "Loaded {} hex augments ({} silver, {} gold, {} prismatic).",
            new Object[]{BY_ID.size(), BY_COLOR.get(HexColor.SILVER).size(), BY_COLOR.get(HexColor.GOLD).size(), BY_COLOR.get(HexColor.PRISMATIC).size()}
         );
   }

   private static void load() {
      try (InputStream var0 = HexRegistry.class.getResourceAsStream("/data/hexbingo/hexes.json")) {
         if (var0 != null) {
            JsonObject var1 = JsonParser.parseReader(new InputStreamReader(var0, StandardCharsets.UTF_8)).getAsJsonObject();

            for (JsonElement var3 : var1.getAsJsonArray("hexes")) {
               JsonObject var4 = var3.getAsJsonObject();
               String var5 = var4.get("id").getAsString();
               HexColor var6 = HexColor.byId(var4.get("color").getAsString());
               ArrayList var7 = new ArrayList();

               for (JsonElement var9 : var4.getAsJsonArray("effects")) {
                  var7.add((HexEffect)GSON.fromJson(var9, HexEffect.class));
               }

               Hex var13 = new Hex(var5, var6, List.copyOf(var7));
               BY_ID.put(var5, var13);
               BY_COLOR.get(var6).add(var13);
            }
         } else {
            HexBingo.LOGGER.error("Missing hex data file /data/hexbingo/hexes.json");
         }
      } catch (Exception var12) {
         HexBingo.LOGGER.error("Failed to load hex augments", var12);
      }
   }

   public static Hex byId(String var0) {
      return BY_ID.get(var0);
   }

   public static List<Hex> all() {
      return List.copyOf(BY_ID.values());
   }

   public static List<Hex> byColor(HexColor var0) {
      return BY_COLOR.get(var0);
   }

   public static List<Hex> offer(int var0, Collection<String> var1, boolean var2, int var3) {
      int var4 = Math.min(2, var0 + Math.max(0, var3));
      boolean var9 = var0 == 2;
      ArrayList var5 = new ArrayList(var2 ? 4 : 3);
      switch (var4) {
         case 0:
            pick(HexColor.SILVER, 2, var1, var5);
            pick(HexColor.GOLD, 1, var1, var5);
            break;
         case 1:
            pickRandom(var1, var5);
            pickRandom(var1, var5);
            pickRandom(var1, var5);
            break;
         default:
            pick(HexColor.GOLD, 2, var1, var5);
            pick(HexColor.PRISMATIC, 1, var1, var5);
            fill(var1, var5, null, var9);
            maybeAddAscetic(var5, var1, var9);
      }

      if (var2) {
         if (var4 == 0) {
            pick(HexColor.GOLD, 1, var1, var5);
         } else {
            pick(HexColor.PRISMATIC, 1, var1, var5, null, var9);
         }
      }

      Collections.shuffle(var5, ThreadLocalRandom.current());
      return var5;
   }

   public static List<Hex> offerPrismatic(Collection<String> var0, boolean var1) {
      return offerPrismatic(var0, var1, null);
   }

   public static List<Hex> offerPrismatic(Collection<String> var0, boolean var1, String var2) {
      return offerPrismatic(var0, var1, var2, false);
   }

   public static List<Hex> offerPrismatic(Collection<String> var0, boolean var1, String var2, boolean var3) {
      ArrayList var4 = new ArrayList(var1 ? 4 : 3);
      pick(HexColor.PRISMATIC, var1 ? 4 : 3, var0, var4, var2, var3);
      if (var4.size() < 3) {
         fill(var0, var4, var2, var3);
      }

      maybeAddAscetic(var4, var0, var3);
      Collections.shuffle(var4, ThreadLocalRandom.current());
      return var4;
   }

   private static void maybeAddAscetic(List<Hex> var0, Collection<String> var1, boolean var2) {
      if (var2 && !var1.contains(ASCETIC) && !var0.isEmpty()) {
         Hex var3 = BY_ID.get(ASCETIC);
         if (var3 != null && !var0.contains(var3) && ThreadLocalRandom.current().nextDouble() < 0.35) {
            var0.set(ThreadLocalRandom.current().nextInt(var0.size()), var3);
         }
      }
   }

   private static void pick(HexColor var0, int var1, Collection<String> var2, List<Hex> var3) {
      pick(var0, var1, var2, var3, null, false);
   }

   private static void pick(HexColor var0, int var1, Collection<String> var2, List<Hex> var3, String var4) {
      pick(var0, var1, var2, var3, var4, false);
   }

   private static void pick(HexColor var0, int var1, Collection<String> var2, List<Hex> var3, String var4, boolean var5) {
      ArrayList var6 = new ArrayList();

      for (Hex var8 : BY_COLOR.get(var0)) {
         if (!var2.contains(var8.id())
            && !var3.contains(var8)
            && (var4 == null || !var4.equals(var8.id()))
            && (var5 || !var8.id().equals(ASCETIC))
            && !var8.id().equals(ALL_MINE)) {
            var6.add(var8);
         }
      }

      ThreadLocalRandom var10 = ThreadLocalRandom.current();

      for (int var11 = 0; var11 < var1 && !var6.isEmpty(); var11++) {
         var3.add((Hex)var6.remove(var10.nextInt(var6.size())));
      }
   }

   private static void fill(Collection<String> var0, List<Hex> var1) {
      fill(var0, var1, null, false);
   }

   private static void fill(Collection<String> var0, List<Hex> var1, String var2) {
      fill(var0, var1, var2, false);
   }

   private static void fill(Collection<String> var0, List<Hex> var1, String var2, boolean var3) {
      ThreadLocalRandom var4 = ThreadLocalRandom.current();

      while (var1.size() < 3) {
         ArrayList var5 = new ArrayList();

         for (Hex var7 : BY_ID.values()) {
            if (var7.color() != HexColor.SILVER
               && !var0.contains(var7.id())
               && !var1.contains(var7)
               && (var2 == null || !var2.equals(var7.id()))
               && (var3 || !var7.id().equals(ASCETIC))
               && !var7.id().equals(ALL_MINE)) {
               var5.add(var7);
            }
         }

         if (var5.isEmpty()) {
            return;
         }

         var1.add((Hex)var5.get(var4.nextInt(var5.size())));
      }
   }

   private static void pickRandom(Collection<String> var0, List<Hex> var1) {
      ThreadLocalRandom var2 = ThreadLocalRandom.current();
      double var3 = var2.nextDouble();
      HexColor var5 = var3 < 0.5 ? HexColor.SILVER : (var3 < 0.85 ? HexColor.GOLD : HexColor.PRISMATIC);
      ArrayList var6 = new ArrayList();

      for (Hex var8 : BY_COLOR.get(var5)) {
         if (!var0.contains(var8.id())
            && !var1.contains(var8)
            && !var8.id().equals(OFFER_UPGRADE)
            && !var8.id().equals(ASCETIC)
            && !var8.id().equals(ALL_MINE)) {
            var6.add(var8);
         }
      }

      if (var6.isEmpty()) {
         for (HexColor var10 : HexColor.values()) {
            for (Hex var12 : BY_COLOR.get(var10)) {
               if (!var0.contains(var12.id())
                  && !var1.contains(var12)
                  && !var12.id().equals(OFFER_UPGRADE)
                  && !var12.id().equals(ASCETIC)
                  && !var12.id().equals(ALL_MINE)) {
                  var6.add(var12);
               }
            }
         }
      }

      if (!var6.isEmpty()) {
         var1.add((Hex)var6.get(var2.nextInt(var6.size())));
      }
   }

   /** 品质提升：保证本次三选一至少出现 1 个彩色。 */
   public static void forcePrismatic(List<Hex> var0, Collection<String> var1) {
      for (Hex var3 : var0) {
         if (var3.color() == HexColor.PRISMATIC) {
            return;
         }
      }

      ArrayList<Hex> var7 = new ArrayList<>();

      for (Hex var4 : BY_COLOR.get(HexColor.PRISMATIC)) {
         if (!var1.contains(var4.id()) && !var0.contains(var4) && !var4.id().equals(ALL_MINE)) {
            var7.add(var4);
         }
      }

      if (!var7.isEmpty()) {
         int var5 = -1;

         for (int var8 = 0; var8 < var0.size(); var8++) {
            if (var0.get(var8).color() == HexColor.SILVER && !var0.get(var8).id().equals(ASCETIC)) {
               var5 = var8;
               break;
            }
         }

         if (var5 < 0) {
            for (int var9 = 0; var9 < var0.size(); var9++) {
               if (!var0.get(var9).id().equals(ASCETIC)) {
                  var5 = var9;
                  break;
               }
            }
         }

         if (var5 >= 0) {
            var0.set(var5, var7.get(ThreadLocalRandom.current().nextInt(var7.size())));
         }
      }
   }

   private HexRegistry() {
   }

   static {
      for (HexColor var3 : HexColor.values()) {
         BY_COLOR.put(var3, new ArrayList<>());
      }

      load();
   }
}
