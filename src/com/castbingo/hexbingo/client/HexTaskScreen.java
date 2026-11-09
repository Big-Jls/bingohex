/*
 * Copyright (c) 2026 Big_Jls and DeepSeek-v4.1-Flash-1017.
 * Licensed under the Apache License, Version 2.0.
 * See LICENSE and NOTICE.
 */
package com.castbingo.hexbingo.client;

import com.castbingo.hexbingo.network.HexTaskListPayload;
import com.castbingo.hexbingo.network.HexTaskPickPayload;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

/** 目标封锁：选择一个任务。 */
public class HexTaskScreen extends Screen {
   private static final int ROW_H = 22;
   private static final int LIST_TOP = 46;
   private final List<HexTaskListPayload.Entry> tasks;
   private int selected = -1;
   private int scroll;
   private int hovered = -1;

   public HexTaskScreen(List<HexTaskListPayload.Entry> var1) {
      super(Component.translatable("hexbingo.deny.title"));
      this.tasks = new ArrayList<>(var1);
   }

   public boolean isPauseScreen() {
      return false;
   }

   public void extractRenderState(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      var1.fill(0, 0, this.width, this.height, -535818216);
      var1.centeredText(this.font, this.title, this.width / 2, 14, -10496);
      var1.centeredText(
         this.font,
         Component.translatable("hexbingo.deny.subtitle", new Object[]{Component.literal(Integer.toString(this.tasks.size()))}),
         this.width / 2,
         28,
         -4144960
      );
      int var5 = 24;
      int var6 = this.width - 48;
      int var7 = this.height - 44;
      int var8 = var7 - 46;
      int var9 = this.tasks.size() * 22;
      int var10 = Math.max(0, var9 - var8);
      this.scroll = Math.max(0, Math.min(this.scroll, var10));
      this.hovered = this.hoveredRow(var2, var3, var5, var6, var7);
      var1.enableScissor(var5, 46, var5 + var6, var7);

      for (int var11 = 0; var11 < this.tasks.size(); var11++) {
         int var12 = 46 + var11 * 22 - this.scroll;
         if (var12 + 22 >= 46 && var12 <= var7) {
            boolean var13 = var11 == this.hovered;
            boolean var14 = var11 == this.selected;
            int var15 = var14 ? -13882310 : (var13 ? -14935012 : -15198176);
            var1.fill(var5, var12, var5 + var6, var12 + 20, var15);
            var1.fill(var5, var12, var5 + 3, var12 + 20, -43691);
            Component var16 = this.tasks.get(var11).name();
            var1.text(this.font, var16, var5 + 10, var12 + 7, var14 || var13 ? -1 : -2039584, false);
         }
      }

      var1.disableScissor();
      if (var9 > var8) {
         int var17 = var5 + var6 + 4;
         var1.fill(var17, 46, var17 + 4, var7, -15198176);
         int var18 = Math.max(16, (int)((float)(var8 * var8) / var9));
         int var19 = 46 + (int)((float)(var8 - var18) * this.scroll / Math.max(1, var10));
         var1.fill(var17, var19, var17 + 4, var19 + var18, -8355712);
      }

      this.drawConfirmButton(var1, var2, var3);
      var1.centeredText(this.font, Component.translatable("hexbingo.deny.hint"), this.width / 2, this.height - 14, -8355712);
   }

   private void drawConfirmButton(GuiGraphicsExtractor var1, int var2, int var3) {
      int var4 = this.buttonX();
      int var5 = this.buttonY();
      boolean var6 = this.selected >= 0;
      boolean var7 = var2 >= var4 && var2 < var4 + 100 && var3 >= var5 && var3 < var5 + 18;
      int var8 = var6 ? (var7 ? -12677570 : -13669585) : -14013910;
      int var9 = var6 ? -10027162 : -11184811;
      var1.fill(var4, var5, var4 + 100, var5 + 18, var8);
      var1.fill(var4, var5, var4 + 100, var5 + 1, var9);
      var1.fill(var4, var5 + 18 - 1, var4 + 100, var5 + 18, var9);
      var1.fill(var4, var5, var4 + 1, var5 + 18, var9);
      var1.fill(var4 + 100 - 1, var5, var4 + 100, var5 + 18, var9);
      var1.centeredText(this.font, Component.translatable("hexbingo.select.confirm"), this.width / 2, var5 + 5, var6 ? -1 : -8947849);
   }

   private int hoveredRow(double var1, double var3, int var5, int var6, int var7) {
      if (var1 >= var5 && var1 < var5 + var6 && var3 >= 46.0 && var3 < var7) {
         int var8 = (int)((var3 - 46.0 + this.scroll) / 22.0);
         return var8 >= 0 && var8 < this.tasks.size() ? var8 : -1;
      } else {
         return -1;
      }
   }

   public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
      if (var1.button() == 0) {
         int var3 = this.hoveredRow(var1.x(), var1.y(), 24, this.width - 48, this.height - 44);
         if (var3 >= 0) {
            this.selected = var3;
            return true;
         }

         if (this.selected >= 0 && this.isOverButton(var1.x(), var1.y())) {
            ClientPacketDistributor.sendToServer(new HexTaskPickPayload(this.tasks.get(this.selected).index()), new CustomPacketPayload[0]);
            this.onClose();
            return true;
         }
      }

      return super.mouseClicked(var1, var2);
   }

   public boolean mouseScrolled(double var1, double var3, double var5, double var7) {
      this.scroll = Math.max(0, this.scroll - (int)(var7 * 22.0 * 2.0));
      return true;
   }

   private boolean isOverButton(double var1, double var3) {
      int var5 = this.buttonX();
      int var6 = this.buttonY();
      return var1 >= var5 && var1 < var5 + 100 && var3 >= var6 && var3 < var6 + 18;
   }

   private int buttonX() {
      return (this.width - 100) / 2;
   }

   private int buttonY() {
      return this.height - 34;
   }
}
