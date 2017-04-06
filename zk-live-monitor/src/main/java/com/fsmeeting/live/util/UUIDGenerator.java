package com.fsmeeting.live.util;

import com.fsmeeting.live.enums.LiveServerType;

import java.util.UUID;

/**
 * Discription:
 * Created by yicai.liu<虚竹子> on 2017/4/6.
 */
public class UUIDGenerator {

    /**
     * Description: 根据服务类型生成UUID
     *
     * @Author:yicai.liu<虚竹子>
     * @Date 2017/4/6 10:07
     */
    public static String generator(LiveServerType type) {
        return new StringBuilder(type.getName()).append("-{").append(UUID.randomUUID().toString()).append("}").toString();
    }

    public static void main(String[] args) {
        System.out.println(generator(LiveServerType.MEDIA));
    }
}
