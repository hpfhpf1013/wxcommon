package cn.mangot.wxcommon.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import cn.mangot.wxcommon.entity.AppInfo;
import cn.mangot.wxcommon.log.CommonLog;
import cn.mangot.wxcommon.model.AccessTokenBean;
import cn.mangot.wxcommon.model.JsapiTicketBean;
import cn.mangot.wxcommon.service.WeiXinService;
import cn.mangot.wxcommon.util.HttpURLUtils;

@Service("weiXinService")
public class WeiXinServiceImpl implements WeiXinService {

	public static final String grant_type = "client_credential";// 获取access_token填写client_credential
	public static Map<String,AccessTokenBean> atMap = new HashMap<String,AccessTokenBean>();
	
	@Override
	public String getAccessToken(AppInfo appInfo) {
		AccessTokenBean bean = atMap.get(appInfo.getAppid());
		
		if(bean!=null && bean.isValid()) {
			CommonLog.debug("缓存命中token:" + bean.getAccess_token());
			return bean.getAccess_token();
		}

		// 这个url链接地址和参数皆不能变
		String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type="
				+ grant_type + "&appid=" + appInfo.getAppid() + "&secret=" + appInfo.getSecret();

		String message="";
		AccessTokenBean b =  new AccessTokenBean();
		try {
			message = HttpURLUtils.doGet(url);
			JSONObject json = JSONObject.parseObject(message);
			String access_token = json.getString("access_token");
			String expires_in = json.getString("expires_in");
			
			b.setAccess_token(access_token);
			b.setExpires_in(Long.parseLong(expires_in));
			atMap.put(appInfo.getAppid(), b);
			return b.getAccess_token();
		} catch (Exception e) {
			e.printStackTrace();
			CommonLog.error(message);
		}
		return null;
	}

	public static Map<String,JsapiTicketBean> ticketMap = new HashMap<String,JsapiTicketBean>();
	@Override
	public String getTicket(AppInfo appInfo) {
		JsapiTicketBean bean = ticketMap.get(appInfo.getAppid());
		
		if(bean!=null && bean.isValid()) {
			CommonLog.debug("缓存命中ticket:" + bean.getTicket());
			return bean.getTicket();
		}
		
		String access_token = this.getAccessToken(appInfo);
		if(access_token==null) {
			return null;
		}
		String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="
				+ access_token + "&type=jsapi";
		
		String message = "";
		try {
			bean = new JsapiTicketBean();
			message = HttpURLUtils.doGet(url);
			JSONObject json = JSONObject.parseObject(message);
			CommonLog.debug(message);
			bean.setTicket(json.getString("ticket"));
			bean.setExpires_in(json.getLongValue("expires_in"));
			ticketMap.put(appInfo.getAppid(), bean);
			return bean.getTicket();
		} catch (Exception e) {
			e.printStackTrace();
			CommonLog.error("获取ticket错误:"+message);
		}
		return null;
	}

	private static Map<String,JSONObject> userInfoMap = new HashMap<String,JSONObject>();
	//带缓存的从微信获取用户信息
	public JSONObject getUserInfoByOpenId(String openid,String accessToken) {
		JSONObject userinfoJson  = userInfoMap.get(openid);
		if(userinfoJson==null) {
			String userinfo = HttpURLUtils.doGet("https://api.weixin.qq.com/sns/userinfo?access_token=" + accessToken
					+ "&openid=" + openid + "&lang=zh_CN");
			userinfoJson = JSONObject.parseObject(userinfo);
			userInfoMap.put(openid, userinfoJson);
		}
		return userinfoJson;
	}
}
