package cn.mangot.wxcommon.redis;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

import cn.mangot.wxcommon.constant.RedisKeyConstants;
import cn.mangot.wxcommon.model.ComponentVerfiyModel;
import cn.mangot.wxcommon.model.thirdAuthInfo.AuthorizationInfo;

@Component
public class RedisLogic extends RedisUtil{
	
	public ComponentVerfiyModel getComponentVerfiyModel() {
		String s = get(RedisKeyConstants.ComponentVerfiyModel,0);
		try {
			return new ComponentVerfiyModel(s);
		} catch (Exception e) {
			return null;
		}
	}
	
	public AuthorizationInfo getAuthorizationInfo(String appId) {
		String s = get(RedisKeyConstants.Authorization_info+appId,0);
		try {
			AuthorizationInfo a= JSONObject.toJavaObject(JSONObject.parseObject(s),AuthorizationInfo.class);
			return a;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
}