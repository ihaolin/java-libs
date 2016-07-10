package org.apache.zookeeper.book.practice;

import org.apache.zookeeper.*;
import org.apache.zookeeper.book.ChildrenCache;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * Author: haolin
 * On: 3/4/15
 */
public class Master implements Watcher {

    private static final Logger LOG = LoggerFactory.getLogger(Master.class);

    private ZooKeeper zk;

    private String connnectHost;

    private Random random = new Random();

    private String serverId = Integer.toHexString(random.nextInt());

    private boolean isLeader;

    public Master(String connnectHost){
        this.connnectHost = connnectHost;
    }

    public void startZK() throws IOException {
        zk = new ZooKeeper(connnectHost, 15000, this);
    }

    public void stopZk() throws InterruptedException {
        zk.close();
    }

//    public void runForMaster() throws KeeperException, InterruptedException {
//        while (true) {
//            try {
//                zk.create("/master", serverId.getBytes(),
//                        ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
//                // 创建/master节点成功
//                isLeader = true;
//                break;
//            } catch (KeeperException.NodeExistsException e) {
//                // /master已经存在
//                isLeader = false;
//                break;
//            } catch (KeeperException.ConnectionLossException e) {
//            }
//            if (checkMaster()) break;
//        }
//    }

//    private AsyncCallback.StringCallback masterCreateCb = new AsyncCallback.StringCallback() {
//        @Override
//        public void processResult(int rc, String path, Object ctx, String name) {
//            switch (KeeperException.Code.get(rc)){
//                case CONNECTIONLOSS:
//                    checkMaster();
//                    return;
//                case OK:
//                    isLeader = true;
//                    break;
//                default:
//                    isLeader = false;
//            }
//            System.out.println("I'm " + (isLeader ? "" : "not ") +
//                    "the leader");
//        }
//    };

    private AsyncCallback.StringCallback masterCreateCb = new AsyncCallback.StringCallback() {
        public void processResult(int rc, String path, Object ctx, String name) {
            switch (KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    checkMaster(); // 检查Master状态
                    break;
                case OK:
                    state = MasterStates.ELECTED; // 自己成为了Master
                    takeLeadership();             // 获取领导权
                    break;
                case NODEEXISTS:
                    state = MasterStates.NOTELECTED; // Master已经存在，自己没被选上
                    masterExists();                 //监听master存在性
                    break;
                default:
                    state = MasterStates.NOTELECTED;
                    LOG.error("Something went wrong when running for master.");
            }
        }
    };

    private MasterStates state;

    // Master状态
    private enum MasterStates {
        RUNNING,
        ELECTED,
        NOTELECTED
    }

    /**
     * 监听master存在性
     */
    private void masterExists() {
        zk.exists("/master",
                masterExistsWatcher, masterExistsCallback, null);
    }

    private AsyncCallback.StatCallback masterExistsCallback = new AsyncCallback.StatCallback() {
        public void processResult(int rc, String path, Object ctx, Stat stat) {
            switch (KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    masterExists(); // 连接失败，继续监听master存在性
                    break;
                case OK:
                    if(stat == null) { // master节点已不存在
                        state = MasterStates.RUNNING;
                        runForMaster(); // 选举自己为Master
                    }
                    break;
                default:
                    checkMaster(); // 检查Master状态
                    break;
            }
        }
    };

    // Master存在性监听器
    private Watcher masterExistsWatcher = new Watcher() {
        public void process(WatchedEvent e) {
            if(e.getType() == Event.EventType.NodeDeleted) { // 若master节点已被删除
                assert "/master".equals(e.getPath());
                runForMaster();
            }
        }
    };

    // 获取领导权
    private void takeLeadership() {
        // ...
    }

    public void runForMaster(){
        zk.create("/master", serverId.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL, masterCreateCb, null);
    }

    private AsyncCallback.DataCallback masterCheckCb = new AsyncCallback.DataCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
            switch(KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    checkMaster();
                    return;
                case NONODE:
                    runForMaster();
                    return;
                case NODEEXISTS:
                    System.out.println("node exists.");
            }
        }
    };

    private void checkMaster() {
        zk.getData("/master", false, masterCheckCb, null);
    }

    private Watcher workersChangeWatcher = new Watcher() {
        public void process(WatchedEvent e) {
            // Children发生变化，则重新获取最新的Worker列表
            if(e.getType() == Event.EventType.NodeChildrenChanged) {
                assert "/workers".equals(e.getPath());
                getWorkers();
            }
        }
    };

    private AsyncCallback.ChildrenCallback workersGetChildrenCallback = new AsyncCallback.ChildrenCallback() {
        public void processResult(int rc, String path, Object ctx, List<String> children) {
            switch (KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    // 发生连接错误，尝试重新获取Worker列表
                    getWorkers();
                    break;
                case OK:
                    LOG.info("Succesfully got a list of workers: " + children.size() + " workers");
                    // 重分配崩溃的Worker的任务给其他Worker
                    reassignAndSet(children);
                    break;
                default:
                    LOG.error("getChildren failed",
                            KeeperException.create(KeeperException.Code.get(rc), path));
            }
        }
    };

    /**
     * 监听worker列表
     */
    private void getWorkers() {
        zk.getChildren("/workers", workersChangeWatcher, workersGetChildrenCallback, null);
    }

    /**
     * 缓存最新的Worker列表
     */
    private ChildrenCache workersCache;

    /**
     * 重分配崩溃的Worker的任务给其他Worker
     */
    private void reassignAndSet(List<String> children) {
        List<String> toProcess = null;
        if(workersCache == null) {
            // 初始化缓存
            workersCache = new ChildrenCache(children);
        } else {
            LOG.info( "Removing and setting" );
            // 检查某些被删除的Worker
            toProcess = workersCache.removedAndSet(children);
        }
        if(toProcess != null) {
            for(String worker : toProcess) {
                // 重新分配删除的Worker的任务
                getAbsentWorkerTasks(worker);
            }
        }
    }

    private void getAbsentWorkerTasks(String worker) {
        // ...
    }

    /**
     * 任务列表变化监听
     */
    private Watcher tasksChangeWatcher = new Watcher() {
        public void process(WatchedEvent e) {
            if(e.getType() == Event.EventType.NodeChildrenChanged) {
                assert "/tasks".equals( e.getPath() );
                getTasks();
            }
        }
    };

    AsyncCallback.ChildrenCallback tasksGetChildrenCallback = new AsyncCallback.ChildrenCallback() {
        public void processResult(int rc, String path, Object ctx, List<String> children) {
            switch(KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    // 连接错误，尝试重新获取任务列表
                    getTasks();
                    break;
                case OK:
                    if(children != null) {
                        // 分配新任务
                        assignTasks(children);
                    }
                    break;
                default:
                    LOG.error("getChildren failed.",
                            KeeperException.create(KeeperException.Code.get(rc), path));
            }
        }
    };

    private void assignTasks(List<String> tasks) {
        for(String task : tasks) {
            getTaskData(task);
        }
    }


    private void getTaskData(String task) {
        zk.getData("/tasks/" + task,
                false, taskDataCallback, task);
    }

    private AsyncCallback.DataCallback taskDataCallback = new AsyncCallback.DataCallback(){
        @Override
        public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
            switch(KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    // 连接断开，再次尝试获取任务数据
                    getTaskData((String) ctx);
                    break;
                case OK:
                    // 选择一个Worker
                    int worker = random.nextInt(workersCache.getList().size());
                    String designatedWorker = workersCache.getList().get(worker);
                    String assignmentPath = "/assign/" + designatedWorker + "/" + ctx;
                    // 创建任务分配
                    createAssignment(assignmentPath, data);
                    break;
                default:
                    LOG.error("Error when trying to get task data.",
                            KeeperException.create(KeeperException.Code.get(rc), path));
            }
        }
    };

private void createAssignment(String path ,byte[]data){
        zk.create(path,
        data, ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT,
                assignTaskCallback,
                data);
    }

    private AsyncCallback.StringCallback assignTaskCallback = new AsyncCallback.StringCallback() {
        public void processResult(int rc, String path, Object ctx, String name) {
            switch(KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    createAssignment(path, (byte[]) ctx);
                    break;
                case OK:
                    LOG.info("Task assigned correctly: " + name);
                    // 分配完成后，删除任务
                    deleteTask(name.substring(name.lastIndexOf("/") + 1 ));
                    break;
                case NODEEXISTS:
        LOG.warn("Task already assigned");
        break;
default:
        LOG.error("Error when trying to assign task.",
                            KeeperException.create(KeeperException.Code.get(rc), path));
        }
        }
        };

/**
 * 删除任务
     * @param name
     */
    private void deleteTask(String name) {
        zk.delete("/tasks/" + name, -1, taskDeleteCallback, null);
    }

    private AsyncCallback.VoidCallback taskDeleteCallback = new AsyncCallback.VoidCallback(){
        public void processResult(int rc, String path, Object ctx){
            switch (KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    deleteTask(path);

                    break;
                case OK:
                    LOG.info("Successfully deleted " + path);

                    break;
                case NONODE:
                    LOG.info("Task has been deleted already");

                    break;
                default:
                    LOG.error("Something went wrong here, " +
                            KeeperException.create(KeeperException.Code.get(rc), path));
            }
        }
    };

    /**
     * 获取任务列表
     */
    public void getTasks() {
        zk.getChildren("/tasks", tasksChangeWatcher, tasksGetChildrenCallback, null);
    }

    public void bootstrap() {
        createParent("/workers", new byte[0]);
        createParent("/assign", new byte[0]);
        createParent("/tasks", new byte[0]);
        createParent("/status", new byte[0]);
    }

    private void createParent(String path, byte[] data) {
        zk.create(path,
            data,
            ZooDefs.Ids.OPEN_ACL_UNSAFE,
            CreateMode.PERSISTENT, // 持久节点
                createParentCb,
            data);
    }

    private AsyncCallback.StringCallback createParentCb  = new AsyncCallback.StringCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx, String name) {
            switch (KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    createParent(path, (byte[]) ctx);
                    break;
                case OK:
                    System.out.println("Parent created");
                    break;
                case NODEEXISTS:
                    System.out.println("Parent already registered: " + path);
                    break;
                default:
                    System.err.println("Something went wrong: " + KeeperException.create(KeeperException.Code.get(rc), path));
            }
        }
    };

    /**
     * 检查master是否存在
     * @return 存在返回true，反之false
     */
//    private boolean checkMaster() throws InterruptedException{
//        while (true) {
//            try {
//                Stat stat = new Stat();
//                byte data[] = zk.getData("/master", false, stat);
//                isLeader = new String(data).equals(serverId);
//                return true;
//            } catch (KeeperException.NoNodeException e) {
//                // no master, so try create again
//                return false;
//            } catch (KeeperException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    @Override
    public void process(WatchedEvent event) {
        System.out.print(event);
    }

    public static void main(String[] args) throws Exception {
        String connnectHost = "localhost:2181";
        Master m = new Master(connnectHost);
        m.startZK();
        m.runForMaster();
        if (m.isLeader){
            System.out.println("I am leader");
        } else {
            System.out.println("I am not leader");
        }
        Thread.sleep(600000);
    }
}
