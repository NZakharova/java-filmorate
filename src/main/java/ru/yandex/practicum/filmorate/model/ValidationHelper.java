package ru.yandex.practicum.filmorate.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ValidationHelper {
    private ValidationHelper() {
    }

    static FormatHolder format = new FormatHolder();

    private static class FormatHolder {
        private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    }

    public static Date parseDate(String s) throws ValidationException {
        try {
            return format.dateFormat.parse(s);
        } catch (ParseException ex) {
            throw new ValidationException("Некорректный формат даты", ex);
        }
    }
}
