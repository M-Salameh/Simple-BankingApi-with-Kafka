package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MyRecord
{
    /*SET JohnDoe "{\"id\": 1, \"name\": \"John Doe\", \"email\": \"johndoe@example.com\"}"*/

    private String country;
    private Double balance;
    private Integer warnings;

    public MyRecord(){}

    public MyRecord(String country , double balance , int warnings)
    {
        this.balance = balance;
        this.country = country;
        this.warnings = warnings;
    }
    public Integer getWarnings() {
        return warnings;
    }

    public void setWarnings(Integer warnings) {
        this.warnings = warnings;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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

    public boolean modifyBalance(String jsonData , double amount)
    {
        try
        {
            MyRecord myRecord = reverseJson(jsonData);
            if (myRecord.balance >= amount)
            {
                myRecord.balance -= amount;
                return  true;
            }
            return false;
        }
        catch (JsonProcessingException e)
        {
            throw new RuntimeException(e);
        }
    }
    private MyRecord reverseJson(String json) throws JsonProcessingException
    {
        ObjectMapper objectMapper = new ObjectMapper();
        MyRecord myRecord = objectMapper.readValue(json , MyRecord.class);
        return myRecord;
    }
}

