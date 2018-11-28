package cn.mangot.wxcommon.model;

import com.alibaba.fastjson.JSONObject;

public class WxAuthorizeraccesstokenModel {
	private String authorizer_access_token;
	private long expires_in;
	private long createTime;
	private String authorizer_refresh_token;
	
	public long getExpires_in() {
		return expires_in;
	}
	public void setExpires_in(long expires_in) {
		this.expires_in = expires_in;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	
	public WxAuthorizeraccesstokenModel(String json) {
		JSONObject j = JSONObject.parseObject(json);
		this.setAuthorizer_access_token(j.getString("authorizer_access_token"));
		long expires_in = j.getLong("expires_in")*1000-(5*60*1000);
		this.setExpires_in(expires_in);
		this.setCreateTime(System.currentTimeMillis());
		this.setAuthorizer_refresh_token(j.getString("authorizer_refresh_token"));
	}
	public String getAuthorizer_access_token() {
		return authorizer_access_token;
	}
	public void setAuthorizer_access_token(String authorizer_access_token) {
		this.authorizer_access_token = authorizer_access_token;
	}
	public String getAuthorizer_refresh_token() {
		return authorizer_refresh_token;
	}
	public void setAuthorizer_refresh_token(String authorizer_refresh_token) {
		this.authorizer_refresh_token = authorizer_refresh_token;
	}
	
	
}
