package cn.mangot.wxcommon.controller;

import java.net.URLDecoder;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;

import cn.mangot.wxcommon.config.WxConfig;
import cn.mangot.wxcommon.constant.RedisKeyConstants;
import cn.mangot.wxcommon.entity.AppInfo;
import cn.mangot.wxcommon.log.CommonLog;
import cn.mangot.wxcommon.model.ComponentVerfiyModel;
import cn.mangot.wxcommon.model.ResultBean;
import cn.mangot.wxcommon.redis.RedisLogic;
import cn.mangot.wxcommon.service.AppInfoService;
import cn.mangot.wxcommon.service.impl.WeiXin3ServiceImpl;
import cn.mangot.wxcommon.util.HttpURLUtils;
import cn.mangot.wxcommon.util.RSA;
import cn.mangot.wxcommon.util.StringUtils;

/**
 * @author Hugh 用于第三方平台微信服务号相关接口服务
 *
 */
@RestController
@RequestMapping("/wx3")
@CrossOrigin
public class WeiXin3Controller {
	@Resource
	private AppInfoService appInfoService;

	@Resource
	private WeiXin3ServiceImpl weiXin3ServiceImpl;

	@Autowired
	private RedisLogic redisLogic;

	/**
	 * 初始化weixin js sdk 参数(第三方开发)
	 * 
	 * @return
	 */
	@RequestMapping("/config")
	public Object wxConfigThird(String url, String appid) {
		AppInfo appInfo = appInfoService.getAppInfoByAppId(appid);

		if (appInfo == null) {
			return new ResultBean(1, "无效的appid", null);
		}
		String signature = "";
		String jsapi_ticket = weiXin3ServiceImpl.getTicket(appInfo);

		if (jsapi_ticket==null) {
			return new ResultBean(1,"未能获取jsapi_ticket",null);
		}
		// 3、时间戳和随机字符串
		String noncestr = UUID.randomUUID().toString().replace("-", "").substring(0, 16);// 随机字符串
		String timestamp = String.valueOf(System.currentTimeMillis() / 1000);// 时间戳
		try {
			url = URLDecoder.decode(url, "utf-8");
		} catch (Exception e) {
		}

		String str = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + noncestr + "&timestamp=" + timestamp + "&url="
				+ url;
		signature = RSA.SHA1(str);
		JSONObject json = new JSONObject();
		json.put("appid", appInfo.getAppid());
		json.put("noncestr", noncestr);
		json.put("timestamp", timestamp);
		json.put("signature", signature);
		return new ResultBean(0,"",json);
	}

	/**
	 * 在第三方平台创建审核通过后， 微信服务器会向其“授权事件接收URL”每隔10分钟定时推送component_verify_ticket
	 * 
	 * @return
	 */
	@PostMapping("/callback")
	public String callback(HttpServletRequest request) {

		String timestamp = request.getParameter("timestamp");
		String signature = request.getParameter("signature");
		String nonce = request.getParameter("nonce");
		String encrypt_type = request.getParameter("encrypt_type");
		String msg_signature = request.getParameter("msg_signature");
		// TODO 验证合法性

		String fromXML = HttpURLUtils.getRequestData(request);
		try {
			WXBizMsgCrypt pc = new WXBizMsgCrypt(WxConfig.token, WxConfig.encodingAesKey, WxConfig.AppIdThired);
			String result2 = pc.decryptMsg(msg_signature, timestamp, nonce, fromXML);
			CommonLog.info("WxCallBackController.callback:" + result2);
			WeiXin3ServiceImpl.componentVerfiyModel = new ComponentVerfiyModel(result2);
			redisLogic.set(RedisKeyConstants.ComponentVerfiyModel, result2, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "success";
	}

	// 公众号授权后的回调
	@GetMapping("rurl")
	public Object authorization_code(String auth_code, String expires_in) {

		JSONObject r = weiXin3ServiceImpl.getApi_query_auth(WxConfig.AppIdThired, auth_code);
		// 根据appid，把获取的数据存起来
		JSONObject rr = r.getJSONObject("authorization_info");
		CommonLog.info("有公众号授权并回调:" + rr);
		String authorizer_appid = rr.getString("authorizer_appid");
		// 存入redis
		redisLogic.set(RedisKeyConstants.Authorization_info + authorizer_appid, rr.toString(), 0);
		return "success";
	}
	
	/**
	 *获取用户基本信息 
	 * @param inv
	 * @return
	 */
	@RequestMapping("auth")
	public Object getWxUserInfoThirdParty(String code,String appid) {
		if (StringUtils.isEmpty(code)) {
			return new ResultBean(1,"code为空",null);
		}

		String gettokenurl = "https://api.weixin.qq.com/sns/oauth2/component/access_token?appid=" + appid
				+ "&code=" + code + "&grant_type=authorization_code&component_appid=" + WxConfig.AppIdThired
				+ "&component_access_token=" + weiXin3ServiceImpl.getComponent_access_token();
		String access_token;
		String openid;

		try {
			String tokenString = HttpURLUtils.doGet(gettokenurl);
			JSONObject accessTokenJson = JSONObject.parseObject(tokenString);
			access_token = accessTokenJson.getString("access_token");
			openid = accessTokenJson.getString("openid");

			if (StringUtils.isEmpty(access_token) || StringUtils.isEmpty(openid)) {
				return new ResultBean(1,"未能获取accesstoken或opid",null);
			}
			
			JSONObject userinfoJson = weiXin3ServiceImpl.getUserInfoByOpenId(openid, access_token);
			return new ResultBean(0,"",userinfoJson);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultBean(1,e.getMessage(),null);
		}
	}
	
	/**
	 * 获取公众号给第三方开发授权用的相关信息
	 * 
	 * @return
	 */
	@RequestMapping("shouquan")
	public Object shouquan() {
		JSONObject j = new JSONObject();
		j.put("component_appid", WxConfig.AppIdThired);
		j.put("pre_auth_code", weiXin3ServiceImpl.getPreAuthCode());
		return j;
	}
}
