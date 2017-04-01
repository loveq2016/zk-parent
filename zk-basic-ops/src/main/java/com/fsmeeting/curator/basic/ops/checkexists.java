package com.fsmeeting.curator.basic.ops;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Description: 检查节点是否存在
 *
 * @Author:yicai.liu<虚竹子>
 * @Date 2017/3/30 17:01
 */
public class CheckExists {

    public static void main(String[] args) throws Exception {

        ExecutorService es = Executors.newFixedThreadPool(5);

        CuratorFramework client = SessionManager.createSession();

        client.checkExists().inBackground(new BackgroundCallback() {

            public void processResult(CuratorFramework arg0, CuratorEvent arg1)
                    throws Exception {

                Stat stat = arg1.getStat();
                System.out.println(stat);
                System.out.println(arg1.getContext());


            }
        }, "123", es).forPath("/live");

        Thread.sleep(Integer.MAX_VALUE);


    }

}
