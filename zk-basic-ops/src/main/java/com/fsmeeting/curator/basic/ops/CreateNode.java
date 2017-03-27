package com.fsmeeting.curator.basic.ops;

import com.fsmeeting.bean.LiveServices;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryUntilElapsed;
import org.apache.zookeeper.CreateMode;

public class CreateNode {

    public static void main(String[] args) throws Exception {

        //RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        //RetryPolicy retryPolicy = new RetryNTimes(5, 1000);
        RetryPolicy retryPolicy = new RetryUntilElapsed(5000, 1000);
//		CuratorFramework client = CuratorFrameworkFactory
//				.newClient("192.168.7.178:2181",5000,5000, retryPolicy);

        CuratorFramework client = CuratorFrameworkFactory
                .builder()
                .connectString("192.168.7.178:2181")
                .sessionTimeoutMs(5000)
                .connectionTimeoutMs(5000)
                .retryPolicy(retryPolicy)
                .build();

        client.start();


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
