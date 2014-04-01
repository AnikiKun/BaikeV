package com.hwd.article.adapter;

import java.util.ArrayList;
import java.util.List;

import com.example.article.R;
import com.google.gson.Gson;
import com.hwd.article.ArticleDetailActivity;
import com.hwd.article.BaseActivity;
import com.hwd.article.MainActivity;
import com.hwd.article.bean.Article;
import com.hwd.article.task.GetNewArticleTask;
import com.hwd.article.task.GetNewArticleTaskHelper;
import com.hwd.article.util.StringUtil;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

/**
 * 文章列表处理项
 */
public class ArticleListAdapter extends ArrayAdapter<Article> implements GetNewArticleTaskHelper{

	private int resId;
	
	private List<Article> list;
	
	private int page_size = 0;
	
	private MainActivity act;
	
	private boolean isLoadingMore = false;
	
	private boolean hasMore = false;
	
	private int index = 0;
	
	private String user_id;
	
	public ArticleListAdapter(MainActivity act, List<Article> list, int page_size, String user_id) {
		super(act, R.layout.list_item_article, list);
		this.act = act;
		this.resId = R.layout.list_item_article;
		this.list = list;
		this.page_size = page_size;
		this.user_id = user_id;
		isLoadingMore = false;
		
		if (list.size() >= page_size) {
			hasMore = true;
		} else {
			hasMore = false;
		}
		if (hasMore) {
			index = page_size;
		}
		
		saveData();
	}
	
	
	private void saveData(){
		for(Article art:list){
			String str = new Gson().toJson(art);
			long id = art.getId();
			if(id>0){
				act.saveDataString(art.getId()+"", str);
			}
		}
	}

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Article art = getItem(position);
		int count = getCount();
		//如果list的长度只有1，且这个art为空，则说明没有数据
		if (count == 1){

			if((art).getUser_id()==null){
				convertView = getNoDataView();
			}
		}else{
			//最后一个
			if(position==(count-1)){
				if(convertView==null){
					convertView = act.createView(R.layout.list_end_no_more);
				}else{
					if(convertView.findViewWithTag("end")==null){
						convertView = act.createView(R.layout.list_end_no_more);
					}
				}
				initEndView(convertView);
			}else{
				if(convertView==null){
					convertView = act.createView(R.layout.list_item_article);
				}
				//如果不是item视图。则转为item视图
				else{
					if(convertView.findViewWithTag("mid")==null){
						convertView = act.createView(R.layout.list_item_article);
					}
				}
				initItemView(art, convertView);
			}
		}		
		
		return convertView;
	}
	

	private void initEndView(View view){
		TextView msg = (TextView) view.findViewById(R.id.loadmore_tv);
		if (!isLoadingMore) {
			if (hasMore) {
				msg.setText("加载更多");
				initNextPage(view.findViewById(R.id.refreshable_view_loadmore_layout));
				view.findViewById(R.id.loadmore_icon).setVisibility(View.GONE);
				view.findViewById(R.id.refreshable_view_loadmore_layout).setClickable(true);
			} else {
				msg.setText("没有更多了");
				view.findViewById(R.id.loadmore_icon).setVisibility(View.GONE);
				view.findViewById(R.id.refreshable_view_loadmore_layout).setClickable(false);
			}
		} else {
			msg.setText("正在加载");
			view.findViewById(R.id.refreshable_view_loadmore_layout).setClickable(false);
			view.findViewById(R.id.loadmore_icon).setVisibility(View.VISIBLE);
		}
	}
	
	private View loadView;
	
	private GetNewArticleTask task;
	
	private void initNextPage(final View loadView) {
		this.loadView = loadView;
		loadView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				v.setClickable(false);
				TextView msg = (TextView) loadView.findViewById(R.id.loadmore_tv);
				msg.setText("正在加载");
				isLoadingMore = true;
				loadView.findViewById(R.id.loadmore_icon).setVisibility(View.VISIBLE);
				if(task!=null){
					task.stopTask();
				}
				task = new GetNewArticleTask(ArticleListAdapter.this, index, page_size, user_id);
				task.startTask();
				timeOut();
			}
		});
	}
	
	
	private void timeOut() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if (task != null) {
					if (!task.isFinished()) {
						task.cancel(true);
						getDataResult("");
					}
				}
			}
		}, 45000);
	}
	
	
	private void initItemView(final Article art, View view){
		TextView topic = (TextView)view.findViewById(R.id.list_item_article_topic_tv);
		TextView time = (TextView)view.findViewById(R.id.list_item_article_time_tv);
		TextView name = (TextView)view.findViewById(R.id.list_item_article_name_tv);
		
		
		topic.setText(art.getTopic());
		time.setText(StringUtil.getDateStr(art.getTime()));
		name.setText(art.getUsername());
		
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent =new Intent(act, ArticleDetailActivity.class);
				intent.putExtra("id", art.getId()+"");
				act.startActivity(intent);
			}
		});
		
	}
	
	
	private View getNoDataView() {
		View view = act.createView(R.layout.list_no_data);
		LayoutParams params = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		int height = act.getScreenHeight() - act.getResources().getDimensionPixelSize(R.dimen.TITLE_BAR_HEIGHT) * 2  - act.getResources().getDimensionPixelSize(R.dimen.TITLE_BAR_HEIGHT);
		params.height = height;

		view.findViewById(R.id.no_data_root_layout).setLayoutParams(params);
		return view;
	}


	@Override
	public MainActivity getActivity() {
		
		return act;
	}


	@Override
	public void getDataResult(String result) {
		isLoadingMore = false;
		if(StringUtil.isEmpty(result)|| result.startsWith("FAIL")){
			getActivity().toast("网络不给力，请稍后再试");
			if (loadView != null && loadView.findViewById(R.id.loadmore_tv) != null) {
				TextView msg = (TextView) loadView.findViewById(R.id.loadmore_tv);
				msg.setText("加载更多");
				loadView.findViewById(R.id.loadmore_icon).setVisibility(View.GONE);
				loadView.findViewById(R.id.refreshable_view_loadmore_layout).setClickable(true);
			}
		}else{
			
			List<String> strList = null;
			try {
				strList = new Gson().fromJson(result, ArrayList.class);
			} catch (Exception e) {
			}
			if (strList == null) {
				strList = new ArrayList<String>();
			}

			List<Article> artList = new ArrayList<Article>();
			for (String one : strList) {
				Article oneArt = null;
				try {
					oneArt = new Gson().fromJson(one, Article.class);
				} catch (Exception e) {
				}
				if (oneArt != null) {
					artList.add(oneArt);
				}
			}
			
			if(artList.size()>=page_size){
				hasMore = true;
			}
			

			TextView msg = (TextView) loadView.findViewById(R.id.loadmore_tv);

			if (hasMore) {
				msg.setText("加载更多");
				loadView.findViewById(R.id.loadmore_icon).setVisibility(View.GONE);
				loadView.findViewById(R.id.refreshable_view_loadmore_layout).setClickable(true);
			} else {
				msg.setText("没有更多了");
				loadView.findViewById(R.id.loadmore_icon).setVisibility(View.GONE);
				loadView.findViewById(R.id.refreshable_view_loadmore_layout).setClickable(false);
			}
			
			// 删除adapter最后的空post
			this.remove(getItem(getCount() - 1));
			for(Article art:artList){
				this.add(art);
			}
			index += page_size;
			// 告诉数据已变化
			this.notifyDataSetChanged();
		}
	}

}
