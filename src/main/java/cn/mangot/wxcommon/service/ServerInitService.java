package cn.mangot.wxcommon.service;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import cn.mangot.wxcommon.log.CommonLog;
import cn.mangot.wxcommon.redis.RedisLogic;
import cn.mangot.wxcommon.service.impl.WeiXin3ServiceImpl;

@Component
public class ServerInitService  implements CommandLineRunner{
	@Resource
	private AppInfoService appInfoService;
	@Autowired
	private RedisLogic redisLogic;
	
	@Override
	public void run(String... args) throws Exception {
		WeiXin3ServiceImpl.componentVerfiyModel = redisLogic.getComponentVerfiyModel();//从redis里获取ticket
		CommonLog.info("服务器启动初始化成功...");
	}

}
