package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService mealService;

    @Test
    public void create() {
        Meal newMeal = getNewMeal();
        Meal created = mealService.create(newMeal, USER_ID);
        System.out.println(created);
        Integer id = created.getId();
        newMeal.setId(id);
        assertMatch(created, newMeal);
        assertMatch(mealService.get(id, USER_ID), newMeal);
        assertMatch(mealService.getAll(USER_ID), newMeal,
                USER_MEAL_07, USER_MEAL_06, USER_MEAL_05, USER_MEAL_04, USER_MEAL_03, USER_MEAL_02, USER_MEAL_01);
    }

    //todo Have to create duplicateMealCreate ??? And how?

    @Test
    public void delete() {
        mealService.delete(USER_MEAL_01.getId(), USER_ID);
        assertMatch(mealService.getAll(USER_ID), USER_MEAL_07, USER_MEAL_06, USER_MEAL_05, USER_MEAL_04, USER_MEAL_03, USER_MEAL_02);
    }

    @Test(expected = NotFoundException.class)
    public void deleteAndNotFound() {
        Integer id = USER_MEAL_02.getId();
        mealService.delete(id, USER_ID);
        mealService.get(id, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void deleteWithoutAccess() {
        mealService.delete(USER_MEAL_03.getId(), ADMIN_ID);
    }

    @Test
    public void getMeal() {
        Meal meal = mealService.get(USER_MEAL_01.getId(), USER_ID);
        assertMatch(meal, USER_MEAL_01);
    }

    @Test(expected = NotFoundException.class)
    public void getMealNotFound() {
        mealService.get(1, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void getWithoutAccess() {
        mealService.get(USER_MEAL_04.getId(), ADMIN_ID);
    }

    @Test
    public void getBetweenHalfOpen() {
        assertMatch(mealService.getBetweenHalfOpen(USER_MEAL_01.getDate(), USER_MEAL_01.getDate(), USER_ID),
                USER_MEAL_03, USER_MEAL_02,  USER_MEAL_01);
    }

    @Test
    public void update() {
        Meal meal = MealTestData.update();
        mealService.update(meal, USER_ID);
        assertMatch(mealService.get(meal.getId(), USER_ID), meal);
    }

    @Test(expected = NotFoundException.class)
    public void updateWithoutAccess() {
        Meal meal = MealTestData.update();
        mealService.update(meal, ADMIN_ID);
    }

    @Test
    public void getAll() {
        assertMatch(mealService.getAll(USER_ID),
                USER_MEAL_07, USER_MEAL_06, USER_MEAL_05, USER_MEAL_04, USER_MEAL_03, USER_MEAL_02,  USER_MEAL_01);
    }
}