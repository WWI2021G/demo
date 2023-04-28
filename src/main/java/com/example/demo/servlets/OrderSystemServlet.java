package com.example.demo.servlets;

import com.example.demo.classes.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "orderSystem", value = "/order-system")
public class OrderSystemServlet extends HttpServlet
{

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        String desk = request.getParameter("desk");

        if (desk == null)
        {
            desk = "";
        }

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html><body>");
        out.println("<h1>Order System</h1>");
        out.println("<form action=\"order-system\" method=\"post\">");

        out.println("<label for=\"desk\">Tisch:</label>");
        out.println("<select id=\"desk\" name=\"desk\">");
        for (int i = 1; i <= 10; i++)
        {
            if (!desk.isEmpty() && desk.equals(String.valueOf(i)))
            {
                out.println("<option value=\"" + i + "\" selected>" + i + "</option>");
            }
            else
            {
                out.println("<option value=\"" + i + "\">" + i + "</option>");
            }
        }
        out.println("</select><br><br>");

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
        out.println("<input type=\"submit\" value=\"Submit\">");
        out.println("</form>");
        out.println("</body></html>");
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        String foodIdentifier = req.getParameter("food");
        String drinksIdentifier = req.getParameter("drinks");
        User user = new User(req.getParameter("desk"));
        OrderSystem orderSystem = OrderSystem.getInstance();

        FoodEnum foodEnum = FoodEnum.findByName(foodIdentifier);
        DrinkEnum drinksEnum = DrinkEnum.findByName(drinksIdentifier);

        if (foodEnum == null && drinksEnum == null)
        {
            out.println("<html><body>");
            out.println("<h1>Order System</h1>");
            out.println("<h2>Invalid input</h2>");
            out.println("<a href=\"order-system\">Back</a>");
            out.println("</body></html>");
            return;
        }
        else if (foodEnum != null && drinksEnum != null)
        {
            orderSystem.addUser(user);
            user.addFood(new Food(drinksIdentifier, drinksEnum.getValue()));
            user.addFood(new Food(foodIdentifier, foodEnum.getValue()));
            resp.sendRedirect(req.getContextPath() + "/desk-system?desk=" + req.getParameter("desk"));
            return;
        }
        else if (foodEnum == null)
        {
            orderSystem.addUser(user);
            user.addFood(new Food(drinksIdentifier, drinksEnum.getValue()));
            resp.sendRedirect(req.getContextPath() + "/desk-system?desk=" + req.getParameter("desk"));
            return;
        }
        else if (drinksEnum == null)
        {
            orderSystem.addUser(user);
            user.addFood(new Food(foodIdentifier, foodEnum.getValue()));
            resp.sendRedirect(req.getContextPath() + "/desk-system?desk=" + req.getParameter("desk"));
        }
    }

    public void destroy() {
    }
}
