package com.hwd.article;

import com.baidu.sharesdk.BaiduShareException;
import com.baidu.sharesdk.BaiduSocialShare;
import com.baidu.sharesdk.ShareListener;
import com.baidu.sharesdk.SocialShareLogger;
import com.baidu.sharesdk.Utility;
import com.example.article.R;
import com.google.gson.Gson;
import com.hwd.article.bean.User;
import com.hwd.article.task.RegisterTask;
import com.hwd.article.util.SocialShareConfig;
import com.hwd.article.util.StringUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

public class LoginActivity extends BaseActivity{

	
	private boolean isFrom = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		String isOk = getDataString("FROM");
		
		
		if(!StringUtil.isEmpty(isOk)){
			isFrom = true;
		}
		
		initView();
		
	}
	
	
	private void initView(){
		setContentView(R.layout.activity_login);
		findViewById(R.id.qzone_login_btn).setOnClickListener(this);
		findViewById(R.id.renren_login_btn).setOnClickListener(this);
		findViewById(R.id.weibo_login_btn).setOnClickListener(this);
		findViewById(R.id.title_bar_left_btn).setOnClickListener(this);
	}
	

	private String type = Utility.SHARE_TYPE_QZONE;
	

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if(id==R.id.qzone_login_btn){
			type = Utility.SHARE_TYPE_QZONE;
		}else if(id==R.id.renren_login_btn){
			type = Utility.SHARE_TYPE_RENREN;
		}else if(id==R.id.weibo_login_btn){
			type = Utility.SHARE_TYPE_SINA_WEIBO;
		}else if(id==R.id.title_bar_left_btn){
			startMain();;
		}
		
		login();
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if(keyCode==KeyEvent.KEYCODE_BACK){
			startMain();
		}
		
		return false;
	}
	
	
	private void startMain(){
		finish();
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		startActivity(intent);
	}
	
	
	private void login(){
		final BaiduSocialShare share = getSocailShare();
		if (share != null) {
			share.cleanAllAccessToken();
			share.cleanAccessToken(type);
		}
		
		
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if (share != null) {
					if(share.isAccessTokenValid(type)){
						share.getUserInfoWithShareType(getApplicationContext(), Utility.SHARE_TYPE_QZONE, new UserInfoListener());
					}else{
						share.authorize(LoginActivity.this, type, new UserInfoListener());
					}
				} else {
					toast("无法初始化分享组件");
				}
			}
		}, 150);
	}
	
	
	private void initUserInfo(String msg){
		if(msg!=null && !msg.equals("")){
			User user = new Gson().fromJson(msg, User.class);
			if(user!=null){
				saveDataString(USER_KEY, msg);
				new RegisterTask(LoginActivity.this, user.getMedia_uid(), user.getUsername()).startTask();
				toast("登录成功");
				startMain();
			}else{
				toast("登录失败");
			}
		}else{
			toast("登录失败");
		}
	}
	
	private class UserInfoListener implements ShareListener {

		final Handler handler = new Handler(Looper.getMainLooper());

		@Override
		public void onAuthComplete(Bundle values) {
			getSocailShare().getUserInfoWithShareType(LoginActivity.this, type, new UserInfoListener());
		}

		@Override
		public void onApiComplete(String responses) {

			final String responseStr = responses;
			handler.post(new Runnable() {
				@Override
				public void run() {
					String msg = StringUtil.decodeUnicode(responseStr);
					initUserInfo(msg);
				}
			});
		}

		@Override
		public void onError(BaiduShareException e) {
			final String error = e.toString();
			handler.post(new Runnable() {
				@Override
				public void run() {
					toast(error);
				}
			});
		}

	}
	
}
