package com.hwd.article;

import java.util.ArrayList;
import java.util.List;

import com.example.article.R;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnPullEventListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.State;
import com.hwd.article.adapter.ArticleListAdapter;
import com.hwd.article.bean.Article;
import com.hwd.article.task.GetNewArticleTask;
import com.hwd.article.task.GetNewArticleTaskHelper;
import com.hwd.article.util.StringUtil;

import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;


/**
 * 主界面,最新文章界面
 */
public class MainArticle implements OnRefreshListener<ListView>, GetNewArticleTaskHelper {

	private View root;

	private MainActivity act;

	private PullToRefreshListView listView;

	private ArticleListAdapter adapter;

	public MainArticle(MainActivity act, View root) {
		this.act = act;
		this.root = root;
		initView();
	}

	private void initView() {

		root.findViewById(R.id.title_bar_right_btn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listView.setRefreshing();
			}
		});
		listView = (PullToRefreshListView) root.findViewById(R.id.pull_refresh_list);
		listView.setOnRefreshListener(this);

		listView.setOnPullEventListener(new OnPullEventListener<ListView>() {
			@Override
			public void onPullEvent(PullToRefreshBase<ListView> refreshView, State state, Mode direction) {
				refreshView.findViewById(R.id.pull_to_refresh_sub_text).setVisibility(View.VISIBLE);
			}
		});

		// 加载上次的数据
		loadLastData();

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				listView.setRefreshing();
			}
		}, 2000);
	}

	private static final int PAGE_SIZE = 20;

	private int current_index = 0;

	private GetNewArticleTask task = null;

	private void refreshData() {
		// 位置还原到最顶。
		listView.getRefreshableView().setSelection(0);
		if (task != null) {
			task.cancel(true);
		}
		task = new GetNewArticleTask(this, 0, PAGE_SIZE, null);
		task.startTask();
		timeOut();
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

	private void loadLastData() {
		List<Article> list = new ArrayList<Article>();
		list.add(new Article());
		adapter = new ArticleListAdapter(getActivity(), list, PAGE_SIZE, null);
		listView.setAdapter(adapter);
		setRefreshTime(0);
	}

	private void setRefreshTime(long time) {
		if (listView != null) {
			TextView tv = (TextView) listView.findViewById(R.id.pull_to_refresh_sub_text);
			if (tv != null) {
				if (time <= 100) {
					tv.setText("上次刷新 未知");
				} else {
					tv.setText("上次刷新 " + StringUtil.getDateStr(time));
				}
			}

		}
	}

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		refreshData();
	}

	public MainActivity getActivity() {
		return act;
	}

	@Override
	public void getDataResult(String result) {
		listView.onRefreshComplete();
		if (!result.equals("")) {
			if (!result.startsWith("FAIL")) {
				List<String> list = null;
				try {
					list = new Gson().fromJson(result, ArrayList.class);
				} catch (Exception e) {
				}
				if (list == null) {
					list = new ArrayList<String>();
				}

				List<Article> artList = new ArrayList<Article>();
				for (String one : list) {
					Article oneArt = null;
					try {
						oneArt = new Gson().fromJson(one, Article.class);
					} catch (Exception e) {
					}
					if (oneArt != null) {
						artList.add(oneArt);
					}
				}

				artList.add(new Article());
				adapter = new ArticleListAdapter(getActivity(), artList, PAGE_SIZE, null);
				listView.setAdapter(adapter);
				setRefreshTime(System.currentTimeMillis());
				current_index = 0;
			}else{
				act.toast(result);
			}
		} else {
			act.toast("网络不给力，请稍后再试");
		}
	}
}
