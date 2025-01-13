package com.runtracker.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class Activity {
   private Long id;
   private User pengguna;
   private String title;
   private LocalDate date;
   private String description;
   private Double duration;
   private Double distance;
   private String image;
   private String type;
   private Long createdAt;
   private Long updatedAt;

   private MemberRace memberRace;
}
