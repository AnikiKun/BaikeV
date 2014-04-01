package com.hwd.article;

import com.example.article.R;
import com.google.gson.Gson;
import com.hwd.article.bean.Article;
import com.hwd.article.bean.User;
import com.hwd.article.dialog.ConfirmDialog;
import com.hwd.article.dialog.LoadingDialog;
import com.hwd.article.task.AddArticleTask;
import com.hwd.article.task.DeleteArticleTask;
import com.hwd.article.task.FavArticleTask;
import com.hwd.article.task.HatArticleTask;
import com.hwd.article.util.StringUtil;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;


/**
 * 文章详细界面
 */
public class ArticleDetailActivity extends BaseActivity{
	//标题
	private TextView topicTv;
	//内容
	private TextView contentTv;
	//赞成
	private TextView favTv;
	//反对
	private TextView hatTv;
	//文章
	private Article art;
	//用户名
	private String user_id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		String id = getIntent().getStringExtra("id");
		
		String artStr = "";
		
		if(!StringUtil.isEmpty(id)){
			artStr = getDataString(id);
		}
		
		
		String userStr = getDataString(USER_KEY);
		User user = new Gson().fromJson(userStr, User.class);
		
		if(user==null){
			user = new User();
			user_id = "";
		}else{
			user_id = user.getMedia_uid();
			
		}
		
		art = new Gson().fromJson(artStr, Article.class);
		setContentView(R.layout.activity_article_detail);
		initView();
		
	}
	
	//初始化视图
	private void initView(){
		findViewById(R.id.title_bar_right_btn).setOnClickListener(this);
		findViewById(R.id.title_bar_left_btn).setOnClickListener(this);
		topicTv= (TextView)findViewById(R.id.detailTopicTv);
		contentTv = (TextView)findViewById(R.id.detailContentTv);
		favTv = (TextView)findViewById(R.id.detailFavTv);
		hatTv = (TextView)findViewById(R.id.detailHatTv);
		
		
		topicTv.setText(art.getTopic());
		contentTv.setText("		"+art.getContent());
		favTv.setText(art.getFav_count()+"");
		hatTv.setText(art.getHat_count()+"");
		
		
		findViewById(R.id.favBtn).setOnClickListener(this);
		findViewById(R.id.hatBtn).setOnClickListener(this);
		
		
		boolean isLogin = isLogin();
		
		//如果已经登录，则可以进行赞成和反对
		if(isLogin){
			String fav = getDataString("Fav"+art.getId());
			if(!StringUtil.isEmpty(fav)){
				findViewById(R.id.favBtn).setVisibility(View.GONE);
			}
			
			String hat = getDataString("Hat"+art.getId());
			if(!StringUtil.isEmpty(hat)){
				findViewById(R.id.hatBtn).setVisibility(View.GONE);
			}
		}else{
			findViewById(R.id.favBtn).setVisibility(View.GONE);
			findViewById(R.id.hatBtn).setVisibility(View.GONE);
		}
		
		TextView btn = (TextView)findViewById(R.id.title_bar_right_btn);
		
		//自己的文章才能删除
		if(user_id.equals(art.getUser_id())){
			btn.setText("删除");
			findViewById(R.id.favBtn).setVisibility(View.GONE);
			findViewById(R.id.hatBtn).setVisibility(View.GONE);
		}else{
			findViewById(R.id.title_bar_right_layout).setVisibility(View.INVISIBLE);
			btn.setOnClickListener(null);
		}
	}
	
	
	@Override
	public void onClick(View v) {
		
		int id = v.getId();
		boolean isLogin = isLogin();
		//点击删除按钮
		if(id==R.id.title_bar_right_btn){
			submit();
		}
		//点击返回按钮
		else if(id==R.id.title_bar_left_btn){
			back();
		}
		//点击赞成按钮
		else if(id==R.id.favBtn){
			if(isLogin){
				String fav = getDataString("Fav"+art.getId());
				if(StringUtil.isEmpty(fav)){
					new FavArticleTask(this, art.getId()).startTask();
					toast("赞成 +1");
					saveDataString("Fav"+art.getId(), "OK");
					initView();
					findViewById(R.id.hatBtn).setVisibility(View.GONE);
				}
			}
		}
		//点击反对按钮
		else if(id==R.id.hatBtn){
			if(isLogin){
				String hat = getDataString("Hat"+art.getId());
				if(StringUtil.isEmpty(hat)){
					new HatArticleTask(this, art.getId()).startTask();
					toast("反对 +1");
					saveDataString("Hat"+art.getId(), "OK");
					initView();
					findViewById(R.id.favBtn).setVisibility(View.GONE);
				}
			}
		}
	}

	//返回
	private void back(){
		finish();
	}
	
	//删除文章
	private void submit(){
		if(user_id.equals(art.getUser_id())){
			final ConfirmDialog confirm = new ConfirmDialog(this, "确定", "取消", "确定删除此文章吗？", "提示");
			confirm.getPositiveBtn().setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					confirm.dismiss();
					new DeleteArticleTask(ArticleDetailActivity.this, art.getId()).startTask();
					toast("删除成功");
					back();
				}
			});
			
			confirm.getNegativeBtn().setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					confirm.dismiss();
				}
			});
			
			confirm.show();
		}
	}
	
	
	
	
}
