package org.example;

import redis.clients.jedis.Jedis;

import java.util.Scanner;

public class BankingAPI {
    private static final String REDIS_HOST = "localhost";
    private static final int REDIS_PORT = 6379;

    public static void main(String[] args)
    {
        // Connect to Redis
        Jedis jedis = new Jedis(REDIS_HOST, REDIS_PORT);

        // Read user input
        String name = getUserInput("Enter your name: ");
        String location = getUserInput("Enter your location: ");
        double amount = Double.parseDouble(getUserInput("Enter the transaction amount: "));

        // Search for the user in the Redis database
        String country = jedis.hget("users", name);

        if (country == null) {
            System.out.println("User not found in the database.");
        } else {
            if (country.equals(location)) {
                System.out.println("Transaction is valid.");
                // Perform further processing for valid transaction
            } else {
                System.out.println("Transaction is suspicious.");
                // Perform further processing for suspicious transaction
            }
        }

        // Close the Redis connection
        jedis.close();
    }

    private static String getUserInput(String prompt) {
        System.out.print(prompt);
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
}