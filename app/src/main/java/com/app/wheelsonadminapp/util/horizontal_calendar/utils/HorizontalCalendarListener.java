package com.app.wheelsonadminapp.util.horizontal_calendar.utils;


import com.app.wheelsonadminapp.util.horizontal_calendar.HorizontalCalendarView;

import java.util.Calendar;

public abstract class HorizontalCalendarListener {

    public abstract void onDateSelected(Calendar date, int position);

    public void onCalendarScroll(HorizontalCalendarView calendarView, int dx, int dy) {
    }

    public boolean onDateLongClicked(Calendar date, int position) {
        return false;
    }

}