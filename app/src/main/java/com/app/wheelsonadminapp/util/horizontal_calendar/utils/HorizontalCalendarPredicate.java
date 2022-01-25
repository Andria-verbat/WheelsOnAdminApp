package com.app.wheelsonadminapp.util.horizontal_calendar.utils;


import com.app.wheelsonadminapp.util.horizontal_calendar.model.CalendarItemStyle;

import java.util.Calendar;

public interface HorizontalCalendarPredicate {

    boolean test(Calendar date);

    CalendarItemStyle style();

    class Or implements HorizontalCalendarPredicate {

        private final HorizontalCalendarPredicate firstPredicate;
        private final HorizontalCalendarPredicate secondPredicate;

        public Or(HorizontalCalendarPredicate firstPredicate, HorizontalCalendarPredicate secondPredicate) {
            this.firstPredicate = firstPredicate;
            this.secondPredicate = secondPredicate;
        }

        @Override
        public boolean test(Calendar date) {
            return firstPredicate.test(date) || secondPredicate.test(date);
        }

        @Override
        public CalendarItemStyle style() {
            return firstPredicate.style();
        }
    }
}
