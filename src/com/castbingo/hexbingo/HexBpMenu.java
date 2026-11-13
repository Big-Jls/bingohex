/*
 * Copyright (c) 2026 Big_Jls and DeepSeek-v4.1-Flash-1017.
 * Licensed under the Apache License, Version 2.0.
 * See LICENSE and NOTICE.
 */
package com.castbingo.hexbingo;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

/**
 * 团队背包扩容（72 格）菜单：8 行团队背包 + 3 行玩家背包 + 1 行快捷栏。
 * 服务端通过 {@link Provider} 打开并把背包内容写进打开界面封包；客户端用收到的副本渲染。
 */
public class HexBpMenu extends AbstractContainerMenu {
   private final ItemStackHandler handler;
   private final Runnable dirty;
   private final ItemStack[] snapshot;

   public HexBpMenu(int var0, Inventory var1, ItemStackHandler var2, Runnable var3) {
      super(HexMenus.TEAM_BP.get(), var0);
      this.handler = var2;
      this.dirty = var3;
      this.snapshot = var2 == null ? new ItemStack[0] : new ItemStack[var2.getSlots()];
      if (var2 != null) {
         for (int var4 = 0; var4 < this.snapshot.length; var4++) {
            this.snapshot[var4] = var2.getStackInSlot(var4).copy();
         }

         for (int var5 = 0; var5 < 8; var5++) {
            for (int var6 = 0; var6 < 9; var6++) {
               this.addSlot(new SlotItemHandler(var2, var5 * 9 + var6, 8 + 18 * var6, 17 + 18 * var5));
            }
         }
      }

      for (int var5 = 0; var5 < 3; var5++) {
         for (int var6 = 0; var6 < 9; var6++) {
            this.addSlot(new Slot(var1, var6 + var5 * 9 + 9, 8 + 18 * var6, 170 + 18 * var5));
         }
      }

      for (int var7 = 0; var7 < 9; var7++) {
         this.addSlot(new Slot(var1, var7, 8 + 18 * var7, 228));
      }
   }

   /** 客户端侧：该版本打开界面封包不携带额外数据，先建 72 个空槽，内容由容器同步包填充。 */
   public static HexBpMenu createFromNetwork(int var0, Inventory var1, RegistryFriendlyByteBuf var2) {
      return new HexBpMenu(var0, var1, new ItemStackHandler(72), null);
   }

   /** 服务端每 tick 检测团队背包内容变化：ReBongo 的 SavedData 需要 setDirty 才会落盘，否则重启后物品丢失。 */
   @Override
   public void broadcastChanges() {
      if (this.dirty != null && this.handler != null) {
         boolean var1 = false;

         for (int var2 = 0; var2 < this.snapshot.length && var2 < this.handler.getSlots(); var2++) {
            ItemStack var3 = this.handler.getStackInSlot(var2);
            if (!ItemStack.matches(var3, this.snapshot[var2])) {
               this.snapshot[var2] = var3.copy();
               var1 = true;
            }
         }

         if (var1) {
            this.dirty.run();
         }
      }

      super.broadcastChanges();
   }

   @Override
   public ItemStack quickMoveStack(Player var0, int var1) {
      ItemStack var2 = ItemStack.EMPTY;
      Slot var3 = this.slots.get(var1);
      if (var3 != null && var3.hasItem()) {
         ItemStack var4 = var3.getItem();
         var2 = var4.copy();
         if (var1 < 72) {
            if (!this.moveItemStackTo(var4, 72, 108, true)) {
               return ItemStack.EMPTY;
            }
         } else if (!this.moveItemStackTo(var4, 0, 72, false)) {
            return ItemStack.EMPTY;
         }

         if (var4.isEmpty()) {
            var3.setByPlayer(ItemStack.EMPTY);
         } else {
            var3.setChanged();
         }
      }

      return var2;
   }

   @Override
   public boolean stillValid(Player var0) {
      return true;
   }

   /** 服务端菜单提供器：直接持有队伍背包本身，内容变化时通知 Bingo 落盘。 */
   public static final class Provider implements MenuProvider {
      private final ItemStackHandler handler;
      private final Component team;
      private final Runnable dirty;

      public Provider(ItemStackHandler var1, Component var2, Runnable var3) {
         this.handler = var1;
         this.team = var2;
         this.dirty = var3;
      }

      @Override
      public Component getDisplayName() {
         return Component.translatable("bongo.backpack").append(this.team);
      }

      @Override
      public AbstractContainerMenu createMenu(int var1, Inventory var2, Player var3) {
         return new HexBpMenu(var1, var2, this.handler, this.dirty);
      }
   }
}
