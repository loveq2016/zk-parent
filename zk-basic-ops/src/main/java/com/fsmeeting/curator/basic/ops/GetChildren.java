package com.fsmeeting.curator.basic.ops;

import org.apache.curator.framework.CuratorFramework;

import java.util.List;

/**
 * Description: 获取子节点数据
 *
 * @Author:yicai.liu<虚竹子>
 * @Date 2017/3/30 17:23
 */
public class GetChildren {

    public static void main(String[] args) throws Exception {

        CuratorFramework client = SessionManager.createSession();

        List<String> cList = client.getChildren().forPath("/live/servers");

        System.out.println(cList.toString());


    }

}
