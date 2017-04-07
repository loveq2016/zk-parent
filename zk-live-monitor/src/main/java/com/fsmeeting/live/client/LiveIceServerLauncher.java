package com.fsmeeting.live.client;

import com.alibaba.fastjson.JSON;
import com.fsmeeting.live.bean.LiveService;
import com.fsmeeting.live.common.Constants;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description : mock live ice server
 *
 * @Author:yicai.liu<虚竹子>
 * @Date 2017/4/7 11:27
 */
public class LiveIceServerLauncher {

    private static final Logger logger = LoggerFactory.getLogger(LiveIceServerLauncher.class);

    /**
     * 服务缓存
     */
    private Map<String, LiveService> serviceCaches = new ConcurrentHashMap<String, LiveService>();

    /**
     * 服务器状态
     */
    private volatile boolean running = false;

    /**
     * 持有zk客户端
     */
    private ZkClient zkClient;

    /**
     * 媒体服务器根目录
     */
    private String rootPath;


    /**
     * Description: 启动服务
     *
     * @Author:yicai.liu<虚竹子>
     * @Date 2017/4/6 20:55
     */
    private void start() {

        if (running) {
            throw new RuntimeException("LiveIceServer has startup...");
        }
        running = true;

        //获取子节点
        List<String> children = zkClient.getChildren(rootPath);

        for (String child : children) {
            LiveService service = zkClient.readData(rootPath + "/" + child);
            serviceCaches.put(service.getAppId(), service);
        }

        logger.info("init cache:" + JSON.toJSONString(serviceCaches));

        // 观察子节点的变化
        zkClient.subscribeChildChanges(rootPath, new IZkChildListener() {

            public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                logger.info("parent Path:" + parentPath);

                //交集
                serviceCaches.entrySet().retainAll(currentChilds);

                //差集
                currentChilds.removeAll(serviceCaches.entrySet());

                for (String child : currentChilds) {
                    LiveService service = zkClient.readData(parentPath + "/" + child);
                    serviceCaches.put(service.getAppId(), service);
                }

                logger.info("child changes :" + JSON.toJSONString(serviceCaches));
            }

        });

        logger.info("Press enter/return to quit\n");

        try {
            new BufferedReader(new InputStreamReader(System.in)).readLine();
        } catch (IOException e) {
            logger.error("rubbish", e);
        }

    }

    public static void main(String[] args) {
        ZkClient client = new ZkClient(Constants.ZK.ZK_CONNECT_STRING, 5000, 5000, new SerializableSerializer());
        new LiveIceServerLauncher(client, Constants.ZK.Path.ROOT_MEDIA).start();
    }


    public LiveIceServerLauncher(ZkClient zkClient, String rootPath) {
        this.zkClient = zkClient;
        this.rootPath = rootPath;
    }

    public ZkClient getZkClient() {
        return zkClient;
    }

    public void setZkClient(ZkClient zkClient) {
        this.zkClient = zkClient;
    }

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }
}
