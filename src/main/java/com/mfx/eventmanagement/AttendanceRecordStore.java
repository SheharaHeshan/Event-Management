package com.mfx.eventmanagement;

import java.time.LocalDateTime;

public class AttendanceRecordStore {
    private int attendanceId;
    private int eventId;
    private String fullname;
    private int age;
    private String gender;
    private String about;
    private String address;
    private String email;
    private String phonenumber;
    private LocalDateTime logTimestamp;

    // Constructor for populating ListView
    public AttendanceRecordStore(int attendanceId, String fullname, String email, LocalDateTime logTimestamp) {
        this.attendanceId = attendanceId;
        this.fullname = fullname;
        this.email = email;
        this.logTimestamp = logTimestamp;
    }

    // Full constructor for detailed record
    public AttendanceRecordStore(int attendanceId, int eventId, String fullname, int age, String gender, String about, String address, String email, String phonenumber, LocalDateTime logTimestamp) {
        this.attendanceId = attendanceId;
        this.eventId = eventId;
        this.fullname = fullname;
        this.age = age;
        this.gender = gender;
        this.about = about;
        this.address = address;
        this.email = email;
        this.phonenumber = phonenumber;
        this.logTimestamp = logTimestamp;
    }

    // Getters for FXML/Controller (needed for property binding in ListView)
    public int getAttendanceId() { return attendanceId; }
    public String getFullname() { return fullname; }
    public String getEmail() { return email; }
    public LocalDateTime getLogTimestamp() { return logTimestamp; }

    // You'll need more getters/setters if you want to display more info, but these cover the card prototype.
}