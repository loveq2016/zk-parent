package com.fsmeeting.live.util;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.data.Stat;

/**
 * Created by think on 2017/4/9.
 */
public class PathOperation {

    private CuratorFramework curatorClient;

    public PathOperation(CuratorFramework curatorClient) {
        this.curatorClient = curatorClient;
    }

    public void createIfNotExist(String path) throws Exception {
        Stat stat = curatorClient.checkExists().forPath(path);
        if (null == stat) {
            curatorClient.create().creatingParentsIfNeeded().forPath(path);
        }
    }
}
