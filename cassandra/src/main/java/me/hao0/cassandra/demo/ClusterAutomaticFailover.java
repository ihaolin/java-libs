package me.hao0.cassandra.demo;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.policies.ConstantReconnectionPolicy;
import com.datastax.driver.core.policies.DowngradingConsistencyRetryPolicy;

/**
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 2/10/15
 */
public class ClusterAutomaticFailover {

    public static void main(String[] args){
        Cluster cluster = Cluster.builder()
                .addContactPoints("127.0.0.1", "127.0.0.2")
                .withRetryPolicy(DowngradingConsistencyRetryPolicy.INSTANCE)
                .withReconnectionPolicy(new ConstantReconnectionPolicy(100L))
                .build();
        Session session = cluster.connect();

        session.close();
    }
}
