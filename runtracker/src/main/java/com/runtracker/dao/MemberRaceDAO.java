package com.runtracker.dao;

import com.runtracker.config.DatabaseConfig;
import com.runtracker.models.MemberRace;
import com.runtracker.models.User;
import com.runtracker.models.Race;
import com.runtracker.models.Activity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class MemberRaceDAO extends DatabaseConfig {

   public List<MemberRace> findByMember(User member) {
      List<MemberRace> memberRaces = new ArrayList<>();
      String sql = "SELECT * FROM member_race WHERE member_id = ?";

      try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

         stmt.setLong(1, member.getId());
         ResultSet rs = stmt.executeQuery();

         while (rs.next()) {
            memberRaces.add(mapRowToMemberRace(rs));
         }
      } catch (SQLException e) {
         e.printStackTrace();
      }
      return memberRaces;
   }

   public void save(MemberRace memberRace) {
      String sql = "INSERT INTO member_race (member_id, race_id, activity_id, created_at) VALUES (?, ?, ?, ?)";
      try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

         stmt.setLong(1, memberRace.getMember().getId());
         stmt.setLong(2, memberRace.getRace().getId());
         stmt.setObject(3, memberRace.getActivity() != null ? memberRace.getActivity().getId() : null, Types.BIGINT);
         stmt.setLong(4, System.currentTimeMillis());
         stmt.executeUpdate();

      } catch (SQLException e) {
         e.printStackTrace();
      }
   }

   public void delete(User member, Race race) {
      String sql = "DELETE FROM member_race WHERE member_id = ? AND race_id = ?";
      try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

         stmt.setLong(1, member.getId());
         stmt.setLong(2, race.getId());
         stmt.executeUpdate();

      } catch (SQLException e) {
         e.printStackTrace();
      }
   }

   private MemberRace mapRowToMemberRace(ResultSet rs) throws SQLException {
      MemberRace memberRace = new MemberRace();
      memberRace.setCreatedAt(rs.getLong("created_at"));
      memberRace.setUpdatedAt(rs.getLong("updated_at"));
      return memberRace;
   }
}
