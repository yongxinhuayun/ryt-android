/**
* Created by Administrator on 2016/6/7 0007.
*/

function redirectUser(userid) {
    //console.log("test");
    window.location.href = "rongyitou://jumpToUserHome_?"+userid;
}

//function redirectComment(userid) {
//    //跳转评论
//    window.location.href = "comment://jumpToCommentUser_andCommentArtWorkId_?"+userid + "&qydeyugqqiugd2";
//}       //@TODO 跳转到评论     //@TODO 跳转到评论

function redirectComment(artworkId, currentUserId, messageId, commentid) {
    window.location.href = "comment://jumpToCommentUser_andCommentArtWorkId_andCommentMessageId_andCommentFatherCommentId_?"+currentUserId + "&" + artworkId + "&" + messageId + "&" + commentid;
}

function redirectComment(artworkId, currentUserId, messageId, commentid, name) {
    window.location.href = "comment://jumpToCommentUser_andCommentArtWorkId_andCommentMessageId_andCommentFatherCommentId_andName_?"+currentUserId + "&" + artworkId + "&" + messageId + "&" + commentid + "&" + name;
    
}       //@TODO 跳转到评论