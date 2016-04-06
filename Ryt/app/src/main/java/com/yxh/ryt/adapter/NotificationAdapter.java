package com.yxh.ryt.adapter;

import android.content.Context;
import android.graphics.Color;

import com.yxh.ryt.R;
import com.yxh.ryt.vo.Notification;

import java.util.List;

/**
 * Created by Administrator on 2016/4/5.
 */
public class NotificationAdapter extends CommonAdapter<Notification> {
    public NotificationAdapter(Context context, List<Notification> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);

    }

    @Override
    public void convert(ViewHolder helper, Notification item) {
        if (item.getIsWatch()==0){
            helper.setColor(R.id.cl_01_tv_title, Color.YELLOW);
        }
    }
}
