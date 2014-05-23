package com.hwd.article.task;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.hwd.article.ArticleDetailActivity;
import com.hwd.article.MainArticle;
import com.hwd.article.bean.Article;
import com.hwd.article.util.StringUtil;

public class HatArticleTask extends BaseTask{

	private long id;
	
	private String result = "";
	
	
	public HatArticleTask(ArticleDetailActivity act, long id) {
		super(act);
		this.id = id;
	}

	@Override
	protected void doInBackground() {
			try {

				String urlStr = SERVER+"3fe71f4c982509ecec3c33_hat";
				String param = "key=" + KEY + "&id=" + id;
				URL url = new URL(urlStr);

				
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestProperty("contentType", CODE); 
				conn.setRequestProperty("Accept-Charset", CODE);
				conn.setConnectTimeout(5 * 1000);
				conn.setRequestMethod(POST);
				conn.setDoOutput(true);
				OutputStream os = conn.getOutputStream();
				os.write(param.getBytes(CODE));
				result = "";
				if (conn.getResponseCode() == 200) {
					InputStreamReader isr = new InputStreamReader(conn.getInputStream());
					BufferedReader br = new BufferedReader(isr);
					String temp = null;
					while ((temp = br.readLine()) != null) {
						result = result + temp;
					}
					isr.close();
				} else {
					result = "FAIL: " + conn.getResponseCode();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	@Override
	protected void onPostExecute() {
		
	}

}
