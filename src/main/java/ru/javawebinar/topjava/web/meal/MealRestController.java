package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    private MealService service;

    public MealRestController(MealService service) {
        this.service = service;
    }

    public List<Meal> getAll(LocalDate startDate, LocalDate endDate){
        log.info("getAll, authUserId={}, startDate={}, endDate={}", authUserId(), startDate, endDate);
        return service.getAll(authUserId(), startDate, endDate);
    }

    public List<Meal> getAll() {
        log.info("getAll, authUserId={}", authUserId());
        return service.getAll(authUserId());
    }

    public Meal get(int id) {
        log.info("get {}, authUserId={}", id, authUserId());
        return service.get(id, authUserId());
    }

    public Meal create (Meal meal) {
        log.info("create {}, authUserId={}", meal, authUserId());
        checkNew(meal);
        return service.create(meal, authUserId());
    }

    public void delete(int id) {
        log.info("delete {}, authUserId={}", id, authUserId());
        service.delete(id, authUserId());
    }

    public void update(Meal meal, int id) {
        log.info("update {} with id={}, authUserId={}", meal, id, authUserId());
        assureIdConsistent(meal, id);
        service.update(meal, authUserId());
    }
}