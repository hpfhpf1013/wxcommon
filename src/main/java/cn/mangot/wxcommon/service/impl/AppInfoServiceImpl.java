package cn.mangot.wxcommon.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.mangot.wxcommon.IDao.AppInfoMapper;
import cn.mangot.wxcommon.entity.AppInfo;
import cn.mangot.wxcommon.service.AppInfoService;

@Service("appInfoService")
public class AppInfoServiceImpl implements AppInfoService {
	
	@Resource
	private AppInfoMapper appInfoMapper;

	@Override
	public AppInfo getAppInfoByAppId(String appid) {
		AppInfo appInfo = appInfoMap.get(appid);
		if(appInfo!=null) {
			return appInfo;
		}else {
			appInfo = appInfoMapper.selectByappId(appid);
			if(appInfo!=null) {
				appInfoMap.put(appid, appInfo);
			}
		}
		return appInfo;
	}

	@Override
	public boolean addAppInfo(AppInfo appInfo) {
		return appInfoMapper.insert(appInfo) ==1;
	}
	
	public static Map<String,AppInfo> appInfoMap = new HashMap<String,AppInfo>();

}
