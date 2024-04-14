package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import redis.clients.jedis.Jedis;

public class DataBaseHandler
{
    private static final String REDIS_HOST = "localhost";
    private static final int REDIS_PORT = 6379;

    private static final String SUSPICIOUS_FIELD = "suspicious";
    private static Jedis jedis = new Jedis(REDIS_HOST, REDIS_PORT);

    public static void notifyUser(String name)
    {

        String jsonData = jedis.get(name);

        MyRecord myRecord = new MyRecord();
        myRecord.increaseNotification(jsonData);

        jsonData = myRecord.toString();

        if ((jsonData == null) ) return;

        jedis.set(name , jsonData);

    }
}
