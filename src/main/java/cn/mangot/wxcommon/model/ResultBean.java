package cn.mangot.wxcommon.model;

public class ResultBean {

	private int errorCode;
	private String errorMsg;
	private Object result;
	public int getErrorCode() {
		return errorCode;
	}
	
	public ResultBean(int code,String msg,Object json) {
		setErrorCode(code);
		setErrorMsg(msg);
		setResult(json);
	}
	
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}
	
}
