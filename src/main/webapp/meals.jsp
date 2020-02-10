<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

<html>
<head>
    <title>Meals</title>
    <style>
        @import url("css/table.css");
    </style>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>

<h2>Meals</h2>
<table>
    <tr>
        <th>Дата/Время</th>
        <th>Описание</th>
        <th>Калории</th>
        <th colspan="2"></th>
    </tr>
    <jsp:useBean id="mealToList" scope="request" type="java.util.List"/>
    <c:forEach var="mealTo" items="${mealToList}">
        <tr style="color: ${mealTo.excess? 'red' : 'darkgreen'}">
            <td>${mealTo.dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))}</td>
            <td>${mealTo.description}</td>
            <td>${mealTo.calories}</td>
            <td><a href="meals?action=edit&mealId=${mealTo.id}">Edit</a></td>
            <td><a href="meals?action=delete&mealId=${mealTo.id}">Delete</a></td>
        </tr>
    </c:forEach>
    <tr>
        <form name="addAndEditForm" action="meals" method="post">
            <input type="hidden" name="id" value=${editedMeal.id}>
            <td><input type="datetime-local" max='9999-12-31T23:59:59' name="dateTime" value=${editedMeal.dateTime}></td>
            <td><input type="text" name="description" value="${editedMeal.description}"></td>
            <td><input type="number" name="calories" min='1' max ='2147483647' value=${editedMeal.calories}></td>
            <td><input type="submit" value="Submit"></td>
            <td><input type="reset"></td>
        </form>
    </tr>
</table>

</body>
</html>