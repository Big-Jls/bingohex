/*
 * Copyright (c) 2026 Big_Jls and DeepSeek-v4.1-Flash-1017.
 * Licensed under the Apache License, Version 2.0.
 * See LICENSE and NOTICE.
 */
package com.castbingo.hexbingo.client;

import io.github.noeppi_noeppi.mods.bongo.Bingo;
import io.github.noeppi_noeppi.mods.bongo.Keybinds;
import io.github.noeppi_noeppi.mods.bongo.config.ClientConfig;
import io.github.noeppi_noeppi.mods.bongo.data.Team;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.gui.GuiLayer;

/** 封印海克斯：在被封印的 Bingo 格子上画一层屏障图标（复刻 ReBongo 卡面位置计算）。 */
public final class HexSealOverlay implements GuiLayer {
   public static final HexSealOverlay INSTANCE = new HexSealOverlay();
   private static final Identifier BARRIER_TEXTURE = Identifier.fromNamespaceAndPath("minecraft", "textures/item/barrier.png");
   private static final int SLOTS = 5;

   private HexSealOverlay() {
   }

   @Override
   public void render(GuiGraphicsExtractor var0, DeltaTracker var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var2.level == null || var2.player == null) {
         return;
      }

      Bingo var3 = Bingo.get(var2.level);
      Team var4 = var3.getTeam(var2.player);
      if (!var3.active() || var3.running() && var4 == null) {
         return;
      }

      int var5 = var0.guiWidth();
      int var6 = var0.guiHeight();
      double var7 = 5.0;
      double var8 = (double)var6 / 3.5;
      double var9 = var7;
      double var10 = var7;
      if (Keybinds.BIG_OVERLAY.isDown()) {
         var8 = Math.min((double)var5 - 2.0 * var7, (double)var6 - 6.0 * var7);
         var9 = ((double)var5 - var8) / 2.0;
         var10 = ((double)var6 - var8) / 2.0 - 2.0 * var7;
      } else {
         var8 *= (Double)ClientConfig.bongoMapScaleFactor.get();
         if ((Boolean)ClientConfig.bongoMapOnTheRight.get()) {
            var9 = (double)var5 - var7 - var8;
         }
      }

      var0.pose().pushMatrix();
      var0.pose().translate((float)var9, (float)var10);
      var0.pose().scale((float)(var8 / 138.0), (float)(var8 / 138.0));

      for (int var11 = 0; var11 < var3.tasks().size(); var11++) {
         if (HexClient.isSealed(var11)) {
            int var12 = var11 % SLOTS;
            int var13 = var11 / SLOTS;
            var0.pose().pushMatrix();
            var0.pose().translate((float)(2 + 27 * var12), (float)(2 + 27 * var13));
            var0.pose().scale(1.65F, 1.65F);
            var0.blit(RenderPipelines.GUI_TEXTURED, BARRIER_TEXTURE, 0, 0, 0.0F, 0.0F, 16, 16, 16, 16);
            var0.pose().popMatrix();
         }
      }

      var0.pose().popMatrix();
   }
}
