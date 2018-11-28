package cn.mangot.wxcommon.service;

import cn.mangot.wxcommon.entity.AppInfo;

public interface WeiXinService {

	public String getAccessToken(AppInfo appInfo);
	public String getTicket(AppInfo appInfo);
	
}
