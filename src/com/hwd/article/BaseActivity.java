package com.hwd.article;

import com.baidu.sharesdk.BaiduSocialShare;
import com.baidu.sharesdk.SocialShareLogger;
import com.baidu.sharesdk.Utility;
import com.google.gson.Gson;
import com.hwd.article.bean.User;
import com.hwd.article.util.SocialShareConfig;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public abstract class BaseActivity extends Activity implements OnClickListener {

	public void toast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	private SharedPreferences getPreference() {
		return getSharedPreferences(DATA_ROOT_KEY, 0);
	}

	
	public boolean isLogin() {
		boolean result = false;
		String userStr = getDataString(USER_KEY);
		if (!userStr.equals("")) {
			User user = new Gson().fromJson(userStr, User.class);
			if (user != null) {
				result = true;
			}
		}
		return result;
	}

	public void showLogin(){
		finish();
		Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
		startActivity(intent);
	}
	
	/**
	 * 以下是关于数据的读取也保存
	 */
	private static final String DATA_ROOT_KEY = "DATA_ROOT";
	public static final String USER_KEY = "USER_INFO_KEY";

	public boolean saveDataString(String key, String val) {
		return getPreference().edit().putString(key, val).commit();
	}

	/**
	 * 获取屏幕高度
	 */
	@SuppressWarnings("deprecation")
	public int getScreenHeight() {
		return getWindowManager().getDefaultDisplay().getHeight();
	}

	/**
	 * 获取屏幕宽度
	 */
	@SuppressWarnings("deprecation")
	public int getScreenWidth() {
		return getWindowManager().getDefaultDisplay().getWidth();
	}
	
	/**
	 * 获取inflater
	 */
	private LayoutInflater getInflater() {
		return (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	/**
	 * 根据资源ID，生成对应的view
	 */
	public View createView(int resourceId) {
		return getInflater().inflate(resourceId, null);
	}

	public String getDataString(String key) {
		return getPreference().getString(key, "");
	}

	private static BaiduSocialShare socialShare;

	public BaiduSocialShare getSocailShare() {
		if (socialShare == null) {
			socialShare = BaiduSocialShare.getInstance(this, SocialShareConfig.mbApiKey);
			SocialShareLogger.on();
		}
		return socialShare;
	}

	public void cleanAllData() {
		// 清除所有数据
		getPreference().edit().clear().commit();
		BaiduSocialShare share = getSocailShare();
		share.cleanAllAccessToken();
		share.cleanAccessToken(Utility.SHARE_TYPE_QZONE);
		socialShare = null;
	}

}
