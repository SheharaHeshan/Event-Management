package com.mfx.eventmanagement;

import java.time.LocalDate;
import java.time.LocalTime;

public class EventDataStore {
    private final int eventId;
    private final String name;
    private final String description;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final String eventType;
    private final String attendanceType;


    public EventDataStore(int eventId,String name, String description, LocalDate startDate, LocalDate endDate,
                          LocalTime startTime, LocalTime endTime, String eventType, String attendanceType){
        this.eventId = eventId;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.eventType = eventType;
        this.attendanceType = attendanceType;

    }

    // Getters for the FXML card to use
    public int getEventId() { return eventId; }
    public String getName() { return name; }
    public LocalDate getStartDate() { return startDate; }
    public LocalTime getStartTime() { return startTime; }
    public LocalDate getEndDate() { return endDate; }
    public LocalTime getEndTime() { return endTime; }
    public String getAttendanceType() { return attendanceType; }
    public String getEventType() { return eventType; }

}