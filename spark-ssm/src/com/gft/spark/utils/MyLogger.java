package com.gft.spark.utils;

import org.apache.log4j.Logger;

public class MyLogger {

	public static Logger getLogger(Class<?> clazz){
		return Logger.getLogger(clazz);
	}
	
}
