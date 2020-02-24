package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {

    public static final int USER_ID = START_SEQ;
    public static final int ADMIN_ID = START_SEQ + 1;

    public static final Meal USER_MEAL_01 = new Meal(USER_ID + 2, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак ROLE_USER", 500);
    public static final Meal USER_MEAL_02 = new Meal(USER_ID + 3, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед ROLE_USER", 1000);
    public static final Meal USER_MEAL_03 = new Meal(USER_ID + 4, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин ROLE_USER", 500);
    public static final Meal USER_MEAL_04 = new Meal(USER_ID + 5, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение ROLE_USER", 100);
    public static final Meal USER_MEAL_05 = new Meal(USER_ID + 6, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак ROLE_USER", 1000);
    public static final Meal USER_MEAL_06 = new Meal(USER_ID + 7, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед ROLE_USER", 500);
    public static final Meal USER_MEAL_07 = new Meal(USER_ID + 8, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин ROLE_USER", 410);

    public static final Meal ADMIN_MEAL_01 = new Meal(ADMIN_ID + 31, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак ROLE_ADMIN", 500);
    public static final Meal ADMIN_MEAL_02 = new Meal(ADMIN_ID + 32, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед ROLE_ADMIN", 1000);
    public static final Meal ADMIN_MEAL_03 = new Meal(ADMIN_ID + 33, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин ROLE_ADMIN", 500);
    public static final Meal ADMIN_MEAL_04 = new Meal(ADMIN_ID + 34, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение ROLE_USER", 100);
    public static final Meal ADMIN_MEAL_05 = new Meal(ADMIN_ID + 35, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак ROLE_ADMIN", 1000);
    public static final Meal ADMIN_MEAL_06 = new Meal(ADMIN_ID + 36, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед ROLE_ADMIN", 500);
    public static final Meal ADMIN_MEAL_07 = new Meal(ADMIN_ID + 37, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин ROLE_ADMIN", 410);


//    public static final Meal NEW_MEAL = new Meal(LocalDateTime.of(2020, Month.FEBRUARY, 10, 11, 0), "Late Завтрак", 500);


    public static Meal getNewMeal() {
        return new Meal(null, LocalDateTime.of(2020, Month.FEBRUARY, 10, 11, 0), "New Late Завтрак", 500);
    }

    public static Meal update() {
        return new Meal(USER_MEAL_07.getId(),
                LocalDateTime.now(),
                USER_MEAL_07.getDescription(),
                USER_MEAL_07.getCalories());
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualToComparingFieldByField(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingDefaultElementComparator().isEqualTo(expected);
    }
    /*

            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)


    VALUES (TIMESTAMP '2020-01-30 10:00:00', 'Завтрак ROLE_USER', 500, 100000),
       (TIMESTAMP '2020-01-30 13:00:00', 'Обед ROLE_USER', 1000, 100000),
       (TIMESTAMP '2020-01-30 20:00:00', 'Ужин ROLE_USER', 500, 100000),
       (TIMESTAMP '2020-01-31 00:00:00', 'Еда на граничное значение ROLE_USER', 100, 100000),
       (TIMESTAMP '2020-01-31 10:00:00', 'Завтрак ROLE_USER', 1000, 100000),
       (TIMESTAMP '2020-01-31 13:00:00', 'Обед ROLE_USER', 500, 100000),
       (TIMESTAMP '2020-01-31 20:00:00', 'Ужин ROLE_USER', 410, 100000),

       (TIMESTAMP '2020-01-30 10:00:00', 'Завтрак ROLE_ADMIN', 500, 100001),
       (TIMESTAMP '2020-01-30 13:00:00', 'Обед ROLE_ADMIN', 1000, 100001),
       (TIMESTAMP '2020-01-30 20:00:00', 'Ужин ROLE_ADMIN', 500, 100001),
       (TIMESTAMP '2020-01-31 00:00:00', 'Еда на граничное значение ROLE_ADMIN', 100, 100001),
       (TIMESTAMP '2020-01-31 10:00:00', 'Завтрак ROLE_ADMIN', 1000, 100001),
       (TIMESTAMP '2020-01-31 13:00:00', 'Обед ROLE_ADMIN', 500, 100001),
       (TIMESTAMP '2020-01-31 20:00:00', 'Ужин ROLE_ADMIN', 410, 100001);
     */
}
