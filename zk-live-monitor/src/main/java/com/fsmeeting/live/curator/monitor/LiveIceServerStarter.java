package com.fsmeeting.live.curator.monitor;

import com.fsmeeting.live.curator.SessionManager;
import com.fsmeeting.live.curator.server.LiveIceServer;

/**
 * Description: 直播ICE服务启动程序
 *
 * @Author:yicai.liu<虚竹子>
 */
public class LiveIceServerStarter {

    /**
     * Description: 程序入口
     *
     * @Author:yicai.liu<虚竹子>
     */
    public static void main(String[] args) throws Exception {
        LiveIceServer launcher = new LiveIceServer(SessionManager.createSession());
        launcher.start();
    }

}
