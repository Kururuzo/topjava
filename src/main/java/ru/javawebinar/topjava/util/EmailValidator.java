package ru.javawebinar.topjava.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class EmailValidator implements Validator {

	private final UserService userService;

	public EmailValidator(UserService userService) {
		this.userService = userService;
	}

	//TODO What about userTo???
	@Override
	public boolean supports(Class<?> aClass) {
		return User.class.equals(aClass);
	}

	@Override
	public void validate(Object o, Errors errors) {
		User user = (User) o;
		User userFromDB = userService.getByEmail(user.getEmail().toLowerCase());
		if(userFromDB != null && user.getId().equals(userFromDB.getId())){
			errors.rejectValue("email", "user.email.already.exists");
		}
	}
}