/*
 * Copyright (c) 2026 Big_Jls and DeepSeek-v4.1-Flash-1017.
 * Licensed under the Apache License, Version 2.0.
 * See LICENSE and NOTICE.
 */
package com.castbingo.hexbingo.client;

import com.castbingo.hexbingo.Hex;
import com.castbingo.hexbingo.HexColor;
import java.util.List;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.FormattedCharSequence;

public final class HexRender {
   private static final int[] PRISMATIC_COLORS = new int[]{-43691, -22016, -171, -11141291, -11141121, -5614081};
   private static final int TOOLTIP_W = 270;
   private static final int PAD = 8;

   public static void drawBorder(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, HexColor var5) {
      if (var5 == HexColor.PRISMATIC) {
         int var6 = Math.max(1, var3 / PRISMATIC_COLORS.length);

         for (int var7 = 0; var7 < PRISMATIC_COLORS.length; var7++) {
            int var8 = var1 + var7 * var6;
            int var9 = var7 == PRISMATIC_COLORS.length - 1 ? var1 + var3 : var1 + (var7 + 1) * var6;
            var0.fill(var8, var2, var9, var2 + 3, PRISMATIC_COLORS[var7]);
            var0.fill(var8, var2 + var4 - 3, var9, var2 + var4, PRISMATIC_COLORS[var7]);
         }

         var0.fill(var1, var2, var1 + 2, var2 + var4, -5614081);
         var0.fill(var1 + var3 - 2, var2, var1 + var3, var2 + var4, -5614081);
      } else {
         var0.fill(var1, var2, var1 + var3, var2 + 3, var5.rgb);
         var0.fill(var1, var2 + var4 - 3, var1 + var3, var2 + var4, var5.rgb);
         var0.fill(var1, var2, var1 + 2, var2 + var4, var5.rgb);
         var0.fill(var1 + var3 - 2, var2, var1 + var3, var2 + var4, var5.rgb);
      }
   }

   public static void drawTooltip(GuiGraphicsExtractor var0, Font var1, int var2, int var3, int var4, int var5, Hex var6, boolean var7) {
      drawTooltip(var0, var1, var2, var3, var4, var5, var6, var7, null);
   }

   public static void drawTooltip(
      GuiGraphicsExtractor var0, Font var1, int var2, int var3, int var4, int var5, Hex var6, boolean var7, String prefix
   ) {
      MutableComponent var8 = prefix != null
         ? Component.translatable("hexbingo.hex.prefix." + prefix, Component.translatable("hexbingo.hex." + var6.id() + ".name"))
         : Component.translatable("hexbingo.hex." + var6.id() + ".name");
      MutableComponent var9 = Component.translatable("hexbingo.hex." + var6.id() + ".desc");
      List<FormattedCharSequence> var10 = var1.split(var9, 254);
      int var11 = 36 + var10.size() * 9 + (var7 ? 17 : 0) + 8;
      int var12 = var4 + 14;
      int var13 = var5 + 14;
      if (var12 + 270 > var2 - 4) {
         var12 = var2 - 270 - 4;
      }

      if (var13 + var11 > var3 - 4) {
         var13 = var5 - var11 - 14;
      }

      var12 = Math.max(4, var12);
      var13 = Math.max(4, var13);
      var0.fill(var12, var13, var12 + 270, var13 + var11, -267382768);
      drawBorder(var0, var12, var13, 270, var11, var6.color());
      int var14 = var13 + 8;
      var0.text(var1, var8, var12 + 8, var14, -1, true);
      var14 += 13;
      var0.text(var1, Component.translatable("hexbingo.color." + var6.color().id), var12 + 8, var14, var6.color().rgb, false);
      var14 += 15;

      for (FormattedCharSequence var16 : var10) {
         var0.text(var1, var16, var12 + 8, var14, -2039584, false);
         var14 += 9;
      }

      if (var7) {
         var14 += 8;
         var0.text(var1, Component.translatable("hexbingo.select.confirm_hint"), var12 + 8, var14, -10496, true);
      }
   }

   private HexRender() {
   }
}
