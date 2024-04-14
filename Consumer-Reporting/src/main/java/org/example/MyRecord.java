package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MyRecord
{
    private String country;
    private Double balance;

    public MyRecord(){}


    public MyRecord(String country , Double balance )
    {
        this.balance = balance;
        this.country = country;
        //this.warnings = warnings;
    }



    @Override
    public String toString()
    {
        String ans = null;

        ObjectMapper objectMapper = new ObjectMapper();
        try
        {
            ans = objectMapper.writeValueAsString(this);
        }
        catch (JsonProcessingException e)
        {
            throw new RuntimeException(e);
        }
        finally {return ans;}
    }

    public String getCountry ()
    {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}

