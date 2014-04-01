package com.hwd.article.bean;

import com.google.gson.Gson;

/**
 * 用户
 */
public class User {

	private String username;
	private String sex;
	private String birthday;
	private String tinyurl;
	private String headurl;
	private String mainurl;
	private String province;
	private String city;
	private String is_verified;
	private String media_uid;
	private String media_type;
	private String social_uid;
	
	public static final String QZONE = "qqdenglu";
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getTinyurl() {
		return tinyurl;
	}
	public void setTinyurl(String tinyurl) {
		this.tinyurl = tinyurl;
	}
	public String getHeadurl() {
		return headurl;
	}
	public void setHeadurl(String headurl) {
		this.headurl = headurl;
	}
	public String getMainurl() {
		return mainurl;
	}
	public void setMainurl(String mainurl) {
		this.mainurl = mainurl;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getIs_verified() {
		return is_verified;
	}
	public void setIs_verified(String is_verified) {
		this.is_verified = is_verified;
	}
	public String getMedia_uid() {
		return media_uid;
	}
	public void setMedia_uid(String media_uid) {
		this.media_uid = media_uid;
	}
	public String getMedia_type() {
		return media_type;
	}
	public void setMedia_type(String media_type) {
		this.media_type = media_type;
	}
	public String getSocial_uid() {
		return social_uid;
	}
	public void setSocial_uid(String social_uid) {
		this.social_uid = social_uid;
	}
	
	
	public static void main(String[] args) {
		String msg = "{\"username\":\"windvix\",\"sex\":\"1\",\"birthday\":\"0\",\"tinyurl\":\"http://qzapp.qlogo.cn/qzapp/100358052/73C174D59B0684844FBDD79EB077AFAF/50\",\"headurl\":\"http://qzapp.qlogo.cn/qzapp/100358052/73C174D59B0684844FBDD79EB077AFAF/100\",\"mainurl\":\"\",\"hometown_location\":[],\"work_history\":[],\"university_history\":[],\"hs_history\":[],\"province\":\"\",\"city\":\"\",\"is_verified\":\"0\",\"media_uid\":\"73C174D59B0684844FBDD79EB077AFAF\",\"media_type\":\"qqdenglu\",\"social_uid\":4177829854}";
		User info = new Gson().fromJson(msg, User.class);
		System.out.println(info.getHeadurl());
	}
}
