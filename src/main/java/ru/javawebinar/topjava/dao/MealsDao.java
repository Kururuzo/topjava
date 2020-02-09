package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.Collection;

public interface MealsDao {
    Collection<Meal> selectAll();
    void create(Meal meal);
    Meal getById (Long id);
    void edit(Long id, Meal meal);
    boolean delete(Long id);
}
