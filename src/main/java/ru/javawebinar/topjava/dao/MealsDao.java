package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

public interface MealsDao {
    List<Meal> selectAll();
    void createMeal(LocalDateTime localDateTime, String localDescription, int localCalories);
    Meal getById (long id);
    void edit(long id, LocalDateTime localDateTime, String localDescription, int localCalories);
    void deleteMeal(long id);

}
