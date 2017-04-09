package com.fsmeeting.live.curator.event;

import org.apache.curator.framework.CuratorFramework;

/**
 * Description: 事件处理器
 *
 * @Author:yicai.liu<虚竹子>
 */
public interface IEventProcessor<T> {

    /**
     * 处理事件
     *
     * @param curatorFramework curator zookeeper client
     * @param event            curator 事件
     */
    void process(CuratorFramework curatorFramework, T event);

}
