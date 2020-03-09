package ru.javawebinar.topjava.service.dataJpaTest;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.service.UserServiceTest;

@ActiveProfiles(Profiles.DATAJPA)
public class UserDataJpaTest extends UserServiceTest {
}
