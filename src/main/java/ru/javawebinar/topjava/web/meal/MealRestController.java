package ru.javawebinar.topjava.web.meal;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.springframework.format.annotation.DateTimeFormat.ISO.*;

@RestController
@RequestMapping(value = MealRestController.MEAL_REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class MealRestController extends AbstractMealController {
    public static final String MEAL_REST_URL = "/rest/meals";

    @Override
    @GetMapping("/{id}")
    public Meal get(@PathVariable int id) {
        log.debug("get meal with id={}", id);
        return super.get(id);
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.debug("deleting meal with id={}", id);
        super.delete(id);
    }

    @Override
    @GetMapping
    public List<MealTo> getAll() {
        log.debug("get all meals");
        return super.getAll();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Meal> createNew(@RequestBody Meal meal) {
        Meal created = super.create(meal);
        URI uriOfcreatedResourse = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(MEAL_REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        log.debug("meal with URI={} created", uriOfcreatedResourse);
        return ResponseEntity.created(uriOfcreatedResourse).body(created);
    }

    @Override
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@RequestBody Meal meal, @PathVariable int id) {
        super.update(meal, id);
    }

    @GetMapping("/filter")
    @DateTimeFormat(iso = DATE_TIME)
//    public List<MealTo> getBetween(@RequestParam LocalDate startDate, @RequestParam LocalTime startTime,
//                                   @RequestParam LocalDate endDate, @RequestParam LocalTime endTime) {
//            return super.getBetween(startDate, startTime, endDate, endTime);

    public List<MealTo> getBetweenRest(@RequestParam @DateTimeFormat(iso = DATE) LocalDate startDate,
                                       @DateTimeFormat(iso = TIME) @RequestParam LocalTime startTime,
                                       @DateTimeFormat(iso = DATE) @RequestParam LocalDate endDate,
                                       @DateTimeFormat(iso = TIME) @RequestParam LocalTime endTime) {

        return super.getBetween(startDate, startTime, endDate, endTime);
    }
}