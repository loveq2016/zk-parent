package com.fsmeeting.zkclient.mastersel;

import com.fsmeeting.bean.ServerMessage;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkException;
import org.I0Itec.zkclient.exception.ZkInterruptedException;
import org.I0Itec.zkclient.exception.ZkNoNodeException;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;
import org.apache.zookeeper.CreateMode;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WorkServer {

    private volatile boolean running = false;

    private ZkClient zkClient;

    private static final String MASTER_PATH = "/master";

    private IZkDataListener dataListener;

    private ServerMessage workServer;

    private ServerMessage masterServer;

    private ScheduledExecutorService delayExector = Executors.newScheduledThreadPool(1);
    private int delayTime = 5;

    public WorkServer(ServerMessage server) {
        this.workServer = server;
        this.dataListener = new IZkDataListener() {

            public void handleDataDeleted(String dataPath) throws Exception {

                if (masterServer != null && masterServer.getName().equals(workServer.getName())) {
                    takeMaster();

                } else {//处理网络抖动
                    delayExector.schedule(new Runnable() {
                        public void run() {
                            takeMaster();
                        }
                    }, delayTime, TimeUnit.SECONDS);

                }


            }

            public void handleDataChange(String dataPath, Object data)
                    throws Exception {
                // TODO Auto-generated method stub

            }
        };
    }

    public ZkClient getZkClient() {
        return zkClient;
    }

    public void setZkClient(ZkClient zkClient) {
        this.zkClient = zkClient;
    }

    public void start() throws Exception {
        if (running) {
            throw new Exception("server has startup...");
        }
        running = true;
        zkClient.subscribeDataChanges(MASTER_PATH, dataListener);
        takeMaster();

    }

    public void stop() throws Exception {
        if (!running) {
            throw new Exception("server has stoped");
        }
        running = false;

        delayExector.shutdown();

        zkClient.unsubscribeDataChanges(MASTER_PATH, dataListener);

        releaseMaster();

    }

    private void takeMaster() {
        if (!running)
            return;

        try {
            zkClient.create(MASTER_PATH, workServer, CreateMode.EPHEMERAL);
            masterServer = workServer;
            System.out.println(workServer.getName() + " is master");
            delayExector.schedule(new Runnable() {
                public void run() {
                    // TODO Auto-generated method stub
                    if (checkMaster()) {
                        releaseMaster();
                    }
                }
            }, 5, TimeUnit.SECONDS);

        } catch (ZkNodeExistsException e) {
            ServerMessage workServer = zkClient.readData(MASTER_PATH, true);
            if (workServer == null) {
                takeMaster();
            } else {
                masterServer = workServer;
            }
        } catch (Exception e) {
            // ignore;
        }

    }

    private void releaseMaster() {
        if (checkMaster()) {
            zkClient.delete(MASTER_PATH);

        }

    }

    private boolean checkMaster() {
        try {
            ServerMessage serverMessage = zkClient.readData(MASTER_PATH);
            masterServer = serverMessage;
            if (masterServer.getName().equals(workServer.getName())) {
                return true;
            }
            return false;
        } catch (ZkNoNodeException e) {
            return false;
        } catch (ZkInterruptedException e) {
            return checkMaster();
        } catch (ZkException e) {
            return false;
        }
    }

}
