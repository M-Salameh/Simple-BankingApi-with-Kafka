package org.example;

import java.nio.DoubleBuffer;

public class Customer
{
    private String name;
    private String originalCountry;
    private double cash;

    public Customer (String name , String originalCountry , double cash)
    {
        this.name = name;
        this.originalCountry = originalCountry;
        this.cash = cash;
    }

    public double getCash()
    {
        return cash;
    }
    public String getName()
    {
        return name;
    }
    public String getOriginalCountry()
    {
        return originalCountry;
    }

    public boolean withdrawMoney (double amount)
    {
        cash -= amount;
        if (cash < 0)
        {
            cash += amount;
            return false;
        }
        return true;
    }

    public boolean depositMoney (double amount)
    {
        if (cash + amount < cash) return false;
        cash += amount;
        return true;
    }
}
