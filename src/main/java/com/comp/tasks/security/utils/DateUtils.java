package com.comp.tasks.security.utils;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.TimeZone;

public class DateUtils {
    public static Timestamp getCurrentUtcTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        return new Timestamp(calendar.getTimeInMillis());
    }
}
