package com.yxh.ryt.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.callback.RegisterCallBack;
import com.yxh.ryt.custemview.CircleImageView;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.vo.ChatMsgEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class ChatMsgViewAdapter extends BaseAdapter {

    public static interface IMsgViewType {
        int IMVT_COM_MSG = 0;
        int IMVT_TO_MSG = 1;
    }

    private String userId;
    private String fromId;
    private long millionSeconds1;
    private long millionSeconds2;
    private static final String TAG = ChatMsgViewAdapter.class.getSimpleName();

    private List<ChatMsgEntity> coll;

    private Context ctx;
    private ChatMsgEntity entity;

    private LayoutInflater mInflater;
    private MediaPlayer mMediaPlayer = new MediaPlayer();

    public ChatMsgViewAdapter(Context context, List<ChatMsgEntity> coll, String userId, String fromId) {
        ctx = context;
        this.coll = coll;
        this.userId = userId;
        this.fromId = fromId;
        mInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return coll.size();
    }

    public Object getItem(int position) {
        return coll.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public int getItemViewType(int position) {
        // TODO Auto-generated method stub
        entity = coll.get(position);

        if (entity.getMsgType()) {
            return IMsgViewType.IMVT_COM_MSG;
        } else {
            return IMsgViewType.IMVT_TO_MSG;
        }

    }

    public int getViewTypeCount() {
        // TODO Auto-generated method stub
        return 2;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        final ChatMsgEntity entity = coll.get(position);
        boolean isComMsg = entity.getMsgType();

        ViewHolder viewHolder = null;
        if (convertView == null) {
            if (isComMsg) {
                convertView = mInflater.inflate(
                        R.layout.chatting_item_msg_text_left, null);
            } else {
                convertView = mInflater.inflate(
                        R.layout.chatting_item_msg_text_right, null);
            }

            viewHolder = new ViewHolder();
            viewHolder.tvSendTime = (TextView) convertView
                    .findViewById(R.id.tv_sendtime);
            /*viewHolder.tvUserName = (TextView) convertView
					.findViewById(R.id.tv_username);*/
            viewHolder.tvContent = (TextView) convertView
                    .findViewById(R.id.tv_chatcontent);
            viewHolder.tvTime = (TextView) convertView
                    .findViewById(R.id.tv_time);
            viewHolder.userHead = (CircleImageView) convertView
                    .findViewById(R.id.iv_userhead);
            viewHolder.isComMsg = isComMsg;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        if (position == 0) {
            viewHolder.tvSendTime.setText(entity.getDate());
        } else {
            try {
                millionSeconds1 = sdf.parse(coll.get(position).getDate().toString()).getTime();
                millionSeconds2 = sdf.parse(coll.get(position - 1).getDate().toString()).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if ((millionSeconds1 - millionSeconds2) > 300000) {
                viewHolder.tvSendTime.setText(entity.getDate());
            } else {
                viewHolder.tvSendTime.setVisibility(View.GONE);
            }
        }

//		if (entity.getText().contains(".amr")) {
//			viewHolder.tvContent.setText("");
//			viewHolder.tvContent.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.chatto_voice_playing, 0);
//			viewHolder.tvTime.setText(entity.getTime());
//		} else {
        viewHolder.tvContent.setText(entity.getText());
        viewHolder.tvContent.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        viewHolder.tvTime.setText("");
//		}
        //viewHolder.tvUserName.setText(entity.getName());

        inflatImage(userId, entity.getUserId(), viewHolder.userHead);
        return convertView;
    }

    static class ViewHolder {
        public TextView tvSendTime;
        //public TextView tvUserName;
        public TextView tvContent;
        public TextView tvTime;
        public CircleImageView userHead;
        public boolean isComMsg = true;
    }

    /**
     * @param name
     * @Description
     */
    private void playMusic(String name) {
        try {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(name);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
            mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void stop() {

    }

    public void inflatImage(String userId, String fromId, final CircleImageView imageView) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("userId", entity.getUserId());
        paramsMap.put("currentId", userId);
        paramsMap.put("pageIndex", "1");
        paramsMap.put("pageSize", "20");
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            paramsMap.put("signmsg", EncryptUtil.encrypt(paramsMap));
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "my.do", paramsMap, new RegisterCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                System.out.println("失败了");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                //User user = new User();
                //user = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(response.get("pageInfo")), User.class);
                Map<String, Map<String, String>> map11 = (Map<String, Map<String, String>>) response.get("pageInfo");
                Map<String, String> map22 = null;
                if (map11 != null) {
                    map22 = map11.get("user");
                }
                String pic_url = null;
                if (map22 != null) {
                    pic_url = map22.get("pictureUrl");
                }
                AppApplication.displayImage(pic_url, imageView);
				/*NetRequestUtil.downloadImage(artwork.getPicture_url(), new BitmapCallback() {
					@Override
					public void onError(Call call, Exception e) {

					}

					@Override
					public void onResponse(Bitmap response) {
						if (response != null) {
							File sampleDir = new File(Environment.getExternalStorageDirectory() + File.separator);
							if (!sampleDir.exists()) {
*/
	/*		if(user != null){
				NetRequestUtil.downloadImage(pic_url, new BitmapCallback() {
					@Override
					public void onError(Call call, Exception e) {

					}

					@Override
					public void onResponse(Bitmap response) {
						imageView.setImageBitmap(response);
					}
				});
			}*/


            }
        });
    }
}
