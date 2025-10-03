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
    private String profilePicturePath;

    // Constructor for populating ListView
    public AttendanceRecordStore(int attendanceId, String fullname, String email, LocalDateTime logTimestamp) {
        this.attendanceId = attendanceId;
        this.fullname = fullname;
        this.email = email;
        this.logTimestamp = logTimestamp;
    }

    // Full constructor for detailed record
    public AttendanceRecordStore(int attendanceId, int eventId, String fullname, int age, String gender, String about, String address, String email, String phonenumber,String profilePicturePath, LocalDateTime logTimestamp) {
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
        this.profilePicturePath = profilePicturePath;
    }

    // Getters for FXML/Controller (needed for property binding in ListView)
    public int getAttendanceId() { return attendanceId; }
    public String getFullname() { return fullname; }
    public String getEmail() { return email; }
    public LocalDateTime getLogTimestamp() { return logTimestamp; }
    public String getProfilePicturePath() {return profilePicturePath;}
    public int getEventId() { return eventId; }
    public int getAge() { return age; }
    public String getGender() { return gender; }
    public String getAbout() { return about; }
    public String getAddress() { return address; }
    public String getPhonenumber() { return phonenumber; }
}