package com.example.demo.classes;

import java.util.ArrayList;
import java.util.List;

public class User
{
    private int id;
    private String desk;
    private ArrayList<Food> food;

    public User(String desk)
    {
        this.desk = desk;
        this.food = new ArrayList<>();
    }

    public String getDesk()
    {
        return desk;
    }

    public void addFood(Food food)
    {
        this.food.add(food);
    }

    public ArrayList<Food> getFood()
    {
        return food;
    }

    public double getPrice()
    {
        double price = 0;
        for (Food f : food)
        {
            price += f.getPrice();
        }
        return price;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return id;
    }

}
