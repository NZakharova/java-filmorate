package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ValidationHelper {
    private ValidationHelper() {
    }

    static FormatHolder format = new FormatHolder();

    private static class FormatHolder {
        private final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    }

    public static String formatDate(LocalDate date) {
        return format.dateTimeFormat.format(date);
    }

    public static LocalDate parseDate(String date) {
        return LocalDate.parse(date, format.dateTimeFormat);
    }
}
