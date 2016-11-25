package me.hao0.storm;

import org.apache.storm.LocalCluster;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Author: haolin
 * Email:  haolin.h0@gmail.com
 */
public class LocalModeTest {

    private LocalCluster cluster;

    @Before
    public void init(){
        cluster = new LocalCluster();
    }

    @After
    public void destroy(){
        cluster.shutdown();
    }

    @Test
    public void testShutdown(){
        cluster.shutdown();
    }


}
