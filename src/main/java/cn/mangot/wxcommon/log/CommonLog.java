package cn.mangot.wxcommon.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonLog {
	private final static Logger logger = LoggerFactory.getLogger(CommonLog.class);


	public static void debug(String debug) {
		logger.debug(debug);
	}
	
	public static void info(String info) {
		logger.info(info);
	}
	
	public static void error(String info) {
		logger.error(info);
	}
}
