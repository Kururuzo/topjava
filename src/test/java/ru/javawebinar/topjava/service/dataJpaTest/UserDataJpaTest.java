package ru.javawebinar.topjava.service.dataJpaTest;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserServiceTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static ru.javawebinar.topjava.UserTestData.*;


@ActiveProfiles(Profiles.DATAJPA)
public class UserDataJpaTest extends UserServiceTest {

    @Test
    public void getUserWithMeals() throws NoSuchMethodException {
        User userWithMeals = service.getUserMeals(ADMIN_ID);
        USER_MATCHER.assertMatch(userWithMeals, ADMIN_WITH_MEALS);
        MealTestData.MEAL_MATCHER.assertMatch(userWithMeals.getUserMeals(), ADMIN_WITH_MEALS.getUserMeals());
        Assert.assertEquals(2, userWithMeals.getUserMeals().size());
    }

    @Test
    public void getUserWithMealsNotFound() {
        Assert.assertThrows(NotFoundException.class, () -> {
            service.getUserMeals(1);
        });
    }
}
