package com.fsmeeting.curator.basic.ops;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.data.Stat;

/**
 * Description:获取节点数据，需要认证
 *
 * @Author:yicai.liu<虚竹子>
 * @Date 2017/3/30 17:23
 */
public class GetDataAuth {

    public static void main(String[] args) throws Exception {

        CuratorFramework client = SessionManager.createSession();

        Stat stat = new Stat();

        byte[] ret = client.getData().storingStatIn(stat).forPath("/live/servers");

        System.out.println(new String(ret));

        System.out.println(stat);


    }

}
