package com.hwd.article.task;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.hwd.article.AddArticleActivity;
import com.hwd.article.MainArticle;
import com.hwd.article.bean.Article;

public class AddArticleTask extends BaseTask{

	private String user_id;
	
	private String user_name;
	
	private String topic;
	
	private String content;
	
	private String result = "";
	
	
	
	public AddArticleTask(AddArticleActivity act, String user_id, String user_name, String topic, String content) {
		super(act);
		this.topic = topic;
		this.content = content;
		this.user_id = user_id;
		this.user_name = user_name;
	}

	@Override
	protected void doInBackground() {
			try {
				
				URL url = new URL(SERVER+"3fe71f4c982509ecec3c33_add");

				String param = "key=" + KEY + "&ui=" + user_id + "&un=" + user_name+"&tp="+topic+"&ct="+content;

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
		((AddArticleActivity)getActivity()).sublitResult(result);
	}

}
