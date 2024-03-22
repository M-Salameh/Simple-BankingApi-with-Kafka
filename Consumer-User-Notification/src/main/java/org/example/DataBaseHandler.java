package org.example;

import redis.clients.jedis.Jedis;

public class DataBaseHandler
{
    private static final String REDIS_HOST = "localhost";
    private static final int REDIS_PORT = 6379;

    private static final String SUSPICIOUS_FIELD = "suspicious";
    private static Jedis jedis = new Jedis(REDIS_HOST, REDIS_PORT);

    public static void notifyUser(String name)
    {
        String _notify_num = jedis.hget(name, SUSPICIOUS_FIELD);
        Integer notify_num = Integer.parseInt(_notify_num);
        notify_num++;
        jedis.hset(name , SUSPICIOUS_FIELD , notify_num.toString());
    }


}
