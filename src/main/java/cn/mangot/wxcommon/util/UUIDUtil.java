package cn.mangot.wxcommon.util;

import java.util.UUID;

/*
 Copyright:   Copyright (c)
 Company:     UUME Ico
 @author:     Hugh
 @version:    1.0
 Modification History:
 Date	   Author		Version		Description
 ------------------------------------------------------------------
 2006-08-24 	Hugh		1.0		Initialize Version.
 */
public class UUIDUtil {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		for(int i =0 ;i< 1;i++){
			int a = UUIDUtil.createDigitUUID(8);
			if(String.valueOf(a).length()!=8){
				System.out.println(a);
			}
		}
	}

	/**
	 * 
	 * 
	 * @param length
	 *            char length
	 * @return a random value include number and char
	 */
	public static String createNewUUID(int length) {
		StringBuffer sb = new StringBuffer();
		for (int j = 0; j < length; j++) {
			char ch = (char) ((Math.random() * 10000) % 85);
			ch += 48;
			if (Character.isLetterOrDigit(ch))
				sb.append(ch);
			else
				j--;
		}
		return sb.toString();
	}

	/**
	 * 
	 * 
	 * @param length
	 *            char length
	 * @return a random value include number and char
	 */
	public static String createNewUUID() {
		StringBuffer sb = new StringBuffer();
		for (int j = 0; j < 10; j++) {
			char ch = (char) ((Math.random() * 10000) % 85);
			ch += 48;
			if (Character.isLetterOrDigit(ch))
				sb.append(ch);
			else
				j--;
		}
		return sb.toString();
	}

	/**
	 * 生成唯一的真实UUID
	 * 
	 * @return
	 */
	public static String createRealUUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}
	
	/**
	 * 
	 * 
	 * @param length
	 *            char length
	 * @return a random value include number and char
	 */
	public static int createDigitUUID(int length) {
		StringBuffer sb = new StringBuffer();
		for (int j = 0; j < length; j++) {
			char ch = (char) ((Math.random() * 10000) % 85);
			ch += 48;
			if (Character.isDigit(ch)){
				sb.append(ch);
			}
			else {
				j--;
			}
		}
		return Integer.valueOf(sb.toString());
	}
	
}
