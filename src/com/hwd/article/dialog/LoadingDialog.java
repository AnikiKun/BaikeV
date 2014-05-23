package com.hwd.article.dialog;


import com.example.article.R;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 等待框
 */
public abstract class LoadingDialog extends Dialog{

	private String title;
	private String msg;
	
	public LoadingDialog(Context context, String title, String msg) {
		super(context, R.style.LightDialog);
		this.title = title;
		this.msg = msg;
		setCancelable(true);
		setCanceledOnTouchOutside(true);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_loading);
		setOnShowListener(new OnShowListener() {

			@Override
			public void onShow(DialogInterface dialog) {
				ImageView image = (ImageView) LoadingDialog.this.findViewById(R.id.loading_img);
				Animation anim = new RotateAnimation(360, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
				TextView titleTv = (TextView)findViewById(R.id.loading_dialog_title);
				TextView msgTv = (TextView)findViewById(R.id.loading_dialog_msg);
				titleTv.setText(title);
				msgTv.setText(msg);
				anim.setRepeatCount(Animation.INFINITE); // 设置INFINITE，对应值-1，代表重复次数为无穷次
				anim.setDuration(500); // 设置该动画的持续时间，毫秒单位
				anim.setInterpolator(new LinearInterpolator()); // 设置一个插入器，或叫补间器，用于完成从动画的一个起始到结束中间的补间部分
				image.startAnimation(anim);
			}
		});
	}

	
	@Override
	public void dismiss() {
		beforeDismiss();
		super.dismiss();
	}
	
	public abstract void beforeDismiss();
}
