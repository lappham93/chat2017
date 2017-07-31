package com.mit.utils;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Hung Le on 2/13/17.
 */
public class ZonedDateTimeUtils {
    public static DateTimeFormatter YEAR_FORMAT = DateTimeFormatter.ofPattern("yyyy");
    public static DateTimeFormatter MONTH_FORMAT = DateTimeFormatter.ofPattern("MM/yyyy");
    public static DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    public static DateTimeFormatter DATE_TIME_MINUTE_FORMAT = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
    public static DateTimeFormatter DATE_TIME_HOUR_FORMAT = DateTimeFormatter.ofPattern("MM/dd/yyyy HH");

    public static ZonedDateTime now() {
        ZonedDateTime now = ZonedDateTime.now();
        return now;
    }

    public static ZonedDateTime nowInTimeZone(String zoneInfo) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of(zoneInfo));
        return now;
    }

    public static ZonedDateTime fromString(String dateFormat) {

        return ZonedDateTime.parse(dateFormat, DateTimeFormatter.ISO_DATE_TIME);
    }

    public static ZonedDateTime fromString(String dateFormat, DateTimeFormatter format) {
        return ZonedDateTime.parse(dateFormat, format);
    }

    public static String format(ZonedDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ISO_DATE_TIME);
    }

    public static String format(ZonedDateTime dateTime, DateTimeFormatter format) {
        return dateTime.format(format);
    }

    public static Date toDate(ZonedDateTime dateTime) {
        return Date.from(dateTime.toInstant());
    }

    public static ZonedDateTime fromDate(Date dateTime) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateTime);
        return ZonedDateTime.ofInstant(cal.toInstant(), cal.getTimeZone().toZoneId());
    }

    public static Calendar toCalendar(ZonedDateTime dateTime) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(dateTime.toInstant().toEpochMilli());
        cal.setTimeZone(TimeZone.getTimeZone(dateTime.getZone()));
        return cal;
    }

    public static ZonedDateTime fromCalendar(Calendar cal) {
        return ZonedDateTime.ofInstant(cal.toInstant(), cal.getTimeZone().toZoneId());
    }

    public static Calendar clearTime(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar;
    }
}
