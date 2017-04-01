package com.fsmeeting.curator.basic.ops;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;

/**
 * Description:节点监听
 *
 * @Author:yicai.liu<虚竹子>
 * @Date 2017/3/30 17:21
 */
public class NodeListener {

    public static void main(String[] args) throws Exception {
        CuratorFramework client = SessionManager.createSession();

        final NodeCache cache = new NodeCache(client, "/");
        cache.start();
        cache.getListenable().addListener(new NodeCacheListener() {

            public void nodeChanged() throws Exception {
                // TODO Auto-generated method stub
                byte[] ret = cache.getCurrentData().getData();
                System.out.println("new data:" + new String(ret));
            }
        });

        Thread.sleep(Integer.MAX_VALUE);

    }

}
