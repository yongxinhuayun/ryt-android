/**
 * Created by Administrator on 2016/6/7 0007.
 */

// function loginFunction(artWorkId, currentUserId, username, password)
function redirectLogin(url, param, success, error, callback) {
    if(window.demo.isorLogin()){
        var callbackStr = "ajaxRequest('" + url + "'," + JSON.stringify(param) + "," + success + "," + error + ",'" + callback + "')";
        var paramStr = window.demo.fetchParamObject();
        var paramObject = JSON.parse(paramStr);
        loginFunction(paramObject.artWorkId,paramObject.currentUserId,paramObject.username,paramObject.password,callbackStr);
    }else{
        window.demo.loginSelf();
    }
}


function redirectUser(userid) {
    console.log("test");
//        window.location.href = "rongyitou://userid:skldjflksdjflk"
    window.demo.clickOnAndroid(userid);
}


function redirectComment(artworkId, currentUserId, messageId, fatherCommentId,name) {
    console.log(name+"====================");
    window.demo.comment(artworkId, currentUserId, messageId, fatherCommentId,name);
}       //@TODO 跳转到评论

// hostName = "http://192.168.1.43";                           //服务器域名