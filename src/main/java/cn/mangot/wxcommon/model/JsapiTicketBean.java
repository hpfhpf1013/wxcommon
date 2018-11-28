package cn.mangot.wxcommon.model;

public class JsapiTicketBean {
	private long expires_in;
	private String ticket;
	private long recordTime;
	public long getExpires_in() {
		return expires_in;
	}
	public void setExpires_in(long expires_in) {
		this.expires_in = expires_in;
	}
	public boolean isValid() {
		return (System.currentTimeMillis() - recordTime) < (expires_in * 1000 - 30000);
	}
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
		recordTime = System.currentTimeMillis();
	}
}
