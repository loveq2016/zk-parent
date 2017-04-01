package com.fsmeeting.curator.basic.ops;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Perms;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

import java.util.ArrayList;

/**
 * Description:创建需要认证的节点
 *
 * @Author:yicai.liu<虚竹子>
 * @Date 2017/3/30 17:24
 */
public class CreateNodeAuth {

    public static void main(String[] args) throws Exception {

        CuratorFramework client = SessionManager.createSession();

        ACL aclIp = new ACL(Perms.READ, new Id("ip", "192.168.1.105"));
        ACL aclDigest = new ACL(Perms.READ | Perms.WRITE, new Id("digest", DigestAuthenticationProvider.generateDigest("jike:123456")));
        ArrayList<ACL> acls = new ArrayList<ACL>();
        acls.add(aclDigest);
        acls.add(aclIp);

        String path = client.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT)
                .withACL(acls)
                .forPath("/live/servers", "123".getBytes());

        System.out.println(path);

        Thread.sleep(Integer.MAX_VALUE);


    }

}
