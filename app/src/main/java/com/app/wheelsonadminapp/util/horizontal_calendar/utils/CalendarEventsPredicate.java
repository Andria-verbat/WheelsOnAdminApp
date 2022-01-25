package com.app.wheelsonadminapp.util.horizontal_calendar.utils;


import com.app.wheelsonadminapp.util.horizontal_calendar.model.CalendarEvent;

import java.util.Calendar;
import java.util.List;


public interface CalendarEventsPredicate {

    List<CalendarEvent> events(Calendar date);
}
