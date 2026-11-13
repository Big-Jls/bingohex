/*
 * Copyright (c) 2026 Big_Jls and DeepSeek-v4.1-Flash-1017.
 * Licensed under the Apache License, Version 2.0.
 * See LICENSE and NOTICE.
 */
package com.castbingo.hexbingo;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/** 团队背包扩容（72 格）的自定义菜单类型注册。 */
public final class HexMenus {
   public static final DeferredRegister<MenuType<?>> REGISTER = DeferredRegister.create(Registries.MENU, "hexbingo");
   public static final DeferredHolder<MenuType<?>, MenuType<HexBpMenu>> TEAM_BP = REGISTER.register(
      "team_bp_72",
      () -> IMenuTypeExtension.create(HexBpMenu::createFromNetwork)
   );

   private HexMenus() {
   }
}
