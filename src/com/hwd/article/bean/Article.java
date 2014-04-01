package com.hwd.article.bean;


/**
 * 文章
 */
public class Article {

	private long id;
	
	private String user_id;
	
	private String username;
	
	private long time;
	
	private String topic;
	
	private String content;
	
	private long fav_count;
	
	private long hat_count;

	public long getHat_count() {
		return hat_count;
	}

	public void setHat_count(long hat_count) {
		this.hat_count = hat_count;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getFav_count() {
		return fav_count;
	}

	public void setFav_count(long fav_count) {
		this.fav_count = fav_count;
	}
}
