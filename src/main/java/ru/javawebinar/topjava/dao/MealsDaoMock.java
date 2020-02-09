package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class MealsDaoMock implements MealsDao {
    private static volatile MealsDaoMock instance;
    private Map<Long, Meal> meals = new ConcurrentHashMap<>();
    private AtomicLong counter = new AtomicLong(1);

    private MealsDaoMock() {
    }

    public static MealsDaoMock getInstance() {
        MealsDaoMock localInstance = instance;
        if (localInstance == null) {
            synchronized (MealsDaoMock.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new MealsDaoMock();
                }
            }
        }
        return localInstance;
    }

    {
        MealsUtil.MEALS_LIST.forEach(this::create);
    }

    @Override
    public Collection<Meal> selectAll() {
        return meals.values();
    }

    @Override
    public void create(Meal meal) {
        if (meal.getId() == null) {
            meal.setId(counter.getAndIncrement());
        }
        meals.put(meal.getId(), meal);
    }

    @Override
    public Meal getById(Long id) {
        return meals.getOrDefault(id, null);
    }

    //todo whether to use synchronized?
    @Override
    public synchronized void edit(Long id, Meal meal) {
        delete(id);
        create(meal);
    }

    @Override
    public boolean delete(Long id) {
        if (meals.containsKey(id)) {
            meals.remove(id);
            return true;
        } else {
            return false;
        }
    }
}
