package cn.mangot.wxcommon.controller;

import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import cn.mangot.wxcommon.entity.AppInfo;
import cn.mangot.wxcommon.model.ResultBean;
import cn.mangot.wxcommon.service.AppInfoService;
import cn.mangot.wxcommon.service.impl.WeiXinServiceImpl;
import cn.mangot.wxcommon.util.HttpURLUtils;
import cn.mangot.wxcommon.util.RSA;
import cn.mangot.wxcommon.util.StringUtils;

/**
 * @author Hugh 用于自有微信服务号相关接口服务
 *
 */
@RestController
@RequestMapping("/wx")
public class WeiXinController {
	@Resource
	private AppInfoService appInfoService;

	@Resource
	private WeiXinServiceImpl weiXinServiceImpl;

	/**
	 * 初始化weixin js sdk 参数(自有公众号)
	 * 
	 * @param URL
	 * @return
	 */
	@CrossOrigin
	@RequestMapping("/config")
	public Object wxConfig(String url, String appid) {
		AppInfo appInfo = appInfoService.getAppInfoByAppId(appid);

		if (appInfo == null) {
			return new ResultBean(1, "无效的appid", null);
		}

		String signature = "";

		String jsapi_ticket = weiXinServiceImpl.getTicket(appInfo);
		if (jsapi_ticket == null) {
			return new ResultBean(1, "未能获取jsapi_ticket", null);
		}
		// 3、时间戳和随机字符串
		String noncestr = UUID.randomUUID().toString().replace("-", "").substring(0, 16);// 随机字符串
		String timestamp = String.valueOf(System.currentTimeMillis() / 1000);// 时间戳

		String str = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + noncestr + "&timestamp=" + timestamp + "&url="
				+ url;

		// 6、将字符串进行sha1加密
		signature = RSA.SHA1(str);
		JSONObject json = new JSONObject();
		json.put("appid", appid);
		json.put("noncestr", noncestr);
		json.put("timestamp", timestamp);
		json.put("signature", signature);

		return new ResultBean(0, "", json);
	}

	/**
	 * 通过CODE，获取用户信息 主要获取自己的公众账号的
	 * 
	 * @param inv
	 * @return
	 */
	@RequestMapping("auth")
	public Object getWxUserInfoSelf(String code, String appid) {

		if (StringUtils.isEmpty(code)) {
			return new ResultBean(1, "code为空", null);
		}

		AppInfo appInfo = appInfoService.getAppInfoByAppId(appid);

		if (appInfo == null) {
			return new ResultBean(1, "无效的appid", null);
		}

		// 这个url链接地址和参数皆不能变
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appInfo.getAppid() + "&secret="
				+ appInfo.getSecret() + "&code=" + code + "&grant_type=authorization_code";
		String access_token;
		String openid;

		try {
			String tokenString = HttpURLUtils.doGet(url);
			JSONObject accessTokenJson = JSONObject.parseObject(tokenString);
			access_token = accessTokenJson.getString("access_token");
			openid = accessTokenJson.getString("openid");

			if (StringUtils.isEmpty(access_token) || StringUtils.isEmpty(openid)) {
				return new ResultBean(1,"未能获取accesstoken或opid",tokenString);
			}
			
			JSONObject userinfoJson = weiXinServiceImpl.getUserInfoByOpenId(openid, access_token);
			return new ResultBean(0,"",userinfoJson);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultBean(1,e.getMessage(),null);
		}

	}
}
