package com.yxh.ryt.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.yxh.ryt.Constants;

/**
 * Created by 吴洪杰 on 2016/4/6.
 */
public class WxLoginBroadcastReciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Constants.WX_LOGIN_ACTION.equals(action)) {
            String wxuserStr = intent.getExtras().getString("wxuser");
            System.out.println("wxuserStrwxuserStrwxuserStrwxuserStrwxuserStrwxuserStrwxuserStrwxuserStrwxuserStrwxuserStr"+wxuserStr);
//                AppApplication.gwxuser = AppApplication.getSingleGson().fromJson(wxuserStr, Wxuser.class);
        }
    }

}
