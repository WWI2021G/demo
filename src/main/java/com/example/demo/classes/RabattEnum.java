package com.example.demo.classes;

public enum RabattEnum
{
    RABATT1(0.1),
    RABATT2(0.2),
    RABATT3(0.3);

    // Percentage is the Parameter of the enum
    private final double percentage;
    RabattEnum(double percentage)
    {
        this.percentage = percentage;
    }
    public double getValue() {
        return percentage;
    }

    // Find rabatt by name (case-insensitive) and return it
    public static RabattEnum findByName(String name) {
        for (RabattEnum rabatt : values()) {
            if (rabatt.name().equalsIgnoreCase(name)) {
                return rabatt;
            }
        }
        return null;
    }
}
