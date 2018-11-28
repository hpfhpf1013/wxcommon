package cn.mangot.wxcommon.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StringUtils {

	private StringUtils() {

	}

	/**
	 * 判断字符串是否为空
	 * 
	 * @param src
	 * @return
	 */
	public static boolean isEmpty(String src) {
		if (null == src) {
			return true;
		}

		if ("".equals(src.trim())) {
			return true;
		}

		return false;
	}

	/**
	 * 判断字符串是否为数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		if (StringUtils.isEmpty(str)) {
			return false;
		}
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}
	
	public static int str2Int(String str) {
		if(!isEmpty(str)&&isNumeric(str)) {
			return Integer.parseInt(str);
		}else {
			return 0;
		}
	}

	/**
	 * 将中文转化为Unicode字符 FaceBook只接收这样的JSON串，必须调此方法转化，法克！！！
	 * 
	 * @param str
	 * @return
	 */
	public static String toUnicode(String str) {
		StringBuffer _str = new StringBuffer();
		char[] _charArray = str.toCharArray();
		String _prefix = "";
		for (char c : _charArray) {
			if (c < 256) {
				_prefix = "\\u00";
				_str.append(c);
			} else if (c < 4096) {
				_prefix = "\\u0";
				_str.append(c);
			} else {
				_prefix = "\\u";
				_str.append(_prefix + Integer.toHexString(c & 0xffff));
			}
		}
		return _str.toString();
	}

	public static boolean strLengthLimit(String str, int min, int max) {
		if (str == null) {
			return false;
		}

		int length = str.length();
		boolean result = max >= length && length >= min;
		return result;
	}

	public static int compareLong(long a, long b) {
		long c = b - a;
		if (c > 0) {
			return 1;
		} else if (c < 0) {
			return -1;
		} else {
			return 0;
		}
	}

	/**
	 * 
	 * @Description 去特殊字符
	 * @author LNJ
	 * @date 2013-4-28 上午12:12:23
	 * @param str
	 * @return String
	 */
	public static String replaceBlank(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}

	/**
	 * 
	 * @Description 检查指定字符是否存在
	 * @author LNJ
	 * @date 2013-6-3 下午06:21:30
	 * @param str
	 * @param checkStr
	 * @return boolean
	 */
	public static boolean isIndexOfStr(String str, String checkStr) {
		boolean checkResult = false;
		if (str != null) {
			int result = str.indexOf(checkStr);
			checkResult = result >= 0 ? true : false;
		}
		return checkResult;
	}

	/**
	 * @Description
	 * @author chen.su
	 * @date 2013-6-17 上午11:40:49
	 * @param str
	 * @param date
	 * @return String
	 */
	public static String getLogFile(String str, Date date) {
		return str + "_" + DateUtils.getDayFormat(date);
	}

	/**
	 * @Description
	 * @author chen.su
	 * @date 2013-6-17 上午11:40:53
	 * @param str
	 * @param date
	 * @return String
	 */
	public static String getLogFile(String str, long date) {
		return "/data/log/" + str + "_"
				+ DateUtils.getDayFormat(new Date(date)) + ".log";
	}

	/**
	 * 
	 * @Description
	 * @author chen.su
	 * @date 2013-6-17 上午11:50:53
	 * @param head
	 * @param fileName
	 * @return List<String>
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	public static List<String> readStartWith(String head, String fileName)
			throws UnsupportedEncodingException, IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(fileName), "UTF-8"));

		String line = null;
		List<String> list = new ArrayList<String>();

		while ((line = reader.readLine()) != null) {
			if (line.startsWith(head)) {
				list.add(line);
			}
		}

		return list;
	}

	/**
	 * 按照字母顺序排列并返回
	 * 
	 * @return
	 */
	public static String[] compareString(String[] list) {
		int len = list.length;
		String temp = null;
		for (int i = 0; i < len - 1; i++) {
			for (int j = 0; j < len - 1 - i; j++) {
				if ((list[j].compareTo(list[j + 1])) > 0) {
					temp = list[j];
					list[j] = list[j + 1];
					list[j + 1] = temp;
				}
			}
		}
		return list;
	}

	public static void main(String[] args) {

		String[] list = { "appId", "cpOrderId", "cpUserInfo", "uid", "orderId",
				"orderStatus", "payFee", "productCode", "productName",
				"productCount", "payTime" };
		compareString(list);
		for (String str : list) {
			System.out.println(str);
		}
	}
}
