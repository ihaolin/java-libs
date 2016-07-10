package curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;

import java.util.List;

/**
 * Author: haolin
 * Date  : 8/27/15.
 * Email : haolin.h0@gmail.com
 */
public class CuratorTests {

    @Test
    public void testConnection(){
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient("localhost:2181", retryPolicy);
        client.start();
        client.close();
    }

    @Test
    public void testCreateEphemeral() throws Exception {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient("localhost:2181", retryPolicy);
        client.start();
        System.out.println(client.create().withMode(CreateMode.EPHEMERAL).forPath("/hello_path", "hello".getBytes()));
        Thread.sleep(30000);
        client.close();
    }

    @Test
    public void testCreateEphemeralSequential() throws Exception {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient("localhost:2181", retryPolicy);
        client.start();
        Stat pathStat = client.checkExists().forPath("/push/servers");
        if (pathStat == null){
            client.create().forPath("/push/servers");
        }
        System.out.println(client.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath("/push/servers/server-", "hello".getBytes()));
        Thread.sleep(30000);
        client.close();
    }

    @Test
    public void testGetChildren() throws Exception {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient("localhost:2181", retryPolicy);
        client.start();
        List<String> paths = client.getChildren().forPath("/application");
        for (String path : paths){
            System.out.println(path);
        }
    }

    @Test
    public void testGetData() throws Exception {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient("localhost:2181", retryPolicy);
        client.start();
        String data = new String(client.getData().forPath("/application/superApp"));
        System.out.println(data);
    }

    @Test
    public void testPathChildrenCache() throws Exception {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient("localhost:2181", retryPolicy);
        client.start();
        PathChildrenCache serverCache = new PathChildrenCache(client, "/", false);
        serverCache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                PathChildrenCacheEvent.Type eventType = event.getType();
                ChildData node;
                switch (eventType) {
                    case CHILD_ADDED:
                        node = event.getData();
                        System.out.println("add [" + node.getPath() + "]");
                        break;
                    case CHILD_REMOVED:
                        node = event.getData();
                        System.out.println("remove [" + node.getPath() + "]");
                        break;
                    case CHILD_UPDATED:
                        node = event.getData();
                        System.out.println("update [" + node.getPath() + "]");
                        break;
                    default:
                        break;
                }
            }
        });
        serverCache.start();

        Thread.sleep(1000000000);
    }
}
