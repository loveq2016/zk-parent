package com.fsmeeting.live.common;

/**
 * Description:常量
 *
 * @Author:yicai.liu<虚竹子>
 * @Date 2017/4/5 19:07
 */
public final class Constants {

    /**
     * Zookeeper 常量集
     */
    public static final class ZK {

        /**
         * 会话连接地址
         */
        public static final String ZK_CONNECT_STRING = "192.168.1.11:2181";

        /**
         * 会话超时时间
         */
        public static final int SESSION_TIMEOUT_MS = 5000;

        /**
         * 连接超时时间
         */
        public static final int CONNECT_TIMEOUT_MS = 5000;

        /**
         * 节点路径
         */
        public static final class Path {

            /**
             * 服务器根目录
             */
            public static final String ROOT_SERVER = "/fsmeeting/live/server";

            /**
             * 代理根目录
             */
            public static final String ROOT_PROXY = ROOT_SERVER.concat("/proxy");


            /**
             * 直播根目录
             */
            public static final String ROOT_LIVE = ROOT_SERVER.concat("/live");

            /**
             * 图片根目录
             */
            public static final String ROOT_PIC = ROOT_SERVER.concat("/picture");

            /**
             * 媒体根目录
             */
            public static String ROOT_MEDIA = ROOT_SERVER.concat("/media");

        }

    }

    /**
     * 心跳常量集
     */
    public static final class Heartbeat {

        /**
         * 心跳频率
         */
        public static final long COMMON_RATE_MS = 5000;

    }
}
