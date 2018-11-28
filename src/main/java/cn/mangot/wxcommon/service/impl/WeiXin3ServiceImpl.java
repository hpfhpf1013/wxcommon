package cn.mangot.wxcommon.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import cn.mangot.wxcommon.config.WxConfig;
import cn.mangot.wxcommon.entity.AppInfo;
import cn.mangot.wxcommon.log.CommonLog;
import cn.mangot.wxcommon.model.ComponentVerfiyModel;
import cn.mangot.wxcommon.model.JsapiTicketBean;
import cn.mangot.wxcommon.model.WxAuthorizeraccesstokenModel;
import cn.mangot.wxcommon.model.WxComponentaccesstokenModel;
import cn.mangot.wxcommon.model.thirdAuthInfo.AuthorizationInfo;
import cn.mangot.wxcommon.redis.RedisLogic;
import cn.mangot.wxcommon.service.WeiXin3Service;
import cn.mangot.wxcommon.util.HttpURLUtils;
import cn.mangot.wxcommon.util.StringUtils;

@Service
public class WeiXin3ServiceImpl implements WeiXin3Service {
	
	public static ComponentVerfiyModel componentVerfiyModel = null;// 里面有component_verify_ticket
	public static WxComponentaccesstokenModel wxComponentaccesstokenModel;// 里面有component_access_token
	public static AuthorizationInfo authorizationInfo;//第三方授权的公众号信息
	@Autowired
	private RedisLogic redisLogic;
	/*
	 * 第三方平台component_access_token
	 * 是第三方平台的下文中接口的调用凭据，也叫做令牌
	 */
	public String getComponent_access_token() {
		if (wxComponentaccesstokenModel != null) {
			long curTime = System.currentTimeMillis();
			if (curTime - wxComponentaccesstokenModel.getCreateTime() < wxComponentaccesstokenModel.getExpires_in()) {
				// 在有效期内
				return wxComponentaccesstokenModel.getComponent_access_token();
			}
		}
		if (componentVerfiyModel == null) {
			CommonLog.error("WeiXin3Service.getComponent_access_token() error: componentVerfiyModel is null"); 
			return null;
		}
		String url = "https://api.weixin.qq.com/cgi-bin/component/api_component_token";
		JSONObject json = new JSONObject();
		json.put("component_appid", WxConfig.AppIdThired);
		json.put("component_appsecret", WxConfig.secretThired);
		json.put("component_verify_ticket", componentVerfiyModel.getComponentVerifyTicket());
		String result = HttpURLUtils.post(url, json.toString());
		try {
			if (!StringUtils.isEmpty(result)) {
				wxComponentaccesstokenModel = new WxComponentaccesstokenModel(result);
				return wxComponentaccesstokenModel.getComponent_access_token();
			} else {
				return null;
			}
		} catch (Exception e) {
			CommonLog.error(result);
			return null;
		}
		
	}
	
	public static Map<String,WxAuthorizeraccesstokenModel> api_authorizer_tokenMap = new HashMap<String,WxAuthorizeraccesstokenModel>();
	/*
	 * 获取（刷新）授权公众号或小程序的接口调用凭据（令牌）
	 */
	public String getApi_authorizer_token(String appid) {
		WxAuthorizeraccesstokenModel authorizeraccesstokenModel = api_authorizer_tokenMap.get(appid);
		if ( authorizeraccesstokenModel!= null) {
			long curTime = System.currentTimeMillis();
			if (curTime - authorizeraccesstokenModel.getCreateTime() < authorizeraccesstokenModel.getExpires_in()) {
				// 在有效期内
				return authorizeraccesstokenModel.getAuthorizer_access_token();
			}
		}
		if(authorizationInfo==null) {
			authorizationInfo = redisLogic.getAuthorizationInfo(appid);
		}
		
		String url = "https://api.weixin.qq.com/cgi-bin/component/api_authorizer_token?component_access_token="+getComponent_access_token();
		JSONObject json = new JSONObject();
		json.put("component_appid", WxConfig.AppIdThired);
		json.put("authorizer_appid", appid);
		json.put("authorizer_refresh_token",authorizationInfo.getAuthorizer_refresh_token() );
		String result = HttpURLUtils.post(url, json.toString());
		CommonLog.debug("WeixinLogicManager.getApi_authorizer_token:"+result);
		try {
			if (!StringUtils.isEmpty(result)) {
				authorizeraccesstokenModel = new WxAuthorizeraccesstokenModel(result);
				api_authorizer_tokenMap.put(appid, authorizeraccesstokenModel);
				return authorizeraccesstokenModel.getAuthorizer_access_token();
			} else {
				return null;
			}
		} catch (Exception e) {
			CommonLog.error("获取api_authorizer_token失败："+result);
			e.printStackTrace();
			return null;
		}
		
	}
	
	/*
	 * 获得授权用的preauthcode
	 */
	public String getPreAuthCode() {
		String accesstonken = getComponent_access_token();
		String url1 = "https://api.weixin.qq.com/cgi-bin/component/api_create_preauthcode?component_access_token="
				+ accesstonken;
		JSONObject json1 = new JSONObject();
		json1.put("component_appid", WxConfig.AppIdThired);
		JSONObject result1 = JSONObject.parseObject(HttpURLUtils.post(url1, json1.toString()));
		String re = result1.getString("pre_auth_code");
		if(StringUtils.isEmpty(re)) {
			CommonLog.error("weixin3service.getPreAuthCode error:"+result1.toJSONString());
		}
		return re;
	}
	
	/*
	 * 获得授权用的preauthcode
	 */
	public JSONObject getApi_query_auth(String component_appid,String authorization_code) {
		String accesstonken = getComponent_access_token();
		String url1 = "https://api.weixin.qq.com/cgi-bin/component/api_query_auth?component_access_token="
				+ accesstonken;
		JSONObject json1 = new JSONObject();
		json1.put("component_appid", component_appid);
		json1.put("authorization_code", authorization_code);
		JSONObject result = JSONObject.parseObject(HttpURLUtils.post(url1, json1.toString()));
		return result;
	}
	
	public static Map<String,JsapiTicketBean> ticketMap = new HashMap<String,JsapiTicketBean>();

	public String getTicket(AppInfo appInfo) {
		JsapiTicketBean bean = ticketMap.get(appInfo.getAppid());
		
		if(bean!=null && bean.isValid()) {
			CommonLog.debug("缓存命中ticket:" + bean.getTicket());
			return bean.getTicket();
		}
		
		String access_token = this.getApi_authorizer_token(appInfo.getAppid());
		if(access_token==null) {
			CommonLog.error("WeiXin3ServiceImpl.getTicket() return accessToken为null,appId:"+appInfo.getAppid());
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
			CommonLog.error("WeiXin3ServiceImpl.getTicket()获取ticket错误:"+message);
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
