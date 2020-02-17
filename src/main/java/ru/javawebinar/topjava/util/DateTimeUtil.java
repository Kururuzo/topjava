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

    public static LocalDate stringToDate (String str, LocalDate date) {
        return str == null || str.isEmpty() ?  date : LocalDate.parse(str); //LocalDate.MIN
    }

    public static LocalTime stringToTime (String str, LocalTime time) {
        return str == null || str.isEmpty() ? time : LocalTime.parse(str);
    }

    //Проверку isBetweenDate сделать в DateTimeUtil.
    //Попробуйте использовать дженерики и объединить ее с isBetweenTime (см. Generics Tutorials)
    public static <T extends Comparable<T>> boolean isBetweenAllInclusive(T lt, T startTime, T endTime) {
        return lt.compareTo(startTime) >= 0 && lt.compareTo(endTime) <= 0;
    }

    public static boolean isBetweenDate(LocalDate lt, LocalDate startTime, LocalDate endTime) {
        return lt.compareTo(startTime) >= 0 && lt.compareTo(endTime) <= 0;
    }

    public static boolean isBetweenInclusive(LocalTime lt, LocalTime startTime, LocalTime endTime) {
        return lt.compareTo(startTime) >= 0 && lt.compareTo(endTime) <= 0;
    }


}

