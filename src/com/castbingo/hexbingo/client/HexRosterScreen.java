/*
 * Copyright (c) 2026 Big_Jls and DeepSeek-v4.1-Flash-1017.
 * Licensed under the Apache License, Version 2.0.
 * See LICENSE and NOTICE.
 */
package com.castbingo.hexbingo.client;

import com.castbingo.hexbingo.Hex;
import com.castbingo.hexbingo.HexRegistry;
import com.castbingo.hexbingo.network.HexRosterPayload;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class HexRosterScreen extends Screen {
   private static final int LINE_H = 13;
   private static final int LIST_TOP = 38;
   private static final int MARGIN = 24;
   private final List<HexRosterScreen.Line> lines = new ArrayList<>();
   private int scroll;
   private int hovered = -1;

   public HexRosterScreen(HexRosterPayload var1) {
      super(Component.translatable("hexbingo.roster.title"));

      for (HexRosterPayload.TeamEntry var3 : var1.teams()) {
         this.lines.add(new HexRosterScreen.Line(var3.name(), null, var3.color() | 0xFF000000, 0, null));

         for (HexRosterPayload.PlayerEntry var5 : var3.players()) {
            this.lines.add(new HexRosterScreen.Line(var5.name(), null, -1, 14, null));
            if (var5.hexes().isEmpty()) {
               this.lines.add(new HexRosterScreen.Line(Component.translatable("hexbingo.roster.none"), null, -8355712, 28, null));
            } else {
               for (String var7 : var5.hexes()) {
                  Hex var8 = HexRegistry.byId(var7);
                  if (var8 != null) {
                     String var17 = var5.prefixes().get(var7);
                     Component var18 = var17 != null
                        ? Component.translatable("hexbingo.hex.prefix." + var17, Component.translatable("hexbingo.hex." + var7 + ".name"))
                        : Component.translatable("hexbingo.hex." + var7 + ".name");
                     MutableComponent var9 = Component.literal("[")
                        .append(Component.translatable("hexbingo.color." + var8.color().id))
                        .append(Component.literal("] "))
                        .append(var18);
                     int var19 = var5.counts().getOrDefault(var7, 0);
                     if (var19 > 0) {
                        var9.append(Component.translatable("hexbingo.roster.stacks", var19).withColor(0xAAAAAA));
                     }

                     this.lines.add(new HexRosterScreen.Line(var9, var8, -2039584, 28, var17));
                  }
               }
            }
         }
      }

      if (this.lines.isEmpty()) {
         this.lines.add(new HexRosterScreen.Line(Component.translatable("hexbingo.roster.empty"), null, -8355712, 0, null));
      }
   }

   public boolean isPauseScreen() {
      return false;
   }

   public void extractRenderState(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      var1.fill(0, 0, this.width, this.height, -535818216);
      var1.centeredText(this.font, this.title, this.width / 2, 12, -10496);
      byte var5 = 24;
      int var6 = this.width - 48;
      int var7 = this.height - 24;
      int var8 = var7 - 38;
      int var9 = this.lines.size() * 13;
      int var10 = Math.max(0, var9 - var8);
      this.scroll = Math.max(0, Math.min(this.scroll, var10));
      this.hovered = this.hoveredLine(var2, var3, var5, var6, var7);
      var1.enableScissor(var5, 38, var5 + var6, var7);

      for (int var11 = 0; var11 < this.lines.size(); var11++) {
         HexRosterScreen.Line var12 = this.lines.get(var11);
         int var13 = 38 + var11 * 13 - this.scroll;
         if (var13 + 13 >= 38 && var13 <= var7) {
            boolean var14 = var11 == this.hovered && var12.hex() != null;
            if (var14) {
               var1.fill(var5, var13 - 2, var5 + var6, var13 + 13 - 2, -14935012);
            }

            var1.text(this.font, var12.text(), var5 + var12.indent(), var13, var12.color(), false);
         }
      }

      var1.disableScissor();
      if (var9 > var8) {
         int var15 = var5 + var6 + 4;
         var1.fill(var15, 38, var15 + 4, var7, -15198176);
         int var16 = Math.max(16, (int)((float)(var8 * var8) / var9));
         int var17 = 38 + (int)((float)(var8 - var16) * this.scroll / Math.max(1, var10));
         var1.fill(var15, var17, var15 + 4, var17 + var16, -8355712);
      }

      var1.centeredText(this.font, Component.translatable("hexbingo.roster.hint"), this.width / 2, this.height - 13, -8355712);
      if (this.hovered >= 0 && this.lines.get(this.hovered).hex() != null) {
         HexRosterScreen.Line var18 = this.lines.get(this.hovered);
         HexRender.drawTooltip(var1, this.font, this.width, this.height, var2, var3, var18.hex(), false, var18.prefix());
      }
   }

   private int hoveredLine(double var1, double var3, int var5, int var6, int var7) {
      if (var1 >= var5 && var1 < var5 + var6 && var3 >= 38.0 && var3 < var7) {
         int var8 = (int)((var3 - 38.0 + this.scroll) / 13.0);
         return var8 >= 0 && var8 < this.lines.size() ? var8 : -1;
      } else {
         return -1;
      }
   }

   public boolean mouseScrolled(double var1, double var3, double var5, double var7) {
      this.scroll = Math.max(0, this.scroll - (int)(var7 * 13.0 * 3.0));
      return true;
   }

   private record Line(Component text, Hex hex, int color, int indent, String prefix) {
   }
}
