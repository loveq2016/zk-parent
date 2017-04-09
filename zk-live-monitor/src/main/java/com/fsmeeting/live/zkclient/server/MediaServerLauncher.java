package com.fsmeeting.live.zkclient.server;

import com.fsmeeting.live.bean.LiveService;
import com.fsmeeting.live.common.Constants;
import com.fsmeeting.live.enums.LiveServerType;
import com.fsmeeting.live.util.UUIDGenerator;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNoNodeException;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Description:mock media server
 *
 * @Author:yicai.liu<虚竹子>
 * @Date 2017/4/7 15:13
 */
public class MediaServerLauncher {

    private static final Logger logger = LoggerFactory.getLogger(MediaServerLauncher.class);

    /**
     * 服务器状态
     */
    private volatile boolean running = false;

    /**
     * 持有zk客户端
     */
    private ZkClient zkClient;

    /**
     * 媒体服务器数据
     */
    private LiveService liveService;

    /**
     * 媒体服务器根目录
     */
    private String rootPath;

    public MediaServerLauncher(ZkClient zkClient, LiveService liveService, String rootPath) {
        this.zkClient = zkClient;
        this.liveService = liveService;
        this.rootPath = rootPath;
    }

    public static void main(String[] args) {

        ZkClient client = new ZkClient(Constants.ZK.ZK_CONNECT_STRING, 5000, 5000, new SerializableSerializer());

        LiveService service = new LiveService();
        service.setAppId(UUIDGenerator.generator(LiveServerType.MEDIA));
        service.setAddress("192.168.7.178:4532;192.168.7.178:6765;192.168.7.178:8975;");
        service.setAppType(LiveServerType.MEDIA.getCode());
        service.setCurLoad(10);
        service.setLoad(1000);
        service.setDesc("proxy：" + service.getAppId());

        new MediaServerLauncher(client, service, Constants.ZK.Path.ROOT_MEDIA).start();

        logger.info("Press enter/return to quit\n");
        try {
            new BufferedReader(new InputStreamReader(System.in)).readLine();
        } catch (IOException e) {
            logger.error("rampage...", e);
        }

    }

    /**
     * Description: 启动服务
     *
     * @Author:yicai.liu<虚竹子>
     * @Date 2017/4/6 20:55
     */
    private void start() {

        if (running) {
            throw new RuntimeException("MediaServer has startup...");
        }
        running = true;
        registerMe();
    }

    /**
     * Description:注册自己
     *
     * @Author:yicai.liu<虚竹子>
     * @Date 2017/4/6 20:55
     */
    private void registerMe() {
        try {
            // List<ACL> acls = new ArrayList<ACL>();
           /* try {
                String digestMessage = DigestAuthenticationProvider.generateDigest("fsmeeting:123456");
                logger.info("digestMessage:" + digestMessage);
                // ACL aclDigest = new ACL(Perms.READ | Perms.WRITE | Perms.CREATE, new Id("digest", digestMessage));
                ACL aclDigest = new ACL(Perms.ALL, new Id("digest", digestMessage));
                acls.add(aclDigest);
            } catch (NoSuchAlgorithmException e) {
                logger.error("No Such Auth Algorithm", e);
            }*/
            zkClient.createEphemeral(rootPath + "/" + liveService.getAppId(), liveService);
        } catch (ZkNoNodeException e) {
            zkClient.createPersistent(rootPath, true);
        }
    }
}
