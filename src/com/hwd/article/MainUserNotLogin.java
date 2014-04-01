package com.hwd.article;

import com.example.article.R;

import android.view.View;
import android.view.View.OnClickListener;

/**
 * 未登录 的个人中心界面 
 */
public class MainUserNotLogin implements OnClickListener{

	private View root;

	private MainActivity act;

	public MainUserNotLogin(MainActivity act, View root) {
		this.act = act;
		this.root = root;
		initView();
	}
	
	private void initView(){
		root.findViewById(R.id.login_btn).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		act.showLogin();
	}
	
}
