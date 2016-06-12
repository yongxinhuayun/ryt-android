/**
 * Created by Administrator on 2016/5/31 0031.
 */
function initPage(artWorkId, currentUserId) {
    var param = new Object();
    param.artWorkId = artWorkId;
    param.currentUserId = currentUserId;
    PageVariable.param = param;
    PageVariable.artWorkId = artWorkId;
    refreshPageEntity();
    getArtWorkBaseInfoData(getArtWorkBaseInfo);
    getArtWorkDetailData(getArtWorkDetail);
    getCurrentDefaultConsumerAddressData();
}

function getAuctionTimeResult() {
    if (PageVariable.artWorkInfo.step == "30") {
        countDown(PageVariable.artWorkInfo.auctionStartDatetime);
    } else if (PageVariable.artWorkInfo.step == "31") {
        countDown(PageVariable.artWorkInfo.auctionEndDatetime);
    }
}

//获得项目基本信息的controller
function getArtWorkBaseInfo() {
    var artWorkInfo = PageVariable.artWorkInfo;
    var artWorkProject = PageVariable.artWorkProject;
    if (!artWorkProject.messageList.length > 0) {
        $("#dt").hide();
    }
    $("#mainPicture").html(getArtWorkBaseInfoPictureHtml(artWorkInfo));
    $("#mainInfo").html(getArtWorkBaseInfoMainHtml(artWorkInfo));
    $("#rz").html(getArtWorkScheduleInvestHtml(artWorkProject));
    $("#cz").html(getArtWorkScheduleCreateHtml(artWorkProject));
    $("#pm").html(getArtWorkScheduleAuctionHtml(artWorkProject));
    $("#dt").append(getArtWorkScheduleMessageHtml(artWorkProject));
    $("#auctionMessage").html(getArtWorkBaseInfoAuctionMessageHtml(artWorkInfo));
    $("#winner").html(getArtWorkAuctionWinnerHtml(artWorkInfo));
    getBottomButton();
    getAuctionTimeResult();
    getArtWorkAuctionData(getArtWorkAuctionBidding);
    tabsHeight();
}
//获得项目详情的controller
function getArtWorkDetail() {
    var artWorkView = PageVariable.artWorkView;
    $("#artWorkView").html(getArtWorkDetailHtml(artWorkView));
    tabsHeight();
}
//获得用户评价的controller
function getArtWorkComment() {
    var artWorkComment = PageVariable.artWorkComment;
    if (pageEntity.pageIndex == 1) {
        $("#userComment").html(getArtWorkCommentHtml(artWorkComment));
    } else {
        $("#userComment").append(getArtWorkCommentHtml(artWorkComment));
    }
    pageEntity.pageIndex = pageEntity.pageIndex + 1;
    tabsHeight();
}

function getArtWorkAuctionBidding() {
    var artWorkAuction = PageVariable.artWorkAuction;
    $("#auctionNum").html(PageVariable.biddingUsersNum + "人参与拍卖");
    if (pageEntity.pageIndex == 1) {
        $("#auctionList").html(getArtWorkAuctionBiddingHtml(artWorkAuction));
    } else {
        $("#auctionList").append(getArtWorkAuctionBiddingHtml(artWorkAuction));
    }
    $("#auctionMessage").html(getArtWorkBaseInfoAuctionMessageHtml(PageVariable.artWorkInfo));
    pageEntity.pageIndex = pageEntity.pageIndex + 1;
    getArtWorkAuctionBiddingTopThree()
    tabsHeight();
}

function getArtWorkAuctionBiddingTopThree() {
    var artWorkAuction = PageVariable.artWorkAuction;
    $("#currentAuctionTopThreeRecord").html(getArtWorkAuctionBiddingTopThreeHtml(artWorkAuction))
}

function getBottomButton() {
    var artWorkBaseInfo = PageVariable.artWorkInfo;
    $("#bottomButton").append(getPreBottomButtonHtml(artWorkBaseInfo));
    $("#bottomButton").append(getBeingBottomButtonHtml(artWorkBaseInfo));
    $("#bottomButton").append(getAfterBottomButtonHtml(artWorkBaseInfo));
    ChooseCountComponent();
}

function getAlert(content) {
    $("#bottomButton").append(getAlertHtml(content));
}
//获得项目的基本信息
function getArtWorkBaseInfoData(callback) {
    var success = function (data) {
        ajaxSuccessFunctionTemplage(function (dataTemp) {
            var obj = dataTemp;
            var msgList = obj["artWorkMessage"];
            var artWork = obj["artwork"];
            var auctionStartDatetime = new Date();
            auctionStartDatetime.setTime(artWork.auctionStartDatetime);
            PageVariable.artWorkInfo = new ArtWorkInfo(artWork.picture_url, artWork.author.name, artWork.brief, artWork.auctionStartDatetime, artWork.step, artWork.title, artWork.author.master.title, artWork.author.pictureUrl, artWork.author.id, artWork.startingPrice, artWork.winner, artWork.auctionEndDatetime, artWork.newBidingPrice, artWork.creationEndDatetime);
            PageVariable.artWorkProject = new ArtWorkProject(artWork.investEndDatetime, artWork.step, artWork.investNum, artWork.investStartDatetime, msgList, artWork.auctionStartDatetime, artWork.creationEndDatetime);
            PageVariable.viewNum = artWork.viewNum;
            PageVariable.auctionNum = artWork.auctionNum;
            PageVariable.startingPrice = artWork.startingPrice;
            PageVariable.isSubmitDepositPrice = obj["isSubmitDepositPrice"];
            // PageVariable.isSubmitDepositPrice = "1";
        }, data, callback);
    }
    ajaxRequest(hostName + RequestUrl.initPage, dealRequestParam(getParamObject()), success, function () {
    }, "post");
}
//获得项目详情信息数据
function getArtWorkDetailData(callback) {
    var success = function (data) {
        ajaxSuccessFunctionTemplage(function (dataTemp) {
            var obj = dataTemp["object"];
            PageVariable.artWorkView = new ArtWorkView(obj.artworkAttachmentList, obj.artWork.description, obj.artworkdirection.make_instru, obj.artworkdirection.financing_aq);
        }, data, callback);
    }
    ajaxRequest(hostName + RequestUrl.artWorkViewTab, dealRequestParam(getParamObject()), success, function () {
    }, "post");
}
//获得项目评价信息数据
function getArtWorkCommentData(callback) {
    var success = function (data) {
        ajaxSuccessFunctionTemplage(function (dataTemp) {
            var obj = dataTemp["object"];
            PageVariable.artWorkComment = new ArtWorkComment(obj.artworkCommentList);
        }, data, callback)
    };
    var param = getParamObject();
    param.pageIndex = pageEntity.pageIndex;
    param.pageSize = pageEntity.pageSize;
    ajaxRequest(hostName + RequestUrl.commentTab, dealRequestParam(param), success, function () {
    }, "post");
}
//拍卖纪录
function getArtWorkAuctionData(callback) {
    var success = function (data) {
        ajaxSuccessFunctionTemplage(function (dataTemp) {
            var obj = dataTemp;
            PageVariable.artWorkAuction = new ArtWorkAuction(obj.artworkBiddingList, obj.biddingTopThree);
            PageVariable.auctionNum = obj.auctionNum;
            PageVariable.biddingUsersNum = obj.biddingUsersNum;
        }, data, callback)
    };
    var param = getParamObject();
    param.pageIndex = pageEntity.pageIndex;
    param.pageSize = pageEntity.pageSize;
    ajaxRequest(hostName + RequestUrl.auctionTab, dealRequestParam(param), success, function () {
    }, "post");
}

function getCurrentDefaultConsumerAddressData(callback) {
    var success = function (data) {
        ajaxSuccessFunctionTemplage(function (dataTemp) {
            var obj = dataTemp;
            PageVariable.consumerAddress = obj.defaultAddress;
        }, data, callback)
    };
    ajaxRequest(hostName + RequestUrl.consumerAddress, dealRequestParam(getParamObject()), success, function () {
    }, "post");
}


function artWorkProjectPanelAction() {
    loadDataAction = function () {
        console.log("bottom");
    }
}                   //页面滑到项目进度时候的action
function artWorkCommentPanelAction() {
    //初始化页面分页信息
    getArtWorkCommentData(getArtWorkComment); //页面从新加载评论的数据
    loadDataAction = function () {
        getArtWorkCommentData(getArtWorkComment);
    }
}                   //页面滑到用户评价时候的action
function artWorkViewPanelAction() {
    loadDataAction = function () {
        console.log("bottom");
    }
}                      //页面滑到项目详情时候的action
function artWorkAuctionPanelAction() {
    //初始化页面分页信息
    getArtWorkAuctionData(getArtWorkAuctionBidding); //页面从新加载拍卖的数据
    loadDataAction = function () {
        getArtWorkAuctionData(getArtWorkAuctionBidding);
    }
}                   //页面滑到拍卖纪录时候的action

function getAlert(content, callback) {
    $(".pm_dialog").remove();
    var out = ' <div class="pm_dialog"> <div class="content" style="height: auto;"> <div class="close"></div> <div class="info" style="text-align: center;padding-bottom: 25px;height: auto"> <p>' + content + '</p> </div> ';
    out += '<a id="confirm" class="btn" title="去充值" style="margin-top: 10px;margin-bottom: 10px;">确认</a> </div> </div>';
    var ParentDiv2 = $("#dialog");
    var dailogClick = function () {
        $('.pm_dialog').remove();
    }
    if (typeof callback == "function") {
        ParentDiv2.on("click", "#confirm", callback);
    } else {
        ParentDiv2.on("click", "#confirm", dailogClick);
    }
    ParentDiv2.html(out);
    // return out;
}