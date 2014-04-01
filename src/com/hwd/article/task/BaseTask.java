package com.hwd.article.task;

import com.hwd.article.BaseActivity;

import android.os.AsyncTask;

public abstract class BaseTask extends AsyncTask<Void, Void, Void> {

	private BaseActivity act;
	
	//服务器地址
	protected static final String SERVER = "http://192.168.88.5:8080/article1/";
	
	
	protected static final String KEY = "0e19aca262bbc46f5c84d49633a78a2a";
	protected static final String POST = "POST";
	protected static final String CODE = "UTF-8";

	public BaseTask(BaseActivity act) {
		this.act = act;
	}

	protected BaseActivity getActivity() {
		return act;
	}

	@Override
	protected Void doInBackground(Void... params) {
		if (!isCancelled()) {
			doInBackground();
		}
		return null;
	}

	/**
	 * 后台执行的任务
	 */
	protected abstract void doInBackground();

	@Override
	protected void onPostExecute(Void result) {
		isFinished = true;
		if (!isCancelled()) {
			onPostExecute();
		}
	}

	private boolean isFinished = false;

	public boolean isFinished() {
		if (isCancelled()) {
			return true;
		} else {
			return isFinished;
		}
	}

	protected abstract void onPostExecute();

	public void stopTask() {
		cancel(true);
	}

	public void startTask() {
		execute();
	}

	
}
