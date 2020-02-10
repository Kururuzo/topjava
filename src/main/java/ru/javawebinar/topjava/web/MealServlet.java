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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            log.trace("doGet params {}", request.getQueryString());

            String action = request.getParameter("action");
            log.debug("action={}", action);
            if (action != null) {
                action = action.toLowerCase();
                Long mealId = Long.parseLong(request.getParameter("mealId"));
                if ("delete".equals(action)) {
                    log.debug("deleting {}", mealId);
                    if (!mealsDao.delete(mealId)) {
                        log.error("id was not found while deleting. Meal was not deleted.");
                    }
                } else if ("edit".equals(action)) {
                    log.debug("editing {}", mealId);
                    Meal editedMeal = mealsDao.getById(mealId);
                    if (editedMeal == null) {
                        log.error("meal was not found while editing. id=null");
                    }
                    request.setAttribute("editedMeal", editedMeal);
                }
            }
            renderingMeals(request, response);
            //TODO i dont know what else to refactor...
        } catch (Exception e) {
            log.error("doGet error!", e);
        }
//        response.sendRedirect("some error page");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setCharacterEncoding("UTF-8");
            Long id = request.getParameter("id").isEmpty() ? null : Long.parseLong(request.getParameter("id"));
            LocalDateTime dateTime = LocalDateTime.parse(
                    request.getParameter("dateTime"), DateTimeFormatter.ISO_DATE_TIME);
            String description = request.getParameter("description");
            /* TODO: 10.02.2020 if use edit on item "Еда на граничное значение" in description rendering only "Еда".
            Your answer: В jsp возьми value в кавычки, все будет ок. And its WORKING!
            Think, that problem was in encoding, but how it works!!!? */
            int calories = Integer.parseInt(request.getParameter("calories"));

            log.debug("Saving meal(id= {} dateTime={}, description={}, calories={})",
                    id, dateTime, description, calories);
            mealsDao.save(new Meal(id, dateTime, description, calories));
            renderingMeals(request, response);
        } catch (Exception e) {
            log.error("doPost error!", e);
        }
//        response.sendRedirect("some error page");

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
