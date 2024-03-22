package org.example;

import redis.clients.jedis.Jedis;

public class DataBaseHandler
{
    private static final String REDIS_HOST = "localhost";
    private static final int REDIS_PORT = 6379;

    private static final String BALANCE_FIELD = "balance";
    private static Jedis jedis = new Jedis(REDIS_HOST, REDIS_PORT);

    public static boolean editAmount(String name , double amount)
    {
        String _balance = jedis.hget(name, BALANCE_FIELD);
        Double balance = Double.parseDouble(_balance);
        if (amount > balance) return false;
        balance -= amount;
        jedis.hset(name , BALANCE_FIELD , balance.toString());
        return true;
    }


}
