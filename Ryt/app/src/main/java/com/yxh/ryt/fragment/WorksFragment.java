package com.yxh.ryt.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.callback.RZCommentCallBack;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.vo.MasterWork;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/7/18.
 */
public class WorksFragment extends BaseFragment {
    private String userId;
    private List<MasterWork> workDatas;
    private int currentPage=1;
    public WorksFragment(String userId) {
        this.userId = userId;
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        workDatas=new ArrayList<MasterWork>();
        loadData(true, currentPage);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_works, null);
        GridView gridView = (GridView) view.findViewById(R.id.gridview);
        gridView.setAdapter(new ImageAdapter(getActivity(),workDatas));
        //单击GridView元素的响应

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //弹出单击的GridView元素的位置
                Toast.makeText(getActivity(), mThumbIds[position], Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    private class ImageAdapter extends BaseAdapter {
        private Context mContext;
        private LayoutInflater mInflater;
        private List<MasterWork> mDatas;

        public ImageAdapter(Context context, List<MasterWork> mDatas) {
            mInflater = LayoutInflater.from(context);
            this.mContext = context;
            this.mDatas = mDatas;
        }

        @Override
        public int getCount() {
            if (mDatas == null) {return 0;}
            return mDatas.size();
        }

        @Override
        public Object getItem(int position) {
            return mDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //定义一个ImageView,显示在GridView里
            //ImageView imageView;
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_image_works, parent,
                        false);
                viewHolder = new ViewHolder();
                /*imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(140, 250));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(8, 8, 8, 8);*/
                viewHolder.mImageView = (ImageView) convertView.findViewById(R.id.iv_works);
                viewHolder.mTitle = (TextView) convertView.findViewById(R.id.tv_title);
                viewHolder.mNum = (TextView) convertView.findViewById(R.id.tv_works_num);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            AppApplication.displayImage(mDatas.get(position).getPictureUrl(),viewHolder.mImageView);
            viewHolder.mTitle.setText(mDatas.get(position).getName());
           // viewHolder.mNum.setText(mDatas.get(position).get);
            return convertView;

        }
        private final class ViewHolder
        {
            ImageView mImageView;
            TextView mTitle;
            TextView mNum;
        }

    }

    //展示图片
    private Integer[] mThumbIds = {
            R.mipmap.abaose, R.mipmap.about_ryt,
            R.mipmap.abaose, R.mipmap.about_ryt,
            R.mipmap.abaose, R.mipmap.about_ryt,
            R.mipmap.abaose, R.mipmap.about_ryt,
            R.mipmap.abaose, R.mipmap.about_ryt,
            R.mipmap.abaose, R.mipmap.about_ryt,
            R.mipmap.abaose, R.mipmap.about_ryt,
            R.mipmap.abaose, R.mipmap.about_ryt,
            R.mipmap.abaose, R.mipmap.about_ryt,
            R.mipmap.abaose, R.mipmap.about_ryt,
            R.mipmap.abaose, R.mipmap.about_ryt,
            R.mipmap.abaose, R.mipmap.about_ryt,
            R.mipmap.abaose, R.mipmap.about_ryt,
            R.mipmap.abaose, R.mipmap.about_ryt,
            R.mipmap.abaose, R.mipmap.about_ryt,
            R.mipmap.abaose, R.mipmap.about_ryt,

    };

    private void loadData(final boolean flag,int pageNum) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("userId", userId);
        paramsMap.put("pageSize", Constants.pageSize + "");
        paramsMap.put("pageIndex", pageNum + "");
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            AppApplication.signmsg = EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "userWork.do", paramsMap, new RZCommentCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                System.out.println("444444失败了");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                if ("0".equals(response.get("resultCode"))) {
                    Map<String, Object> object = (Map<String, Object>) response.get("object");
                    ImageAdapter imageAdapter = new ImageAdapter(getActivity(),workDatas);
                    if (flag) {
                        List<MasterWork> imageList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(object.get("masterWorkList")), new TypeToken<List<MasterWork>>() {
                        }.getType());
                        if (imageList != null) {
                            workDatas.addAll(imageList);
                            imageList.clear();
                        }
                        imageAdapter.notifyDataSetChanged();

                    } else {
                        List<MasterWork> imageList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(object.get("masterWorkList")), new TypeToken<List<MasterWork>>() {
                        }.getType());

                        if (imageList != null) {
                            workDatas.addAll(imageList);
                            imageList.clear();
                        }
                        imageAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
        }
    }


