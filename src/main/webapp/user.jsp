<%@ page import="com.example.demo.classes.Food" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% ArrayList<Food> foods = (ArrayList<Food>)request.getAttribute("foods"); %>

<html>
<head>
    <title>User</title>
</head>
<body>
    <h1>User Id ${user.id}</h1>

    <h3>Bestellung</h3>
    <table>
        <tr>
            <th>Name</th>
            <th>Preis</th>
        </tr>
        <% for (Food food : foods) { %>
            <tr>
                <td><%=food.getName()%></td>
                <td><%=food.getPrice()%></td>
            </tr>
        <% } %>
    </table>
    <br>
    <a href="add-new-food?userId=${user.id}">Neues Bestellung hinzufügen</a>
</body>
</html>
