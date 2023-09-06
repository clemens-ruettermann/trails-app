package com.kenspeckle.trails.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class DateUtils {

    public static String convertLocalDateTimeToLocalizedDateTime(String str) {
        LocalDateTime localDateTime = LocalDateTime.parse(str);
        return localDateTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT));
    }

    public static String convertSeperateDateAndTimeString(String dateStr, String timeStr) {
        LocalDate date = LocalDate.parse(dateStr);
        if (timeStr != null) {
            LocalTime time = LocalTime.parse(timeStr);
            LocalDateTime localDateTime = LocalDateTime.of(date, time);
            return localDateTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT));
        } else {
            return date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT));
        }
    }

    public static int compareDateTimeString(String dateStr1, String timeStr1, String dateStr2, String timeStr2) {
        LocalDate date1 = LocalDate.parse(dateStr1);
        LocalDate date2 = LocalDate.parse(dateStr2);
        if (timeStr1 == null || timeStr2 == null) {
            return date1.compareTo(date2);
        }
        LocalTime time1 = LocalTime.parse(timeStr1);
        LocalTime time2 = LocalTime.parse(timeStr2);
        LocalDateTime localDateTime1 = LocalDateTime.of(date1, time1);
        LocalDateTime localDateTime2 = LocalDateTime.of(date2, time2);

        return localDateTime1.compareTo(localDateTime2);
    }
}
