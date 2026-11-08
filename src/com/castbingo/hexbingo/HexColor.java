/*
 * Copyright (c) 2026 Big_Jls and DeepSeek-v4.1-Flash-1017.
 * Licensed under the Apache License, Version 2.0.
 * See LICENSE and NOTICE.
 */
package com.castbingo.hexbingo;

public enum HexColor {
   SILVER("silver", -4144960),
   GOLD("gold", -10496),
   PRISMATIC("prismatic", -43521);

   public final String id;
   public final int rgb;

   HexColor(String nullxx, int nullxxx) {
      this.id = nullxx;
      this.rgb = nullxxx;
   }

   public static HexColor byId(String var0) {
      for (HexColor var4 : values()) {
         if (var4.id.equals(var0)) {
            return var4;
         }
      }

      return SILVER;
   }
}
