package com.yxh.ryt.custemview;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;
public class MyScrollView extends ScrollView {
	private OnScrollListener onScrollListener;
	private int lastScrollY;
	
	public MyScrollView(Context context) {
		this(context, null);
	}
	
	public MyScrollView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MyScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public void setOnScrollListener(OnScrollListener onScrollListener) {
		this.onScrollListener = onScrollListener;
	}


	private Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			int scrollY = MyScrollView.this.getScrollY();
			
			if(lastScrollY != scrollY){
				lastScrollY = scrollY;
				handler.sendMessageDelayed(handler.obtainMessage(), 5);  
			}
			if(onScrollListener != null){
				onScrollListener.onScroll(scrollY);
			}
			
		};

	}; 

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if(onScrollListener != null){
			onScrollListener.onScroll(lastScrollY = this.getScrollY());
		}
		switch(ev.getAction()){
		case MotionEvent.ACTION_UP:
	         handler.sendMessageDelayed(handler.obtainMessage(), 5);  
			break;
		}
		return super.onTouchEvent(ev);
	}


	public interface OnScrollListener{
		public void onScroll(int scrollY);
	}
	
	

}
