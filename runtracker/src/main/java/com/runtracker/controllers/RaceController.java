package com.runtracker.controllers;

import com.runtracker.models.Race;
import com.runtracker.models.User;
import com.runtracker.services.RaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/race")
public class RaceController {

   @Autowired
   private RaceService raceService;

   // Menampilkan halaman Race
   @GetMapping
   public String renderRacePage(HttpSession session, Model model) {
      User user = (User) session.getAttribute("dataSession");

      if (user == null) {
         return "redirect:/user/signin";
      }

      List<Race> races = raceService.getRacesByCreator(user);

      String page = "race";
      if ("ADMIN".equals(user.getRole())) {
         page = "admin-race";
      }

      model.addAttribute("title", "Races");
      model.addAttribute("page", page);
      model.addAttribute("races", races);

      return "index";
   }

   // Menambah Race
   @PostMapping
   public ResponseEntity<String> createRace(@ModelAttribute Race race, HttpSession session) {
      User user = (User) session.getAttribute("dataSession");

      if (user == null) {
         return ResponseEntity.status(403).body("Unauthorized");
      }

      race.setCreator(user);
      raceService.createRace(race);
      return ResponseEntity.ok("Race created successfully");
   }

   // Mengubah Race
   @PutMapping("/{id}")
   public ResponseEntity<String> updateRace(@PathVariable Long id, @ModelAttribute Race updatedRace,
         HttpSession session) {
      User user = (User) session.getAttribute("dataSession");

      if (user == null) {
         return ResponseEntity.status(403).body("Unauthorized");
      }

      updatedRace.setUpdator(user);
      raceService.updateRace(id, updatedRace);
      return ResponseEntity.ok("Race updated successfully");
   }

   // Menghapus Race
   @DeleteMapping("/{id}")
   public ResponseEntity<String> deleteRace(@PathVariable Long id, HttpSession session) {
      User user = (User) session.getAttribute("dataSession");

      if (user == null) {
         return ResponseEntity.status(403).body("Unauthorized");
      }

      raceService.deleteRace(id);
      return ResponseEntity.ok("Race deleted successfully");
   }
}
