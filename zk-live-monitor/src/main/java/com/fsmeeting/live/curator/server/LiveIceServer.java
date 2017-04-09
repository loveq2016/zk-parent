package com.fsmeeting.live.curator.server;

import com.alibaba.fastjson.JSON;
import com.fsmeeting.live.bean.LiveService;
import com.fsmeeting.live.common.Constants;
import com.fsmeeting.live.curator.event.PathChildrenServiceLoader;
import com.fsmeeting.live.curator.ha.balance.MinRateLoadStrategy;
import com.fsmeeting.live.enums.LiveServerType;
import com.fsmeeting.live.util.PathOperation;
import com.google.common.base.Strings;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Description : mock live ice server
 *
 * @Author:yicai.liu<虚竹子>
 */
public class LiveIceServer {

    private static final Logger logger = LoggerFactory.getLogger(LiveIceServer.class);

    /**
     * 所有服务缓存
     */
    private Map<String, LiveService> serviceCaches = new ConcurrentHashMap<String, LiveService>();

    /**
     * 代理服务缓存
     */
    private Map<String, LiveService> proxyServiceCaches = new ConcurrentHashMap<String, LiveService>();

    /**
     * 媒体服务缓存
     */
    private Map<String, LiveService> mediaServiceCaches = new ConcurrentHashMap<String, LiveService>();

    /**
     * 图片服务缓存
     */
    private Map<String, LiveService> pictureServiceCaches = new ConcurrentHashMap<String, LiveService>();

    /**
     * 直播服务缓存
     */
    private Map<String, LiveService> liveServiceCaches = new ConcurrentHashMap<String, LiveService>();

    /**
     * 服务器状态
     */
    private volatile boolean running = false;

    /**
     * 持有zk客户端
     */
    private CuratorFramework curatorClient;


    /**
     * Description: 启动服务
     *
     * @Author:yicai.liu<虚竹子>
     */
    public void start() throws Exception {
        if (running) {
            throw new RuntimeException("LiveIceServer has startup...");
        }
        running = true;

        loadServiceData2Cache(); //读取服务数据——>缓存

        listeningService();  //监听服务

        ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutor.scheduleAtFixedRate(new Runnable() { // 查询负载
            @Override
            public void run() {
                queryLoadService();
            }
        }, Constants.Heartbeat.COMMON_RATE_MS, Constants.Heartbeat.COMMON_RATE_MS, TimeUnit.MILLISECONDS);
        queryLoadService(); //负载查询
        logger.info("Press enter/return to quit\n");
        try {
            new BufferedReader(new InputStreamReader(System.in)).readLine();
        } catch (IOException e) {
            logger.error("rampage.....", e);
        }

    }

    /**
     * 获取负载服务
     */
    private void queryLoadService() {
        LiveService proxyLoadSerive = new MinRateLoadStrategy(proxyServiceCaches).getService(LiveServerType.PROXY);
        LiveService liveLoadSerive = new MinRateLoadStrategy(liveServiceCaches).getService(LiveServerType.LIVE);
        LiveService picLoadSerive = new MinRateLoadStrategy(pictureServiceCaches).getService(LiveServerType.PIC);
        LiveService mediaLoadSerive = new MinRateLoadStrategy(mediaServiceCaches).getService(LiveServerType.MEDIA);
        logger.info("proxyLoadSerive:" + JSON.toJSONString(proxyLoadSerive));
        logger.info("liveLoadSerive:" + JSON.toJSONString(liveLoadSerive));
        logger.info("picLoadSerive:" + JSON.toJSONString(picLoadSerive));
        logger.info("mediaLoadSerive:" + JSON.toJSONString(mediaLoadSerive));

    }

    /**
     * Description:监听服务(增删改)
     *
     * @Author:yicai.liu<虚竹子>
     */
    private void listeningService() {

        ExecutorService pool = Executors.newFixedThreadPool(2);

        final PathChildrenCache proxyCache = new PathChildrenCache(curatorClient, Constants.ZK.Path.ROOT_PROXY, true);
        proxyCache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                new PathChildrenServiceLoader(proxyServiceCaches).process(client, event);
                logger.info("current proxy cache:" + JSON.toJSONString(proxyServiceCaches));
            }
        }, pool);

        //监听媒体服务
        final PathChildrenCache mediaCache = new PathChildrenCache(curatorClient, Constants.ZK.Path.ROOT_MEDIA, true);
        mediaCache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                new PathChildrenServiceLoader(mediaServiceCaches).process(client, event);
                logger.info("current media cache:" + JSON.toJSONString(mediaServiceCaches));
            }
        }, pool);

      /*  final TreeCache treeCache = new TreeCache(curatorClient, Constants.ZK.Path.rootPath);
        treeCache.getListenable().addListener(
                new TreeCacheListener() {
                    @Override
                    public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
                        new ChangedServiceLoader(serviceCaches).process(client, event);
                        logger.info("current cache:" + JSON.toJSONString(serviceCaches));
                    }
                }, pool);*/

        try {
            proxyCache.start();
            mediaCache.start();
            //treeCache.start();
        } catch (Exception e) {
            logger.error("rampage....", e);
        }
    }

    /**
     * Description: 加载ZK节点服务数据
     *
     * @Author:yicai.liu<虚竹子>
     */
    private void loadServiceData2Cache() throws Exception {
        PathOperation pathOperation = new PathOperation(curatorClient);
        pathOperation.createIfNotExist(Constants.ZK.Path.ROOT_PROXY);
        pathOperation.createIfNotExist(Constants.ZK.Path.ROOT_MEDIA);
        pathOperation.createIfNotExist(Constants.ZK.Path.ROOT_PIC);
        pathOperation.createIfNotExist(Constants.ZK.Path.ROOT_LIVE);

        //获取代理服务器数据
        List<String> proxyChildren = curatorClient.getChildren().forPath(Constants.ZK.Path.ROOT_PROXY);
        for (String child : proxyChildren) {
            byte[] bs = curatorClient.getData().forPath(Constants.ZK.Path.ROOT_PROXY.concat("/").concat(child));
            LiveService service = (LiveService) new SerializableSerializer().deserialize(bs);
            if (null == service || Strings.isNullOrEmpty(service.getAppId())) {
                logger.error("proxy service convert error...");
                continue;
            }
            proxyServiceCaches.put(service.getAppId(), service);
        }

        //获取媒体服务器数据
        List<String> mediaChildren = curatorClient.getChildren().forPath(Constants.ZK.Path.ROOT_MEDIA);
        for (String child : mediaChildren) {
            byte[] bs = curatorClient.getData().forPath(Constants.ZK.Path.ROOT_MEDIA.concat("/").concat(child));
            LiveService service = (LiveService) new SerializableSerializer().deserialize(bs);
            if (null == service || Strings.isNullOrEmpty(service.getAppId())) {
                logger.error("media service convert error...");
                continue;
            }
            mediaServiceCaches.put(service.getAppId(), service);
        }

        //获取图片服务器数据
        List<String> pictureChildren = curatorClient.getChildren().forPath(Constants.ZK.Path.ROOT_PIC);
        for (String child : pictureChildren) {
            byte[] bs = curatorClient.getData().forPath(Constants.ZK.Path.ROOT_PIC.concat("/").concat(child));
            LiveService service = (LiveService) new SerializableSerializer().deserialize(bs);
            if (null == service || Strings.isNullOrEmpty(service.getAppId())) {
                logger.error("picture service convert error...");
                continue;
            }
            pictureServiceCaches.put(service.getAppId(), service);
        }

        //获取直播服务器数据
        List<String> liveChildren = curatorClient.getChildren().forPath(Constants.ZK.Path.ROOT_LIVE);
        for (String child : liveChildren) {
            byte[] bs = curatorClient.getData().forPath(Constants.ZK.Path.ROOT_LIVE.concat("/").concat(child));
            LiveService service = (LiveService) new SerializableSerializer().deserialize(bs);
            if (null == service || Strings.isNullOrEmpty(service.getAppId())) {
                logger.error("live service convert error...");
                continue;
            }
            liveServiceCaches.put(service.getAppId(), service);
        }

        logger.info("load proxy data to cache:" + JSON.toJSONString(proxyServiceCaches));
        logger.info("load media data to cache:" + JSON.toJSONString(mediaServiceCaches));
        logger.info("load pic data to cache:" + JSON.toJSONString(pictureServiceCaches));
        logger.info("load live data to cache:" + JSON.toJSONString(liveServiceCaches));
    }

    /**
     * Description: 构造函数
     *
     * @param curatorClient zk客户端
     * @Author:yicai.liu<虚竹子>
     */
    public LiveIceServer(CuratorFramework curatorClient) {
        this.curatorClient = curatorClient;
    }


}
