package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.Collection;

public interface MealsDao {
    Collection<Meal> selectAll();
    void save(Meal meal);
    Meal getById (Long id);
    boolean delete(Long id);
}
