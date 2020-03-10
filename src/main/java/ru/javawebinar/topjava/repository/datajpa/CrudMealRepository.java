package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

public interface CrudMealRepository extends JpaRepository<Meal, Integer> {

    @Transactional
    @Modifying
    int deleteMealByIdAndUser_Id(int id, int userId);

    Meal getMealByIdAndUser_Id(int id, int userId);

    List<Meal> getAllByUser_IdOrderByDateTimeDesc(int userId);

    List<Meal> getAllByUser_IdAndDateTimeGreaterThanEqualAndDateTimeIsLessThanOrderByDateTimeDesc
   (int userId, LocalDateTime startDateTime, LocalDateTime endDateTime);

    @Query("SELECT meal from Meal meal JOIN FETCH meal.user u WHERE meal.id=:id AND u.id=:userId")
    Meal getMealWithUser (@Param("id") int id, @Param("userId") int userId);

}

