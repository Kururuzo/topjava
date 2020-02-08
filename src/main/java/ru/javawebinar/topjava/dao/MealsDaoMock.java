package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class MealsDaoMock implements MealsDao {
    private List<Meal> meals;
    private AtomicInteger counter;

    public MealsDaoMock() {
        counter = new AtomicInteger(1);

        List<Meal> tempMeals = Arrays.asList(
                new Meal(counter.getAndIncrement(), LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new Meal(counter.getAndIncrement(), LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new Meal(counter.getAndIncrement(), LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new Meal(counter.getAndIncrement(), LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(counter.getAndIncrement(), LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(counter.getAndIncrement(), LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new Meal(counter.getAndIncrement(), LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
        meals = new CopyOnWriteArrayList<>(tempMeals);
    }

    @Override
    public List<Meal> selectAll() {
        meals.sort(Comparator.comparing(Meal::getDateTime)); //for fun )
        return meals;
    }

    @Override
    public void createMeal(LocalDateTime localDateTime, String localDescription, int localCalories) {
        meals.add(new Meal(counter.getAndIncrement(), localDateTime, localDescription, localCalories));
    }

    @Override
    public Meal getById(long id) {
        return meals.stream()
                .filter(meal -> meal.getId() == id)
                .findFirst().get();
    }

    //todo whether to use synchronized?
    @Override
    public synchronized void edit(long id, LocalDateTime localDateTime, String localDescription, int localCalories) {
        deleteMeal(id);
        meals.add(new Meal(id, localDateTime, localDescription, localCalories));
    }

    @Override
    public void deleteMeal(long id) {
        meals.removeIf(meal -> meal.getId() == id);
    }
}
