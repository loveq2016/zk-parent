package com.fsmeeting.live.curator.event;

import com.alibaba.fastjson.JSON;
import com.fsmeeting.live.bean.LiveService;
import com.google.common.base.Strings;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Description:变更服务信息重新加载
 *
 * @Author:yicai.liu<虚竹子>
 */
public class PathChildrenServiceLoader implements IEventProcessor<PathChildrenCacheEvent> {

    private static final Logger logger = LoggerFactory.getLogger(PathChildrenServiceLoader.class);

    /**
     * 服务缓存
     */
    private Map<String, LiveService> serviceCaches;

    public PathChildrenServiceLoader(Map<String, LiveService> serviceCaches) {
        this.serviceCaches = serviceCaches;
    }

    @Override
    public void process(CuratorFramework curatorFramework, PathChildrenCacheEvent event) {
        ChildData data = event.getData();
        if (null != data && null != data.getData() && data.getData().length > 0) {
            switch (event.getType()) {
                case CHILD_ADDED:
                    onChildAdded(data);
                    break;
                case CHILD_REMOVED:
                    onChildRemoved(data);
                    break;
                case CHILD_UPDATED:
                    onChildUpdated(data);
                    break;
                default:
                    break;
            }
        } else {
            logger.info("For blank data type : " + event.getType());
            if (null != data) {
                logger.info("For blank data path : " + data.getPath());
            }
        }
    }

    /**
     * 节点新增
     *
     * @param data 节点数据
     */
    private void onChildAdded(ChildData data) {
        String path = data.getPath();
        LiveService service = (LiveService) new SerializableSerializer().deserialize(data.getData());
        if (null == service || Strings.isNullOrEmpty(service.getAppId())) { // mark error log and abort
            logger.error("CHILD_ADDED error: " + path);
            return;
        }
        serviceCaches.put(service.getAppId(), service);
        logger.info("CHILD_ADDED path: " + path);
        logger.info("CHILD_ADDED data: " + JSON.toJSONString(service));
    }

    /**
     * 节点删除
     *
     * @param data 节点数据
     */
    private void onChildRemoved(ChildData data) {
        String path = data.getPath();
        LiveService service = (LiveService) new SerializableSerializer().deserialize(data.getData());
        if (null == service || Strings.isNullOrEmpty(service.getAppId())) { // mark error log and abort
            logger.error("CHILD_REMOVED error: " + path);
            return;
        }
        serviceCaches.remove(service.getAppId());
        logger.info("CHILD_REMOVED path: " + path);
        logger.info("CHILD_REMOVED data: " + JSON.toJSONString(service));
    }

    /**
     * 节点更新
     *
     * @param data 节点数据
     */
    private void onChildUpdated(ChildData data) {
        String path = data.getPath();
        LiveService service = (LiveService) new SerializableSerializer().deserialize(data.getData());

        if (null == service || Strings.isNullOrEmpty(service.getAppId())) { // mark error log and abort
            logger.error("CHILD_UPDATED error: " + path);
            return;
        }
        serviceCaches.put(service.getAppId(), service);

        logger.info("CHILD_UPDATED path: " + path);
        logger.info("CHILD_UPDATED data: " + JSON.toJSONString(service));
    }
}
