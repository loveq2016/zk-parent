/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.fsmeeting.curator.mastersel;

import com.fsmeeting.bean.ServerMessage;
import com.fsmeeting.bean.ServerType;
import com.fsmeeting.curator.basic.ops.SessionManager;
import com.google.common.collect.Lists;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.utils.CloseableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class LeaderSelectorCurator {

    private static final Logger logger = LoggerFactory.getLogger(LeaderSelectorCurator.class);
    /**
     * 模拟N个客户端连接
     */
    private static final int CLIENT_QTY = 10;

    /**
     * zookeeper节点路径
     */
    private static final String PATH = "/leader";

    /**
     * 客户端列表
     */
    private List<CuratorFramework> clients = Lists.newArrayList();
    /**
     * worker服务器列表
     */
    private List<WorkServer> workServers = Lists.newArrayList();

    public static void main(String[] args) throws Exception {
        LeaderSelectorCurator selector = new LeaderSelectorCurator();

        try {
            for (int i = 0; i < CLIENT_QTY; ++i) {
                CuratorFramework client = SessionManager.createSession();
                selector.getClients().add(client);

                ServerMessage serverMessage = new ServerMessage();
                serverMessage.setId(new Long(i));
                serverMessage.setType(ServerType.PROXY.getCode());
                serverMessage.setName("Client #" + i);

                WorkServer workServer = new WorkServer(client, PATH, serverMessage);
                workServer.setListener(new IRunningListener() {
                    @Override
                    public void processStart(Object context) {
                        logger.info(context.toString() + "- processStart...");
                    }

                    @Override
                    public void processStop(Object context) {
                        logger.info(context.toString() + "- processStop...");
                    }

                    @Override
                    public void processActiveEnter(Object context) {
                        logger.info(context.toString() + "- processActiveEnter...");
                    }

                    @Override
                    public void processActiveExit(Object context) {
                        logger.info(context.toString() + "- processActiveExit...");
                    }

                });

                selector.getWorkServers().add(workServer);
                workServer.start();
            }

            logger.info("Press enter/return to quit\n");
            new BufferedReader(new InputStreamReader(System.in)).readLine();
        } finally {
            logger.info("Shutting down...");
            selector.release();
        }
    }

    /**
     * Description: 释放资源
     *
     * @Author:yicai.liu<虚竹子>
     * @Date 2017/4/3 16:35
     */
    public void release() {
        for (WorkServer workServer : workServers) {
            CloseableUtils.closeQuietly(workServer);
        }
        for (CuratorFramework client : clients) {
            CloseableUtils.closeQuietly(client);
        }
    }

    public List<CuratorFramework> getClients() {
        return clients;
    }

    public void setClients(List<CuratorFramework> clients) {
        this.clients = clients;
    }

    public List<WorkServer> getWorkServers() {
        return workServers;
    }

    public void setWorkServers(List<WorkServer> workServers) {
        this.workServers = workServers;
    }
}
