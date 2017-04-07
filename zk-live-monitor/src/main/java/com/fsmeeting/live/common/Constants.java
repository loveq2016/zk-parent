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


}
