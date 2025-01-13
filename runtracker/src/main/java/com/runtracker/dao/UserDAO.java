package com.runtracker.dao;

import com.runtracker.config.DatabaseConfig;
import com.runtracker.models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public class UserDAO extends DatabaseConfig {

   public Optional<User> findById(Long id) {
      String sql = "SELECT * FROM pengguna WHERE pengguna_id = ?";
      try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

         stmt.setLong(1, id);
         ResultSet rs = stmt.executeQuery();

         if (rs.next()) {
            return Optional.of(mapRowToUser(rs));
         }
      } catch (SQLException e) {
         e.printStackTrace();
      }
      return Optional.empty();
   }

   public Optional<User> findByEmail(String email) {
      String sql = "SELECT * FROM pengguna WHERE email = ?";
      try (Connection conn = getConnection();


      
            PreparedStatement stmt = conn.prepareStatement(sql)) {

         stmt.setString(1, email);
         ResultSet rs = stmt.executeQuery();

         if (rs.next()) {
            return Optional.of(mapRowToUser(rs));
         }
      } catch (SQLException e) {
         e.printStackTrace();
      }
      return Optional.empty();
   }

   public List<User> findAll() {
      List<User> users = new ArrayList<>();
      String sql = "SELECT * FROM pengguna";

      try (Connection conn = getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

         while (rs.next()) {
            users.add(mapRowToUser(rs));
         }
      } catch (SQLException e) {
         e.printStackTrace();
      }
      return users;
   }

   public void save(User user) {
      String sql = "INSERT INTO pengguna (name, email, password, role, created_at) VALUES (?, ?, ?, ?, ?)";
      try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

         stmt.setString(1, user.getName());
         stmt.setString(2, user.getEmail());
         stmt.setString(3, user.getPassword());

         // Check if the role is not set or is empty and default to "MEMBER"
         String role = user.getRole();
         if (role == null || role.isEmpty()) {
            role = "MEMBER";
         }
         stmt.setString(4, role);

         stmt.setLong(5, System.currentTimeMillis());
         stmt.executeUpdate();

      } catch (SQLException e) {
         e.printStackTrace();
      }
   }

   public void update(User user) {
      String sql = "UPDATE pengguna SET name = ?, email = ?, password = ?, role = ?, updated_at = ? WHERE pengguna_id = ?";
      try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

         stmt.setString(1, user.getName());
         stmt.setString(2, user.getEmail());
         stmt.setString(3, user.getPassword());
         stmt.setString(4, user.getRole());
         stmt.setLong(5, System.currentTimeMillis());
         stmt.setLong(6, user.getId());
         stmt.executeUpdate();

      } catch (SQLException e) {
         e.printStackTrace();
      }
   }

   public void delete(Long id) {
      String sql = "DELETE FROM pengguna WHERE pengguna_id = ?";
      try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

         stmt.setLong(1, id);
         stmt.executeUpdate();

      } catch (SQLException e) {
         e.printStackTrace();
      }
   }

   private User mapRowToUser(ResultSet rs) throws SQLException {
      User user = new User();
      user.setId(rs.getLong("pengguna_id"));
      user.setName(rs.getString("name"));
      user.setEmail(rs.getString("email"));
      user.setPassword(rs.getString("password"));
      user.setRole(rs.getString("role"));
      user.setCreatedAt(rs.getLong("created_at"));
      user.setUpdatedAt(rs.getLong("updated_at"));
      return user;
   }
}
