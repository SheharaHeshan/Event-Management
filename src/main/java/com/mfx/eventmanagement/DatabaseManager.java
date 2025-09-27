package com.mfx.eventmanagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public class DatabaseManager {

    // ⚠️ IMPORTANT: Update these credentials
    private static final String DB_URL = "jdbc:mysql://68.183.226.234:3306/main_event";
    private static final String DB_USER = "root"; // e.g., "root"
    private static final String DB_PASSWORD = "1234"; // e.g., "password123"

    /**
     * Establishes a connection to the MySQL database.
     * @return A valid Connection object, or null if the connection fails.
     */
    private Connection getConnection() throws SQLException {
        // Ensure the MySQL driver is loaded (optional for modern JDBC, but safe)
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
            throw new SQLException("JDBC Driver not available.");
        }
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    /**
     * Inserts a new event record into the 'events' table.
     */
    public boolean insertNewEvent(
            String name, String description, LocalDate startDate, LocalDate endDate,
            LocalTime startTime, LocalTime endTime, String eventType, String attendanceType) {

        String sql = "INSERT INTO events (event_name, event_description, start_date, end_date, start_time, end_time, event_type, attendance_type) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setString(2, description);
            pstmt.setDate(3, java.sql.Date.valueOf(startDate)); // Convert LocalDate to sql.Date
            pstmt.setDate(4, java.sql.Date.valueOf(endDate));
            pstmt.setTime(5, java.sql.Time.valueOf(startTime)); // Convert LocalTime to sql.Time
            pstmt.setTime(6, java.sql.Time.valueOf(endTime));
            pstmt.setString(7, eventType);
            pstmt.setString(8, attendanceType);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0; // Return true if insertion was successful

        } catch (SQLException e) {
            System.err.println("Database error during event insertion: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}