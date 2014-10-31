package com.store.api.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

public class TimeUtil {

	private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 时间转换成String
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static String getDateString(Date date, String format) {
		String dateTime = "";
		java.text.DateFormat dateFormat = new java.text.SimpleDateFormat(format);
		dateTime = dateFormat.format(date);
		return dateTime;
	}

	/**
	 * 时间String 换成Date
	 * 
	 * @return
	 * @throws ParseException
	 */
	public static Date getStringToDate(String dateStr) throws ParseException {
		java.text.DateFormat dateFormat = new java.text.SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		Date d = dateFormat.parse(dateStr);
		return d;
	}

	public static Date getStringToDate(String dateStr, int type)
			throws ParseException {
		java.text.DateFormat dateFormat = null;
		if (type == 1) {
			dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		} else if (type == 2) {
			dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
		}

		Date d = dateFormat.parse(dateStr);
		return d;
	}
	
	public static Long getLongByDate(Date date, int type){
		java.text.DateFormat dateFormat = null;
		if (type == 1) {
			dateFormat = new java.text.SimpleDateFormat("yyyyMM-ddHHmmss");
		} else if (type == 2) {
			dateFormat = new java.text.SimpleDateFormat("yyyyMMdd");
		}
		String strDate = dateFormat.format(date);
		return Long.parseLong(strDate);
	}
	
	/**
	 * 获取昨天的日期,时间格式为yyyyMMdd
	 */
	public static String getCurrentday() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 0);
		return new SimpleDateFormat("yyyyMMdd").format(cal.getTime());
	}

	/**
	 * 获取昨天的日期
	 */
	public static String getYesterday() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		return df.format(cal.getTime());
	}

	/**
	 * 获取上周一时间
	 */
	public static String getLastWeekMonday() {
		Date a = DateUtils.addDays(new Date(), -1);
		Calendar cal = Calendar.getInstance();
		cal.setTime(a);
		cal.add(Calendar.WEEK_OF_YEAR, -1);// 一周
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return df.format(cal.getTime());
	}

	/**
	 * 获取上周天时间
	 */
	public static String getLastWeekSunday() {
		Date a = DateUtils.addDays(new Date(), -1);
		Calendar cal = Calendar.getInstance();
		cal.setTime(a);
		cal.set(Calendar.DAY_OF_WEEK, 1);
		return df.format(cal.getTime());
	}

	/**
	 * 获取上月最后一天
	 */
	public static String getLastMonthLast() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 0);// 设置为1号,当前日期既为本月第一天
		return df.format(cal.getTime());
	}

	/**
	 * 获取上月第一天
	 */
	public static String getLastMonthFirst() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		cal.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
		return df.format(cal.getTime());
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
	  * 得到本周周一
	  * 
	  * @return yyyy-MM-dd
	 * @throws ParseException 
	  */
	 public static Date getMondayOfThisWeek() throws ParseException {
	  Calendar c = Calendar.getInstance();
	  int dayofweek = c.get(Calendar.DAY_OF_WEEK) - 1;
	  if (dayofweek == 0)
	   dayofweek = 7;
	  c.add(Calendar.DATE, -dayofweek + 1);
	  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	  return getStringToDate(sdf.format(c.getTime()),2);
	 }

	/**
	  *得到本周周日 
	  * @return yyyy-MM-dd
	 * @throws ParseException 
	  */
	 public static Date getSundayOfThisWeek() throws ParseException {
	  Calendar c = Calendar.getInstance();
	  int dayofweek = c.get(Calendar.DAY_OF_WEEK) - 1;
	  if (dayofweek == 0)
	   dayofweek = 7;
	  c.add(Calendar.DATE, -dayofweek + 7);
	  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	  return getStringToDate(sdf.format(c.getTime()),2);
	 }
	
		public static void main(String[] args) throws ParseException {
//			System.out.println(getYesterday());
			System.out.println(TimeUtil.getStringToDate(TimeUtil.getDateString(new Date(), "yyyy-MM-dd"), 2));
			System.out.println(new Date());
			System.out.println(TimeUtil.getMondayOfThisWeek());
			System.out.println(getDateString(new Date(), "M月dd日"));
		}
}