package com.example.demo.classes;

public enum DrinkEnum
{
    WASSER(1.5),
    COLA(2.5),
    FANTA(2.5),
    SPRITE(2.5),
    BIER(3.5);

    // Price is the Parameter of the enum
    private final double price;
    DrinkEnum(double price)
    {
        this.price = price;
    }

    public double getValue() {
        return price;
    }

    // Find drink by name (case-insensitive) and return it
    public static DrinkEnum findByName(String name) {
        for (DrinkEnum drink : values()) {
            if (drink.name().equalsIgnoreCase(name)) {
                return drink;
            }
        }
        return null;
    }
}
