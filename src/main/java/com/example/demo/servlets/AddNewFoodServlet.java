package com.example.demo.servlets;

import com.example.demo.classes.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "addNewFood", value = "/add-new-food")
public class AddNewFoodServlet extends HttpServlet
{
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String userId = req.getParameter("userId") != null ? req.getParameter("userId") : "";

        // Same as
        //        if (req.getParameter("userId") != null)
        //        {
        //            userId = req.getParameter("userId");
        //        }
        //        else
        //        {
        //            userId = "";
        //        }


        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        if (userId.isEmpty())
        {
            out.println("<html><body>");
            out.println("<h1>User not found</h1>");
            out.println("</body></html>");
            return;
        }

        out.println("<html><body>");
        out.println("<h1>Add new Food</h1>");
        out.println("<form action=\"add-new-food\" method=\"post\">");
        out.println("<label for=\"food\">Speisen:</label>");
        out.println("<select id=\"food\" name=\"food\">" +
                "<option disabled selected value> -- Select an Option -- </option>" +
                "<option value=\"pizza\">Pizza</option>" +
                "<option value=\"pasta\">Pasta</option>" +
                "<option value=\"salat\">Salat</option>" +
                "<option value=\"burger\">Burger</option>" +
                "<option value=\"doener\">Döner</option>" +
                "</select><br><br>");

        out.println("<label for=\"drinks\">Getränke:</label>");
        out.println("<select id=\"drinks\" name=\"drinks\">" +
                "<option disabled selected value> -- Select an Option -- </option>" +
                "<option value=\"wasser\">Wasser</option>" +
                "<option value=\"cola\">Cola</option>" +
                "<option value=\"fanta\">Fanta</option>" +
                "<option value=\"sprite\">Sprite</option>" +
                "<option value=\"bier\">Bier</option>" +
                "</select><br><br>");
        out.println("<input type=\"hidden\" name=\"userId\" value=\"" + userId + "\">");
        out.println("<input type=\"submit\" value=\"Submit\">");
        out.println("</form>");
        out.println("</body></html>");
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String userId = req.getParameter("userId");
        String foodIdentifier = req.getParameter("food");
        String drinksIdentifier = req.getParameter("drinks");
        OrderSystem orderSystem = OrderSystem.getInstance();

        FoodEnum foodEnum = FoodEnum.findByName(foodIdentifier);
        DrinkEnum drinksEnum = DrinkEnum.findByName(drinksIdentifier);

        User user = orderSystem.getUserById(Integer.parseInt(userId));

        if (foodEnum != null)
        {
            user.addFood(new Food(foodIdentifier, foodEnum.getValue()));
        }

        if (drinksEnum != null)
        {
            user.addFood(new Food(drinksIdentifier, drinksEnum.getValue()));
        }

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        out.println("<html><body>");
        out.println("<h1>Order successful</h1>");
        out.println("<a href=\"user?userId=" + userId + "\">Zur&Uumlck</a>");
        out.println("</body></html>");
    }
}
