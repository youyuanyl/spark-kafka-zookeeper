package com.gft.spark.common;


import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.gft.spark.utils.SparkUtils;


public class SendMessageServ2 extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		System.out.println("接收数据servlet初始化开始！");
		ServletContext sc = config.getServletContext();
		WebApplicationContext web = WebApplicationContextUtils.getWebApplicationContext(sc);
		
		ReceiveMsgThread thread = new ReceiveMsgThread();
		thread.setValue(web);
		thread.start();
		System.out.println("接收数据servlet初始化结束！");
	}
	
}
