package com.runtracker.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Data
@NoArgsConstructor
public class Race {
   private Long id;
   private User creator;
   private User updator;
   private String name;
   private String description;
   private LocalDate date;
   private LocalTime time;
   private String status;
   private Long createdAt;
   private Long updatedAt;

   private Set<MemberRace> memberRaces;

   public void setStatus(String status) {
      if (!status.equals("upcoming") && !status.equals("ongoing") && !status.equals("finished")) {
         throw new IllegalArgumentException("Invalid status value");
      }
      this.status = status;
   }
}
