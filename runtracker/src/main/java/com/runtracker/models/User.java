package com.runtracker.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class User {
   private Long id;
   private String name;
   private String email;
   private String password;
   private String role;
   private Long createdAt;
   private Long updatedAt;

   private Set<Activity> activities;
   private Set<Race> createdRaces;
   private Set<Race> updatedRaces;
   private Set<MemberRace> memberRaces;
}
