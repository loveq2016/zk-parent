package com.fsmeeting.live.curator.server;

import com.fsmeeting.live.bean.LiveService;
import com.fsmeeting.live.common.Constants;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * Description:mock media server register itself
 *
 * @Author:yicai.liu<虚竹子>
 * @Date 2017/4/7 15:13
 */
public class MediaServer {

    private static final Logger logger = LoggerFactory.getLogger(MediaServer.class);

    /**
     * 服务器状态
     */
    private volatile boolean running = false;

    /**
     * 持有zk客户端
     */
    private CuratorFramework curatorClient;

    /**
     * 媒体服务器数据
     */
    private LiveService liveService;


    /**
     * 种子生成器
     */
    private Random random = new Random();

    public MediaServer(CuratorFramework curatorClient, LiveService liveService) {
        this.curatorClient = curatorClient;
        this.liveService = liveService;
    }

    /**
     * Description: 启动服务
     *
     * @Author:yicai.liu<虚竹子>
     * @Date 2017/4/6 20:55
     */
    public void start() {

        if (running) {
            throw new RuntimeException("MediaServer has startup...");
        }
        running = true;
        registerMe();
    }

    /**
     * Description:注册自己
     *
     * @Author:yicai.liu<虚竹子>
     * @Date 2017/4/6 20:55
     */
    private void registerMe() {
        try {
            String path = Constants.ZK.Path.ROOT_MEDIA.concat("/").concat(liveService.getAppId());
            byte[] data = new SerializableSerializer().serialize(liveService);
            curatorClient.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path, data);
        } catch (Exception e) {
            logger.error("rampage.....", e);
        }
    }

    /**
     * Description: 心跳 ，有一定机率变更数据
     *
     * @Author:yicai.liu<虚竹子>
     */
    public void heartbeat() {
        int seed = random.nextInt(2);
        if (0 == seed) {//hit,命中
            logger.info("update hit , binggo...");
            doUpdate();
        } else {//丢失
            logger.info("miss update chance...");
        }

    }

    /**
     * Description:更新
     *
     * @Author:yicai.liu<虚竹子>
     */
    private void doUpdate() {
        liveService.setCurLoad(random.nextInt(liveService.getLoad() + 1));
        String path = Constants.ZK.Path.ROOT_MEDIA.concat("/").concat(liveService.getAppId());
        byte[] data = new SerializableSerializer().serialize(liveService);
        try {
            curatorClient.setData().forPath(path, data);
        } catch (Exception e) {
            logger.error("rampage.....", e);
        }
    }
}
