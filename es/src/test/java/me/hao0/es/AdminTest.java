package me.hao0.es;

import org.elasticsearch.client.AdminClient;
import org.junit.Before;
import org.junit.Test;
import java.net.UnknownHostException;

/**
 * Author: haolin
 * Email:  haolin.h0@gmail.com
 */
public class AdminTest extends BaseTest {

    private AdminClient admin;

    @Before
    public void init() throws UnknownHostException {
        super.init();
        admin = client.admin();
    }

    @Test
    public void testAddIndex(){

    }
}
