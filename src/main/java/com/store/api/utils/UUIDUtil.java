package com.store.api.utils;

import java.util.UUID;

/**
 * UUID生成工具类
 * 
 */
public class UUIDUtil {
	/**
	 * 生成新的UUID
	 * 
	 * @return
	 */
	public static String getUUid() {
		UUID uuid = UUID.randomUUID();
		String uuidStr = uuid.toString();
		return uuidStr.replace("-", "").toUpperCase();
	}

	/**
	 * 获取uuid前10位
	 * 
	 * @return
	 */
	public static String getTenUUid() {
		String uuid = getUUid();
		return uuid.substring(0, 10);
	}

	/**
	 * 获取uuid前12位
	 * 
	 * @return
	 */
	public static String get12UUid() {
		String uuid = getUUid();
		return uuid.substring(0, 12);
	}

	/**
	 * 取16位的券号：12位UUID+时间戳后4位
	 * 
	 * @return
	 */
	public static String get16TicketNo() {
		String currentTime = System.currentTimeMillis() + "";
		String uuid = get12UUid()
				+ currentTime.substring(currentTime.length() - 4,
						currentTime.length());
		return uuid;
	}
	public static void main(String[] args) {
		System.out.println(get16TicketNo());
	}
}
