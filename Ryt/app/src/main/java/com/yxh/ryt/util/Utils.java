package com.yxh.ryt.util;

import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.yxh.ryt.custemview.refreash.PullToRefreshListView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @Author SunnyCoffee
 * @Date 2014-1-28
 * @version 1.0
 * @Desc 工具类
 */

public class Utils {
	private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd");
		}
	};
	public static String timeToFormatTemp(String tmpl,long time){
		SimpleDateFormat sdf = new SimpleDateFormat(tmpl);
		return sdf.format(new Date(time));
	}
	public static String getCurrentTime(String format) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
		String currentTime = sdf.format(date);
		return currentTime;
	}

	public static String getCurrentTime() {
		return getCurrentTime("yyyy-MM-dd  HH:mm:ss");
	}
	public static String timeTrans(long time){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm");
		return sdf.format(new Date(time));
	}
	public static String timeTrans2(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		return sdf.format(new Date(time));
	}
	public static Date timeTransfore(long time) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormater2.get().parse(sdf.format(new Date(time)));
	}
	public static String timeTransComment(long time){
			boolean b = false;
		Date currtime = null;
		try {
			currtime = timeTransfore(time);
		} catch (Exception e) {

		}
		Date today = new Date();
			if(currtime != null){
				String nowDate = dateFormater2.get().format(today);
				String timeDate = dateFormater2.get().format(currtime);
				if(nowDate.equals(timeDate)){
					b = true;
				}
			}
		if (b){
			return timeTrans2(time);
		}
		return  timeTrans(time);
		}
	public static void setListViewHeightBasedOnChildren(GridView listView) {
		// 获取listview的adapter
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		// 固定列宽，有多少列
		int col = 4;// listView.getNumColumns();
		int totalHeight = 0;
		// i每次加4，相当于listAdapter.getCount()小于等于4时 循环一次，计算一次item的高度，
		// listAdapter.getCount()小于等于8时计算两次高度相加
		for (int i = 0; i < listAdapter.getCount(); i += col) {
			// 获取listview的每一个item
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			// 获取item的高度和
			totalHeight += listItem.getMeasuredHeight();
		}

		// 获取listview的布局参数
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		// 设置高度
		params.height = totalHeight+40;
		// 设置margin
		((ViewGroup.MarginLayoutParams) params).setMargins(DisplayUtil.dip2px(6), DisplayUtil.dip2px(10),0, DisplayUtil.dip2px(5));
		// 设置参数
		listView.setLayoutParams(params);
	}
	public static void setListViewHeightBasedOnChildren01(GridView listView) {
		// 获取listview的adapter
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		// 固定列宽，有多少列
		int col = 1;// listView.getNumColumns();
		int totalHeight = 0;
		// i每次加4，相当于listAdapter.getCount()小于等于4时 循环一次，计算一次item的高度，
		// listAdapter.getCount()小于等于8时计算两次高度相加
		for (int i = 0; i < listAdapter.getCount(); i += col) {
			// 获取listview的每一个item
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			// 获取item的高度和
			totalHeight += listItem.getMeasuredHeight();
		}

		// 获取listview的布局参数
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		// 设置高度
		params.height = totalHeight+40;
		// 设置margin
		((ViewGroup.MarginLayoutParams) params).setMargins(DisplayUtil.dip2px(6), DisplayUtil.dip2px(10),0, DisplayUtil.dip2px(5));
		// 设置参数
		listView.setLayoutParams(params);
	}

	public void setListViewHeightBasedOnChildren1(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		params.height += 5;//if without this statement,the listview will be a little short
		listView.setLayoutParams(params);
	}
}
