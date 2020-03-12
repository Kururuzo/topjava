package ru.javawebinar.topjava.service.dataJpaTest;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserServiceTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;


@ActiveProfiles(Profiles.DATAJPA)
public class UserDataJpaTest extends UserServiceTest {

    @Test
    public void getUserWithMeals() {
        User userWithMeals = service.getUserMeals(USER_ID);
        USER_MATCHER.assertMatch(userWithMeals, USER);
        MEAL_MATCHER.assertMatch(userWithMeals.getMeals(), MEALS);
    }

    @Test
    public void getUserWithMealsNotFound() {
        Assert.assertThrows(NotFoundException.class, () -> {
            service.getUserMeals(1);
        });
    }
}
