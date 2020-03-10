package ru.javawebinar.topjava.service.dataJpaTest;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealServiceTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_MATCHER;

@ActiveProfiles(Profiles.DATAJPA)
public class MealDataJpaTest extends MealServiceTest {

    @Test
    public void getMealwithUser() throws NoSuchMethodException {
        Meal mealWithUser = service.getMealWithUser(ADMIN_MEAL_ID, ADMIN_ID);
        MEAL_MATCHER.assertMatch(mealWithUser, ADMIN_MEAL1_WITH_USER);
        USER_MATCHER.assertMatch(mealWithUser.getUser(), ADMIN_MEAL1_WITH_USER.getUser());
    }


    @Test
    public void getMealwithUserNotFound() {
        Assert.assertThrows(NotFoundException.class, ()->{
            service.getMealWithUser(1, ADMIN_ID);
        });
    }

}
