/*
 * Copyright (c) 2026 Big_Jls and DeepSeek-v4.1-Flash-1017.
 * Licensed under the Apache License, Version 2.0.
 * See LICENSE and NOTICE.
 */
package com.castbingo.hexbingo.client;

import com.castbingo.hexbingo.HexBpMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

/** 团队背包扩容（72 格）屏幕：8 行团队背包 + 玩家背包，纯填充绘制背景。 */
public class HexBpScreen extends AbstractContainerScreen<HexBpMenu> {
   private static final int IMAGE_WIDTH = 176;
   private static final int IMAGE_HEIGHT = 253;

   public HexBpScreen(HexBpMenu var1, Inventory var2, Component var3) {
      super(var1, var2, var3, IMAGE_WIDTH, IMAGE_HEIGHT);
      this.inventoryLabelX = 8;
      this.inventoryLabelY = 166;
   }

   @Override
   public void extractRenderState(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      this.drawBackground(var1);
      super.extractRenderState(var1, var2, var3, var4);
   }

   private void drawBackground(GuiGraphicsExtractor var1) {
      int var2 = this.leftPos;
      int var3 = this.topPos;
      // 面板底色与边框
      var1.fill(var2, var3, var2 + IMAGE_WIDTH, var3 + IMAGE_HEIGHT, 0xFFC6C6C6);
      var1.fill(var2, var3, var2 + IMAGE_WIDTH, var3 + 4, 0xFF373737);
      var1.fill(var2, var3 + IMAGE_HEIGHT - 4, var2 + IMAGE_WIDTH, var3 + IMAGE_HEIGHT, 0xFF373737);
      var1.fill(var2, var3, var2 + 4, var3 + IMAGE_HEIGHT, 0xFF373737);
      var1.fill(var2 + IMAGE_WIDTH - 4, var3, var2 + IMAGE_WIDTH, var3 + IMAGE_HEIGHT, 0xFF373737);

      // 每个槽位画一个暗色凹槽
      for (Slot var5 : this.menu.slots) {
         var1.fill(var2 + var5.x - 1, var3 + var5.y - 1, var2 + var5.x + 17, var3 + var5.y + 17, 0xFF8B8B8B);
      }
   }
}
