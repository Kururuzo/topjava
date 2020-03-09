package ru.javawebinar.topjava.service.jdbcTest;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.service.UserServiceTest;

@ActiveProfiles(Profiles.JDBC)
public class UserJdbcTest extends UserServiceTest {
}
