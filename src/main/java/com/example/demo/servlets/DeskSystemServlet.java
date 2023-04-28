package com.example.demo.servlets;

import com.example.demo.classes.OrderSystem;
import com.example.demo.classes.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

@WebServlet(name = "deskSystem", value = "/desk-system")
public class DeskSystemServlet extends HttpServlet
{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {

        String desk = req.getParameter("desk");

        if (desk == null)
        {
            desk = "";
        }

        resp.setContentType("text/html");
        resp.getWriter().println("<html><body>");
        resp.getWriter().println("<h1>Desk System</h1>");
        resp.getWriter().println("<form action=\"desk-system\" method=\"get\">");
        resp.getWriter().println("<label for=\"desk\">Tisch:</label>");
        resp.getWriter().println("<select id=\"desk\" name=\"desk\">");
        for (int i = 1; i <= 10; i++)
        {
            if (!desk.isEmpty() && Objects.equals(desk, String.valueOf(i)))
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

        if (!Objects.equals(desk, ""))
        {
            OrderSystem orderSystem = OrderSystem.getInstance();
            ArrayList<User> usersOnTable = orderSystem.getUserByDesk(desk);

            if (!usersOnTable.isEmpty())
            {
                resp.getWriter().println("<h3>Users am Tisch</h3>");
                resp.getWriter().println("<table border=\"1\">");
                resp.getWriter().println("<tr><th>User</th></tr>");
                for (User user : usersOnTable)
                {
                    resp.getWriter().println("<tr>");
                    resp.getWriter().println("<td><a href=\"user?userId=" + user.getId() + "\">" + user.getId() + "</a></td>");
                    resp.getWriter().println("</tr>");
                }
                resp.getWriter().println("</table>");
                resp.getWriter().println("<br>");
                resp.getWriter().println("<a href=\"cashout?desk=" + desk + "\"" + ">Cash Out</a>");
            }
            else
            {
                resp.getWriter().println("<h3>Keine User am Tisch</h3>");

            }
        }

        resp.getWriter().println("<br><br>");
        resp.getWriter().println("<a href=\"order-system?desk=" + desk + "\"" + ">Create new Order</a>");
    }

}
