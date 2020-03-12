package ru.javawebinar.topjava.service.dataJpaTest;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealServiceTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(Profiles.DATAJPA)
public class MealDataJpaTest extends MealServiceTest {

    @Test
    public void getMealwithUser() {
        Meal mealWithUser = service.getMealWithUser(ADMIN_MEAL_ID, ADMIN_ID);
        MEAL_MATCHER.assertMatch(mealWithUser, ADMIN_MEAL1);
        USER_MATCHER.assertMatch(mealWithUser.getUser(), ADMIN);
    }

    @Test
    public void getMealwithUserNotFound() {
        Assert.assertThrows(NotFoundException.class, ()->{
            service.getMealWithUser(1, ADMIN_ID);
        });
    }

}
