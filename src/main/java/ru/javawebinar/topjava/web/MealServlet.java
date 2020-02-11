package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealsDao;
import ru.javawebinar.topjava.dao.MealsDaoMock;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(UserServlet.class);
    private MealsDao mealsDao;
    private static final int CALORIES_PER_DAY = 2000;

    @Override
    public void init() {
        mealsDao = new MealsDaoMock();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            log.trace("doGet params {}", request.getQueryString());
            String action = request.getParameter("action");
            log.debug("action={}", action);
            if (action != null) {
                action = action.toLowerCase();
                Long mealId = Long.parseLong(request.getParameter("mealId"));
                if ("delete".equals(action)) {
                    log.debug("deleting {}", mealId);
                    mealsDao.delete(mealId);
                    response.sendRedirect("meals");
                    return;
                } else if ("edit".equals(action)) {
                    log.debug("editing {}", mealId);
                    Meal editedMeal = mealsDao.getById(mealId);
                    request.setAttribute("editedMeal", editedMeal);
                }
            }
            renderingMeals(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            request.setCharacterEncoding("UTF-8");
            Long id = request.getParameter("id").isEmpty() ? null : Long.parseLong(request.getParameter("id"));
            LocalDateTime dateTime = LocalDateTime.parse(
                    request.getParameter("dateTime"), DateTimeFormatter.ISO_DATE_TIME);
            String description = request.getParameter("description");
            /* TODO: 11.02.2020 if use edit on item "Еда на граничное значение" in description rendering only "Еда".
            Your answer: В jsp возьми value в кавычки, все будет ок. And its WORKING!
            Think, that problem was in encoding, but how it works!!!? */
            int calories = Integer.parseInt(request.getParameter("calories"));

            log.debug("Saving meal(id= {} dateTime={}, description={}, calories={})",
                    id, dateTime, description, calories);
            mealsDao.save(new Meal(id, dateTime, description, calories));
            renderingMeals(request, response);
    }

    private void renderingMeals(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.trace("rendering All meals");
        Collection<Meal> mealsList = mealsDao.selectAll();
        List<MealTo> mealToList = MealsUtil.filteredByStreams(mealsList, LocalTime.MIN, LocalTime.MAX, CALORIES_PER_DAY);
        request.setAttribute("mealToList", mealToList);
        log.debug("forwarding to meals.jsp");
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }
}
