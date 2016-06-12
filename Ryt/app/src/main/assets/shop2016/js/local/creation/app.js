/**
* Created by Administrator on 2016/6/7 0007.
*/


function redirectUser(userid) {
    console.log("test");
//        window.location.href = "rongyitou://userid:skldjflksdjflk"
        window.demo.clickOnAndroid(userid);
}

function redirectComment(artworkId, currentUserId, messageId, fatherCommentId) {
    window.location.href = "";
}       //跳转到评论

hostName = "http://192.168.1.75:8080";                           //服务器域名