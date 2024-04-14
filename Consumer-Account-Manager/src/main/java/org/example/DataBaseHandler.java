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
        String jsonData = jedis.get(name);

        MyRecord myRecord = new MyRecord();
        boolean result = myRecord.modifyBalance(jsonData , amount);

        jsonData = myRecord.toString();

        if (jsonData == null) return false;
        jedis.set(name , jsonData);


        return result;
    }


}
