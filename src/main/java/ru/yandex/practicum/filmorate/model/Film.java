package ru.yandex.practicum.filmorate.model;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import lombok.*;

@Data
public class Film {
    private static final int MAX_DESCRIPTION_LENGTH = 200;
    private static final Date MIN_DATE = new GregorianCalendar(1895, Calendar.DECEMBER, 28).getTime();

    private int id;
    @NonNull
    private final String name;
    private final String description;
    @NonNull
    private final String releaseDate;
    @NonNull
    private final int duration;

    public void validate() throws ValidationException {
        if (name.length() == 0) {
            throw new ValidationException("Имя не может быть пустым");
        }

        if (description != null && description.length() > MAX_DESCRIPTION_LENGTH) {
            throw new ValidationException("Описание не может быть длиннее " + MAX_DESCRIPTION_LENGTH + " символов");
        }

        if (ValidationHelper.parseDate(releaseDate).before(MIN_DATE)) {
            throw new ValidationException("Дата выхода не может быть раньше чем " + ValidationHelper.formatDate(MIN_DATE));
        }

        if (duration < 0) {
            throw new ValidationException("Продолжительность не может быть отрицательной");
        }
    }
}
