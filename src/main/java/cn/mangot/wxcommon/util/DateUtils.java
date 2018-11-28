package cn.mangot.wxcommon.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @description:时间工具类
 * @author pengfei.he
 * @date 2011-6-23
 */
public class DateUtils {
	private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static DateFormat logDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

	private static SimpleDateFormat dayFormater = new SimpleDateFormat("yyyy-MM-dd");

	private static SimpleDateFormat timerOfDayFormater = new SimpleDateFormat("HH:mm:ss");
	//	一天的毫秒数
	public static final long ONE_DAY_MS = 24 * 60 * 60 * 1000;
	
	public static void parserDateFormat( ) {
		
	}
	
	public static String getDefaultFormatByDate(Date date) {
		return dateFormat.format(date);
	}

	public static String getLogFormatByDate(Date date) {
		return logDateFormat.format(date);
	}
	/* 日志格式属性的getter方法 */
	public static DateFormat getLogDateFormat() {
		return logDateFormat;
	}

	public static String getDayFormat(Date date) {
		return dayFormater.format(date);
	}
	
	public static final Date TIMER_THREE_START_DATE;
	static{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime( new Date() );
		int day = calendar.get(Calendar.DAY_OF_YEAR);
		calendar.set(Calendar.DAY_OF_YEAR, day + 1);
		calendar.set(Calendar.HOUR_OF_DAY, 3);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		
		TIMER_THREE_START_DATE = calendar.getTime();
	}
	
	/*** 定时器启动的默认时间 为次日的凌晨 */
	public static final Date TIMER_DEFAULT_START_DATE;
	
	static {
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime( new Date() );
		int day = calendar.get(Calendar.DAY_OF_YEAR);
		calendar.set(Calendar.DAY_OF_YEAR, day + 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		
		TIMER_DEFAULT_START_DATE = calendar.getTime();
		
	}
	
	public static final Date TIMER_START_DATE_NOON;
	
	static {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime( new Date() );
		int day = calendar.get(Calendar.DAY_OF_YEAR);
		calendar.set(Calendar.DAY_OF_YEAR, day + 1);
		calendar.set(Calendar.HOUR_OF_DAY, 12);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		
		TIMER_START_DATE_NOON = calendar.getTime();
	}
	
	public static Date getTomorrowZeroHour() {
		Calendar calendar = Calendar.getInstance();
		int day = calendar.get(Calendar.DAY_OF_YEAR);
		
		calendar.set(Calendar.DAY_OF_YEAR, day + 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		
		return calendar.getTime();
	}
	
	public static Date getNextZeroHour() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR_OF_DAY, 1);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	public static Date getNoonHour() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR_OF_DAY, 12);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	
	/**
	 * 两个时间之间的天数
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static long getDays(String date1, String date2) {
		if (date1 == null || date1.equals(""))
			return 0;
		if (date2 == null || date2.equals(""))
			return 0;
		// 转换为标准时间
		SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date = null;
		java.util.Date mydate = null;
		try {
			date = myFormatter.parse(date1);
			mydate = myFormatter.parse(date2);
		} catch (Exception e) {
		}
		long day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
		return day;
	}

	public static long getDateTime( String date ) {
		
		long day = 0;
		
		try {
			day = dateFormat.parse(date).getTime();
		} catch ( Throwable e ) {
			
		}
		
		return day;
	}
	
	public static Date getDefaultDate( String date ) throws ParseException {
		return dateFormat.parse(date);
	}
	
	/**
	 * 返回第二天的时间 yyyy-MM-dd 00:00:00
	 * 
	 * @return
	 */
	public static Date getTomorrow() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_YEAR, 1);
		Date tomorrow = c.getTime();
		String t = dayFormater.format(tomorrow);
		try {
			tomorrow = dayFormater.parse(t);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return tomorrow;
	}
	
	/**
	 * 
	 * @Description 返回第二天早点6点的时间 yyyy-MM-dd 06:00:00
	 * @author linanjun
	 * @date 2012-12-26 下午02:46:53 
	 * @return Date
	 */
	public static Date getTomorrowSixHours(){
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_YEAR, 1);
		c.add(Calendar.HOUR_OF_DAY, 6);
		Date date = c.getTime();
		String t = dayFormater.format(date);
		try {
			date = dayFormater.parse(t);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 获取几天前或者几天后的时间，具体为：YYYY-mm-dd 00:00:00
	 * 
	 * @param days
	 * 
	 * @return
	 */
	public static Calendar getDateTimeByDay(int days) {
		Calendar _date = Calendar.getInstance();
		_date.set(Calendar.AM_PM, Calendar.AM);
		_date.set(Calendar.HOUR, 0);
		_date.set(Calendar.MINUTE, 0);
		_date.set(Calendar.SECOND, 0);
		_date.add(Calendar.DAY_OF_YEAR, days);
		return _date;
	}
	
	/**
	 * 
	 * @Description 获取N个小时之后的时间
	 * @author linanjun
	 * @date 2013-3-20 下午05:25:20 
	 * @param days
	 * @param hour
	 * @return Calendar
	 */
	public static Calendar getDateTimeByNextHour(long days, int hour) {
		Calendar _date = Calendar.getInstance();
		_date.setTimeInMillis(days);
		_date.add(Calendar.HOUR_OF_DAY, hour);
		return _date;
	}
		
	public static Date getDayOfWeek( Date date, int dayOfWeek ) {
		Calendar calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		//	
		calendar.setTime(date);
		//	设置到本周几
		calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
		
		if( calendar.getTime().getTime() >= date.getTime() ) {
			//	是之后的直接返回
			return calendar.getTime();
		}
		
		calendar.add(Calendar.WEEK_OF_MONTH, 1);	
		calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
		
		Date nowDate = calendar.getTime();
		
		return nowDate;
	}
		
	/**
	 * 
	 * @Description 获取几天前或者几天后的时间，具体为：YYYY-mm-dd 00:00:00
	 * @author linanjun
	 * @date 2012-10-23 下午12:06:10 
	 * @param oldTime
	 * @param keepDay
	 * @return String
	 */
	public static String getDateTimeByDay(long oldTime, int keepHour){
		Calendar _date = Calendar.getInstance();
		_date.setTimeInMillis(oldTime);
		/** 取出旧的时间在当天的哪一小时*/
		int day_hour = _date.get(Calendar.HOUR_OF_DAY);
		/** 计算新的时间*/
		_date.set(Calendar.HOUR_OF_DAY, day_hour + keepHour);
		return getDefaultFormatByDate(_date.getTime());
	}
	
	/**
	 * @Description Long型数据转化为时间类型的方法
	 * @author linanjun
	 * @date 2012-4-24 下午4:36:45
	 * @param l
	 * @return
	 * @throws ParseException
	 *             Date
	 */
	public static Date longToDate(Long l) throws ParseException {
		Date date = new Date();
		date.setTime(l);
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd EEE a hh:mm:ss ");
		date = strChangeToDate(formater.format(date));
		return date;
	}
	
	/**
	 * @Description 由字符串（指定）格式（yyyy-MM-dd HH:mm:SS）转化成Date
	 * @author linanjun
	 * @date 2012-4-24 下午4:35:36
	 * @param str
	 * @return Date
	 */
	public static Date strChangeToDate(String str) {
		
		Date date = null;
		try {
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
			date = sd.parse(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}
	
	/**
	 * 
	 * @Description 将date转换为毫秒
	 * @author linanjun
	 * @date 2013-1-10 下午04:06:44 
	 * @param date
	 * @return long
	 */
	public static long dateChangeToLong(Date date){
		Calendar _date = Calendar.getInstance();
		_date.setTime(date);
		return _date.getTimeInMillis();
	}

	public static Date getTodayLastTime() {
		return null;
	}
	
	public static boolean isYesterday( long lastRequestRewardTime ) {
		
		if( 0 == lastRequestRewardTime ) {
			return false;
		}
		
		Calendar calendar1 = Calendar.getInstance();
		
		calendar1.setTime( new Date(lastRequestRewardTime) );

		Calendar calendar2 = Calendar.getInstance();
		
		calendar2.setTime( new Date() );
		
		calendar2.add( Calendar.DATE, -1 );
		
		if( calendar1.get(Calendar.YEAR) != calendar2.get(Calendar.YEAR) ) {
			return false;
		}
		
		if( calendar1.get(Calendar.MONTH) != calendar2.get(Calendar.MONTH) ) {
			return false;
		}
		
		if( calendar1.get(Calendar.DAY_OF_MONTH) != calendar2.get(Calendar.DAY_OF_MONTH) ) {
			return false;
		}
		
		return true;
	}
	
	public static boolean isToday( long lastRequestRewardTime ) {
		
		if( 0 == lastRequestRewardTime ) {
			return false;
		}
		
		Calendar calendar1 = Calendar.getInstance();
		
		calendar1.setTime( new Date(lastRequestRewardTime) );

		Calendar calendar2 = Calendar.getInstance();
		
		calendar2.setTime( new Date() );
		
		if( calendar1.get(Calendar.YEAR) != calendar2.get(Calendar.YEAR) ) {
			return false;
		}
		
		if( calendar1.get(Calendar.MONTH) != calendar2.get(Calendar.MONTH) ) {
			return false;
		}
		
		if( calendar1.get(Calendar.DAY_OF_MONTH) != calendar2.get(Calendar.DAY_OF_MONTH) ) {
			return false;
		}
		
		return true;
	}
	/**
	 * 
	 * @param date
	 * @return
	 */
//	public static long getLongDate(Date date) {
//		Calendar calendar = Calendar.getInstance();
//		
//		calendar.setTime(date);
//		
//		/************************************************************************
//		 *	+		+		+		+		+		+		+		+		+	*
//		 *	0		8		16		24		32		40		48		56		64	*
//		 *	+-------+-------+-------+-------+-------+-------+-------+-------+	*
//		 *	+	year		+month	+day	+hour	+minute +seconds+		+	*	
//		 *	+-------+-------+-------+-------+-------+-------+-------+-------+	*
//		 ************************************************************************/
//		
//		//	0xFFFFFFFFFFFFFFFFL
//		//	0x00FFFFFFFFFFFFFFL
//		//	0x0000FFFFFFFFFFFFL
//		//	0x000000FFFFFFFFFFL
//		//	0x00000000FFFFFFFFL
//		//	0x0000000000FFFFFFL
//		//	0x000000000000FFFFL
//		//	0x00000000000000FFL
//		
//		long year = calendar.get( Calendar.YEAR );
//		long month = calendar.get( Calendar.MONTH );
//		long day = calendar.get( Calendar.DAY_OF_MONTH );
//		long hour = calendar.get( Calendar.HOUR_OF_DAY );
//		long minute = calendar.get( Calendar.MINUTE );
//		long seconds = calendar.get( Calendar.SECOND );
//		
//		long result = 0;
//		
//		return result;
//	}
	
	public static String timeOfDayFormater( Date date ) {
		return timerOfDayFormater.format(date);
	}
	
	public static Date getMonthEnd() {	
		Calendar calendar = Calendar.getInstance();
		calendar.set( Calendar.DATE,  1 );
		calendar.roll(Calendar.DATE,  - 1 );
		
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		return calendar.getTime();
	}
	
	public static Date getNextMonthEnd() {
		Calendar calendar = Calendar.getInstance();
		
		calendar.roll(Calendar.MONTH, 1 );
		calendar.set( Calendar.DATE,  1 );
		calendar.roll(Calendar.DATE,  -1 );
		
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		
		return calendar.getTime();
	}

	
	
	/**
	 * 得到几天前的时间
	 * 
	 * @param d
	 * @param day
	 * @return
	 */
	public static Date getDateBefore(Date d, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
		return now.getTime();
	}
	/**
	 * 得到几天前的时间（重载）
	 * @param d 字符串类型的时间
	 * @param day
	 * @return String类型的时间
	 */
	public static String getDateBefore(String d, int day){
		return DateUtils.getDefaultFormatByDate(getDateBefore(strChangeToDate(d), day));
	}

	/**
	 * 得到几天后的时间
	 * 
	 * @param d
	 * @param day
	 * @return
	 */
	public static Date getDateAfter(Date d, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
		return now.getTime();
	}
	/**
	 * 得到几天后的时间（重载）
	 * @param d 字符串类型的时间
	 * @param day
	 * @return String类型的时间
	 */
	public static String getDateAfter(String d, int day){
		return DateUtils.getDefaultFormatByDate(getDateAfter(strChangeToDate(d), day));
	}
	/**
	 * @Description 获取date的零点正
	 * @author pingyang.li
	 * @date 2013-6-7 上午11:49:36 
	 * @param date
	 * @return long
	 */
	public static Date getZeroDate(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

	/**
	 * 获取当天时间的最后时间 23:59:59
	 * @param d
	 * @return
	 */
	public static Date getLastTimeByDay(Date d){
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.HOUR_OF_DAY, 23);
		now.set(Calendar.MINUTE, 59);
		now.set(Calendar.SECOND, 59);
		
		return now.getTime();
	}
	
	public static Date getWeekEnd() {
		Calendar calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek(Calendar.MONDAY); 
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 00);
		return calendar.getTime();
	}
	
	public static Date getNextWeekEnd() {
		Calendar calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.add(Calendar.WEEK_OF_YEAR, 1);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 00);
		return calendar.getTime();
	}
	
	public static boolean getWeekDay( Date date, int weekday ) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);	
		return calendar.get(Calendar.DAY_OF_WEEK) == weekday;
	}
	
	public static Date getMonthStart() {
		Calendar calendar = Calendar.getInstance();
		calendar.set( Calendar.DAY_OF_MONTH,  1 );
		
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}
	
	public static Date getNextMonthStart() {
		Calendar calendar = Calendar.getInstance();
		
		calendar.roll( Calendar.MONTH, 1 );
		calendar.set( Calendar.DAY_OF_MONTH, 1 );
		
		calendar.set( Calendar.HOUR_OF_DAY, 0 );
		calendar.set( Calendar.MINUTE, 0 );
		calendar.set( Calendar.SECOND, 0 );
		
		return calendar.getTime();
	}
	
	public static boolean isOneDay( long time1, long time2 ) {
		
		Calendar calendar = Calendar.getInstance();
		
		calendar.setTimeInMillis(time1);
		
		int year1 = calendar.get(Calendar.YEAR);
		int month1 = calendar.get(Calendar.MONTH);
		int day1 = calendar.get(Calendar.DAY_OF_YEAR);
		
		calendar.setTimeInMillis(time2);
		
		int year2 = calendar.get(Calendar.YEAR);
		int month2 = calendar.get(Calendar.MONTH);
		int day2 = calendar.get(Calendar.DAY_OF_YEAR);
		
		if( year1 == year2 && month1 == month2 && day1 == day2 ) {
			return true;
		}
		
		return false;
	}
	
	public static Date getTodayHHmmss( String HHmmss ) {
		
		String[] array = HHmmss.split(":");
		Calendar calendar = Calendar.getInstance();
			
		calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(array[0]));
		calendar.set(Calendar.MINUTE, Integer.valueOf(array[1]));
		calendar.set(Calendar.SECOND, Integer.valueOf(array[2]));
			
		return calendar.getTime();
		
	}
	
	public static Date getTodayHHmmss( Date date ) {
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		String str = "";
		
		str = str + calendar.get(Calendar.HOUR_OF_DAY) + ":";
		str = str + calendar.get(Calendar.MINUTE) + ":";
		str = str + calendar.get(Calendar.SECOND);
		
		return getTodayHHmmss( str );
		
	}
	
	/**
	 * 
	 * @Description 方法说明
	 * @author chen.su
	 * @date 2013-4-24 下午05:04:18 
	 * @param srcDate
	 * @param descDate
	 * @return Date
	 */
	public static Date copyHHmmss( Date srcDate, Date descDate ) {
		
		Calendar calendar = Calendar.getInstance();
		Calendar calendar1 = Calendar.getInstance();
		
		calendar.setTime(srcDate);
		calendar1.setTime(descDate);
		
		calendar.set(Calendar.HOUR_OF_DAY, calendar1.get(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, calendar1.get(Calendar.MINUTE));
		calendar.set(Calendar.SECOND, calendar1.get(Calendar.SECOND));
		
		return calendar.getTime();
	}
	
	public static Date deleteMillisecond( Date date ) {
		return new Date((date.getTime() / 1000) * 1000);
	}
	
	/**
	 * 
	 * @Description 返回两天之间相差的天数, 并假设 time2 >= time1. 如果time2 < time1 则返回-1.
	 * 
	 * @author chen.su
	 * @date 2013-5-23 下午10:16:11 
	 * @param time1
	 * @param time2
	 * @return int
	 */
    public static int getdays(long time1, long time2) {
		if( time1 > time2 ) {
			return -1;
		}
		
		if( time1 == time2 || isOneDay(time1, time2)) {
			return 0;
		}
		
		int days = (int)(( time2 - time1 ) / (long)(24 * 60 * 60 * 1000 ));
		
		if( ( time2 - time1 ) % (long)(24 * 60 * 60 * 1000 ) > 1 ) {
			days = days + 1;
		}
        return days;
    }
    
    public static Date getNextMoonDate() {
    	Calendar calendar = Calendar.getInstance();
    	calendar.add(Calendar.DAY_OF_YEAR, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 12);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
    }
    
	public static void main( String[] args ) {
//		System.out.println(getDefaultFormatByDate(getMonthEnd()));
//		System.out.println(getDefaultFormatByDate(getNextMonthEnd()));
//		System.out.println(getDefaultFormatByDate(getMonthStart()));
//		System.out.println(getDefaultFormatByDate(getNextMonthStart()));
//		getTodayHHmmss( "00:55:01");
//											  1369043853000
//		System.out.println(deleteMillisecond(new Date(1369043853920L)).getTime());
//		
//		System.out.println(
//				getdays(strChangeToDate("1920-02-02 23:59:59").getTime(), strChangeToDate("1920-02-03 00:00:00").getTime())		
//		);
//		
//		System.out.println(
//				getDefaultFormatByDate(getNextMoonDate())
//		);
		
		System.out.println(getWeekDay(new Date(), Calendar.WEDNESDAY));
	}
	
}
