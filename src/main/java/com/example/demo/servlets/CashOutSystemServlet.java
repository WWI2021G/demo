package com.example.demo.servlets;

import com.example.demo.classes.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

@WebServlet(value = "/cashout")
public class CashOutSystemServlet extends HttpServlet
{
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {

        String desk = req.getParameter("desk");
        OrderSystem orderSystem = OrderSystem.getInstance();
        ArrayList<User> usersOnTable = new ArrayList<>();

        if (desk == null)
        {
            desk = "1";
        }

        usersOnTable = orderSystem.getUserByDesk(desk);
        req.setAttribute("usersOnTable", usersOnTable);

        resp.setContentType("text/html");
        resp.getWriter().println("<html><body>");
        resp.getWriter().println("<h1>Cash Out System</h1>");
        resp.getWriter().println("<form action=\"cashout\" method=\"get\">");
        resp.getWriter().println("<label for=\"desk\">Tisch:</label>");
        resp.getWriter().println("<select id=\"desk\" name=\"desk\">");
        for (int i = 1; i <= 10; i++)
        {
            if (!desk.isEmpty() && desk.equals(String.valueOf(i)))
            {
                resp.getWriter().println("<option value=\"" + i + "\" selected>" + i + "</option>");
            }
            else
            {
                resp.getWriter().println("<option value=\"" + i + "\">" + i + "</option>");
            }
        }
        resp.getWriter().println("</select><br><br>");
        resp.getWriter().println("<input type=\"submit\" value=\"Submit\">");
        resp.getWriter().println("</form>");

        if (!desk.isEmpty() && !usersOnTable.isEmpty())
        {
            resp.getWriter().println("<h3>Users am Tisch</h3>");
            resp.getWriter().println("<form action=\"cashout?" + "desk=" + desk + "\" method=\"post\">");
            resp.getWriter().println("<select id=\"users\" name=\"users\" multiple>");
            for (User user : usersOnTable)
            {
                resp.getWriter().println("<option value=\"" + user.getId() + "\">" + user.getId() + "</option>");
            }
            resp.getWriter().println("</select><br><br>");
            resp.getWriter().println("<input type=\"text\" id=\"rabatt\" name=\"rabatt\" placeholder=\"Rabatt Code\"><br>");
            resp.getWriter().println("<label for=\"all\">Alle am Tisch bezahlen</label>");
            resp.getWriter().println("<input type=\"checkbox\" id=\"all\" name=\"all\" value=\"all\"><br>");
            resp.getWriter().println("<input type=\"submit\" value=\"Submit\">");

            resp.getWriter().println("</form>");

        }
        else if (!desk.isEmpty())
        {
            resp.getWriter().println("<h3>Keine User am Tisch gefunden</h3>");
        }

        resp.getWriter().println("</body></html>");
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        OrderSystem orderSystem = OrderSystem.getInstance();
        String[] users = req.getParameterValues("users");
        String deskString = req.getParameter("desk") != null ? req.getParameter("desk") : "";
        String rabatt = req.getParameter("rabatt") != null ? req.getParameter("rabatt") : "";
        boolean all = req.getParameter("all") != null;
        ArrayList<User> usersToCashOut = new ArrayList<>();
        RabattEnum rabattEnum = RabattEnum.findByName(rabatt);
        ArrayList<Integer> userIds = new ArrayList<>();

        if (all)
        {
            double totalCashOutPrice = 0;
            usersToCashOut = orderSystem.getUserByDesk(deskString);
            // Using Hashmap to group food by name
            HashMap<String, Integer> foodMap = new HashMap<>();

            for (User user : usersToCashOut)
            {
                userIds.add(user.getId());
                double userToCashOutPrice = user.getPrice();
                ArrayList<Food> allFoods = user.getFood();

                for (Food food : allFoods)
                {
                    // If food already exists in map, increase amount by 1
                    if (foodMap.containsKey(food.getName()))
                    {
                        foodMap.put(food.getName(), foodMap.get(food.getName()) + 1);
                    }
                    else
                    {
                        foodMap.put(food.getName(), 1);
                    }
                }

                totalCashOutPrice += userToCashOutPrice;
            }

            resp.getWriter().println("<html><body>");
            resp.getWriter().println("<h1>Rechnung</h1>");
            resp.getWriter().println("<h3>Tisch: " + deskString + "</h3>");
            resp.getWriter().println("<h3>Bestellung:</h3>");
            resp.getWriter().println("<table border=\"1\">");
            resp.getWriter().println("<tr>");
            resp.getWriter().println("<th>Produkt</th>");
            resp.getWriter().println("<th>Anzahl</th>");
            resp.getWriter().println("<th>Einzelpreis</th>");
            resp.getWriter().println("<th>Gesammtpreis</th>");
            resp.getWriter().println("</tr>");
            for (String foodName : foodMap.keySet())
            {
                resp.getWriter().println("<tr>");
                resp.getWriter().println("<td>" + foodName + "</td>");
                resp.getWriter().println("<td>" + foodMap.get(foodName) + "</td>");
                if (FoodEnum.findByName(foodName) != null)
                {
                    resp.getWriter().println("<td>" + FoodEnum.valueOf(foodName.toUpperCase()).getValue() + "</td>");
                    resp.getWriter().println("<td>" + FoodEnum.valueOf(foodName.toUpperCase()).getValue() * foodMap.get(foodName) + "</td>");
                }
                else if (DrinkEnum.findByName(foodName) != null)
                {
                    resp.getWriter().println("<td>" + DrinkEnum.valueOf(foodName.toUpperCase()).getValue() + "</td>");
                    resp.getWriter().println("<td>" + DrinkEnum.valueOf(foodName.toUpperCase()).getValue() * foodMap.get(foodName) + "</td>");
                }

                resp.getWriter().println("</tr>");
            }

            if (rabatt != null && !rabatt.isEmpty())
            {
                resp.getWriter().println("<tr>");
                resp.getWriter().println("<td>" + rabatt + "</td>");
                resp.getWriter().println("<td>1</td>");
                resp.getWriter().println("<td></td>");
                resp.getWriter().println("<td>" + (Math.round((totalCashOutPrice * RabattEnum.valueOf(rabatt.toUpperCase()).getValue()) * Math.pow(10, 2)) / Math.pow(10,2)) * -1 + "</td>");
                resp.getWriter().println("</tr>");

                totalCashOutPrice = totalCashOutPrice - (Math.round((totalCashOutPrice * RabattEnum.valueOf(rabatt.toUpperCase()).getValue()) * Math.pow(10, 2)) / Math.pow(10,2));
            }

            resp.getWriter().println("</table>");
            resp.getWriter().println("<h3>Gesammtpreis: " + totalCashOutPrice + "</h3>");
            resp.getWriter().println("<form action=\"payment\" method=\"post\">");
            resp.getWriter().println("<label for=\"cash\">Barzahlung</label>");
            resp.getWriter().println("<input type=\"number\" id=\"cash\" min=0 step=\"0.01\" name=\"cash\" placeholder=\"Wert\"><br>");
            resp.getWriter().println("<label for=\"card\">Karte</label>");
            resp.getWriter().println("<input type=\"checkbox\" id=\"card\" name=\"card\" value=\"card\"><br><br>");
            resp.getWriter().println("<input type=\"hidden\" id=\"desk\" name=\"desk\" value=\"" + deskString + "\">");
            for (int userId : userIds)
            {
                resp.getWriter().println("<input type=\"hidden\" id=\"users\" name=\"users\" value=\"" + userId + "\">");
            }
            resp.getWriter().println("<input type=\"hidden\" id=\"fullPrice\" name=\"fullPrice\" value=\"" + totalCashOutPrice + "\">");
            resp.getWriter().println("<input type=\"submit\" value=\"Bezahlen\">");
            resp.getWriter().println("</form>");
            resp.getWriter().println("</body></html>");

        }
        else
        {
            double totalCashOutPrice = 0;
            // Using Hashmap to group food by name
            HashMap<String, Integer> foodMap = new HashMap<>();

            for (String userId : users)
            {
                usersToCashOut.add(orderSystem.getUserById(Integer.parseInt(userId)));
            }

            for (User user : usersToCashOut)
            {
                userIds.add(user.getId());
                double userCashOutPrice = user.getPrice();
                ArrayList<Food> allFoods = user.getFood();

                for (Food food : allFoods)
                {
                    // If food already exists in map, increase amount by 1
                    if (foodMap.containsKey(food.getName()))
                    {
                        foodMap.put(food.getName(), foodMap.get(food.getName()) + 1);
                    }
                    else
                    {
                        foodMap.put(food.getName(), 1);
                    }
                }

                totalCashOutPrice += userCashOutPrice;
            }

            resp.getWriter().println("<html><body>");
            resp.getWriter().println("<h1>Rechnung</h1>");
            resp.getWriter().println("<h3>Tisch: " + deskString + "</h3>");
            resp.getWriter().println("<h3>Bestellung:</h3>");
            resp.getWriter().println("<table border=\"1\">");
            resp.getWriter().println("<tr>");
            resp.getWriter().println("<th>Gericht</th>");
            resp.getWriter().println("<th>Anzahl</th>");
            resp.getWriter().println("<th>Einzelpreis</th>");
            resp.getWriter().println("<th>Gesammtpreis</th>");
            resp.getWriter().println("</tr>");
            for (String foodName : foodMap.keySet())
            {
                resp.getWriter().println("<tr>");
                resp.getWriter().println("<td>" + foodName + "</td>");
                resp.getWriter().println("<td>" + foodMap.get(foodName) + "</td>");
                if (FoodEnum.findByName(foodName) != null)
                {
                    resp.getWriter().println("<td>" + FoodEnum.valueOf(foodName.toUpperCase()).getValue() + "</td>");
                    resp.getWriter().println("<td>" + FoodEnum.valueOf(foodName.toUpperCase()).getValue() * foodMap.get(foodName) + "</td>");
                }
                else if (DrinkEnum.findByName(foodName) != null)
                {
                    resp.getWriter().println("<td>" + DrinkEnum.valueOf(foodName.toUpperCase()).getValue() + "</td>");
                    resp.getWriter().println("<td>" + DrinkEnum.valueOf(foodName.toUpperCase()).getValue() * foodMap.get(foodName) + "</td>");
                }

                resp.getWriter().println("</tr>");
            }

            if (rabatt != null && !rabatt.isEmpty())
            {
                resp.getWriter().println("<tr>");
                resp.getWriter().println("<td>" + rabatt + "</td>");
                resp.getWriter().println("<td>1</td>");
                resp.getWriter().println("<td></td>");
                resp.getWriter().println("<td>" +  (Math.round((totalCashOutPrice * RabattEnum.valueOf(rabatt.toUpperCase()).getValue()) * Math.pow(10, 2)) / Math.pow(10,2))  * -1 + "</td>");
                resp.getWriter().println("</tr>");

                totalCashOutPrice = totalCashOutPrice - (Math.round((totalCashOutPrice * RabattEnum.valueOf(rabatt.toUpperCase()).getValue()) * Math.pow(10, 2)) / Math.pow(10,2));
            }

            resp.getWriter().println("</table>");
            resp.getWriter().println("<h3>Gesammtpreis: " + totalCashOutPrice + "</h3>");
            resp.getWriter().println("<form action=\"payment\" method=\"post\">");
            resp.getWriter().println("<label for=\"cash\">Barzahlung:</label>");
            resp.getWriter().println("<input type=\"number\" id=\"cash\" name=\"cash\" placeholder=\"Wert\"><br>");
            resp.getWriter().println("<label for=\"card\">Karte</label>");
            resp.getWriter().println("<input type=\"checkbox\" id=\"card\" name=\"card\" value=\"card\"><br><br>");
            resp.getWriter().println("<input type=\"hidden\" id=\"desk\" name=\"desk\" value=\"" + deskString + "\">");
             for (int userId : userIds)
            {
                resp.getWriter().println("<input type=\"hidden\" id=\"users\" name=\"users\" value=\"" + userId + "\">");
            }
            resp.getWriter().println("<input type=\"hidden\" id=\"fullPrice\" name=\"fullPrice\" value=\"" + totalCashOutPrice + "\">");
            resp.getWriter().println("<input type=\"submit\" value=\"Bezahlen\">");
            resp.getWriter().println("</form>");
            resp.getWriter().println("</body></html>");
        }
    }
}
