package com.fsmeeting.bean;

import java.io.Serializable;

/**
 * Description:挂载到Zookeeper上的服务器
 *
 * @Author:yicai.liu<虚竹子>
 */
public class ServerMessage implements Serializable {

    private static final long serialVersionUID = -522933808813265389L;

    /**
     * 服务器ID
     */
    private Long id;

    /**
     * 服务器类型
     */
    private Integer type;

    /**
     * 服务器名称
     */
    private String name;

    /**
     * 生死符
     */
    private boolean active = true;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

}
