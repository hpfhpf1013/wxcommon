package cn.mangot.wxcommon.model;

import org.dom4j.DocumentHelper;

/*
 * 微信回调http://bjjy.quanquanddz.com/bjjyserver 后取得的数据
 */
public class ComponentVerfiyModel {
	private String appId;
	private String createTime;
	private String infoType;
	private String componentVerifyTicket;
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getInfoType() {
		return infoType;
	}
	public void setInfoType(String infoType) {
		this.infoType = infoType;
	}
	public String getComponentVerifyTicket() {
		return componentVerifyTicket;
	}
	public void setComponentVerifyTicket(String componentVerifyTicket) {
		this.componentVerifyTicket = componentVerifyTicket;
	}
	
	public ComponentVerfiyModel(String xml) throws Exception {
			org.dom4j.Document doc = DocumentHelper.parseText(xml);
			org.dom4j.Element rootElt = doc.getRootElement(); // 获取根节点
	        setAppId(rootElt.element("AppId").getText());
	        setCreateTime(rootElt.element("CreateTime").getText());
	        setInfoType(rootElt.element("InfoType").getText());
	        setComponentVerifyTicket(rootElt.element("ComponentVerifyTicket").getText());
	}
}
