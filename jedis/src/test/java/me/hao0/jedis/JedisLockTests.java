package me.hao0.jedis;

import com.github.jedis.lock.JedisLock;
import org.junit.Test;
import redis.clients.jedis.Jedis;

/**
 * Author: haolin
 * Date:   8/15/16
 * Email:  haolin.h0@gmail.com
 */
public class JedisLockTests {

    @Test
    public void testLock() throws InterruptedException {
        Jedis jedis = new Jedis("localhost");
        JedisLock lock = new JedisLock(jedis, "testmaster", 10000, 30000);
        lock.acquire();
        try {

        } finally {
            lock.release();
        }
    }
}
