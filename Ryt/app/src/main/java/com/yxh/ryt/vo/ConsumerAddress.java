package com.yxh.ryt.vo;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/4/19.
 *
 */

public class ConsumerAddress implements Serializable {
    private String id;
    private AddressProvince province;//省
    private AddressProvince district;//地区
    private AddressProvince city;//城市
    private String details;//详细地址
    private String post;
    private String phone;//收货人手机号
    private String email;
    private User consumer;//关联用户
    private String status;   //1 正常的 2 默认的
    private  String consignee; //收货人姓名
    private String provinceStr;//省
    private String districtStr;//区
    private String cityStr;//市


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public AddressProvince getCity() {
        return city;
    }

    public void setCity(AddressProvince city) {
        this.city = city;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public AddressProvince getProvince() {
        return province;
    }

    public void setProvince(AddressProvince province) {
        this.province = province;
    }

    public AddressProvince getDistrict() {
        return district;
    }

    public void setDistrict(AddressProvince district) {
        this.district = district;
    }

    public User getConsumer() {
        return consumer;
    }

    public void setConsumer(User consumer) {
        this.consumer = consumer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    @Override
    public String toString() {
        return "ConsumerAddress{id = " + id + "}";
    }

    public String getCityStr() {
        return cityStr;
    }

    public void setCityStr(String cityStr) {
        this.cityStr = cityStr;
    }

    public String getDistrictStr() {
        return districtStr;
    }

    public void setDistrictStr(String districtStr) {
        this.districtStr = districtStr;
    }

    public String getProvinceStr() {
        return provinceStr;
    }

    public void setProvinceStr(String provinceStr) {
        this.provinceStr = provinceStr;
    }
}
