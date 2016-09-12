/*
 * Copyright (C) 2012 www.amsoft.cn
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lanma.lostandfound.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 该类的作用:日期相关类 可以获取当前日期,把String转为日期等
 * <p/>
 * 作者 :任强强 创建时间:2016/1/24
 */

public class DateUtils {

	/**
	 * 时间日期格式化到年月日时分秒.
	 */
	public static final String dateFormatYMDHMS = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 时间日期格式化到年月日.
	 */
	public static final String dateFormatYMD = "yyyy-MM-dd";

	/**
	 * 时间日期格式化到年月.
	 */
	public static final String dateFormatYM = "yyyy-MM";

	/**
	 * 时间日期格式化到年月日时分.
	 */
	public static final String dateFormatYMDHM = "yyyy-MM-dd HH:mm";

	/**
	 * 时间日期格式化到月日.
	 */
	public static final String dateFormatMD = "MM/dd";

	/**
	 * 时分秒.
	 */
	public static final String dateFormatHMS = "HH:mm:ss";

	/**
	 * 时分.
	 */
	public static final String dateFormatHM = "HH:mm";

	/**
	 * 上午.
	 */
	public static final String AM = "AM";

	/**
	 * 下午.
	 */
	public static final String PM = "PM";

	/**
	 * 描述：String类型的日期时间转化为Date类型.
	 *
	 * @param strDate
	 *            String形式的日期时间
	 * @param format
	 *            格式化字符串，如："yyyy-MM-dd HH:mm:ss"
	 * @return Date Date类型日期时间
	 */
	public static Date getDateByFormat(String strDate, String format) {
		SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
		Date date = null;
		try {
			date = mSimpleDateFormat.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 描述：Date类型转化为String类型.
	 *
	 * @param date
	 *            the date
	 * @param format
	 *            the format
	 * @return String String类型日期时间
	 */
	public static String getStringByFormat(Date date, String format) {
		SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
		String strDate = null;
		try {
			strDate = mSimpleDateFormat.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strDate;
	}

	/**
	 * 描述：获取milliseconds表示的日期时间的字符串.
	 *
	 * @param milliseconds
	 *            the milliseconds
	 * @param format
	 *            格式化字符串，如："yyyy-MM-dd HH:mm:ss"
	 * @return String 日期时间字符串
	 */
	public static String getStringByFormat(long milliseconds, String format) {
		String thisDateTime = null;
		try {
			SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
			thisDateTime = mSimpleDateFormat.format(milliseconds);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return thisDateTime;
	}

	/**
	 * 描述：获取表示当前日期时间的字符串.
	 *
	 * @param format
	 *            格式化字符串，如："yyyy-MM-dd HH:mm:ss"
	 * @return String String类型的当前日期时间
	 */
	public static String getCurrentDate(String format) {
		String curDateTime = null;
		try {
			SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(DateUtils.dateFormatYMD);
			Calendar c = new GregorianCalendar();
			curDateTime = mSimpleDateFormat.format(c.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return curDateTime;

	}

	/**
	 * 描述：计算两个日期所差的天数.
	 *
	 * @param milliseconds1
	 *            the milliseconds1
	 * @param milliseconds2
	 *            the milliseconds2
	 * @return int 所差的天数
	 */
	public static int getOffsettDay(long milliseconds1, long milliseconds2) {
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTimeInMillis(milliseconds1);
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTimeInMillis(milliseconds2);
		// 先判断是否同年
		int y1 = calendar1.get(Calendar.YEAR);
		int y2 = calendar2.get(Calendar.YEAR);
		int d1 = calendar1.get(Calendar.DAY_OF_YEAR);
		int d2 = calendar2.get(Calendar.DAY_OF_YEAR);
		int maxDays = 0;
		int day = 0;
		if (y1 - y2 > 0) {
			maxDays = calendar2.getActualMaximum(Calendar.DAY_OF_YEAR);
			day = d1 - d2 + maxDays;
		} else if (y1 - y2 < 0) {
			maxDays = calendar1.getActualMaximum(Calendar.DAY_OF_YEAR);
			day = d1 - d2 - maxDays;
		} else {
			day = d1 - d2;
		}
		return day;
	}

	/**
	 * 描述：计算两个日期所差的小时数.
	 *
	 * @param date1
	 *            第一个时间的毫秒表示
	 * @param date2
	 *            第二个时间的毫秒表示
	 * @return int 所差的小时数
	 */
	public static int getOffsetHour(long date1, long date2) {
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTimeInMillis(date1);
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTimeInMillis(date2);
		int h1 = calendar1.get(Calendar.HOUR_OF_DAY);
		int h2 = calendar2.get(Calendar.HOUR_OF_DAY);
		int h = 0;
		int day = getOffsettDay(date1, date2);
		h = h1 - h2 + day * 24;
		return h;
	}

	/**
	 * 描述：计算两个日期所差的分钟数.
	 *
	 * @param date1
	 *            第一个时间的毫秒表示
	 * @param date2
	 *            第二个时间的毫秒表示
	 * @return int 所差的分钟数
	 */
	public static int getOffectMinutes(long date1, long date2) {
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTimeInMillis(date1);
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTimeInMillis(date2);
		int m1 = calendar1.get(Calendar.MINUTE);
		int m2 = calendar2.get(Calendar.MINUTE);
		int h = getOffsetHour(date1, date2);
		int m = 0;
		m = m1 - m2 + h * 60;
		return m;
	}

	/**
	 * 描述：获取本周一.
	 *
	 * @param format
	 *            格式化字符串，如："yyyy-MM-dd HH:mm:ss"
	 * @return String String类型日期时间
	 */
	public static String getFirstDayOfWeek(String format) {
		return getDayOfWeek(format, Calendar.MONDAY);
	}

	/**
	 * 描述：获取本周日.
	 *
	 * @param format
	 *            格式化字符串，如："yyyy-MM-dd HH:mm:ss"
	 * @return String String类型日期时间
	 */
	public static String getLastDayOfWeek(String format) {
		return getDayOfWeek(format, Calendar.SUNDAY);
	}

	/**
	 * 描述：获取本周的某一天.
	 *
	 * @param format
	 *            格式化字符串，如："yyyy-MM-dd HH:mm:ss"
	 * @param calendarField
	 *            指定本周的某一天
	 * @return String String类型日期时间
	 */
	private static String getDayOfWeek(String format, int calendarField) {
		String strDate = null;
		try {
			Calendar c = new GregorianCalendar();
			SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
			int week = c.get(Calendar.DAY_OF_WEEK);
			if (week == calendarField) {
				strDate = mSimpleDateFormat.format(c.getTime());
			} else {
				int offectDay = calendarField - week;
				if (calendarField == Calendar.SUNDAY) {
					offectDay = 7 - Math.abs(offectDay);
				}
				c.add(Calendar.DATE, offectDay);
				strDate = mSimpleDateFormat.format(c.getTime());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strDate;
	}

	/**
	 * 描述：获取本月第一天.
	 *
	 * @param format
	 *            格式化字符串，如："yyyy-MM-dd HH:mm:ss"
	 * @return String String类型日期时间
	 */
	public static String getFirstDayOfMonth(String format) {
		String strDate = null;
		try {
			Calendar c = new GregorianCalendar();
			SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
			// 当前月的第一天
			c.set(GregorianCalendar.DAY_OF_MONTH, 1);
			strDate = mSimpleDateFormat.format(c.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strDate;

	}

	/**
	 * 描述：获取本月最后一天.
	 *
	 * @param format
	 *            格式化字符串，如："yyyy-MM-dd HH:mm:ss"
	 * @return String String类型日期时间
	 */
	public static String getLastDayOfMonth(String format) {
		String strDate = null;
		try {
			Calendar c = new GregorianCalendar();
			SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
			// 当前月的最后一天
			c.set(Calendar.DATE, 1);
			c.roll(Calendar.DATE, -1);
			strDate = mSimpleDateFormat.format(c.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strDate;
	}

	/**
	 * 描述：获取表示当前日期的0点时间毫秒数.
	 *
	 * @return 当前日期的0点时间毫秒数
	 */
	public static long getFirstTimeOfDay() {
		Date date = null;
		try {
			String currentDate = getCurrentDate(dateFormatYMD);
			date = getDateByFormat(currentDate + " 00:00:00", dateFormatYMDHMS);
			return date.getTime();
		} catch (Exception ignored) {
		}
		return -1;
	}

	/**
	 * 描述：获取表示当前日期24点时间毫秒数.
	 *
	 * @return 当前日期24点时间毫秒数
	 */
	public static long getLastTimeOfDay() {
		Date date = null;
		try {
			String currentDate = getCurrentDate(dateFormatYMD);
			date = getDateByFormat(currentDate + " 24:00:00", dateFormatYMDHMS);
			return date.getTime();
		} catch (Exception ignored) {
		}
		return -1;
	}

	/**
	 * 描述：判断是否是闰年()
	 * <p>
	 * (year能被4整除 并且 不能被100整除) 或者 year能被400整除,则该年为闰年.
	 *
	 * @param year
	 *            年代（如2012）
	 * @return boolean 是否为闰年
	 */
	public static boolean isLeapYear(int year) {
		return (year % 4 == 0 && year % 400 != 0) || year % 400 == 0;
	}

	/**
	 * 取指定日期为星期几.
	 *
	 * @param strDate
	 *            指定的日期
	 * @param inFormat
	 *            格式化字符串，如："yyyy-MM-dd HH:mm:ss"
	 * @return String 星期几
	 */
	public static String getWeekNumber(String strDate, String inFormat) {
		String week = "星期日";
		Calendar calendar = new GregorianCalendar();
		DateFormat df = new SimpleDateFormat(inFormat);
		try {
			calendar.setTime(df.parse(strDate));
		} catch (Exception e) {
			return "错误";
		}
		int intTemp = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		switch (intTemp) {
		case 0:
			week = "星期日";
			break;
		case 1:
			week = "星期一";
			break;
		case 2:
			week = "星期二";
			break;
		case 3:
			week = "星期三";
			break;
		case 4:
			week = "星期四";
			break;
		case 5:
			week = "星期五";
			break;
		case 6:
			week = "星期六";
			break;
		}
		return week;
	}

	/**
	 * 根据给定的日期判断是否为上下午.
	 *
	 * @param strDate
	 *            给定的日期
	 * @param format
	 *            格式化字符串，如："yyyy-MM-dd HH:mm:ss"
	 * @return 否为上下午
	 */
	public static String getTimeQuantum(String strDate, String format) {
		Date mDate = getDateByFormat(strDate, format);
		int hour = mDate.getHours();
		if (hour >= 12)
			return "PM";
		else
			return "AM";
	}

	/**
	 * 根据给定的毫秒数算得时间的描述.
	 *
	 * @param milliseconds
	 *            毫秒数
	 * @return 根据给定的毫秒数算得时间的描述
	 */
	public static String getTimeDescription(long milliseconds) {
		if (milliseconds > 1000) {
			// 大于一分
			if (milliseconds / 1000 / 60 > 1) {
				long minute = milliseconds / 1000 / 60;
				long second = milliseconds / 1000 % 60;
				return minute + "分" + second + "秒";
			} else {
				// 显示秒
				return milliseconds / 1000 + "秒";
			}
		} else {
			return milliseconds + "毫秒";
		}
	}

	/**
	 * 比较两个日期间差距的秒数
	 * 
	 * @param date1
	 *            date1 不分先后
	 * @param date2
	 *            date2 不分先后
	 * @return
	 */
	public static int getOffsetSecondWithDate(Date date1, Date date2) {
		long millOffset = Math.abs(date1.getTime() - date2.getTime());
		return (int) (millOffset / 1000);
	}
}
