package com.runtracker.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.runtracker.models.User;

import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class IndexController {

   // Render halaman home
   @GetMapping("/")
   public String halaman() {
      return "redirect:/home";
   }

   // Render halaman home
   @GetMapping("/home")
   public String renderHomePage(Model model) {
      model.addAttribute("title", "Home"); // Judul halaman
      model.addAttribute("page", "home"); // Nama halaman
      return "index";
   }

   // Render halaman dashboard member
   @GetMapping("/dashboard")
   public String renderDashboardPage(Model model, HttpSession session) {
      User user = (User) session.getAttribute("dataSession");

      if (user == null) {
         return "redirect:/user/signin";
      }
      String page = "dashboard";
      if ("ADMIN".equals(user.getRole())) {
         page = "admin-dashboard";
      }

      model.addAttribute("title", "Dashboard"); // Judul halaman
      model.addAttribute("page", page); // Nama halaman
      return "index";
   }
}
