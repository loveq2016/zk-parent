package com.fsmeeting.live.curator.ha.balance;


import com.fsmeeting.live.bean.LiveService;
import com.fsmeeting.live.enums.LiveServerType;

/**
 * Description:负载策略
 *
 * @Author:yicai.liu<虚竹子>
 */
public abstract class LoadStrategy {

    /**
     * 获取负载服务
     *
     * @param oldAddress 需要过滤的服务器地址
     * @param appType    服务器类型
     * @param params     预留
     * @return 负载服务器
     */
    public abstract LiveService getService(LiveServerType appType, String oldAddress, Object... params);

    public LiveService getService(LiveServerType appType, Object... params) {
        return getService(appType, null, params);
    }
}