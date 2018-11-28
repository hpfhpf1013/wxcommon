package cn.mangot.wxcommon.service;

import cn.mangot.wxcommon.entity.AppInfo;

public interface AppInfoService {

	public AppInfo getAppInfoByAppId(String appid);
	
	boolean addAppInfo (AppInfo appInfo);
}
