package ru.javawebinar.topjava.repository.datajpa;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DataJpaMealRepository implements MealRepository {

    @Autowired
    private CrudMealRepository crudRepository;

    @Autowired
    private CrudUserRepository crudUserRepository;

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        if (!meal.isNew() && get(meal.getId(), userId) == null) { //isNew checking first, NPE?
            return null;
        }
        meal.setUser(crudUserRepository.getOne(userId));
        return crudRepository.save(meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        return crudRepository.deleteMealByIdAndUser_Id(id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        return crudRepository.getMealByIdAndUser_Id(id, userId);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return crudRepository.getAllByUser_IdOrderByDateTimeDesc(userId);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return crudRepository.getAllByUser_IdAndDateTimeGreaterThanEqualAndDateTimeIsLessThanOrderByDateTimeDesc(userId, startDateTime, endDateTime);
    }

    @Override
    public Meal getMealWithUser (int id, int userId) {
        return crudRepository.getMealWithUser(id, userId);
    }
}
