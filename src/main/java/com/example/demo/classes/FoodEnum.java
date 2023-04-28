package com.example.demo.classes;

public enum FoodEnum
{
    PIZZA(10),
    PASTA(8),
    SALAT(5),
    BURGER(7),
    Doener(6);

    // Price is the Parameter of the enum
    private final double price;
    FoodEnum(double price)
    {
        this.price = price;
    }

    public double getValue() {
        return price;
    }

    // Find food by name (case-insensitive) and return it
    public static FoodEnum findByName(String name) {
        for (FoodEnum food : values()) {
            if (food.name().equalsIgnoreCase(name)) {
                return food;
            }
        }
        return null;
    }
}
