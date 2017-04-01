package com.fsmeeting.curator.basic.ops;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.data.Stat;

/**
 * Description: 更新数据
 *
 * @Author:yicai.liu<虚竹子>
 * @Date 2017/3/30 17:20
 */
public class UpdateData {

    public static void main(String[] args) throws Exception {

        CuratorFramework client = SessionManager.createSession();

        Stat stat = new Stat();
        client.getData().storingStatIn(stat).forPath("/live");

        client.setData().withVersion(stat.getVersion()).forPath("/live", "123".getBytes());

    }

}
