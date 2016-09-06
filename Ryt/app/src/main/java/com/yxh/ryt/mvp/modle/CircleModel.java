package com.yxh.ryt.mvp.modle;

import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.callback.RZCommentCallBack;
import com.yxh.ryt.listener.IDataRequestListener;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.vo.ArtWorkPraise;
import com.yxh.ryt.vo.ArtworkComment;
import com.yxh.ryt.vo.ArtworkMessage;
import com.yxh.ryt.vo.CommentConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;


/**
 * 
* @ClassName: CircleModel 
* @Description: 因为逻辑简单，这里我就不写model的接口了
* @author yiw
* @date 2015-12-28 下午3:54:55 
 */
public class CircleModel {


	private final String artWorkId;
	public CircleModel(String artWorkId){
		//
		this.artWorkId = artWorkId;
	}

	public void loadData(int pageNum, final IDataRequestListener listener){
		requestServer(pageNum, listener);
	}
	
	public void deleteCircle(int pageNum, final IDataRequestListener listener) {
		requestServer(pageNum, listener);
	}

	public void addFavort(ArtworkMessage artworkMessage, int circlePosition, final IDataRequestListener listener) {
		requestAddFavort(artworkMessage, circlePosition, listener);
	}

	private void requestAddFavort(ArtworkMessage artworkMessage, final int circlePosition, final IDataRequestListener listener) {
		Map<String,String> paramsMap=new HashMap<>();
		paramsMap.put("artworkId",artWorkId);
		paramsMap.put("messageId",artworkMessage.getId() + "");
		paramsMap.put("action", "1");
		paramsMap.put("timestamp", System.currentTimeMillis() + "");
		try {
			AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
			paramsMap.put("signmsg", AppApplication.signmsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		NetRequestUtil.post(Constants.BASE_PATH + "artworkPraise.do", paramsMap, new RZCommentCallBack() {
			@Override
			public void onError(Call call, Exception e) {
				e.printStackTrace();
				System.out.println("失败了");
			}

			@Override
			public void onResponse(Map<String, Object> response) {
				if ("0".equals(response.get("resultCode"))) {
					Map<String, Object> artworkMessageList = (Map<String, Object>) response.get("artworkMessage");
					List<ArtWorkPraise> praiseList =  AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(artworkMessageList.get("artWorkPraiseList")), new TypeToken<List<ArtWorkPraise>>() {
					}.getType());
					listener.loadSuccess(praiseList);
				}

			}
		});

	}

	public void deleteFavort(ArtworkMessage artworkMessage, final IDataRequestListener listener) {
		requestDeleteFavort(artworkMessage, listener);
	}

	private void requestDeleteFavort(ArtworkMessage artworkMessage, final IDataRequestListener listener) {
		Map<String,String> paramsMap=new HashMap<>();
		paramsMap.put("artworkId",artWorkId);
		paramsMap.put("messageId",artworkMessage.getId() + "");
		paramsMap.put("action", "0");
		paramsMap.put("timestamp", System.currentTimeMillis() + "");
		try {
			AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
			paramsMap.put("signmsg", AppApplication.signmsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		NetRequestUtil.post(Constants.BASE_PATH + "artworkPraise.do", paramsMap, new RZCommentCallBack() {
			@Override
			public void onError(Call call, Exception e) {
				e.printStackTrace();
				System.out.println("失败了");
			}

			@Override
			public void onResponse(Map<String, Object> response) {
				if ("0".equals(response.get("resultCode"))) {
					Map<String, Object> artworkMessageList = (Map<String, Object>) response.get("artworkMessage");
					List<ArtWorkPraise> praiseList =  AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(artworkMessageList.get("artWorkPraiseList")), new TypeToken<List<ArtWorkPraise>>() {
					}.getType());
					listener.loadSuccess(praiseList);
				}

			}
		});
	}

	public void addComment(final String content, ArtworkMessage artworkMessage, final IDataRequestListener listener) {
		requestAddComment(content, artworkMessage,listener);
	}

	private void requestAddComment(final String content, ArtworkMessage artworkMessage, final IDataRequestListener listener) {
		Map<String,String> paramsMap=new HashMap<>();
		paramsMap.put("artworkId",artWorkId);
		paramsMap.put("currentUserId", AppApplication.gUser.getId());
		paramsMap.put("content", content);
		paramsMap.put("messageId", artworkMessage.getId() + "");
		paramsMap.put("timestamp", System.currentTimeMillis() + "");
		try {
			AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
			paramsMap.put("signmsg", AppApplication.signmsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		NetRequestUtil.post(Constants.BASE_PATH + "artworkComment.do", paramsMap, new RZCommentCallBack() {
			@Override
			public void onError(Call call, Exception e) {
				e.printStackTrace();
				System.out.println("失败了");
			}

			@Override
			public void onResponse(Map<String, Object> response) {
				if ("0".equals(response.get("resultCode"))) {

					ArtworkComment comment = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(response.get("artworkComment")), ArtworkComment.class);

					listener.loadSuccess(comment);
				}

			}
		});
	}

	public void addReplyComment(final String content, ArtworkMessage artworkMessage, final CommentConfig config, final IDataRequestListener listener) {
		requestAddReplyComment(content, artworkMessage, config, listener);
	}

	private void requestAddReplyComment(String content, ArtworkMessage artworkMessage, final CommentConfig config, final IDataRequestListener listener) {
		Map<String,String> paramsMap=new HashMap<>();
		paramsMap.put("artworkId",artWorkId);
		paramsMap.put("currentUserId", AppApplication.gUser.getId());
		paramsMap.put("content", content);
		paramsMap.put("messageId", artworkMessage.getId() + "");
		paramsMap.put("fatherCommentId", artworkMessage.getArtworkCommentList().get(config.commentPosition).getId() + "");
		paramsMap.put("timestamp", System.currentTimeMillis() + "");
		try {
			AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
			paramsMap.put("signmsg", AppApplication.signmsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		NetRequestUtil.post(Constants.BASE_PATH + "artworkComment.do", paramsMap, new RZCommentCallBack() {
			@Override
			public void onError(Call call, Exception e) {
				e.printStackTrace();
				System.out.println("失败了");
			}

			@Override
			public void onResponse(Map<String, Object> response) {
				if ("0".equals(response.get("resultCode"))) {

					ArtworkComment comment = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(response.get("artworkComment")), ArtworkComment.class);

					listener.loadSuccess(comment);
				}

			}
		});
	}

	/*public void deleteComment(final IDataRequestListener listener) {
		requestServer(listener);
	}
	*/
	/**
	 * 
	* @Title: requestServer 
	* @Description: 与后台交互, 因为demo是本地数据，不做处理
	* @param  listener    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private void requestServer(int pageNum, final IDataRequestListener listener) {
		/*new AsyncTask<Object, Integer, Object>(){
			@Override
			protected Object doInBackground(Object... params) {
				//和后台交互

				return null;
			}
			
			protected void onPostExecute(Object result) {
				listener.loadSuccess(result);
			};
		}.execute();*/
		Map<String,String> paramsMap=new HashMap<>();
		paramsMap.put("artWorkId",artWorkId);
		paramsMap.put("pageSize", Constants.pageSize+"");
		paramsMap.put("pageIndex", pageNum + "");
		paramsMap.put("timestamp", System.currentTimeMillis() + "");
		try {
			AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
			paramsMap.put("signmsg", AppApplication.signmsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		NetRequestUtil.post(Constants.BASE_PATH + "artWorkCreationView.do", paramsMap, new RZCommentCallBack() {
			@Override
			public void onError(Call call, Exception e) {
				e.printStackTrace();
				System.out.println("失败了");
			}

			@Override
			public void onResponse(Map<String, Object> response) {
				if ("0".equals(response.get("resultCode"))) {
					Log.i("++++++++++++++","+++++++");
					System.out.println(response);
					Map<String, Object> object = (Map<String, Object>) response.get("object");
							List<ArtworkMessage> circleDatas =  AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(object.get("artworkMessageList")), new TypeToken<List<ArtworkMessage>>() {
							}.getType());
					listener.loadSuccess(circleDatas);
				}

			}
		});
	}
	
}
