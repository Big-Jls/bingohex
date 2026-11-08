/*
 * Copyright (c) 2026 Big_Jls and DeepSeek-v4.1-Flash-1017.
 * Licensed under the Apache License, Version 2.0.
 * See LICENSE and NOTICE.
 */
package com.castbingo.hexbingo;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;
import net.neoforged.neoforge.common.ModConfigSpec.Builder;
import net.neoforged.neoforge.common.ModConfigSpec.IntValue;

public final class HexConfig {
   public static final ModConfigSpec SPEC;
   public static final BooleanValue FIRST_REWARD_ON_LINE;
   public static final IntValue SECOND_MINUTES;
   public static final IntValue THIRD_MINUTES;
   public static final IntValue FOURTH_MINUTES;
   public static final BooleanValue ANNOUNCE;

   private HexConfig() {
   }

   static {
      Builder var0 = new Builder();
      var0.comment("Bingo: Hex Mayhem settings");
      var0.push("mayhem");
      FIRST_REWARD_ON_LINE = var0.comment(
            "If true the first hex is granted when a team completes their first bingo line. If false it is granted on their first completed task."
         )
         .define("first_reward_on_line", false);
      SECOND_MINUTES = var0.comment("Minutes after the game started when the second hex reward is granted.").defineInRange("second_minutes", 5, 1, 600);
      THIRD_MINUTES = var0.comment("Minutes after the game started when the third hex reward is granted.").defineInRange("third_minutes", 10, 1, 600);
      FOURTH_MINUTES = var0.comment("Minutes after the game started when the fourth hex reward is granted. The fourth reward is always pure prismatic.")
         .defineInRange("fourth_minutes", 15, 1, 600);
      ANNOUNCE = var0.comment("Announce chosen hexes to the team.").define("announce", true);
      var0.pop();
      SPEC = var0.build();
   }
}
