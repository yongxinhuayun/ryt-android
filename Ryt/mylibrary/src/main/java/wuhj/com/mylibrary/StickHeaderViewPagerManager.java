package wuhj.com.mylibrary;

import android.content.Context;
import android.os.Build;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Any problem about the library. Contact me
 * <p/>
 * https://github.com/w446108264/StickHeaderLayout
 * shengjun8486@gmail.com
 * <p/>
 * Created by sj on 15/11/22.
 */
public class StickHeaderViewPagerManager implements ViewPager.OnPageChangeListener, StickHeaderLayout.OnPlaceHoderListener, ScrollHolder {
    private SparseArrayCompat<PlaceHoderHeaderLayout> placeHoderHeaderLayoutList = new SparseArrayCompat<>();
    private HashSet<Integer> canPullToRefreshPosiTionSet = new HashSet<>();
    private ViewPager mViewPager;
    private StickHeaderLayout mStickHeaderLayout;
    private int placeHoderHeight;
    private int mStickHeaderTranslationY;
    OnListViewScrollListener onListViewScrollListener;
    public StickHeaderViewPagerManager(StickHeaderLayout stickHeaderLayout, ViewPager viewPager) {
        this.mViewPager = viewPager;
        this.mStickHeaderLayout = stickHeaderLayout;
        mViewPager.addOnPageChangeListener(this);
        mStickHeaderLayout.addOnPlaceHoderListener(this);
    }
    public OnListViewScrollListener getOnListViewScrollListener() {
        return onListViewScrollListener;
    }

    public void setOnListViewScrollListener(OnListViewScrollListener onListViewScrollListener) {
        this.onListViewScrollListener = onListViewScrollListener;
    }

    public void addPlaceHoderHeaderLayout(final int position, final PlaceHoderHeaderLayout layout) {
        addPlaceHoderHeaderLayout(position, layout, true);
    }

    public void addPlaceHoderHeaderLayout(final int position, final PlaceHoderHeaderLayout layout, boolean isCanPullToRefresh) {
        if (mStickHeaderLayout == null) {
            throw new IllegalStateException("StickHeaderLayout can not be null");
        }
        if (layout == null) {
            return;
        }
        if (isCanPullToRefresh) {
            canPullToRefreshPosiTionSet.add(position);
        }
        placeHoderHeaderLayoutList.put(position, layout);
        layout.updatePlaceHeight(placeHoderHeight, StickHeaderViewPagerManager.this, position);
        layout.setOnAttachedToWindowListener(new PlaceHoderHeaderLayout.OnAttachedToWindowListener() {
            @Override
            public void onAttachedToWindow(PlaceHoderHeaderLayout placeHoderHeaderLayout) {
                layout.updatePlaceHeight(placeHoderHeight, StickHeaderViewPagerManager.this, position);
            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        int currentItem = mViewPager.getCurrentItem();
        if (positionOffsetPixels > 0) {
            PlaceHoderHeaderLayout placeHoderHeaderLayout;
            if (position < currentItem) {
                placeHoderHeaderLayout = placeHoderHeaderLayoutList.valueAt(position);
            } else {
                placeHoderHeaderLayout = placeHoderHeaderLayoutList.valueAt(position + 1);
            }
            View mStickheader = mStickHeaderLayout.getStickHeaderView();
            if (placeHoderHeaderLayout != null) {
                placeHoderHeaderLayout.adjustScroll((int) (mStickheader.getHeight() + mStickheader.getTranslationY()+2), mStickheader.getHeight(), false);
            }
        }
    }
    @Override
    public void onPageSelected(int position) {
        if (placeHoderHeaderLayoutList == null) {
            return;
        }
        PlaceHoderHeaderLayout placeHoderHeaderLayout = placeHoderHeaderLayoutList.valueAt(position);
        View mStickheader = mStickHeaderLayout.getStickHeaderView();
        if (placeHoderHeaderLayout != null) {
            placeHoderHeaderLayout.adjustScroll((int) (mStickheader.getHeight() + mStickheader.getTranslationY()+2), mStickheader.getHeight(), true);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onSizeChanged(int placeHoderHeight, int stickHeight) {
        this.placeHoderHeight = placeHoderHeight;
        for (int i = 0; i < placeHoderHeaderLayoutList.size(); i++) {
            if (placeHoderHeaderLayoutList.get(i) != null) {
                placeHoderHeaderLayoutList.get(i).updatePlaceHeight(placeHoderHeight, this, placeHoderHeaderLayoutList.indexOfValue(placeHoderHeaderLayoutList.get(i)));
            }
        }
    }

    @Override
    public void onScrollChanged(int height) {
        mStickHeaderTranslationY = height;
        if(onHeaderScrollListener != null){
            onHeaderScrollListener.onScrollChanged(height);
        }
    }

    @Override
    public void onHeaderTranslationY(float translationY) {

    }

    @Override
    public void onListViewScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount, int pagePosition) {
        if(onListViewScrollListener!=null)onListViewScrollListener.onListViewScroll(view,firstVisibleItem,visibleItemCount,totalItemCount-1);
        System.out.println(firstVisibleItem+"====="+visibleItemCount+"======="+totalItemCount);
        if (mViewPager.getCurrentItem() == pagePosition) {
            mStickHeaderLayout.onListViewScroll(view, firstVisibleItem, visibleItemCount, totalItemCount, pagePosition);
        }
    }

    @Override
    public void onListViewScrollStateChanged(AbsListView view, int state) {
        if(onListViewScrollListener!=null)onListViewScrollListener.onListViewScrollStateChanged(view,state);
    }

    @Override
    public void onScrollViewScroll(ScrollView view, int x, int y, int oldX, int oldY, int pagePosition) {
        if (mViewPager.getCurrentItem() == pagePosition) {
            mStickHeaderLayout.onScrollViewScroll(view, x, y, oldX, oldY, pagePosition);
        }
    }

    @Override
    public void onRecyclerViewScroll(RecyclerView view, int scrollY, int pagePosition, boolean isScrollToTop) {
        if (mViewPager.getCurrentItem() == pagePosition) {
            mStickHeaderLayout.onRecyclerViewScroll(view, scrollY, pagePosition, isScrollToTop);
        }
    }

    public boolean isCanPullToRefresh() {
        int currentItem = mViewPager.getCurrentItem();
        if (mStickHeaderTranslationY <= 0 && canPullToRefreshPosiTionSet.contains(currentItem)) {
            return mStickHeaderLayout == null ? true : !mStickHeaderLayout.isHorizontalScrolling();
        }
        return false;
    }

    public int getStickHeaderTranslationY() {
        return mStickHeaderTranslationY;
    }

    OnHeaderScrollListener onHeaderScrollListener;

    public void setOnPlaceHoderListener(OnHeaderScrollListener onHeaderScrollListener) {
        this.onHeaderScrollListener = onHeaderScrollListener;
    }

    public interface OnHeaderScrollListener {
        void onScrollChanged(int height);
    }

    public interface OnListViewScrollListener {
        void onListViewScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount);
        void onListViewScrollStateChanged(AbsListView view,int scrollState);
    }
}
