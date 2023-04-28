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
import java.util.List;

@WebServlet(value = "/payment")
public class PaymentServlet extends HttpServlet
{
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        // Liste der User, die bezahlen
        String[] users = req.getParameterValues("users");
        String fullPrice = req.getParameter("fullPrice");
        boolean card = req.getParameter("card") != null;
        String cash = req.getParameter("cash");
        OrderSystem orderSystem = OrderSystem.getInstance();
        ArrayList<User> userList = new ArrayList<>();

        for (String user : users)
        {
            userList.add(orderSystem.getUserById(Integer.parseInt(user)));
        }

        if (card && cash.isEmpty())
        {
            resp.getWriter().println("<html><body>");
            resp.getWriter().println("<h1>Vielen Dank für Ihre Bezahlung!</h1>");
            resp.getWriter().println("<h2>Der Betrag von " + fullPrice + "€ wurde von Ihrer Karte abgebucht.</h2>");
            resp.getWriter().println("</body></html>");

            for (User user : userList)
            {
                orderSystem.removeUser(user);
            }

        }
        else if (!card && !cash.isEmpty())
        {
            double change = Double.parseDouble(cash) - Double.parseDouble(fullPrice);

            if (change < 0)
            {
                resp.getWriter().println("<html><body>");
                resp.getWriter().println("<h1>Es ist ein Fehler aufgetreten!</h1>");
                resp.getWriter().println("<h2>Bitte geben Sie genug Geld ein</h2>");
                resp.getWriter().println("<button onclick=\"history.back()\">Zurück</button>");
                resp.getWriter().println("</body></html>");
                return;
            }

            resp.getWriter().println("<html><body>");
            resp.getWriter().println("<h1>Vielen Dank für Ihre Bezahlung!</h1>");
            resp.getWriter().println("<h2>Der Betrag von " + fullPrice + "€ wurde von Ihnen in Bar bezahlt.</h2>");
            resp.getWriter().println("<h2>Ihr Rückgeld beträgt " + change + "€.</h2>");
            resp.getWriter().println("</body></html>");

            for (User user : userList)
            {
                orderSystem.removeUser(user);
            }
        }
        else
        {
            resp.getWriter().println("<html><body>");
            resp.getWriter().println("<h1>Es ist ein Fehler aufgetreten!</h1>");
            resp.getWriter().println("<h2>Bitte Richtige Zahlungsart wählen</h2>");
            resp.getWriter().println("<button onclick=\"history.back()\">Zurück</button>");
            resp.getWriter().println("</body></html>");
        }
    }
}
