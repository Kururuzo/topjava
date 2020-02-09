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
    public void init() throws ServletException {
        super.init();
        mealsDao = MealsDaoMock.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();

        log.debug("beginning " + methodName);
        try {
            if (request.getParameter("action") != null) {
                log.debug(methodName + ", request parameter action presents");
                String action = request.getParameter("action");
                Long mealId = null;
                if (action != null) {
                    log.debug(methodName + ", action present, parsing mealId");
                    mealId = Long.parseLong(request.getParameter("mealId"));
                    log.debug(methodName + ", parsed mealId=" + mealId);
                }

                if ("delete".equalsIgnoreCase(action)) {
                    log.debug(methodName + ", deleting mealId=" + mealId);
                    if (!mealsDao.delete(mealId)) {
                        log.error(methodName + ", id was not found. Meal not deleted.");
                    }
                } else if ("edit".equalsIgnoreCase(action)) {
                    Meal editedMeal = mealsDao.getById(mealId);
                    if (editedMeal == null) {
                        log.error(methodName + ", meal was not found. id=null");
                    }
                    log.debug(methodName + ", setAttribute editedMeal for editing c" + mealId);
                    request.setAttribute("editedMeal", editedMeal);
                    // TODO: 08.02.2020 if use edit on item "Еда на граничное значение" in description rendering only "Еда". Why ???
                }
            } else {
                log.debug(methodName + ", there was no  action parameter in request");
            }
            renderingMeals(request, response, methodName);
        } catch (Exception e) {
            log.error(methodName + " error! " + e);
        }
//        response.sendRedirect("some error page");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        log.debug("beginning " + methodName);
        try {
            request.setCharacterEncoding("UTF-8");
            log.debug(methodName + ", parsing localDateTime");
            LocalDateTime localDateTime = LocalDateTime.parse(
                    request.getParameter("dateTime"), DateTimeFormatter.ISO_DATE_TIME);
            log.debug(methodName + ", parsing finished. localDateTime=" + localDateTime);
            String localDescription = request.getParameter("description");
            log.debug(methodName + ", parsing calories");
            int localCalories = Integer.parseInt(request.getParameter("calories"));
            log.debug(methodName + ", parsing finished. localCalories=" + localCalories);

            if (!request.getParameter("id").isEmpty()) {
                log.debug(methodName + ", id is not empty. start parsing id");
                long id = Long.parseLong(request.getParameter("id"));
                log.debug(methodName + ", parsed id=" + id + ". Editing meal.");
                mealsDao.edit(id, new Meal(id, localDateTime, localDescription, localCalories));
            } else {
                log.debug(methodName + ", id in request param was empty. Creating new Meal");
                mealsDao.create(new Meal(localDateTime, localDescription, localCalories));
            }
            renderingMeals(request, response, methodName);

        } catch (Exception e) {
            log.error(methodName + " error! " + e);
        }
//        response.sendRedirect("some error page");

    }

    private void renderingMeals(HttpServletRequest request, HttpServletResponse response, String methodName) throws ServletException, IOException {
        log.debug(methodName + ", selecting All meals");
        Collection<Meal> mealsList = mealsDao.selectAll();
        log.debug(methodName + ", filtering meals");
        List<MealTo> mealToList = MealsUtil.filteredByStreams(mealsList, LocalTime.MIN, LocalTime.MAX, CALORIES_PER_DAY);
        log.debug(methodName + ", setting Attribute \"mealToList\"");
        request.setAttribute("mealToList", mealToList);
        log.debug(methodName + ", redirect to meals.jsp");
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }
}
