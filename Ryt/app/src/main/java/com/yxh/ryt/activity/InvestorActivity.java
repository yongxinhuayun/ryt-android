package com.yxh.ryt.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.yxh.ryt.R;
import com.yxh.ryt.adapter.CommonAdapter;
import com.yxh.ryt.adapter.ViewHolder;
import com.yxh.ryt.custemview.AutoListView;
import com.yxh.ryt.util.DateUtil;
import com.yxh.ryt.vo.ArtworkInvest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InvestorActivity extends Activity {
    private List<ArtworkInvest> investorDatas;
    private CommonAdapter<ArtworkInvest> investorAdapter;
    private AutoListView iListview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investor);
        iListview = (AutoListView) findViewById(R.id.iListview);
        investorDatas = (ArrayList<ArtworkInvest>) getIntent().getSerializableExtra("data");
        initData();
    }

    private void initData() {
        investorAdapter = new CommonAdapter<ArtworkInvest>(this, investorDatas, R.layout.investorrecord_item) {
            @Override
            public void convert(ViewHolder helper, ArtworkInvest item) {
                if (item.getCreator() != null) {
                    helper.setImageByUrl(R.id.iri_iv_icon, item.getCreator().getPictureUrl());
                    if (item.getCreator().getName() != null) {
                        if (item.getCreator().getName().length() > 5) {
                            helper.setText(R.id.iri_tv_nickname, item.getCreator().getName().substring(0, 5) + "...");
                        } else {
                            helper.setText(R.id.iri_tv_nickname, item.getCreator().getName());
                        }
                    }
                }
                helper.getView(R.id.civ_top).setVisibility(View.GONE);
                helper.getView(R.id.cl_01_civ_pm).setVisibility(View.VISIBLE);
                helper.setText(R.id.cl_01_civ_pm, (helper.getPosition() + 1 ) + "");
                // }
                helper.setText(R.id.iri_tv_content, "￥" + item.getPrice() + ".00");
                helper.setText(R.id.iri_tv_date, long2Timestamp(item.getCreateDatetime()));
            }
        };
        iListview.setAdapter(investorAdapter);
    }
    private String long2Timestamp(long time) {
        String sTime = DateUtil.date2String(time, "yyyy-MM-dd  HH:mm:ss");
        Date dt = DateUtil.string2Date(sTime, "yyyy-MM-dd  HH:mm:ss");
        return DateUtil.getTimestampString(dt);
    }
}
