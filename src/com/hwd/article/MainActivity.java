package com.hwd.article;

import java.util.ArrayList;
import java.util.List;

import com.example.article.R;
import com.google.gson.Gson;
import com.hwd.article.bean.User;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

/**
 * 主界面
 */
public class MainActivity extends BaseActivity {

	private ViewPager viewpager;

	private List<View> list = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}

	private void initView() {
		setContentView(R.layout.activity_main);
		viewpager = (ViewPager) findViewById(R.id.viewpager);

		list = new ArrayList<View>();

		View view01 = createView(R.layout.main_tab_01);
		View view02 = null;
		View view03 = null;
		
		if(isLogin()){
			view02 = createView(R.layout.main_tab_02);
			view03 = createView(R.layout.main_tab_03);
			new MainUser(this, view02);
			new MainSetting(this, view03);
		}else{
			view02 = createView(R.layout.main_tab_02_not_login);
			view03 = createView(R.layout.main_tab_03_not_login);
			new MainUserNotLogin(this, view02);
			new MainSettingNotLogin(this, view03);
		}
		
		

		list.add(view01);
		list.add(view02);
		list.add(view03);

		new MainArticle(this, view01);

		viewpager.setAdapter(new MainViewPagerAdapter(list));
		viewpager.setOnPageChangeListener(new MainViewPagerPageChangeListener());

		findViewById(R.id.show1).setOnClickListener(new MyOnClickListener(0));
		findViewById(R.id.show2).setOnClickListener(new MyOnClickListener(1));
		findViewById(R.id.show3).setOnClickListener(new MyOnClickListener(2));

		findViewById(R.id.img_frd).setOnClickListener(new MyOnClickListener(0));
		findViewById(R.id.img_info).setOnClickListener(new MyOnClickListener(1));
		findViewById(R.id.img_settings).setOnClickListener(new MyOnClickListener(2));

		viewpager.setCurrentItem(0);

		viewpager.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
	}

	/**
	 * 主界面底部tab按钮的点击事件
	 */
	private class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			viewpager.setCurrentItem(index);
		}
	};

	private class MainViewPagerAdapter extends PagerAdapter {

		private List<View> list = null;

		public MainViewPagerAdapter(List<View> list) {
			this.list = list;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(list.get(position));
			return list.get(position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(list.get(position));
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

	}

	private class MainViewPagerPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int state) {
		}

		@Override
		public void onPageScrolled(int page, float positionOffset, int positionOffsetPixels) {
		}

		@Override
		public void onPageSelected(int page) {
			((ImageView) findViewById(R.id.img_frd)).setImageResource(R.drawable.tab_find_frd_normal);
			((ImageView) findViewById(R.id.img_info)).setImageResource(R.drawable.tab_address_normal);
			((ImageView) findViewById(R.id.img_settings)).setImageResource(R.drawable.tab_settings_normal);
			findViewById(R.id.show1).setBackgroundResource(R.drawable.bottom_tab_default);
			findViewById(R.id.show2).setBackgroundResource(R.drawable.bottom_tab_default);
			findViewById(R.id.show3).setBackgroundResource(R.drawable.bottom_tab_default);
			if (page == 0) {
				((ImageView) findViewById(R.id.img_frd)).setImageResource(R.drawable.tab_find_frd_pressed);
				findViewById(R.id.show1).setBackgroundResource(R.drawable.bottom_tab_click);
			}

			if (page == 1) {
				((ImageView) findViewById(R.id.img_info)).setImageResource(R.drawable.tab_address_pressed);
				findViewById(R.id.show2).setBackgroundResource(R.drawable.bottom_tab_click);
			}
			if (page == 2) {
				((ImageView) findViewById(R.id.img_settings)).setImageResource(R.drawable.tab_settings_pressed);
				findViewById(R.id.show3).setBackgroundResource(R.drawable.bottom_tab_click);
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	// 创建Handler对象，用来处理消息
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {// 处理消息
			super.handleMessage(msg);
			isExit = false;
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			toQuitTheApp();
		}
		return false;
	}

	private boolean isExit = false;

	// 封装ToQuitTheApp方法
	public void toQuitTheApp() {
		if (isExit) {
			// ACTION_MAIN with category CATEGORY_HOME 启动主屏幕
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			startActivity(intent);
			finish();
			System.exit(0);// 使虚拟机停止运行并退出程序

		} else {
			isExit = true;
			toast("再按一次退出");
			mHandler.sendEmptyMessageDelayed(0, 3000);// 3秒后发送消息
		}
	}

}
