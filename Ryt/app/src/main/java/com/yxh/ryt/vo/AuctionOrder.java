package com.yxh.ryt.vo;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrator on 2016/4/19.
 */

public class AuctionOrder implements Serializable {
    private String id;//订单编号
    private Artwork artwork;//关联项目
    private User user;//下单用户
    private ConsumerAddress consumerAddress;//收货地址
    private Long createDatetime;//创建时间
    private String payWay;// 0 账户余额 1 支付宝 2 微信
    private String status;// 1 可用 0 作废
    private String type;// 0 待付尾款 1 代发货 2交易成功  3待收货
    private BigDecimal finalPayment;//尾款金额
    private String payStatus;// 0 支付成功 1 支付失败 3未支付
    private BigDecimal amount;//拍卖金额

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Artwork getArtwork() {
        return artwork;
    }

    public void setArtwork(Artwork artwork) {
        this.artwork = artwork;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Long createDatetime) {
        this.createDatetime = createDatetime;
    }

    public ConsumerAddress getConsumerAddress() {
        return consumerAddress;
    }

    public void setConsumerAddress(ConsumerAddress consumerAddress) {
        this.consumerAddress = consumerAddress;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getFinalPayment() {
        return finalPayment;
    }

    public void setFinalPayment(BigDecimal finalPayment) {
        this.finalPayment = finalPayment;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
