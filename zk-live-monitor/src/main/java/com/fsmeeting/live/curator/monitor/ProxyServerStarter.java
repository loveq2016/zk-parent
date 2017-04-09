package com.fsmeeting.live.curator.monitor;

import com.fsmeeting.live.bean.LiveService;
import com.fsmeeting.live.common.Constants;
import com.fsmeeting.live.curator.SessionManager;
import com.fsmeeting.live.curator.server.ProxyServer;
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
 * Description:代理服务启动程序
 *
 * @Author:yicai.liu<虚竹子>
 */
public class ProxyServerStarter {

    private static final Logger logger = LoggerFactory.getLogger(ProxyServerStarter.class);

    public static void main(String[] args) {

        CuratorFramework client = SessionManager.createSession();

        ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(10);// 后台心跳,定时调度,更新负载

        //模拟10台
        for (int i = 0; i < 10; i++) {
            LiveService service = new LiveService();
            service.setAppId(UUIDGenerator.generator(LiveServerType.PROXY));
            service.setAddress("192.168.7.178:" + (2000 + i));
            service.setAppType(LiveServerType.PROXY.getCode());
            service.setCurLoad(new Random().nextInt(1000));
            service.setLoad(1000);
            service.setDesc("proxy：" + service.getAppId());

            final ProxyServer proxyServer = new ProxyServer(client, service);
            proxyServer.start();

            scheduledExecutor.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    proxyServer.heartbeat();
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
