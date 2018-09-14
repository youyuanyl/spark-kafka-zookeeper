package com.gft.spark.common;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;

import com.gft.spark.utils.SparkUtils;

public class ReceiveMsgThread extends Thread {
	
	private WebApplicationContext web;
	
	@Override
	public void run() {
		SparkUtils.receiveAndSaveData(web);
	}
	
	public void setValue(WebApplicationContext web){
		this.web = web;
	}
	
}
