package com.fsmeeting.curator.basic.ops;

import com.fsmeeting.bean.LiveServices;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;

/**
 * Description: 节点创建
 *
 * @Author:yicai.liu<虚竹子>
 * @Date 2017/3/30 15:16
 */
public class CreateNode {

    public static void main(String[] args) throws Exception {
        CuratorFramework client = SessionManager.createSession();

        LiveServices proxy = new LiveServices();
        proxy.setId(1);
        proxy.setName("proxy_01");
        proxy.setAddress("192.168.7.178:8080");

        String path = client.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .forPath("/live/servers/proxy", new SerializableSerializer().serialize(proxy));

        System.out.println(path);

        Thread.sleep(Integer.MAX_VALUE);


    }

}
