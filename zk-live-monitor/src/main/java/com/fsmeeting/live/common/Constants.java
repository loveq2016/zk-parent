package com.fsmeeting.live.common;

/**
 * Description:常量
 *
 * @Author:yicai.liu<虚竹子>
 * @Date 2017/4/5 19:07
 */
public final class Constants {

    /**
     * Zookeeper
     */
    public static final class ZK {

        /**
         * zookeeper 会话连接地址
         */
        public static final String ZK_CONNECT_STRING = "192.168.7.178:2181";

        /**
         * 节点路径
         */
        public static final class Path {

            /**
             * 代理根目录
             */
            public static String ROOT_PROXY = "/fsmeeting/live/server/proxy";


            /**
             * 直播根目录
             */
            public static String ROOT_LIVE = "/fsmeeting/live/server/live";

            /**
             * 图片根目录
             */
            public static String ROOT_PIC = "/fsmeeting/live/server/pic";

            /**
             * 媒体根目录
             */
            public static String ROOT_MEDIA = "/fsmeeting/live/server/media";

        }
    }


}
