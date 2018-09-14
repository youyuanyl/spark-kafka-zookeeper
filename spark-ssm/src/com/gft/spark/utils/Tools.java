package com.gft.spark.utils;

import java.util.UUID;

public class Tools {

	/**
	 * 获取UUID
	 * 
	 * @return
	 */
	public static String getUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}
	
}
