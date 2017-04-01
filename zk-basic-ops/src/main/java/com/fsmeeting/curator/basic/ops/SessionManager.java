package com.fsmeeting.curator.basic.ops;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryUntilElapsed;

/**
 * Description:会话管理器
 *
 * @Author:yicai.liu<虚竹子>
 * @Date 2017/3/30 15:47
 */
public class SessionManager {

    /**
     * 创建会话
     *
     * @return
     */
    public static CuratorFramework createSession() {

        //RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        //RetryPolicy retryPolicy = new RetryNTimes(5, 1000);
        RetryPolicy retryPolicy = new RetryUntilElapsed(5000, 1000);
        //CuratorFramework client = CuratorFrameworkFactory.newClient(SessionConstants.CONNECT_STRING,SessionConstants.SESSION_TIMEOUT_MS,SessionConstants.CONNECT_TIMEOUT_MS, retryPolicy);

        CuratorFramework client = CuratorFrameworkFactory
                .builder()
                .connectString(SessionConstants.CONNECT_STRING)
                .sessionTimeoutMs(SessionConstants.SESSION_TIMEOUT_MS)
                .connectionTimeoutMs(SessionConstants.CONNECT_TIMEOUT_MS)
                .retryPolicy(retryPolicy)
                .build();

        client.start();

        return client;
    }


    class SessionConstants {
        public static final String CONNECT_STRING = "192.168.7.178:2181";
        public static final int SESSION_TIMEOUT_MS = 5000;
        public static final int CONNECT_TIMEOUT_MS = 5000;
    }
}
