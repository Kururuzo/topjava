package ru.javawebinar.topjava.service;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.JpaUtil;

import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.util.Set;

public abstract class AbstractUserServiceJpaTest extends AbstractUserServiceTest {
    @Autowired
    private CacheManager cacheManager;

    @Autowired
    protected JpaUtil jpaUtil;

    @Before
    public void setUp() {
        cacheManager.getCache("users").clear();
        jpaUtil.clear2ndLevelHibernateCache();
        evictAllCaches();

    }

    @Test
    public void createWithException() {
//        Assume.assumeTrue(isJpaOrDataJpa());
        validateRootCause(() -> service.create(new User(null, "  ", "mail@yandex.ru", "password", Role.ROLE_USER)), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new User(null, "User", "  ", "password", Role.ROLE_USER)), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new User(null, "User", "mail@yandex.ru", "  ", Role.ROLE_USER)), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new User(null, "User", "mail@yandex.ru", "password", 9, true, new Date(), Set.of())), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new User(null, "User", "mail@yandex.ru", "password", 10001, true, new Date(), Set.of())), ConstraintViolationException.class);
    }

    public void evictAllCaches(){
        for(String name : cacheManager.getCacheNames()){
            cacheManager.getCache(name).clear();
        }
    }
}
