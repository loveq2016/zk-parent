package com.fsmeeting.bean;

import java.io.Serializable;

/**
 * Discription:
 * Created by yicai.liu<虚竹子> on 2017/3/27.
 */
public class LiveServices implements Serializable {

    private static final long serialVersionUID = 7649335429005950497L;

    private int id;

    private String name;

    private String address;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
