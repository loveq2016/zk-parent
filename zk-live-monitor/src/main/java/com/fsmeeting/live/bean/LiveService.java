package com.fsmeeting.live.bean;

import com.fsmeeting.live.enums.LiveServerType;

import java.io.Serializable;
import java.util.Date;

/**
 * Description:直播服务
 *
 * @Author:yicai.liu<虚竹子>
 * @Date 2017/4/5 17:43
 */
public class LiveService implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 服务器唯一标识
     */
    private String appId;

    /**
     * 服务器类型
     *
     * @see LiveServerType
     */
    private int appType;

    /**
     * 服务当前负载
     */
    private int curLoad;

    /**
     * 负载权重
     */
    private int load;

    /**
     * 最后更新时间
     */
    private Date lastActiveTime = new Date();

    /**
     * 服务协议、IP、端口
     */
    private String address;

    /**
     * 服务名称、描述
     */
    private String desc;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public int getAppType() {
        return appType;
    }

    public void setAppType(int appType) {
        this.appType = appType;
    }

    public int getCurLoad() {
        return curLoad;
    }

    public void setCurLoad(int curLoad) {
        this.curLoad = curLoad;
    }

    public int getLoad() {
        return load;
    }

    public void setLoad(int load) {
        this.load = load;
    }

    public Date getLastActiveTime() {
        return lastActiveTime;
    }

    public void setLastActiveTime(Date lastActiveTime) {
        this.lastActiveTime = lastActiveTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


}
