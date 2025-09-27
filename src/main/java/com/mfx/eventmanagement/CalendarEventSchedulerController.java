package com.mfx.eventmanagement;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.view.CalendarView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class CalendarEventSchedulerController {

    @FXML private Label eventTitleLabel;
    @FXML private BorderPane calendarContainer; // Container from FXML

    private EventDataStore mainEvent;
    private CalendarView calendarView;

    @FXML
    public void initialize() {
        // Initialize CalendarFX view but don't configure it yet, waiting for mainEvent
        calendarView = new CalendarView();

        // Optional: Customize the view (e.g., hide unnecessary elements)
        calendarView.setShowToolBar(true);
        calendarView.setShowPageSwitcher(true); // Day/Week/Month/Year tabs
        calendarView.setShowSourceTray(false);
        calendarView.setShowSearchResultsTray(false);

        // Add the CalendarFX view to the FXML container
        calendarContainer.setCenter(calendarView);
    }

    /**
     * Receives the selected main event data and configures the calendar.
     */
    public void setMainEvent(EventDataStore event) {
        this.mainEvent = event;
        eventTitleLabel.setText( event.getName());

        // 1. Create a Calendar for this specific main event
        Calendar subEventCalendar = new Calendar(event.getName() + " Sub-Events");

        // You can set the initial time frame based on the main event's start date
        calendarView.setDate(event.getStartDate());

        // 2. Create a CalendarSource (a group for calendars, like an account)
        CalendarSource source = new CalendarSource(event.getName());
        source.getCalendars().add(subEventCalendar);

        // 3. Add the source to the CalendarView
        calendarView.getCalendarSources().setAll(source);

        // TODO: In the next step, you would load existing sub-events from a new database table
        //       (e.g., 'sub_events' linked by event.getEventId()) and add them to subEventCalendar.
        // subEventCalendar.addEntry(new Entry("Sub-Event Title", interval));
    }
}
