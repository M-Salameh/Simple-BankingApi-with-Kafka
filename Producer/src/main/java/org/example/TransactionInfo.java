package org.example;

import org.apache.kafka.common.protocol.types.Field;

import java.security.PublicKey;
import java.security.SecureRandom;

public class TransactionInfo
{
    private String name;
    private String location;
    private double amount;

    public TransactionInfo()
    {}
    public TransactionInfo (String name , String location , double amount)
    {
        this.amount = amount;
        this.name = name;
        this.location = location;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount)
    {
        this.amount = amount;
    }
}