package com.fsmeeting.live.curator;

import com.fsmeeting.live.common.Constants;
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
     * Description:创建会话
     *
     * @return curator zookeeper客戶端连接
     * @Author:yicai.liu<虚竹子>
     */
    public static CuratorFramework createSession() {

        //会话超时重试策略
        //RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        //RetryPolicy retryPolicy = new RetryNTimes(5, 1000);
        RetryPolicy retryPolicy = new RetryUntilElapsed(5000, 1000);
        //CuratorFramework client = CuratorFrameworkFactory.newClient(Constants.ZK.ZK_CONNECT_STRING,Constants.ZK.SESSION_TIMEOUT_MS,Constants.ZK.CONNECT_TIMEOUT_MS, retryPolicy);

        CuratorFramework client = CuratorFrameworkFactory
                .builder()
                .connectString(Constants.ZK.ZK_CONNECT_STRING)
                .sessionTimeoutMs(Constants.ZK.SESSION_TIMEOUT_MS)
                .connectionTimeoutMs(Constants.ZK.CONNECT_TIMEOUT_MS)
                .retryPolicy(retryPolicy)
                .build();

        client.start();

        return client;
    }

}
