package com.hwd.article;

import com.example.article.R;
import com.hwd.article.dialog.ConfirmDialog;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;


/**
 * 主界面，更多界面
 */
public class MainSetting implements OnClickListener{

	private View root;

	private MainActivity act;

	public MainSetting(MainActivity act, View root) {
		this.act = act;
		this.root = root;
		
		initView();
	}
	
	private void initView(){
		root.findViewById(R.id.logout_btn).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		final ConfirmDialog confirm = new ConfirmDialog(act, "确定", "取消", "确定注销登录吗？", "提示");
		confirm.getNegativeBtn().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				confirm.dismiss();
			}
		});
		confirm.getPositiveBtn().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				confirm.dismiss();
				exit();
			}
		});
		confirm.show();
	}
	
	//退出
	private void exit(){
		act.cleanAllData();
		act.finish();
		Intent intent = new Intent(act, MainActivity.class);
		act.startActivity(intent);
	}
}
