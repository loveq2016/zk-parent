package com.fsmeeting.curator.basic.ops;

import org.apache.curator.framework.CuratorFramework;

/**
 * Description: 删除节点
 *
 * @Author:yicai.liu<虚竹子>
 * @Date 2017/3/30 17:24
 */
public class DelNode {

    public static void main(String[] args) throws Exception {

        CuratorFramework client = SessionManager.createSession();

        client.delete().guaranteed().deletingChildrenIfNeeded().withVersion(-1).forPath("/jike20");


        Thread.sleep(Integer.MAX_VALUE);


    }

}
