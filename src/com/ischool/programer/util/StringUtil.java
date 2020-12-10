package com.ischool.programer.util;
/**
 * 
 * @author win
 *String类的一些共用操作方法
 */
public class StringUtil {
	public static boolean isEmpty(String str) {
		if(str == null || "".equals(str)) return true;
		return false;
	}
}
