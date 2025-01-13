package com.runtracker.controllers;

import com.runtracker.models.User;
import com.runtracker.services.UserService;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {

   @Autowired
   private final UserService userService;

   public UserController(UserService userService) {
      this.userService = userService;
   }

   @GetMapping("/signup")
   public String renderSignupPage() {
      return "signup";
   }

   @GetMapping("/signin")
   public String renderSigninPage() {
      return "signin";
   }

   @GetMapping("/profile")
   public String renderProfilePage(HttpSession session, Model model) {
      User user = (User) session.getAttribute("dataSession");

      if (user == null) {
         return "redirect:/user/signin";
      }

      model.addAttribute("title", "User Profile");
      model.addAttribute("page", "profile");
      model.addAttribute("user", user);

      return "index";
   }

   @PostMapping("/signin")
   public String signin(@RequestParam String email, @RequestParam String password, HttpSession session, Model model) {
      try {
         User user = userService.authenticate(email, password);

         session.setAttribute("dataSession", user);

         return "redirect:/dashboard";
      } catch (IllegalArgumentException e) {
         model.addAttribute("error", e.getMessage());
         return "signin";
      }
   }

   @PostMapping("/signup")
   public String signup(@ModelAttribute User user, Model model) {
      try {
         if (userService.isEmailTaken(user.getEmail())) {
            model.addAttribute("error", "Email is already in use.");
            return "signup";
         }

         userService.createUser(user);
         return "redirect:/user/signin";
      } catch (IllegalArgumentException e) {
         return "signup";
      }

   }

   @PostMapping("/signout")
   public String signout(HttpSession session) {
      session.invalidate();
      return "redirect:/user/signin";
   }

   @PutMapping("/profile")
   public ResponseEntity<String> updateProfile(
         @RequestParam String name,
         @RequestParam String email,
         @RequestParam("current-password") String currentPassword,
         @RequestParam("new-password") String newPassword,
         HttpSession session) {
      User sessionUser = (User) session.getAttribute("dataSession");
      if (sessionUser == null) {
         return ResponseEntity.status(403).body("Unauthorized");
      }

      boolean isUpdated = userService.updateProfile(sessionUser.getId(), name, email, currentPassword, newPassword);
      if (isUpdated) {
         return ResponseEntity.ok("Profile updated successfully");
      } else {
         return ResponseEntity.badRequest().body("Update failed. Invalid current password or other issue.");
      }
   }

   @DeleteMapping("/profile")
   public ResponseEntity<String> deleteAccount(HttpSession session) {
      User sessionUser = (User) session.getAttribute("dataSession");
      if (sessionUser == null) {
         return ResponseEntity.status(403).body("Unauthorized");
      }

      userService.deleteUser(sessionUser.getId());
      session.invalidate();
      return ResponseEntity.ok("Account deleted successfully");
   }
}
