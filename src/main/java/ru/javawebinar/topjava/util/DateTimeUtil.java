package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }

    //input/output may be null
    public static LocalDate stringToDate (String str) {
        return str == null || str.isEmpty() ?  null : LocalDate.parse(str);
    }

    //input/output may be null
    public static LocalTime stringToTime (String str) {
        return str == null || str.isEmpty() ? null : LocalTime.parse(str);
    }

    public static <T extends Comparable<T>> boolean isBetweenAnyDateAndTimeInclusive(T lt, T startTime, T endTime) {
        return lt.compareTo(startTime) >= 0 && lt.compareTo(endTime) <= 0;
    }
}

