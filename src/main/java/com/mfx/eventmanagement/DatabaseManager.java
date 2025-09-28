package com.mfx.eventmanagement;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

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

    /**
     * Retrieves all event names and their IDs for the ComboBox.
     * @return A Map where Key is eventName (String) and Value is eventId (Integer).
     */
    public Map<String, Integer> getEventNamesAndIds() {
        Map<String, Integer> events = new HashMap<>();
        String sql = "SELECT event_id, event_name FROM events ORDER BY event_name ASC";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                events.put(rs.getString("event_name"), rs.getInt("event_id"));
            }

        } catch (SQLException e) {
            System.err.println("Error loading event names and IDs: " + e.getMessage());
        }
        return events;
    }

    /**
     * Retrieves attendance records for a specific event.
     */
    public List<AttendanceRecordStore > getAttendanceByEventId(int eventId) {
        List<AttendanceRecordStore> attendanceList = new ArrayList<>();
        String sql = "SELECT attendance_id, fullname, email, log_timestamp " +
                "FROM attendance WHERE event_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, eventId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("attendance_id");
                    String fullname = rs.getString("fullname");
                    String email = rs.getString("email");
                    LocalDateTime logTimestamp = rs.getTimestamp("log_timestamp").toLocalDateTime();

                    attendanceList.add(new AttendanceRecordStore(id, fullname, email, logTimestamp));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error loading attendance for event ID " + eventId + ": " + e.getMessage());
        }
        return attendanceList;
    }

    /**
     * Inserts a new attendance record (for Add Manually feature).
     */
    public boolean insertAttendance(int eventId, String fullname, int age, String gender, String about, String address, String email, String phonenumber) {
        String sql = "INSERT INTO attendance (event_id, fullname, age, gender, about, address, email, phonenumber) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, eventId);
            pstmt.setString(2, fullname);
            pstmt.setInt(3, age);
            pstmt.setString(4, gender);
            pstmt.setString(5, about);
            pstmt.setString(6, address);
            pstmt.setString(7, email);
            pstmt.setString(8, phonenumber);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error inserting new attendance: " + e.getMessage());
            return false;
        }
    }
}
