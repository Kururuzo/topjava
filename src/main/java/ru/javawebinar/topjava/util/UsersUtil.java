package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.AbstractBaseEntity;
import ru.javawebinar.topjava.model.AbstractNamedEntity;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class UsersUtil {
    public static final List <User> USERS = Arrays.asList(
            new User(null, "Vasya", "vasya@mail.ru", "vasyaPassword", Role.ROLE_USER),
            new User(null, "Petya", "petya@mail.ru", "petyaPassword", Role.ROLE_USER),
            new User(null, "Kolya", "kolya@mail.ru", "kolyaPassword", Role.ROLE_ADMIN),
            new User(null, "Pupkin", "pupkin@mail.com", "pupkinPassword", Role.ROLE_ADMIN, Role.ROLE_USER)
    );

    public static List<User> sortUsersByName(List<User> users) {
        users.sort(Comparator.comparing(AbstractNamedEntity::getName)
                .thenComparing(AbstractBaseEntity::getId));
        return users;
    }

}
