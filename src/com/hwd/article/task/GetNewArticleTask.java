package com.hwd.article.task;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.hwd.article.MainArticle;
import com.hwd.article.bean.Article;
import com.hwd.article.util.StringUtil;

public class GetNewArticleTask extends BaseTask{

	private int index;
	
	private int count;
	
	private GetNewArticleTaskHelper helper;
	
	private String result = "";
	
	private String user_id;
	
	
	public GetNewArticleTask(GetNewArticleTaskHelper helper, int index, int count, String user_id) {
		super(helper.getActivity());
		this.helper = helper;
		this.index = index;
		this.count = count;
		this.user_id = user_id;
	}

	@Override
	protected void doInBackground() {
			try {

				String urlStr = SERVER+"3fe71f4c982509ecec3c33_get";
				String param = "key=" + KEY + "&index=" + index + "&count=" + count;

				
				if(!StringUtil.isEmpty(user_id)){
					urlStr = SERVER+"3fe71f4c982509ecec3c33_user";
					param = param+"&ui="+user_id;
				}
				
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
		helper.getDataResult(result);
	}

}
