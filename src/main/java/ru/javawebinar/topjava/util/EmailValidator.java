package ru.javawebinar.topjava.util;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javawebinar.topjava.HasEmail;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserService;


@Component
public class EmailValidator implements Validator {

	private final UserService userService;

	public EmailValidator(UserService userService) {
		this.userService = userService;
	}

	//https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#validation
	@Override
	public boolean supports(Class<?> aClass) {
		return HasEmail.class.isAssignableFrom(aClass);
	}

	@Override
	public void validate(Object o, Errors errors) {
		HasEmail user = (HasEmail) o;
		User userFromDB = userService.getByEmail(user.getEmail().toLowerCase());

		//what about update? if user is new, his id = null
//		if(userFromDB != null && !user.getId().equals(userFromDB.getId())){
		if(userFromDB != null){
			errors.rejectValue("email", "user.email.already.exists");
		}
	}
}