package com.example.demo.servlets;

import com.example.demo.classes.Food;
import com.example.demo.classes.OrderSystem;
import com.example.demo.classes.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "user", value = "/user")
public class UserServlet extends HttpServlet
{
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        int userId = Integer.parseInt(req.getParameter("userId"));
        OrderSystem orderSystem = OrderSystem.getInstance();
        User user = orderSystem.getUserById(userId);
        ArrayList<Food> foods = user.getFood();

        req.setAttribute("user", user);
        req.setAttribute("foods", foods);

        req.getRequestDispatcher("/user.jsp").forward(req, resp);
    }
}
