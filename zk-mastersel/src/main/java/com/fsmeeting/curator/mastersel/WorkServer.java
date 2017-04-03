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
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Description: leader 选举,{@link LeaderSelectorListenerAdapter}：处理连接状态问题
 *
 * @Author:yicai.liu<虚竹子>
 */
public class WorkServer extends LeaderSelectorListenerAdapter implements Closeable {

    private static final Logger logger = LoggerFactory.getLogger(WorkServer.class);

    /**
     * 服务器
     */
    private ServerMessage serverMessage;

    /**
     * 选主器
     */
    private final LeaderSelector leaderSelector;

    /**
     * 监听器
     */
    private IRunningListener listener;

    /**
     * 初始化
     *
     * @param client
     * @param path
     * @param serverMessage
     */
    public WorkServer(CuratorFramework client, String path, ServerMessage serverMessage) {
        this.serverMessage = serverMessage;
        leaderSelector = new LeaderSelector(client, path, this);
        leaderSelector.autoRequeue();// 放弃选主的时候要自动重入队列
    }

    /**
     * 工作服务启动
     *
     * @throws IOException
     */
    public void start() throws IOException {
        leaderSelector.start();
        processStart(this.getServerMessage().getId());
    }

    /**
     * 工作服务停止
     *
     * @throws IOException
     */
    public void close() throws IOException {
        leaderSelector.close();
        processStop(this.getServerMessage().getId());
    }

    /**
     * Description:
     * 你可以在takeLeadership进行任务的分配等等，并且不要返回，如果你想要要此实例一直是leader的话可以加一个死循环。
     * 一旦此方法执行完毕之后，就会重新选举
     *
     * @Author:yicai.liu<虚竹子>
     */
    @Override
    public void takeLeadership(CuratorFramework client) throws Exception {

        processActiveEnter(this.getServerMessage().getId());

        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(5));
        } catch (InterruptedException e) {
            logger.info(serverMessage.getName() + " was interrupted.");
            Thread.currentThread().interrupt();
        } finally {
            logger.info(serverMessage.getName() + " relinquishing leadership.\n");
            processActiveExit(this.getServerMessage().getId());
        }
    }

    private void processStart(Object context) {
        if (listener != null) {
            try {
                listener.processStart(context);
            } catch (Exception e) {
                logger.error("processStart failed", e);
            }
        }
    }

    private void processStop(Object context) {
        if (listener != null) {
            try {
                listener.processStop(context);
            } catch (Exception e) {
                logger.error("processStop failed", e);
            }
        }
    }

    private void processActiveEnter(Object context) {
        if (listener != null) {
            try {
                listener.processActiveEnter(context);
            } catch (Exception e) {
                logger.error("processActiveEnter failed", e);
            }
        }
    }

    private void processActiveExit(Object context) {
        if (listener != null) {
            try {
                listener.processActiveExit(context);
            } catch (Exception e) {
                logger.error("processActiveExit failed", e);
            }
        }
    }

    public IRunningListener getListener() {
        return listener;
    }

    public void setListener(IRunningListener listener) {
        this.listener = listener;
    }


    public ServerMessage getServerMessage() {
        return serverMessage;
    }

    public void setServerMessage(ServerMessage serverMessage) {
        this.serverMessage = serverMessage;
    }
}
