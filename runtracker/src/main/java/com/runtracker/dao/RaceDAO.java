package com.runtracker.dao;

import com.runtracker.config.DatabaseConfig;
import com.runtracker.models.Race;
import com.runtracker.models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public class RaceDAO extends DatabaseConfig {

   public boolean existsById(Long id) {
      String sql = "SELECT 1 FROM race WHERE race_id = ?";
      try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
         stmt.setLong(1, id);
         ResultSet rs = stmt.executeQuery();
         return rs.next();
      } catch (SQLException e) {
         e.printStackTrace();
      }
      return false;
   }

   public Optional<Race> findById(Long id) {
      String sql = "SELECT * FROM race WHERE race_id = ?";
      try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

         stmt.setLong(1, id);
         ResultSet rs = stmt.executeQuery();

         if (rs.next()) {
            return Optional.of(mapRowToRace(rs));
         }
      } catch (SQLException e) {
         e.printStackTrace();
      }
      return Optional.empty();
   }

   public List<Race> findByCreator(User creator) {
      List<Race> races = new ArrayList<>();
      String sql = "SELECT * FROM race WHERE created_by = ?";

      try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

         stmt.setLong(1, creator.getId());
         ResultSet rs = stmt.executeQuery();

         while (rs.next()) {
            races.add(mapRowToRace(rs));
         }
      } catch (SQLException e) {
         e.printStackTrace();
      }
      return races;
   }

   public void save(Race race) {
      String sql = "INSERT INTO race (created_by, updated_by, name, description, status, created_at) VALUES (?, ?, ?, ?, ?, ?)";
      try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

         stmt.setLong(1, race.getCreator().getId());
         stmt.setObject(2, race.getUpdator() != null ? race.getUpdator().getId() : null, Types.BIGINT);
         stmt.setString(3, race.getName());
         stmt.setString(4, race.getDescription());
         stmt.setString(5, race.getStatus());
         stmt.setLong(6, System.currentTimeMillis());
         stmt.executeUpdate();

      } catch (SQLException e) {
         e.printStackTrace();
      }
   }

   public void update(Race race) {
      String sql = "UPDATE race SET updated_by = ?, name = ?, description = ?, status = ?, updated_at = ? WHERE race_id = ?";
      try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

         stmt.setObject(1, race.getUpdator() != null ? race.getUpdator().getId() : null, Types.BIGINT);
         stmt.setString(2, race.getName());
         stmt.setString(3, race.getDescription());
         stmt.setString(4, race.getStatus());
         stmt.setLong(5, System.currentTimeMillis());
         stmt.setLong(6, race.getId());
         stmt.executeUpdate();

      } catch (SQLException e) {
         e.printStackTrace();
      }
   }

   public void delete(Long id) {
      String sql = "DELETE FROM race WHERE race_id = ?";
      try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

         stmt.setLong(1, id);
         stmt.executeUpdate();

      } catch (SQLException e) {
         e.printStackTrace();
      }
   }

   private Race mapRowToRace(ResultSet rs) throws SQLException {
      Race race = new Race();
      race.setId(rs.getLong("race_id"));
      race.setName(rs.getString("name"));
      race.setDescription(rs.getString("description"));
      race.setStatus(rs.getString("status"));
      race.setCreatedAt(rs.getLong("created_at"));
      race.setUpdatedAt(rs.getLong("updated_at"));
      return race;
   }
}
