package com.runtracker.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class DatabaseConfig {

   private static final String DB_URL = "jdbc:postgresql://localhost:5432/run_tracker";
   private static final String DB_USER = "admin_runtracker";
   private static final String DB_PASSWORD = "runtracker";

   private static final HikariDataSource dataSource;

   static {
      HikariConfig config = new HikariConfig();
      config.setJdbcUrl(DB_URL);
      config.setUsername(DB_USER);
      config.setPassword(DB_PASSWORD);

      // Konfigurasi tambahan untuk performa dan stabilitas

      // Maksimal 10 koneksi dalam pool
      config.setMaximumPoolSize(10);
      // Minimal 2 koneksi idle
      config.setMinimumIdle(2);
      // 30 detik idle timeout
      config.setIdleTimeout(30000);
      // 30 menit maksimal koneksi
      config.setMaxLifetime(1800000);
      // 10 detik timeout untuk mendapatkan koneksi
      config.setConnectionTimeout(10000);

      dataSource = new HikariDataSource(config);
   }

   protected Connection getConnection() throws SQLException {
      return dataSource.getConnection();
   }

   public static void closePool() {
      if (dataSource != null && !dataSource.isClosed()) {
         dataSource.close();
      }
   }
}
