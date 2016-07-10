package org.apache.zookeeper.book.practice;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Author: haolin
 * On: 3/9/15
 */
public class Client implements Watcher {

    private static final Logger LOG = LoggerFactory.getLogger(Worker.class);

    private ZooKeeper zk;

    private String connnectHost;

    public Client(String connnectHost) { this.connnectHost = connnectHost; }

    public void startZK() throws Exception {
        zk = new ZooKeeper(connnectHost, 15000, this);
    }

    public String queueCommand(String command) throws Exception {
        String name = null;
        while (true) {
            try {
                // 新节点名称
                name = zk.create("/tasks/task-",
                        command.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                        CreateMode.PERSISTENT_SEQUENTIAL); // 持久临时节点
                return name;
            } catch (KeeperException.NodeExistsException e) {
                throw new Exception(name + " already appears to be running");
            } catch (KeeperException.ConnectionLossException e) {
            }
        }
    }

    private void submitTask(String task, TaskObject taskCtx) {
        taskCtx.setTask(task);
        zk.create(
            "/tasks/task-",
            task.getBytes(),
            ZooDefs.Ids.OPEN_ACL_UNSAFE,
            CreateMode.PERSISTENT_SEQUENTIAL, // 持久连续节点
            createTaskCallback,
            taskCtx
        );
    }

    /**
     * 创建任务回调
     */
    private AsyncCallback.StringCallback createTaskCallback = new AsyncCallback.StringCallback(){
        @Override
        public void processResult(int rc, String path, Object ctx, String name) {
            switch (KeeperException.Code.get(rc)){
                case CONNECTIONLOSS:
                    // 连接失败，再次尝试提交任务
                    submitTask(((TaskObject)ctx).getTask(), (TaskObject) ctx);
                    break;
                case OK:
                    LOG.info("My created task name: " + name);
                    ((TaskObject) ctx).setTaskName(name);
                    // 监听任务状态
                    watchTaskStatus("/status/" + name.replace("/tasks/", ""), ctx);
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 关联任务节点路径和任务对象
     */
    private ConcurrentHashMap<String, Object> ctxMap = new ConcurrentHashMap<String, Object>();

    /**
     * 监听任务执行状态
     * @param path
     * @param ctx
     */
    private void watchTaskStatus(String path, Object ctx){
        ctxMap.put(path, ctx);
        zk.exists(
            path,
            taskStatusWatcher,
            taskExistsCallback,
            ctx
        );
    }

    private AsyncCallback.StatCallback taskExistsCallback = new AsyncCallback.StatCallback() {
        public void processResult(int rc, String path, Object ctx, Stat stat) {
            switch (KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    // 连接失败，尝试重新监听任务状态
                    watchTaskStatus(path, ctx);
                    break;
                case OK:
                    if (stat != null) {
                        // 获取任务数据
                        zk.getData(path, false, getDataCallback, null);
                    }
                    break;
                case NONODE:
                    break;
                default:
                    LOG.error("Something went wrong when " +
                            "checking if the status node exists: " + KeeperException.create(KeeperException.Code.get(rc), path));
                    break;
            }
        }
    };

    private Watcher taskStatusWatcher = new Watcher() {
        @Override
        public void process(WatchedEvent e) {
            if(e.getType() == Event.EventType.NodeCreated) {
                assert e.getPath().contains("/status/task-");
                zk.getData(e.getPath(),false,
                        getDataCallback, ctxMap.get(e.getPath()));
            }
        }
    };

    AsyncCallback.DataCallback getDataCallback = new AsyncCallback.DataCallback(){
        public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
            switch (KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    // 连接失败，尝试重新获取任务数据
                    zk.getData(path, false, getDataCallback, ctxMap.get(path));
                    return;
                case OK:
                    String taskResult = new String(data);
                    LOG.info("Task " + path + ", " + taskResult);
                    assert(ctx != null);
                    // 本地设置任务已完成
                    ((TaskObject) ctx).setStatus(taskResult.contains("done"));
                    // 删除任务状态节点
                    zk.delete(path, -1, taskDeleteCallback, null);
                    ctxMap.remove(path);
                    break;
                case NONODE:
                    LOG.warn("Status node is gone!");
                    return;
                default:
                    LOG.error("Something went wrong here, " +
                            KeeperException.create(KeeperException.Code.get(rc), path));
            }
        }
    };

    AsyncCallback.VoidCallback taskDeleteCallback = new AsyncCallback.VoidCallback(){
        public void processResult(int rc, String path, Object ctx){
            switch (KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    // 删除失败，尝试重新删除任务状态节点
                    zk.delete(path, -1, taskDeleteCallback, null);
                    ctxMap.remove(path);
                    break;
                case OK:
                    LOG.info("Successfully deleted " + path);
                    break;
                default:
                    LOG.error("Something went wrong here, " +
                            KeeperException.create(KeeperException.Code.get(rc), path));
            }
        }
    };

    @Override
    public void process(WatchedEvent event) {

    }

    public static void main(String args[]) throws Exception {
        Client c = new Client("localhost:2181");
        c.startZK();
        String name = c.queueCommand("some cmd");
        System.out.println("Created " + name);
    }
}




