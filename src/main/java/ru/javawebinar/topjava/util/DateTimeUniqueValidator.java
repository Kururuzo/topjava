package ru.javawebinar.topjava.util;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javawebinar.topjava.HasDateTime;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.web.SecurityUtil;


@Component
public class DateTimeUniqueValidator implements Validator {

	private final MealService mealService;

	public DateTimeUniqueValidator(MealService mealService) {
		this.mealService = mealService;
	}

	//https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#validation
	@Override
	public boolean supports(Class<?> aClass) {
		return HasDateTime.class.isAssignableFrom(aClass);
	}

	@Override
	public void validate(Object o, Errors errors) {
		HasDateTime meal = (HasDateTime) o;
		Meal mealFromDB = mealService.get(meal.getId(), SecurityUtil.authUserId());

		if(mealFromDB != null){
			errors.rejectValue("dateTime", "meal.dateTime.already.exists");
		}
	}
}