package com.yxh.ryt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.viewpagerindicator.TabPageIndicator;
import com.yxh.ryt.R;
import com.yxh.ryt.adapter.RZTitlePageIndicatorAdapter;
import com.yxh.ryt.fragment.BaseFragment;
import com.yxh.ryt.fragment.RZDetailFragment;
import com.yxh.ryt.fragment.RZInvestFragment;
import com.yxh.ryt.fragment.RZProjectFragment;
import com.yxh.ryt.fragment.WorksFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/5.
 */
public class FinanceSummaryActivity extends BaseActivity implements View.OnClickListener {
    List<BaseFragment> rZFragments=new ArrayList<>();
    FragmentPagerAdapter rZAdapter;
    private ImageView back;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_financesummary);
        back = (ImageView) findViewById(R.id.ib_top_lf);
        title = ((TextView) findViewById(R.id.afs_tv_title));
        back.setOnClickListener(this);
        Intent intent = this.getIntent();
        String artWorkId = intent.getStringExtra("id");
        String name = intent.getStringExtra("name");
        String userId=intent.getStringExtra("userId");
        rZFragments.add(new RZProjectFragment(artWorkId));
        rZFragments.add(new RZDetailFragment(artWorkId));
        rZFragments.add(new RZInvestFragment(artWorkId));
        rZFragments.add(new WorksFragment(userId));
        title.setText(name);
        rZAdapter = new RZTitlePageIndicatorAdapter(this.getSupportFragmentManager(),rZFragments);
        ViewPager pager = (ViewPager)findViewById(R.id.pager);
        pager.setOffscreenPageLimit(3);
        pager.setAdapter(rZAdapter);
        //实例化TabPageIndicator然后设置ViewPager与之关联
        TabPageIndicator mindicator = (TabPageIndicator) findViewById(R.id.indicator);
        mindicator.setViewPager(pager);


    }

    public FinanceSummaryActivity() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_top_lf:
                finish();
                break;
            default:
                break;
        }
    }
}
