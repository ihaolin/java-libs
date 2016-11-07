package me.hao0.es;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.junit.Before;
import org.junit.Test;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Author: haolin
 * Email:  haolin.h0@gmail.com
 */
public class BaseTest {

    /**
     * real client
     */
    protected TransportClient client;

    @Before
    public void init() throws UnknownHostException {
        Settings settings =
                Settings.settingsBuilder()
                        .put("cluster.name", "local-cluster")
                        .put("client.transport.sniff", true)
                        .build();
        client = TransportClient.builder().settings(settings).build();
        client.addTransportAddress(
                new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
    }

    @Test
    public void indexCreate(){

    }
}
