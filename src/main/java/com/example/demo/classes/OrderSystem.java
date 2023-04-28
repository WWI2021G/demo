package com.example.demo.classes;

import java.util.ArrayList;

public class OrderSystem
{
    private static OrderSystem instance = null;
    ArrayList<User> users;

    private OrderSystem()
    {
        users = new ArrayList<>();
    }

    // Singleton pattern to ensure that only one instance of OrderSystem exists using as data storage
    public static OrderSystem getInstance()
    {
        if (instance == null)
        {
            instance = new OrderSystem();
        }
        return instance;
    }

    public void addUser(User user)
    {
        int rand = (int) (Math.random() * 1000);
        user.setId(rand);
        users.add(user);
    }

    public void removeUser(User user)
    {
        users.remove(user);
    }

    public User getUserById(int id)
    {
        for (User u : users)
        {
            if (u.getId() == id)
            {
                return u;
            }
        }
        return null;
    }

    public ArrayList<User> getUserByDesk(String desk)
    {
        ArrayList<User> result = new ArrayList<>();
        for (User u : users)
        {
            if (u.getDesk().equals(desk))
            {
                result.add(u);
            }
        }
        return result;
    }
}
