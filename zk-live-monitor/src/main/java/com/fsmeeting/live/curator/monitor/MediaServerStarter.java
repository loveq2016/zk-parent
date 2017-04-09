package com.fsmeeting.live.curator.monitor;

import com.fsmeeting.live.bean.LiveService;
import com.fsmeeting.live.common.Constants;
import com.fsmeeting.live.curator.SessionManager;
import com.fsmeeting.live.curator.server.MediaServer;
import com.fsmeeting.live.enums.LiveServerType;
import com.fsmeeting.live.util.UUIDGenerator;
import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Description:媒体服务启动程序
 *
 * @Author:yicai.liu<虚竹子>
 */
public class MediaServerStarter {

    private static final Logger logger = LoggerFactory.getLogger(MediaServerStarter.class);

    public static void main(String[] args) {

        CuratorFramework client = SessionManager.createSession();

        ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(10);// 后台心跳,定时调度,更新负载

        //模拟10台
        for (int i = 0; i < 10; i++) {
            LiveService service = new LiveService();
            service.setAppId(UUIDGenerator.generator(LiveServerType.MEDIA));
            service.setAddress("192.168.7.178:" + (1000 + i));
            service.setAppType(LiveServerType.MEDIA.getCode());
            service.setCurLoad(new Random().nextInt(1000));
            service.setLoad(1000);
            service.setDesc("media：" + service.getAppId());

            final MediaServer mediaServer = new MediaServer(client, service);
            mediaServer.start();

            scheduledExecutor.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    mediaServer.heartbeat();
                }
            }, Constants.Heartbeat.COMMON_RATE_MS, Constants.Heartbeat.COMMON_RATE_MS, TimeUnit.MILLISECONDS);
        }


        logger.info("Press enter/return to quit\n");
        try {
            new BufferedReader(new InputStreamReader(System.in)).readLine();
        } catch (IOException e) {
            logger.error("rampage.....", e);
        }

    }
}
