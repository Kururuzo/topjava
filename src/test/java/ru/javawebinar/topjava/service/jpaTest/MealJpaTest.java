package ru.javawebinar.topjava.service.jpaTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.service.MealServiceTest;

@ActiveProfiles(Profiles.JPA)
public class MealJpaTest extends MealServiceTest {

}
