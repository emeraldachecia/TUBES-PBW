package com.runtracker.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberRace {
   private User member;
   private Race race;
   private Activity activity;
   private Long createdAt;
   private Long updatedAt;
}
