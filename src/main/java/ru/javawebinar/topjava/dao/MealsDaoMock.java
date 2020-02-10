package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class MealsDaoMock implements MealsDao {
    private Map<Long, Meal> meals = new ConcurrentHashMap<>();
    private AtomicLong counter = new AtomicLong(1);

    {
        MealsUtil.MEALS_LIST.forEach(this::save);
    }

    @Override
    public Collection<Meal> selectAll() {
        return meals.values();
    }

    @Override
    public void save(Meal meal) {
        if (meal.getId() == null) {
            meal.setId(counter.getAndIncrement());
        }
        meals.put(meal.getId(), meal);
    }

    @Override
    public Meal getById(Long id) {
        return meals.getOrDefault(id, null);
    }

    @Override
    public boolean delete(Long id) {
        return meals.remove(id) != null;
    }
}
