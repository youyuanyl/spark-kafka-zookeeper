package com.gft.spark.common;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;

public class SessionListener implements HttpSessionListener {

	private Logger logger = Logger.getLogger(SessionListener.class);
	
	@Override
	public void sessionCreated(HttpSessionEvent session) {
		logger.info("创建session，id：" + session.getSession().getId());
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent session) {
		logger.info("销毁session，id：" + session.getSession().getId());
		
	}

}
