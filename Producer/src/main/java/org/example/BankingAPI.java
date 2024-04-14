package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import redis.clients.jedis.Jedis;

import java.util.Scanner;

public class BankingAPI {
    private static final String REDIS_HOST = "192.168.184.1";
    private static final int REDIS_PORT = 6379;
    private static Jedis jedis;
    public static void main(String[] args)
    {
        jedis = new Jedis(REDIS_HOST, REDIS_PORT);
        String name = getUserInput("Enter your name: ");
        String location = getUserInput("Enter your location: ");
        double amount = Double.parseDouble(getUserInput("Enter the transaction amount: "));


        String country = "";
        String jsonData = jedis.get(name);
        System.out.println(jsonData);
        ObjectMapper objectMapper = new ObjectMapper();
        try
        {
            MyRecord myRecord  = objectMapper.readValue(jsonData , MyRecord.class);
            System.out.println(myRecord.getCountry());
            country = myRecord.getCountry();

        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


        if (country == null)
        {
            System.out.println("User not found in the database.");
        } else
        {
            if (country.equals(location))
            {
                System.out.println("Transaction is valid.");
                TransactionInfo transactionInfo = new TransactionInfo(name , location , amount);
                System.out.println(transactionInfo.getAmount());
                boolean x = MyProducer.logTransaction(transactionInfo , "VALID_TOPIC");
                if (x)
                {
                    System.out.println("Success !!");
                }
                else
                {
                    System.out.println("ERROR");
                }
            }
            else
            {
                TransactionInfo transactionInfo = new TransactionInfo(name , location , amount);
                System.out.println(transactionInfo.getAmount());
                System.out.println("Transaction is suspicious.");
                boolean x = MyProducer.logTransaction(transactionInfo , "SUSPICIOUS_TOPIC");
                if (x)
                {
                    System.out.println("Success !!");
                }
                else
                {
                    System.out.println("ERRORR");
                }
            }
        }

        jedis.close();
    }

    private static String getUserInput(String prompt) {
        System.out.print(prompt);
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    private static String getCountry(String name)
    {
        String jsonData = jedis.get(name);
        System.out.println(jsonData);
        ObjectMapper objectMapper = new ObjectMapper();
        try
        {
            System.out.println("Trying");
            MyRecord myRecord  = objectMapper.readValue(jsonData , MyRecord.class);
            System.out.println("returning nourw");
            System.out.println(myRecord.getCountry());
            return myRecord.getCountry();
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}