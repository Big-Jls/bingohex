/*
 * Copyright (c) 2026 Big_Jls and DeepSeek-v4.1-Flash-1017.
 * Licensed under the Apache License, Version 2.0.
 * See LICENSE and NOTICE.
 */
package com.castbingo.hexbingo.client;

import com.castbingo.hexbingo.network.HexTaskListPayload;
import com.castbingo.hexbingo.network.HexTaskPickPayload;
import io.github.noeppi_noeppi.mods.bongo.Bingo;
import io.github.noeppi_noeppi.mods.bongo.data.Team;
import io.github.noeppi_noeppi.mods.bongo.render.RenderOverlay;
import io.github.noeppi_noeppi.mods.bongo.task.Task;
import io.github.noeppi_noeppi.mods.bongo.util.ItemRenderUtil;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

/**
 * 目标封锁 / 空白格：按 Bingo 大地图（快捷键 Y）的样子放大当前卡牌，
 * 每个格子可直接点击选择；客户端没有卡牌数据时退回文字列表。
 */
public class HexTaskScreen extends Screen {
   private static final int CARD_SIZE = 138;
   private static final int CELL = 27;
   private static final int ROW_H = 22;
   private final String mode;
   private final List<HexTaskListPayload.Entry> tasks;
   private final Set<Integer> allowed = new HashSet<>();
   private int selected = -1;
   private int scroll;
   private int hoveredRow = -1;
   private int hoveredSlot = -1;
   private double cardX;
   private double cardY;
   private double cardScale = 1.0;

   public HexTaskScreen(HexTaskListPayload var1) {
      super(Component.translatable("hexbingo.taskpick.title"));
      this.mode = var1.mode();
      this.tasks = new ArrayList<>(var1.tasks());

      for (HexTaskListPayload.Entry var3 : var1.tasks()) {
         this.allowed.add(var3.index());
      }
   }

   public boolean isPauseScreen() {
      return false;
   }

   public void extractRenderState(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      var1.fill(0, 0, this.width, this.height, -535818216);
      Component var5 = Component.translatable("blank".equals(this.mode) ? "hexbingo.blank.title" : "hexbingo.deny.title");
      var1.centeredText(this.font, var5, this.width / 2, 10, -10496);
      var1.centeredText(
         this.font,
         Component.translatable("hexbingo.taskpick.subtitle", Component.literal(Integer.toString(this.allowed.size()))),
         this.width / 2,
         24,
         -4144960
      );
      Bingo var6 = this.bingo();
      boolean var7 = var6 != null && var6.active() && var6.tasks().size() >= 25;
      if (var7) {
         this.drawCard(var1, var6, var2, var3);
      } else {
         this.drawList(var1, var2, var3);
      }

      this.drawFooter(var1, var2, var3);
   }

   private Bingo bingo() {
      return this.minecraft.level == null ? null : Bingo.get(this.minecraft.level);
   }

   private void drawCard(GuiGraphicsExtractor var1, Bingo var2, int var3, int var4) {
      double var5 = Math.min(this.width - 40, this.height - 110);
      double var7 = Math.max(150.0, Math.min(420.0, var5));
      this.cardScale = var7 / 138.0;
      this.cardX = (this.width - var7) / 2.0;
      this.cardY = 38.0 + Math.max(0.0, this.height - 110 - var7) / 2.0;
      this.hoveredSlot = this.slotAt(var3, var4);
      Team var9 = this.minecraft.player == null ? null : var2.getTeam(this.minecraft.player);
      var1.pose().pushMatrix();
      var1.pose().translate((float)this.cardX, (float)this.cardY);
      var1.pose().scale((float)this.cardScale, (float)this.cardScale);
      ItemRenderUtil.blit(var1, RenderOverlay.BINGO_TEXTURE, 0, 0, 0, 0, 138, 138, 256, 256);

      for (int var10 = 0; var10 < 25; var10++) {
         int var11 = var10 % 5;
         int var12 = var10 / 5;
         var1.pose().pushMatrix();
         var1.pose().translate(6 + 27 * var11, 6 + 27 * var12);
         var2.task(var10).renderSlot(this.minecraft, var1);
         var1.pose().popMatrix();
      }

      for (int var27 = 0; var27 < 25; var27++) {
         int var28 = var27 % 5;
         int var29 = var27 / 5;
         var1.pose().pushMatrix();
         var1.pose().translate(7 + 27 * var28, 7 + 27 * var29);
         var2.task(var27).renderSlotContent(this.minecraft, var1, true);
         var1.pose().popMatrix();
      }

      for (int var30 = 0; var30 < 25; var30++) {
         int var31 = 2 + 27 * (var30 % 5);
         int var32 = 2 + 27 * (var30 / 5);
         if (!this.allowed.contains(var30)) {
            var1.fill(var31, var32, var31 + 25, var32 + 25, 0x99000000);
         } else if (var9 != null && var9.completed(var30)) {
            var1.fill(var31, var32, var31 + 25, var32 + 25, 0x5533FF33);
         } else if (var9 != null && var9.locked(var30)) {
            var1.fill(var31, var32, var31 + 25, var32 + 25, 0x55FF3333);
         }
      }

      if (this.selected >= 0) {
         this.drawBorder(var1, this.selected, -171, 2);
      }

      if (this.hoveredSlot >= 0) {
         this.drawBorder(var1, this.hoveredSlot, this.allowed.contains(this.hoveredSlot) ? -1 : -65536, 1);
      }

      var1.pose().popMatrix();
   }

   private void drawBorder(GuiGraphicsExtractor var1, int var2, int var3, int var4) {
      int var5 = 2 + 27 * (var2 % 5);
      int var6 = 2 + 27 * (var2 / 5);
      int var7 = var5 + 27;
      int var8 = var6 + 27;
      var1.fill(var5, var6, var7, var6 + var4, var3);
      var1.fill(var5, var8 - var4, var7, var8, var3);
      var1.fill(var5, var6, var5 + var4, var8, var3);
      var1.fill(var7 - var4, var6, var7, var8, var3);
   }

   private int slotAt(double var1, double var3) {
      double var5 = (var1 - this.cardX) / this.cardScale;
      double var7 = (var3 - this.cardY) / this.cardScale;
      if (!(var5 < 2.0) && !(var7 < 2.0) && var5 < 137.0 && var7 < 137.0) {
         int var9 = Math.min(4, (int)((var5 - 2.0) / 27.0));
         int var10 = Math.min(4, (int)((var7 - 2.0) / 27.0));
         return var9 + var10 * 5;
      } else {
         return -1;
      }
   }

   private void drawList(GuiGraphicsExtractor var1, int var2, int var3) {
      int var5 = 24;
      int var6 = this.width - 48;
      int var7 = this.height - 84;
      int var8 = var7 - 46;
      int var9 = this.tasks.size() * 22;
      int var10 = Math.max(0, var9 - var8);
      this.scroll = Math.max(0, Math.min(this.scroll, var10));
      this.hoveredRow = this.hoveredRow(var2, var3, var5, var6, var7);
      var1.enableScissor(var5, 46, var5 + var6, var7);

      for (int var11 = 0; var11 < this.tasks.size(); var11++) {
         int var12 = 46 + var11 * 22 - this.scroll;
         if (var12 + 22 >= 46 && var12 <= var7) {
            HexTaskListPayload.Entry var13 = this.tasks.get(var11);
            boolean var14 = var11 == this.hoveredRow;
            boolean var15 = var13.index() == this.selected;
            int var16 = var15 ? -13882310 : (var14 ? -14935012 : -15198176);
            var1.fill(var5, var12, var5 + var6, var12 + 20, var16);
            var1.fill(var5, var12, var5 + 3, var12 + 20, -43691);
            var1.text(this.font, var13.name(), var5 + 10, var12 + 7, var15 || var14 ? -1 : -2039584, false);
         }
      }

      var1.disableScissor();
      if (var9 > var8) {
         int var18 = var5 + var6 + 4;
         var1.fill(var18, 46, var18 + 4, var7, -15198176);
         int var19 = Math.max(16, (int)((float)(var8 * var8) / var9));
         int var20 = 46 + (int)((float)(var8 - var19) * this.scroll / Math.max(1, var10));
         var1.fill(var18, var20, var18 + 4, var20 + var19, -8355712);
      }

      this.hoveredSlot = -1;
   }

   private void drawFooter(GuiGraphicsExtractor var1, int var2, int var3) {
      int var5 = this.selected >= 0 ? this.selected : this.hoveredSlot;
      Bingo var6 = this.bingo();
      if (var5 >= 0 && var6 != null && var5 < var6.tasks().size()) {
         Task var7 = var6.task(var5);
         var1.centeredText(this.font, var7.typeName(), this.width / 2, this.height - 56, -4144960);
         var1.centeredText(this.font, var7.renderDisplayName(this.minecraft), this.width / 2, this.height - 44, -1);
      } else {
         var1.centeredText(this.font, Component.translatable("hexbingo.taskpick.hint"), this.width / 2, this.height - 48, -8355712);
      }

      this.drawConfirmButton(var1, var2, var3);
      var1.centeredText(this.font, Component.translatable("hexbingo.taskpick.close"), this.width / 2, this.height - 12, -8355712);
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
         Bingo var3 = this.bingo();
         if (var3 != null && var3.active() && var3.tasks().size() >= 25) {
            int var4 = this.slotAt(var1.x(), var1.y());
            if (var4 >= 0) {
               if (this.allowed.contains(var4)) {
                  this.selected = var4;
               }

               return true;
            }
         } else {
            int var5 = this.hoveredRow(var1.x(), var1.y(), 24, this.width - 48, this.height - 84);
            if (var5 >= 0) {
               this.selected = this.tasks.get(var5).index();
               return true;
            }
         }

         if (this.selected >= 0 && this.isOverButton(var1.x(), var1.y())) {
            ClientPacketDistributor.sendToServer(new HexTaskPickPayload(this.selected), new CustomPacketPayload[0]);
            this.onClose();
            return true;
         }
      }

      return super.mouseClicked(var1, var2);
   }

   public boolean mouseScrolled(double var1, double var3, double var5, double var7) {
      Bingo var9 = this.bingo();
      if (var9 == null || !var9.active() || var9.tasks().size() < 25) {
         this.scroll = Math.max(0, this.scroll - (int)(var7 * 22.0 * 2.0));
      }

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
      return this.height - 32;
   }
}
