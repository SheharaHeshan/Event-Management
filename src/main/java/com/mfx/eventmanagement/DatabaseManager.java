package com.mfx.eventmanagement;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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

    //Method to Load All Events from Database

    public List<EventDataStore> loadAllEvents() {
        List<EventDataStore> events = new ArrayList<>();
        // Select all columns needed for the EventDataStore constructor
        String sql = "SELECT event_name, event_description, start_date, end_date, start_time, end_time, event_type, attendance_type FROM events ORDER BY start_date ASC";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // Retrieve data from the current row
                String name = rs.getString("event_name");
                String description = rs.getString("event_description");

                // Convert SQL Date/Time types to Java 8 Local types
                LocalDate startDate = rs.getDate("start_date").toLocalDate();
                LocalDate endDate = rs.getDate("end_date").toLocalDate();
                LocalTime startTime = rs.getTime("start_time").toLocalTime();
                LocalTime endTime = rs.getTime("end_time").toLocalTime();

                String eventType = rs.getString("event_type");
                String attendanceType = rs.getString("attendance_type");

                // Create and add the EventDataStore object
                EventDataStore event = new EventDataStore(
                        name, description, startDate, endDate,
                        startTime, endTime, eventType, attendanceType
                );
                events.add(event);
            }

        } catch (SQLException e) {
            System.err.println("Error loading events from database: " + e.getMessage());
            e.printStackTrace();
        }
        return events;
    }
}
