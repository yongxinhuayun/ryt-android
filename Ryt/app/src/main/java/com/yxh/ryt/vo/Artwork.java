package com.yxh.ryt.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class Artwork implements Serializable {
    private String id;
    private String title;//标题
    private String brief;//简介
    private String description;//描述
    private String status;  //0 可用  1 废弃
    private BigDecimal investGoalMoney;
    private Date investStartDatetime;//融资开始时间
    private Date investEndDatetime;//融资结束时间、创作开始时间
    private Date auctionStartDatetime;//拍卖开始时间
    private Date auctionEndDatetime;
    private User author;
//    private Master master;
    private Date createDatetime;
    private List<ArtworkAttachment> artworkAttachment;
    private List<ArtworkComment> artworkComments;//项目评论
    private List<ArtworkInvest> artworkInvests;//项目投资

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getInvestGoalMoney() {
        return investGoalMoney;
    }

    public void setInvestGoalMoney(BigDecimal investGoalMoney) {
        this.investGoalMoney = investGoalMoney;
    }

    public Date getInvestStartDatetime() {
        return investStartDatetime;
    }

    public void setInvestStartDatetime(Date investStartDatetime) {
        this.investStartDatetime = investStartDatetime;
    }

    public Date getInvestEndDatetime() {
        return investEndDatetime;
    }

    public void setInvestEndDatetime(Date investEndDatetime) {
        this.investEndDatetime = investEndDatetime;
    }

    public Date getAuctionStartDatetime() {
        return auctionStartDatetime;
    }

    public void setAuctionStartDatetime(Date auctionStartDatetime) {
        this.auctionStartDatetime = auctionStartDatetime;
    }

    public Date getAuctionEndDatetime() {
        return auctionEndDatetime;
    }

    public void setAuctionEndDatetime(Date auctionEndDatetime) {
        this.auctionEndDatetime = auctionEndDatetime;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public List<ArtworkAttachment> getArtworkAttachment() {
        return artworkAttachment;
    }

    public void setArtworkAttachment(List<ArtworkAttachment> artworkAttachment) {
        this.artworkAttachment = artworkAttachment;
    }

    public List<ArtworkComment> getArtworkComments() {
        return artworkComments;
    }

    public void setArtworkComments(List<ArtworkComment> artworkComments) {
        this.artworkComments = artworkComments;
    }

    public List<ArtworkInvest> getArtworkInvests() {
        return artworkInvests;
    }

    public void setArtworkInvests(List<ArtworkInvest> artworkInvests) {
        this.artworkInvests = artworkInvests;
    }

    public ArtworkDraw getArtworkDraw() {
        return artworkDraw;
    }

    public void setArtworkDraw(ArtworkDraw artworkDraw) {
        this.artworkDraw = artworkDraw;
    }

    public String getPicture_url() {
        return picture_url;
    }

    public void setPicture_url(String picture_url) {
        this.picture_url = picture_url;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public BigDecimal getInvestsMoney() {
        return investsMoney;
    }

    public void setInvestsMoney(BigDecimal investsMoney) {
        this.investsMoney = investsMoney;
    }

    public Date getCreationEndDatetime() {
        return creationEndDatetime;
    }

    public void setCreationEndDatetime(Date creationEndDatetime) {
        this.creationEndDatetime = creationEndDatetime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNewCreationDate() {
        return newCreationDate;
    }

    public void setNewCreationDate(String newCreationDate) {
        this.newCreationDate = newCreationDate;
    }

    public Integer getAuctionNum() {
        return auctionNum;
    }

    public void setAuctionNum(Integer auctionNum) {
        this.auctionNum = auctionNum;
    }

    public BigDecimal getNewBidingPrice() {
        return newBidingPrice;
    }

    public void setNewBidingPrice(BigDecimal newBidingPrice) {
        this.newBidingPrice = newBidingPrice;
    }

    public String getNewBiddingDate() {
        return newBiddingDate;
    }

    public void setNewBiddingDate(String newBiddingDate) {
        this.newBiddingDate = newBiddingDate;
    }

    private ArtworkDraw artworkDraw;
    private String picture_url;
    private String step; //1 : 审核阶段
    private BigDecimal investsMoney;//已筹金额
    private Date creationEndDatetime;//创作完成时间=融资结束时间+30(默认)
    private String type;//1 融资阶段  2 制作阶段  3 拍卖阶段  4 抽奖阶段

    private String newCreationDate;//最新创作时间

    private  Integer  auctionNum;//竞价记录次数
    private  BigDecimal newBidingPrice;//最新竞价价格
    private  String newBiddingDate;//最新出价时间

}
