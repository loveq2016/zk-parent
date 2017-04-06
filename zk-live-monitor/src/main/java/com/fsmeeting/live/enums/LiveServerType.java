package com.fsmeeting.live.enums;

/**
 * Description:服务器类型
 *
 * @Author:yicai.liu<虚竹子>
 * @Date 2017/4/5 17:43
 */
public enum LiveServerType {

    /**
     * 未知
     */
    UNKNOWN(0, "UNKNOWN"),

    /**
     * 直播服务器
     */
    LIVE(1, "Live"),

    /**
     * 媒体服务器
     */
    MEDIA(2, "Media"),

    /**
     * 代理服务器
     */
    PROXY(3, "Proxy"),

    /**
     * 图片服务器
     */
    PIC(4, "Picture");

    private int code;

    private String name;

    private LiveServerType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

}
