package com.hwd.article;

import com.example.article.R;
import com.google.gson.Gson;
import com.hwd.article.bean.User;
import com.hwd.article.dialog.LoadingDialog;
import com.hwd.article.task.AddArticleTask;
import com.hwd.article.util.StringUtil;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

/**
 * 增加文章界面
 */
public class AddArticleActivity extends BaseActivity{

	/**
	 * 主题textview
	 */
	private TextView topicTv;
	
	/**
	 * 内容textview
	 */
	private TextView contentTv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_add_article);
		initView();
		
	}
	
	/**
	 * 初始化视图
	 */
	private void initView(){
		findViewById(R.id.nouse).requestFocus();
		findViewById(R.id.title_bar_right_btn).setOnClickListener(this);
		findViewById(R.id.title_bar_left_btn).setOnClickListener(this);
		topicTv= (TextView)findViewById(R.id.AddTopicTv);
		contentTv = (TextView)findViewById(R.id.AddContentTv);
	}
	
	
	@Override
	public void onClick(View v) {
		
		int id = v.getId();
		
		if(id==R.id.title_bar_right_btn){
			submit();
		}else{
			back();
		}
	}

	/**
	 * 返回
	 */
	private void back(){
		finish();
	}
	
	//等待框
	private LoadingDialog loading;
	private AddArticleTask task;
	
	/**
	 * 文章提交
	 */
	private void submit(){
		String topic = topicTv.getText().toString();
		String content = contentTv.getText().toString();
		if(task!=null){
			task.stopTask();
		}
		
		if(StringUtil.isEmpty(topic) || StringUtil.isEmpty(content)){
			toast("主题和内容不能为空");
		}else{
			if(topic.length()>50){
				toast("标题过长");
			}else{
				String userStr = getDataString(USER_KEY);
				User user = new Gson().fromJson(userStr, User.class);
				String user_id = user.getMedia_uid();
				String user_name = user.getUsername();
				
				task = new AddArticleTask(this, user_id, user_name, topic, content);
				task.startTask();
				loading = new LoadingDialog(this, "提示", "正在提交") {
					
					@Override
					public void beforeDismiss() {
						task.stopTask();
					}
				};
				loading.show();
			}
		}
	}
	
	//提交结果
	public void sublitResult(String result){
		loading.dismiss();
		
		if(StringUtil.isEmpty(result)){
			toast("网络不给力，请稍后再试");
		}else{
			if(result.startsWith("FAIL")){
				toast(result);
			}else{
				toast("提交成功");
				new Handler().postDelayed(new Runnable() {
					
					@Override
					public void run() {
						back();
					}
				},1000);
			}
		}
		
	}
	
}
