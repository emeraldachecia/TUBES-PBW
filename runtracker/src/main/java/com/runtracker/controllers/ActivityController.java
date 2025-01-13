package com.runtracker.controllers;

import com.runtracker.models.Activity;
import com.runtracker.models.User;
import com.runtracker.services.ActivityService;
import jakarta.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
public class ActivityController {

   private final ActivityService activityService;

   public ActivityController(ActivityService activityService) {
      this.activityService = activityService;
   }

   // Render halaman activity
   @GetMapping("/activity")
   public String renderActivityPage(HttpSession session, Model model) {
      User user = (User) session.getAttribute("dataSession");
      if (user == null) {
         return "redirect:/user/signin";
      }

      List<Activity> activities = activityService.getActivitiesByUser(user);

      model.addAttribute("title", "Activities");
      model.addAttribute("page", "activity");
      model.addAttribute("activities", activities);

      return "index";
   }

   // Render halaman activity
   @GetMapping("/activity/{id}")
   public ResponseEntity<?> getActivityDetail(@PathVariable Long id, HttpSession session) {
      User user = (User) session.getAttribute("dataSession");
      if (user == null) {
         return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized");
      }

      Activity activity = activityService.getActivityDetail(id);
      return ResponseEntity.ok(activity);
   }

   // Tambah activity baru
   @PostMapping("/activity")
   public String addActivity(@ModelAttribute Activity activity,
         @RequestParam("fileImage") MultipartFile file,
         HttpSession session) {
      User user = (User) session.getAttribute("dataSession");

      if (user == null) {
         return "redirect:/user/signin";
      }

      if (file.isEmpty()) {
         return "redirect:/activity";
      }

      try {
         // Save the file to the server
         byte[] bytes = file.getBytes();
         Path path = Paths.get("runtracker/src/main/resources/static/image/" + file.getOriginalFilename());
         Files.write(path, bytes);

         // Set the filename to the image field
         activity.setImage(file.getOriginalFilename());
      } catch (IOException e) {
         e.printStackTrace();
         return "redirect:/activity";
      }

      activity.setPengguna(user);
      
      activityService.createActivity(activity);

      return "redirect:/activity";
   }

   // Ubah activity
   @PutMapping("/activity/{id}")
   public ResponseEntity<String> updateActivity(@PathVariable Long id, @ModelAttribute Activity updatedActivity,
         HttpSession session) {
      User user = (User) session.getAttribute("dataSession");
      if (user == null) {
         return ResponseEntity.status(403).body("Unauthorized");
      }

      activityService.updateActivity(id, updatedActivity);
      return ResponseEntity.ok("Activity updated successfully");
   }

   // Hapus activity
   @DeleteMapping("/activity/{id}")
   public ResponseEntity<String> deleteActivity(@PathVariable Long id, HttpSession session) {
      User user = (User) session.getAttribute("dataSession");
      if (user == null) {
         return ResponseEntity.status(403).body("Unauthorized");
      }

      activityService.deleteActivity(id);
      return ResponseEntity.ok("Activity deleted successfully");
   }
}
