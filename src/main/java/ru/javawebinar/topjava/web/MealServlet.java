package ru.javawebinar.topjava.web;

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
import java.util.List;


public class MealServlet extends HttpServlet {
    private MealsDao mealsDao = new MealsDaoMock();
    private static final int CALORIES_PER_DAY = 2000;

    //todo whether to use logging?
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String action = request.getParameter("action");
            if ("delete".equalsIgnoreCase(action)) {
                int mealId = Integer.parseInt(request.getParameter("mealId"));
                mealsDao.deleteMeal(mealId);
                //TODO: whether to check the empty values while adding and changing. input types already do it...
            } else if ("edit".equalsIgnoreCase(action)) {
                int mealId = Integer.parseInt(request.getParameter("mealId"));
                Meal editedMeal = mealsDao.getById(mealId);
                request.setAttribute("editedMeal", editedMeal);
                // TODO: 08.02.2020 if use edit on item "Еда на граничное значение" in description rendering only "Еда". Why ???
            }

            List<Meal> mealsList = mealsDao.selectAll();
            List<MealTo> mealToList = MealsUtil.filteredByStreams(mealsList, LocalTime.MIN, LocalTime.MAX, CALORIES_PER_DAY);
            request.setAttribute("mealToList", mealToList);
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
        } catch (Exception e) {
            System.out.println("doGet error!"); //until logging
//            e.printStackTrace();
        }
//        response.sendRedirect("some error page");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.setCharacterEncoding("UTF-8");

            LocalDateTime localDateTime = LocalDateTime.parse(
                    request.getParameter("dateTime"), DateTimeFormatter.ISO_DATE_TIME);
            String localDescription = request.getParameter("description");
            int localCalories = Integer.parseInt(request.getParameter("calories"));

            if (!request.getParameter("id").isEmpty()) {
                long id = Long.parseLong(request.getParameter("id"));
                mealsDao.edit(id, localDateTime, localDescription, localCalories);
            } else {
                mealsDao.createMeal(localDateTime, localDescription, localCalories);
            }

            //todo next 4 strings are the same in doGet method. How improve?
            List<Meal> mealsList = mealsDao.selectAll();
            List<MealTo> mealToList = MealsUtil.filteredByStreams(mealsList, LocalTime.MIN, LocalTime.MAX, CALORIES_PER_DAY);
            request.setAttribute("mealToList", mealToList);
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
        } catch (Exception e) {
            System.out.println("doPost error!"); //until logging
//            e.printStackTrace();
        }
//        response.sendRedirect("some error page");

    }
}
