/**
 * Created by Administrator on 2016/6/2 0002.
 */


hostName = "http://localhost";                           //服务器域名

function redirectUser(userid) {
    window.demo.clickOnAndroid(userid);
    //window.location.href = "rongyitou://jumpToUserHome_?" + userid;
}          // 跳转到用户的个人主页


function redirectPay(price, type) {
    var artWorkId = getParamObject()["artWorkId"];
    var currentUserId = getCurrentUserId();
    window.location.href = "";
    //去充值 去付尾款
}      //@TODO 跳转到付款的页面


function redirectComment(userid) {
    window.location.href = "";
}       //@TODO 跳转到评论

function redirectConsumerAddress(){
    
}