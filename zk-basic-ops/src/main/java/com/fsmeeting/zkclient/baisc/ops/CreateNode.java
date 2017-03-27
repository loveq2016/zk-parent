package com.fsmeeting.zkclient.baisc.ops;

import com.fsmeeting.bean.LiveServices;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.apache.zookeeper.CreateMode;

public class CreateNode {

    public static void main(String[] args) {
        ZkClient zc = new ZkClient("192.168.7.178:2181", 10000, 10000, new SerializableSerializer());
        System.out.println("conneted ok!");


        LiveServices proxy = new LiveServices();
        proxy.setId(1);
        proxy.setName("proxy_01");
        proxy.setAddress("192.168.7.178:8080");
        String path = zc.create("/live/servers/proxy", proxy, CreateMode.PERSISTENT);
        System.out.println("created path:" + path);

    }

}
