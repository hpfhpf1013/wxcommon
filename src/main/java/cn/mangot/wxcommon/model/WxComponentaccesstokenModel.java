package cn.mangot.wxcommon.model;

import com.alibaba.fastjson.JSONObject;

public class WxComponentaccesstokenModel {
	private String component_access_token;
	private long expires_in;
	private long createTime;
	public String getComponent_access_token() {
		return component_access_token;
	}
	public void setComponent_access_token(String component_access_token) {
		this.component_access_token = component_access_token;
	}
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
	
	public WxComponentaccesstokenModel(String json) {
		JSONObject j = JSONObject.parseObject(json);
		this.setComponent_access_token(j.getString("component_access_token"));
		long expires_in = j.getLong("expires_in")*1000-(5*60*1000);
		this.setExpires_in(expires_in);
		this.setCreateTime(System.currentTimeMillis());
	}
	
	
}
