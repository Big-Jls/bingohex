/*
 * Copyright (c) 2026 Big_Jls and DeepSeek-v4.1-Flash-1017.
 * Licensed under the Apache License, Version 2.0.
 * See LICENSE and NOTICE.
 */
package com.castbingo.hexbingo;

import com.mojang.serialization.MapCodec;
import io.github.noeppi_noeppi.mods.bongo.render.RenderOverlay;
import io.github.noeppi_noeppi.mods.bongo.task.TaskType;
import io.github.noeppi_noeppi.mods.bongo.util.ItemRenderUtil;
import java.util.Comparator;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public final class EnchantTaskType implements TaskType<Identifier> {
   public static final EnchantTaskType INSTANCE = new EnchantTaskType();
   public static final Identifier DUMMY = Identifier.fromNamespaceAndPath("minecraft", "sharpness");

   private EnchantTaskType() {
   }

   public String id() {
      return "bongo.enchantment";
   }

   public Class<Identifier> taskClass() {
      return Identifier.class;
   }

   public MapCodec<Identifier> codec() {
      return Identifier.CODEC.fieldOf("value");
   }

   public Component name() {
      return Component.translatable("bingo.task.enchantment.name");
   }

   public Component contentName(Identifier var1, @Nullable MinecraftServer var2) {
      return Component.translatable("enchantment." + var1.getNamespace() + "." + var1.getPath());
   }

   public Comparator<Identifier> order() {
      return Comparator.comparing(Identifier::toString);
   }

   public void validate(Identifier var1, MinecraftServer var2) {
      if (var2.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getValue(var1) == null) {
         throw new IllegalStateException("Unknown enchantment: " + var1);
      }
   }

   public Stream<Identifier> listElements(MinecraftServer var1, @Nullable ServerPlayer var2) {
      return var1.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).keySet().stream();
   }

   public boolean shouldComplete(ServerPlayer var1, Identifier var2, Identifier var3) {
      Registry var4 = var1.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
      Enchantment var5 = (Enchantment)var4.getValue(var2);
      if (var5 == null) {
         return false;
      }

      Holder var6 = var4.wrapAsHolder(var5);

      for (ItemStack var8 : var1.getInventory().getNonEquipmentItems()) {
         if (!var8.isEmpty() && EnchantmentHelper.getItemEnchantmentLevel(var6, var8) > 0) {
            return true;
         }
      }

      for (EquipmentSlot var10 : EquipmentSlot.values()) {
         ItemStack var11 = var1.getItemBySlot(var10);
         if (!var11.isEmpty() && EnchantmentHelper.getItemEnchantmentLevel(var6, var11) > 0) {
            return true;
         }
      }

      return false;
   }

   @OnlyIn(Dist.CLIENT)
   public void renderSlot(Minecraft var1, GuiGraphicsExtractor var2) {
      ItemRenderUtil.blit(var2, RenderOverlay.BINGO_SLOTS_TEXTURE, 0, 0, 0, 0, 18, 18, 256, 256);
   }

   @OnlyIn(Dist.CLIENT)
   public void renderSlotContent(Minecraft var1, GuiGraphicsExtractor var2, Identifier var3, boolean var4) {
      ItemRenderUtil.renderItem(var2, new ItemStack(Items.ENCHANTED_BOOK), !var4);
   }
}
