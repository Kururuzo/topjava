package ru.javawebinar.topjava.service.jdbc;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;

import java.util.Set;

import static ru.javawebinar.topjava.Profiles.JDBC;
import static ru.javawebinar.topjava.UserTestData.USER_MATCHER;

@ActiveProfiles(JDBC)
public class JdbcUserServiceTest extends AbstractUserServiceTest {

    @Test
    public void addUserWithRoles() {
        User newUser = UserTestData.getNew();
        newUser.setRoles(Set.of(Role.ROLE_USER, Role.ROLE_ADMIN));
        User createdUser = service.create(newUser);

        User gettedUser = service.get(createdUser.getId());
        USER_MATCHER.assertMatch(createdUser, gettedUser);
    }

    @Test
    public void deleteUserRoles(){
        User createdUser = service.create(UserTestData.getNew());
        service.deleteUserRoles(createdUser);
        Assert.assertTrue(service.get(createdUser.getId()).getRoles().isEmpty());
    }

    @Override
//    @Test(expected = IllegalArgumentException.class)
    public void getByEmail() {
        super.getByEmail();
    }
}