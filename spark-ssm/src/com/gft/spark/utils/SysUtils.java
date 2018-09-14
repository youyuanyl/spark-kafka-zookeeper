package com.gft.spark.utils;

import java.util.UUID;

public class SysUtils {

	
	public static String getUuid(){
		return UUID.randomUUID().toString().replace("-", "");
	}
	
	
}
