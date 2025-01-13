package com.runtracker.services;

import com.runtracker.dao.UserDAO;
import com.runtracker.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

   @Autowired
   private UserDAO userDAO;

   // Using BCryptPasswordEncoder to hash and verify passwords
   private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

   public User authenticate(String email, String password) {
      Optional<User> userOpt = userDAO.findByEmail(email);
      if (userOpt.isEmpty() || !passwordEncoder.matches(password, userOpt.get().getPassword())) {
         throw new IllegalArgumentException("Invalid email or password.");
      }
      return userOpt.get();
   }

   public boolean isEmailTaken(String email) {
      return userDAO.findByEmail(email).isPresent();
   }

   public void createUser(User user) {
      if (isEmailTaken(user.getEmail())) {
         throw new IllegalArgumentException("Email is already in use.");
      }
      // Hashing the password before saving the user
      user.setPassword(passwordEncoder.encode(user.getPassword()));
      userDAO.save(user);
   }

   public boolean updateProfile(Long userId, String name, String email, String currentPassword, String newPassword) {
      User existingUser = userDAO.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

      if (!passwordEncoder.matches(currentPassword, existingUser.getPassword())) {
         return false; // Current password does not match
      }

      existingUser.setName(name);
      existingUser.setEmail(email);

      if (newPassword != null && !newPassword.isEmpty()) {
         existingUser.setPassword(passwordEncoder.encode(newPassword));
      }

      userDAO.update(existingUser);
      return true;
   }

   public void deleteUser(Long userId) {
      if (!userDAO.findById(userId).isPresent()) {
         throw new IllegalArgumentException("User not found");
      }
      userDAO.delete(userId);
   }

   public Optional<User> getUserByEmail(String email) {
      return userDAO.findByEmail(email);
   }
}
