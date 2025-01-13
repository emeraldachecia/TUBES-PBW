package com.runtracker.services;

import com.runtracker.dao.RaceDAO;
import com.runtracker.models.Race;
import com.runtracker.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RaceService {

   @Autowired
   private RaceDAO raceDAO;

   public RaceService(RaceDAO raceDAO) {
      this.raceDAO = raceDAO;
   }

   public List<Race> getRacesByCreator(User creator) {
      return raceDAO.findByCreator(creator);
   }

   public Optional<Race> getRaceById(Long id) {
      return raceDAO.findById(id);
   }

   public void createRace(Race race) {
      race.setCreatedAt(System.currentTimeMillis());
      raceDAO.save(race);
   }

   public void updateRace(Long id, Race updatedRace) {
      Optional<Race> existingRaceOptional = raceDAO.findById(id);

      if (!existingRaceOptional.isPresent()) {
         throw new IllegalArgumentException("Race not found");
      }

      Race existingRace = existingRaceOptional.get();
      existingRace.setName(updatedRace.getName());
      existingRace.setDescription(updatedRace.getDescription());
      existingRace.setStatus(updatedRace.getStatus());
      existingRace.setUpdator(updatedRace.getUpdator());
      existingRace.setUpdatedAt(System.currentTimeMillis());

      raceDAO.update(existingRace);
   }

   public void deleteRace(Long id) {
      if (!raceDAO.existsById(id)) {
         throw new IllegalArgumentException("Race not found");
      }
      raceDAO.delete(id);
   }
}
