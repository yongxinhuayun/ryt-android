package com.yxh.ryt.vo;


import java.io.Serializable;

/**
 * User: Kyll
 * Time: 2008-7-3 15:27:49
 */

public class AddressProvince implements Serializable {
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
