package com.mfx.eventmanagement;

import java.time.LocalDate;

/**
 * A data model for an event.
 * Using a record is a concise way to create a class that primarily stores data.
 */
public record Event(String name,
                    String description,
                    String location,
                    LocalDate startDate,
                    LocalDate endDate,
                    String eventType) {
}