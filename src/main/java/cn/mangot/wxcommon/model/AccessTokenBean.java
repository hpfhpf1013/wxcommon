package cn.mangot.wxcommon.model;

public class AccessTokenBean {
	private long expires_in;
	private String access_token;
	private long recordTime;
	public long getExpires_in() {
		return expires_in;
	}
	public void setExpires_in(long expires_in) {
		this.expires_in = expires_in;
	}
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
		recordTime = System.currentTimeMillis();
	}
	
	public boolean isValid() {
		return (System.currentTimeMillis() - recordTime) < (expires_in * 1000 - 30000);
	}
}
