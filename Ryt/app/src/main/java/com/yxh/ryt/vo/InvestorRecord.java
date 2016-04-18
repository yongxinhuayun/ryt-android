package com.yxh.ryt.vo;

/**
 * Created by Administrator on 2016/4/14.
 */
public class InvestorRecord {

    /**
     * id : 3
     * price : 66.0
     * status : 1
     * creator : {"id":"ich9th9y00008h8v","username":"wangwu","name":"王五","pictureUrl":"http: //tenant.efeiyi.com/background/蔡水况.jpg","cityId":"null","status":"null","createDatetime":"null","type":"10000","master":"null"}
     * createDatetime : 1.456218968E12
     */

    private String id;
    private String price;
    private String status;
    /**
     * id : ich9th9y00008h8v
     * username : wangwu
     * name : 王五
     * pictureUrl : http: //tenant.efeiyi.com/background/蔡水况.jpg
     * cityId : null
     * status : null
     * createDatetime : null
     * type : 10000
     * master : null
     */

    private CreatorBean creator;
    private String createDatetime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public CreatorBean getCreator() {
        return creator;
    }

    public void setCreator(CreatorBean creator) {
        this.creator = creator;
    }

    public String getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(String createDatetime) {
        this.createDatetime = createDatetime;
    }

    public static class CreatorBean {
        private String id;
        private String username;
        private String name;
        private String pictureUrl;
        private String cityId;
        private String status;
        private String createDatetime;
        private String type;
        private String master;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPictureUrl() {
            return pictureUrl;
        }

        public void setPictureUrl(String pictureUrl) {
            this.pictureUrl = pictureUrl;
        }

        public String getCityId() {
            return cityId;
        }

        public void setCityId(String cityId) {
            this.cityId = cityId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCreateDatetime() {
            return createDatetime;
        }

        public void setCreateDatetime(String createDatetime) {
            this.createDatetime = createDatetime;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getMaster() {
            return master;
        }

        public void setMaster(String master) {
            this.master = master;
        }
    }
}
