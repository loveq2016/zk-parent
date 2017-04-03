package com.fsmeeting.bean;

/**
 * Description: 服务器类型
 *
 * @Author:yicai.liu<虚竹子>
 */
public enum ServerType {

    /**
     * 代理
     */
    PROXY(1),

    /**
     * 直播
     */
    LIVE(2),

    /**
     * 小文件
     */
    TFC(3),

    /**
     * 媒体
     */
    MEDIA(4);

    private int code;

    private ServerType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
