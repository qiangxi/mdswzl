package com.lanma.lostandfound.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 该类的作用:String识别类
 * <p/>
 * 包含测试给定String是否是合法手机号,邮箱等 作者 :任强强 创建时间:2016/1/24
 */
public class StringUtils {

	/**
	 * 描述：判断一个字符串是否为null或空值.
	 *
	 * @param str
	 *            指定的字符串
	 * @return true or false
	 */
	public static boolean isEmpty(String str) {
		return str == null || str.trim().length() == 0;
	}

	/**
	 * 描述：获取指定长度的字符所在位置.
	 *
	 * @param str
	 *            指定的字符串
	 * @param maxL
	 *            要取到的长度（字符长度，中文字符计2个）
	 * @return 字符的所在位置
	 */
	public static int subStringLength(String str, int maxL) {
		int currentIndex = 0;
		int valueLength = 0;
		String chinese = "[\u0391-\uFFE5]";
		// 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
		for (int i = 0; i < str.length(); i++) {
			// 获取一个字符
			String temp = str.substring(i, i + 1);
			// 判断是否为中文字符
			if (temp.matches(chinese)) {
				// 中文字符长度为2
				valueLength += 2;
			} else {
				// 其他字符长度为1
				valueLength += 1;
			}
			if (valueLength >= maxL) {
				currentIndex = i;
				break;
			}
		}
		return currentIndex;
	}

	/**
	 * 描述：手机号格式验证.
	 *
	 * @param str
	 *            指定的手机号码字符串
	 * @return 是否为手机号码格式:是为true，否则false
	 */
	public static Boolean isMobileNo(String str) {
		Boolean isMobileNo = false;
		try {
			Pattern p = Pattern.compile("^1[3|4|5|7|8][0-9]\\d{4,8}$");
			Matcher m = p.matcher(str);
			isMobileNo = m.matches();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isMobileNo;
	}

	/**
	 * 描述：是否只是字母和数字.
	 *
	 * @param str
	 *            指定的字符串
	 * @return 是否只是字母和数字:是为true，否则false
	 */
	public static Boolean isNumberLetter(String str) {
		Boolean isNoLetter = false;
		String expr = "^[A-Za-z0-9]+$";
		if (str.matches(expr)) {
			isNoLetter = true;
		}
		return isNoLetter;
	}

	/**
	 * 描述：是否只是数字.
	 *
	 * @param str
	 *            指定的字符串
	 * @return 是否只是数字:是为true，否则false
	 */
	public static Boolean isNumber(String str) {
		Boolean isNumber = false;
		String expr = "^[0-9]+$";
		if (str.matches(expr)) {
			isNumber = true;
		}
		return isNumber;
	}

	/**
	 * 描述：是否是邮箱.
	 *
	 * @param str
	 *            指定的字符串
	 * @return 是否是邮箱:是为true，否则false
	 */
	public static Boolean isEmail(String str) {
		Boolean isEmail = false;
		String expr = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		if (str.matches(expr)) {
			isEmail = true;
		}
		return isEmail;
	}

	/**
	 * 描述：是否是中文.
	 *
	 * @param str
	 *            指定的字符串
	 * @return 是否是中文:是为true，否则false
	 */
	public static Boolean isChinese(String str) {
		Boolean isChinese = true;
		String chinese = "^([\u0391-\uFFE5]|A-Za-z)";
		if (!isEmpty(str)) {
			// 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
			for (int i = 0; i < str.length(); i++) {
				// 获取一个字符
				String temp = str.substring(i, i + 1);
				// 判断是否为中文字符
				if (temp.matches(chinese)) {
				} else {
					isChinese = false;
				}
			}
		}
		return isChinese;
	}

	/**
	 * 判断指定字符串是否是中文或字母,且长度为10以内
	 * 
	 * @param str
	 *            指定字符串
	 * @return
	 */
	public static Boolean isChineseAndNumber(String str) {
		Boolean isChineseAndNumber = false;
		String chinese = "[\u0391-\uFFE5\\a-zA-Z]{0,10}";
		if (!isEmpty(str)) {
			isChineseAndNumber = str.matches(chinese);
		}
		return isChineseAndNumber;
	}

	/**
	 * 描述：是否包含中文.
	 *
	 * @param str
	 *            指定的字符串
	 * @return 是否包含中文:是为true，否则false
	 */
	public static Boolean isContainChinese(String str) {
		Boolean isChinese = false;
		String chinese = "[\u0391-\uFFE5]";
		if (!isEmpty(str)) {
			// 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
			for (int i = 0; i < str.length(); i++) {
				// 获取一个字符
				String temp = str.substring(i, i + 1);
				// 判断是否为中文字符
				if (temp.matches(chinese)) {
					isChinese = true;
				} else {

				}
			}
		}
		return isChinese;
	}

	/**
	 * 描述：从输入流中获得String.
	 *
	 * @param is
	 *            输入流
	 * @return 获得的String
	 */
	public static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line).append("\n");
			}

			// 最后一个\n删除
			if (sb.indexOf("\n") != -1 && sb.lastIndexOf("\n") == sb.length() - 1) {
				sb.delete(sb.lastIndexOf("\n"), sb.lastIndexOf("\n") + 1);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

}
