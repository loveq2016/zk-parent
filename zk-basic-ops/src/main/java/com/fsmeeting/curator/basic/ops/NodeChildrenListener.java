package com.fsmeeting.curator.basic.ops;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;

/**
 * Description:子节点监听
 *
 * @Author:yicai.liu<虚竹子>
 * @Date 2017/3/30 17:22
 */
public class NodeChildrenListener {

    public static void main(String[] args) throws Exception {
        CuratorFramework client = SessionManager.createSession();

        final PathChildrenCache cache = new PathChildrenCache(client, "/live", true);
        cache.start();
        cache.getListenable().addListener(new PathChildrenCacheListener() {

            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event)
                    throws Exception {
                switch (event.getType()) {
                    case CHILD_ADDED:
                        System.out.println("CHILD_ADDED:" + event.getData());
                        break;
                    case CHILD_UPDATED:
                        System.out.println("CHILD_UPDATED:" + event.getData());
                        break;
                    case CHILD_REMOVED:
                        System.out.println("CHILD_REMOVED:" + event.getData());
                        break;
                    default:
                        break;
                }
            }
        });

        Thread.sleep(Integer.MAX_VALUE);

    }

}
