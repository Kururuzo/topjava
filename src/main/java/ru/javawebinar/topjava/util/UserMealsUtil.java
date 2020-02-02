package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

//        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        List<UserMealWithExcess> mealsTo = filteredByCyclesOptional2(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

//        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
//        System.out.println(filteredByStreamsOptional2(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }


    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesByDays = new HashMap<>();
        for (UserMeal meal : meals) {
            caloriesByDays.put(meal.getDateTime().toLocalDate(),
                    caloriesByDays.getOrDefault(meal.getDateTime().toLocalDate(), 0) + meal.getCalories());
        }

        List<UserMealWithExcess> result = new ArrayList<>();
        for (UserMeal meal : meals) {
            if (TimeUtil.isBetweenInclusive(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                boolean excess = caloriesPerDay < caloriesByDays.get(meal.getDateTime().toLocalDate());
                result.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess));
            }
        }
        return result;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesByDays = meals.stream()
                .collect(Collectors.groupingBy(userMeal1 -> userMeal1.getDateTime().toLocalDate(),
                        Collectors.summingInt(UserMeal::getCalories)));

        List<UserMealWithExcess> result = meals.stream()
                .filter(userMeal -> TimeUtil.isBetweenInclusive(userMeal.getDateTime().toLocalTime(), startTime, endTime))
                .map(userMeal -> new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(),
                        caloriesPerDay < caloriesByDays.get(userMeal.getDateTime().toLocalDate())))
                .collect(toList());

        return result;
    }

    // second attempt
    public static List<UserMealWithExcess> filteredByCyclesOptional2(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExcess> result = new ArrayList<>();

        Collection<List<UserMeal>> mealsByDays = meals.stream()
                .collect(Collectors.groupingBy(userMeal1 -> userMeal1.getDateTime().toLocalDate()))
                .values();

        for (List<UserMeal> mealByDay : mealsByDays) {
            boolean excess = mealByDay.stream()
                    .mapToInt(UserMeal::getCalories)
                    .reduce(0, Integer::sum) > caloriesPerDay;
            for (UserMeal meal : mealByDay) {
                if (TimeUtil.isBetweenInclusive(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                    result.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess));
                }
            }
        }

        return result;


// first attempt
//        List<UserMealWithExcess> result = new ArrayList<>();
//
//        for (UserMeal meal : meals) {
//            LocalDate mealDate = meal.getDateTime().toLocalDate();
//
//            if (TimeUtil.isBetweenInclusive(meal.getDateTime().toLocalTime(), startTime, endTime)) {
//
//                result.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(),
//                        meals.stream()
//                                .filter(userMeal1 -> userMeal1.getDateTime().toLocalDate().equals(meal.getDateTime().toLocalDate()))
//                                .mapToInt(UserMeal::getCalories)
//                                .reduce(0, Integer::sum) > caloriesPerDay));
//            }
//        }
//        return result;
    }

    // second attempt
    public static List<UserMealWithExcess> filteredByStreamsOptional2(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return meals.stream()
                .collect(Collectors.groupingBy(userMeal1 -> userMeal1.getDateTime().toLocalDate()))
                .values() //mealsByDays - 2 lists <List<UserMeal>>
                .stream()
                .flatMap(userMeals ->
                        userMeals.stream()
                                .filter(userMeal -> TimeUtil.isBetweenInclusive(userMeal.getDateTime().toLocalTime(), startTime, endTime))
                                .map(userMeal -> new UserMealWithExcess(userMeal.getDateTime(),
                                        userMeal.getDescription(),
                                        userMeal.getCalories(),
                                        userMeals.stream()
                                                .mapToInt(UserMeal::getCalories)
                                                .reduce(0, Integer::sum) > caloriesPerDay
                                )))
                .collect(Collectors.toList());

// first attempt
//        //two cycles and O(n2), not good :(
//        List<UserMealWithExcess> caloriesByDays = meals.stream()
//                .filter(userMeal -> TimeUtil.isBetweenInclusive(userMeal.getDateTime().toLocalTime(), startTime, endTime))
//                .map(userMeal -> new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(),
//                        meals.stream()
//                                .filter(userMeal1 -> userMeal1.getDateTime().toLocalDate().equals(userMeal.getDateTime().toLocalDate()))
//                                .mapToInt(UserMeal::getCalories)
//                                .reduce(0, Integer::sum) > caloriesPerDay))
//                .collect(Collectors.toList());
//        return caloriesByDays;
    }
}
