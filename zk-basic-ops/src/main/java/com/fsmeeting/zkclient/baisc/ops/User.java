package com.fsmeeting.zkclient.baisc.ops;

import java.io.Serializable;

public class User implements Serializable {

    private static final long serialVersionUID = -1740642644191035157L;
    private Integer id;
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
