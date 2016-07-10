package org.apache.zookeeper.book.practice;

import org.apache.zookeeper.*;
import org.apache.zookeeper.book.ChildrenCache;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Author: haolin
 * On: 3/5/15
 */
public class Worker implements Watcher {

    private static final Logger LOG = LoggerFactory.getLogger(Worker.class);

    private ZooKeeper zk;

    private String connnectHost;

    private Random random = new Random();

    private String serverId = Integer.toHexString(random.nextInt());

    private String status;

    private String name;

    public Worker(String connnectHost) {
        this.connnectHost = connnectHost;
    }

    public void startZK() throws IOException {
        zk = new ZooKeeper(connnectHost, 15000, this);
    }

    @Override
    public void process(WatchedEvent event) {
        LOG.info(event.toString() + ", " + connnectHost);
    }

    /**
     * 注册自己为Worker
     */
    public void register() {
        name = "worker-" + serverId;
        zk.create(
            "/workers/worker-" + name, // worker标识
            "Idle".getBytes(), //状态空闲
            ZooDefs.Ids.OPEN_ACL_UNSAFE,
            CreateMode.EPHEMERAL, //临时节点
            createWorkerCallback,
            null
        );
    }

    /**
     * 创建Worker回调
     */
    private AsyncCallback.StringCallback createWorkerCallback = new AsyncCallback.StringCallback() {
        public void processResult(int rc, String path, Object ctx, String name) {
            switch (KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    register();
                    break;
                case OK:
                    LOG.info("Registered successfully: " + serverId);
                    break;
                case NODEEXISTS:
                    LOG.warn("Already registered: " + serverId);
                    break;
                default:
                    LOG.error("Something went wrong: "
                            + KeeperException.create(KeeperException.Code.get(rc), path));
            }

        }
    };

    private AsyncCallback.StatCallback statusUpdateCallback = new AsyncCallback.StatCallback() {
        public void processResult(int rc, String path, Object ctx, Stat stat) {
            switch(KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    updateStatus((String)ctx);
                    return;
            }
        }
    };

    synchronized private void updateStatus(String status) {
        if (status == this.status) {
            zk.setData(
                "/workers/" + name,
                status.getBytes(),
                -1, // -1将不会检查版本
                statusUpdateCallback,
                status
            );
        }
    }

    /**
     * 设置状态
     * @param status 新状态
     */
    public void setStatus(String status) {
        // working...
        // idle
        this.status = status;
        updateStatus(status);
    }

    /**
     * 新任务监听器
     */
    private Watcher newTaskWatcher = new Watcher() {
        public void process(WatchedEvent e) {
            if(e.getType() == Event.EventType.NodeChildrenChanged) {
                assert new String("/assign/worker-"+ serverId).equals( e.getPath() );
                getTasks();
            }
        }
    };

    /**
     * 获取Worker下的任务列表
     */
    public void getTasks() {
        zk.getChildren("/assign/worker-" + serverId,
                newTaskWatcher, tasksGetChildrenCallback, null);
    }

    /**
     * 通过线程池来执行任务
     */
    private ThreadPoolExecutor taskExecutor = new ThreadPoolExecutor(
            1, 1, 1000L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(200));

    /**
     * 分配给Worker的任务
     */
    protected ChildrenCache assignedTasksCache = new ChildrenCache();

    private AsyncCallback.ChildrenCallback tasksGetChildrenCallback = new AsyncCallback.ChildrenCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx, List<String> children) {
            switch(KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    // 尝试重新获取分配给自己的任务
                    getTasks();
                    break;
                case OK:
                    if(children != null) {
                        // 通过线程池获取任务数据，避免回调阻塞
                        taskExecutor.execute(new Runnable() {
                            List<String> children;
                            DataCallback cb;

                            public Runnable init (List<String> children, DataCallback cb) {
                                this.children = children;
                                this.cb = cb;
                                return this;
                            }

                            public void run() {
                                if(children == null) {
                                    return;
                                }
                                LOG.info("Looping into tasks");
                                setStatus("Working");
                                for(String task : children){
                                    LOG.trace("New task: {}", task);
                                    // 获取对应任务的数据
                                    zk.getData(
                                        "/assign/worker-" + serverId  + "/" + task,
                                        false,
                                        cb,
                                        task
                                    );
                                }
                            }
                        }.init(assignedTasksCache.addedAndSet(children), taskDataCallback));
                    }
                    break;
                default:
                    System.out.println("getChildren failed: " +
                            KeeperException.create(KeeperException.Code.get(rc), path));
            }
        }
    };

    AsyncCallback.DataCallback taskDataCallback = new AsyncCallback.DataCallback() {
        public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat){
            switch(KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    // 连接错误，再次尝试获取数据
                    zk.getData(path, false, taskDataCallback, null);
                    break;
                case OK:
                    // 在线程池中执行任务，避免回调阻塞
                    taskExecutor.execute( new Runnable() {
                        byte[] data;
                        Object ctx;

                        public Runnable init(byte[] data, Object ctx) {
                            this.data = data;
                            this.ctx = ctx;

                            return this;
                        }
                        public void run() {
                            LOG.info("Executing your task: " + new String(data));
                            // 创建任务执行状态为dome
                            zk.create("/status/" + (String) ctx, "done".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                                    CreateMode.PERSISTENT, taskStatusCreateCallback, null);
                            // 删除Worker下的任务分配
                            zk.delete("/assign/worker-" + serverId + "/" + (String) ctx,
                                    -1, taskVoidCallback, null);
                        }
                    }.init(data, ctx));
                    break;
                default:
                    LOG.error("Failed to get task data: ", KeeperException.create(KeeperException.Code.get(rc), path));
            }
        }
    };

    AsyncCallback.VoidCallback taskVoidCallback = new AsyncCallback.VoidCallback(){
        public void processResult(int rc, String path, Object rtx){
            switch(KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    break;
                case OK:
                    LOG.info("Task correctly deleted: " + path);
                    break;
                default:
                    LOG.error("Failed to delete task data" + KeeperException.create(KeeperException.Code.get(rc), path));
            }
        }
    };

    private AsyncCallback.StringCallback taskStatusCreateCallback = new AsyncCallback.StringCallback(){
        public void processResult(int rc, String path, Object ctx, String name) {
            switch(KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    zk.create(path + "/status", "done".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT,
                            taskStatusCreateCallback, null);
                    break;
                case OK:
                    LOG.info("Created status znode correctly: " + name);
                    break;
                case NODEEXISTS:
                    LOG.warn("Node exists: " + path);
                    break;
                default:
                    LOG.error("Failed to create task data: ", KeeperException.create(KeeperException.Code.get(rc), path));
            }

        }
    };

    public static void main(String args[]) throws Exception {
        Worker w = new Worker("localhost:2181");
        w.startZK();
        w.register();
        Thread.sleep(60000);
    }


}